package com.soze.factory.aggregate;

import com.soze.common.dto.Clock;
import com.soze.common.dto.Resource;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Producer {

	private final Map<Resource, Integer> input = new HashMap<>();
	private final Map<Resource, Integer> output = new HashMap<>();

	private long time;
	private boolean producing = false;

	private long productionStartTime = -1L;

	/**
	 * Time to produce in game minutes (not real minutes).
	 */
	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public boolean isProducing() {
		return producing;
	}

	public void setProducing(boolean producing) {
		this.producing = producing;
	}

	public void startProduction(long currentGameTime) {
		if (isProducing()) {
			return;
		}
		setProducing(true);
		setProductionStartTime(currentGameTime);
	}

	public void stopProduction() {
		if (!isProducing()) {
			return;
		}
		setProducing(false);
		setProductionStartTime(-1L);
	}

	public boolean isFinished(Clock clock) {
		if (!isProducing()) {
			return false;
		}
		long gameTime = clock.getCurrentGameTime();
		long gameTimePassed = gameTime - getProductionStartTime();
		long minutesPassed = TimeUnit.MILLISECONDS.toMinutes(gameTimePassed);
		return minutesPassed >= getTime();
	}

	public long getProductionStartTime() {
		return this.productionStartTime;
	}

	private void setProductionStartTime(long productionStartTime) {
		this.productionStartTime = productionStartTime;
	}

	public Map<Resource, Integer> getInput() {
		return input;
	}

	public Map<Resource, Integer> getOutput() {
		return output;
	}

	public boolean isInput(Resource resource) {
		return input.containsKey(resource);
	}

	public boolean isOutput(Resource resource) {
		return output.containsKey(resource);
	}

	@Override
	public String toString() {
		return "Producer{" + "input=" + input + ", output=" + output + ", time=" + time + ", producing=" + producing + ", productionStartTime=" + productionStartTime + '}';
	}
}
