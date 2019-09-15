package com.soze.factory.controller;

import com.soze.common.json.JsonUtils;
import com.soze.common.message.client.ClientMessage;
import com.soze.factory.service.FactoryService;
import com.soze.factory.service.SocketSessionContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
public class FactoryWebSocketController extends TextWebSocketHandler {

	private static final Logger LOG = LoggerFactory.getLogger(FactoryWebSocketController.class);

	private final SocketSessionContainer socketSessionContainer;
	private final FactoryService factoryService;

	@Autowired
	public FactoryWebSocketController(SocketSessionContainer socketSessionContainer, FactoryService factoryService
																	 ) {
		this.socketSessionContainer = socketSessionContainer;
		this.factoryService = factoryService;
	}

	@Override
	public void afterConnectionEstablished(WebSocketSession session
																				) throws Exception {
		LOG.info("{} connected", session.getId());
		socketSessionContainer.addSession(session);
		factoryService.handleNewSession(session);
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status
																	 ) throws Exception {
		LOG.info("{} disconnected", session.getId());
		socketSessionContainer.removeSession(session.getId());
	}

	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message
																	) throws Exception {
		LOG.trace("Received message from client id {}", session.getId());
		ClientMessage clientMessage = JsonUtils.parse(message.getPayload(), ClientMessage.class);
		LOG.trace("Client message type from client id{}", clientMessage.getType());
	}
}
