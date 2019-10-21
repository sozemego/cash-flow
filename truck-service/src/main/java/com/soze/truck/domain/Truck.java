package com.soze.truck.domain;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "truck")
@TypeDefs({@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)})
public class Truck {

	@Id
	private UUID id;

	@Column(name = "template_id")
	private String templateId;

	@Column(name = "name")
	private String name;

	@Column(name = "texture")
	private String texture;

	@Type(type = "jsonb")
	@Column(name = "storage", columnDefinition = "jsonb")
	private Storage storage;

	@Column(name = "speed")
	private int speed;

	public Truck() {

	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTexture() {
		return texture;
	}

	public void setTexture(String texture) {
		this.texture = texture;
	}

	public Storage getStorage() {
		return storage;
	}

	public void setStorage(Storage storage) {
		this.storage = storage;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	@Override
	public String toString() {
		return "Truck{" + "id='" + id + '\'' + ", templateId='" + templateId + '\'' + ", name='" + name + '\'' + ", texture='" + texture + '\'' + ", storage=" + storage + ", speed=" + speed + '}';
	}

}
