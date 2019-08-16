package com.soze.truck.domain;

public class Truck {

	private String id;
	private String templateId;
	private String name;
	private String texture;

	private Storage storage;

	private int speed;

	public String getId() {
		return id;
	}

	public void setId(String id) {
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
