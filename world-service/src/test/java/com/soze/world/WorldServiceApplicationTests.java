package com.soze.world;

import com.soze.world.domain.City;
import com.soze.world.service.WorldService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

@SpringBootTest
@ActiveProfiles("test")
class WorldServiceApplicationTests {

	@Autowired
	private WorldService worldService;

	@Test
	public void getCities() {
		Assertions.assertFalse(worldService.getCities().isEmpty());
	}

	@Test
	public void getCityById_exists() {
		Optional<City> city = worldService.getCityById("Warsaw");
		Assertions.assertTrue(city.isPresent());
	}

	@Test
	public void getCityById_doesNotExist() {
		Optional<City> city = worldService.getCityById("this id does not exist");
		Assertions.assertFalse(city.isPresent());
	}

	@Test
	public void getCityById_cityIdNull() {
		Assertions.assertThrows(NullPointerException.class, () -> worldService.getCityById(null));
	}

	@Test
	public void getCityByName_exists() {
		Optional<City> city = worldService.getCityByName("Wroclaw");
		Assertions.assertTrue(city.isPresent());
	}

	@Test
	public void getCityByName_doesNotExist() {
		Optional<City> city = worldService.getCityByName("some name that does not exist");
		Assertions.assertFalse(city.isPresent());
	}

	@Test
	public void getCityByName_nameNull() {
		Assertions.assertThrows(NullPointerException.class, () -> worldService.getCityByName(null));
	}

}
