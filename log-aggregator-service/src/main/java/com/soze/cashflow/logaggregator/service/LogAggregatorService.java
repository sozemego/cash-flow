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
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;

@Singleton
public class LogAggregatorService {

	private static final Logger LOG = LoggerFactory.getLogger(LogAggregatorService.class);

	private final LogRepository logRepository;
	private final Queue<LogEventEntity> buffer = new ConcurrentLinkedQueue<>();

	@Inject
	public LogAggregatorService(LogRepository logRepository) {
		this.logRepository = logRepository;
	}

	public void handle(LogEventDTO logEventDTO) {
		LOG.info("{}", logEventDTO);
		LogEventEntity entity = convert(logEventDTO);
		buffer.add(entity);

		if (buffer.size() > 100) {
			LOG.info("Buffer size exceeded, saving LogEventEntities in a batch");
			List<LogEventEntity> batch = sliceForBatchUpdate();
			logRepository.save(batch);
		}
	}

	private LogEventEntity convert(LogEventDTO dto) {
		LogEventEntity entity = new LogEventEntity();
		entity.setId(UUID.randomUUID());
		entity.setApplication(dto.application);
		entity.setLevel(dto.level);
		entity.setMessage(dto.message);
		entity.setTimestamp(new Timestamp(Instant.now().toEpochMilli()));
		return entity;
	}

	private List<LogEventEntity> sliceForBatchUpdate() {
		List<LogEventEntity> batch = new ArrayList<>();

		LogEventEntity current = null;
		int count = 0;
		while ((current = buffer.poll()) != null && count < 100) {
			count++;
			batch.add(current);
		}

		return batch;
	}

	public List<LogEventDTO> getAllLogs() {
		return logRepository.getAllLogs();
	}
}
