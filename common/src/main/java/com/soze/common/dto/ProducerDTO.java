package com.soze.common.dto;

import java.util.Map;

public class ProducerDTO {

	private Map<Resource, Integer> input;
	private Map<Resource, Integer> output;

	private long time;
	private long progress;
	private boolean producing = false;
	private long productionStartTime = -1L;

	public Map<Resource, Integer> getInput() {
		return input;
	}

	public void setInput(Map<Resource, Integer> input) {
		this.input = input;
	}

	public Map<Resource, Integer> getOutput() {
		return output;
	}

	public void setOutput(Map<Resource, Integer> output) {
		this.output = output;
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

	@Override
	public String toString() {
		return "ProducerDTO{" + "input=" + input + ", output=" + output + ", time=" + time + ", progress=" + progress + ", producing=" + producing + ", productionStartTime=" + productionStartTime + '}';
	}
}
