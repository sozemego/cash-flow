package com.soze.factory;

import com.soze.factory.command.Command;
import com.soze.factory.command.CreateFactory;
import com.soze.factory.command.StartProduction;
import com.soze.factory.event.Event;
import com.soze.factory.event.ProductionStarted2;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class StartProductionTest extends CommandTest {

	@Test
	public void startProduction_noFactory() {
		StartProduction startProduction = new StartProduction(UUID.randomUUID(), 0);
		Assertions.assertThrows(NoSuchElementException.class, () -> issueCommand(startProduction));
	}

	@Test
	public void startProduction_noProduction() {
		UUID id = UUID.randomUUID();
		issueCommand(new CreateFactory(id, "Factory", ".png", "Warsaw"));

		StartProduction startProduction = new StartProduction(id, 0);
		issueCommand(startProduction);

		Assertions.assertEquals(1, getSession().getMessages().size());
	}

	@Test
	public void startProduction() {
		UUID id = UUID.randomUUID();
		String templateId = "FORESTER";
		List<Command> commands = getCommandsToCreate(id, templateId, "Warsaw");
		issueCommands(commands);

		StartProduction startProduction = new StartProduction(id, 0);
		issueCommand(startProduction);

		Assertions.assertEquals(4, getSession().getMessages().size());
		Assertions.assertEquals(Event.EventType.PRODUCTION_STARTED2, getSession().getMessage(3, ProductionStarted2.class).getType());
	}

}
