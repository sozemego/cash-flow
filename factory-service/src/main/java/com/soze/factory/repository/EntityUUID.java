package com.soze.factory.repository;

import com.fasterxml.jackson.annotation.JsonCreator;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Embeddable
public class EntityUUID implements Serializable {

	@Column(name = "id")
	@org.hibernate.annotations.Type(type="org.hibernate.type.PostgresUUIDType")
	private UUID id;

	public EntityUUID() {}

	public EntityUUID(String id) {
		this(UUID.fromString(id));
	}

	public EntityUUID(UUID id) {
		this.id = id;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	@JsonCreator
	public static EntityUUID fromString(String id) {
		Objects.requireNonNull(id);
		return new EntityUUID(id);
	}

	@Override
	public String toString() {
		return id.toString();
	}
}
