package com.soze.cashflow.logaggregator.client;

import com.soze.cashflow.logaggregator.controller.LogController;
import com.soze.cashflow.logaggregator.dto.LogEventDTO;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.MediaType;
import io.micronaut.http.MutableHttpHeaders;
import io.micronaut.http.client.HttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URL;

public class LogAggregatorClient implements LogController {

	private static final Logger LOG = LoggerFactory.getLogger(LogAggregatorClient.class);

	private final HttpClient client;

	public LogAggregatorClient(String address) {
		LOG.info("Creating LogAggregatorClient with address = {}", address);
		try {
			client = HttpClient.create(new URL(address));
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void handleLog(LogEventDTO logEvent) {
		HttpRequest<LogEventDTO> request = HttpRequest.POST("/log-aggregator-service/log", logEvent);
		MutableHttpHeaders headers = (MutableHttpHeaders) request.getHeaders();
		headers.contentType(MediaType.APPLICATION_JSON_TYPE);
		client.toBlocking().exchange(request);
	}
}
