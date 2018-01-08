package com.morgan.processor.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.morgan.processor.common.AdjustmentType;
import com.morgan.processor.model.Sale;

public final class SalesCache {
	public static Map<String, List<Sale>> SALE_RECORDS;
	//Adjustment sales having key as the adjustment type and corresponding map of sales of each product type
	public static Map<AdjustmentType, Map<String, List<Sale>>> ADJUSTMENT_SALES;
	
	static {
		SALE_RECORDS = new HashMap<>();
		ADJUSTMENT_SALES = new HashMap<>();
	}
	
	public static void addAjustmentSale(AdjustmentType adjustmentType, Sale sale) {
		if (adjustmentType == null || sale == null) {
			throw new IllegalArgumentException("Invalid Sale or Adjustment Type Specified");
		}
			
		Map<String, List<Sale>> productSalesMap =  ADJUSTMENT_SALES.get(adjustmentType);
		
		if (productSalesMap == null || productSalesMap.isEmpty()) {
			productSalesMap = new HashMap<>();
		}
		
		List<Sale> producstSales = productSalesMap.get(sale.getProduct());
		
		if (producstSales == null || producstSales.isEmpty()) {
			producstSales = new ArrayList<>();
		}
		
		producstSales.add(sale);
		productSalesMap.put(sale.getProduct(), producstSales);
		
		ADJUSTMENT_SALES.put(adjustmentType, productSalesMap);
	}
	
	public static void addSale(Sale sale) {
		if (sale == null) {
			throw new IllegalArgumentException("Invalid Sale Obtained. Terminating !!!");
		}
		
		//get the list of the sale for the particular product type
		List<Sale> productSales = SALE_RECORDS.get(sale.getProduct());
		
		if (productSales == null) {
			productSales = new ArrayList<>();
		}
		
		productSales.add(sale);
		
		SALE_RECORDS.put(sale.getProduct(), productSales);
	}
	
	public static void addMultipleSale(int count, Sale sale) {
		if (sale == null) {
			throw new IllegalArgumentException("Invalid Sale Obtained. Terminating !!!");
		}
		
		if (count == 0) {
			return;
		}

		for (int i = 0; i < count; i++) {
			addSale(sale);
		}		
	}
	
	//settle the amount
	public static void addAmountToSale(String product, int amount) {
		if (null == product || product.isEmpty()) {
			throw new IllegalArgumentException("Invalid Sale Obtained. Terminating !!!");
		}
		
		//get the list of the sale for the particular product type
		List<Sale> productSales = SALE_RECORDS.get(product);
		
		//add the amount only if the count of sales is non zero 
		if (productSales != null && !productSales.isEmpty()) {
			productSales.stream().forEach(sale -> {int newSale = sale
					.getValue() + amount; sale.setValue(newSale);});
		}
	}
}
