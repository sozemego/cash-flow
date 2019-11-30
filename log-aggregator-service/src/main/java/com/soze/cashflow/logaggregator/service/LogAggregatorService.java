package com.soze.cashflow.logaggregator.service;

import com.soze.cashflow.logaggregator.dto.LogEventDTO;
import com.soze.cashflow.logaggregator.entity.LogEventEntity;
import com.soze.cashflow.logaggregator.repository.LogRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

@Singleton
public class LogAggregatorService {

	private static final Logger LOG = LoggerFactory.getLogger(LogAggregatorService.class);

	private final LogRepository logRepository;

	@Inject
	public LogAggregatorService(LogRepository logRepository) {
		this.logRepository = logRepository;
	}

	public void handle(LogEventDTO logEventDTO) {
		LOG.info("{}", logEventDTO);
		LogEventEntity entity = convert(logEventDTO);
		logRepository.save(entity);
	}

	private LogEventEntity convert(LogEventDTO dto) {
		LogEventEntity entity = new LogEventEntity();
		entity.setApplication(dto.application);
		entity.setLevel(dto.level);
		entity.setMessage(dto.message);
		entity.setTimestamp(new Timestamp(Instant.now().toEpochMilli()));
		return entity;
	}

	public List<LogEventDTO> getAllLogs() {
		return logRepository.getAllLogs();
	}
}
