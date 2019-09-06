package com.soze.common.client;

import com.soze.common.dto.Clock;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient("clock-service")
public interface ClockServiceClient {

	@GetMapping(
		value = "/clock",
		produces = MediaType.APPLICATION_JSON_VALUE
	)
	Clock getClock();
}
