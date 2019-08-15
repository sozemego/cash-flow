package com.soze.common.dto;

public class ProducerDTO {

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
		return progress;
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

	public long getProductionStartTime() {
		return productionStartTime;
	}

	public void setProductionStartTime(long productionStartTime) {
		this.productionStartTime = productionStartTime;
	}
}
