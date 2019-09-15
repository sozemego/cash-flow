package com.soze.factory.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public class FactoryCreated extends Event {

	private final String name;
	private final String texture;
	private final String cityId;

	@JsonCreator
	public FactoryCreated(@JsonProperty("entityId") String entityId,
												@JsonProperty("timestamp") LocalDateTime timestamp,
												@JsonProperty("version") int version,
												@JsonProperty("name") String name,
												@JsonProperty("texture") String texture,
												@JsonProperty("cityId") String cityId
											 ) {
		super(entityId, timestamp, version);
		this.name = name;
		this.texture = texture;
		this.cityId = cityId;
	}

	public String getName() {
		return name;
	}

	public String getTexture() {
		return texture;
	}

	public String getCityId() {
		return cityId;
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
