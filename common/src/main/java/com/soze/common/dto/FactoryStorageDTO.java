package com.soze.common.dto;

import java.beans.ConstructorProperties;
import java.util.Map;

public class FactoryStorageDTO {

	private final Map<Resource, Integer> capacities;
	private final Map<Resource, Integer> resources;
	private final Map<Resource, Integer> prices;

	@ConstructorProperties({"capacities", "resources", "prices"})
	public FactoryStorageDTO(Map<Resource, Integer> capacities, Map<Resource, Integer> resources, Map<Resource, Integer> prices) {
		this.capacities = capacities;
		this.resources = resources;
		this.prices = prices;
	}

	public Map<Resource, Integer> getCapacities() {
		return capacities;
	}

	public Map<Resource, Integer> getResources() {
		return resources;
	}

	public Map<Resource, Integer> getPrices() {
		return prices;
	}

	@Override
	public String toString() {
		return "FactoryStorageDTO{" + "capacities=" + capacities + ", resources=" + resources + ", prices=" + prices + '}';
	}
}
