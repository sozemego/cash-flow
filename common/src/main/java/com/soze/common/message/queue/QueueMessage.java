package com.soze.common.message.queue;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type", visible = true)
@JsonSubTypes(value = {
	@JsonSubTypes.Type(value = UserCreated.class, name = "USER_CREATED"),
	@JsonSubTypes.Type(value = UserCreatedConfirmation.class, name = "USER_CREATED_CONFIRMATION"),
})
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class QueueMessage {

	public abstract QueueMessageType getType();

	public enum QueueMessageType {
		USER_CREATED, USER_CREATED_CONFIRMATION
	}

}
