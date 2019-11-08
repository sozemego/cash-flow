package com.soze.common.message.queue;

public class UserCreated {

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
}
