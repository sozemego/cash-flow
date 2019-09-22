package com.soze.factory.service;

import com.soze.common.dto.CityDTO;
import com.soze.factory.aggregate.Factory;
import com.soze.factory.command.*;
import com.soze.factory.event.Event;
import com.soze.factory.event.EventBus;
import com.soze.factory.repository.FactoryRepository;
import com.soze.factory.world.RemoteWorldService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

/**
 * Service that can handle Command objects.
 */
@Service
public class FactoryCommandService implements CommandVisitor {

	private static final Logger LOG = LoggerFactory.getLogger(FactoryCommandService.class);

	private final FactoryRepository repository;
	private final RemoteWorldService worldService;
	private final EventBus eventBus;

	@Autowired
	public FactoryCommandService(FactoryRepository repository, RemoteWorldService worldService, EventBus eventBus
															) {
		this.repository = repository;
		this.worldService = worldService;
		this.eventBus = eventBus;
	}

	@Override
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
		return eventBus.publish(factory.visit(createFactory));
	}

	@Override
	public List<Event> visit(StartProduction startProduction) {
		LOG.info("{}", startProduction);
		Factory factory = getFactory(startProduction.getFactoryId());
		return eventBus.publish(factory.visit(startProduction));
	}

	@Override
	public List<Event> visit(ChangeStorageCapacity changeStorageCapacity) {
		LOG.info("{}", changeStorageCapacity);
		Factory factory = getFactory(changeStorageCapacity.getFactoryId());
		return eventBus.publish(factory.visit(changeStorageCapacity));
	}

	@Override
	public List<Event> visit(AddProductionLine addProductionLine) {
		LOG.info("{}", addProductionLine);
		Factory factory = getFactory(addProductionLine.getFactoryId());
		return eventBus.publish(factory.visit(addProductionLine));
	}

	@Override
	public List<Event> visit(FinishProduction finishProduction) {
		LOG.info("{}", finishProduction);
		Factory factory = getFactory(finishProduction.getFactoryId());
		return new ArrayList<>();
	}

	private Factory getFactory(String id) {
		return repository.findById(UUID.fromString(id)).orElseThrow(
			() -> new NoSuchElementException("Factory with id = " + id + " does not exist"));
	}

	private Factory getFactory(UUID id) {
		return getFactory(id.toString());
	}
}
