package com.soze.common.dto;


public class FactoryDTO {

	private String id;
	private String name;
	private String texture;

	private ProducerDTO producer;
	private StorageDTO storage;

	private String cityId;

	public FactoryDTO() {

	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public ProducerDTO getProducer() {
		return producer;
	}

	public void setProducer(ProducerDTO producer) {
		this.producer = producer;
	}

	public StorageDTO getStorage() {
		return storage;
	}

	public void setStorage(StorageDTO storage) {
		this.storage = storage;
	}

	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	@Override
	public String toString() {
		return "FactoryDTO{" + "id='" + id + '\'' + ", name='" + name + '\'' + ", texture='" + texture + '\'' + ", producer=" + producer + ", storage=" + storage + ", cityId='" + cityId + '\'' + '}';
	}
}
