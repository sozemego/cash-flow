package com.soze.factory.service;

import com.soze.common.dto.CityDTO;
import com.soze.factory.repository.FactoryRepository;
import com.soze.factory.command.Command;
import com.soze.factory.world.RemoteWorldService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Profile("!test")
public class FactoryStarterService {

	private static final Logger LOG = LoggerFactory.getLogger(FactoryStarterService.class);

	private final RemoteWorldService remoteWorldService;
	private final FactoryTemplateLoader templateLoader;
	private final FactoryCommandService factoryCommandService;
	private final FactoryRepository factoryRepository;

	@Autowired
	public FactoryStarterService(RemoteWorldService remoteWorldService, FactoryTemplateLoader templateLoader,
															 FactoryCommandService factoryCommandService, FactoryRepository factoryRepository
															) {
		this.remoteWorldService = remoteWorldService;
		this.templateLoader = templateLoader;
		this.factoryCommandService = factoryCommandService;
		this.factoryRepository = factoryRepository;
	}

	@EventListener
	public void handleApplicationStart(ApplicationReadyEvent e) {
		LOG.info("Factory start...");
		if (factoryRepository.factoryCount() > 0) {
			LOG.info("Some factories already existing, skipping the starter.");
			return;
		}
		CityDTO wroclaw = remoteWorldService.getCityByName("Wroclaw");
		CityDTO warsaw = remoteWorldService.getCityByName("Warsaw");
		CityDTO opole = remoteWorldService.getCityByName("Opole");

		List<Command> commands1 = templateLoader.getFactoryCommandsByTemplateId(UUID.randomUUID(),"FORESTER", wroclaw.id);
		commands1.forEach(command -> command.accept(factoryCommandService));
		List<Command> commands2 = templateLoader.getFactoryCommandsByTemplateId(UUID.randomUUID(),"FORESTER", wroclaw.id);
		commands2.forEach(command -> command.accept(factoryCommandService));
		List<Command> commands3 = templateLoader.getFactoryCommandsByTemplateId(UUID.randomUUID(),"FORESTER", warsaw.id);
		commands3.forEach(command -> command.accept(factoryCommandService));
		List<Command> commands4 = templateLoader.getFactoryCommandsByTemplateId(UUID.randomUUID(),"STONE_QUARRY", opole.id);
		commands4.forEach(command -> command.accept(factoryCommandService));
	}

}
