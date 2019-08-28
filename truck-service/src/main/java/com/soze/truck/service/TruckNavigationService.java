package com.soze.truck.service;

import com.soze.common.dto.CityDTO;
import com.soze.truck.world.RemoteWorldService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
public class TruckNavigationService {

	private static final Logger LOG = LoggerFactory.getLogger(TruckNavigationService.class);

	private final RemoteWorldService remoteWorldService;

	private final Map<String, TruckNavigation> navigations = new HashMap<>();

	@Autowired
	public TruckNavigationService(RemoteWorldService remoteWorldService) {
		this.remoteWorldService = remoteWorldService;
	}

	void setCityId(String truckId, String cityId) {
		LOG.info("Setting cityId for truckId = {} to cityId = {}", truckId, cityId);
		TruckNavigation navigation = getOrCreateTruckNavigation(Objects.requireNonNull(truckId));
		navigation.setCurrentCityId(Objects.requireNonNull(cityId));
	}

	/**
	 * Gets {@link TruckNavigation} for a given truck.
	 * If this truck does not have TruckNavigation, creates a new one.
	 */
	TruckNavigation getOrCreateTruckNavigation(String truckId) {
		return navigations.computeIfAbsent(truckId, TruckNavigation::new);
	}

	String getCityIdForTruck(String truckId) {
		TruckNavigation navigation = getOrCreateTruckNavigation(truckId);
		return navigation.getCurrentCityId();
	}

	TruckNavigation travel(String truckId, String cityId, int speed) {
		TruckNavigation navigation = getOrCreateTruckNavigation(truckId);
		if (navigation.getNextCityId() != null) {
			throw new IllegalStateException(truckId + " is already travelling!");
		}
		navigation.setNextCityId(cityId);
		navigation.setTravelStartTime(System.currentTimeMillis());
		long distance = calculateDistance(navigation.getCurrentCityId(), cityId);
		long time = distance / speed;
		long timeMs = TimeUnit.HOURS.toMillis(time);
		navigation.setArrivalTime(navigation.getTravelStartTime() + timeMs);
		return navigation;
	}

	private long calculateDistance(String fromCityId, String toCityId) {
		double R = 6371e3;
		CityDTO fromCity = remoteWorldService.getCityById(fromCityId).get();
		CityDTO toCity = remoteWorldService.getCityById(toCityId).get();

		double φ1 = (fromCity.latitude * Math.PI) / 180;
		double φ2 = (toCity.latitude * Math.PI) / 180;
		double Δφ = ((toCity.latitude - fromCity.latitude) * Math.PI) / 180;
		double Δλ = ((toCity.longitude - fromCity.longitude) * Math.PI) / 180;

		double a = Math.sin(Δφ / 2) * Math.sin(Δφ / 2) + Math.cos(φ1) * Math.cos(φ2) * Math.sin(Δλ / 2) * Math.sin(Δλ / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

		double d = (R * c) / 1000;
		return (long) d;
	}

}
