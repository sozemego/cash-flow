package com.soze.factory.service;

import com.soze.clock.domain.Clock;
import com.soze.common.message.server.FactoryAdded;
import com.soze.common.message.server.ResourceProductionStarted;
import com.soze.common.message.server.ServerMessage;
import com.soze.factory.FactoryConverter;
import com.soze.factory.domain.Factory;
import com.soze.factory.world.RemoteWorldService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
class FactoryServiceTest {

	private FactoryService factoryService;

	@Autowired
	private FactoryTemplateLoader factoryTemplateLoader;

	@Autowired
	private FactoryConverter factoryConverter;

	@Autowired
	private RemoteWorldService remoteWorldService;

	@BeforeEach
	public void setup() {
		this.factoryService = new FactoryService(
			factoryTemplateLoader, factoryConverter, remoteWorldService, new Clock(60, System.currentTimeMillis()));
	}

	@Test
	public void addFactory_isAdded() {
		Factory factory = this.factoryTemplateLoader.constructFactoryByTemplateId("FORESTER");
		factory.setCityId("cityId");
		this.factoryService.addFactory(factory);

		List<Factory> factories = this.factoryService.getFactories();
		Assertions.assertFalse(factories.isEmpty());
	}

	@Test
	public void addFactory_cityIdNull() {
		Factory factory = this.factoryTemplateLoader.constructFactoryByTemplateId("FORESTER");
		Assertions.assertThrows(IllegalArgumentException.class, () -> this.factoryService.addFactory(factory));
	}

	@Test
	public void addFactory_alreadyAdded() {
		Factory factory = this.factoryTemplateLoader.constructFactoryByTemplateId("FORESTER");
		factory.setCityId("CityID");
		this.factoryService.addFactory(factory);
		Assertions.assertThrows(IllegalArgumentException.class, () -> this.factoryService.addFactory(factory));
	}

	@Test
	public void addFactory_storageNull() {
		Factory factory = this.factoryTemplateLoader.constructFactoryByTemplateId("FORESTER");
		factory.setStorage(null);
		Assertions.assertThrows(IllegalArgumentException.class, () -> this.factoryService.addFactory(factory));
	}

	@Test
	public void addFactory_idNull() {
		Factory factory = this.factoryTemplateLoader.constructFactoryByTemplateId("FORESTER");
		factory.setId(null);
		Assertions.assertThrows(IllegalArgumentException.class, () -> this.factoryService.addFactory(factory));
	}

	@Test
	public void addFactory_producerNull() {
		Factory factory = this.factoryTemplateLoader.constructFactoryByTemplateId("FORESTER");
		factory.setProducer(null);
		Assertions.assertThrows(IllegalArgumentException.class, () -> this.factoryService.addFactory(factory));
	}

	@Test
	public void addFactory_messagesSent() {
		Factory factory = this.factoryTemplateLoader.constructFactoryByTemplateId("FORESTER");
		factory.setCityId("cityId");

		TestWebSocketSession session = new TestWebSocketSession();
		this.factoryService.addSession(session);
		this.factoryService.addFactory(factory);

		List<ServerMessage> messages = session.getMessages();
		Assertions.assertFalse(messages.isEmpty());
		FactoryAdded factoryAdded = (FactoryAdded) messages.get(0);
		Assertions.assertNotNull(factoryAdded);

		ResourceProductionStarted productionStarted = (ResourceProductionStarted) messages.get(1);
		Assertions.assertNotNull(productionStarted);
	}

	@Test
	public void productionTasksScheduled() throws InterruptedException {
		TestWebSocketSession session = new TestWebSocketSession();
		this.factoryService.addSession(session);

		Factory factory = this.factoryTemplateLoader.constructFactoryByTemplateId("FORESTER");
		factory.setCityId("cityId");
		factory.getProducer().setTime(2);
		this.factoryService.addFactory(factory);

		sleep(2000);

		List<ServerMessage> messages = session.getMessages();
		Assertions.assertEquals(4, messages.size());
		Assertions.assertEquals(messages.get(0).getType(), ServerMessage.ServerMessageType.FACTORY_ADDED.name());
		Assertions.assertEquals(
			messages.get(1).getType(), ServerMessage.ServerMessageType.RESOURCE_PRODUCTION_STARTED.name());
		Assertions.assertEquals(messages.get(2).getType(), ServerMessage.ServerMessageType.RESOURCE_PRODUCED.name());
		Assertions.assertEquals(
			messages.get(3).getType(), ServerMessage.ServerMessageType.RESOURCE_PRODUCTION_STARTED.name());
	}

	@Test
	public void addFactory_nullFactory() {
		Assertions.assertThrows(NullPointerException.class, () -> this.factoryService.addFactory(null));
	}

	private void sleep(long ms) throws InterruptedException {
		long interval = 100;
		long time = 0;
		while (time <= ms) {
			Thread.sleep(interval);
			time += interval;
		}
	}
}