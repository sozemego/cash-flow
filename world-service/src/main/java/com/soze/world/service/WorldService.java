package com.soze.world.service;

import com.soze.world.domain.City;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

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

	public Optional<City> getCityById(String cityId) {
		LOG.debug("Called getCityById with id = {}", cityId);
		Objects.requireNonNull(cityId);
		return cities.stream()
								 .filter(city -> cityId.equals(city.id))
								 .findFirst();
	}

	public Optional<City> getCityByName(String name) {
		LOG.debug("called getCityByName with name = {}", name);
		Objects.requireNonNull(name);
		return cities.stream()
								 .filter(city -> name.equals(city.name))
								 .findFirst();

	}
}
