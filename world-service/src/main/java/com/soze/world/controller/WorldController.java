package com.soze.world.controller;

import com.soze.common.dto.CityDTO;
import com.soze.world.domain.City;
import com.soze.world.service.WorldService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
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

	@GetMapping(value = "/id/{cityId}")
	public CityDTO getCityById(@PathVariable("cityId") String cityId, HttpServletResponse response) {
		LOG.info("Called getCityById with id = {}", cityId);
		return worldService.getCityById(cityId)
											 .map(cityConverter::convert)
											 .orElseGet(() -> {
												 response.setStatus(HttpStatus.NOT_FOUND.value());
												 return null;
											 });
	}

	@GetMapping(value = "/name/{name}")
	public CityDTO getCityByName(@PathVariable("name") String name, HttpServletResponse response) {
		LOG.info("Called getCityByName with name = {}", name);
		return worldService.getCityByName(name)
											 .map(cityConverter::convert)
											 .orElseGet(() -> {
												 response.setStatus(HttpStatus.NOT_FOUND.value());
												 return null;
											 });
	}

}
