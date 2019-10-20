package com.soze.clock.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "clock")
public class Clock {

	@Id
	private int id;

	@Column(name = "startTime")
	private long startTime;

	@Column(name = "timeMultiplier")
	private int timeMultiplier;

	public Clock() {

	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public int getTimeMultiplier() {
		return timeMultiplier;
	}

	public void setTimeMultiplier(int timeMultiplier) {
		this.timeMultiplier = timeMultiplier;
	}

	@Override
	public String toString() {
		return "Clock{" + "id=" + id + ", startTime=" + startTime + ", timeMultiplier=" + timeMultiplier + '}';
	}
}
