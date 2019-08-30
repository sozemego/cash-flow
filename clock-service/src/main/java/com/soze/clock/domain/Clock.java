package com.soze.clock.domain;

public class Clock {

	private final long multiplier;
	private final long startTime;
	private final String clockStart;

	public Clock(long multiplier, long startTime, String clockStart) {
		this.multiplier = multiplier;
		this.startTime = startTime;
		this.clockStart = clockStart;
	}

	public long getMultiplier() {
		return multiplier;
	}

	public long getStartTime() {
		return startTime;
	}

	public String getClockStart() {
		return clockStart;
	}

	@Override
	public String toString() {
		return "Clock{" + "multiplier=" + multiplier + ", startTime=" + startTime + ", clockStart='" + clockStart + '\'' + '}';
	}
}
