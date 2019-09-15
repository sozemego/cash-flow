package com.soze.factory.service;

import com.soze.common.dto.CityDTO;
import com.soze.common.dto.Clock;
import com.soze.factory.aggregate.Factory;
import com.soze.factory.aggregate.Producer;
import com.soze.factory.aggregate.Storage;
import com.soze.factory.command.CommandVisitor;
import com.soze.factory.command.CreateFactory;
import com.soze.factory.command.FinishProduction;
import com.soze.factory.command.StartProduction;
import com.soze.factory.event.FactoryCreated;
import com.soze.factory.event.ProductionStarted;
import com.soze.factory.repository.FactoryRepository;
import com.soze.factory.world.RemoteWorldService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Service that can handle Command objects.
 */
@Service
public class FactoryCommandService implements CommandVisitor {

	private static final Logger LOG = LoggerFactory.getLogger(FactoryCommandService.class);

	private final FactoryRepository repository;
	private final ApplicationEventPublisher eventPublisher;
	private final RemoteWorldService worldService;
	private final Clock clock;

	@Autowired
	public FactoryCommandService(FactoryRepository repository, ApplicationEventPublisher eventPublisher,
															 RemoteWorldService worldService, Clock clock
															) {
		this.repository = repository;
		this.eventPublisher = eventPublisher;
		this.worldService = worldService;
		this.clock = clock;
	}

	@Override
	public void visit(CreateFactory createFactory) {
		LOG.info("handling createFactory = {}", createFactory);
		if (repository.findById(createFactory.getFactoryId()).isPresent()) {
			throw new IllegalStateException("Factory with id = " + createFactory.getFactoryId() + " already exists");
		}

		CityDTO city = worldService.getCityById(createFactory.getCityId());
		if (city == null) {
			throw new IllegalArgumentException("City with id = " + createFactory.getCityId() + " does not exist");
		}

		eventPublisher.publishEvent(
			new FactoryCreated(createFactory.getFactoryId().toString(), LocalDateTime.now(), 1, createFactory.getName(),
												 createFactory.getTexture(), createFactory.getCityId()
			));
	}

	@Override
	public void visit(StartProduction startProduction) {
		LOG.info("Handling {}", startProduction);
		Factory factory = getFactory(startProduction.getFactoryId());

		Producer producer = factory.getProducer();
		if (producer.isProducing()) {
			return;
		}
		Storage storage = factory.getStorage();
		if (storage.isFull()) {
			LOG.info("Factory {} is full", factory.getId());
			return;
		}

		long minutes = producer.getTime();
		long timeRemaining = TimeUnit.MINUTES.toMillis(minutes) / clock.getMultiplier();
		LOG.info("Started production of {} at factory = {}, it will finish in {} ms", producer.getResource(),
						 factory.getId(), timeRemaining
						);

		eventPublisher.publishEvent(
			new ProductionStarted(startProduction.getFactoryId(), LocalDateTime.now(), 1, producer.getResource(),
														producer.getProductionStartTime()
			));
	}

	@Override
	public void visit(FinishProduction finishProduction) {
		LOG.info("Handling {}", finishProduction);
		Factory factory = getFactory(finishProduction.getFactoryId());

	}

	private Factory getFactory(String id) {
		return repository.findById(UUID.fromString(id)).orElseThrow(
			() -> new IllegalArgumentException("Factory with id = " + id + " does not exist"));
	}
}
