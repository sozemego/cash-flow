package com.soze.truck.service;


import com.soze.common.json.JsonUtils;
import com.soze.common.message.server.ServerMessage;
import com.soze.truck.domain.Truck;
import com.soze.truck.world.RemoteWorldService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.web.socket.WebSocketMessage;

import java.util.List;

@SpringBootTest
@Import(TruckServiceTestBeanConfiguration.class)
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

	private TestWebSocketSession testWebSocketSession;

	@BeforeEach
	public void setup() {
		testWebSocketSession = new TestWebSocketSession();
		truckService = new TruckService(truckTemplateLoader, truckConverter, truckNavigationService, remoteWorldService);
	}

	@Test
	void test_addTruck() {
		truckService.addSession(testWebSocketSession);

		Truck truck = truckTemplateLoader.constructTruckByTemplateId("BASIC_TRUCK");
		String cityId = "CityID";
		truckService.addTruck(truck, cityId);

		List<WebSocketMessage> messages = testWebSocketSession.getAllMessages();
		Assertions.assertEquals(1, messages.size());
		ServerMessage serverMessage = JsonUtils.parse((String) messages.get(0).getPayload(), ServerMessage.class);
		Assertions.assertEquals(ServerMessage.ServerMessageType.TRUCK_ADDED.name(), serverMessage.getType());
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

		truckService.addSession(testWebSocketSession);
		List<WebSocketMessage> messages = testWebSocketSession.getAllMessages();
		Assertions.assertEquals(1, messages.size());
		ServerMessage serverMessage = JsonUtils.parse((String) messages.get(0).getPayload(), ServerMessage.class);
		Assertions.assertEquals(ServerMessage.ServerMessageType.TRUCK_ADDED.name(), serverMessage.getType());
	}

	@Test
	public void travel_truckDoesNotExist() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> this.truckService.travel("someTruck", "cityId"));
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
		String currentCityId = "Warsaw";
		this.truckService.addTruck(truck, currentCityId);
		String toCityId = "Wro";
		this.truckService.travel(truck.getId(), toCityId);
	}

}