package com.soze.factory.repository;

import com.soze.factory.aggregate.Factory;
import com.soze.factory.event.Event;
import com.soze.factory.event.FactoryCreated;
import com.soze.factory.store.EventStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class FactoryRepository {

	private static final Logger LOG = LoggerFactory.getLogger(FactoryRepository.class);

	private final EventStore eventStore;

	@Autowired
	public FactoryRepository(EventStore eventStore) {
		this.eventStore = eventStore;
	}

	public Optional<Factory> findById(UUID factoryId) {
		List<Event> events = eventStore.getEventsForEntity(factoryId.toString());
		if (events.isEmpty()) {
			return Optional.empty();
		}
		FactoryCreated factoryCreated = (FactoryCreated) events.get(0);
		Factory factory = new Factory(factoryCreated);

		for (int i = 1; i < events.size(); i++) {
			Event event = events.get(i);
			LOG.trace("Applying event = {}", event);
			factory.accept(event);
		}

		return Optional.of(factory);
	}

	public int factoryCount() {
		return eventStore.count();
	}
}
