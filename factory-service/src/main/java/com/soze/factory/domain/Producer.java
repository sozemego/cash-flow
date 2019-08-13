package com.soze.factory.domain;

import com.soze.common.dto.Resource;

public class Producer {

	private Resource resource;
	private float time;
	private float progress;
	private boolean producing = false;

	private long productionStartTime = -1L;

	public Resource getResource() {
		return resource;
	}

	public void setResource(Resource resource) {
		this.resource = resource;
	}

	public float getTime() {
		return time;
	}

	public void setTime(float time) {
		this.time = time;
	}

	public float getProgress() {
		long now = System.currentTimeMillis();
		return now - productionStartTime;
	}

	public void setProgress(float progress) {
		this.progress = progress;
	}

	public boolean isProducing() {
		return producing;
	}

	public void setProducing(boolean producing) {
		this.producing = producing;
	}

	public void startProduction() {
		if (isProducing()) {
			return;
		}
		setProducing(true);
		setProgress(0);
		setProductionStartTime(System.currentTimeMillis());
	}

	public void stopProduction() {
		if (!isProducing()) {
			return;
		}
		setProducing(false);
		setProgress(0);
		setProductionStartTime(-1L);
	}

	private long getProductionStartTime() {
		return this.productionStartTime;
	}

	private void setProductionStartTime(long productionStartTime) {
		this.productionStartTime = productionStartTime;
	}

	@Override
	public String toString() {
		return "Producer{" + "resource=" + resource + ", time=" + time + ", progress=" + progress + ", producing=" + producing + '}';
	}
}
