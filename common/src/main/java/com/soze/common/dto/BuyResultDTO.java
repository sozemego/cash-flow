package com.soze.common.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class BuyResultDTO {

	private final String factoryId;
	private final Resource resource;
	private final int count;

	@JsonCreator
	public BuyResultDTO(@JsonProperty("factoryId") String factoryId,
											@JsonProperty("resource") Resource resource,
											@JsonProperty("count") int count
										 ) {
		this.factoryId = factoryId;
		this.resource = resource;
		this.count = count;
	}

	public String getFactoryId() {
		return factoryId;
	}

	public Resource getResource() {
		return resource;
	}

	public int getCount() {
		return count;
	}

}
