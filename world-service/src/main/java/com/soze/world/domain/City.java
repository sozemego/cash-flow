package com.soze.world.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "city")
public class City {

	@Id
	public String id;

	@Column(name = "name")
	public String name;

	@Column(name = "factorySlots")
	public int factorySlots;

	@Column(name = "latitude")
	public float latitude;
	@Column(name = "longitude")
	public float longitude;

	@Override
	public String toString() {
		return "City{" + "id='" + id + '\'' + ", name='" + name + '\'' + ", factorySlots=" + factorySlots + ", latitude=" + latitude + ", longitude=" + longitude + '}';
	}
}
