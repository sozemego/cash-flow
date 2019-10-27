package com.soze.factory.aggregate;

import com.soze.common.dto.Resource;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class FactoryStorage {

	private final Map<Resource, StorageSlot> resources;

	public FactoryStorage(Map<Resource, StorageSlot> resources) {
		Objects.requireNonNull(this.resources = resources);
		validateSlots(this.resources);
		calculatePrices();
	}

	public void addResource(Resource resource) {
		addResource(resource, 1);
	}

	public void addResource(Resource resource, final int count) {
		if (!canFit(resource, count)) {
			return;
		}
		StorageSlot slot = getSlot(resource);
		slot.setCount(slot.getCount() + count);
		calculatePrices();
	}

	public void addResources(Map<Resource, Integer> resourceCounts) {
		if (!canFit(resourceCounts)) {
			return;
		}
		resourceCounts.forEach(this::addResource);
		calculatePrices();
	}

	public boolean canFit(Resource resource) {
		return canFit(resource, 1);
	}

	public boolean canFit(Resource resource, int count) {
		return getRemainingCapacity(resource) >= count;
	}

	public boolean canFit(Map<Resource, Integer> resourceCounts) {
		for (Map.Entry<Resource, Integer> resourceCount : resourceCounts.entrySet()) {
			if (!canFit(resourceCount.getKey(), resourceCount.getValue())) {
				return false;
			}
		}
		return true;
	}

	public void removeResource(Resource resource) {
		removeResource(resource, 1);
	}

	public void removeResource(Resource resource, final int count) {
		if (!hasResource(resource, count)) {
			return;
		}

		StorageSlot slot = getSlot(resource);
		slot.setCount(slot.getCount() - count);
		calculatePrices();
	}

	public void removeResources(Map<Resource, Integer> resourceCounts) {
		if (!hasResources(resourceCounts)) {
			return;
		}
		resourceCounts.forEach(this::removeResource);
		calculatePrices();
	}

	public boolean hasResource(Resource resource) {
		return hasResource(resource, 1);
	}

	public boolean hasResource(Resource resource, int count) {
		StorageSlot slot = resources.get(resource);
		return slot != null && slot.getCount() >= count;
	}

	public boolean hasResources(Map<Resource, Integer> resourceCounts) {
		for (Map.Entry<Resource, Integer> resourceCount : resourceCounts.entrySet()) {
			if (!hasResource(resourceCount.getKey(), resourceCount.getValue())) {
				return false;
			}
		}
		return true;
	}

	public int getRemainingCapacity(Resource resource) {
		int capacityTaken = getCapacityTaken(resource);
		int capacity = getCapacity(resource);
		return capacity - capacityTaken;
	}

	public int getCapacityTaken(Resource resource) {
		return getSlot(resource).getCount();
	}

	public int getCapacity(Resource resource) {
		return getSlot(resource).getCapacity();
	}

	void transferFrom(FactoryStorage otherStorage) {
		otherStorage.getResources().forEach((resource, slot) -> {
			int transferCount = Math.min(slot.getCount(), getRemainingCapacity(resource));
			addResource(resource, transferCount);
		});
		calculatePrices();
	}

	/**
	 * Checks if any of the capacities are below zero.
	 */
	private void validateSlots(Map<Resource, StorageSlot> resources) {
		resources.forEach((resource, slot) -> {
			if (slot.getCapacity() < 0) {
				throw new IllegalArgumentException("Capacity cannot be negative: " + resource + ":" + slot.getCapacity());
			}
			if (slot.getCount() < 0) {
				throw new IllegalArgumentException("Capacity cannot be negative: " + resource + ":" + slot.getCount());
			}
			if (slot.getPrice() < 0) {
				throw new IllegalArgumentException("Capacity cannot be negative: " + resource + ":" + slot.getPrice());
			}
		});
	}

	/**
	 * This method does not remove any resources. It simply removes resources that have 0 capacity or less.
	 */
	public void clean() {
		Map<Resource, StorageSlot> newResources = new HashMap<>();
		this.resources.forEach((resource, slot) -> {
			if (slot.getCapacity() > 0) {
				newResources.put(resource, slot);
			}
		});
		this.resources.clear();
		this.resources.putAll(newResources);
	}

	public FactoryStorage copy() {
		Map<Resource, StorageSlot> newResources = new HashMap<>();
		resources.forEach((resource, slot) -> {
			newResources.put(resource, new StorageSlot(slot));
		});
		return new FactoryStorage(newResources);
	}

	public void setCapacity(Resource resource, int capacity) {
		getSlot(resource).setCapacity(capacity);
		calculatePrices();
	}

	public void changeCapacities(Map<Resource, Integer> capacityChanges) {
		capacityChanges.forEach((resource, change) -> {
			StorageSlot slot = getSlot(resource);
			slot.setCapacity(slot.getCapacity() + change);
		});
		calculatePrices();
	}

	public Map<Resource, StorageSlot> getResources() {
		return resources;
	}

	public Map<Resource, Integer> getPrices() {
		Map<Resource, Integer> prices = new HashMap<>();
		resources.forEach((resource, slot) -> {
			prices.put(resource, slot.getPrice());
		});
		return prices;
	}

	public Map<Resource, Integer> getCapacities() {
		Map<Resource, Integer> capacities = new HashMap<>();
		resources.forEach((resource, slot) -> {
			capacities.put(resource, slot.getCapacity());
		});
		return capacities;
	}

	private StorageSlot getSlot(Resource resource) {
		return resources.compute(resource, (r, slot) -> {
			if (slot == null) {
				return new StorageSlot(0, 0, 0);
			}
			return slot;
		});
	}

	private void calculatePrices() {
		getResources().forEach((resource, slot) -> {
			if (slot.getCapacity() == 0) {
				return;
			}

			float percentTaken = slot.getCount() / (float) slot.getCapacity();
			float percentFree = 1f - percentTaken;
			float priceRange = resource.getMaxPrice() - resource.getMinPrice();
			float price = (float) resource.getMinPrice() + (priceRange * percentFree);
			slot.setPrice(Math.round(price));
		});
		clean();
	}

	@Override
	public String toString() {
		return "FactoryStorage{" + "resources=" + resources + '}';
	}
}
