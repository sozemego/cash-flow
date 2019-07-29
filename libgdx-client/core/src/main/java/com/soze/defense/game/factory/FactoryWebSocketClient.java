package com.soze.defense.game.factory;

import com.soze.common.json.JsonUtils;
import com.soze.common.ws.factory.server.FactoryAdded;
import com.soze.common.ws.factory.server.ResourceProduced;
import com.soze.common.ws.factory.server.ResourceProductionStarted;
import com.soze.common.ws.factory.server.ServerMessage;
import java.net.URI;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FactoryWebSocketClient extends WebSocketClient {

  private static final Logger LOG = LoggerFactory.getLogger(FactoryWebSocketClient.class);

  private FactoryService factoryService;

  public FactoryWebSocketClient(URI serverUri) {
    super(serverUri);
  }

  public void connectBlocking(FactoryService factoryService) {
    this.factoryService = factoryService;
    try {
      super.connectBlocking();
    } catch (InterruptedException e) {
      throw new IllegalStateException(e);
    }
  }

  @Override
  public void onOpen(ServerHandshake serverHandshake) {
    LOG.info("Connection to server opened, {}", serverHandshake.getHttpStatus());
  }

  @Override
  public void onMessage(String message) {
    ServerMessage serverMessage = JsonUtils.parse(message, ServerMessage.class);
    LOG.info("Websocket message from server, type {}", serverMessage.getType());
    if (serverMessage instanceof ResourceProduced) {
      factoryService.handle((ResourceProduced) serverMessage);
    }
    if (serverMessage instanceof FactoryAdded) {
      factoryService.handle((FactoryAdded) serverMessage);
    }
    if (serverMessage instanceof ResourceProductionStarted) {
      factoryService.handle((ResourceProductionStarted) serverMessage);
    }
  }

  @Override
  public void onClose(int i, String s, boolean b) {

  }

  @Override
  public void onError(Exception e) {
    LOG.info("", e);
  }

  public static FactoryWebSocketClient create(String url) {
    try {
      return new FactoryWebSocketClient(new URI(url));
    } catch (Exception e) {
      throw new IllegalStateException(e);
    }
  }
}
