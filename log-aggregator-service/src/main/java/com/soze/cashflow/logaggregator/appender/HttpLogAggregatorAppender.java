package com.soze.cashflow.logaggregator.appender;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import com.soze.cashflow.logaggregator.client.LogAggregatorClientImpl;
import com.soze.cashflow.logaggregator.controller.LogAggregatorClient;
import com.soze.cashflow.logaggregator.dto.LogEventDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpLogAggregatorAppender extends AppenderBase<ILoggingEvent> {

	private static final Logger LOG = LoggerFactory.getLogger(HttpLogAggregatorAppender.class);

	private AppenderHttpWorker appenderHttpWorker;

	private String host;
	private String path = "";
	private String application;

	public HttpLogAggregatorAppender() {

	}

	public HttpLogAggregatorAppender(LogAggregatorClient client) {
		this.appenderHttpWorker = new AppenderHttpWorker(client);
	}

	@Override
	protected void append(ILoggingEvent event) {
		if (LOG.isTraceEnabled()) {
			LOG.trace("{}", event);
		}
		AppenderHttpWorker worker = getWorker();
		LogEventDTO dto = LoggingEventConverter.convert(event, application);
		worker.addLogEvent(dto);
	}

	private AppenderHttpWorker getWorker() {
		if (appenderHttpWorker == null) {
			appenderHttpWorker = new AppenderHttpWorker(createClient());
		}
		return appenderHttpWorker;
	}

	private LogAggregatorClientImpl createClient() {
		return new LogAggregatorClientImpl(getHost(), getPath());
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getApplication() {
		return application;
	}

	public void setApplication(String application) {
		this.application = application;
	}

	@Override
	public boolean isStarted() {
		return appenderHttpWorker.isStarted();
	}

	@Override
	public void start() {
		getWorker().start();
		super.start();
	}

	@Override
	public void stop() {
		getWorker().stop();
		super.stop();
	}
}
