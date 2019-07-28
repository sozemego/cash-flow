package com.soze.defense.game.ecs.component;

import com.soze.common.dto.Resource;

public class ResourceProducerComponent extends BaseComponent {

  private final Resource resource;
  private final float time;
  private float progress;
  private boolean producing;

  public ResourceProducerComponent(Resource resource, float time) {
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
}
