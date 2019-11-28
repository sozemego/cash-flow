package com.soze.cashflow.logaggregator.appender;

import com.soze.cashflow.logaggregator.controller.LogAggregatorClient;
import com.soze.cashflow.logaggregator.dto.LogEventDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class AppenderHttpWorker implements Runnable {

	private static final Logger LOG = LoggerFactory.getLogger(AppenderHttpWorker.class);

	private final ExecutorService executorService = Executors.newSingleThreadExecutor();
	private Future<?> task = null;

	private final Queue<LogEventDTO> buffer = new ConcurrentLinkedQueue<>();

	private final LogAggregatorClient client;

	private boolean running = false;

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

	public boolean isStarted() {
		return running;
	}

	public void start() {
		if (isStarted()) {
			return;
		}
		task = executorService.submit(this);
	}

	public void stop() {
		if(!isStarted()) {
			return;
		}
		running = false;
		task.cancel(true);
	}
}
