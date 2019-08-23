package com.soze.truck.world;

import com.soze.common.client.WorldServiceClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WorldServiceClientConfiguration {

	@Bean
	public WorldServiceClient worldServiceClient() {
		return WorldServiceClient.createClient();
	}

}
