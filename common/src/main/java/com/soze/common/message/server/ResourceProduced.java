package com.soze.common.message.server;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.soze.common.dto.Resource;

import java.util.UUID;

public class ResourceProduced extends ServerMessage {

	private final String factoryId;
	private final Resource resource;

	@JsonCreator
	public ResourceProduced(@JsonProperty("messageId") UUID messageId,
													@JsonProperty("factoryId") String factoryId,
													@JsonProperty("resource") Resource resource
												 ) {
		super(messageId);
		this.factoryId = factoryId;
		this.resource = resource;
	}

	public ResourceProduced(String factoryId, Resource resource) {
		this(UUID.randomUUID(), factoryId, resource);
	}

	public String getFactoryId() {
		return factoryId;
	}

	public Resource getResource() {
		return resource;
	}

	@Override
	public String getType() {
		return ServerMessageType.RESOURCE_PRODUCED.name();
	}
}
