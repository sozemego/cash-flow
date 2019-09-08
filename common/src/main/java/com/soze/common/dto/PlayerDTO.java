package com.soze.common.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PlayerDTO {

	private final String id;
	private final String name;
	private final long cash;

	@JsonCreator
	public PlayerDTO(@JsonProperty("id") String id,
									 @JsonProperty("name") String name,
									 @JsonProperty("cash") long cash) {
		this.id = id;
		this.name = name;
		this.cash = cash;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public long getCash() {
		return cash;
	}

	@Override
	public String toString() {
		return "PlayerDTO{" + "id='" + id + '\'' + ", name='" + name + '\'' + ", cash=" + cash + '}';
	}
}
