package com.soze.factory.command;


import com.soze.factory.event.Event;

import java.util.List;

public interface CommandVisitor {

	List<Event> visit(CreateFactory createFactory);
	List<Event> visit(StartProduction startProduction);
	List<Event> visit(FinishProduction finishProduction);
	List<Event> visit(ChangeStorageCapacity changeStorageCapacity);
	List<Event> visit(AddProductionLine addProductionLine);
	List<Event> visit(SellResource sellResource);
	List<Event> visit(ChangeResourceStorageCapacity changeResourceStorageCapacity);

}
