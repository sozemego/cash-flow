package com.soze.factory.command;

public interface CommandVisitor {

	void visit(CreateFactory createFactory);
	void visit(StartProduction startProduction);
	void visit(FinishProduction finishProduction);

}
