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
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
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
		Assertions.assertTrue(session.getMessages().get(0) instanceof FactoryAdded);
	}

	@Test
	public void createFactory_alreadyExists() {
		UUID factoryId = UUID.randomUUID();
		CreateFactory createFactory = new CreateFactory(factoryId, "Forester", "texture.png", "Warsaw");
		issueCommand(createFactory);
		Assertions.assertEquals(1, getEvents().size());
		Assertions.assertTrue(getEvents().get(0) instanceof FactoryCreated);
		Assertions.assertTrue(getFactoryRepository().findById(factoryId).isPresent());
		Assertions.assertTrue(session.getMessages().get(0) instanceof FactoryAdded);
		getEvents().clear();
		Assertions.assertThrows(IllegalStateException.class, () -> issueCommand(createFactory));
	}


}