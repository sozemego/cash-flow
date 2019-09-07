package com.soze.truck.service;

import com.soze.common.client.ClockServiceClient;
import com.soze.common.client.FactoryServiceClient;
import com.soze.common.client.WorldServiceClient;
import com.soze.common.dto.CityDTO;
import com.soze.common.dto.Clock;
import com.soze.common.dto.FactoryDTO;
import com.soze.common.dto.SellResultDTO;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.Arrays;
import java.util.List;

@Configuration
@Profile("test")
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
	public ClockServiceClient clockServiceClient() {
		Clock clock = clock();
		return new ClockServiceClient() {
			@Override
			public Clock getClock() {
				return clock;
			}
		};
	}

	@Bean
	public Clock clock() {
		return new Clock(60, System.currentTimeMillis());
	}

	@Bean
	public FactoryServiceClient factoryServiceClient() {
		return new FactoryServiceClient() {
			@Override
			public SellResultDTO sell(String factoryId, String resource, Integer count) {
				return null;
			}

			@Override
			public FactoryDTO getFactory(String factoryId) {
				return null;
			}
		};
	}

}
