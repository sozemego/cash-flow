package com.soze.factory.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public class ProductionFinished extends Event {

	@JsonCreator
	public ProductionFinished(@JsonProperty("entityId") String entityId,
														@JsonProperty("timestamp") LocalDateTime timestamp,
														@JsonProperty("version") int version) {
		super(entityId, timestamp, version);
	}

	@Override
	public EventType getType() {
		return EventType.PRODUCTION_FINISHED;
	}

	@Override
	public void accept(EventVisitor visitor) {
		visitor.visit(this);
	}
}
