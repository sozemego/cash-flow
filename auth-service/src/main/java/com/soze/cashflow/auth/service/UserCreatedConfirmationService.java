package com.soze.cashflow.auth.service;

import com.rabbitmq.client.ConfirmListener;
import com.soze.cashflow.auth.domain.tables.records.UserRecord;
import com.soze.cashflow.auth.repository.UserRepository;
import com.soze.common.message.queue.UserCreated;
import io.micronaut.scheduling.annotation.Scheduled;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.util.List;

@Singleton
public class UserCreatedConfirmationService {

	private static final Logger LOG = LoggerFactory.getLogger(UserCreatedConfirmationService.class);

	private final UserRepository repository;
	private final MessageQueueService messageQueueService;

	@Inject
	public UserCreatedConfirmationService(UserRepository repository, MessageQueueService messageQueueService
																			 ) {
		this.repository = repository;
		this.messageQueueService = messageQueueService;
	}

	@Scheduled(fixedDelay = "5s", initialDelay = "5s")
	public void resendUserCreated() {
		List<UserRecord> unconfirmedUsers = repository.findUnconfirmed();
		LOG.info("Resending UserCreated for {} users", unconfirmedUsers.size());
		for (UserRecord userRecord : unconfirmedUsers) {
				sendUserCreated(userRecord);
		}
	}

	public void sendUserCreated(UserRecord userRecord) {
		LOG.info("Sending UserCreated = {}", userRecord.getName());
		UserCreated userCreated = new UserCreated(userRecord.getId().toString(), userRecord.getName(), userRecord.getCreateTime().toString());
		messageQueueService.sendEvent(userCreated);
	}
}
