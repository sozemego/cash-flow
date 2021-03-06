package com.soze.truck.service;

import com.soze.common.json.JsonUtils;
import com.soze.common.message.queue.PlayerCreated;
import com.soze.common.message.queue.QueueMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.PartitionOffset;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.stereotype.Service;

@Service
public class PlayerCreatedListener {

	private static final Logger LOG = LoggerFactory.getLogger(PlayerCreatedListener.class);

	private final TruckServiceStarter truckServiceStarter;

	@Autowired
	public PlayerCreatedListener(TruckServiceStarter truckServiceStarter) {
		this.truckServiceStarter = truckServiceStarter;
	}

	@KafkaListener(groupId = "player-created-id",
		topicPartitions = @TopicPartition(topic = "domain", partitionOffsets = @PartitionOffset(initialOffset = "0", partition = "0")),
		autoStartup="${kafka.listeners.enabled}"
	)
	public void onPlayerCreated(String message) {
		QueueMessage queueMessage = JsonUtils.parse(message, QueueMessage.class);
		if (queueMessage.getType() == QueueMessage.QueueMessageType.PLAYER_CREATED) {
			LOG.info("Handling {}", queueMessage);
			truckServiceStarter.createPlayer((PlayerCreated) queueMessage);
		}
	}

}
