package com.soze.factory.aggregate;

import com.soze.common.dto.Resource;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class FactoryStorage {

	private final Map<Resource, Integer> capacities;
	private final Map<Resource, Integer> resources = new HashMap<>();

	public FactoryStorage(Map<Resource, Integer> capacities) {
		Objects.requireNonNull(this.capacities = capacities);
		validateCapacities(this.capacities);
	}

	public void addResource(Resource resource) {
		addResource(resource, 1);
	}

	public void addResource(Resource resource, final int count) {
		if (!canFit(resource, count)) {
			return;
		}
		resources.compute(resource, (res, actualCount) -> {
			if (actualCount == null) {
				return count;
			}
			return actualCount + count;
		});
	}

	public boolean canFit(Resource resource) {
		return canFit(resource, 1);
	}

	public boolean canFit(Resource resource, int count) {
		return getRemainingCapacity(resource) >= count;
	}

	public void removeResource(Resource resource) {
		removeResource(resource, 1);
	}

	public void removeResource(Resource resource, final int count) {
		if (!hasResource(resource, count)) {
			return;
		}

		resources.compute(resource, (res, actualCount) -> {
			if (actualCount >= count) {
				return actualCount - count;
			}
			return count;
		});
	}

	public boolean hasResource(Resource resource) {
		return hasResource(resource, 1);
	}

	public boolean hasResource(Resource resource, int count) {
		return resources.getOrDefault(resource, 0) >= count;
	}

	public boolean isFull() {
		return capacities.equals(resources);
	}

	public Map<Resource, Integer> getResources() {
		return new HashMap<>(resources);
	}

	public Map<Resource, Integer> getCapacities() {
		return capacities;
	}

	public int getRemainingCapacity(Resource resource) {
		int capacityTaken = getCapacityTaken(resource);
		int capacity = getCapacity(resource);
		return capacity - capacityTaken;
	}

	public int getCapacityTaken(Resource resource) {
		return resources.getOrDefault(resource, 0);
	}

	public int getCapacity(Resource resource) {
		return capacities.getOrDefault(resource, 0);
	}

	void transferFrom(FactoryStorage otherStorage) {
		otherStorage.getResources().forEach((resource, count) -> {
			int transferCount = Math.min(count, getRemainingCapacity(resource));
			addResource(resource, transferCount);
		});
	}

	/**
	 * Checks if any of the capacities are below zero.
	 */
	private void validateCapacities(Map<Resource, Integer> capacities) {
		capacities.forEach((resource, capacity) -> {
			if (capacity < 0) {
				throw new IllegalArgumentException("Capacity cannot be negative: " + resource + ":" + capacity);
			}
		});
	}

	/**
	 * This method does not remove any resources. It simply removes entries for resources or capacities maps
	 * that have values as 0.
	 */
	public void clean() {
		Map<Resource, Integer> newResources = new HashMap<>();
		this.resources.forEach((resource, count) -> {
			if(count != 0) {
				newResources.put(resource, count);
			}
		});
		this.resources.clear();
		this.resources.putAll(newResources);

		Map<Resource, Integer> newCapacities = new HashMap<>();
		this.capacities.forEach((resource, capacity) -> {
			if(capacity != 0) {
				newCapacities.put(resource, capacity);
			}
		});
		this.capacities.clear();
		this.capacities.putAll(newCapacities);
	}

	@Override
	public String toString() {
		return "FactoryStorage{" + "capacities=" + capacities + ", resources=" + resources + '}';
	}
}
