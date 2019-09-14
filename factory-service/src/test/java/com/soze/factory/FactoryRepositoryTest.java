package com.soze.factory;

import com.soze.factory.aggregate.Factory;
import com.soze.factory.event.FactoryCreated;
import com.soze.factory.repository.FactoryRepository;
import com.soze.factory.store.EventStore;
import com.soze.factory.store.InMemoryEventStore;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;


class FactoryRepositoryTest {

	private FactoryRepository factoryRepository;
	private EventStore eventStore;

	@BeforeEach
	public void setup() {
		eventStore = new InMemoryEventStore();
		factoryRepository = new FactoryRepository(eventStore);
	}

	@Test
	public void getFactoryById() {
		UUID factoryId = UUID.randomUUID();
		FactoryCreated factoryCreated = new FactoryCreated(
			factoryId.toString(), LocalDateTime.now(), 1, "forester", "png", "templateId", "wroclaw");
		eventStore.handleEvent(factoryCreated);
		Optional<Factory> factoryOptional = factoryRepository.findById(factoryId);
		Assertions.assertTrue(factoryOptional.isPresent());
	}


}