package com.soze.factory;

import com.soze.common.client.ClockServiceClient;
import com.soze.common.dto.Clock;
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
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.time.Duration;

@SpringBootApplication
@Configuration
@EnableDiscoveryClient
@EnableScheduling
public class FactoryServiceApplication {

	private static final Logger LOG = LoggerFactory.getLogger(FactoryServiceApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(FactoryServiceApplication.class, args);
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

	@Bean
	public TaskScheduler taskScheduler() {
		ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
		taskScheduler.setPoolSize(10);
		taskScheduler.initialize();
		return taskScheduler;
	}
}
