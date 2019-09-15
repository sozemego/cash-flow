package com.soze.factory.service;

import com.soze.common.dto.Clock;
import com.soze.factory.aggregate.Factory;
import com.soze.factory.command.FinishProduction;
import com.soze.factory.event.ProductionStarted;
import com.soze.factory.repository.FactoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Starts timers that are supposed to trigger some actions when production is supposed to end.
 */
@Service
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

	@EventListener
	public void handleProductionStarted(ProductionStarted productionStarted) {
//		LOG.info("{}", productionStarted);
//		Factory factory = repository.findById(UUID.fromString(productionStarted.getEntityId())).get();
//		long minutes = factory.getProducer().getTime();
//		long timeRemaining = TimeUnit.MINUTES.toMillis(minutes) / clock.getMultiplier();
//		executorService.schedule(() -> {
//			commandService.visit(new FinishProduction(productionStarted.getEntityId()));
//		}, timeRemaining, TimeUnit.MILLISECONDS);
	}

}
