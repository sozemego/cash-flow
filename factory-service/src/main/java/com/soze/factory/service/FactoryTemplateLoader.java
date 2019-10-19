package com.soze.factory.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.soze.common.dto.Resource;
import com.soze.common.json.JsonUtils;
import com.soze.factory.command.AddProductionLine;
import com.soze.factory.command.ChangeResourceStorageCapacity;
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
	public List<Command> getFactoryCommandsByTemplateId(UUID factoryId, String templateId, String cityId) {
		Objects.requireNonNull(factoryId);
		Objects.requireNonNull(cityId);
		JsonNode root = findRootById(Objects.requireNonNull(templateId));
		if (root == null) {
			throw new IllegalArgumentException("Did not find template by id " + templateId);
		}

		List<Command> commands = new ArrayList<>();
		String name = root.get("name").asText();
		String texture = root.get("texture").asText();
		commands.add(new CreateFactory(factoryId, name, texture, cityId));

		Map<Resource, Integer> capacities = new HashMap<>();
		JsonNode storage = root.get("storage").get("capacities");
		storage.fields().forEachRemaining((field) -> {
			capacities.put(Resource.valueOf(field.getKey()), field.getValue().asInt());
		});
		commands.add(new ChangeResourceStorageCapacity(factoryId, capacities));

		JsonNode producer = root.get("producer");
		Resource resource = Resource.valueOf(producer.get("resource").asText());
		long time = producer.get("time").asLong();
		commands.add(new AddProductionLine(factoryId, resource, 1, time));

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

	public Optional<JsonNode> findRootByName(String name) {
		for (JsonNode node : jsonEntities.values()) {
			String nodeName = node.get("name").asText();
			if (name.equals(nodeName)) {
				return Optional.of(node);
			}
		}
		return Optional.empty();
	}

}
