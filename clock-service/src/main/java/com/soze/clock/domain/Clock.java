package com.soze.clock.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Clock {

	private final long multiplier;
	private final long startTime;

	@JsonCreator
	public Clock(@JsonProperty("multiplier") long multiplier, @JsonProperty("startTime") long startTime
							) {
		this.multiplier = multiplier;
		this.startTime = startTime;
	}

	public long getMultiplier() {
		return multiplier;
	}

	public long getStartTime() {
		return startTime;
	}

	@JsonIgnore
	public long getCurrentGameTime() {
		long startTime = getStartTime();
		long currentRealTime = System.currentTimeMillis();
		long timePassed = currentRealTime - startTime;
		long gameTimePassed = timePassed * getMultiplier();
		return startTime + gameTimePassed;
	}

	@Override
	public String toString() {
		return "Clock{" + "multiplier=" + multiplier + ", startTime=" + startTime + ", currentGameTime = " + getCurrentGameTime() + "}";
	}
}
