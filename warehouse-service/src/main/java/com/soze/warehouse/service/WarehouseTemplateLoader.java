package com.soze.warehouse.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.soze.common.json.JsonUtils;
import com.soze.warehouse.domain.Storage;
import com.soze.warehouse.domain.Warehouse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.*;

@Service
public class WarehouseTemplateLoader {

	private static final Logger LOG = LoggerFactory.getLogger(WarehouseTemplateLoader.class);

	private final Map<String, JsonNode> jsonEntities = new HashMap<>();

	@Value("classpath:warehouses.json")
	private org.springframework.core.io.Resource entities;

	@PostConstruct
	public void setup() throws Exception {
		LOG.info("Warehouse templates init...");
		List<JsonNode> objects = JsonUtils.parseList(getEntities(), JsonNode.class);
		for (JsonNode object : objects) {
			jsonEntities.put(object.get("id").asText(), object);
		}
		LOG.info("Loaded {} entities", jsonEntities.size());
	}

	public Warehouse constructWarehouseByTemplateId(String id) {
		JsonNode root = findRootById(Objects.requireNonNull(id));
		if (root == null) {
			throw new IllegalArgumentException("Did not find template by id " + id);
		}

		Warehouse warehouse = new Warehouse();
		warehouse.setId(UUID.randomUUID().toString());
		warehouse.setTemplateId(id);
		warehouse.setName(root.get("name").asText());
		warehouse.setTexture(root.get("texture").asText());


		JsonNode storageNode = root.get("storage");
		Storage storage = new Storage(storageNode.get("capacity").asInt());
		warehouse.setStorage(storage);

		return warehouse;
	}

	private JsonNode findRootById(String id) {
		return jsonEntities.get(id);
	}

	private File getEntities() throws IOException {
		return entities.getFile();
	}

}
