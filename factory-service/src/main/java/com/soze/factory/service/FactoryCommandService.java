package com.soze.factory.service;

import com.soze.common.dto.CityDTO;
import com.soze.factory.command.CommandVisitor;
import com.soze.factory.command.CreateFactory;
import com.soze.factory.command.StartProduction;
import com.soze.factory.event.FactoryCreated;
import com.soze.factory.repository.FactoryRepository;
import com.soze.factory.world.RemoteWorldService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * Service that can handle Command objects.
 */
@Service
public class FactoryCommandService implements CommandVisitor {

	private static final Logger LOG = LoggerFactory.getLogger(FactoryCommandService.class);

	private final FactoryRepository repository;
	private final ApplicationEventPublisher eventPublisher;
	private final RemoteWorldService worldService;

	@Autowired
	public FactoryCommandService(FactoryRepository repository, ApplicationEventPublisher eventPublisher,
															 RemoteWorldService worldService
															) {
		this.repository = repository;
		this.eventPublisher = eventPublisher;
		this.worldService = worldService;
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

	}
}
