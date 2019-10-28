package com.soze.factory;

import com.soze.factory.command.Command;
import com.soze.factory.event.Event;
import com.soze.factory.repository.FactoryRepository;
import com.soze.factory.service.FactoryCommandService;
import com.soze.factory.service.FactoryTemplateLoader;
import com.soze.factory.service.SocketSessionContainer;
import com.soze.factory.service.TestWebSocketSession;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.UUID;

@SpringBootTest
@ActiveProfiles(value = {"test", "database-store"}, inheritProfiles = false)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class CommandTest {

	@Autowired
	private FactoryRepository factoryRepository;

	@Autowired
	private ApplicationEventPublisher eventPublisher;

	@Autowired
	private TestEventLog eventLog;

	@Autowired
	private SocketSessionContainer socketSessionContainer;

	private TestWebSocketSession session;

	@Autowired
	private FactoryCommandService factoryCommandService;

	@Autowired
	private FactoryTemplateLoader factoryTemplateLoader;

	@BeforeEach
	public void setup() {
		getEvents().clear();
		session = new TestWebSocketSession();
		socketSessionContainer.addSession(session);
	}

	public FactoryRepository getFactoryRepository() {
		return factoryRepository;
	}

	void publish(Object event) {
		eventPublisher.publishEvent(event);
	}

	public ApplicationEventPublisher getEventPublisher() {
		return eventPublisher;
	}

	List<Event> getEvents() {
		return eventLog.getEvents();
	}

	public TestWebSocketSession getSession() {
		return session;
	}

	public void issueCommand(Command command) {
		command.accept(factoryCommandService);
	}

	public void issueCommands(List<Command> commands) {
		commands.forEach(this::issueCommand);
	}

	public List<Command> getCommandsToCreate(UUID id, String templateId, String cityId) {
		return factoryTemplateLoader.getFactoryCommandsByTemplateId(id, templateId, cityId);
	}

	public void printMessages() {
		getSession().getMessages().forEach(System.out::println);
	}
}
