package com.soze.factory.controller;

import com.soze.common.json.JsonUtils;
import com.soze.common.ws.factory.client.ClientMessage;
import com.soze.common.ws.factory.client.ClientMessage.ClientMessageType;
import com.soze.common.ws.factory.client.CreateFactory;
import com.soze.factory.service.FactoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class FactoryWebSocketController extends TextWebSocketHandler {

	private static final Logger LOG = LoggerFactory.getLogger(FactoryWebSocketController.class);

	private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

	private final FactoryService factoryService;

	@Autowired
	public FactoryWebSocketController(FactoryService factoryService) {
		this.factoryService = factoryService;
	}

	@Override
	public void afterConnectionEstablished(WebSocketSession session
																				) throws Exception {
		LOG.info("{} connected", session.getId());
		sessions.put(session.getId(), session);
		factoryService.addSession(session);
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status
																	 ) throws Exception {
		LOG.info("{} disconnected", session.getId());
		sessions.remove(session.getId());
		factoryService.removeSession(session);
	}

	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message
																	) throws Exception {
		LOG.trace("Received message from client id {}", session.getId());
		ClientMessage clientMessage = JsonUtils.parse(message.getPayload(), ClientMessage.class);
		LOG.trace("Client message type from client id{}", clientMessage.getType());
		if (clientMessage.getType() == ClientMessageType.CREATE_FACTORY) {
			factoryService.handleCreateFactory((CreateFactory) clientMessage);
		}
	}
}
