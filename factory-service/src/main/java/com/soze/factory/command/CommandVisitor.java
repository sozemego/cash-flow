package com.soze.factory.command;

public interface CommandVisitor {

	void visit(CreateFactory createFactory);

}
