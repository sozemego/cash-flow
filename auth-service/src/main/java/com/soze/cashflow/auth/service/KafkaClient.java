package com.soze.cashflow.auth.service;

import io.micronaut.configuration.kafka.annotation.KafkaKey;
import io.micronaut.configuration.kafka.annotation.Topic;

@io.micronaut.configuration.kafka.annotation.KafkaClient
public interface KafkaClient {

	@Topic("domain")
	void sendMessage(@KafkaKey String brand, String message);

}
