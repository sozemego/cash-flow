package com.soze.truck.service;

import com.soze.common.dto.CityDTO;
import com.soze.common.json.JsonUtils;
import com.soze.common.ws.factory.server.ServerMessage;
import com.soze.common.ws.factory.server.TruckAdded;
import com.soze.truck.domain.Truck;
import com.soze.truck.world.RemoteWorldService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class TruckService {

	private static final Logger LOG = LoggerFactory.getLogger(TruckService.class);

	private final TruckTemplateLoader truckTemplateLoader;
	private final TruckConverter truckConverter;
	private final TruckNavigationService truckNavigationService;
	private final RemoteWorldService remoteWorldService;

	private final List<Truck> trucks = new ArrayList<>();
	private final Set<WebSocketSession> sessions = new HashSet<>();

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
	 *
	 * Sends out {@link TruckAdded} message to all connected sessions.
	 */
	public void addTruck(Truck truck, String cityId) {
		LOG.info("Adding truck = {}", truck);
		trucks.add(truck);

		truckNavigationService.setCityId(truck.getId(), cityId);

		TruckAdded truckAdded = new TruckAdded(truckConverter.convert(truck));
		sendToAll(truckAdded);
	}

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

}
