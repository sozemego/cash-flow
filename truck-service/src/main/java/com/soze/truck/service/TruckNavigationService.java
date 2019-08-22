package com.soze.truck.service;

import com.soze.truck.world.RemoteWorldService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class TruckNavigationService {

	private static final Logger LOG = LoggerFactory.getLogger(TruckNavigationService.class);

	private final RemoteWorldService remoteWorldService;

	private final Map<String, TruckNavigation> navigations = new HashMap<>();

	@Autowired
	public TruckNavigationService(RemoteWorldService remoteWorldService) {
		this.remoteWorldService = remoteWorldService;
	}

	public void setCityId(String truckId, String cityId) {
		TruckNavigation navigation = getTruckNavigation(truckId);
		if (navigation == null) {
			navigation = new TruckNavigation(truckId);
		}
		navigation.setCurrentCityId(cityId);
		navigations.put(truckId, navigation);
	}

	public TruckNavigation getTruckNavigation(String truckId) {
		return navigations.get(truckId);
	}

}
