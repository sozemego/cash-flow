package com.soze.factory.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.soze.common.dto.Resource;

import java.time.LocalDateTime;

public class ProductionStarted extends Event {

	private final Resource resource;
	private final long productionStartTime;

	@JsonCreator
	public ProductionStarted(@JsonProperty("entityId") String entityId,
													 @JsonProperty("timestamp") LocalDateTime timestamp,
													 @JsonProperty("version") int version,
													 @JsonProperty("resource") Resource resource,
													 @JsonProperty("productionStartTime") long productionStartTime
													) {
		super(entityId, timestamp, version);
		this.resource = resource;
		this.productionStartTime = productionStartTime;
	}

	public Resource getResource() {
		return resource;
	}

	public long getProductionStartTime() {
		return productionStartTime;
	}

	@Override
	public EventType getType() {
		return EventType.PRODUCTION_STARTED;
	}

	@Override
	public void accept(EventVisitor visitor) {
		visitor.visit(this);
	}

	@Override
	public String toString() {
		return "ProductionStarted{" + "resource=" + resource + ", productionStartTime=" + productionStartTime + '}';
	}
}
