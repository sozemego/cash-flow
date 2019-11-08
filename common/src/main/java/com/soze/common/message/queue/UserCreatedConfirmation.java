package com.soze.common.message.queue;

public class UserCreatedConfirmation extends QueueMessage {

	public String id;

	@Override
	public QueueMessageType getType() {
		return QueueMessageType.USER_CREATED_CONFIRMATION;
	}
}
