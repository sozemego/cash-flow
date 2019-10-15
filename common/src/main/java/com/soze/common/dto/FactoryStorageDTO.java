package com.soze.common.dto;

import java.beans.ConstructorProperties;
import java.util.Map;

public class FactoryStorageDTO {

	private final Map<Resource, Integer> capacities;
	private final Map<Resource, Integer> resources;

	@ConstructorProperties({"capacities", "resources"})
	public FactoryStorageDTO(Map<Resource, Integer> capacities, Map<Resource, Integer> resources) {
		this.capacities = capacities;
		this.resources = resources;
	}

	public Map<Resource, Integer> getCapacities() {
		return capacities;
	}

	public Map<Resource, Integer> getResources() {
		return resources;
	}

	@Override
	public String toString() {
		return "FactoryStorageDTO{" + "capacities=" + capacities + ", resources=" + resources + '}';
	}
}
