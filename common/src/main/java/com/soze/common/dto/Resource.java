package com.soze.common.dto;

public enum Resource {

	WOOD(4);

	private final int price;

	Resource(int price) {
		this.price = price;
	}

	public int getPrice() {
		return price;
	}
}
