package com.soze.common.dto;

public class ProducerDTO {

	private String resource;
	private long time;
	private long progress;
	private boolean producing = false;
	private long productionStartTime = -1L;

	public String getResource() {
		return resource;
	}

	public void setResource(String resource) {
		this.resource = resource;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public long getProgress() {
		return progress;
	}

	public void setProgress(long progress) {
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
