package com.soze.truck.service;

import com.soze.common.dto.CityDTO;
import com.soze.common.dto.Clock;
import com.soze.truck.external.RemoteWorldService;
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
	private final Clock clock;

	private final Map<String, TruckNavigation> navigations = new HashMap<>();

	@Autowired
	public TruckNavigationService(RemoteWorldService remoteWorldService, Clock clock) {
		this.remoteWorldService = remoteWorldService;
		this.clock = clock;
	}

	void setCityId(String truckId, String cityId) {
		LOG.info("Setting cityId for truckId = {} to cityId = {}", truckId, cityId);
		TruckNavigation navigation = getTruckNavigation(Objects.requireNonNull(truckId));
		navigation.setCurrentCityId(Objects.requireNonNull(cityId));
	}

	/**
	 * Gets {@link TruckNavigation} for a given truck.
	 * If this truck does not have TruckNavigation, creates a new one.
	 */
	TruckNavigation getTruckNavigation(String truckId) {
		return navigations.computeIfAbsent(truckId, TruckNavigation::new);
	}

	String getCityIdForTruck(String truckId) {
		TruckNavigation navigation = getTruckNavigation(truckId);
		return navigation.getCurrentCityId();
	}

	TruckNavigation travel(String truckId, String cityId, int kilometersPerHour) {
		TruckNavigation navigation = getTruckNavigation(truckId);
		if (navigation.getNextCityId() != null) {
			throw new IllegalStateException(truckId + " is already travelling!");
		}
		navigation.setNextCityId(cityId);
		navigation.setStartTime(clock.getCurrentGameTime());
		long distance = calculateDistance(navigation.getCurrentCityId(), cityId);
		LOG.info("Distance between {} and {} is {}m or {}km", navigation.getCurrentCityId(), cityId, distance, distance / 1000);
		long metersPerMinute = (kilometersPerHour * 1000) / 60;
		long timeMinutes = distance / metersPerMinute;
		long timeMs = TimeUnit.MINUTES.toMillis(timeMinutes);
		navigation.setArrivalTime(navigation.getStartTime() + timeMs);
		return navigation;
	}

	void finishTravel(String truckId) {
		TruckNavigation navigation = getTruckNavigation(truckId);
		if (navigation.getNextCityId() == null) {
			throw new IllegalStateException("Cannot finish travel, truck with id = " + truckId + " is not travelling");
		}
		setCityId(truckId, navigation.getNextCityId());
		navigation.setNextCityId(null);
		navigation.setArrivalTime(-1);
		navigation.setStartTime(-1);
	}

	/**
	 * Calculates distance in meters between two cities.
	 */
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

		double d = R * c;
		return (long) d;
	}

}
