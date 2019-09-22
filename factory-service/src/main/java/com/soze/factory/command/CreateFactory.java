package com.soze.factory.command;

import com.soze.factory.event.Event;

import java.util.List;
import java.util.UUID;

public class CreateFactory implements Command {

	private final UUID factoryId;
	private final String name;
	private final String texture;
	private final String cityId;
	private final String playerId = ""; //unused for now

	public CreateFactory(UUID factoryId, String name, String texture, String cityId) {
		this.factoryId = factoryId;
		this.name = name;
		this.texture = texture;
		this.cityId = cityId;
	}

	public UUID getFactoryId() {
		return factoryId;
	}

	public String getCityId() {
		return cityId;
	}

	public String getPlayerId() {
		return playerId;
	}

	public String getName() {
		return name;
	}

	public String getTexture() {
		return texture;
	}

	@Override
	public UUID getEntityId() {
		return getFactoryId();
	}

	@Override
	public List<Event> accept(CommandVisitor commandVisitor) {
		return commandVisitor.visit(this);
	}

	@Override
	public String toString() {
		return "CreateFactory{" + "factoryId=" + factoryId + ", name='" + name + '\'' + ", texture='" + texture + '\'' + ", cityId='" + cityId + '\'' + ", playerId='" + playerId + '\'' + '}';
	}
}
