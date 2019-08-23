package com.soze.truck.world;

import com.soze.common.client.WorldServiceClient;
import com.soze.common.dto.CityDTO;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

@Service
public class RemoteWorldService {

	private static final Logger LOG = LoggerFactory.getLogger(RemoteWorldService.class);

	private final Map<String, CityDTO> cities = new HashMap<>();

	private final WorldServiceClient client;

	@Autowired
	public RemoteWorldService(WorldServiceClient client) {
		this.client = client;
	}

	@PostConstruct
	public void setup() {
		LOG.info("RemoteWorldService init...");
		List<CityDTO> cities = getAllCities();
		for (CityDTO city : cities) {
			LOG.info("Loaded {}.", city);
			this.cities.put(city.name, city);
		}
		LOG.info("Loaded {} cities from world service", cities.size());
	}

	public CityDTO getCityByName(String name) {
		return cities.get(name);
	}

	private List<CityDTO> getAllCities() {
		LOG.info("Getting all cities");
		CircuitBreaker circuitBreaker = CircuitBreaker.ofDefaults("RemoteWorldService");
		RetryConfig retryConfig = RetryConfig.custom()
																				 .maxAttempts(25)
																				 .waitDuration(Duration.ofMillis(500))
																				 .build();

		Retry retry = Retry.of("RemoteWorldService", retryConfig);
		Callable<List<CityDTO>> callable = circuitBreaker.decorateCallable(Retry.decorateCallable(retry, this::fetchAllCities));

		return Try.ofCallable(callable)
							.onFailure(t -> LOG.info("Problem getting all cities", t))
							.get();
	}

	private List<CityDTO> fetchAllCities() {
		try {
			LOG.info("Fetching all cities");
			List<CityDTO> cities = client.getAllCities();
			LOG.info("Fetched {}", cities);
			return cities;
		} catch (Exception e) {
			LOG.error("Exception when fetching all cities", e);
			throw e;
		}
	}

}
