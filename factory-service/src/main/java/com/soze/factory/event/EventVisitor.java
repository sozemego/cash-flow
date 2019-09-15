package com.soze.factory.event;

public interface EventVisitor {

	void visit(FactoryCreated factoryCreated);
	void visit(ProductionStarted productionStarted);
	void visit(StorageCapacityChanged storageCapacityChanged);

}
