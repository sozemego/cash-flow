package com.soze.truck.world;

import com.soze.common.dto.CityDTO;
import com.soze.common.json.JsonUtils;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

@Service
public class RemoteWorldService {

	private static final Logger LOG = LoggerFactory.getLogger(RemoteWorldService.class);

	private static final String WORLD_SERVICE_URL = "http://localhost:9000/world";

	private final RestTemplate restTemplate = new RestTemplate();

	private final Map<String, CityDTO> cities = new HashMap<>();

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
		LOG.info("Fetching all cities");
		CircuitBreaker circuitBreaker = CircuitBreaker.ofDefaults("RemoteWorldService");
		RetryConfig retryConfig = RetryConfig.custom().maxAttempts(25).waitDuration(Duration.ofMillis(500)).build();

		Retry retry = Retry.of("RemoteWorldService", retryConfig);

		Callable<List<CityDTO>> callable = Retry.decorateCallable(retry, () -> {
			try {
				LOG.info("Fetching all cities from = {}", WORLD_SERVICE_URL);
				ResponseEntity<String> responseEntity = restTemplate.getForEntity(WORLD_SERVICE_URL, String.class);
				String payload = responseEntity.getBody();
				List<CityDTO> cities = JsonUtils.parseList(payload, CityDTO.class);
				LOG.info("Fetched {}", cities);
				return cities;
			} catch (Exception e) {
				LOG.error("Exception when fetching all cities", e);
				throw e;
			}
		});

		callable = circuitBreaker.decorateCallable(callable);

		return Try.ofCallable(callable).onFailure(t -> {
			LOG.info("Problem getting all cities", t);
		}).get();
	}

}
