package com.soze.factory.aggregate;

import com.soze.factory.domain.Producer;
import com.soze.factory.domain.Storage;
import com.soze.factory.event.Event;
import com.soze.factory.event.FactoryCreated;

/**
 * Aggregate root for the factory.
 */
public class Factory {

	private final String id;
	private final String templateId;
	private final String name;

	private Storage storage = new Storage(0);
	private Producer producer = new Producer();

	public Factory(String id, String templateId, String name) {
		this.id = id;
		this.templateId = templateId;
		this.name = name;
	}

	public Factory(FactoryCreated factoryCreated) {
		this.id = factoryCreated.getEntityId();
		this.templateId = factoryCreated.getTemplateId();
		this.name = factoryCreated.getName();
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

	public Storage getStorage() {
		return storage;
	}

	public Producer getProducer() {
		return producer;
	}

	public void accept(Event event) {

	}
}
