package com.soze.factory;

import com.soze.common.message.server.FactoryAdded;
import com.soze.factory.command.CreateFactory;
import com.soze.factory.event.FactoryCreated;
import com.soze.factory.service.SocketSessionContainer;
import com.soze.factory.service.TestWebSocketSession;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;


@SpringBootTest
class CreateFactoryTest extends CommandTest {

	@Autowired
	private SocketSessionContainer socketSessionContainer;

	private TestWebSocketSession session;

	@BeforeEach
	public void setup() {
		getEvents().clear();
		session = new TestWebSocketSession();
		socketSessionContainer.addSession(session);
	}

	@Test
	void createFactory() {
		UUID factoryId = UUID.randomUUID();
		CreateFactory createFactory = new CreateFactory(factoryId, "Forester", "texture.png", "Warsaw");
		issueCommand(createFactory);
		Assertions.assertEquals(1, getEvents().size());
		Assertions.assertTrue(getEvents().get(0) instanceof FactoryCreated);
		Assertions.assertTrue(getFactoryRepository().findById(factoryId).isPresent());
		Assertions.assertTrue(session.getMessage(0, FactoryAdded.class) != null);
	}

	@Test
	public void createFactory_alreadyExists() {
		UUID factoryId = UUID.randomUUID();
		CreateFactory createFactory = new CreateFactory(factoryId, "Forester", "texture.png", "Warsaw");
		issueCommand(createFactory);
		Assertions.assertEquals(1, getEvents().size());
		Assertions.assertTrue(getEvents().get(0) instanceof FactoryCreated);
		Assertions.assertTrue(getFactoryRepository().findById(factoryId).isPresent());
		Assertions.assertTrue(session.getMessage(0, FactoryAdded.class) != null);
		getEvents().clear();
		Assertions.assertThrows(IllegalStateException.class, () -> issueCommand(createFactory));
	}

	@Test
	public void createFactory_cityDoesNotExist() {
		UUID id = UUID.randomUUID();
		Assertions.assertThrows(
			IllegalArgumentException.class, () -> issueCommand(new CreateFactory(id, "Factory", ".png", "helloId")));
	}
}