package com.soze.truck.world;

import com.soze.common.dto.CityDTO;
import com.soze.common.json.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
		ResponseEntity<String> responseEntity = restTemplate.getForEntity(WORLD_SERVICE_URL, String.class);
		String payload = responseEntity.getBody();
		List<CityDTO> cities = JsonUtils.parseList(payload, CityDTO.class);
		return cities;
	}

}
