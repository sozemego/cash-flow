package com.soze.factory.aggregate;

import com.soze.common.dto.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashMap;
import java.util.Map;

@ActiveProfiles({"test", "memory-store"})
class FactoryStorageTest {

	@Test
	public void createStorage_oneResource() {
		Map<Resource, StorageSlot> resources = new HashMap<>();
		resources.put(Resource.WOOD, new StorageSlot(0, 5, 0));
		FactoryStorage factoryStorage = new FactoryStorage(resources);

		Assertions.assertEquals(factoryStorage.getCapacity(Resource.WOOD), 5);
		Assertions.assertTrue(factoryStorage.canFit(Resource.WOOD));
		Assertions.assertFalse(factoryStorage.canFit(Resource.STONE));
	}

	@Test
	public void createStorage_moreResources() {
		Map<Resource, StorageSlot> resources = new HashMap<>();
		resources.put(Resource.WOOD, new StorageSlot(0, 5, 0));
		resources.put(Resource.STONE, new StorageSlot(0, 10, 0));
		FactoryStorage factoryStorage = new FactoryStorage(resources);

		Assertions.assertEquals(factoryStorage.getCapacity(Resource.WOOD), 5);
		Assertions.assertEquals(factoryStorage.getCapacity(Resource.STONE), 10);
		Assertions.assertTrue(factoryStorage.canFit(Resource.WOOD));
		Assertions.assertTrue(factoryStorage.canFit(Resource.STONE));
	}

	@Test
	public void addResources() {
		Map<Resource, StorageSlot> resources = new HashMap<>();
		resources.put(Resource.WOOD, new StorageSlot(0, 5, 0));
		resources.put(Resource.STONE, new StorageSlot(0, 10, 0));
		FactoryStorage factoryStorage = new FactoryStorage(resources);

		factoryStorage.addResource(Resource.WOOD, 5);
		Assertions.assertEquals(0, factoryStorage.getRemainingCapacity(Resource.WOOD));
		Assertions.assertEquals(10, factoryStorage.getRemainingCapacity(Resource.STONE));

		Assertions.assertEquals(5, factoryStorage.getCapacityTaken(Resource.WOOD));
		Assertions.assertEquals(0, factoryStorage.getCapacityTaken(Resource.STONE));
	}

	@Test
	public void addResources_overCapacity() {
		Map<Resource, StorageSlot> resources = new HashMap<>();
		resources.put(Resource.WOOD, new StorageSlot(0, 5, 0));
		FactoryStorage factoryStorage = new FactoryStorage(resources);


		factoryStorage.addResource(Resource.WOOD, 50);

		Assertions.assertEquals(5, factoryStorage.getCapacity(Resource.WOOD));
		Assertions.assertEquals(5, factoryStorage.getRemainingCapacity(Resource.WOOD));
		Assertions.assertEquals(0, factoryStorage.getCapacityTaken(Resource.WOOD));
	}

	@Test
	public void addResource_notInCapacities() {
		Map<Resource, StorageSlot> resources = new HashMap<>();
		resources.put(Resource.WOOD, new StorageSlot(0, 5, 0));
		FactoryStorage factoryStorage = new FactoryStorage(resources);

		factoryStorage.addResource(Resource.WOOD, 50);

		Assertions.assertEquals(0, factoryStorage.getCapacity(Resource.STONE));
		Assertions.assertEquals(0, factoryStorage.getRemainingCapacity(Resource.STONE));
		Assertions.assertEquals(0, factoryStorage.getCapacityTaken(Resource.STONE));
	}

	@Test
	public void addResource_removeResource() {
		Map<Resource, StorageSlot> resources = new HashMap<>();
		resources.put(Resource.WOOD, new StorageSlot(0, 5, 0));
		resources.put(Resource.STONE, new StorageSlot(0, 10, 0));
		FactoryStorage factoryStorage = new FactoryStorage(resources);


		factoryStorage.addResource(Resource.WOOD, 5);
		Assertions.assertEquals(0, factoryStorage.getRemainingCapacity(Resource.WOOD));
		Assertions.assertEquals(10, factoryStorage.getRemainingCapacity(Resource.STONE));

		Assertions.assertEquals(5, factoryStorage.getCapacityTaken(Resource.WOOD));
		Assertions.assertEquals(0, factoryStorage.getCapacityTaken(Resource.STONE));

		factoryStorage.removeResource(Resource.WOOD, 2);
		Assertions.assertEquals(2, factoryStorage.getRemainingCapacity(Resource.WOOD));
		Assertions.assertEquals(10, factoryStorage.getRemainingCapacity(Resource.STONE));

		Assertions.assertEquals(3, factoryStorage.getCapacityTaken(Resource.WOOD));
		Assertions.assertEquals(0, factoryStorage.getCapacityTaken(Resource.STONE));

	}

}