package com.soze.truck.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class TruckNavigationServiceTest {

	@Autowired
	private TruckNavigationService truckNavigationService;

	@Test
	public void test_setCityId() {
		String truckId = "truckId";
		String cityId = "cityId";
		truckNavigationService.setCityId(truckId, cityId);
		Assertions.assertEquals(truckNavigationService.getCityIdForTruck(truckId), cityId);
	}

	@Test
	public void test_setCityId_cityIdNull() {
		Assertions.assertThrows(NullPointerException.class, () -> truckNavigationService.setCityId("truckId", null));
	}

	@Test
	public void test_setCityId_truckIdNull() {
		Assertions.assertThrows(NullPointerException.class, () -> truckNavigationService.setCityId(null, "cityId"));
	}

}