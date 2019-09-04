package com.soze.common.message.server;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.soze.common.dto.Resource;

import java.util.UUID;

public class StorageContentChanged extends ServerMessage {

	private final String entityId;
	private final Resource resource;
	private final int change;

	@JsonCreator
	public StorageContentChanged(@JsonProperty("messageId") UUID messageId,
															 @JsonProperty("entityId") String entityId,
															 @JsonProperty("resource") Resource resource,
															 @JsonProperty("change") int change
															) {
		super(messageId);
		this.entityId = entityId;
		this.resource = resource;
		this.change = change;
	}

	public StorageContentChanged(String entityId, Resource resource, int change) {
		this(UUID.randomUUID(), entityId, resource, change);
	}

	public String getEntityId() {
		return entityId;
	}

	public Resource getResource() {
		return resource;
	}

	public int getChange() {
		return change;
	}

	@Override
	public String getType() {
		return ServerMessageType.STORAGE_CONTENT_CHANGED.name();
	}

}
