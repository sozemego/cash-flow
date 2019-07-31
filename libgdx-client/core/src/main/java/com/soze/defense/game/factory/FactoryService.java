package com.soze.defense.game.factory;


import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.soze.common.ws.factory.server.FactoryAdded;
import com.soze.common.ws.factory.server.ResourceProduced;
import com.soze.common.ws.factory.server.ResourceProductionStarted;
import com.soze.defense.game.ObjectFactory;
import com.soze.defense.game.Renderer;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FactoryService {

  private static final Logger LOG = LoggerFactory.getLogger(FactoryService.class);

  private final List<Factory> factories = new ArrayList<>();

  private final FactoryWebSocketClient webSocketClient;
  private final ObjectFactory objectFactory;

  public FactoryService(FactoryWebSocketClient webSocketClient,
                        ObjectFactory objectFactory) {
    this.webSocketClient = webSocketClient;
    this.objectFactory = objectFactory;
  }

  public void init() {
    LOG.info("FactoryService init...");

    LOG.info("Connecting to websocket client");
    webSocketClient.connectBlocking(this);

    if (!webSocketClient.isOpen()) {
      throw new IllegalStateException("websocket client is not open");
    }

    LOG.info("Connected to websocket client");
  }

  public void update(float delta) {
    factories.forEach(factory -> factory.update(delta));
  }

  public void render(Renderer renderer) {
    factories.forEach(factory -> factory.render(renderer));
  }

  public void handle(ResourceProduced message) {
//    Optional<Entity> factoryOptional = engine.getEntityById(message.getFactoryId());
//
//    if (!factoryOptional.isPresent()) {
//      return;
//    }
//
//    Entity factory = factoryOptional.get();
//
//    ResourceProducerComponent resourceProducerComponent = factory
//        .getComponent(ResourceProducerComponent.class);
//    resourceProducerComponent.setProducing(false);
//    resourceProducerComponent.setProgress(0);
//
//    BaseStorage storage = factory.getComponentByParent(BaseStorage.class);
//    storage.addResource(message.getResource());
  }

  public void handle(FactoryAdded message) {
    Factory factory = objectFactory.createFactory(message.getFactoryDTO());
    factories.add(factory);
  }

  public void handle(ResourceProductionStarted message) {
//    Optional<Entity> factoryOptional = engine.getEntityById(message.getFactoryId());
//
//    if (!factoryOptional.isPresent()) {
//      return;
//    }
//
//    Entity factory = factoryOptional.get();
//    ResourceProducerComponent resourceProducerComponent = factory
//        .getComponent(ResourceProducerComponent.class);
//    resourceProducerComponent.setProducing(true);
//    resourceProducerComponent.setProgress(0);
  }
}
