package com.soze.factory.service;

import com.soze.common.dto.FactoryDTO;
import com.soze.common.dto.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.annotation.PostConstruct;
import org.springframework.stereotype.Service;

@Service
public class FactoryService {

  private final List<FactoryDTO> factories = new ArrayList<>();

  @PostConstruct
  public void setup() {
    FactoryDTO forester1 = new FactoryDTO();
    forester1.setTemplateId("FORESTER");
    forester1.setId(UUID.randomUUID().toString());
    forester1.setX(4);
    forester1.setY(4);
    forester1.setWidth(128);
    forester1.setHeight(128);
    forester1.setResource(Resource.WOOD);
    forester1.setName("Forester");
    forester1.setTexture("textures/buildings/medieval/medieval_lumber.png");
    factories.add(forester1);
    FactoryDTO forester2 = new FactoryDTO();
    forester2.setTemplateId("FORESTER");
    forester2.setId(UUID.randomUUID().toString());
    forester2.setX(6);
    forester2.setY(6);
    forester2.setWidth(128);
    forester2.setHeight(128);
    forester2.setResource(Resource.WOOD);
    forester2.setName("Forester");
    forester2.setTexture("textures/buildings/medieval/medieval_lumber.png");
    factories.add(forester2);
  }

  public List<FactoryDTO> getFactories() {
    return factories;
  }

}
