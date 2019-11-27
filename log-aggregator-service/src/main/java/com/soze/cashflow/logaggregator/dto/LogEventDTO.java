package com.soze.cashflow.logaggregator.dto;

public class LogEventDTO {

	public String application;

	public String level;
	public String message;

	@Override
	public String toString() {
		return "LogEventDTO{" + "application='" + application + '\'' + ", level='" + level + '\'' + ", message='" + message + '\'' + '}';
	}
}
