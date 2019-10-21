package com.soze.truck.service;

import com.soze.common.dto.CityDTO;
import com.soze.common.dto.Clock;
import com.soze.truck.domain.TruckNavigation;
import com.soze.truck.external.RemoteWorldService;
import com.soze.truck.repository.TruckNavigationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class TruckNavigationService {

	private static final Logger LOG = LoggerFactory.getLogger(TruckNavigationService.class);

	private final TruckNavigationRepository repository;
	private final RemoteWorldService remoteWorldService;
	private final Clock clock;

	@Autowired
	public TruckNavigationService(TruckNavigationRepository repository, RemoteWorldService remoteWorldService, Clock clock
															 ) {
		this.repository = repository;
		this.remoteWorldService = remoteWorldService;
		this.clock = clock;
	}

	@Transactional
	public void setCityId(UUID truckId, String cityId) {
		LOG.info("Setting cityId for truckId = {} to cityId = {}", truckId, cityId);
		TruckNavigation navigation = getTruckNavigation(Objects.requireNonNull(truckId));
		navigation.currentCityId = Objects.requireNonNull(cityId);
	}

	TruckNavigation getTruckNavigation(UUID truckId) {
		return repository.getTruckNavigation(truckId);
	}

	String getCityIdForTruck(UUID truckId) {
		TruckNavigation navigation = getTruckNavigation(truckId);
		return navigation.currentCityId;
	}

	@Transactional
	public TruckNavigation travel(UUID truckId, String cityId, int kilometersPerHour) {
		TruckNavigation navigation = getTruckNavigation(truckId);
		if (navigation.nextCityId != null) {
			throw new IllegalStateException(truckId + " is already travelling!");
		}
		navigation.nextCityId = cityId;
		navigation.startTime = clock.getCurrentGameTime();
		long distanceMeters = calculateDistance(navigation.currentCityId, cityId);
		LOG.info("Distance between {} and {} is {}m or {}km", navigation.currentCityId, cityId, distanceMeters, distanceMeters / 1000);
		long metersPerMinute = (kilometersPerHour * 1000) / 60;
		long timeMinutes = distanceMeters / metersPerMinute;
		long timeMs = TimeUnit.MINUTES.toMillis(timeMinutes);
		navigation.arrivalTime = navigation.startTime + timeMs;
		return navigation;
	}

	@Transactional
	public void finishTravel(UUID truckId) {
		LOG.info("Finishing travel for truck {}", truckId);
		TruckNavigation navigation = getTruckNavigation(truckId);
		if (navigation.nextCityId == null) {
			throw new IllegalStateException("Cannot finish travel, truck with id = " + truckId + " is not travelling");
		}
		setCityId(truckId, navigation.nextCityId);
		navigation.nextCityId = null;
		navigation.arrivalTime = -1;
		navigation.startTime = -1;
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
