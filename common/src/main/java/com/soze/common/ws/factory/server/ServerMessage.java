package com.soze.common.ws.factory.server;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.Objects;
import java.util.UUID;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type", visible = true)
@JsonSubTypes(value = {@JsonSubTypes.Type(value = ResourceProduced.class, name = "RESOURCE_PRODUCED"), @JsonSubTypes.Type(value = ResourceProductionStarted.class, name = "RESOURCE_PRODUCTION_STARTED"), @JsonSubTypes.Type(value = FactoryAdded.class, name = "FACTORY_ADDED"),})
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class ServerMessage {

	private final UUID messageId;

	public ServerMessage(UUID messageId) {
		Objects.requireNonNull(this.messageId = messageId);
	}

	public UUID getMessageId() {
		return messageId;
	}

	public abstract String getType();

	public enum ServerMessageType {
		RESOURCE_PRODUCED, RESOURCE_PRODUCTION_STARTED, FACTORY_ADDED
	}
}
