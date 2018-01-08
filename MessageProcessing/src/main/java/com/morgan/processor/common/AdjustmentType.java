package com.morgan.processor.common;

public enum AdjustmentType {
	ADD("add"), SUBTRACT("subtract"), MULTIPLY("multiply");
	
	private String type;

	private AdjustmentType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}
}
