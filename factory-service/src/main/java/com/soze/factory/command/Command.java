package com.soze.factory.command;

/**
 * Marker interface for commands.
 */
public interface Command {

	void accept(CommandVisitor commandVisitor);

}
