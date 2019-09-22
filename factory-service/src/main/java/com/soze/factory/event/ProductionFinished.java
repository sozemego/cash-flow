package com.soze.factory.event;

import java.time.LocalDateTime;

public class ProductionFinished extends Event {

	public ProductionFinished() {

	}

	public ProductionFinished(String entityId,
														LocalDateTime timestamp) {
		super(entityId, timestamp, 1);
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
