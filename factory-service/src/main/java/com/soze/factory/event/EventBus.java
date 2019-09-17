package com.soze.factory.event;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventBus {

	private final ApplicationEventPublisher eventPublisher;

	public EventBus(ApplicationEventPublisher eventPublisher) {
		this.eventPublisher = eventPublisher;
	}

	public void publish(Object event) {
		eventPublisher.publishEvent(event);
	}

	public List<Event> publish(List<Event> events) {
		for (Event event : events) {
			publish(event);
		}
		return events;
	}
}
