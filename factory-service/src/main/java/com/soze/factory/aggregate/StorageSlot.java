package com.soze.factory.aggregate;

public class StorageSlot {

	private int count;
	private int capacity;
	private int price;

	public StorageSlot() {

	}

	public StorageSlot(int count, int capacity, int price) {
		this.count = count;
		this.capacity = capacity;
		this.price = price;
	}

	public StorageSlot(StorageSlot slot) {
		this.count = slot.getCount();
		this.capacity = slot.getCapacity();
		this.price = slot.getPrice();
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}
}
