package com.soze.truck.external;

import com.soze.common.client.WorldServiceClient;
import com.soze.common.dto.CityDTO;
import com.soze.common.resilience.RetryUtils;
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
import java.util.Optional;
import java.util.concurrent.Callable;

@Service
public class RemoteWorldService {

	private static final Logger LOG = LoggerFactory.getLogger(RemoteWorldService.class);

	private final Map<String, CityDTO> cityByName = new HashMap<>();
	private final Map<String, CityDTO> cityById = new HashMap<>();

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
			this.cityByName.put(city.name, city);
			this.cityById.put(city.id, city);
		}
		LOG.info("Loaded {} cities from world service", cities.size());
	}

	public CityDTO getCityByName(String name) {
		return cityByName.get(name);
	}

	public Optional<CityDTO> getCityById(String id) {
		return Optional.ofNullable(cityById.get(id));
	}

	private List<CityDTO> getAllCities() {
		LOG.info("Getting all cities");

		return RetryUtils.retry(25, Duration.ofMillis(2500), () -> {
			try {
				return this.fetchAllCities();
			} catch (Exception e) {
				LOG.warn("Problem fetching all cities", e);
				throw e;
			}
		});
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
