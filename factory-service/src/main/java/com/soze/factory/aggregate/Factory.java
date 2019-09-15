package com.soze.factory.aggregate;

import com.soze.factory.command.*;
import com.soze.factory.event.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
 * Aggregate root for the factory.
 */
public class Factory implements EventVisitor, CommandVisitor {

	private String id;
	private String name;
	private String texture;
	private String cityId;

	private Storage storage = new Storage(0);
	private Producer producer = new Producer();

	public Factory() {

	}

	public Factory(FactoryCreated factoryCreated) {
		visit(factoryCreated);
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
	public List<Event> visit(CreateFactory createFactory) {
		return Collections.singletonList(
			new FactoryCreated(createFactory.getFactoryId().toString(), LocalDateTime.now(), 1, createFactory.getName(),
												 createFactory.getTexture(), createFactory.getCityId()
			));
	}

	@Override
	public List<Event> visit(StartProduction startProduction) {
		Producer producer = getProducer();
		if (producer.isProducing()) {
			return Collections.emptyList();
		}
		if (producer.getResource() == null) {
			return Collections.emptyList();
		}

		Storage storage = getStorage();
		if (storage.isFull()) {
			return Collections.emptyList();
		}

		return Collections.singletonList(
			new ProductionStarted(startProduction.getFactoryId(), LocalDateTime.now(), 1, producer.getResource(),
														startProduction.getCurrentGameTime()
			));
	}

	@Override
	public List<Event> visit(FinishProduction finishProduction) {
		return Collections.emptyList();
	}

	@Override
	public List<Event> visit(ChangeStorageCapacity changeStorageCapacity) {
		return Collections.singletonList(
			new StorageCapacityChanged(changeStorageCapacity.getFactoryId().toString(), LocalDateTime.now(), 1,
																 changeStorageCapacity.getChange()
			));
	}

	@Override
	public void visit(FactoryCreated factoryCreated) {
		this.id = factoryCreated.getEntityId();
		this.name = factoryCreated.getName();
		this.texture = factoryCreated.getTexture();
		this.cityId = factoryCreated.getCityId();
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
