package com.soze.factory.event;

import com.soze.common.dto.Resource;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class ResourcePriceChanged extends Event {

	public Map<Resource, Integer> prices = new HashMap<>();

	public ResourcePriceChanged() {

	}

	public ResourcePriceChanged(String entityId, LocalDateTime timestamp, Map<Resource, Integer> prices
														 ) {
		super(entityId, timestamp, 1);
		this.prices = prices;
	}

	@Override
	public EventType getType() {
		return EventType.RESOURCE_PRICE_CHANGED;
	}

	@Override
	public void accept(EventVisitor visitor) {
		visitor.visit(this);
	}

	@Override
	public String toString() {
		return "ResourcePriceChanged{" + "prices=" + prices + ", entityId='" + entityId + '\'' + ", timestamp=" + timestamp + ", version=" + version + '}';
	}
}
