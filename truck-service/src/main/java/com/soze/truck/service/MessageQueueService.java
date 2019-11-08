package com.soze.truck.service;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.soze.common.json.JsonUtils;
import com.soze.common.resilience.RetryUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

@Service
@Profile("!test")
public class MessageQueueService {

	private static final Logger LOG = LoggerFactory.getLogger(MessageQueueService.class);

	private static final String QUEUE_NAME = "game-event-queue";

	private final String queueHost;

	private Connection connection;

	@Autowired
	public MessageQueueService(@Value("${queue.host}") String queueHost) {
		this.queueHost = queueHost;
	}

	@PostConstruct
	public void setup() {
		LOG.info("{} init...", this.getClass().getSimpleName());
		connect();
	}

	public void sendEvent(Object object) {
		LOG.trace("Sending event {}", object);
		String payload = JsonUtils.serialize(object);
		send(payload.getBytes());
	}

	private void send(byte[] payload) {
		RetryUtils.retry(25, Duration.ofMillis(1000), () -> {
			try (Channel channel = connection.createChannel()) {
				channel.basicPublish("", QUEUE_NAME, null, payload);
			} catch (Throwable t) {
				LOG.info("Throwable", t);
				throw new IllegalStateException(t);
			}
		});
	}

	private void connect() {
		LOG.info("Connecting to queue = {}", QUEUE_NAME);
		RetryUtils.retry(10, Duration.ofMillis(2000), () -> {
			ConnectionFactory factory = new ConnectionFactory();
			try {
				Connection connection = factory.newConnection(queueHost);
				Channel channel = connection.createChannel();
				Map<String, Object> args = new HashMap<>();
				args.put("x-queue-type", "classic");
				channel.queueDeclare(QUEUE_NAME, true, false, false, args);
				setConnection(connection);
			} catch (IOException | TimeoutException e) {
				e.printStackTrace();
				throw new IllegalStateException(e);
			}
		});
	}

	private void setConnection(Connection connection) {
		this.connection = connection;
	}

}
