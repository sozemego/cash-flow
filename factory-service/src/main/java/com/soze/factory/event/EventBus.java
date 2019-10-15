package com.soze.factory.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EventBus {

	private final ApplicationEventPublisher eventPublisher;
	private final EventUpcastService upcastService;

	@Autowired
	public EventBus(ApplicationEventPublisher eventPublisher, EventUpcastService upcastService) {
		this.eventPublisher = eventPublisher;
		this.upcastService = upcastService;
	}

	public void publish(Object event) {
		eventPublisher.publishEvent(event);
	}

	public List<Event> publish(List<Event> events) {
		List<Event> upcastEvents = new ArrayList<>();
		for (Event event : events) {
			upcastEvents.add(upcastService.upcast(event));
		}
		for (Event event : upcastEvents) {
			publish(upcastService.upcast(event));
		}
		return upcastEvents;
	}
}
