package com.soze.clock;

import com.soze.clock.entity.Clock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
@Transactional
public class ClockRepository {

	@PersistenceContext
	private EntityManager em;

	private static final Logger LOG = LoggerFactory.getLogger(ClockRepository.class);

	private Clock clock;

	@EventListener
	public void handleApplicationStart(ApplicationReadyEvent e) {
		LOG.info("ClockRepository init...");
		clock = getClock();
		if (clock.getStartTime() == 0) {
			clock.setStartTime(System.currentTimeMillis());
			clock = update(clock);
		}
		LOG.info("Loaded clock = {}", clock);
	}

	public long getStartTime() {
		return clock.getStartTime();
	}

	public long getTimeMultiplier() {
		return clock.getTimeMultiplier();
	}

	private Clock getClock() {
		return em.find(Clock.class, 1);
	}

	private Clock update(Clock clock) {
		LOG.info("Updating clock = {}", clock);
		return em.merge(clock);
	}
}
