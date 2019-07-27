package com.soze.defense.game.factory;


import com.soze.common.dto.FactoryDTO;
import com.soze.defense.game.ObjectFactory;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FactoryService {

  private static final Logger LOG = LoggerFactory.getLogger(FactoryService.class);

  private final FactoryClient client;
  private final FactoryWebSocketClient webSocketClient;
  private final ObjectFactory objectFactory;

  public FactoryService(FactoryClient client,
                        FactoryWebSocketClient webSocketClient,
                        ObjectFactory objectFactory) {
    this.client = client;
    this.webSocketClient = webSocketClient;
    this.objectFactory = objectFactory;
  }

  public void init() {
    LOG.info("FactoryService init...");
    try {
      LOG.info("Connecting to websocket client");
      webSocketClient.connectBlocking();
    } catch (InterruptedException e) {
      throw new IllegalStateException(e);
    }

    if (!webSocketClient.isOpen()) {
      throw new IllegalStateException("websocket client is not open");
    }

    LOG.info("Connected to websocket client {}", webSocketClient.isOpen());
    List<FactoryDTO> factories = client.getFactories();
    for (FactoryDTO factory : factories) {
      objectFactory.createFactory(factory);
    }
  }

}
