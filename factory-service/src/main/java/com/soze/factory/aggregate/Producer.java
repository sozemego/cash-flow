package com.soze.factory.aggregate;

import com.soze.common.dto.Clock;
import com.soze.common.dto.Resource;

import java.util.concurrent.TimeUnit;

public class Producer {

	private Resource resource;
	private long time;
	private boolean producing = false;

	private long productionStartTime = -1L;

	public Resource getResource() {
		return resource;
	}

	public void setResource(Resource resource) {
		this.resource = resource;
	}

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

	@Override
	public String toString() {
		return "Producer{" + "resource=" + resource + ", time=" + time + ", producing=" + producing + '}';
	}
}
