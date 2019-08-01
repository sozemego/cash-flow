package com.soze.defense.game.factory;


import com.badlogic.gdx.math.Vector2;
import com.soze.common.json.JsonUtils;
import com.soze.common.ws.factory.client.CreateFactory;
import com.soze.common.ws.factory.server.FactoryAdded;
import com.soze.common.ws.factory.server.ResourceProduced;
import com.soze.common.ws.factory.server.ResourceProductionStarted;
import com.soze.defense.game.ObjectFactory;
import com.soze.defense.game.Renderer;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FactoryService {

  private static final Logger LOG = LoggerFactory.getLogger(FactoryService.class);

  private final Map<String, Factory> factories = new ConcurrentHashMap<>();
  private final Queue<Runnable> tasks = new ConcurrentLinkedQueue<>();

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
    Runnable task;
    while ((task = tasks.poll()) != null) {
      task.run();
    }
    factories.values().forEach(factory -> factory.update(delta));
  }

  public void render(Renderer renderer) {
    factories.values().forEach(factory -> factory.render(renderer));
  }

  public void createFactory(String templateId, Vector2 position) {
    CreateFactory createFactory = new CreateFactory(templateId, (int) position.x, (int) position.y);
    LOG.info("Sending CreateFactory message, {}", createFactory);
    String payload = JsonUtils.serialize(createFactory);
    webSocketClient.send(payload);
  }

  public void handle(ResourceProduced message) {
    Factory factory = getById(message.getFactoryId());
    if (factory == null) {
      return;
    }

    tasks.add(() -> {
      Producer producer = factory.getProducer();
      producer.stopProduction();

      Storage storage = factory.getStorage();
      storage.addResource(message.getResource());
    });
  }

  public void handle(FactoryAdded message) {
    Factory factory = objectFactory.createFactory(message.getFactoryDTO());
    tasks.add(() -> {
      factories.put(factory.getId(), factory);
    });
  }

  public void handle(ResourceProductionStarted message) {
    Factory factory = getById(message.getFactoryId());
    if (factory == null) {
      return;
    }

    tasks.add(() -> {
      Producer producer = factory.getProducer();
      producer.startProduction();
    });
  }

  private Factory getById(String id) {
    return factories.get(id);
  }
}
