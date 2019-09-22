package com.soze.factory.command;

import com.soze.factory.event.Event;

import java.util.List;
import java.util.UUID;

/**
 * Marker interface for commands.
 */
public interface Command {

	UUID getEntityId();

	List<Event> accept(CommandVisitor commandVisitor);

}
