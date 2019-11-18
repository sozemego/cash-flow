package com.soze.truck.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "player")
public class Player {

	@Id
	private UUID id;

	@Column(name = "name")
	private String name;

	@Column
	private boolean initialized;

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isInitialized() {
		return initialized;
	}

	public void setInitialized(boolean initialized) {
		this.initialized = initialized;
	}

	@Override
	public String toString() {
		return "Player{" + "id=" + id + ", name='" + name + '\'' + ", initialized=" + initialized + '}';
	}
}
