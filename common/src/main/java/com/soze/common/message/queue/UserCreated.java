package com.soze.common.message.queue;

public class UserCreated extends QueueMessage {

	public String id;
	public String name;
	public String createTime;

	public UserCreated() {
	}

	public UserCreated(String id, String name, String createTime) {
		this.id = id;
		this.name = name;
		this.createTime = createTime;
	}

	@Override
	public QueueMessageType getType() {
		return QueueMessageType.USER_CREATED;
	}

	@Override
	public String toString() {
		return "UserCreated{" + "id='" + id + '\'' + ", name='" + name + '\'' + ", createTime='" + createTime + '\'' + '}';
	}
}
