package com.soze.factory.service;

import com.soze.common.json.JsonUtils;
import com.soze.common.ws.factory.client.CreateFactory;
import com.soze.common.ws.factory.server.FactoryAdded;
import com.soze.common.ws.factory.server.ResourceProduced;
import com.soze.common.ws.factory.server.ResourceProductionStarted;
import com.soze.common.ws.factory.server.ServerMessage;
import com.soze.factory.FactoryConverter;
import com.soze.factory.domain.Factory;
import com.soze.factory.domain.Producer;
import com.soze.factory.domain.Storage;
import com.soze.factory.world.WorldService;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

@Service
public class FactoryService {

	private static final Logger LOG = LoggerFactory.getLogger(FactoryService.class);

	private final FactoryTemplateLoader templateLoader;
	private final FactoryConverter factoryConverter;
	private final WorldService worldService;

	private final List<Factory> factories = new ArrayList<>();

	private final Set<WebSocketSession> sessions = new HashSet<>();

	private final ScheduledExecutorService executorService = Executors
		.newSingleThreadScheduledExecutor();

	@Autowired
	public FactoryService(FactoryTemplateLoader templateLoader,
												FactoryConverter factoryConverter,
												WorldService worldService) {
		this.templateLoader = templateLoader;
		this.factoryConverter = factoryConverter;
		this.worldService = worldService;
	}

	@PostConstruct
	public void setup() {
		Factory forester1 = templateLoader.constructFactoryByTemplateId("FORESTER");
		forester1.setX(4);
		forester1.setY(4);
		addFactory(forester1);

		Factory forester2 = templateLoader.constructFactoryByTemplateId("FORESTER");
		forester2.setX(6);
		forester2.setY(6);
		addFactory(forester2);

		LOG.info("Created {} factories", factories.size());
	}

	public void addFactory(Factory factory) {
		LOG.info("Adding factory {}", factory);

		startProducing(factory);

		this.factories.add(factory);
		this.markAsTaken(factory.getX(), factory.getY());

		FactoryAdded factoryAdded = new FactoryAdded(factoryConverter.convert(factory));
		sendToAll(factoryAdded);
	}

	private void markAsTaken(int x, int y) {
		executorService.schedule(() -> {
			CircuitBreaker worldServiceBreaker = CircuitBreaker.ofDefaults("worldService");
			RetryConfig retryConfig = RetryConfig.custom()
																					 .maxAttempts(25)
																					 .waitDuration(Duration.ofMillis(500))
																					 .build();
			Retry retry = Retry.of("worldService", retryConfig);

			Runnable retryableRunnable = Retry.decorateRunnable(retry, () -> {
				worldService.markAsTaken(x, y);
			});

			Runnable runnable = CircuitBreaker.decorateRunnable(worldServiceBreaker, retryableRunnable);

			Try.runRunnable(runnable);
		}, 0, TimeUnit.MILLISECONDS);
	}

	private void startProducing(Factory factory) {
		Producer producer = factory.getProducer();
		if (producer.isProducing()) {
			return;
		}

		producer.startProduction();
		float timeRemaining = producer.getTime() - producer.getProgress();
		executorService.schedule(() -> {
			finishProducing(factory);
			startProducing(factory);
		}, (long) timeRemaining, TimeUnit.MILLISECONDS);

		ResourceProductionStarted resourceProductionStarted = new ResourceProductionStarted(
			factory.getId(), factory.getProducer().getResource());
		sendToAll(resourceProductionStarted);
	}

	private void finishProducing(Factory factory) {
		LOG.trace("Factory {} finished producing {}", factory.getId(),
							factory.getProducer().getResource()
						 );
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

	public void handleCreateFactory(CreateFactory createFactory) {
		int x = createFactory.getX();
		int y = createFactory.getY();
		boolean taken = worldService.isTileTaken(x, y);
		if (taken) {
			LOG.info("Tried to place a factory on a taken tile");
			return;
		}

		Factory factory = templateLoader.constructFactoryByTemplateId(createFactory.getTemplateId());
		factory.setX(x);
		factory.setY(y);
		executorService.schedule(() -> {
			addFactory(factory);
		}, 0, TimeUnit.MILLISECONDS);
	}

}
