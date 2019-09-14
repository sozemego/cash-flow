package com.soze.factory.store;

import com.soze.factory.event.Event;
import org.springframework.context.event.EventListener;

import java.util.List;
import java.util.UUID;

/**
 * Event store for factory service.
 * Used for Event Sourcing pattern.
 */
public interface EventStore {

	@EventListener
	void handleEvent(Event event);

	List<Event> getEventsForEntity(String entityId);

	List<String> getAllIds();

	int count();

}
