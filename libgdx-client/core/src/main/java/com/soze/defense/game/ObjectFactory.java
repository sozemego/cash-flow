package com.soze.defense.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
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
import com.soze.klecs.entity.Entity;
import com.soze.klecs.entity.EntityFactory;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ObjectFactory {

  private static final Logger LOG = LoggerFactory.getLogger(ObjectFactory.class);

  private final MyAssetManager assetManager;
  private final EntityFactory entityFactory;
  private final World world;

  private final Map<String, Element> entities = new HashMap<>();

  public ObjectFactory(MyAssetManager assetManager, EntityFactory entityFactory,
      World world) {
    this.assetManager = assetManager;
    this.entityFactory = entityFactory;
    this.world = world;
  }

  public void loadEntityTemplates() {
    String path = "data/entities.xml";
    LOG.info("Loading entity templates from {}...", path);
    XmlReader xmlReader = new XmlReader();
    Element root = xmlReader.parse(Gdx.files.internal(path));
    Array<Element> elements = root.getChildrenByName("entity");
    elements.forEach(entity -> entities.put(entity.getChildByName("id").getText(), entity));
    LOG.info("Loaded {} entity templates", entities.size());
  }

  public Entity createEntity(String id, Vector2 position) {
    return createEntity(id, position, new HashMap<>());
  }

  public Entity createEntity(String id, Vector2 position, Map<String, Object> context) {
    LOG.info("Creating entity {} at {}", id, position);
    Entity entity = entityFactory.createEntity();

    Element element = entities.get(id);
    if (element == null) {
      throw new IllegalArgumentException("No entity with id " + id);
    }

    PhysicsComponent physicsComponent = new PhysicsComponent(position,
        new Vector2(Tile.WIDTH, Tile.HEIGHT));
    entity.addComponent(physicsComponent);

    Element texture = element.getChildByName("texture");
    if (texture != null) {
      String textureName = texture.getText();
      Sprite sprite = new Sprite(assetManager.getTexture(textureName));
      sprite.setPosition(position.x, position.y);
      GraphicsComponent graphicsComponent = new GraphicsComponent(sprite);
      entity.addComponent(graphicsComponent);
    }

    Element storage = element.getChildByName("storage");
    if (storage != null) {
      int capacity = Integer.parseInt(storage.getChildByName("capacity").getText());
      boolean warehouse = Boolean.parseBoolean(storage.getAttribute("warehouse", "false"));
      if (warehouse) {
        WarehouseStorageComponent warehouseStorageComponent = new WarehouseStorageComponent(
            capacity);
        entity.addComponent(warehouseStorageComponent);
      } else {
        StorageComponent storageComponent = new StorageComponent(capacity);
        entity.addComponent(storageComponent);
      }
    }

    Element producer = element.getChildByName("producer");
    if (producer != null) {
      Resource resource = Resource.valueOf(producer.getChildByName("resource").getText());
      float time = Float.parseFloat(producer.getChildByName("time").getText());
      ResourceProducerComponent resourceProducerComponent = new ResourceProducerComponent(resource,
          time);
      entity.addComponent(resourceProducerComponent);
    }

    Element pathFollower = element.getChildByName("pathfollower");
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

    Element occupyTile = element.getChildByName("occupy_tile");
    if (occupyTile != null) {
      OccupyTileComponent occupyTileComponent = new OccupyTileComponent();
      Tile tile = world.getTileAt(position);
      occupyTileComponent.setTile(tile);
      entity.addComponent(occupyTileComponent);
    }

    LOG.info("Created entity {} at {} with {} components", id, position,
        entity.getAllComponents(BaseComponent.class).size());
    return entity;
  }
}
