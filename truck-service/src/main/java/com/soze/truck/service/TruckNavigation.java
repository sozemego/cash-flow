package com.soze.truck.service;

import java.beans.ConstructorProperties;

public class TruckNavigation {

	public final String truckId;

	public String currentCityId = null;

	public long startTime = -1L;
	public long arrivalTime = -1L;
	public String nextCityId;

	public TruckNavigation(String truckId) {
		this.truckId = truckId;
	}

	@ConstructorProperties({"truckId", "currentCityId", "startTime", "arrivalTime", "nextCityId"})
	public TruckNavigation(String truckId, String currentCityId, long startTime, long arrivalTime, String nextCityId
												) {
		this.truckId = truckId;
		this.currentCityId = currentCityId;
		this.startTime = startTime;
		this.arrivalTime = arrivalTime;
		this.nextCityId = nextCityId;
	}
}
