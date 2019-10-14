package com.soze.common.dto;

import java.beans.ConstructorProperties;

public class ResourceDTO {

	private final String name;
	private final int minPrice;
	private final int maxPrice;

	public ResourceDTO(Resource resource) {
		this.name = resource.name();
		this.minPrice = resource.getMinPrice();
		this.maxPrice = resource.getMaxPrice();
	}

	@ConstructorProperties({"name", "minPrice", "maxPrice"})
	public ResourceDTO(String name, int minPrice, int maxPrice) {
		this.name = name;
		this.minPrice = minPrice;
		this.maxPrice = maxPrice;
	}

	public String getName() {
		return name;
	}

	public int getMinPrice() {
		return minPrice;
	}

	public int getMaxPrice() {
		return maxPrice;
	}

	@Override
	public String toString() {
		return "ResourceDTO{" + "name='" + name + '\'' + ", minPrice=" + minPrice + ", maxPrice=" + maxPrice + '}';
	}
}
