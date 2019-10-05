package com.soze.factory.controller;

import com.soze.common.client.FactoryServiceClient;
import com.soze.common.dto.FactoryDTO;
import com.soze.common.dto.Resource;
import com.soze.common.dto.SellResultDTO;
import com.soze.factory.FactoryConverter;
import com.soze.factory.aggregate.Factory;
import com.soze.factory.command.SellResource;
import com.soze.factory.event.Event;
import com.soze.factory.repository.FactoryRepository;
import com.soze.factory.service.FactoryCommandService;
import com.soze.factory.service.FactoryService;
import com.soze.factory.service.FactoryTemplateLoader;
import com.soze.factory.store.EventStore;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.nio.file.Files;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@Api(value = "Factory")
public class FactoryController implements FactoryServiceClient {

	private static final Logger LOG = LoggerFactory.getLogger(FactoryController.class);

	private final FactoryService factoryService;
	private final FactoryCommandService factoryCommandService;
	private final FactoryTemplateLoader factoryTemplateLoader;
	private final FactoryConverter factoryConverter;
	private final FactoryRepository factoryRepository;
	private final HttpServletResponse response;
	private final EventStore eventStore;

	@Autowired
	public FactoryController(FactoryService factoryService, FactoryCommandService factoryCommandService,
													 FactoryTemplateLoader factoryTemplateLoader, FactoryConverter factoryConverter,
													 FactoryRepository factoryRepository, HttpServletResponse response, EventStore eventStore
													) {
		this.factoryService = factoryService;
		this.factoryCommandService = factoryCommandService;
		this.factoryTemplateLoader = factoryTemplateLoader;
		this.factoryConverter = factoryConverter;
		this.factoryRepository = factoryRepository;
		this.response = response;
		this.eventStore = eventStore;
	}

	@GetMapping(value = "/")
	public List<FactoryDTO> getFactories() {
		LOG.info("Calling getFactories");
		List<Factory> factories = factoryRepository.getAll();
		List<FactoryDTO> factoryDTOS = factories.stream().map(factoryConverter::convert).collect(Collectors.toList());
		LOG.info("Returning {} factories", factoryDTOS.size());
		return factoryDTOS;
	}

	@GetMapping(value = "/templates")
	public String getTemplates() throws Exception {
		LOG.info("Calling getTemplates");
		File entities = factoryTemplateLoader.getEntities();
		List<String> list = Files.readAllLines(entities.toPath());
		return String.join("\n", list);
	}

	public FactoryDTO getFactory(String factoryId) {
		LOG.info("called /getFactory, factoryId = {}", factoryId);
		Optional<Factory> factoryOptional = factoryRepository.findById(UUID.fromString(factoryId));
		if (!factoryOptional.isPresent()) {
			response.setStatus(HttpStatus.NOT_FOUND.value());
			return null;
		}
		return factoryConverter.convert(factoryOptional.get());
	}

	public SellResultDTO sell(String factoryId, String resourceStr, Integer count) {
		LOG.info("Called /sell endpoint, factoryId = {}, resource = {}, count = {}", factoryId, resourceStr, count);
		Resource resource = Resource.valueOf(resourceStr);
		try {
			factoryCommandService.visit(new SellResource(UUID.fromString(factoryId), resource, count));
			return new SellResultDTO(factoryId, resource, count);
		} catch (Exception e) {
			LOG.warn("Exception when trying to sell resource", e);
			return new SellResultDTO(factoryId, resource, 0);
		}

	}

	@GetMapping("/events")
	public List<Event> getEvents(@RequestParam String id) {
		LOG.info("Called getEvents, id = {}", id);
		return eventStore.getEventsForEntity(id);
	}

}
