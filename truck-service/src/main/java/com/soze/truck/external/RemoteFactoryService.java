package com.soze.truck.external;

import com.soze.common.dto.FactoryDTO;
import com.soze.factory.client.FactoryServiceClient;
import com.soze.factory.domain.SellResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RemoteFactoryService {

	private static final Logger LOG = LoggerFactory.getLogger(RemoteFactoryService.class);

	private final FactoryServiceClient client;

	@Autowired
	public RemoteFactoryService(FactoryServiceClient client) {
		this.client = client;
	}

	public SellResult sell(String factoryId, String resource, int count) {
		LOG.info("calling /sell, factoryId = {}, resource = {}, count = {}", factoryId, resource, count);
		return client.sell(factoryId, resource, count);
	}

	public Optional<FactoryDTO> getFactory(String factoryId) {
		LOG.info("Calling /getFactory, factoryId = {}", factoryId);
		try {
			return Optional.of(client.getFactory(factoryId));
		} catch (Exception e) {
			LOG.info("Exception when fetching factory {}", factoryId, e);
			return Optional.empty();
		}
	}
}
