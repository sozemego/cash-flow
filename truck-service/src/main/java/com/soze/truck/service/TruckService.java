package com.soze.truck.service;

import com.soze.common.dto.CityDTO;
import com.soze.common.json.JsonUtils;
import com.soze.common.message.server.ServerMessage;
import com.soze.common.message.server.TruckAdded;
import com.soze.truck.domain.Truck;
import com.soze.truck.world.RemoteWorldService;
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

	private final TruckTemplateLoader truckTemplateLoader;
	private final TruckConverter truckConverter;
	private final TruckNavigationService truckNavigationService;
	private final RemoteWorldService remoteWorldService;

	private final List<Truck> trucks = new ArrayList<>();
	private final Set<WebSocketSession> sessions = new HashSet<>();

	private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

	@Autowired
	public TruckService(TruckTemplateLoader truckTemplateLoader, TruckConverter truckConverter,
											TruckNavigationService truckNavigationService, RemoteWorldService remoteWorldService
										 ) {
		this.truckTemplateLoader = truckTemplateLoader;
		this.truckConverter = truckConverter;
		this.truckNavigationService = truckNavigationService;
		this.remoteWorldService = remoteWorldService;
	}

	/**
	 * Adds a truck to the world in a certain cityId.
	 * <p>
	 * Sends out {@link TruckAdded} message to all connected sessions.
	 */
	public void addTruck(Truck truck, String cityId) {
		LOG.info("Adding truck = {}", truck);
		validateTruck(truck);

		trucks.add(truck);

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

		for (Truck truck : trucks) {
			sendTo(session, new TruckAdded(truckConverter.convert(truck)));
		}
	}

	public void removeSession(WebSocketSession session) {
		sessions.remove(session);
	}

	public List<Truck> getTrucks() {
		return trucks;
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

	private void sendToAll(ServerMessage serverMessage) {
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

		TruckNavigation currentNavigation = truckNavigationService.getOrCreateTruckNavigation(truckId);
		if (currentNavigation.getCurrentCityId().equals(cityId)) {
			throw new IllegalArgumentException("Truck with id = " + truckId + " is already at city id = " + cityId);
		}

		TruckNavigation navigation = truckNavigationService.travel(truckId, cityId, truck.getSpeed());
		long time = navigation.getArrivalTime() - navigation.getTravelStartTime();
		LOG.info("Truck {} will arrive at {} in {} ms, hours = {}", truckId, cityId, time, TimeUnit.MILLISECONDS.toHours(time));
		executorService.schedule(() -> {

		}, time, TimeUnit.MILLISECONDS);
	}

	public Optional<Truck> getTruck(String truckId) {
		Objects.requireNonNull(truckId);

		return this.trucks.stream().filter(truck -> truck.getId().equals(truckId)).findFirst();
	}

}
