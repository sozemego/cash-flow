package com.soze.factory;

import com.soze.factory.event.Event;
import com.soze.factory.repository.FactoryRepository;
import com.soze.factory.service.SocketSessionContainer;
import com.soze.factory.service.TestWebSocketSession;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

@SpringBootTest
@ActiveProfiles({"test", "memory-store"})
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
}
