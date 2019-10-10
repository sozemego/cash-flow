package com.soze.truck.service;

import com.soze.common.dto.CityDTO;
import com.soze.common.dto.Clock;
import com.soze.common.dto.Resource;
import com.soze.common.json.JsonUtils;
import com.soze.common.message.server.*;
import com.soze.truck.domain.Storage;
import com.soze.truck.domain.Truck;
import com.soze.truck.external.RemoteFactoryService;
import com.soze.truck.external.RemotePlayerService;
import com.soze.truck.external.RemoteWorldService;
import com.soze.truck.repository.TruckRepository;
import com.soze.truck.saga.BuyResourceSaga;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
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

	private final Set<WebSocketSession> sessions = new HashSet<>();

	private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

	@Autowired
	public TruckService(TruckConverter truckConverter, TruckNavigationService truckNavigationService,
											RemoteWorldService remoteWorldService, RemoteFactoryService remoteFactoryService,
											RemotePlayerService playerService, Clock clock, TruckRepository truckRepository
										 ) {
		this.truckConverter = truckConverter;
		this.truckNavigationService = truckNavigationService;
		this.remoteWorldService = remoteWorldService;
		this.remoteFactoryService = remoteFactoryService;
		this.playerService = playerService;
		this.clock = clock;
		this.truckRepository = truckRepository;
	}

	/**
	 * Adds a truck to the world in a certain cityId.
	 * <p>
	 * Sends out {@link TruckAdded} message to all connected sessions.
	 */
	public void addTruck(Truck truck, String cityId) {
		LOG.info("Adding truck = {}", truck);
		validateTruck(truck);

		truckRepository.addTruck(truck);

		truckNavigationService.setCityId(truck.getId(), cityId);

		TruckAdded truckAdded = new TruckAdded(truckConverter.convert(truck));
		sendToAll(truckAdded);
	}

	/**
	 * Adds a session to active sessions.
	 * Sends {@link TruckAdded} message for each current truck.
	 */
	public void addSession(WebSocketSession session) {
		sessions.add(session);

		for (Truck truck : getTrucks()) {
			sendTo(session, new TruckAdded(truckConverter.convert(truck)));
		}
	}

	public void removeSession(WebSocketSession session) {
		sessions.remove(session);
	}

	public List<Truck> getTrucks() {
		return truckRepository.getTrucks();
	}

	private void sendTo(WebSocketSession session, ServerMessage serverMessage) {
		TextMessage textMessage = new TextMessage(JsonUtils.serialize(serverMessage));
		sendTo(textMessage, session);
	}

	private void sendTo(TextMessage textMessage, WebSocketSession session) {
		try {
			session.sendMessage(textMessage);
		} catch (IOException e) {
			LOG.warn("Exception when sending a server message, to session {}", session.getId(), e);
		}
	}

	public void sendToAll(ServerMessage serverMessage) {
		TextMessage textMessage = new TextMessage(JsonUtils.serialize(serverMessage));
		for (WebSocketSession session : sessions) {
			sendTo(textMessage, session);
		}
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
	public void travel(String truckId, String cityId) {
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
			truckId, cityId, navigation.startTime, navigation.arrivalTime);
		sendToAll(truckTravelStarted);

		executorService.schedule(() -> {
			truckNavigationService.finishTravel(truckId);
			sendToAll(new TruckArrived(truckId));
		}, realTimeTravelDuration, TimeUnit.MILLISECONDS);
	}

	public Optional<Truck> getTruck(String truckId) {
		return truckRepository.findTruckById(truckId);
	}

	/**
	 * <code>TruckId</code> buys <code>count</code> resources from factory with id <code>factoryId</code>.
	 */
	public void buyResource(String truckId, String factoryId, Resource resource, int count) {
		new BuyResourceSaga(this, truckRepository, remoteFactoryService, playerService, truckId, factoryId, resource, count)
			.run();
	}

	/**
	 * Removes all content from truck.
	 */
	public void dump(String truckId) {
		LOG.info("Dumping contents of truck {}", truckId);
		Truck truck = getTruck(truckId).orElseThrow(
			() -> new IllegalArgumentException("Truck with id = " + truckId + " does not exist"));

		Storage storage = truck.getStorage();
		Map<Resource, Integer> resources = storage.getResources();
		List<ServerMessage> storageContentChangedList = new ArrayList<>();
		for (Map.Entry<Resource, Integer> entry : resources.entrySet()) {
			storageContentChangedList.add(new StorageContentChanged(truckId, entry.getKey(), -entry.getValue()));
		}
		for (ServerMessage serverMessage : storageContentChangedList) {
			sendToAll(serverMessage);
		}
		storage.clear();
		LOG.info("Contents of truck {} cleared", truckId);
		truckRepository.update(truck);
	}

}
