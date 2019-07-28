package com.soze.defense.game.factory;


import com.soze.common.ws.factory.server.FactoryAdded;
import com.soze.common.ws.factory.server.ResourceProduced;
import com.soze.common.ws.factory.server.ResourceProductionStarted;
import com.soze.defense.game.ObjectFactory;
import com.soze.defense.game.ecs.component.BaseStorage;
import com.soze.defense.game.ecs.component.ResourceProducerComponent;
import com.soze.klecs.engine.Engine;
import com.soze.klecs.entity.Entity;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FactoryService {

  private static final Logger LOG = LoggerFactory.getLogger(FactoryService.class);

  private final FactoryClient client;
  private final FactoryWebSocketClient webSocketClient;
  private final ObjectFactory objectFactory;
  private final Engine engine;

  public FactoryService(FactoryClient client,
                        FactoryWebSocketClient webSocketClient,
                        ObjectFactory objectFactory, Engine engine) {
    this.client = client;
    this.webSocketClient = webSocketClient;
    this.objectFactory = objectFactory;
    this.engine = engine;
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

  public void handle(ResourceProduced message) {
    Optional<Entity> factoryOptional = engine.getEntityById(message.getFactoryId());

    if (!factoryOptional.isPresent()) {
      return;
    }

    Entity factory = factoryOptional.get();

    ResourceProducerComponent resourceProducerComponent = factory.getComponent(ResourceProducerComponent.class);
    resourceProducerComponent.setProducing(false);
    resourceProducerComponent.setProgress(0);

    BaseStorage storage = factory.getComponentByParent(BaseStorage.class);
    storage.addResource(message.getResource());
  }

  public void handle(FactoryAdded message) {
    objectFactory.createFactory(message.getFactoryDTO());
  }

  public void handle(ResourceProductionStarted message) {

  }
}
