package com.soze.defense.game.ecs.system;

import com.badlogic.gdx.math.MathUtils;
import com.soze.defense.game.GameService;
import com.soze.defense.game.ecs.NodeHelper;
import com.soze.defense.game.ecs.component.ResourceProducerComponent;
import com.soze.defense.game.pathfinder.PathFinder;
import com.soze.klecs.engine.Engine;
import com.soze.klecs.entity.Entity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResourceProducerSystem extends BaseEntitySystem {

  private static final Logger LOG = LoggerFactory.getLogger(ResourceProducerSystem.class);

  private final GameService gameService;
  private final PathFinder pathFinder;

  public ResourceProducerSystem(Engine engine, GameService gameService, PathFinder pathFinder) {
    super(engine);
    this.gameService = gameService;
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
