package com.soze.factory;

import com.soze.factory.command.CreateFactory;
import com.soze.factory.event.Event;
import com.soze.factory.event.FactoryCreated;
import com.soze.factory.repository.FactoryRepository;
import com.soze.factory.service.FactoryCommandService;
import com.soze.factory.world.RemoteWorldService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.UUID;


@SpringBootTest()
@ActiveProfiles({"test", "memory-store"})
@Configuration
class CreateFactoryTest {

	@Autowired
	private FactoryRepository factoryRepository;

	@Autowired
	private RemoteWorldService remoteWorldService;

	@Autowired
	private ApplicationEventPublisher eventPublisher;

	private FactoryCommandService factoryCommandService;

	@Autowired
	private TestEventLog eventLog;

	@BeforeEach
	public void setup() {
		getEvents().clear();
		factoryCommandService = new FactoryCommandService(factoryRepository, remoteWorldService);
	}

	@Test
	void createFactory() {
		UUID factoryId = UUID.randomUUID();
		CreateFactory createFactory = new CreateFactory(factoryId, "Forester", "texture.png", "Warsaw");
		eventPublisher.publishEvent(createFactory);
		Assertions.assertEquals(1, eventLog.getEvents().size());
		Assertions.assertTrue(eventLog.getEvents().get(0) instanceof FactoryCreated);
		Assertions.assertTrue(factoryRepository.findById(factoryId).isPresent());
	}

	@Test
	public void createFactory_alreadyExists() {
		UUID factoryId = UUID.randomUUID();
		CreateFactory createFactory = new CreateFactory(factoryId, "Forester", "texture.png", "Warsaw");
		eventPublisher.publishEvent(createFactory);
		Assertions.assertEquals(1, getEvents().size());
		Assertions.assertTrue(getEvents().get(0) instanceof FactoryCreated);
		Assertions.assertTrue(factoryRepository.findById(factoryId).isPresent());
		getEvents().clear();
		Assertions.assertThrows(IllegalStateException.class, () -> eventPublisher.publishEvent(createFactory));
	}

	private List<Event> getEvents() {
		return eventLog.getEvents();
	}

}