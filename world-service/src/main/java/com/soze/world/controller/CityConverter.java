package com.soze.world.controller;

import com.soze.common.dto.CityDTO;
import com.soze.world.domain.City;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CityConverter {

	public List<CityDTO> convert(List<City> cities) {
		return cities.stream().map(this::convert).collect(Collectors.toList());
	}

	public CityDTO convert(City city) {
		CityDTO dto = new CityDTO();
		dto.id = city.id;
		dto.name = city.name;
		dto.factorySlots = city.factorySlots;
		dto.latitude = city.latitude;
		dto.longitude = city.longitude;
		return dto;
	}

}
