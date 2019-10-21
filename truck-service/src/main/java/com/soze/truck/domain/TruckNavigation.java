package com.soze.truck.domain;


import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "navigation")
public class TruckNavigation {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public int id;

	@Column(name = "truck_id")
	public UUID truckId;

	@Column(name = "current_city_id")
	public String currentCityId = "MOON";

	@Column(name = "start_time")
	public long startTime = -1L;

	@Column(name = "arrival_time")
	public long arrivalTime = -1L;

	@Column(name = "next_city_id")
	public String nextCityId;

	public TruckNavigation() {
	}

	public TruckNavigation(UUID truckId) {
		this.truckId = truckId;
	}

	@Override
	public String toString() {
		return "TruckNavigation{" + "id=" + id + ", truckId='" + truckId + '\'' + ", currentCityId='" + currentCityId + '\'' + ", startTime=" + startTime + ", arrivalTime=" + arrivalTime + ", nextCityId='" + nextCityId + '\'' + '}';
	}
}
