package com.soze.cashflow.auth.service;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.soze.common.json.JsonUtils;
import com.soze.common.message.queue.QueueMessage;
import com.soze.common.resilience.RetryUtils;
import io.micronaut.context.annotation.Context;
import io.micronaut.context.annotation.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;

@Singleton
@Context
public class MessageQueueService {

	private static final Logger LOG = LoggerFactory.getLogger(MessageQueueService.class);

	private static final String EXCHANGE_NAME = "domain-event-queue";

	private final String queueHost;

	private Connection connection;

	private final Set<Consumer<QueueMessage>> queueMessageConsumers = Collections.synchronizedSet(new HashSet<>());

	@Inject
	public MessageQueueService(@Value("${queue.host}") String queueHost) {
		this.queueHost = queueHost;
	}

	@PostConstruct
	public void setup() {
		LOG.info("{} init...", this.getClass().getSimpleName());
		connectToExchange();
	}

	public void sendEvent(Object object) {
		LOG.trace("Sending event {}", object);
		String payload = JsonUtils.serialize(object);
		send(payload.getBytes());
	}

	private void send(byte[] payload) {
		RetryUtils.retry(5, Duration.ofMillis(1000), () -> {
			try (Channel channel = connection.createChannel()) {
				channel.basicPublish(EXCHANGE_NAME, "", null, payload);
			} catch (Throwable t) {
				LOG.info("Throwable", t);
				throw new IllegalStateException(t);
			}
		});

	}

	private void connectToExchange() {
		LOG.info("Connecting to exchange = {}", EXCHANGE_NAME);
		RetryUtils.retry(10, Duration.ofMillis(2000), () -> {
			ConnectionFactory factory = new ConnectionFactory();
			try {
				Connection connection = factory.newConnection(queueHost);
				Channel channel = connection.createChannel();
				channel.exchangeDeclare(EXCHANGE_NAME, "fanout");

				String queueName = channel.queueDeclare().getQueue();
				channel.queueBind(queueName, EXCHANGE_NAME, "");

				channel.basicConsume(queueName, true, (consumerTag, delivery) -> {
					String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
					QueueMessage queueMessage = JsonUtils.parse(message, QueueMessage.class);
					for (Consumer<QueueMessage> consumer : queueMessageConsumers) {
						consumer.accept(queueMessage);
					}
				}, consumerTag -> {

				});

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

	public void registerQueueMessageConsumer(Consumer<QueueMessage> consumer) {
		Objects.requireNonNull(consumer);
		queueMessageConsumers.add(consumer);
	}

}
