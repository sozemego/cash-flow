package com.soze.world.controller;

import com.soze.common.dto.CityDTO;
import com.soze.world.domain.City;
import com.soze.world.service.WorldService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Api(value = "World")
public class WorldController {

	private static final Logger LOG = LoggerFactory.getLogger(WorldController.class);

	private final WorldService worldService;
	private final CityConverter cityConverter;

	@Autowired
	public WorldController(WorldService worldService, CityConverter cityConverter) {
		this.worldService = worldService;
		this.cityConverter = cityConverter;
	}

	@GetMapping(value = "/")
	@ApiOperation(value = "Retrieves list of all cities")
	public List<CityDTO> getCities() {
		LOG.info("Calling getCities");
		List<City> cities = worldService.getCities();
		LOG.info("Returning {} cities", cities.size());
		return cityConverter.convert(cities);
	}

}
