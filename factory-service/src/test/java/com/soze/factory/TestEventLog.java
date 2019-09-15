package com.soze.factory;

import com.soze.factory.event.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TestEventLog {

	private static final Logger LOG = LoggerFactory.getLogger(TestEventLog.class);

	private final List<Event> events = new ArrayList<>();

	@EventListener
	public void handle(Event e) {
		LOG.info("{}", e);
		events.add(e);
	}

	public List<Event> getEvents() {
		return events;
	}

}
