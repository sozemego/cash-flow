package com.soze.factory.event;

import com.soze.common.dto.Resource;

import java.time.LocalDateTime;
import java.util.Map;

public class ProductionLineAdded2 extends Event {

	public Map<Resource, Integer> input;
	public Map<Resource, Integer> output;
	public long time;

	public ProductionLineAdded2() {
	}

	public ProductionLineAdded2(String entityId, LocalDateTime timestamp, int version, Map<Resource, Integer> input,
															Map<Resource, Integer> output, long time
														 ) {
		super(entityId, timestamp, version);
		this.input = input;
		this.output = output;
		this.time = time;
	}

	@Override
	public EventType getType() {
		return EventType.PRODUCTION_LINE_ADDED2;
	}

	@Override
	public void accept(EventVisitor visitor) {
		visitor.visit(this);
	}

	@Override
	public String toString() {
		return "ProductionLineAdded2{" + "input=" + input + ", output=" + output + ", time=" + time + ", entityId='" + entityId + '\'' + ", timestamp=" + timestamp + ", version=" + version + '}';
	}
}
