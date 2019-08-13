package com.soze.factory.domain;

public class Factory {

	private String id;
	private String templateId;
	private String name;
	private String texture;

	private int x;
	private int y;
	private int width;
	private int height;

	private Producer producer;
	private Storage storage;

	public Factory() {

	}

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

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public Producer getProducer() {
		return producer;
	}

	public void setProducer(Producer producer) {
		this.producer = producer;
	}

	public Storage getStorage() {
		return storage;
	}

	public void setStorage(Storage storage) {
		this.storage = storage;
	}

	@Override
	public String toString() {
		return "Factory{" + "id='" + id + '\'' + ", templateId='" + templateId + '\'' + ", name='" + name + '\'' + ", texture='" + texture + '\'' + ", x=" + x + ", y=" + y + ", width=" + width + ", height=" + height + ", producer=" + producer + ", storage=" + storage + '}';
	}
}
