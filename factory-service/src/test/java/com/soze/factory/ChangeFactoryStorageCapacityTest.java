package com.soze.factory;

import com.soze.common.dto.Resource;
import com.soze.common.message.server.StorageCapacityChanged;
import com.soze.factory.aggregate.Factory;
import com.soze.factory.aggregate.FactoryStorage;
import com.soze.factory.command.ChangeStorageCapacity;
import com.soze.factory.command.CreateFactory;
import jdk.nashorn.internal.ir.annotations.Ignore;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.NoSuchElementException;
import java.util.UUID;

@SpringBootTest
public class ChangeFactoryStorageCapacityTest extends CommandTest {

	@Test
	void changeStorageCapacity() {
		UUID factoryId = UUID.randomUUID();
		CreateFactory createFactory = new CreateFactory(factoryId, "Forester", "texture.png", "Warsaw");
		issueCommand(createFactory);

		ChangeStorageCapacity command = new ChangeStorageCapacity(factoryId, 5);
		issueCommand(command);
		Factory factory = getFactoryRepository().findById(factoryId).get();
		FactoryStorage storage = factory.getStorage();
		Assertions.assertEquals(5, storage.getCapacity(Resource.WOOD));
		Assertions.assertTrue(getSession().getMessages().get(1) instanceof StorageCapacityChanged);
	}

	@Test
	public void changeStorageCapacity_factoryDoesNotExist() {
		UUID factoryId = UUID.randomUUID();
		ChangeStorageCapacity command = new ChangeStorageCapacity(factoryId, 5);
		Assertions.assertThrows(NoSuchElementException.class, () -> issueCommand(command));
		Assertions.assertTrue(getSession().getMessages().isEmpty());
	}

}
