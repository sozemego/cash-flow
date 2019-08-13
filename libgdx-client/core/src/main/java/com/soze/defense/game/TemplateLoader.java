package com.soze.defense.game;

import com.fasterxml.jackson.databind.JsonNode;
import com.soze.common.json.JsonUtils;
import com.soze.defense.client.HttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TemplateLoader {

	private static final Logger LOG = LoggerFactory.getLogger(
		TemplateLoader.class);

	private final String factoryTemplatesUrl = "http://localhost:9001/factory/templates";
	private final List<String> urls = Arrays.asList(factoryTemplatesUrl);

	private final Map<String, JsonNode> templates = new HashMap<>();

	public TemplateLoader() {

	}

	public void init() {
		LOG.info("TemplateLoader init...");
		LOG.info("Loading templates from {} urls", urls.size());

		for (String url : urls) {
			LOG.info("Loading templates from {}", url);
			HttpClient client = new HttpClient(url);
			String response = client.get();
			List<JsonNode> templates = JsonUtils.parseList(response, JsonNode.class);
			LOG.info("Fetched {} templates from {}", templates.size(), url);
			for (JsonNode template : templates) {
				this.templates.put(template.get("id").asText(), template);
			}
		}

		LOG.info("Loaded all templates! In total: {}", templates.size());
	}

	public Map<String, JsonNode> getTemplates() {
		if (templates.isEmpty()) {
			init();
		}
		return templates;
	}
}
