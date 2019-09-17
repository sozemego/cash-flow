package com.soze.common.message.server;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public class StorageCapacityChanged extends ServerMessage {

	private final String entityId;
	private final int change;

	@JsonCreator
	public StorageCapacityChanged(@JsonProperty("messageId") UUID messageId,
																@JsonProperty("entityId") String entityId,
																@JsonProperty("change") int change) {
		super(messageId);
		this.entityId = entityId;
		this.change = change;
	}

	public StorageCapacityChanged(String entityId, int change) {
		this(UUID.randomUUID(), entityId, change);
	}


	@Override
	public String getType() {
		return ServerMessageType.STORAGE_CAPACITY_CHANGED.name();
	}

	public String getEntityId() {
		return entityId;
	}

	public int getChange() {
		return change;
	}

	@Override
	public String toString() {
		return "StorageCapacityChanged{" + "entityId='" + entityId + '\'' + ", change=" + change + '}';
	}
}
