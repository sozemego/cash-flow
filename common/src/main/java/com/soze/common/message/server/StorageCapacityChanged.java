package com.soze.common.message.server;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public class StorageCapacityChanged extends ServerMessage {

	private final String factoryId;
	private final int change;

	@JsonCreator
	public StorageCapacityChanged(@JsonProperty("messageId") UUID messageId,
																@JsonProperty("factoryId") String factoryId,
																@JsonProperty("change") int change) {
		super(messageId);
		this.factoryId = factoryId;
		this.change = change;
	}

	public StorageCapacityChanged(String factoryId, int change) {
		this(UUID.randomUUID(), factoryId, change);
	}


	@Override
	public String getType() {
		return ServerMessageType.STORAGE_CAPACITY_CHANGED.name();
	}

	public String getFactoryId() {
		return factoryId;
	}

	public int getChange() {
		return change;
	}

	@Override
	public String toString() {
		return "StorageCapacityChanged{" + "factoryId='" + factoryId + '\'' + ", change=" + change + '}';
	}
}
