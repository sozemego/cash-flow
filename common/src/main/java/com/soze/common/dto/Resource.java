package com.soze.common.dto;

public enum Resource {

	WOOD(2, 6),
	STONE(3, 8)
	;

	private final int minPrice;
	private final int maxPrice;

	Resource(int minPrice, int maxPrice) {
		this.minPrice = minPrice;
		this.maxPrice = maxPrice;
	}

	public int getMinPrice() {
		return minPrice;
	}

	public int getMaxPrice() {
		return maxPrice;
	}
}
