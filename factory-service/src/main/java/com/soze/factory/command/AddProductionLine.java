package com.soze.factory.command;

import com.soze.common.dto.Resource;
import com.soze.factory.event.Event;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class AddProductionLine implements Command {

	private final UUID factoryId;
	private final Map<Resource, Integer> input;
	private final Map<Resource, Integer> output;
	private final long time;

	public AddProductionLine(UUID factoryId, Map<Resource, Integer> input, Map<Resource, Integer> output, long time) {
		this.factoryId = factoryId;
		this.input = input;
		this.output = output;
		this.time = time;
	}

	public UUID getFactoryId() {
		return factoryId;
	}

	public Map<Resource, Integer> getInput() {
		return input;
	}

	public Map<Resource, Integer> getOutput() {
		return output;
	}

	public long getTime() {
		return time;
	}

	@Override
	public UUID getEntityId() {
		return getFactoryId();
	}

	@Override
	public List<Event> accept(CommandVisitor commandVisitor) {
		return commandVisitor.visit(this);
	}

	@Override
	public String toString() {
		return "AddProductionLine{" + "factoryId=" + factoryId + ", input=" + input + ", output=" + output + ", time=" + time + '}';
	}
}
