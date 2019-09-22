package com.soze.factory.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.soze.common.dto.Resource;

import java.time.LocalDateTime;

public class ProductionLineAdded extends Event {

	private final Resource resource;
	private final int count;
	private final long time;

	@JsonCreator
	public ProductionLineAdded(@JsonProperty("entityId") String entityId,
														 @JsonProperty("timestamp") LocalDateTime timestamp,
														 @JsonProperty("version") int version,
														 @JsonProperty("resource") Resource resource,
														 @JsonProperty("count") int count,
														 @JsonProperty("time") long time
														) {
		super(entityId, timestamp, version);
		this.resource = resource;
		this.count = count;
		this.time = time;
	}

	public Resource getResource() {
		return resource;
	}

	public int getCount() {
		return count;
	}

	public long getTime() {
		return time;
	}

	@Override
	public EventType getType() {
		return EventType.PRODUCTION_LINE_ADDED;
	}

	@Override
	public void accept(EventVisitor visitor) {
		visitor.visit(this);
	}
}
