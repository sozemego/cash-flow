package com.soze.factory.aggregate;

import com.soze.common.dto.Resource;
import com.soze.factory.command.*;
import com.soze.factory.event.*;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Aggregate root for the factory.
 */
public class Factory implements EventVisitor, CommandVisitor {

	private UUID id;
	private String name;
	private String texture;
	private String cityId;

	private FactoryStorage storage = new FactoryStorage(new HashMap<>());
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

	public FactoryStorage getStorage() {
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

		if (producer.getInput().isEmpty() && producer.getOutput().isEmpty()) {
			return Collections.emptyList();
		}

		FactoryStorage storage = getStorage();
		Map<Resource, Integer> input = producer.getInput();
		if (!storage.hasResources(input)) {
			return Collections.emptyList();
		}

		Map<Resource, Integer> output = producer.getOutput();
		if (!storage.canFit(output)) {
			return Collections.emptyList();
		}

		return Collections.singletonList(
			new ProductionStarted2(startProduction.getFactoryId().toString(), LocalDateTime.now(), 1,
														startProduction.getCurrentGameTime()
			));
	}

	@Override
	public List<Event> visit(FinishProduction finishProduction) {
		ProductionFinished productionFinished = new ProductionFinished(getId().toString(), LocalDateTime.now());
		FactoryStorage storage = getStorage().copy();
		Producer producer = getProducer();
		storage.addResources(producer.getOutput());
		ResourcePriceChanged resourcePriceChanged = new ResourcePriceChanged(
			getId().toString(), LocalDateTime.now(), storage.getPrices());
		return Arrays.asList(productionFinished, resourcePriceChanged);
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
			new ProductionLineAdded2(getId().toString(), LocalDateTime.now(), 1, addProductionLine.getInput(),
															addProductionLine.getOutput(), addProductionLine.getTime()
			));
	}

	@Override
	public List<Event> visit(SellResource sellResource) {
		FactoryStorage storage = getStorage().copy();
		if (!storage.hasResource(sellResource.getResource(), sellResource.getCount())) {
			return new ArrayList<>();
		}
		Producer producer = getProducer();
		Map<Resource, Integer> input = producer.getInput();
		if (input.containsKey(sellResource.getResource())) {
			return new ArrayList<>();
		}

		storage.removeResource(sellResource.getResource(), sellResource.getCount());

		ResourceSold resourceSold = new ResourceSold(getId().toString(), LocalDateTime.now(), 1,
																								 sellResource.getResource().name(), sellResource.getCount()
		);
		ResourcePriceChanged resourcePriceChanged = new ResourcePriceChanged(getId().toString(), LocalDateTime.now(), storage.getPrices());

		return Arrays.asList(resourceSold, resourcePriceChanged);
	}

	@Override
	public List<Event> visit(BuyResource buyResource) {
		FactoryStorage storage = getStorage().copy();
		if (!storage.canFit(buyResource.getResource(), buyResource.getCount())) {
			return new ArrayList<>();
		}
		storage.addResource(buyResource.getResource(), buyResource.getCount());

		ResourceBought resourceBought = new ResourceBought(getId().toString(), LocalDateTime.now(), 1,
																								 buyResource.getResource().name(), buyResource.getCount()
		);

		ResourcePriceChanged resourcePriceChanged = new ResourcePriceChanged(getId().toString(), LocalDateTime.now(), storage.getPrices());

		return Arrays.asList(resourceBought, resourcePriceChanged);
	}

	@Override
	public List<Event> visit(ChangeResourceStorageCapacity changeResourceStorageCapacity) {
		return Collections.singletonList(new ResourceStorageCapacityChanged(getId().toString(), LocalDateTime.now(), 1,
																																				changeResourceStorageCapacity.getCapacityChanges()
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
		//do nothing, this even will be upcast
	}

	@Override
	public void visit(ProductionStarted2 productionStarted2) {
		FactoryStorage storage = getStorage();
		storage.removeResources(producer.getInput());
		producer.startProduction(productionStarted2.productionStartTime);
	}

	@Override
	public void visit(StorageCapacityChanged storageCapacityChanged) {
		//do nothing, this even will be upcast
	}

	@Override
	public void visit(ResourceStorageCapacityChanged resourceStorageCapacityChanged) {
		FactoryStorage factoryStorage = getStorage();
		factoryStorage.changeCapacities(resourceStorageCapacityChanged.capacityChanges);
	}

	@Override
	public void visit(ProductionLineAdded productionLineAdded) {

	}

	@Override
	public void visit(ProductionLineAdded2 productionLineAdded2) {
		Producer producer = new Producer();
		producer.getInput().putAll(productionLineAdded2.input);
		producer.getOutput().putAll(productionLineAdded2.output);
		producer.setProducing(false);
		producer.setTime(productionLineAdded2.time);
		this.producer = producer;
	}

	@Override
	public void visit(ProductionFinished productionFinished) {
		Map<Resource, Integer> output = getProducer().getOutput();
		FactoryStorage storage = getStorage();
		storage.addResources(output);
		getProducer().stopProduction();
	}

	@Override
	public void visit(ResourceSold resourceSold) {
		FactoryStorage storage = getStorage();
		storage.removeResource(Resource.valueOf(resourceSold.resource), resourceSold.count);
	}

	@Override
	public void visit(ResourceBought resourceBought) {
		FactoryStorage storage = getStorage();
		storage.addResource(Resource.valueOf(resourceBought.resource), resourceBought.count);
	}

	@Override
	public void visit(ResourcePriceChanged resourcePriceChanged) {

	}

}
