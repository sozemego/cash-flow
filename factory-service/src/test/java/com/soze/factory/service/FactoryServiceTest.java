package com.soze.factory.service;

import com.soze.common.dto.Clock;
import com.soze.common.dto.Resource;
import com.soze.factory.FactoryConverter;
import com.soze.factory.repository.FactoryRepository;
import com.soze.factory.world.RemoteWorldService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class FactoryServiceTest {

	private FactoryService factoryService;

	@Autowired
	private FactoryTemplateLoader factoryTemplateLoader;

	@Autowired
	private FactoryConverter factoryConverter;

	@Autowired
	private RemoteWorldService remoteWorldService;

	@Autowired
	private FactoryRepository repository;

	@BeforeEach
	public void setup() {
		this.factoryService = new FactoryService(
			factoryConverter, repository, new Clock(60, System.currentTimeMillis()));
	}

	@Test
	public void sell_factoryMustExist() {
		Assertions.assertThrows(IllegalStateException.class, () -> this.factoryService.sell("factoryId", Resource.WOOD, 1));
	}

	private void sleep(long ms) throws InterruptedException {
		long interval = 100;
		long time = 0;
		while (time <= ms) {
			Thread.sleep(interval);
			time += interval;
		}
	}
}