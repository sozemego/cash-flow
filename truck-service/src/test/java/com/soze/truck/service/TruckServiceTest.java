package com.soze.truck.service;


import com.soze.common.client.PlayerServiceClient;
import com.soze.common.dto.*;
import com.soze.common.client.FactoryServiceClient;
import com.soze.truck.domain.Storage;
import com.soze.truck.domain.Truck;
import com.soze.truck.domain.TruckNavigation;
import com.soze.truck.external.RemoteFactoryService;
import com.soze.truck.external.RemotePlayerService;
import com.soze.truck.external.RemoteWorldService;
import com.soze.truck.repository.TruckNavigationRepository;
import com.soze.truck.repository.TruckRepository;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestClientException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@SpringBootTest
@Import(TruckServiceTestBeanConfiguration.class)
@ActiveProfiles("test")
@DirtiesContext
class TruckServiceTest {

	@Autowired
	private TruckService truckService;

	@Autowired
	private TruckTemplateLoader truckTemplateLoader;

	@Autowired
	private TruckConverter truckConverter;

	@Autowired
	private TruckNavigationService truckNavigationService;

	@Autowired
	private RemoteWorldService remoteWorldService;

	@Autowired
	private RemoteFactoryService remoteFactoryService;

	@Autowired
	private RemotePlayerService playerService;

	@Autowired
	private TruckRepository truckRepository;

	@Autowired
	private TruckNavigationRepository truckNavigationRepository;

	@Autowired
	private SessionRegistry sessionRegistry;

	@MockBean
	private FactoryServiceClient factoryServiceClient;

	@MockBean
	private PlayerServiceClient playerServiceClient;

	private TestWebSocketSession testWebSocketSession;

	@BeforeEach
	public void setup() {
		testWebSocketSession = new TestWebSocketSession();
		truckService = new TruckService(truckConverter, truckNavigationService, remoteWorldService,
																		remoteFactoryService, playerService, new Clock(60, System.currentTimeMillis()),
																		truckRepository, sessionRegistry
		);
	}

	@AfterEach
	public void clear() {
		truckRepository.deleteAll();
		truckNavigationRepository.deleteAll();
	}

	@Test
	void test_addTruck() {
		Truck truck = truckTemplateLoader.constructTruckByTemplateId("BASIC_TRUCK");
		String cityId = "CityID";
		truckService.addTruck(truck, cityId);

		Assertions.assertEquals(cityId, truckNavigationService.getCityIdForTruck(truck.getId()));
	}

	@Test
	public void test_addTruck_TruckIsNull() {
		String cityId = "CityID";
		Assertions.assertThrows(NullPointerException.class, () -> truckService.addTruck(null, cityId));
	}

	@Test
	public void test_addTruck_cityIdIsNull() {
		Truck truck = truckTemplateLoader.constructTruckByTemplateId("BASIC_TRUCK");
		Assertions.assertThrows(NullPointerException.class, () -> truckService.addTruck(truck, null));
	}

	@Test
	public void test_addTruck_truckWithoutId() {
		Truck truck = truckTemplateLoader.constructTruckByTemplateId("BASIC_TRUCK");
		truck.setId(null);
		String cityId = "CityID";
		Assertions.assertThrows(IllegalArgumentException.class, () -> truckService.addTruck(truck, cityId));
	}

	@Test
	public void test_addTruck_nullStorage() {
		Truck truck = truckTemplateLoader.constructTruckByTemplateId("BASIC_TRUCK");
		truck.setStorage(null);
		String cityId = "CityID";
		Assertions.assertThrows(IllegalArgumentException.class, () -> truckService.addTruck(truck, cityId));
	}

	@Test
	public void test_addTruck_alreadyAdded() {
		Truck truck = truckTemplateLoader.constructTruckByTemplateId("BASIC_TRUCK");
		truckService.addTruck(truck, "cityId");
		Assertions.assertThrows(IllegalArgumentException.class, () -> truckService.addTruck(truck, "cityId"));
	}

	@Test
	public void test_addSession() {
		Truck truck = truckTemplateLoader.constructTruckByTemplateId("BASIC_TRUCK");
		String cityId = "CityID";
		truckService.addTruck(truck, cityId);

		List<Truck> trucks = truckService.getTrucks();

		Assertions.assertEquals(1, trucks.size());
	}

	@Test
	public void travel_truckDoesNotExist() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> this.truckService.travel(UUID.randomUUID(), "cityId"));
	}

	@Test
	public void travel_cityDoesNotExist() {
		Truck truck = truckTemplateLoader.constructTruckByTemplateId("BASIC_TRUCK");
		String cityId = "cityId";
		this.truckService.addTruck(truck, cityId);
		Assertions.assertThrows(IllegalArgumentException.class, () -> this.truckService.travel(truck.getId(), cityId));
	}

	@Test
	public void travel_alreadyAtCity() {
		Truck truck = truckTemplateLoader.constructTruckByTemplateId("BASIC_TRUCK");
		String cityId = "Warsaw";
		this.truckService.addTruck(truck, cityId);
		Assertions.assertThrows(IllegalArgumentException.class, () -> this.truckService.travel(truck.getId(), cityId));
	}


	@Test
	public void travel() {
		Truck truck = truckTemplateLoader.constructTruckByTemplateId("BASIC_TRUCK");
		truck.setSpeed(500);
		String currentCityId = "Warsaw";
		this.truckService.addTruck(truck, currentCityId);
		String toCityId = "Wro";

		TestWebSocketSession session = new TestWebSocketSession();
		this.truckService.travel(truck.getId(), toCityId);

		TruckNavigation navigation = truckNavigationService.getTruckNavigation(truck.getId());
		Assertions.assertEquals(toCityId, navigation.nextCityId);
	}

	@Test
	public void buyResource_truckDoesNotExist() {
		truckService.buyResource(UUID.randomUUID(), "Warsaw", Resource.WOOD, 1);
	}

	@Test
	public void buyResource_truckDoesNotHaveEnoughStorage() {
		Truck truck = truckTemplateLoader.constructTruckByTemplateId("BASIC_TRUCK");
		Storage truckStorage = new Storage(1);
		truck.setStorage(truckStorage);

		this.truckService.addTruck(truck, "Warsaw");
		truckService.buyResource(truck.getId(), "Warsaw", Resource.WOOD, 5);

		Assertions.assertEquals(0, truckStorage.getCapacityTaken());
	}

	@Test
	public void buyResource_factoryDoesNotExist() {
		Truck truck = truckTemplateLoader.constructTruckByTemplateId("BASIC_TRUCK");
		Storage truckStorage = new Storage(10);
		truck.setStorage(truckStorage);

		this.truckService.addTruck(truck, "Warsaw");

		String factoryId = "factoryId";
		Mockito.when(factoryServiceClient.getFactory(factoryId)).thenThrow(new RestClientException("Not found!"));

		truckService.buyResource(truck.getId(), "factoryId", Resource.WOOD, 5);

		Assertions.assertEquals(0, truckStorage.getCapacityTaken());
	}

	@Test
	public void buyResource_factoryDoesNotHaveEnoughResources() {
		Truck truck = truckTemplateLoader.constructTruckByTemplateId("BASIC_TRUCK");
		Storage truckStorage = new Storage(10);
		truck.setStorage(truckStorage);

		this.truckService.addTruck(truck, "Warsaw");

		String factoryId = "factoryId";
		FactoryDTO factory = new FactoryDTO();
		Map<Resource, StorageSlotDTO> resources = new HashMap<>();
		resources.put(Resource.WOOD, new StorageSlotDTO(Resource.WOOD, 2, 10, 0));
		factory.setStorage(resources);

		Mockito.when(factoryServiceClient.getFactory(factoryId)).thenReturn(factory);

		truckService.buyResource(truck.getId(), "factoryId", Resource.WOOD, 5);

		Assertions.assertEquals(0, truckStorage.getCapacityTaken());
	}

	@Test
	public void buyResource() {
		Truck truck = truckTemplateLoader.constructTruckByTemplateId("BASIC_TRUCK");
		Storage truckStorage = new Storage(10);
		truck.setStorage(truckStorage);

		this.truckService.addTruck(truck, "Warsaw");

		String factoryId = "factoryId";
		int count = 5;
		FactoryDTO factory = new FactoryDTO();
		Map<Resource, StorageSlotDTO> resources = new HashMap<>();
		resources.put(Resource.WOOD, new StorageSlotDTO(Resource.WOOD, 5, 10, 5));
		factory.setStorage(resources);

		Mockito.when(factoryServiceClient.getFactory(factoryId)).thenReturn(factory);
		SellResultDTO sellResult = new SellResultDTO(factoryId, Resource.WOOD, count);
		Mockito.when(factoryServiceClient.sell(factoryId, Resource.WOOD.name(), count)).thenReturn(sellResult);

		PlayerDTO playerDTO = new PlayerDTO("id", "name", 500);
		Mockito.when(playerServiceClient.getPlayer()).thenReturn(playerDTO);

		TransferResultDTO transferResultDTO = new TransferResultDTO(-25);
		Mockito.when(playerServiceClient.transfer(-25)).thenReturn(transferResultDTO);

		truckService.buyResource(truck.getId(), factoryId, Resource.WOOD, count);

		Mockito.verify(factoryServiceClient, Mockito.times(1)).sell(factoryId, Resource.WOOD.name(), count);
		Assertions.assertEquals(5, truckStorage.getCapacityTaken());
	}

	@Test
	public void buyResource_notEnoughCash() {
		Truck truck = truckTemplateLoader.constructTruckByTemplateId("BASIC_TRUCK");
		Storage truckStorage = new Storage(10);
		truck.setStorage(truckStorage);

		this.truckService.addTruck(truck, "Warsaw");

		String factoryId = "factoryId";
		int count = 5;
		FactoryDTO factory = new FactoryDTO();

		Map<Resource, StorageSlotDTO> resources = new HashMap<>();
		resources.put(Resource.WOOD, new StorageSlotDTO(Resource.WOOD, count, 10, 5));
		factory.setStorage(resources);

		Mockito.when(factoryServiceClient.getFactory(factoryId)).thenReturn(factory);
		SellResultDTO sellResult = new SellResultDTO(factoryId, Resource.WOOD, count);
		Mockito.when(factoryServiceClient.sell(factoryId, Resource.WOOD.name(), count)).thenReturn(sellResult);

		PlayerDTO playerDTO = new PlayerDTO("id", "name", 0);
		Mockito.when(playerServiceClient.getPlayer()).thenReturn(playerDTO);

		truckService.buyResource(truck.getId(), factoryId, Resource.WOOD, count);
		Assertions.assertEquals(0, truckStorage.getCapacityTaken());
	}

}