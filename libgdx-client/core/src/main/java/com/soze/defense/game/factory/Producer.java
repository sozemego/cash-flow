package com.soze.defense.game.factory;

import com.soze.common.dto.Resource;

public class Producer {

  private final Resource resource;
  private final float time;
  private float progress;
  private boolean producing = false;

  private long productionStartTime = -1L;

  public Producer(Resource resource, float time) {
    this.resource = resource;
    this.time = time;
  }

  public Resource getResource() {
    return resource;
  }

  public float getTime() {
    return time;
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

  private void setProductionStartTime(long productionStartTime) {
    this.productionStartTime = productionStartTime;
  }

  private long getProductionStartTime() {
    return this.productionStartTime;
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
