package com.soze.defense.game.ecs.component;

import com.soze.defense.game.Resource;

public class ResourceProducerComponent extends BaseComponent {

  private final Resource resource;
  private final float time;
  private float progress;

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
}
