package com.soze.cashflow.logaggregator.appender;

import ch.qos.logback.classic.spi.ILoggingEvent;
import com.soze.cashflow.logaggregator.dto.LogEventDTO;

public class LoggingEventConverter {

	public static LogEventDTO convert(ILoggingEvent e) {
		LogEventDTO dto = new LogEventDTO();
		dto.level = e.getLevel().levelStr;
		dto.message = e.getMessage();
		return dto;
	}

}
