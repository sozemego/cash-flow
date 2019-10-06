package com.soze.clock.service;

import com.soze.clock.ClockRepository;
import org.springframework.stereotype.Service;

@Service
public class ClockService {

	private final ClockRepository clockRepository;

	/**
	 * Time in game is sped up by this amount relative to real time.
	 */
	private static final long MULTIPLIER = 60;

	public ClockService(ClockRepository clockRepository) {
		this.clockRepository = clockRepository;
	}

	public long getStartTime() {
		return clockRepository.getStartTime();
	}

	public long getTimeMultiplier() {
		return MULTIPLIER;
	}
}
