package com.soze.factory.service;

import com.soze.clock.domain.Clock;
import com.soze.common.dto.CityDTO;
import com.soze.common.json.JsonUtils;
import com.soze.common.message.server.FactoryAdded;
import com.soze.common.message.server.ResourceProduced;
import com.soze.common.message.server.ResourceProductionStarted;
import com.soze.common.message.server.ServerMessage;
import com.soze.factory.FactoryConverter;
import com.soze.factory.domain.Factory;
import com.soze.factory.domain.Producer;
import com.soze.factory.domain.Storage;
import com.soze.factory.world.RemoteWorldService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class FactoryService {

	private static final Logger LOG = LoggerFactory.getLogger(FactoryService.class);

	private final FactoryTemplateLoader templateLoader;
	private final FactoryConverter factoryConverter;
	private final RemoteWorldService remoteWorldService;
	private final Clock clock;

	private final List<Factory> factories = new ArrayList<>();

	private final Set<WebSocketSession> sessions = new HashSet<>();

	private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

	@Autowired
	public FactoryService(FactoryTemplateLoader templateLoader, FactoryConverter factoryConverter,
												RemoteWorldService remoteWorldService, Clock clock
											 ) {
		this.templateLoader = templateLoader;
		this.factoryConverter = factoryConverter;
		this.remoteWorldService = remoteWorldService;
		this.clock = clock;
	}

	@PostConstruct
	public void setup() {
		CityDTO wroclaw = remoteWorldService.getCityByName("Wroclaw");
		CityDTO warsaw = remoteWorldService.getCityByName("Warsaw");

		Factory forester1 = templateLoader.constructFactoryByTemplateId("FORESTER");
		forester1.setCityId(wroclaw.id);
		addFactory(forester1);

		Factory forester2 = templateLoader.constructFactoryByTemplateId("FORESTER");
		forester2.setCityId(wroclaw.id);
		addFactory(forester2);

		Factory forester3 = templateLoader.constructFactoryByTemplateId("FORESTER");
		forester3.setCityId(warsaw.id);
		addFactory(forester3);

		LOG.info("Created {} factories", factories.size());
	}

	public void addFactory(Factory factory) {
		LOG.info("Adding factory {}", factory);
		validateFactory(factory);

		this.factories.add(factory);

		FactoryAdded factoryAdded = new FactoryAdded(this.factoryConverter.convert(factory));
		sendToAll(factoryAdded);

		startProducing(factory);
	}

	private void validateFactory(Factory factory) {
		if (factory.getId() == null) {
			throw new IllegalArgumentException("Factory cannot have null id");
		}
		if (factory.getCityId() == null) {
			throw new IllegalArgumentException("Factory cannot have null cityId");
		}
		if (factory.getProducer() == null) {
			throw new IllegalArgumentException("Factory cannot have null producer");
		}
		if (factory.getStorage() == null) {
			throw new IllegalArgumentException("Factory cannot have null storage");
		}
		Optional<Factory> previousFactory = this.factories.stream()
																											.filter(filteredFactory -> factory.getId().equals(filteredFactory.getId()))
																											.findFirst();

		if (previousFactory.isPresent()) {
			throw new IllegalArgumentException("Factory with id = " + factory.getId() + " already exists");
		}
	}

	private void startProducing(Factory factory) {
		Producer producer = factory.getProducer();
		if (producer.isProducing()) {
			return;
		}

		producer.startProduction();
		float timeRemaining = producer.getTime() - producer.getProgress();
		LOG.debug("Starting production, it will finish in {} ms", timeRemaining);
		executorService.schedule(() -> {
			finishProducing(factory);
			startProducing(factory);
		}, (long) timeRemaining, TimeUnit.MILLISECONDS);

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

	public List<Factory> getFactories() {
		return factories;
	}

	public void addSession(WebSocketSession session) {
		sessions.add(session);

		//send back all factories to the session
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

}
