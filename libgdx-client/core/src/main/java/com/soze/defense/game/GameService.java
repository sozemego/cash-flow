package com.soze.defense.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.soze.defense.MyAssetManager;
import com.soze.defense.game.ecs.NodeHelper;
import com.soze.defense.game.ecs.component.BaseStorage;
import com.soze.defense.game.ecs.component.OccupyTileComponent;
import com.soze.defense.game.ecs.component.PathFollowerComponent;
import com.soze.defense.game.ecs.component.PhysicsComponent;
import com.soze.defense.game.ecs.system.GraphicsSystem;
import com.soze.defense.game.ecs.system.PathFollowerRenderingSystem;
import com.soze.defense.game.ecs.system.PathFollowerSystem;
import com.soze.defense.game.ecs.system.ResourceProducerSystem;
import com.soze.defense.game.ecs.system.TooltipSystem;
import com.soze.defense.game.pathfinder.Path;
import com.soze.defense.game.pathfinder.PathFinder;
import com.soze.defense.game.world.World;
import com.soze.defense.input.MousePointer;
import com.soze.klecs.engine.Engine;
import com.soze.klecs.entity.Entity;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GameService {

  private static final Logger LOG = LoggerFactory.getLogger(GameService.class);

  private final MyAssetManager assetManager;

  private final World world;
  private final Engine engine = new Engine();
  private final ObjectFactory objectFactory;
  private final SpriteBatch batch;
  private final MousePointer mousePointer;

  public GameService(World world, MyAssetManager assetManager, SpriteBatch batch,
      MousePointer mousePointer) {
    this.world = world;
    this.assetManager = assetManager;
    this.objectFactory = new ObjectFactory(assetManager, engine.getEntityFactory(), world);
    this.batch = batch;
    this.mousePointer = mousePointer;
  }

  public void init() {
    objectFactory.loadEntityTemplates();
    world.init();
    engine.addSystem(new ResourceProducerSystem(engine, this, new PathFinder(this)));
    engine.addSystem(new TooltipSystem(engine, batch, assetManager, mousePointer));
    engine.addSystem(new PathFollowerSystem(engine));
    engine.addSystem(new PathFollowerRenderingSystem(engine, batch, assetManager));
    engine.addSystem(new GraphicsSystem(engine, batch));

    createObject("FORESTER", new Vector2(240, 240));
    createObject("FORESTER", new Vector2(2400, 2400));
    createObject("FORESTER", new Vector2(2400, 0));
    createObject("FORESTER", new Vector2(360, 240));
    createObject("FORESTER", new Vector2(360, 0));
    createObject("WAREHOUSE", new Vector2(600, 600));
  }

  public void update(float delta) {
    engine.update(delta);
  }

  public void render(SpriteBatch batch, float delta) {
    engine.render(delta);
  }

  public void createObject(String id, Vector2 position) {
    Tile tile = world.getTileAt(position);
    if (tile == null) {
      LOG.info("Tile does not exist at {}. Create object call skipped", position);
      return;
    }
    engine.addEntity(objectFactory.createEntity(id, tile.getCenter()));
  }

  public List<Entity> getAllEntities() {
    return engine.getAllEntities();
  }

  public Tile getTileUnderEntity(Entity entity) {
    PhysicsComponent physicsComponent = entity.getComponent(PhysicsComponent.class);
    Vector2 position = physicsComponent.getPosition();
    return world.getTileAt(position);
  }

  public List<Tile> getNeighbours(Tile center, boolean diagonal) {
    Stream<Tile> tiles = null;
    if (diagonal) {
      tiles = Stream.of(
          world.getTileAt(center.getPosition().cpy().add(-128, 128)),
          world.getTileAt(center.getPosition().cpy().add(0, 128)),
          world.getTileAt(center.getPosition().cpy().add(128, 128)),
          world.getTileAt(center.getPosition().cpy().add(-128, 0)),
          world.getTileAt(center.getPosition().cpy().add(128, 0)),
          world.getTileAt(center.getPosition().cpy().add(-128, -128)),
          world.getTileAt(center.getPosition().cpy().add(0, -128)),
          world.getTileAt(center.getPosition().cpy().add(128, -128))
      );
    } else {
      tiles = Stream.of(
          world.getTileAt(center.getPosition().cpy().add(0, 128)),
          world.getTileAt(center.getPosition().cpy().add(-128, 0)),
          world.getTileAt(center.getPosition().cpy().add(128, 0)),
          world.getTileAt(center.getPosition().cpy().add(0, -128))
      );
    }

    return tiles
        .filter(Objects::nonNull)
        .collect(Collectors.toList());
  }

  public Optional<Entity> getEntityOnTile(Tile tile) {
    return engine.getEntitiesByNode(NodeHelper.OCCUPY_TILE).stream().filter(entity -> {
      OccupyTileComponent occupyTileComponent = entity.getComponent(OccupyTileComponent.class);
      return occupyTileComponent.getTile() == tile;
    }).findFirst();
  }

  public boolean isTileFree(Tile tile) {
    return !getEntityOnTile(tile).isPresent();
  }

  public void transportResource(Entity consumer, Path path, Resource resource) {
    Map<String, Object> context = new HashMap<>();
    String resourceName = resource.toString().toLowerCase();
    context.put("texture", "textures/resources/" + resourceName + ".png");
    Vector2 position = path.getCurrent().get().getCenter();
    Entity transportedResource = objectFactory
        .createEntity("resource_path_follower", position, context);
    PhysicsComponent physicsComponent = transportedResource.getComponent(PhysicsComponent.class);
    physicsComponent.setPosition(position.x, position.y);
    physicsComponent.setSize(32, 32);
    transportedResource.getComponent(PathFollowerComponent.class).setPath(path);
    path.next();
    engine.addEntity(transportedResource);

    path.setOnFinish(() -> {
      BaseStorage storage = consumer.getComponentByParent(BaseStorage.class);
      if (storage == null) {
        return;
      }
      storage.addResource(resource);
    });
  }

}
