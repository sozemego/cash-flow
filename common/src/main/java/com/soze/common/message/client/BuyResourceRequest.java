package com.soze.common.message.client;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.soze.common.dto.Resource;

import java.util.UUID;

public class BuyResourceRequest extends ClientMessage {

	private final String truckId;
	private final String factoryId;
	private final Resource resource;
	private final int count;

	@JsonCreator
	public BuyResourceRequest(@JsonProperty("messageId") UUID messageId,
														@JsonProperty("truckId") String truckId,
														@JsonProperty("factoryId") String factoryId,
														@JsonProperty("resource") Resource resource,
														@JsonProperty("count") int count) {
		super(messageId);
		this.truckId = truckId;
		this.factoryId = factoryId;
		this.resource = resource;
		this.count = count;
	}

	public BuyResourceRequest(String truckId, String factoryId, Resource resource, int count) {
		this(UUID.randomUUID(), truckId, factoryId, resource, count);
	}

	public String getTruckId() {
		return truckId;
	}

	public String getFactoryId() {
		return factoryId;
	}

	public Resource getResource() {
		return resource;
	}

	public int getCount() {
		return count;
	}

	@Override
	public ClientMessageType getType() {
		return ClientMessageType.BUY_RESOURCE_REQUEST;
	}
}
