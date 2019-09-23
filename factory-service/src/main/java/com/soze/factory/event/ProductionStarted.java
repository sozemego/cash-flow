package com.soze.factory.event;

import com.soze.common.dto.Resource;

import java.time.LocalDateTime;

public class ProductionStarted extends Event {

	public Resource resource;
	public long productionStartTime;

	public ProductionStarted() {
	}

	public ProductionStarted(String entityId, LocalDateTime timestamp, int version, Resource resource,
													 long productionStartTime
													) {
		super(entityId, timestamp, version);
		this.resource = resource;
		this.productionStartTime = productionStartTime;
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
		return "ProductionStarted{" + "resource=" + resource + ", productionStartTime=" + productionStartTime + ", entityId='" + entityId + '\'' + ", timestamp=" + timestamp + ", version=" + version + '}';
	}
}
