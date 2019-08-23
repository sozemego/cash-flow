package com.soze.truck.service;

import com.soze.common.dto.CityDTO;
import com.soze.truck.domain.Truck;
import com.soze.truck.world.RemoteWorldService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

public class TruckServiceStarter {

	private final TruckService truckService;
	private final TruckTemplateLoader truckTemplateLoader;
	private final TruckNavigationService truckNavigationService;
	private final RemoteWorldService remoteWorldService;

	@Autowired
	public TruckServiceStarter(TruckService truckService, TruckTemplateLoader truckTemplateLoader,
														 TruckNavigationService truckNavigationService, RemoteWorldService remoteWorldService
														) {
		this.truckService = truckService;
		this.truckTemplateLoader = truckTemplateLoader;
		this.truckNavigationService = truckNavigationService;
		this.remoteWorldService = remoteWorldService;
	}

	@PostConstruct
	public void setup() {
		CityDTO city = remoteWorldService.getCityByName("Wroclaw");

		Truck truck1 = truckTemplateLoader.constructTruckByTemplateId("BASIC_TRUCK");
		this.truckNavigationService.setCityId(truck1.getId(), city.id);
		truckService.addTruck(truck1);
		Truck truck2 = truckTemplateLoader.constructTruckByTemplateId("BASIC_TRUCK");
		this.truckNavigationService.setCityId(truck2.getId(), city.id);
		truckService.addTruck(truck2);
	}
}
