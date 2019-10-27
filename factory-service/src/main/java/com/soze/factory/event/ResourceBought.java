package com.soze.factory.event;

import java.time.LocalDateTime;

public class ResourceBought extends Event {

	public String resource;
	public int count;

	public ResourceBought() {

	}

	public ResourceBought(String entityId, LocalDateTime timestamp, int version, String resource, int count) {
		super(entityId, timestamp, version);
		this.resource = resource;
		this.count = count;
	}

	@Override
	public EventType getType() {
		return EventType.RESOURCE_BOUGHT;
	}

	@Override
	public void accept(EventVisitor visitor) {
		visitor.visit(this);
	}

	@Override
	public String toString() {
		return "ResourceSold{" + "resource='" + resource + '\'' + ", count=" + count + ", entityId='" + entityId + '\'' + ", timestamp=" + timestamp + ", version=" + version + '}';
	}
}
