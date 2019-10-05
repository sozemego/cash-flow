package com.soze.common.message.client;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public class DumpContent extends ClientMessage {

	private final String entityId;

	@JsonCreator
	public DumpContent(@JsonProperty("messageId") UUID messageId, @JsonProperty("entityId") String entityId) {
		super(messageId);
		this.entityId = entityId;
	}

	public String getEntityId() {
		return entityId;
	}

	@Override
	public ClientMessageType getType() {
		return ClientMessageType.DUMP_CONTENT;
	}

	@Override
	public String toString() {
		return "DumpContent{" + "entityId='" + entityId + '\'' + '}';
	}
}
