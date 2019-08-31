package com.soze.clock.service;

import org.springframework.stereotype.Service;

@Service
public class ClockService {

	/**
	 * Time in game is sped up by this amount relative to real time.
	 */
	private static final long MULTIPLIER = 60;

	private final long startTime = System.currentTimeMillis();

	public long getStartTime() {
		return startTime;
	}

	public long getTimeMultiplier() {
		return MULTIPLIER;
	}
}
