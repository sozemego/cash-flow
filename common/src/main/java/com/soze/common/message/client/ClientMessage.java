package com.soze.common.message.client;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.Objects;
import java.util.UUID;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type", visible = true)
@JsonSubTypes(value = {
	@JsonSubTypes.Type(value = TruckTravelRequest.class, name = "TRUCK_TRAVEL_REQUEST"),
	@JsonSubTypes.Type(value = BuyResourceRequest.class, name = "BUY_RESOURCE_REQUEST"),
	@JsonSubTypes.Type(value = SellResourceRequest.class, name = "SELL_RESOURCE_REQUEST"),
	@JsonSubTypes.Type(value = DumpContent.class, name = "DUMP_CONTENT")
})
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class ClientMessage {

	private final UUID messageId;

	public ClientMessage(UUID messageId) {
		Objects.requireNonNull(this.messageId = messageId);
	}

	public UUID getMessageId() {
		return messageId;
	}

	public abstract ClientMessageType getType();

	public enum ClientMessageType {
		TRUCK_TRAVEL_REQUEST, BUY_RESOURCE_REQUEST, SELL_RESOURCE_REQUEST, DUMP_CONTENT
	}

}
