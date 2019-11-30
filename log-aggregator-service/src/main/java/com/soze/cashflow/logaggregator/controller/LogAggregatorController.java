package com.soze.cashflow.logaggregator.controller;

import com.soze.cashflow.logaggregator.dto.LogEventDTO;
import com.soze.cashflow.logaggregator.service.LogAggregatorService;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Consumes;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Produces;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.List;

@Controller("/log")
public class LogAggregatorController implements LogAggregatorClient {

	private static final Logger LOG = LoggerFactory.getLogger(LogAggregatorController.class);

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

	@Get
	@Produces(MediaType.APPLICATION_JSON)
	public List<LogEventDTO> getAllLogs() {
		LOG.info("getAllLogs called");
		return service.getAllLogs();
	}
}
