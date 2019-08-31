package com.soze.truck.service;

import com.soze.clock.domain.Clock;
import com.soze.common.client.WorldServiceClient;
import com.soze.common.dto.CityDTO;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@Configuration
public class TruckServiceTestBeanConfiguration {

	@Bean
	public WorldServiceClient client() {
		WorldServiceClient worldServiceClient = new WorldServiceClient() {
			@Override
			public List<CityDTO> getAllCities() {
				CityDTO wroclaw = new CityDTO();
				wroclaw.id = "Wro";
				wroclaw.name = "Wroclaw";
				wroclaw.longitude = 25;
				wroclaw.latitude = 25;
				CityDTO warsaw = new CityDTO();
				warsaw.id = "Warsaw";
				warsaw.name = "Warsaw";
				warsaw.longitude = 26;
				warsaw.latitude = 26;
				return Arrays.asList(wroclaw, warsaw);
			}

			@Override
			public CityDTO getCityByName(String name) {
				return null;
			}

			@Override
			public CityDTO getCityById(String cityId) {
				return null;
			}
		};
		return worldServiceClient;
	}

	@Bean
	public Clock clock() {
		return new Clock(60, System.currentTimeMillis(), "12:00");
	}


}
