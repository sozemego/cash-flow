package com.soze.cashflow.logaggregator.repository;

import com.soze.cashflow.logaggregator.dto.LogEventDTO;
import com.soze.cashflow.logaggregator.entity.LogEventEntity;
import io.micronaut.configuration.hibernate.jpa.scope.CurrentSession;
import io.micronaut.context.annotation.Prototype;
import io.micronaut.spring.tx.annotation.Transactional;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Prototype
public class LogRepository {

	@PersistenceContext
	private EntityManager entityManager;

	@Inject
	public LogRepository(@CurrentSession EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Transactional
	public void save(LogEventEntity entity) {
		entityManager.persist(entity);
	}

	@Transactional
	public void save(List<LogEventEntity> entities) {
		for (LogEventEntity logEventEntity : entities) {
			entityManager.persist(logEventEntity);
		}
	}

	@Transactional(readOnly = true)
	public List<LogEventDTO> getAllLogs() {
		Query query = entityManager.createQuery("SELECT l FROM LogEventEntity l");
		return query.getResultList();
	}
}
