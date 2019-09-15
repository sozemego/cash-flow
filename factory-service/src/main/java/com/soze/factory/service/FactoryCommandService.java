package com.soze.factory.service;

import com.soze.common.dto.CityDTO;
import com.soze.common.dto.Clock;
import com.soze.factory.aggregate.Factory;
import com.soze.factory.command.*;
import com.soze.factory.event.Event;
import com.soze.factory.repository.FactoryRepository;
import com.soze.factory.world.RemoteWorldService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Service that can handle Command objects.
 */
@Service
public class FactoryCommandService implements CommandVisitor {

	private static final Logger LOG = LoggerFactory.getLogger(FactoryCommandService.class);

	private final FactoryRepository repository;
	private final RemoteWorldService worldService;

	@Autowired
	public FactoryCommandService(FactoryRepository repository,
															 RemoteWorldService worldService
															) {
		this.repository = repository;
		this.worldService = worldService;
	}

	@Override
	@EventListener
	public List<Event> visit(CreateFactory createFactory) {
		LOG.info("{}", createFactory);
		if (repository.findById(createFactory.getFactoryId()).isPresent()) {
			throw new IllegalStateException("Factory with id = " + createFactory.getFactoryId() + " already exists");
		}

		CityDTO city = worldService.getCityById(createFactory.getCityId());
		if (city == null) {
			throw new IllegalArgumentException("City with id = " + createFactory.getCityId() + " does not exist");
		}
		Factory factory = new Factory();
		return factory.visit(createFactory);
	}

	@Override
	@EventListener
	public List<Event> visit(StartProduction startProduction) {
		LOG.info("{}", startProduction);
		Factory factory = getFactory(startProduction.getFactoryId());
		return factory.visit(startProduction);
	}

	@Override
	@EventListener
	public List<Event> visit(ChangeStorageCapacity changeStorageCapacity) {
		LOG.info("{}", changeStorageCapacity);
		Factory factory = getFactory(changeStorageCapacity.getFactoryId());
		return factory.visit(changeStorageCapacity);
	}

	@Override
	@EventListener
	public List<Event> visit(FinishProduction finishProduction) {
		LOG.info("{}", finishProduction);
		Factory factory = getFactory(finishProduction.getFactoryId());
		return new ArrayList<>();
	}

	private Factory getFactory(String id) {
		return repository.findById(UUID.fromString(id)).orElseThrow(
			() -> new IllegalArgumentException("Factory with id = " + id + " does not exist"));
	}

	private Factory getFactory(UUID id) {
		return getFactory(id.toString());
	}
}
