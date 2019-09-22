package com.soze.factory.aggregate;

import com.soze.common.dto.Resource;
import com.soze.factory.command.*;
import com.soze.factory.event.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * Aggregate root for the factory.
 */
public class Factory implements EventVisitor, CommandVisitor {

	private UUID id;
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

	public UUID getId() {
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
			new ProductionStarted(startProduction.getFactoryId().toString(), LocalDateTime.now(), 1, producer.getResource(),
														startProduction.getCurrentGameTime()
			));
	}

	@Override
	public List<Event> visit(FinishProduction finishProduction) {
		return Collections.singletonList(
			new ProductionFinished(finishProduction.getFactoryId().toString(), LocalDateTime.now()));
	}

	@Override
	public List<Event> visit(ChangeStorageCapacity changeStorageCapacity) {
		return Collections.singletonList(
			new StorageCapacityChanged(changeStorageCapacity.getFactoryId().toString(), LocalDateTime.now(), 1,
																 changeStorageCapacity.getChange()
			));
	}

	@Override
	public List<Event> visit(AddProductionLine addProductionLine) {
		return Collections.singletonList(
			new ProductionLineAdded(getId().toString(), LocalDateTime.now(), 1, addProductionLine.getResource(),
															addProductionLine.getCount(), addProductionLine.getTime()
			));
	}

	@Override
	public void visit(FactoryCreated factoryCreated) {
		this.id = UUID.fromString(factoryCreated.entityId);
		this.name = factoryCreated.name;
		this.texture = factoryCreated.texture;
		this.cityId = factoryCreated.cityId;
	}

	@Override
	public void visit(ProductionStarted productionStarted) {
		producer.startProduction(productionStarted.productionStartTime);
	}

	@Override
	public void visit(StorageCapacityChanged storageCapacityChanged) {
		Storage previousStorage = getStorage();
		int newCapacity = previousStorage.getCapacity() + storageCapacityChanged.change;
		storage = new Storage(newCapacity);
		storage.transferFrom(previousStorage);
	}

	@Override
	public void visit(ProductionLineAdded productionLineAdded) {
		Producer producer = new Producer();
		producer.setResource(productionLineAdded.resource);
		producer.setProducing(false);
		producer.setTime(productionLineAdded.time);
		this.producer = producer;
	}

	@Override
	public void visit(ProductionFinished productionFinished) {
		Resource resource = getProducer().getResource();
		Storage storage = getStorage();
		storage.addResource(resource);
		getProducer().stopProduction();
	}
}
