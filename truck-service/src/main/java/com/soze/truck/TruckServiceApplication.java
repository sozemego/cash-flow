package com.soze.truck;

import com.soze.common.client.ClockServiceClient;
import com.soze.common.dto.Clock;
import com.soze.common.resilience.RetryUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.time.Duration;


@SpringBootApplication
@EnableDiscoveryClient
@EnableScheduling
public class TruckServiceApplication {

	private static final Logger LOG = LoggerFactory.getLogger(TruckServiceApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(TruckServiceApplication.class, args);
	}

	@Autowired
	private ClockServiceClient clockServiceClient;

	@Bean
	@Profile("!test")
	public Clock clock() {
		return RetryUtils.retry(2500, Duration.ofMillis(5000), () -> {
			LOG.info("Fetching clock");
			try {
				return clockServiceClient.getClock();
			} catch (Exception e) {
				LOG.warn("Error when fetching clock", e);
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
