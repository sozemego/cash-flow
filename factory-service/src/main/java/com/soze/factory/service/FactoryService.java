package com.soze.factory.service;

import com.soze.common.dto.Clock;
import com.soze.common.dto.Resource;
import com.soze.common.json.JsonUtils;
import com.soze.common.message.server.*;
import com.soze.factory.FactoryConverter;
import com.soze.factory.aggregate.Factory;
import com.soze.factory.aggregate.Producer;
import com.soze.factory.aggregate.Storage;
import com.soze.factory.repository.FactoryRepository;
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
public class FactoryService {

	private static final Logger LOG = LoggerFactory.getLogger(FactoryService.class);

	private final FactoryConverter factoryConverter;
	private final FactoryRepository repository;
	private final Clock clock;

	private final Set<WebSocketSession> sessions = new HashSet<>();

	private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

	@Autowired
	public FactoryService(FactoryConverter factoryConverter,
												FactoryRepository repository,
												Clock clock
											 ) {
		this.factoryConverter = factoryConverter;
		this.repository = repository;
		this.clock = clock;
	}

	private void startProducing(Factory factory) {
		Producer producer = factory.getProducer();
		if (producer.isProducing()) {
			return;
		}
		Storage storage = factory.getStorage();
		if (storage.isFull()) {
			LOG.info("Factory {} is full", factory.getId());
			return;
		}

		producer.startProduction(clock);
		long minutes = producer.getTime();
		long timeRemaining = TimeUnit.MINUTES.toMillis(minutes) / clock.getMultiplier();
		LOG.info("Starting production of {} at factory = {}, it will finish in {} ms", producer.getResource(),
						 factory.getId(), timeRemaining
						);
		executorService.schedule(() -> {
			finishProducing(factory);
			startProducing(factory);
		}, timeRemaining, TimeUnit.MILLISECONDS);

		ResourceProductionStarted resourceProductionStarted = new ResourceProductionStarted(factory.getId(),
																																												factory.getProducer()
																																															 .getResource(),
																																												factory.getProducer()
																																															 .getProductionStartTime()
		);
		sendToAll(resourceProductionStarted);
	}

	private void finishProducing(Factory factory) {
		LOG.trace("Factory {} finished producing {}", factory.getId(), factory.getProducer().getResource());
		Producer producer = factory.getProducer();
		producer.stopProduction();
		Storage storage = factory.getStorage();
		storage.addResource(producer.getResource());
		sendToAll(new ResourceProduced(factory.getId(), producer.getResource()));
	}

	public void addSession(WebSocketSession session) {
		sessions.add(session);

		//send back all factories to the session
		List<Factory> factories = repository.getAll();
		LOG.info("Sending FactoryAdded message for {} factories", factories.size());
		for (Factory factory : factories) {
			FactoryAdded factoryAdded = new FactoryAdded(factoryConverter.convert(factory));
			sendTo(factoryAdded, session);
		}
	}

	public void removeSession(WebSocketSession session) {
		sessions.remove(session);
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
		for (WebSocketSession session : sessions) {
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
		startProducing(factory);
	}

}
