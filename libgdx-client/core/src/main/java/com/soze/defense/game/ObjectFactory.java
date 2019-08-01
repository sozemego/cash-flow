package com.soze.defense.game;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.fasterxml.jackson.databind.JsonNode;
import com.soze.common.dto.FactoryDTO;
import com.soze.common.dto.ProducerDTO;
import com.soze.common.dto.StorageDTO;
import com.soze.defense.MyAssetManager;
import com.soze.defense.game.factory.Factory;
import com.soze.defense.game.factory.Producer;
import com.soze.defense.game.factory.Storage;
import com.soze.defense.game.world.World;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ObjectFactory {

  private static final Logger LOG = LoggerFactory.getLogger(ObjectFactory.class);

  private final MyAssetManager assetManager;
  private final World world;

  private final Map<String, JsonNode> templates = new HashMap<>();

  public ObjectFactory(MyAssetManager assetManager,
                       World world) {
    this.assetManager = assetManager;
    this.world = world;
  }

  public void loadEntityTemplates() {
    TemplateLoader templateLoader = new TemplateLoader();
    templates.clear();
    templates.putAll(templateLoader.getTemplates());
    LOG.info("Loaded {} entity templates", templates.size());
  }

  public Factory createFactory(FactoryDTO factoryDTO) {
    LOG.info("Creating Factory id = {}", factoryDTO.getId());

    String name = factoryDTO.getName();
    String textureName = factoryDTO.getTexture();
    Sprite sprite = assetManager.getSprite(textureName);

    ProducerDTO producerDTO = factoryDTO.getProducer();
    Producer producer = new Producer(producerDTO.getResource(), producerDTO.getTime());
    producer.setProducing(producerDTO.isProducing());
    producer.setProgress(producerDTO.getProgress());

    StorageDTO storageDTO = factoryDTO.getStorage();
    Storage storage = new Storage(storageDTO.getCapacity());

    storageDTO.getResources().forEach((resource, count) -> {
      for (int i = 0; i < count; i++) {
        storage.addResource(resource);
      }
    });

    Vector2 position = new Vector2(factoryDTO.getX() * Tile.WIDTH, factoryDTO.getY() * Tile.HEIGHT);
    Vector2 size = new Vector2(factoryDTO.getWidth() * Tile.WIDTH,
        factoryDTO.getHeight() * Tile.HEIGHT);

    return new Factory(
        factoryDTO.getId(),
        name,
        position.add(size.x / 2, size.y / 2),
        size,
        sprite,
        producer,
        storage
    );
  }

}
