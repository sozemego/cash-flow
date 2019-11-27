package com.soze.cashflow.logaggregator;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import com.soze.cashflow.logaggregator.client.LogAggregatorClient;
import com.soze.cashflow.logaggregator.dto.LogEventDTO;

public class HttpLogAggregatorAppender extends AppenderBase<ILoggingEvent> {

	private LogAggregatorClient client;

	private String address;

	public HttpLogAggregatorAppender() {

	}

	public HttpLogAggregatorAppender(LogAggregatorClient client) {
		this.client = client;
	}

	@Override
	protected void append(ILoggingEvent event) {
		System.out.println(event);
		getClient().handleLog(convert(event));
	}

	private LogAggregatorClient getClient() {
		if (client == null) {
			client = createClient();
		}
		return client;
	}

	private LogAggregatorClient createClient() {
		return new LogAggregatorClient(getAddress());
	}

	private LogEventDTO convert(ILoggingEvent e) {
		LogEventDTO dto = new LogEventDTO();
		dto.level = e.getLevel().levelStr;
		dto.message = e.getMessage();
		return dto;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
}
