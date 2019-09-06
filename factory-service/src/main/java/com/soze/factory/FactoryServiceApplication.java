package com.soze.factory;

import com.soze.clock.client.ClockServiceClient;
import com.soze.clock.domain.Clock;
import com.soze.common.client.WorldServiceClient;
import com.soze.common.resilience.RetryUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Profile;

import java.time.Duration;

@SpringBootApplication
@Configuration
@EnableDiscoveryClient
public class FactoryServiceApplication {

	private static final Logger LOG = LoggerFactory.getLogger(FactoryServiceApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(FactoryServiceApplication.class, args);
	}

	@Bean
	public WorldServiceClient worldServiceClient() {
		return WorldServiceClient.createClient();
	}

	@Autowired
	@Lazy
	private ClockServiceClient clockServiceClient;

	@Bean
	@Profile("!test")
	public Clock clock() {
		LOG.info("Init clock ...");
		return RetryUtils.retry(25, Duration.ofMillis(2000), () -> {
			try {
				return clockServiceClient.getClock();
			} catch (Exception e) {
				LOG.info("Error during clock fetching", e);
				throw e;
			}
		});
	}
}
