package com.soze.factory.store;

import com.soze.factory.event.Event;
import com.soze.factory.event.EventUpcastService;
import com.soze.factory.repository.EventCrudRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.soze.common.CollectionsUtils.fromIterable;

@Service
@Profile("database-store")
public class DBEventStore implements EventStore {

	private static final Logger LOG = LoggerFactory.getLogger(DBEventStore.class);
	private final EventUpcastService upcaster;
	private final EventCrudRepository repository;

	@Autowired
	public DBEventStore(EventUpcastService upcaster, EventCrudRepository repository) {
		this.upcaster = upcaster;
		this.repository = repository;
	}

	@Override
	@Transactional
	public void handleEvent(Event event) {
		LOG.info("{}", event);
		EventEntity eventEntity = new EventEntity();
		eventEntity.setId(UUID.randomUUID());
		eventEntity.setEvent(event);
		repository.save(eventEntity);
	}

	@Override
	public List<Event> getEventsForEntity(UUID entityId) {
		List<EventEntity> eventEntities = repository.getByEntityId(entityId.toString());
		return eventEntities.stream()
												.map(EventEntity::getEvent)
												.map(upcaster::upcast)
												.collect(Collectors.toList());
	}

	@Override
	public List<UUID> getAllIds() {
		List<EventEntity> entities = fromIterable(repository.findAll());
		return entities.stream()
									 .map(EventEntity::getEvent)
									 .map(event -> event.entityId)
									 .map(UUID::fromString).distinct()
									 .collect(Collectors.toList());
	}

	@Override
	public int count() {
		return (int) repository.count();
	}
}
