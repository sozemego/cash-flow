package com.soze.factory.service;

import com.soze.common.dto.Resource;
import com.soze.common.json.JsonUtils;
import com.soze.common.message.server.*;
import com.soze.factory.FactoryConverter;
import com.soze.factory.aggregate.Factory;
import com.soze.factory.aggregate.Storage;
import com.soze.factory.event.EventVisitor;
import com.soze.factory.event.FactoryCreated;
import com.soze.factory.event.ProductionStarted;
import com.soze.factory.event.StorageCapacityChanged;
import com.soze.factory.repository.FactoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.*;

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

	public void sell(String factoryId, Resource resource, int count) {
		LOG.info("Attempting to sell {} of {} from factoryId = {}", count, resource, factoryId);
		Factory factory = repository.findById(UUID.fromString(factoryId)).orElseThrow(
			() -> new IllegalStateException("Factory with id " + factoryId + " does not exist"));

		Storage storage = factory.getStorage();
		boolean hasResource = storage.hasResource(resource, count);
		if (!hasResource) {
			throw new IllegalStateException(
				"Factory with id = " + factoryId + ", does not have " + count + " of " + resource);
		}
		storage.removeResource(resource, count);
		LOG.info("Removed {} of {} from factoryId = {}", count, resource, factory.getId());

		sendToAll(new StorageContentChanged(factoryId, resource, -count));
	}

	@EventListener
	@Override
	public void visit(FactoryCreated factoryCreated) {
		LOG.info("{}", factoryCreated);
		Factory factory = repository.findById(UUID.fromString(factoryCreated.getEntityId())).get();
		sendToAll(new FactoryAdded(factoryConverter.convert(factory)));
	}

	@EventListener
	@Override
	public void visit(ProductionStarted productionStarted) {
		LOG.info("{}", productionStarted);
	}

	@Override
	public void visit(StorageCapacityChanged storageCapacityChanged) {
		LOG.info("{}", storageCapacityChanged);
	}
}
