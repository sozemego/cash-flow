package com.soze.defense.game.ecs.system;

import com.badlogic.gdx.math.MathUtils;
import com.soze.defense.game.Game;
import com.soze.defense.game.ecs.NodeHelper;
import com.soze.defense.game.ecs.component.ResourceProducerComponent;
import com.soze.defense.game.pathfinder.PathFinder;
import com.soze.klecs.engine.Engine;
import com.soze.klecs.entity.Entity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResourceProducerSystem extends BaseEntitySystem {

  private static final Logger LOG = LoggerFactory.getLogger(ResourceProducerSystem.class);

  private final Game game;
  private final PathFinder pathFinder;

  public ResourceProducerSystem(Engine engine, Game game, PathFinder pathFinder) {
    super(engine);
    this.game = game;
    this.pathFinder = pathFinder;
  }

  @Override
  public void update(float delta) {
    getEngine().getEntitiesByNode(NodeHelper.RESOURCE_PRODUCER)
               .forEach(entity -> update(entity, delta));
  }

  private void update(Entity entity, float delta) {
    ResourceProducerComponent resourceProducerComponent = entity
        .getComponent(ResourceProducerComponent.class);

    float progress = resourceProducerComponent.getProgress();
    boolean producing = resourceProducerComponent.isProducing();

    if (!producing) {
      return;
    }

    float nextProgress = progress + (delta * 1000);
    resourceProducerComponent
        .setProgress(MathUtils.clamp(nextProgress, 0, resourceProducerComponent.getTime()));
  }

}
