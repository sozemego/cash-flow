package com.soze.common.dto;

import java.beans.ConstructorProperties;

public class StorageSlotDTO {

	private final Resource resource;
	private final int count;
	private final int capacity;
	private final int price;

	@ConstructorProperties({"resource", "count", "capacity", "price"})
	public StorageSlotDTO(Resource resource, int count, int capacity, int price) {
		this.resource = resource;
		this.count = count;
		this.capacity = capacity;
		this.price = price;
	}

	public Resource getResource() {
		return resource;
	}

	public int getCount() {
		return count;
	}

	public int getCapacity() {
		return capacity;
	}

	public int getPrice() {
		return price;
	}

	@Override
	public String toString() {
		return "StorageSlotDTO{" + "resource=" + resource + ", count=" + count + ", capacity=" + capacity + ", price=" + price + '}';
	}
}
