package com.soze.defense.game.ecs.system;

import static com.soze.defense.game.ecs.NodeHelper.ACCEPTS_RESOURCE_PREDICATE;

import com.badlogic.gdx.math.MathUtils;
import com.soze.defense.game.GameService;
import com.soze.defense.game.Resource;
import com.soze.defense.game.Tile;
import com.soze.defense.game.ecs.NodeHelper;
import com.soze.defense.game.ecs.component.BaseStorage;
import com.soze.defense.game.ecs.component.ResourceProducerComponent;
import com.soze.defense.game.ecs.component.StorageComponent;
import com.soze.defense.game.pathfinder.Path;
import com.soze.defense.game.pathfinder.PathFinder;
import com.soze.klecs.engine.Engine;
import com.soze.klecs.entity.Entity;
import java.util.List;
import java.util.stream.Collectors;
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
    StorageComponent storageComponent = entity.getComponent(StorageComponent.class);

    float progress = resourceProducerComponent.getProgress();
    boolean started = progress != 0f;

    if (!started && storageComponent != null && storageComponent.isFull()) {
      return;
    } else {
      resourceProducerComponent.setProgress(progress + delta);
    }

    float percent = progress / resourceProducerComponent.getTime();
    if (MathUtils.isEqual(progress, resourceProducerComponent.getTime(), 0.05f) || percent >= 1f) {
      resourceProducerComponent.setProgress(0);
      if (storageComponent != null) {
        storageComponent.addResource(resourceProducerComponent.getResource());
        findConsumer(entity, resourceProducerComponent.getResource());
      }
      LOG.trace("Produced {}", resourceProducerComponent.getResource());
    }

  }

  private void findConsumer(Entity entity, Resource resource) {
    //1. find all consumers for the resource
    List<Entity> consumers = gameService.getAllEntities()
        .stream()
        .filter(consumer -> !consumer.getId().equals(entity.getId()))
        .filter(ACCEPTS_RESOURCE_PREDICATE)
        .filter(consumer -> {
          BaseStorage storage = consumer.getComponentByParent(BaseStorage.class);
          return storage.canFit(resource);
        })
        .filter(consumer -> {
          ResourceProducerComponent producerComponent = consumer
              .getComponent(ResourceProducerComponent.class);
          if (producerComponent == null) {
            return true;
          }
          return !resource.equals(producerComponent.getResource());
        })
        .collect(Collectors.toList());

    if (consumers.isEmpty()) {
      return;
    }

    Entity consumer = consumers.get(0);

    //2. find path to consumer
    Tile from = gameService.getTileUnderEntity(entity);
    LOG.trace("from {}", from);
    Tile to = gameService.getTileUnderEntity(consumer);
    LOG.trace("to {}", to);
    //assume path is always found
    Path path = pathFinder.findPath(from, to);
    LOG.debug("path {}", path);
    gameService.transportResource(consumer, path, resource);

    StorageComponent storageComponent = entity.getComponent(StorageComponent.class);
    storageComponent.removeResource(resource);
  }

}
