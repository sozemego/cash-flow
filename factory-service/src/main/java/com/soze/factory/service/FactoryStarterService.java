package com.soze.factory.service;

import com.soze.common.dto.CityDTO;
import com.soze.factory.repository.FactoryRepository;
import com.soze.factory.command.Command;
import com.soze.factory.world.RemoteWorldService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
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

		List<Command> commands = new ArrayList<>();
		commands.addAll(templateLoader.getFactoryCommandsByTemplateId("FORESTER", wroclaw.id));
		commands.addAll(templateLoader.getFactoryCommandsByTemplateId("FORESTER", wroclaw.id));
		commands.addAll(templateLoader.getFactoryCommandsByTemplateId("FORESTER", warsaw.id));
		for (Command command : commands) {
			command.accept(factoryCommandService);
		}
	}

}
