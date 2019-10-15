package com.soze.factory.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.soze.common.dto.Resource;
import com.soze.factory.aggregate.Factory;
import com.soze.factory.aggregate.FactoryStorage;
import com.soze.factory.command.ChangeResourceStorageCapacity;
import com.soze.factory.command.Command;
import com.soze.factory.repository.FactoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * The purpose of this service is to run once and upgrade all factories to the new
 * model of having capacity per resources, not one general capacity.
 */
@Service
@Profile("factory-storage-upgrade")
public class FactoryStorageUpgradeService {

	private static final Logger LOG = LoggerFactory.getLogger(FactoryStorageUpgradeService.class);

	private final FactoryTemplateLoader templateLoader;
	private final FactoryRepository repository;
	private final FactoryCommandService factoryCommandService;

	@Autowired
	public FactoryStorageUpgradeService(FactoryTemplateLoader templateLoader, FactoryRepository repository,
																			FactoryCommandService factoryCommandService
																		 ) {
		this.templateLoader = templateLoader;
		this.repository = repository;
		this.factoryCommandService = factoryCommandService;
	}

	@EventListener
	public void handleApplicationStart(ApplicationReadyEvent e) {
		LOG.info(this.getClass().getSimpleName() + " init...");

		List<Factory> factories = repository.getAll();
		LOG.info("Upgrading {} factories", factories.size());
		List<Command> correctiveCommands = new ArrayList<>();
		for (Factory factory : factories) {
			Map<Resource, Integer> intendedCapacities = getIntendedCapacities(factory);
			Map<Resource, Integer> capacityChanges = new HashMap<>();
			FactoryStorage storage = factory.getStorage();
			Map<Resource, Integer> currentCapacities = storage.getCapacities();
			for (Resource resource : Resource.values()) {
				Integer currentCapacity = currentCapacities.get(resource);
				Integer intendedCapacity = intendedCapacities.get(resource);

				if (intendedCapacity == null && currentCapacity != null) {
					capacityChanges.put(resource, -currentCapacity);
				}

				if (intendedCapacity == null && currentCapacity == null) {

				}

				if (intendedCapacity != null && currentCapacity == null) {
					capacityChanges.put(resource, intendedCapacity);
				}

				if (intendedCapacity != null && currentCapacity != null) {
					int difference = intendedCapacity - currentCapacity;
					if (difference != 0) {
						capacityChanges.put(resource, difference);
					}
				}
			}
			Command command = new ChangeResourceStorageCapacity(factory.getId(), capacityChanges);
			correctiveCommands.add(command);
		}
		LOG.info("Applying {} corrective commands", correctiveCommands.size());
		for (Command correctiveCommand : correctiveCommands) {
			correctiveCommand.accept(factoryCommandService);
		}
	}

	private Map<Resource, Integer> getIntendedCapacities(Factory factory) {
		Optional<JsonNode> templateOptional = templateLoader.findRootByName(factory.getName());
		if (!templateOptional.isPresent()) {
			throw new IllegalStateException("No template for factory with name: " + factory.getName());
		}
		JsonNode template = templateOptional.get();
		JsonNode capacitiesTemplate = template.get("storage").get("capacities");
		Map<Resource, Integer> intendedCapacities = new HashMap<>();
		capacitiesTemplate.fields().forEachRemaining(e -> {
			String resourceName = e.getKey();
			int capacity = e.getValue().asInt();
			intendedCapacities.put(Resource.valueOf(resourceName), capacity);
		});
		return intendedCapacities;
	}
}
