package com.soze.defense.game.ecs.system;

import com.soze.klecs.engine.Engine;
import com.soze.klecs.system.EntitySystem;

public abstract class BaseEntitySystem implements EntitySystem {

  private final Engine engine;

  protected BaseEntitySystem(Engine engine) {
    this.engine = engine;
  }

  @Override
  public Engine getEngine() {
    return engine;
  }

  @Override
  public boolean isRenderer() {
    return false;
  }
}
