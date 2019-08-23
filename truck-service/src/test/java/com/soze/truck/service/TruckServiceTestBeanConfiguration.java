package com.soze.truck.service;

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
				return Arrays.asList(wroclaw);
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


}
