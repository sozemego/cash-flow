package com.soze.factory.service;

import com.soze.common.dto.Clock;
import com.soze.factory.aggregate.Factory;
import com.soze.factory.aggregate.Producer;
import com.soze.factory.aggregate.Storage;
import com.soze.factory.command.FinishProduction;
import com.soze.factory.command.StartProduction;
import com.soze.factory.event.ProductionStarted;
import com.soze.factory.repository.FactoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Starts timers that are supposed to trigger some actions when production ends.
 */
@Service
@Profile("!test")
public class FactoryProductionScheduler {

	private static final Logger LOG = LoggerFactory.getLogger(FactoryProductionScheduler.class);

	private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

	private final FactoryCommandService commandService;
	private final FactoryRepository repository;
	private final Clock clock;

	@Autowired
	public FactoryProductionScheduler(FactoryCommandService commandService, FactoryRepository repository, Clock clock
																	 ) {
		this.commandService = commandService;
		this.repository = repository;
		this.clock = clock;
	}

	/**
	 * Every X seconds checks all factories if they are producing. If not
	 * sends a {@link com.soze.factory.command.StartProduction} command.
	 */
	@Scheduled(fixedDelay = 5000)
	public void checkFactories() {
		LOG.debug("Checking for idle factories");
		repository.getAll().forEach(factory -> {
			Producer producer = factory.getProducer();
			if (producer.isProducing()) {
				LOG.trace("Factory {} already producing", factory.getId());
				return;
			}
			Storage storage = factory.getStorage();
			if (storage.isFull()) {
				LOG.trace("Factory {} is full", factory.getId());
				return;
			}
			commandService.visit(new StartProduction(factory.getId(), clock.getCurrentGameTime()));
		});
	}

	/**
	 * Checks if factory finished producing.
	 */
	@Scheduled(fixedDelay = 5000)
	public void checkFactoriesFinished() {
		LOG.debug("Checking for factories which finished production");
		repository.getAll().forEach(factory -> {
			Producer producer = factory.getProducer();
			if (!producer.isFinished(clock)) {
				LOG.trace("Factory {} still producing", factory.getId());
				return;
			}
			commandService.visit(new FinishProduction(factory.getId()));
		});
	}

	@EventListener
	public void handleProductionStarted(ProductionStarted productionStarted) {
		LOG.info("{}", productionStarted);
		Factory factory = repository.findById(UUID.fromString(productionStarted.entityId)).get();
		long minutes = factory.getProducer().getTime();
		long timeRemaining = TimeUnit.MINUTES.toMillis(minutes) / clock.getMultiplier();
		executorService.schedule(() -> {
			commandService.visit(new FinishProduction(UUID.fromString(productionStarted.entityId)));
		}, timeRemaining, TimeUnit.MILLISECONDS);
	}

}
