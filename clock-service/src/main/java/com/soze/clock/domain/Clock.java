package com.soze.clock.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Clock {

	private final long multiplier;
	private final long startTime;
	private final String clockStart;

	@JsonCreator
	public Clock(@JsonProperty("multiplier") long multiplier,
							 @JsonProperty("startTime") long startTime,
							 @JsonProperty("clockStart") String clockStart
							) {
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
