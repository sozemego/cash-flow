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
		worker = new AppenderHttpWorker(new LogAggregatorClient() {
			@Override
			public void handleLog(LogEventDTO logEvent) {
				events.add(logEvent);
			}
		});
	}

	@Test
	void test_handleEvent() {
		LogEventDTO dto = new LogEventDTO();
		worker.addLogEvent(dto);
		Assertions.assertTrue(events.isEmpty());
		worker.start();
		sleep(1000);
		worker.stop();
		Assertions.assertFalse(events.isEmpty());
	}

	@ParameterizedTest
	@ValueSource(ints = { 5, 25, 5000, 25000, 750000 })
	void test_handleManyEvents(int number) {
		for (int i = 0; i < number; i++) {
			LogEventDTO dto = new LogEventDTO();
			worker.addLogEvent(dto);
		}
		Assertions.assertTrue(events.isEmpty());
		worker.start();
		sleep(1000);
		worker.stop();
		Assertions.assertFalse(events.isEmpty());
		Assertions.assertEquals(number, events.size());
	}

	private void sleep(long ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}