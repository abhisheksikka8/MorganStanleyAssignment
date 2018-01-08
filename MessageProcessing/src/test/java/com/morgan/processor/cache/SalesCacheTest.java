package com.morgan.processor.cache;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.morgan.processor.common.AdjustmentType;
import com.morgan.processor.model.Sale;

public class SalesCacheTest {
	@Test
	public void testAddSaleForValidSale() {
		SalesCache.addSale(new Sale("apple", 20));
		assertEquals(1, SalesCache.SALE_RECORDS.get("apple").size());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testAddSaleForInvalidSale() {
		SalesCache.addSale(null);
	}
	
	@Test
	public void testAddForValidMutipleSale() {
		SalesCache.addMultipleSale(2, new Sale("peer", 2));
		
		assertEquals(1, SalesCache.SALE_RECORDS.size());
		assertEquals(2, SalesCache.SALE_RECORDS.get("peer").size());
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testAddForInValidMutipleSale() {
		SalesCache.addMultipleSale(0, null);
		
		//assertEquals(3, SalesCache.SALE_RECORDS.size());
	}
	
	@Test
	public void testAddAdjustMentSale() {
		SalesCache.addAjustmentSale(AdjustmentType.ADD, new Sale("apple", 2));
		
		assertEquals(1, SalesCache.ADJUSTMENT_SALES.get(AdjustmentType.ADD).get("apple").size());
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testAddAdjustMentSaleForInvalidAdjustment() {
		SalesCache.addAjustmentSale(null, null);
		//assertEquals(1, SalesCache.ADJUSTMENT_SALES.get("apple").size());
	}
	
	@Test
	public void testAddAmtToSale() {
		SalesCache.addSale(new Sale("apple", 20));
		SalesCache.addAmountToSale("apple", 20);
		
		System.out.println(SalesCache.SALE_RECORDS.get("apple").stream().mapToInt(sale -> sale.getValue()).sum());
		
		assertEquals(40, SalesCache.SALE_RECORDS.get("apple").stream().mapToInt(sale -> sale.getValue()).sum());
	}
}
