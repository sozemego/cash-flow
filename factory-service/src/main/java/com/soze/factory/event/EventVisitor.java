package com.soze.factory.event;

public interface EventVisitor {

	void visit(FactoryCreated factoryCreated);
	void visit(ProductionStarted productionStarted);
	void visit(StorageCapacityChanged storageCapacityChanged);
	void visit(ProductionLineAdded productionLineAdded);
	void visit(ProductionFinished productionFinished);
	void visit(ResourceSold resourceSold);
	void visit(ResourceStorageCapacityChanged resourceStorageCapacityChanged);
	void visit(ResourcePriceChanged resourcePriceChanged);
  void visit(ProductionStarted2 productionStarted2);
	void visit(ProductionLineAdded2 productionLineAdded2);
	void visit(ResourceBought resourceBought);

}
