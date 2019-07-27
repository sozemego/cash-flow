package com.soze.factory.service;

import com.soze.common.json.JsonUtils;
import com.soze.common.ws.factory.server.ResourceProduced;
import com.soze.common.ws.factory.server.ServerMessage;
import com.soze.factory.domain.Factory;
import com.soze.factory.domain.Producer;
import com.soze.factory.domain.Storage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

@Service
public class FactoryService {

  private static final Logger LOG = LoggerFactory.getLogger(FactoryService.class);

  private final List<Factory> factories = new ArrayList<>();

  private final FactoryTemplateLoader templateLoader;

  private final Set<WebSocketSession> sessions = new HashSet<>();

  private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

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

    startProducing(factory);

    this.factories.add(factory);
  }

  private void startProducing(Factory factory) {
    Producer producer = factory.getProducer();
    if (producer.isProducing()) {
      return;
    }
    float timeRemaining = producer.getTime() - producer.getProgress();
    executorService.schedule(() -> {
      finishProducing(factory);
      startProducing(factory);
    }, (long) timeRemaining, TimeUnit.MILLISECONDS);
  }

  private void finishProducing(Factory factory) {
    LOG.info("Factory {} finished producing {}", factory.getId(), factory.getProducer().getResource());
    Producer producer = factory.getProducer();
    Storage storage = factory.getStorage();
    producer.setProgress(0);
    producer.setProducing(false);
    storage.addResource(producer.getResource());
    sendToAll(new ResourceProduced(factory.getId(), producer.getResource()));
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

  private void sendToAll(ServerMessage serverMessage) {
    TextMessage textMessage = new TextMessage(JsonUtils.serialize(serverMessage));
    try {
      for (WebSocketSession session : sessions) {
        session.sendMessage(textMessage);
      }
    } catch (IOException e) {
      LOG.warn("Exception when sending a server message", e);
    }

  }

}
