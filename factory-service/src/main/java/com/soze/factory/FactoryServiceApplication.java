package com.soze.factory;

import com.soze.common.client.WorldServiceClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
@Configuration
public class FactoryServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(FactoryServiceApplication.class, args);
	}

	@Bean
	public WorldServiceClient worldServiceClient() {
		return WorldServiceClient.createClient();
	}
}
