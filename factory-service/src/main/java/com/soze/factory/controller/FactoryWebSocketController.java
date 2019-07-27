package com.soze.factory.controller;

import com.soze.factory.service.FactoryService;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
public class FactoryWebSocketController extends TextWebSocketHandler {

  private static final Logger LOG = LoggerFactory.getLogger(FactoryWebSocketController.class);

  private final Map<String, WebSocketSession> sessions = new HashMap<>();

  private final FactoryService factoryService;

  @Autowired
  public FactoryWebSocketController(FactoryService factoryService) {
    this.factoryService = factoryService;
  }

  @Override
  public void afterConnectionEstablished(WebSocketSession session) throws Exception {
    LOG.info("{} connected", session.getId());
    sessions.put(session.getId(), session);
    factoryService.addSession(session);
  }

  @Override
  public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
    LOG.info("{} disconnected", session.getId());
    sessions.remove(session.getId());
    factoryService.removeSession(session);
  }
}