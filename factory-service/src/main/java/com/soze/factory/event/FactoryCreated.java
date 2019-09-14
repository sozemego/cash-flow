package com.soze.factory.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public class FactoryCreated extends Event {

	private final String name;
	private final String texture;
	private final String templateId;
	private final String cityId;

	@JsonCreator
	public FactoryCreated(@JsonProperty("entityId") String entityId,
												@JsonProperty("timestamp") LocalDateTime timestamp,
												@JsonProperty("version") int version,
												@JsonProperty("name") String name,
												@JsonProperty("texture") String texture,
												@JsonProperty("templateId") String templateId,
												@JsonProperty("cityId") String cityId
											 ) {
		super(entityId, timestamp, version);
		this.name = name;
		this.texture = texture;
		this.templateId = templateId;
		this.cityId = cityId;
	}

	public String getName() {
		return name;
	}

	public String getTexture() {
		return texture;
	}

	public String getTemplateId() {
		return templateId;
	}

	public String getCityId() {
		return cityId;
	}

	@Override
	public EventType getType() {
		return EventType.FACTORY_CREATED;
	}
}
