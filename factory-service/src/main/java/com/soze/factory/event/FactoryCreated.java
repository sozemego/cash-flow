package com.soze.factory.event;

import java.time.LocalDateTime;

public class FactoryCreated extends Event {

	public String name;
	public String texture;
	public String cityId;

	public FactoryCreated() {

	}

	public FactoryCreated(String entityId, LocalDateTime timestamp, int version, String name, String texture,
												String cityId
											 ) {
		super(entityId, timestamp, version);
		this.name = name;
		this.texture = texture;
		this.cityId = cityId;
	}

	@Override
	public EventType getType() {
		return EventType.FACTORY_CREATED;
	}

	@Override
	public void accept(EventVisitor visitor) {
		visitor.visit(this);
	}

	@Override
	public String toString() {
		return "FactoryCreated{" + "name='" + name + '\'' + ", texture='" + texture + '\'' + ", cityId='" + cityId + '\'' + '}';
	}
}
