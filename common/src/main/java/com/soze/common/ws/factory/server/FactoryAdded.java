package com.soze.common.ws.factory.server;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.soze.common.dto.FactoryDTO;

import java.util.UUID;

public class FactoryAdded extends ServerMessage {

	private final FactoryDTO factoryDTO;

	@JsonCreator
	public FactoryAdded(@JsonProperty("messageId") UUID messageId,
											@JsonProperty("factoryDTO") FactoryDTO factoryDTO
										 ) {
		super(messageId);
		this.factoryDTO = factoryDTO;
	}

	public FactoryAdded(FactoryDTO factoryDTO) {
		this(UUID.randomUUID(), factoryDTO);
	}

	public FactoryDTO getFactoryDTO() {
		return factoryDTO;
	}

	@Override
	public String getType() {
		return ServerMessageType.FACTORY_ADDED.name();
	}
}
