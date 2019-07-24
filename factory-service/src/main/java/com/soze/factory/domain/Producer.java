package com.soze.factory.domain;

import com.soze.common.dto.Resource;

public class Producer {

  private Resource resource;
  private float time;
  private float progress;
  private boolean producing = false;

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

  @Override
  public String toString() {
    return "Producer{" +
        "resource=" + resource +
        ", time=" + time +
        ", progress=" + progress +
        ", producing=" + producing +
        '}';
  }
}
