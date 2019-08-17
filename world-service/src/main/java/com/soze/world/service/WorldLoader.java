package com.soze.world.service;

import com.soze.common.json.JsonUtils;
import com.soze.world.domain.City;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class WorldLoader {

	private static final Logger LOG = LoggerFactory.getLogger(WorldLoader.class);

	private static final String FILE_NAME = "cities.json";

	private final List<City> cities = new ArrayList<>();

	@PostConstruct
	public void setup() throws Exception {
		LOG.info("World loader init...");
		InputStream stream = new ClassPathResource(FILE_NAME).getInputStream();
		this.cities.addAll(JsonUtils.parseList(stream, City.class));
		for (City city : cities) {
			LOG.info("Loaded city = {}", city);
		}
		LOG.info("Loaded {} cities", cities.size());
	}

	public List<City> getCities() {
		return cities;
	}

}
