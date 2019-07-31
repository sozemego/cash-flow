package com.soze.defense.game.factory;


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
    Factory factory = getById(message.getFactoryId());
    if (factory == null) {
      return;
    }

    Producer producer = factory.getProducer();
    producer.stopProduction();

    Storage storage = factory.getStorage();
    storage.addResource(message.getResource());
  }

  public void handle(FactoryAdded message) {
    Factory factory = objectFactory.createFactory(message.getFactoryDTO());
    factories.add(factory);
  }

  public void handle(ResourceProductionStarted message) {
    Factory factory = getById(message.getFactoryId());
    if (factory == null) {
      return;
    }

    Producer producer = factory.getProducer();
    producer.startProduction();
  }

  private Factory getById(String id) {
    for (Factory factory : factories) {
      if (factory.getId().equals(id)) {
        return factory;
      }
    }
    return null;
  }
}
