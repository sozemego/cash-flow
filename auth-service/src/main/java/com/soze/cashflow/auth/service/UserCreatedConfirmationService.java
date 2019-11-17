package com.soze.cashflow.auth.service;

import com.soze.cashflow.auth.domain.tables.records.UserRecord;
import com.soze.common.json.JsonUtils;
import com.soze.common.message.queue.UserCreated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class UserCreatedConfirmationService {

	private static final Logger LOG = LoggerFactory.getLogger(UserCreatedConfirmationService.class);

	private final KafkaClient kafkaClient;

	@Inject
	public UserCreatedConfirmationService(KafkaClient kafkaClient) {
		this.kafkaClient = kafkaClient;
	}

	public void sendUserCreated(UserRecord userRecord) {
		LOG.info("Sending UserCreated = {}", userRecord.getName());
		UserCreated userCreated = new UserCreated(
			userRecord.getId().toString(), userRecord.getName(), userRecord.getCreateTime().toString());
		kafkaClient.sendMessage(null, JsonUtils.serialize(userCreated));
	}

}
