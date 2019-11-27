package com.soze.cashflow.logaggregator.dto;

public class LogEventDTO {

	public String level;
	public String message;

	@Override
	public String toString() {
		return "LogEventDto{" + "level=" + level + ", message='" + message + '\'' + '}';
	}
}
