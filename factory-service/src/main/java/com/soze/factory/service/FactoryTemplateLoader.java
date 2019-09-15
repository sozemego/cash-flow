package com.soze.factory.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.soze.common.json.JsonUtils;
import com.soze.factory.command.Command;
import com.soze.factory.command.CreateFactory;
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

	/**
	 * Returns a list of {@link Command} objects which need to be handled
	 * in order to create a initial version of factory with this templateId.
	 * Since factory cannot exist without a city, it is required for its creation.
	 */
	public List<Command> getFactoryCommandsByTemplateId(String id, String cityId) {
		JsonNode root = findRootById(Objects.requireNonNull(id));
		if (root == null) {
			throw new IllegalArgumentException("Did not find template by id " + id);
		}

		List<Command> commands = new ArrayList<>();
		String name = root.get("name").asText();
		String texture = root.get("texture").asText();
		commands.add(new CreateFactory(UUID.randomUUID(), name, texture, cityId));

		//TODO storage
		//TODO producer

		return commands;
	}

	public JsonNode findRootById(String id) {
		return jsonEntities.get(id);
	}

	public File getEntities() throws IOException {
		return entities.getFile();
	}

	public boolean exists(String id) {
		return findRootById(id) != null;
	}

}
