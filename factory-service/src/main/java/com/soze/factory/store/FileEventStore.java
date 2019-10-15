package com.soze.factory.store;

import com.soze.common.json.JsonUtils;
import com.soze.factory.event.Event;
import com.soze.factory.event.EventUpcastService;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
@Profile("file-store")
public class FileEventStore implements EventStore {

	private static final Logger LOG = LoggerFactory.getLogger(FileEventStore.class);

	private static final String FILE = "factory-store.json";

	private final Map<String, List<Event>> events = new ConcurrentHashMap<>();

	private final EventUpcastService upcaster;

	@Autowired
	public FileEventStore(EventUpcastService upcaster) {
		this.upcaster = upcaster;
	}

	@PostConstruct
	public void setup() {
		LOG.info("FileEventStore init");
		File file = FileUtils.getFile(FILE);
		List<Event> eventList = JsonUtils.parseList(file, Event.class);
		LOG.info("Loaded {} events", eventList.size());
		for (Event event : eventList) {
			Event upcastEvent = upcaster.upcast(event);
			LOG.trace("Storing event in memory = {}", upcastEvent);
			List<Event> entityEvents = events.computeIfAbsent(upcastEvent.entityId, (id) -> new ArrayList<>());
			entityEvents.add(upcastEvent);
		}
	}

	@Override
	public void handleEvent(Event event) {
		LOG.info("Handling event = {}", event);
		List<Event> entityEvents = events.computeIfAbsent(event.entityId, (id) -> new ArrayList<>());
		entityEvents.add(event);
		persistEvents();
	}

	@Override
	public List<Event> getEventsForEntity(String entityId) {
		return events.getOrDefault(entityId, new ArrayList<>());
	}

	@Override
	public List<String> getAllIds() {
		return new ArrayList<>(events.keySet());
	}

	@Override
	public int count() {
		return events.keySet().size();
	}

	private void persistEvents() {
		List<Event> allEvents = events.values()
																	.stream()
																	.flatMap(Collection::stream)
																	.collect(Collectors.toList());
		LOG.info("Writing {} events to file", allEvents.size());
		String json = JsonUtils.serialize(allEvents);
		try {
			FileUtils.write(FileUtils.getFile(FILE), json);
		} catch (IOException e) {
			LOG.warn("Cannot persist events", e);
		}
		LOG.info("Persisted events");
	}
}
