package com.soze.clock.service;

import com.soze.clock.ClockRepository;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class ClockService {

	private final ClockRepository clockRepository;

	public ClockService(ClockRepository clockRepository) {
		this.clockRepository = clockRepository;
	}

	public long getStartTime() {
		return clockRepository.getStartTime();
	}

	public long getTimeMultiplier() {
		return clockRepository.getTimeMultiplier();
	}
}
