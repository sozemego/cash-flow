package com.soze.truck.service;

public class TruckNavigation {

	private final String truckId;

	private String currentCityId = null;

	private long travelStartTime = -1L;
	private long arrivalTime = -1L;
	private String nextCityId;

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

	public long getTravelStartTime() {
		return travelStartTime;
	}

	public void setTravelStartTime(long travelStartTime) {
		this.travelStartTime = travelStartTime;
	}

	public String getNextCityId() {
		return nextCityId;
	}

	public void setNextCityId(String nextCityId) {
		this.nextCityId = nextCityId;
	}

	public long getArrivalTime() {
		return arrivalTime;
	}

	public void setArrivalTime(long arrivalTime) {
		this.arrivalTime = arrivalTime;
	}
}
