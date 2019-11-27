package com.soze.cashflow.logaggregator.controller;

import com.soze.cashflow.logaggregator.dto.LogEventDTO;

public interface LogController {

	void handleLog(LogEventDTO logEvent);

}
