package com.soze.truck.service;

import com.soze.common.dto.CityDTO;
import com.soze.common.dto.Clock;
import com.soze.common.dto.Resource;
import com.soze.common.message.server.ServerMessage;
import com.soze.common.message.server.StorageContentChanged;
import com.soze.common.message.server.TruckAdded;
import com.soze.common.message.server.TruckTravelStarted;
import com.soze.truck.domain.Storage;
import com.soze.truck.domain.Truck;
import com.soze.truck.domain.TruckNavigation;
import com.soze.truck.external.RemoteFactoryService;
import com.soze.truck.external.RemotePlayerService;
import com.soze.truck.external.RemoteWorldService;
import com.soze.truck.repository.TruckRepository;
import com.soze.truck.saga.BuyResourceSaga;
import com.soze.truck.saga.SellResourceSaga;
import com.soze.truck.ws.SessionRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class TruckService {

	private static final Logger LOG = LoggerFactory.getLogger(TruckService.class);

	private final TruckConverter truckConverter;
	private final TruckNavigationService truckNavigationService;
	private final RemoteWorldService remoteWorldService;
	private final RemoteFactoryService remoteFactoryService;
	private final RemotePlayerService playerService;
	private final Clock clock;
	private final TruckRepository truckRepository;
	private final SessionRegistry sessionRegistry;

	@Autowired
	public TruckService(TruckConverter truckConverter, TruckNavigationService truckNavigationService,
											RemoteWorldService remoteWorldService, RemoteFactoryService remoteFactoryService,
											RemotePlayerService playerService, Clock clock, TruckRepository truckRepository,
											SessionRegistry sessionRegistry
										 ) {
		this.truckConverter = truckConverter;
		this.truckNavigationService = truckNavigationService;
		this.remoteWorldService = remoteWorldService;
		this.remoteFactoryService = remoteFactoryService;
		this.playerService = playerService;
		this.clock = clock;
		this.truckRepository = truckRepository;
		this.sessionRegistry = sessionRegistry;
	}

	/**
	 * Adds a truck to the world in a certain cityId.
	 *
	 * Sends out {@link TruckAdded} message to all connected sessions.
	 */
	public void addTruck(Truck truck, String cityId) {
		LOG.info("Adding truck = {}", truck);
		validateTruck(truck);

		truckRepository.addTruck(truck);

		truckNavigationService.setCityId(truck.getId(), cityId);

		TruckAdded truckAdded = new TruckAdded(truckConverter.convert(truck));
		sessionRegistry.sendToAll(truckAdded);
	}

	public List<Truck> getTrucks() {
		return truckRepository.getTrucks();
	}

	public List<Truck> getTrucks(UUID playerId) {
		return truckRepository.findByPlayerId(playerId);
	}

	private void validateTruck(Truck truck) {
		if (truck.getId() == null) {
			throw new IllegalArgumentException("id cannot be null");
		}
		if (truck.getStorage() == null) {
			throw new IllegalArgumentException("storage cannot be null");
		}
		Optional<Truck> previousTruck = getTruck(truck.getId());
		if (previousTruck.isPresent()) {
			throw new IllegalArgumentException("Truck with id = " + truck.getId() + " already exists");
		}
	}

	/**
	 * Sends a given truck to a given city.
	 */
	public void travel(UUID truckId, String cityId) {
		LOG.info("Truck {} wants to travel to {}", truckId, cityId);
		Truck truck = getTruck(truckId).orElseThrow(
			() -> new IllegalArgumentException("Truck with id = " + truckId + " does not exist"));
		CityDTO city = remoteWorldService.getCityById(cityId).orElseThrow(
			() -> new IllegalArgumentException("City with id = " + cityId + " does not exist"));

		TruckNavigation currentNavigation = truckNavigationService.getTruckNavigation(truckId);
		if (currentNavigation.currentCityId.equals(cityId)) {
			throw new IllegalArgumentException("Truck with id = " + truckId + " is already at city id = " + cityId);
		}

		TruckNavigation navigation = truckNavigationService.travel(truckId, cityId, truck.getSpeed());
		long gameTimeTravelDuration = navigation.arrivalTime - navigation.startTime;
		long realTimeTravelDuration = (navigation.arrivalTime - navigation.startTime) / clock.getMultiplier();
		LOG.info("Truck {} will arrive at {} in [game - {} ms, {} minutes], [real - {} ms, {} minutes]", truckId, cityId,
						 gameTimeTravelDuration, TimeUnit.MILLISECONDS.toMinutes(gameTimeTravelDuration), realTimeTravelDuration,
						 TimeUnit.MILLISECONDS.toMinutes(realTimeTravelDuration)
						);

		TruckTravelStarted truckTravelStarted = new TruckTravelStarted(
			truckId.toString(), cityId, navigation.startTime, navigation.arrivalTime);
		sessionRegistry.sendToAll(truckTravelStarted);
	}

	public Optional<Truck> getTruck(UUID truckId) {
		return truckRepository.findTruckById(truckId);
	}

	/**
	 * <code>TruckId</code> buys <code>count</code> resources from factory with id <code>factoryId</code>.
	 */
	public void buyResource(UUID truckId, String factoryId, Resource resource, int count) {
		new BuyResourceSaga(
			this, truckRepository, remoteFactoryService, playerService, sessionRegistry, truckId, factoryId, resource,
			count
		).run();
	}

	public void sellResource(UUID truckId, String factoryId, Resource resource, int count) {
		new SellResourceSaga(
			this, truckRepository, remoteFactoryService, playerService, sessionRegistry, truckId, resource, count,
			factoryId
		).run();
	}

	/**
	 * Removes all content from truck.
	 */
	public void dump(UUID truckId) {
		LOG.info("Dumping contents of truck {}", truckId);
		Truck truck = getTruck(truckId).orElseThrow(
			() -> new IllegalArgumentException("Truck with id = " + truckId + " does not exist"));

		Storage storage = truck.getStorage();
		Map<Resource, Integer> resources = storage.getResources();
		List<ServerMessage> storageContentChangedList = new ArrayList<>();
		for (Map.Entry<Resource, Integer> entry : resources.entrySet()) {
			storageContentChangedList.add(new StorageContentChanged(truckId.toString(), entry.getKey(), -entry.getValue()));
		}
		for (ServerMessage serverMessage : storageContentChangedList) {
			sessionRegistry.sendToAll(serverMessage);
		}
		storage.clear();
		LOG.info("Contents of truck {} cleared", truckId);
		truckRepository.update(truck);
	}

}
