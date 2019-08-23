package com.soze.truck.service;

import com.soze.truck.world.RemoteWorldService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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

}
