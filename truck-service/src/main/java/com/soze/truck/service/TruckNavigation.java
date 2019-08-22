package com.soze.truck.service;

public class TruckNavigation {

	private final String truckId;

	private String currentCityId = null;

	public TruckNavigation(String truckId) {
		this.truckId = truckId;
	}

	public String getTruckId() {
		return truckId;
	}

	public String getCurrentCityId() {
		return currentCityId;
	}

	public void setCurrentCityId(String currentCityId) {
		this.currentCityId = currentCityId;
	}
}
