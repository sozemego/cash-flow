package com.soze.truck.service;

import com.soze.common.dto.CityDTO;
import com.soze.truck.domain.Truck;
import com.soze.truck.external.RemoteWorldService;
import com.soze.truck.repository.TruckRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class TruckServiceStarter {

	private static final Logger LOG = LoggerFactory.getLogger(TruckServiceStarter.class);

	private final TruckService truckService;
	private final TruckTemplateLoader truckTemplateLoader;
	private final RemoteWorldService remoteWorldService;

	@Autowired
	public TruckServiceStarter(TruckService truckService, TruckTemplateLoader truckTemplateLoader,
														 RemoteWorldService remoteWorldService
														) {
		this.truckService = truckService;
		this.truckTemplateLoader = truckTemplateLoader;
		this.remoteWorldService = remoteWorldService;
	}

	@PostConstruct
	public void setup() {
		LOG.info("TruckServiceStarted init...");
		CityDTO city = remoteWorldService.getCityByName("Wroclaw");

		if (!truckService.getTrucks().isEmpty()) {
			LOG.info("Trucks are already present, skipping starter trucks.");
			return;
		}

		Truck truck1 = truckTemplateLoader.constructTruckByTemplateId("BASIC_TRUCK");
		truckService.addTruck(truck1, city.id);
		Truck truck2 = truckTemplateLoader.constructTruckByTemplateId("BASIC_TRUCK");
		truckService.addTruck(truck2, city.id);
		Truck truck3 = truckTemplateLoader.constructTruckByTemplateId("FAST_TRUCK");
		truckService.addTruck(truck3, city.id);
	}
}
