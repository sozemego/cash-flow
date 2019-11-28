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
	private final Queue<RetryableLogEventDTO> buffer = new ConcurrentLinkedQueue<>();
	private final LogAggregatorClient client;
	private Future<?> task = null;
	private int maxRetries = 5;
	private boolean running = false;

	public AppenderHttpWorker(LogAggregatorClient client) {
		this.client = client;
	}

	@Override
	public void run() {
		LOG.info("Starting loop...");
		RetryableLogEventDTO current = null;

		while (running) {

			try {

				if (LOG.isTraceEnabled()) {
					LOG.trace("Sending {} events from buffer", buffer.size());
				}
				while ((current = buffer.poll()) != null) {
					client.handleLog(current.dto);
				}

				sleep(250);

			} catch (Exception e) {
					if (current != null) {
						current.retries++;
						if (current.retries < maxRetries) {
							addRetryableLogEvent(current);
						}
					}
			}
		}

		LOG.info("Ending loop...");
	}

	public void addLogEvent(LogEventDTO event) {
		buffer.add(new RetryableLogEventDTO(event));
	}

	private void addRetryableLogEvent(RetryableLogEventDTO event) {
		buffer.add(event);
	}

	private void sleep(long ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			running = false;
		}
	}

	public boolean isStarted() {
		return running;
	}

	public void start() {
		if (isStarted()) {
			return;
		}
		task = executorService.submit(this);
		running = true;
	}

	public void stop() {
		if (!isStarted()) {
			return;
		}
		running = false;
		task.cancel(true);
	}

	private static class RetryableLogEventDTO {

		public int retries = 0;
		public LogEventDTO dto;

		public RetryableLogEventDTO(LogEventDTO dto) {
			this.dto = dto;
		}
	}
}
