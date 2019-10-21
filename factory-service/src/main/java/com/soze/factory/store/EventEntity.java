package com.soze.factory.store;

import com.soze.factory.event.Event;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "factory_event")
@TypeDefs({@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)})
public class EventEntity {

	@Id
	@Type(type = "pg-uuid")
	@Column(name = "id", columnDefinition = "uuid")
	private UUID id;

	@Type(type = "jsonb")
	@Column(name = "event", columnDefinition = "jsonb")
	private Event event;

	public EventEntity() {

	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}

	@Override
	public String toString() {
		return "EventEntity{" + "id=" + id + ", event=" + event + '}';
	}
}
