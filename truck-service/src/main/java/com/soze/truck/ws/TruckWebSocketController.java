package com.soze.truck.ws;

import com.soze.common.json.JsonUtils;
import com.soze.common.message.client.ClientMessage;
import com.soze.common.message.client.TruckTravelRequest;
import com.soze.truck.service.TruckService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
public class TruckWebSocketController extends TextWebSocketHandler {

	private static final Logger LOG = LoggerFactory.getLogger(TruckWebSocketController.class);

	private final TruckService truckService;

	@Autowired
	public TruckWebSocketController(TruckService truckService) {
		this.truckService = truckService;
	}


	@Override
	public void afterConnectionEstablished(WebSocketSession session
																				) throws Exception {
		LOG.info("{} connected", session.getId());
		truckService.addSession(session);
	}


	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status
																	 ) throws Exception {
		LOG.info("{} disconnected", session.getId());
		truckService.removeSession(session);
	}

	@Override
	public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
		LOG.info("TransportationError", exception);
	}

	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message
																	) throws Exception {
		LOG.trace("Received message from client id {}", session.getId());
		ClientMessage clientMessage = JsonUtils.parse(message.getPayload(), ClientMessage.class);
		LOG.trace("Client message type from client id{}", clientMessage.getType());
		if (clientMessage.getType() == ClientMessage.ClientMessageType.TRUCK_TRAVEL_REQUEST) {
			TruckTravelRequest truckTravelRequest = (TruckTravelRequest) clientMessage;
			truckService.travel(truckTravelRequest.getTruckId(), truckTravelRequest.getDestinationCityId());
		}
	}
}
