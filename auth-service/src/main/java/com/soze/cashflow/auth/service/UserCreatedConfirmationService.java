package com.soze.cashflow.auth.service;

import com.soze.cashflow.auth.domain.tables.records.UserRecord;
import com.soze.cashflow.auth.repository.UserRepository;
import com.soze.common.message.queue.QueueMessage;
import com.soze.common.message.queue.UserCreated;
import com.soze.common.message.queue.UserCreatedConfirmation;
import io.micronaut.scheduling.annotation.Scheduled;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.UUID;

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
		messageQueueService.registerQueueMessageConsumer(this::consumeQueueMessage);
	}

	@Scheduled(fixedDelay = "360s", initialDelay = "240s")
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

	private void consumeQueueMessage(QueueMessage queueMessage) {
		LOG.info("Consuming {}", queueMessage.getType());
		if (queueMessage.getType() == QueueMessage.QueueMessageType.USER_CREATED_CONFIRMATION) {
			handleUserCreatedConfirmation((UserCreatedConfirmation) queueMessage);
		}
	}

	private void handleUserCreatedConfirmation(UserCreatedConfirmation confirmation) {
		UserRecord userRecord = repository.findUserById(UUID.fromString(confirmation.id));
		if (userRecord == null) {
			LOG.warn("Cannot confirm used creation for user {}, does not exist", confirmation.id);
			return;
		}
		userRecord.setConfirmed(true);
		userRecord.store();
	}
}
