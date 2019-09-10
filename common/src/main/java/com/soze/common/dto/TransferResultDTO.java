package com.soze.common.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TransferResultDTO {

	private final long amountTransferred;

	@JsonCreator
	public TransferResultDTO(@JsonProperty("amountTransferred") long amountTransferred) {
		this.amountTransferred = amountTransferred;
	}

	public long getAmountTransferred() {
		return amountTransferred;
	}

	@Override
	public String toString() {
		return "TransferResultDTO{" + "amountTransferred=" + amountTransferred + '}';
	}
}
