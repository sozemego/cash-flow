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

	private final CityRepository cityRepository;

	@Autowired
	public WorldService(CityRepository cityRepository) {
		this.cityRepository = cityRepository;
	}

	public List<City> getCities() {
		LOG.info("Called getCities");
		Iterable<City> cities = cityRepository.findAll();
		List<City> allCities = new ArrayList<>();
		cities.forEach(allCities::add);
		return allCities;
	}

	public Optional<City> getCityById(String cityId) {
		LOG.debug("Called getCityById with id = {}", cityId);
		Objects.requireNonNull(cityId);
		return cityRepository.findById(cityId);
	}

	public Optional<City> getCityByName(String name) {
		LOG.debug("called getCityByName with name = {}", name);
		Objects.requireNonNull(name);
		return cityRepository.getByName(name);
	}
}
