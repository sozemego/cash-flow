package com.soze.truck.service;

import com.soze.common.dto.Clock;
import com.soze.common.message.server.TruckArrived;
import com.soze.truck.domain.Truck;
import com.soze.truck.domain.TruckNavigation;
import com.soze.truck.ws.SocketRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Profile("!test")
public class TruckSchedulerService {

	private static final Logger LOG = LoggerFactory.getLogger(TruckSchedulerService.class);

	private final TruckService truckService;
	private final TruckNavigationService truckNavigationService;
	private final SocketRegistry socketRegistry;
	private final Clock clock;
	private final MessageProducerService messageProducerService;

	@Autowired
	public TruckSchedulerService(TruckService truckService, TruckNavigationService truckNavigationService,
															 SocketRegistry socketRegistry, Clock clock,
															 MessageProducerService messageProducerService
															) {
		this.truckService = truckService;
		this.truckNavigationService = truckNavigationService;
		this.socketRegistry = socketRegistry;
		this.clock = clock;
		this.messageProducerService = messageProducerService;
	}

	@Scheduled(fixedRate = 1000L)
	public void checkNavigationFinish() {
		LOG.trace("checkNavigationFinish started");
		List<Truck> trucks = truckService.getTrucks();
		for (Truck truck : trucks) {
			UUID truckId = truck.getId();
			TruckNavigation navigation = truckNavigationService.getTruckNavigation(truckId);
			if (navigation.nextCityId == null) {
				continue;
			}
			long arrivalTime = navigation.arrivalTime;
			long currentGameTime = clock.getCurrentGameTime();
			if (currentGameTime >= arrivalTime) {
				LOG.trace("Truck with id = {} finished travel", truckId);
				truckNavigationService.finishTravel(truckId);
				TruckArrived truckArrived = new TruckArrived(truckId.toString(), navigation.nextCityId);
				socketRegistry.sendToAll(truckArrived);
				messageProducerService.sendEvent(truckArrived);
			}
		}
	}
}
