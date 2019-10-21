package com.soze.factory.store;

import com.soze.factory.event.Event;
import org.springframework.context.event.EventListener;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import java.util.List;
import java.util.UUID;

/**
 * Event store for factory service.
 * Used for Event Sourcing pattern.
 */
public interface EventStore {

	@EventListener
	@Order(value = Ordered.HIGHEST_PRECEDENCE)
	void handleEvent(Event event);

	List<Event> getEventsForEntity(UUID entityId);

	List<UUID> getAllIds();

	int count();

}
