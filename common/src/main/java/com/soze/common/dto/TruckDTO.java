package com.soze.common.dto;

public class TruckDTO {

	private String id;
	private String templateId;
	private String name;
	private String texture;

	private StorageDTO storage;

	private int speed;

	private TruckNavigationDTO navigation;

	public TruckDTO() {

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

	public StorageDTO getStorage() {
		return storage;
	}

	public void setStorage(StorageDTO storage) {
		this.storage = storage;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public TruckNavigationDTO getNavigation() {
		return navigation;
	}

	public void setNavigation(TruckNavigationDTO navigation) {
		this.navigation = navigation;
	}

	@Override
	public String toString() {
		return "TruckDTO{" + "id='" + id + '\'' + ", templateId='" + templateId + '\'' + ", name='" + name + '\'' + ", texture='" + texture + '\'' + ", storage=" + storage + ", speed=" + speed + '}';
	}
}
