package com.soze.cashflow.logaggregator.appender;

import com.soze.cashflow.logaggregator.controller.LogAggregatorClient;
import com.soze.cashflow.logaggregator.dto.LogEventDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class AppenderHttpWorker implements Runnable {

	private static final Logger LOG = LoggerFactory.getLogger(AppenderHttpWorker.class);

	private final Queue<LogEventDTO> buffer = new ConcurrentLinkedQueue<>();

	private final LogAggregatorClient client;

	public AppenderHttpWorker(LogAggregatorClient client) {
		this.client = client;
	}

	@Override
	public void run() {
		LOG.info("Starting loop...");
		boolean running = true;
		while (running) {

			if (LOG.isTraceEnabled()) {
				LOG.trace("Sending {} events from buffer", buffer.size());
			}
			LogEventDTO current = null;
			while ((current = buffer.poll()) != null) {
				client.handleLog(current);
			}

			try {
				Thread.sleep(250);
			} catch (InterruptedException e) {
				running = false;
			}

		}
		LOG.info("Ending loop...");
	}

	public void addLogEvent(LogEventDTO event) {
		buffer.add(event);
	}
}
