package com.soze.factory.client;

import com.soze.common.dto.FactoryDTO;
import com.soze.factory.domain.SellResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "factory-service")
public interface FactoryServiceClient {

	@PostMapping(
		path = "/factory/sell",
		produces = MediaType.APPLICATION_JSON_VALUE
	)
	SellResult sell(@RequestParam("factoryId") String factoryId, @RequestParam("resource") String resource,
									@RequestParam("count") Integer count
								 );

	@GetMapping(
		path = "/factory/single/{factoryId}",
		produces = MediaType.APPLICATION_JSON_VALUE
	)
	FactoryDTO getFactory(@PathVariable("factoryId") String factoryId);
}
