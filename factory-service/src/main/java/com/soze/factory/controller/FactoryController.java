package com.soze.factory.controller;

import com.soze.common.dto.FactoryDTO;
import com.soze.common.dto.ProducerDTO;
import com.soze.common.dto.StorageDTO;
import com.soze.factory.domain.Factory;
import com.soze.factory.domain.Producer;
import com.soze.factory.service.FactoryService;
import com.soze.factory.service.FactoryTemplateLoader;
import java.io.File;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FactoryController {

  private static final Logger LOG = LoggerFactory.getLogger(FactoryController.class);

  private final FactoryService factoryService;
  private final FactoryTemplateLoader factoryTemplateLoader;

  @Autowired
  public FactoryController(FactoryService factoryService,
                           FactoryTemplateLoader factoryTemplateLoader) {
    this.factoryService = factoryService;
    this.factoryTemplateLoader = factoryTemplateLoader;
  }

  @GetMapping(value = "/")
  public List<FactoryDTO> getFactories() {
    LOG.info("Calling getFactories");
    List<FactoryDTO> factoryDTOS = factoryService.getFactories().stream().map(this::convert)
                                                 .collect(Collectors.toList());
    LOG.info("Returning {} factories", factoryDTOS.size());
    return factoryDTOS;
  }

  @GetMapping(value = "/templates")
  public String getTemplate() throws Exception {
    File entities = factoryTemplateLoader.getEntities();
    List<String> list = Files.readAllLines(entities.toPath());
    return String.join("\n", list);
  }

  private FactoryDTO convert(Factory factory) {
    FactoryDTO factoryDTO = new FactoryDTO();
    factoryDTO.setId(factory.getId());
    factoryDTO.setTemplateId(factory.getTemplateId());
    factoryDTO.setName(factory.getName());
    factoryDTO.setTexture(factory.getTexture());
    factoryDTO.setWidth(factory.getWidth());
    factoryDTO.setHeight(factory.getHeight());
    factoryDTO.setX(factory.getX());
    factoryDTO.setY(factory.getY());

    StorageDTO storageDTO = new StorageDTO();
    storageDTO.setCapacity(factory.getStorage().getCapacity());
    factoryDTO.setStorage(storageDTO);

    ProducerDTO producerDTO = new ProducerDTO();
    Producer producer = factory.getProducer();
    producerDTO.setProducing(producer.isProducing());
    producerDTO.setProgress(producer.getProgress());
    producerDTO.setTime(producer.getTime());
    producerDTO.setResource(producer.getResource());
    factoryDTO.setProducer(producerDTO);

    return factoryDTO;
  }

}
