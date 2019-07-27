package com.soze.factory.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.soze.common.dto.Resource;
import com.soze.common.json.JsonUtils;
import com.soze.factory.domain.Factory;
import com.soze.factory.domain.Producer;
import com.soze.factory.domain.Storage;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Loads and holds templates for factories.
 */
@Service
public class FactoryTemplateLoader {

  private static final Logger LOG = LoggerFactory.getLogger(FactoryTemplateLoader.class);

  @Value("classpath:entities.json")
  private org.springframework.core.io.Resource entities;

  private final Map<String, JsonNode> jsonEntities = new HashMap<>();

  @PostConstruct
  public void setup() {
    LOG.info("Factory templates init...");
    try {
      List<JsonNode> objects = JsonUtils.parseList(entities.getFile(), JsonNode.class);
      for (JsonNode object : objects) {
        jsonEntities.put(object.get("id").asText(), object);
      }
      LOG.info("Loaded {} entities", jsonEntities.size());
    } catch (IOException e) {
      throw new IllegalStateException(e);
    }
  }

  public Factory constructFactoryByTemplateId(String id) {
    JsonNode root = findRootById(Objects.requireNonNull(id));
    if (root == null) {
      throw new IllegalArgumentException("Did not find template by id " + id);
    }

    Factory factory = new Factory();
    factory.setId(UUID.randomUUID().toString());
    factory.setWidth(root.get("width").asInt());
    factory.setHeight(root.get("height").asInt());

    factory.setTemplateId(root.get("id").asText());
    factory.setName(root.get("name").asText());
    factory.setTexture(root.get("texture").asText());

    JsonNode storageNode = root.get("storage");
    Storage storage = new Storage(storageNode.get("capacity").asInt());
    factory.setStorage(storage);

    JsonNode producerNode = root.get("producer");
    Producer producer = new Producer();
    producer.setProducing(false);
    producer.setProgress(0);
    producer.setTime(producerNode.get("time").asInt());
    producer.setResource(Resource.valueOf(producerNode.get("resource").asText()));
    factory.setProducer(producer);

    return factory;
  }

  private JsonNode findRootById(String id) {
    return jsonEntities.get(id);
  }

}
