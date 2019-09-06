package com.soze.common.client;

import com.soze.common.dto.CityDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient("world-service")
public interface WorldServiceClient {

	@GetMapping(
		path = "/world",
		produces = MediaType.APPLICATION_JSON_VALUE
	)
	List<CityDTO> getAllCities();

	@GetMapping(
		path = "/world/name/{name}",
		produces = MediaType.APPLICATION_JSON_VALUE
	)
	CityDTO getCityByName(@PathVariable("name") String name);

	@GetMapping(
		path = "/world/id/{cityId}",
		produces = MediaType.APPLICATION_JSON_VALUE
	)
	CityDTO getCityById(@PathVariable("cityId") String cityId);

}
