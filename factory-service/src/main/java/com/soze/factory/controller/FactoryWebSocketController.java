package com.soze.factory.controller;

import com.soze.factory.service.FactoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
public class FactoryWebSocketController extends TextWebSocketHandler {

  private static final Logger LOG = LoggerFactory.getLogger(FactoryWebSocketController.class);

  private final FactoryService factoryService;

  @Autowired
  public FactoryWebSocketController(FactoryService factoryService) {
    this.factoryService = factoryService;
  }

  @Override
  public void afterConnectionEstablished(WebSocketSession session) throws Exception {
    LOG.info("{} connected", session.getId());
    super.afterConnectionEstablished(session);
  }
}
