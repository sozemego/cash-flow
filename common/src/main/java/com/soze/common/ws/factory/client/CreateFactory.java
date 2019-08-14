package com.soze.common.ws.factory.client;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public class CreateFactory extends ClientMessage {

	private final String templateId;
	private final int x;
	private final int y;

	@JsonCreator
	public CreateFactory(@JsonProperty("messageId") UUID messageId,
											 @JsonProperty("templateId") String templateId,
											 @JsonProperty("x") int x,
											 @JsonProperty("y") int y
											) {
		super(messageId);
		this.templateId = templateId;
		this.x = x;
		this.y = y;
	}

	public CreateFactory(String templateId, int x, int y) {
		this(UUID.randomUUID(), templateId, x, y);
	}

	public String getTemplateId() {
		return templateId;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	@Override
	public ClientMessageType getType() {
		return ClientMessageType.CREATE_FACTORY;
	}

	@Override
	public String toString() {
		return "CreateFactory{" + "templateId='" + templateId + '\'' + ", x=" + x + ", y=" + y + '}';
	}
}
