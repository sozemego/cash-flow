package com.soze.factory.aggregate;

import com.soze.factory.event.Event;
import com.soze.factory.event.FactoryCreated;

/**
 * Aggregate root for the factory.
 */
public class Factory {

	private final String id;
	private final String templateId;
	private final String name;
	private final String texture;
	private final String cityId;

	private Storage storage = new Storage(0);
	private Producer producer = new Producer();

	public Factory(FactoryCreated factoryCreated) {
		this.id = factoryCreated.getEntityId();
		this.templateId = factoryCreated.getTemplateId();
		this.name = factoryCreated.getName();
		this.texture = factoryCreated.getTexture();
		this.cityId = factoryCreated.getCityId();
	}

	public String getId() {
		return id;
	}

	public String getTemplateId() {
		return templateId;
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

	public Storage getStorage() {
		return storage;
	}

	public Producer getProducer() {
		return producer;
	}

	public void accept(Event event) {

	}
}
