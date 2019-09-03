package com.soze.factory.domain;

import com.soze.common.dto.Resource;

import java.util.HashMap;
import java.util.Map;

public class Storage {

	private final int capacity;
	private final Map<Resource, Integer> resources = new HashMap<>();

	private int capacityTaken = 0;

	public Storage(int capacity) {
		this.capacity = capacity;
	}

	public void addResource(Resource resource) {
		if (!canFit(resource)) {
			return;
		}
		resources.compute(resource, (res, count) -> {
			if (count == null) {
				return 1;
			}
			return ++count;
		});
		capacityTaken += 1;
	}

	public boolean canFit(Resource resource) {
		return capacityTaken < capacity;
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
		capacityTaken -= count;
	}

	public boolean hasResource(Resource resource) {
		return hasResource(resource, 1);
	}

	public boolean hasResource(Resource resource, int count) {
		return resources.getOrDefault(resource, 0) >= count;
	}

	public int getCapacity() {
		return capacity;
	}

	public int getCapacityTaken() {
		return capacityTaken;
	}

	public boolean isFull() {
		return getCapacity() == getCapacityTaken();
	}

	public int getRemainingCapacity() {
		return getCapacity() - getCapacityTaken();
	}

	public Map<Resource, Integer> getResources() {
		return new HashMap<>(resources);
	}

	@Override
	public String toString() {
		return "Storage{" + "capacity=" + capacity + ", resources=" + resources + ", capacityTaken=" + capacityTaken + '}';
	}
}
