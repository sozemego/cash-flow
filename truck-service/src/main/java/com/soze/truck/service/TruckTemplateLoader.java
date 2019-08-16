package com.soze.truck.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.soze.common.json.JsonUtils;
import com.soze.truck.domain.Storage;
import com.soze.truck.domain.Truck;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.*;

@Service
public class TruckTemplateLoader {

	private static final Logger LOG = LoggerFactory.getLogger(TruckTemplateLoader.class);
	private final Map<String, JsonNode> jsonEntities = new HashMap<>();

	@Value("classpath:trucks.json")
	private org.springframework.core.io.Resource entities;

	@PostConstruct
	public void setup() throws Exception {
		LOG.info("Truck templates init...");
		List<JsonNode> objects = JsonUtils.parseList(getEntities(), JsonNode.class);
		for (JsonNode object : objects) {
			jsonEntities.put(object.get("id").asText(), object);
		}
		LOG.info("Loaded {} entities", jsonEntities.size());
	}

	public Truck constructTruckByTemplateId(String id) {
		JsonNode root = findRootById(Objects.requireNonNull(id));
		if (root == null) {
			throw new IllegalArgumentException("Did not find template by id " + id);
		}

		Truck truck = new Truck();
		truck.setId(UUID.randomUUID().toString());
		truck.setTemplateId(id);
		truck.setName(root.get("name").asText());
		truck.setSpeed(root.get("speed").asInt());
		truck.setTexture(root.get("texture").asText());

		JsonNode storageNode = root.get("storage");
		Storage storage = new Storage(storageNode.get("capacity").asInt());
		truck.setStorage(storage);

		return truck;
	}

	private JsonNode findRootById(String id) {
		return jsonEntities.get(id);
	}

	public File getEntities() throws IOException {
		return entities.getFile();
	}

}
