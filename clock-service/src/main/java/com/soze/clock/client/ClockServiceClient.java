package com.soze.clock.client;

import com.soze.clock.domain.Clock;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient("clock-service")
public interface ClockServiceClient {

	@GetMapping(value = "/clock", produces = MediaType.APPLICATION_JSON_VALUE)
	Clock getClock();
}
