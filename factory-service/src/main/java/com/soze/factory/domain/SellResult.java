package com.soze.factory.domain;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.soze.common.dto.Resource;

public class SellResult {

	private final String factoryId;
	private final Resource resource;
	private final int count;

	@JsonCreator
	public SellResult(@JsonProperty("factoryId") String factoryId,
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
