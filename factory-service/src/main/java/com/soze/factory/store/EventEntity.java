package com.soze.factory.store;

import com.soze.factory.event.Event;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "factory_event")
@TypeDefs({@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)})
@SqlResultSetMapping(
	name = "Mapping",
	classes = @ConstructorResult(
		targetClass = EventEntity.class,
		columns = {
			@ColumnResult(name = "id", type = UUID.class),
			@ColumnResult(name = "event", type = Event.class),
		}))
public class EventEntity implements Serializable {

	@Id
	@Type(type = "pg-uuid")
	@Column(name = "id", columnDefinition = "uuid")
	private UUID id;

	@Type(type = "jsonb")
	@Column(name = "event", columnDefinition = "jsonb")
	private Event event;

	public EventEntity() {

	}

	public EventEntity(UUID id, Event event) {
		this.id = id;
		this.event = event;
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
