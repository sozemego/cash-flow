package com.soze.factory.service;

import com.soze.common.json.JsonUtils;
import com.soze.common.message.server.FactoryAdded;
import com.soze.common.message.server.ServerMessage;
import com.soze.factory.FactoryConverter;
import com.soze.factory.aggregate.Factory;
import com.soze.factory.event.*;
import com.soze.factory.repository.FactoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
public class FactoryService implements EventVisitor {

	private static final Logger LOG = LoggerFactory.getLogger(FactoryService.class);

	private final FactoryConverter factoryConverter;
	private final FactoryRepository repository;
	private final SocketSessionContainer socketSessionContainer;

	@Autowired
	public FactoryService(FactoryConverter factoryConverter, FactoryRepository repository,
												SocketSessionContainer socketSessionContainer
											 ) {
		this.factoryConverter = factoryConverter;
		this.repository = repository;
		this.socketSessionContainer = socketSessionContainer;
	}

	public void handleNewSession(WebSocketSession session) {

		//send back all factories to the session
		List<Factory> factories = repository.getAll();
		LOG.info("Sending FactoryAdded message for {} factories", factories.size());
		for (Factory factory : factories) {
			FactoryAdded factoryAdded = new FactoryAdded(factoryConverter.convert(factory));
			sendTo(factoryAdded, session);
		}
	}

	private void sendTo(ServerMessage serverMessage, WebSocketSession session) {
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
		for (WebSocketSession session : socketSessionContainer.getAllSessions()) {
			sendTo(textMessage, session);
		}
	}

	private void sendToAll(Object message) {
		TextMessage textMessage = new TextMessage(JsonUtils.serialize(message));
		for (WebSocketSession session : socketSessionContainer.getAllSessions()) {
			sendTo(textMessage, session);
		}
	}

	@EventListener
	@Override
	public void visit(FactoryCreated factoryCreated) {
		LOG.info("{}", factoryCreated);
		Factory factory = repository.findById(UUID.fromString(factoryCreated.entityId)).get();
		sendToAll(new FactoryAdded(factoryConverter.convert(factory)));
	}

	@EventListener
	@Override
	public void visit(ProductionStarted productionStarted) {
		LOG.info("{}", productionStarted);
		sendToAll(productionStarted);
	}

	@Override
	@EventListener
	public void visit(StorageCapacityChanged storageCapacityChanged) {
		LOG.info("{}", storageCapacityChanged);
		sendToAll(new com.soze.common.message.server.StorageCapacityChanged(storageCapacityChanged.entityId,
																																				storageCapacityChanged.change
		));
	}

	@Override
	@EventListener
	public void visit(ProductionLineAdded productionLineAdded) {
		LOG.info("{}", productionLineAdded);
		sendToAll(productionLineAdded);
	}

	@Override
	@EventListener
	public void visit(ProductionFinished productionFinished) {
		LOG.info("{}", productionFinished);
		sendToAll(productionFinished);
	}

	@Override
	@EventListener
	public void visit(ResourceSold resourceSold) {
		LOG.info("{}", resourceSold);
		sendToAll(resourceSold);
	}
}
