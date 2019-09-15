package com.soze.factory.aggregate;

import com.soze.factory.event.EventVisitor;
import com.soze.factory.event.FactoryCreated;
import com.soze.factory.event.ProductionStarted;
import com.soze.factory.event.StorageCapacityChanged;

/**
 * Aggregate root for the factory.
 */
public class Factory implements EventVisitor {

	private final String id;
	private final String name;
	private final String texture;
	private final String cityId;

	private Storage storage = new Storage(0);
	private Producer producer = new Producer();

	public Factory(FactoryCreated factoryCreated) {
		this.id = factoryCreated.getEntityId();
		this.name = factoryCreated.getName();
		this.texture = factoryCreated.getTexture();
		this.cityId = factoryCreated.getCityId();
	}

	public String getId() {
		return id;
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

	@Override
	public void visit(FactoryCreated factoryCreated) {

	}

	@Override
	public void visit(ProductionStarted productionStarted) {
		producer.startProduction(productionStarted.getProductionStartTime());
	}

	@Override
	public void visit(StorageCapacityChanged storageCapacityChanged) {
		Storage previousStorage = getStorage();
		int newCapacity = previousStorage.getCapacity() + storageCapacityChanged.getChange();
		storage = new Storage(newCapacity);
		storage.transferFrom(previousStorage);
	}
}
