package com.soze.factory.event;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.io.Serializable;
import java.time.LocalDateTime;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type", visible = true)
@JsonSubTypes(value = {
	@JsonSubTypes.Type(value = FactoryCreated.class, name = "FACTORY_CREATED"),
	@JsonSubTypes.Type(value = ProductionStarted.class, name = "PRODUCTION_STARTED"),
	@JsonSubTypes.Type(value = StorageCapacityChanged.class, name = "STORAGE_CAPACITY_CHANGED"),
	@JsonSubTypes.Type(value = ProductionLineAdded.class, name = "PRODUCTION_LINE_ADDED"),
	@JsonSubTypes.Type(value = ProductionFinished.class, name = "PRODUCTION_FINISHED"),
	@JsonSubTypes.Type(value = ResourceSold.class, name = "RESOURCE_SOLD"),
	@JsonSubTypes.Type(value = ResourceStorageCapacityChanged.class, name = "RESOURCE_STORAGE_CAPACITY_CHANGED"),
	@JsonSubTypes.Type(value = ResourcePriceChanged.class, name = "RESOURCE_PRICE_CHANGED"),
})
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class Event implements Serializable {

	public String entityId;
	public LocalDateTime timestamp;
	public int version;

	public Event() {

	}

	public Event(String entityId, LocalDateTime timestamp, int version) {
		this.entityId = entityId;
		this.timestamp = timestamp;
		this.version = version;
	}

	public abstract EventType getType();

	public abstract void accept(EventVisitor visitor);

	@Override
	public String toString() {
		return "Event{" + "entityId='" + entityId + '\'' + ", timestamp=" + timestamp + ", version=" + version + '}';
	}

	public enum EventType {
		FACTORY_CREATED, PRODUCTION_STARTED, STORAGE_CAPACITY_CHANGED, PRODUCTION_LINE_ADDED, PRODUCTION_FINISHED, RESOURCE_SOLD,
		RESOURCE_STORAGE_CAPACITY_CHANGED, RESOURCE_PRICE_CHANGED
	}
}
