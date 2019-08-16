package com.soze.truck.service;

import com.soze.truck.domain.Truck;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class TruckService {

	private static final Logger LOG = LoggerFactory.getLogger(TruckService.class);

	private final TruckTemplateLoader truckTemplateLoader;

	private final List<Truck> trucks = new ArrayList<>();

	private final Set<WebSocketSession> sessions = new HashSet<>();

	@Autowired
	public TruckService(TruckTemplateLoader truckTemplateLoader) {
		this.truckTemplateLoader = truckTemplateLoader;
	}

	@PostConstruct
	public void setup() {
		Truck truck1 = truckTemplateLoader.constructTruckByTemplateId("BASIC_TRUCK");
		addTruck(truck1);
		Truck truck2 = truckTemplateLoader.constructTruckByTemplateId("BASIC_TRUCK");
		addTruck(truck2);
	}

	public void addTruck(Truck truck) {
		LOG.info("Adding truck = {}", truck);
		trucks.add(truck);
	}

	public void addSession(WebSocketSession session) {
		sessions.add(session);
	}

	public void removeSession(WebSocketSession session) {
		sessions.remove(session);
	}
}
