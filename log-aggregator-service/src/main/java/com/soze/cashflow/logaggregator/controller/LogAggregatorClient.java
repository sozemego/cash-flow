package com.soze.cashflow.logaggregator.controller;

import com.soze.cashflow.logaggregator.dto.LogEventDTO;

public interface LogAggregatorClient {

	void handleLog(LogEventDTO logEvent);

}
