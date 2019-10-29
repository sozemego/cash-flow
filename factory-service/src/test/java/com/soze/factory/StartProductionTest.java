package com.soze.factory;

import com.soze.common.dto.Resource;
import com.soze.factory.command.BuyResource;
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

	@Test
	public void startProduction_noOutput() {
		UUID id = UUID.randomUUID();
		String templateId = "STONE_CRUSHER";
		List<Command> commands = getCommandsToCreate(id, templateId, "Warsaw");
		issueCommands(commands);

		BuyResource buyResource = new BuyResource(id, Resource.STONE, 10);
		issueCommand(buyResource);

		StartProduction startProduction = new StartProduction(id, 0);
		issueCommand(startProduction);

		printMessages();
		Assertions.assertEquals(Event.EventType.PRODUCTION_STARTED2, getSession().getMessage(5, ProductionStarted2.class).getType());
	}

}
