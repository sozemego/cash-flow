package com.soze.factory.event;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.time.LocalDateTime;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type", visible = true)
@JsonSubTypes(value = {
	@JsonSubTypes.Type(value = FactoryCreated.class, name = "FACTORY_CREATED"),
})
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class Event {

	private final String entityId;
	private final LocalDateTime timestamp;
	private final int version;

	public Event(String entityId, LocalDateTime timestamp, int version) {
		this.entityId = entityId;
		this.timestamp = timestamp;
		this.version = version;
	}

	public String getEntityId() {
		return entityId;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public int getVersion() {
		return version;
	}

	public abstract EventType getType();

	@Override
	public String toString() {
		return "Event{" + "entityId='" + entityId + '\'' + ", timestamp=" + timestamp + ", version=" + version + '}';
	}

	public enum EventType {
		FACTORY_CREATED
	}
}
