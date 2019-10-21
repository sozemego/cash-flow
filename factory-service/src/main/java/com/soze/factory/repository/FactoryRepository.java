package com.soze.factory.repository;

import com.soze.factory.aggregate.Factory;
import com.soze.factory.event.Event;
import com.soze.factory.event.FactoryCreated;
import com.soze.factory.store.EventStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class FactoryRepository {

	private static final Logger LOG = LoggerFactory.getLogger(FactoryRepository.class);

	private final EventStore eventStore;

	@Autowired
	public FactoryRepository(EventStore eventStore) {
		this.eventStore = eventStore;
	}

	public Optional<Factory> findById(UUID factoryId) {
		LOG.trace("Searching for {}", factoryId);
		List<Event> events = eventStore.getEventsForEntity(factoryId);
		if (events.isEmpty()) {
			return Optional.empty();
		}
		FactoryCreated factoryCreated = (FactoryCreated) events.get(0);
		Factory factory = new Factory(factoryCreated);

		for (int i = 1; i < events.size(); i++) {
			Event event = events.get(i);
			LOG.trace("Applying event = {}", event);
			event.accept(factory);
		}

		return Optional.of(factory);
	}

	public int factoryCount() {
		return eventStore.count();
	}

	public List<Factory> getAll() {
		return eventStore.getAllIds()
										 .stream()
										 .map(this::findById)
										 .filter(Optional::isPresent)
										 .map(Optional::get)
										 .collect(Collectors.toList());
	}
}
