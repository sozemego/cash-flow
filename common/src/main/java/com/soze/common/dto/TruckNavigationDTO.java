package com.soze.common.dto;

public class TruckNavigationDTO {

	private String currentCityId;
	private long travelStartTime;
	private long arrivalTime;
	private String nextCityId;

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

	public long getArrivalTime() {
		return arrivalTime;
	}

	public void setArrivalTime(long arrivalTime) {
		this.arrivalTime = arrivalTime;
	}

	public String getNextCityId() {
		return nextCityId;
	}

	public void setNextCityId(String nextCityId) {
		this.nextCityId = nextCityId;
	}
}
