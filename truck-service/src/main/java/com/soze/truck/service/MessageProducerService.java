package com.soze.truck.service;

import com.soze.common.json.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
@Profile("!test")
public class MessageProducerService {

	private static final Logger LOG = LoggerFactory.getLogger(MessageProducerService.class);

	private static final String TOPIC_NAME = "game-event";

	private final KafkaTemplate<String, String> kafkaTemplate;

	@Autowired
	public MessageProducerService(KafkaTemplate<String, String> kafkaTemplate) {
		this.kafkaTemplate = kafkaTemplate;
	}

	public void sendEvent(Object object) {
		LOG.trace("Sending event {}", object);
		String payload = JsonUtils.serialize(object);
		kafkaTemplate.send(TOPIC_NAME, payload);
	}

}
