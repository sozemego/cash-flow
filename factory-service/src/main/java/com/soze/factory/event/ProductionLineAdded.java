package com.soze.factory.event;

import com.soze.common.dto.Resource;

import java.time.LocalDateTime;

public class ProductionLineAdded extends Event {

	public Resource resource;
	public int count;
	public long time;

	public ProductionLineAdded() {
	}

	public ProductionLineAdded(String entityId, LocalDateTime timestamp, int version, Resource resource, int count,
														 long time
														) {
		super(entityId, timestamp, version);
		this.resource = resource;
		this.count = count;
		this.time = time;
	}

	@Override
	public EventType getType() {
		return EventType.PRODUCTION_LINE_ADDED;
	}

	@Override
	public void accept(EventVisitor visitor) {
		visitor.visit(this);
	}

	@Override
	public String toString() {
		return "ProductionLineAdded{" + "resource=" + resource + ", count=" + count + ", time=" + time + ", entityId='" + entityId + '\'' + ", timestamp=" + timestamp + ", version=" + version + '}';
	}
}
