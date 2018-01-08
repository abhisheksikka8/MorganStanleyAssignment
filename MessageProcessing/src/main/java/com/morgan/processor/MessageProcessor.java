package com.morgan.processor;

import static com.morgan.processor.cache.SalesCache.ADJUSTMENT_SALES;
import static com.morgan.processor.cache.SalesCache.SALE_RECORDS;
import static com.morgan.processor.cache.SalesCache.addAjustmentSale;
import static com.morgan.processor.cache.SalesCache.addMultipleSale;
import static com.morgan.processor.cache.SalesCache.addSale;
import static com.morgan.processor.common.MessagePattern.TYPE_1;
import static com.morgan.processor.common.MessagePattern.TYPE_2;
import static com.morgan.processor.common.MessagePattern.TYPE_3;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

import com.morgan.processor.common.AdjustmentType;
import com.morgan.processor.model.Sale;

public class MessageProcessor {
	private String inputFilePath;
	private int messageCount = 1;
	
	private Matcher matcher;

	public MessageProcessor(String inputFilePath) {
		super();
		this.inputFilePath = inputFilePath;
	}

	public void processMessage() {
		List<String> saleRecordLines = null;

		try {
			saleRecordLines = Files.readAllLines(Paths.get(this.inputFilePath));

			for (int i = 0; i < saleRecordLines.size(); i++) {
				processLine(saleRecordLines.get(i));

				if (messageCount != 50 && messageCount % 10 == 0) {
					logInterimReport();
				}

				if (messageCount == 50) {

					System.out.println("Pausing !!!!");
					System.out.println("Logging Adjustments !!!!");
					
					doAdjustmentsAndLogReport();
					messageCount = 0;
				}
			}

		} catch (IOException e) {
			throw new IllegalArgumentException(String
					.format("IO Error. Invalid file path specified: [%s]"
							,this.inputFilePath));
		}
	}

	private void logInterimReport() {
		SALE_RECORDS.keySet().stream().forEach(product -> {
			int totVal = SALE_RECORDS.get(product).stream().mapToInt(sale -> sale.getValue()).sum();
			System.out.println("Product: " + product + " Num Sales : " 
					+ SALE_RECORDS.get(product).size() + " Total Value: " + totVal);
		});
		
		System.out.print("\n");
	}

	private void doAdjustmentsAndLogReport() {
		doAdjustmentsForMultiplyAdjustments();
		doAdjustmentsForAdditionAdjustments();
		doAdjustmentsForSubtractionAdjustments();
		
		logInterimReport();
	}

	private void doAdjustmentsForAdditionAdjustments() {
		Map<String, List< Sale>> adjSales = ADJUSTMENT_SALES.get(AdjustmentType.ADD);
		adjSales.keySet().stream().forEach(prodType -> 
		{
			List<Sale> prodAdjSales = adjSales.get(prodType);
			prodAdjSales.stream().forEach(prodAdjSale -> 
			{
				//get the earlier sale & then do the adjustments
				List<Sale> earlierSales = SALE_RECORDS.get(prodAdjSale.getProduct());
				earlierSales.stream().forEach(earlierSale -> 
				{
					earlierSale.setValue(prodAdjSale.getValue() + earlierSale.getValue());
				});
			});
		});
	}

	private void doAdjustmentsForSubtractionAdjustments() {
		Map<String, List< Sale>> adjSales = ADJUSTMENT_SALES.get(AdjustmentType.SUBTRACT);
		adjSales.keySet().stream().forEach(prodType -> 
		{
			List<Sale> prodAdjSales = adjSales.get(prodType);
			prodAdjSales.stream().forEach(prodAdjSale -> 
			{
				//get the earlier sale & then do the adjustments
				List<Sale> earlierSales = SALE_RECORDS.get(prodAdjSale.getProduct());
				earlierSales.stream().forEach(earlierSale -> 
				{
					earlierSale.setValue(earlierSale.getValue() - prodAdjSale.getValue());
				});
			});
		});
	}

	private void doAdjustmentsForMultiplyAdjustments() {
		Map<String, List< Sale>> adjSales = ADJUSTMENT_SALES.get(AdjustmentType.MULTIPLY);
		
		if (adjSales != null && !adjSales.isEmpty()) {
			adjSales.keySet().stream().forEach(prodType -> 
			{
				List<Sale> prodAdjSales = adjSales.get(prodType);
				prodAdjSales.stream().forEach(prodAdjSale -> 
				{
					//get the earlier sale & then do the adjustments
					List<Sale> earlierSales = SALE_RECORDS.get(prodAdjSale.getProduct());
					earlierSales.stream().forEach(earlierSale -> 
					{
						earlierSale.setValue(earlierSale.getValue() * prodAdjSale.getValue());
					});
				});
			});
		} else {
			System.out.println("There are no adjustments to be made. Processing further records!!!");
		}
	}

	private void processLine(String line) {
		if (TYPE_1.matcher(line).matches()) {
			processTypeOneMesssage(line);
		} else if (TYPE_2.matcher(line).matches()) {
			processTypeTwoMesssage(line);
		} else if (TYPE_3.matcher(line).matches()) {
			processTypeThreeMesssage(line);
		} else {
			throw new IllegalArgumentException(String.format("The input sale specified in in invalid format. Line: [%s]. Quiting !!!", line));
		}

		this.messageCount++;
	}
	
	private void processTypeOneMesssage(String line) {

		matcher = TYPE_1.matcher(line);
		
		if (matcher.matches()) {
			Sale sale = new Sale(matcher.group(1), Integer.parseInt(matcher.group(2)));
			addSale(sale);
		}
	
	}
	
	private void processTypeTwoMesssage(String line) {
		matcher = TYPE_2.matcher(line);
		
		if (matcher.matches()) {
			int numSales = Integer.parseInt(matcher.group(1));
			Sale sale = new Sale(matcher.group(2), Integer.parseInt(matcher.group(3)));

			addMultipleSale(numSales, sale);
		}
	}
		
	private void processTypeThreeMesssage(String line) {

		matcher = TYPE_3.matcher(line);
		if (matcher.matches()) {
			String adjType = matcher.group(1);

			try {	
				AdjustmentType adjustmentType = AdjustmentType.valueOf(adjType.toUpperCase());
				Sale sale = new Sale(matcher.group(3), Integer.parseInt(matcher.group(2)));

				addAjustmentSale(adjustmentType, sale);
			} catch(Exception ex) {
				throw new IllegalArgumentException(String
						.format("Invalid Adjustment Type Specified. Adj Type:[%s], "
								+ "Line: [%s]", adjType, line));
			}
		}
	
	}
}
