package com.soze.defense.game.factory;

import com.soze.common.dto.FactoryDTO;
import com.soze.common.json.JsonUtils;
import com.soze.defense.client.HttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class FactoryClient {

	private static final Logger LOG = LoggerFactory.getLogger(
		FactoryClient.class);

	private final HttpClient client = new HttpClient(
		"http://localhost:9001/factory");

	public List<FactoryDTO> getFactories() {
		String response = client.get();
		List<FactoryDTO> factories = JsonUtils.parseList(
			response, FactoryDTO.class);
		LOG.info("Fetched {} factories", factories.size());
		return factories;
	}

}
