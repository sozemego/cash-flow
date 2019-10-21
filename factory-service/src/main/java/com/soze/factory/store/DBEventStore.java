package com.soze.factory.store;

import com.soze.factory.event.Event;
import com.soze.factory.event.EventUpcastService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Profile("database-store")
public class DBEventStore implements EventStore {

	private static final Logger LOG = LoggerFactory.getLogger(InMemoryEventStore.class);
	private final EventUpcastService upcaster;
	@PersistenceContext
	private EntityManager em;

	@Autowired
	public DBEventStore(EventUpcastService upcaster) {
		this.upcaster = upcaster;
	}

	@Override
	@Transactional
	public void handleEvent(Event event) {
		EventEntity eventEntity = new EventEntity();
		eventEntity.setId(UUID.randomUUID());
		eventEntity.setEvent(event);
		em.persist(eventEntity);
	}

	@Override
	public List<Event> getEventsForEntity(UUID entityId) {
		Query query = em.createQuery("SELECT e.event FROM EventEntity e");
		List<Event> events = query.getResultList();

		return events.stream()
								 .filter(event -> UUID.fromString(event.entityId).equals(entityId))
								 .map(upcaster::upcast)
								 .collect(Collectors.toList());
	}

	@Override
	public List<UUID> getAllIds() {
		Query query = em.createQuery("SELECT e.event FROM EventEntity e");
		List<Event> events = query.getResultList();
		return events.stream().map(event -> event.entityId).map(UUID::fromString).distinct().collect(Collectors.toList());
	}

	@Override
	public int count() {
		Query query = em.createQuery("SELECT e.event FROM EventEntity e");
		List<Event> events = query.getResultList();
		return events.size();
	}
}
