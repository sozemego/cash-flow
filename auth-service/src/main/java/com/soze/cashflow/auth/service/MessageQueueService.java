package com.soze.cashflow.auth.service;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmListener;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.soze.common.json.JsonUtils;
import com.soze.common.resilience.RetryUtils;
import io.micronaut.context.annotation.Context;
import io.micronaut.context.annotation.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.jms.JmsProperties;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

@Singleton
@Context
public class MessageQueueService {

	private static final Logger LOG = LoggerFactory.getLogger(MessageQueueService.class);

	private static final String EXCHANGE_NAME = "domain-event-queue";
	private static final String DEAD_LETTER_QUEUE_NAME = "domain-event-queue-dead-letter";

	private final String queueHost;

	private Connection connection;

	@Inject
	public MessageQueueService(@Value("${queue.host}") String queueHost) {
		this.queueHost = queueHost;
	}

	@PostConstruct
	public void setup() {
		LOG.info("{} init...", this.getClass().getSimpleName());
		connect();
	}

	public void sendEvent(Object object) {
		this.sendEvent(object, null);
	}

	public void sendEvent(Object object, ConfirmListener confirmListener) {
		LOG.trace("Sending event {}", object);
		String payload = JsonUtils.serialize(object);
		send(payload.getBytes(), confirmListener);
	}

	private void send(byte[] payload, ConfirmListener confirmListener) {
		RetryUtils.retry(25, Duration.ofMillis(1000), () -> {
			try {
				Channel channel = connection.createChannel();
				if (confirmListener != null) {
					channel.addConfirmListener(confirmListener);
				}
				channel.basicPublish(EXCHANGE_NAME, "", null, payload);
			} catch (Throwable t) {
				LOG.info("Throwable", t);
				throw new IllegalStateException(t);
			}
		});
	}

	private void connect() {
		LOG.info("Connecting to exchange = {}", EXCHANGE_NAME);
		RetryUtils.retry(10, Duration.ofMillis(2000), () -> {
			ConnectionFactory factory = new ConnectionFactory();
			try {
				Connection connection = factory.newConnection(queueHost);
				Channel channel = connection.createChannel();
				channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
				setConnection(connection);

				Map<String, Object> args = new HashMap<String, Object>();
				args.put("x-dead-letter-exchange", EXCHANGE_NAME);
				channel.queueDeclare(DEAD_LETTER_QUEUE_NAME, true, false, false, args);

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
