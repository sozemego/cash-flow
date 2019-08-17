package com.soze.world.service;

import com.soze.world.domain.City;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Service
public class WorldService {

	private static final Logger LOG = LoggerFactory.getLogger(WorldService.class);

	private final WorldLoader worldLoader;
	private final List<City> cities = new ArrayList<>();

	@Autowired
	public WorldService(WorldLoader worldLoader) {
		this.worldLoader = worldLoader;
	}

	@PostConstruct
	public void setup() {
		LOG.info("WorldService init...");
		cities.addAll(worldLoader.getCities());
	}

	public List<City> getCities() {
		return cities;
	}
}
