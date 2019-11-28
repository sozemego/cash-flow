package com.soze.cashflow.logaggregator.controller;

import com.soze.cashflow.logaggregator.dto.LogEventDTO;
import com.soze.cashflow.logaggregator.service.LogAggregatorService;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Consumes;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;

import javax.inject.Inject;

@Controller("/log")
public class LogAggregatorController implements LogAggregatorClient {

	private final LogAggregatorService service;

	@Inject
	public LogAggregatorController(LogAggregatorService service) {
		this.service = service;
	}

	@Post
	@Consumes(MediaType.APPLICATION_JSON)
	@Override
	public void handleLog(@Body LogEventDTO logEvent) {
		service.handle(logEvent);
	}
}