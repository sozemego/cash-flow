package com.soze.common.message.client;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.soze.common.dto.Resource;

import java.util.UUID;

public class SellResourceRequest extends ClientMessage {

	private final String truckId;
	private final String factoryId;
	private final Resource resource;
	private final int count;

	@JsonCreator
	public SellResourceRequest(@JsonProperty("messageId") UUID messageId,
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

	public SellResourceRequest(String truckId, String factoryId, Resource resource, int count) {
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
		return ClientMessageType.SELL_RESOURCE_REQUEST;
	}

	@Override
	public String toString() {
		return "SellResourceRequest{" + "truckId='" + truckId + '\'' + ", factoryId='" + factoryId + '\'' + ", resource=" + resource + ", count=" + count + '}';
	}
}
