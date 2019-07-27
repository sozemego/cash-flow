package com.soze.factory.service;

import com.soze.factory.domain.Factory;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

@Service
public class FactoryService {

  private static final Logger LOG = LoggerFactory.getLogger(FactoryService.class);

  private final List<Factory> factories = new ArrayList<>();

  private final FactoryTemplateLoader templateLoader;

  private final Set<WebSocketSession> sessions = new HashSet<>();

  @Autowired
  public FactoryService(FactoryTemplateLoader templateLoader) {
    this.templateLoader = templateLoader;
  }

  @PostConstruct
  public void setup() {
    Factory forester1 = templateLoader.constructFactoryByTemplateId("FORESTER");
    forester1.setX(4);
    forester1.setY(4);
    addFactory(forester1);

    Factory forester2 = templateLoader.constructFactoryByTemplateId("FORESTER");
    forester2.setX(6);
    forester2.setY(6);
    addFactory(forester2);
    LOG.info("Created {} factories", factories.size());
  }

  public void addFactory(Factory factory) {
    LOG.info("Adding factory {}", factory);
    this.factories.add(factory);
  }

  public List<Factory> getFactories() {
    return factories;
  }

  public void addSession(WebSocketSession session) {
    sessions.add(session);
  }

  public void removeSession(WebSocketSession session) {
    sessions.remove(session);
  }

}
