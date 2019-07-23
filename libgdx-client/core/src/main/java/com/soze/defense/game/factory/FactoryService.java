package com.soze.defense.game.factory;


import com.soze.common.dto.FactoryDTO;
import com.soze.defense.game.ObjectFactory;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FactoryService {

  private static final Logger LOG = LoggerFactory.getLogger(FactoryService.class);

  private final FactoryClient client;
  private final ObjectFactory objectFactory;

  public FactoryService(FactoryClient client, ObjectFactory objectFactory) {
    this.client = client;
    this.objectFactory = objectFactory;
  }

  public void init() {
    LOG.info("FactoryService init...");
    List<FactoryDTO> factories = client.getFactories();
    for (FactoryDTO factory : factories) {
      objectFactory.createFactory(factory);
    }
  }

}
