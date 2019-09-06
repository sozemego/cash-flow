package com.soze.factory.service;

import com.soze.common.client.WorldServiceClient;
import com.soze.common.dto.CityDTO;
import com.soze.common.dto.Clock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.Arrays;
import java.util.List;

@Configuration
@Profile("test")
public class FactoryServiceTestBeanConfiguration {

	@Bean
	public WorldServiceClient client() {
		CityDTO wroclaw = new CityDTO();
		wroclaw.id = "Wroclaw";
		wroclaw.name = "Wroclaw";
		CityDTO warsaw = new CityDTO();
		warsaw.id = "Warsaw";
		warsaw.name = "Warsaw";
		List<CityDTO> cities = Arrays.asList(wroclaw, warsaw);

		return new WorldServiceClient() {
			@Override
			public List<CityDTO> getAllCities() {
				return cities;
			}

			@Override
			public CityDTO getCityByName(String name) {
				return cities.stream().filter(city -> name.equals(city.name)).findFirst().orElse(null);
			}

			@Override
			public CityDTO getCityById(String cityId) {
				return cities.stream().filter(city -> cityId.equals(city.id)).findFirst().orElse(null);
			}
		};
	}

	@Bean
	public Clock clock() {
		return new Clock(60, System.currentTimeMillis());
	}


}
