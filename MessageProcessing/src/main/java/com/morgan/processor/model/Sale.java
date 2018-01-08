package com.morgan.processor.model;

public class Sale {
	private String product;
	private Integer value;
	
	public String getProduct() {
		return product;
	}
	
	public Integer getValue() {
		return value;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

	public Sale(String product, Integer value) {
		super();
		this.product = product;
		this.value = value;
	}

	@Override
	public String toString() {
		return "Sale [product=" + product + ", value=" + value + "]";
	}
}
