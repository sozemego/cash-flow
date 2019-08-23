package com.soze.factory.world;

import com.soze.common.client.WorldServiceClient;
import com.soze.common.dto.CityDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RemoteWorldService {

	private static final Logger LOG = LoggerFactory.getLogger(RemoteWorldService.class);

	private final WorldServiceClient client;

	@Autowired
	public RemoteWorldService(WorldServiceClient client) {
		this.client = client;
	}

	public List<CityDTO> getAllCities() {
		return client.getAllCities();
	}

	public CityDTO getCityByName(String name) {
		return client.getCityByName(name);
	}

	public CityDTO getCityById(String cityId) {
		return client.getCityById(cityId);
	}
}
