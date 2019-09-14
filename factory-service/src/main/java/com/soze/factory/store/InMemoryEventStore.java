package com.soze.factory.store;

import com.soze.factory.event.Event;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Profile("memory-store")
public class InMemoryEventStore implements EventStore {

	private final Map<String, List<Event>> events = new ConcurrentHashMap<>();

	@Override
	@EventListener
	public void handleEvent(Event event) {
		String entityId = event.getEntityId();
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

}
