package com.soze.defense.game;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.fasterxml.jackson.databind.JsonNode;
import com.soze.common.dto.FactoryDTO;
import com.soze.defense.MyAssetManager;
import com.soze.defense.game.ecs.component.BaseComponent;
import com.soze.defense.game.ecs.component.GraphicsComponent;
import com.soze.defense.game.ecs.component.OccupyTileComponent;
import com.soze.defense.game.ecs.component.PathFollowerComponent;
import com.soze.defense.game.ecs.component.PhysicsComponent;
import com.soze.defense.game.ecs.component.ResourceProducerComponent;
import com.soze.defense.game.ecs.component.StorageComponent;
import com.soze.defense.game.ecs.component.TooltipComponent;
import com.soze.defense.game.ecs.component.WarehouseStorageComponent;
import com.soze.defense.game.world.World;
import com.soze.klecs.engine.Engine;
import com.soze.klecs.entity.Entity;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ObjectFactory {

  private static final Logger LOG = LoggerFactory.getLogger(ObjectFactory.class);

  private final MyAssetManager assetManager;
  private final Engine engine;
  private final World world;

  private final Map<String, JsonNode> templates = new HashMap<>();

  public ObjectFactory(MyAssetManager assetManager, Engine engine,
      World world) {
    this.assetManager = assetManager;
    this.engine = engine;
    this.world = world;
  }

  public void loadEntityTemplates() {
    TemplateLoader templateLoader = new TemplateLoader();
    templates.clear();
    templates.putAll(templateLoader.getTemplates());
    LOG.info("Loaded {} entity templates", templates.size());
  }

  public Entity createEntity(String id, String templateId, Vector2 position) {
    return createEntity(id, templateId, position, new HashMap<>());
  }

  public Entity createEntity(String id, String templateId, Vector2 position, Map<String, Object> context) {
    LOG.info("Creating entity with templateId {} at {}", templateId, position);
    Entity entity = engine.getEntityFactory().createEntity(id);

    JsonNode root = templates.get(templateId);
    if (root == null) {
      throw new IllegalArgumentException("No entity template with id " + templateId);
    }

    PhysicsComponent physicsComponent = new PhysicsComponent(position,
        new Vector2(Tile.WIDTH, Tile.HEIGHT));
    entity.addComponent(physicsComponent);

    JsonNode texture = root.get("texture");
    if (texture != null) {
      String textureName = texture.asText();
      Sprite sprite = new Sprite(assetManager.getTexture(textureName));
      sprite.setPosition(position.x, position.y);
      GraphicsComponent graphicsComponent = new GraphicsComponent(sprite);
      entity.addComponent(graphicsComponent);
    }

    JsonNode storage = root.get("storage");
    if (storage != null) {
      int capacity = Integer.parseInt(storage.get("capacity").asText());
      boolean warehouse = storage.get("storage") != null && Boolean.parseBoolean(storage.get("storage").asText());
      if (warehouse) {
        WarehouseStorageComponent warehouseStorageComponent = new WarehouseStorageComponent(
            capacity);
        entity.addComponent(warehouseStorageComponent);
      } else {
        StorageComponent storageComponent = new StorageComponent(capacity);
        entity.addComponent(storageComponent);
      }
    }

    JsonNode producer = root.get("producer");
    if (producer != null) {
      Resource resource = Resource.valueOf(producer.get("resource").asText());
      float time = Float.parseFloat(producer.get("time").asText());
      ResourceProducerComponent resourceProducerComponent = new ResourceProducerComponent(resource,
          time);
      entity.addComponent(resourceProducerComponent);
    }

    JsonNode pathFollower = root.get("pathfollower");
    if (pathFollower != null) {
      PathFollowerComponent pathFollowerComponent = new PathFollowerComponent();
      entity.addComponent(pathFollowerComponent);
    }

    String textureName = (String) context.get("texture");
    if (textureName != null) {
      Sprite sprite = new Sprite(assetManager.getTexture(textureName));
      sprite.setPosition(position.x, position.y);
      GraphicsComponent graphicsComponent = new GraphicsComponent(sprite);
      entity.addComponent(graphicsComponent);
    }

    entity.addComponent(new TooltipComponent());

    JsonNode occupyTile = root.get("occupyTile");
    if (occupyTile != null && occupyTile.isBoolean() && occupyTile.asBoolean()) {
      OccupyTileComponent occupyTileComponent = new OccupyTileComponent();
      Tile tile = world.getTileAt(position);
      occupyTileComponent.setTile(tile);
      entity.addComponent(occupyTileComponent);
    }

    LOG.info("Created entity {} at {} with {} components", id, position,
        entity.getAllComponents(BaseComponent.class).size());
    engine.addEntity(entity);
    return entity;
  }

  public void createFactory(FactoryDTO factoryDTO) {
    LOG.info("Creating entity from factory {}", factoryDTO.getId());
    Vector2 position = new Vector2(Tile.WIDTH * factoryDTO.getX(), Tile.HEIGHT * factoryDTO.getY());
    Tile tile = world.getTileAt(position);
    createEntity(factoryDTO.getId(), factoryDTO.getTemplateId(), tile.getCenter());
  }

}
