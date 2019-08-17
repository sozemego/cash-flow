package com.soze.world.controller;

import com.soze.world.domain.City;
import com.soze.world.service.WorldService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Api(value = "World")
public class WorldController {

	private static final Logger LOG = LoggerFactory.getLogger(WorldController.class);

	private final WorldService worldService;

	public WorldController(WorldService worldService) {
		this.worldService = worldService;
	}

	@GetMapping(value = "/")
	@ApiOperation(value = "Retrieves list of all cities")
	public List<City> getCities() {
		LOG.info("Calling getCities");
		List<City> cities = worldService.getCities();
		LOG.info("Returning {} cities", cities.size());
		return cities;
	}

}
