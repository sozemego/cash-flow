package com.soze.factory.controller;

import com.soze.common.dto.FactoryDTO;
import com.soze.factory.service.FactoryService;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FactoryController {

  private static final Logger LOG = LoggerFactory.getLogger(FactoryController.class);

  private final FactoryService factoryService;

  @Autowired
  public FactoryController(FactoryService factoryService) {
    this.factoryService = factoryService;
  }

  @GetMapping(value = "/")
  public List<FactoryDTO> getFactories() {
    LOG.info("Calling getFactories");
    List<FactoryDTO> factories = factoryService.getFactories();
    LOG.info("Returning {} factories", factories.size());
    return factories;
  }

}
