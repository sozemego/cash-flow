package com.soze.clock.controller;

import com.soze.clock.domain.Clock;
import com.soze.clock.service.ClockService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ClockController {

	private static final Logger LOG = LoggerFactory.getLogger(ClockController.class);

	private final ClockService clockService;

	@Autowired
	public ClockController(ClockService clockService) {
		this.clockService = clockService;
	}

	@GetMapping(value = "/")
	public Clock getClock() {
		LOG.info("Called getClock");
		Clock clock = new Clock(
			clockService.getTimeMultiplier(),
			clockService.getStartTime()
		);
		LOG.info("Returning = {}", clock);
		return clock;
	}

}
