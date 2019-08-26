package com.soze.factory.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.soze.common.dto.Resource;
import com.soze.common.json.JsonUtils;
import com.soze.factory.domain.Factory;
import com.soze.factory.domain.Producer;
import com.soze.factory.domain.Storage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Loads and holds templates for factories.
 */
@Service
public class FactoryTemplateLoader {

	private static final Logger LOG = LoggerFactory.getLogger(FactoryTemplateLoader.class);
	private final Map<String, JsonNode> jsonEntities = new HashMap<>();

	@Value("classpath:factories.json")
	private org.springframework.core.io.Resource entities;

	@PostConstruct
	public void setup() throws Exception {
		LOG.info("Factory templates init...");
		List<JsonNode> objects = JsonUtils.parseList(getEntities(), JsonNode.class);
		for (JsonNode object : objects) {
			jsonEntities.put(object.get("id").asText(), object);
		}
		LOG.info("Loaded {} entities", jsonEntities.size());
	}

	public Factory constructFactoryByTemplateId(String id) {
		JsonNode root = findRootById(Objects.requireNonNull(id));
		if (root == null) {
			throw new IllegalArgumentException("Did not find template by id " + id);
		}

		Factory factory = new Factory();
		factory.setId(UUID.randomUUID().toString());

		factory.setTemplateId(root.get("id").asText());
		factory.setName(root.get("name").asText());
		factory.setTexture(root.get("texture").asText());

		JsonNode storageNode = root.get("storage");
		Storage storage = new Storage(storageNode.get("capacity").asInt());
		factory.setStorage(storage);

		JsonNode producerNode = root.get("producer");
		Producer producer = new Producer();
		producer.stopProduction();
		producer.setTime(producerNode.get("time").asLong());
		producer.setResource(Resource.valueOf(producerNode.get("resource").asText()));
		factory.setProducer(producer);

		return factory;
	}

	private JsonNode findRootById(String id) {
		return jsonEntities.get(id);
	}

	public File getEntities() throws IOException {
		return entities.getFile();
	}

}
