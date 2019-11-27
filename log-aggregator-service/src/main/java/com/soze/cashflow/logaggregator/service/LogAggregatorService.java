package com.soze.cashflow.logaggregator.service;

import com.soze.cashflow.logaggregator.dto.LogEventDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;

@Singleton
public class LogAggregatorService {

	private static final Logger LOG = LoggerFactory.getLogger(LogAggregatorService.class);

	public void handle(LogEventDTO logEventDTO) {
		LOG.info("{}", logEventDTO);
	}

}
