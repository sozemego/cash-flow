package com.soze.clock.service;

import org.springframework.stereotype.Service;

import java.time.LocalTime;

@Service
public class ClockService {

	/**
	 * Time in game is sped up by this amount relative to real time.
	 */
	private static final long MULTIPLIER = 60;

	/**
	 * Time at which the game clock starts.
	 */
	private final LocalTime clockStart = LocalTime.of(12, 0);

	private final long startTime = System.currentTimeMillis();


	public LocalTime getClockStart() {
		return clockStart;
	}

	public long getStartTime() {
		return startTime;
	}

	public long getTimeMultiplier() {
		return MULTIPLIER;
	}
}
