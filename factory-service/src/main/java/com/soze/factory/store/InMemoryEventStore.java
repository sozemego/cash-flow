package com.soze.factory.store;

import com.soze.factory.event.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.security.acl.LastOwnerException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Profile("memory-store")
public class InMemoryEventStore implements EventStore {

	private static final Logger LOG = LoggerFactory.getLogger(InMemoryEventStore.class);

	private final Map<String, List<Event>> events = new ConcurrentHashMap<>();

	@Override
	@EventListener
	public void handleEvent(Event event) {
		LOG.info("{}", event);
		String entityId = event.entityId;
		List<Event> entityEvents = events.computeIfAbsent(entityId, (key) -> new ArrayList<>());
		entityEvents.add(event);
	}

	@Override
	public List<Event> getEventsForEntity(String entityId) {
		return events.getOrDefault(entityId, new ArrayList<>());
	}

	@Override
	public int count() {
		return events.keySet().size();
	}

	@Override
	public List<String> getAllIds() {
		return new ArrayList<>(events.keySet());
	}
}
