package com.soze.cashflow.logaggregator.appender;

import com.soze.cashflow.logaggregator.controller.LogAggregatorClient;
import com.soze.cashflow.logaggregator.dto.LogEventDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.List;

class AppenderHttpWorkerTest {

	private AppenderHttpWorker worker;

	private List<LogEventDTO> events = new ArrayList<>();

	@BeforeEach
	void setup() {
		events = new ArrayList<>();
		worker = new AppenderHttpWorker(events::add);
	}

	@Test
	void test_handleEvent() {
		worker.start();

		LogEventDTO dto = new LogEventDTO();
		worker.addLogEvent(dto);
		Assertions.assertTrue(events.isEmpty());
		sleep(1000);
		worker.stop();
		Assertions.assertFalse(events.isEmpty());
	}

	@ParameterizedTest
	@ValueSource(ints = {5, 25, 5000, 25000, 750000})
	void test_handleManyEvents(int number) {
		worker.start();
		for (int i = 0; i < number; i++) {
			LogEventDTO dto = new LogEventDTO();
			worker.addLogEvent(dto);
		}
		sleep(1000);
		worker.stop();
		Assertions.assertFalse(events.isEmpty());
		Assertions.assertEquals(number, events.size());
	}

	@Test
	void test_handleEvent_throwsNullPointerException() {
		events = new ArrayList<>();
		worker = new AppenderHttpWorker(new LogAggregatorClient() {
			int count = 0;

			@Override
			public void handleLog(LogEventDTO logEvent) {
				if (count == 0) {
					count += 1;
					throw new NullPointerException("LogEvent cannot be null");
				}
				events.add(logEvent);
			}
		});
		worker.start();

		LogEventDTO dto = new LogEventDTO();
		worker.addLogEvent(dto);
		sleep(1000);

		dto = new LogEventDTO();
		worker.addLogEvent(dto);
		sleep(1000);
		Assertions.assertFalse(events.isEmpty());
	}

	@Test
	void test_handleEvent_throwExceptionRetry() {
		events = new ArrayList<>();
		worker = new AppenderHttpWorker(new LogAggregatorClient() {
			int count = 0;

			@Override
			public void handleLog(LogEventDTO logEvent) {
				if (count == 0) {
					count += 1;
					throw new NullPointerException("LogEvent cannot be null");
				}
				if (count == 1) {
					count += 1;
					throw new IllegalStateException("LogEvent cannot be null");
				}
				events.add(logEvent);
			}
		});
		worker.start();

		LogEventDTO dto = new LogEventDTO();
		worker.addLogEvent(dto);
		sleep(1000);
		Assertions.assertFalse(events.isEmpty());

		dto = new LogEventDTO();
		worker.addLogEvent(dto);
		sleep(1000);
		Assertions.assertFalse(events.isEmpty());
	}

	@Test
	void test_testHandleEvent_retryOverMaxRetries() {
		events = new ArrayList<>();
		worker = new AppenderHttpWorker(logEvent -> {
			throw new NullPointerException("LogEvent cannot be null");
		});
		worker.start();

		LogEventDTO dto = new LogEventDTO();
		worker.addLogEvent(dto);
		sleep(1500);
		Assertions.assertTrue(events.isEmpty());
	}

	private void sleep(long ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}