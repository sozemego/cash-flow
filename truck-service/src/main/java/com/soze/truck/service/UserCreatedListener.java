package com.soze.truck.service;

import com.soze.common.json.JsonUtils;
import com.soze.common.message.queue.QueueMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.PartitionOffset;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.stereotype.Service;


@Service
public class UserCreatedListener {

	private static final Logger LOG = LoggerFactory.getLogger(UserCreatedListener.class);

	@KafkaListener(groupId = "user-created-id",
		topicPartitions = @TopicPartition(topic = "domain", partitionOffsets = @PartitionOffset(initialOffset = "0", partition = "0"))
	)
	public void onUserCreated(String message) {
		QueueMessage queueMessage = JsonUtils.parse(message, QueueMessage.class);
		if (queueMessage.getType() == QueueMessage.QueueMessageType.USER_CREATED) {
			System.out.println(queueMessage);
		}
	}

}
