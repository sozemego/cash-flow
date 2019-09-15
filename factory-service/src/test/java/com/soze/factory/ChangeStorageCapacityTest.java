package com.soze.factory;

import com.soze.common.message.server.StorageCapacityChanged;
import com.soze.factory.aggregate.Factory;
import com.soze.factory.aggregate.Storage;
import com.soze.factory.command.ChangeStorageCapacity;
import com.soze.factory.command.CreateFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.NoSuchElementException;
import java.util.UUID;

@SpringBootTest
@ActiveProfiles({"test", "memory-store"})
public class ChangeStorageCapacityTest extends CommandTest {

	@Test
	void changeStorageCapacity() {
		UUID factoryId = UUID.randomUUID();
		CreateFactory createFactory = new CreateFactory(factoryId, "Forester", "texture.png", "Warsaw");
		publish(createFactory);

		ChangeStorageCapacity command = new ChangeStorageCapacity(factoryId, 5);
		publish(command);
		Factory factory = getFactoryRepository().findById(factoryId).get();
		Storage storage = factory.getStorage();
		Assertions.assertEquals(5, storage.getCapacity());
		Assertions.assertTrue(getSession().getMessages().get(1) instanceof StorageCapacityChanged);
	}

	@Test
	public void changeStorageCapacity_factoryDoesNotExist() {
		UUID factoryId = UUID.randomUUID();
		ChangeStorageCapacity command = new ChangeStorageCapacity(factoryId, 5);
		Assertions.assertThrows(NoSuchElementException.class, () -> publish(command));
		Assertions.assertTrue(getSession().getMessages().isEmpty());
	}

}
