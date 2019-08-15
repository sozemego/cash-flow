package com.soze.common.ws.factory.server;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.soze.common.dto.Resource;

import java.util.UUID;

public class ResourceProductionStarted extends ServerMessage {

	private final String factoryId;
	private final Resource resource;
	private final long productionStartTime;

	@JsonCreator
	public ResourceProductionStarted(@JsonProperty("messageId") UUID messageId,
																	 @JsonProperty("factoryId") String factoryId,
																	 @JsonProperty("resource") Resource resource,
																	 @JsonProperty("productionStartTime") long productionStartTime
																	) {
		super(messageId);
		this.factoryId = factoryId;
		this.resource = resource;
		this.productionStartTime = productionStartTime;
	}

	public ResourceProductionStarted(String factoryId, Resource resource, long productionStartTime) {
		this(UUID.randomUUID(), factoryId, resource, productionStartTime);
	}

	public String getFactoryId() {
		return factoryId;
	}

	public Resource getResource() {
		return resource;
	}

	public long getProductionStartTime() {
		return productionStartTime;
	}

	@Override
	public String getType() {
		return ServerMessageType.RESOURCE_PRODUCTION_STARTED.name();
	}
}
