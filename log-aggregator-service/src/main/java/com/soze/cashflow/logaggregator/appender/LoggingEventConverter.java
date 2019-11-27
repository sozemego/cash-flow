package com.soze.cashflow.logaggregator.appender;

import ch.qos.logback.classic.spi.ILoggingEvent;
import com.soze.cashflow.logaggregator.dto.LogEventDTO;

public class LoggingEventConverter {

	public static LogEventDTO convert(ILoggingEvent e, String app) {
		LogEventDTO dto = new LogEventDTO();
		dto.application = app;
		dto.level = e.getLevel().levelStr;
		dto.message = e.getFormattedMessage();
		return dto;
	}

}
