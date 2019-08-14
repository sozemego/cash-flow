package com.soze.factory.controller;

import com.soze.common.dto.FactoryDTO;
import com.soze.factory.FactoryConverter;
import com.soze.factory.service.FactoryService;
import com.soze.factory.service.FactoryTemplateLoader;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Api(value = "Factory")
public class FactoryController {

	private static final Logger LOG = LoggerFactory.getLogger(FactoryController.class);

	private final FactoryService factoryService;
	private final FactoryTemplateLoader factoryTemplateLoader;
	private final FactoryConverter factoryConverter;

	@Autowired
	public FactoryController(FactoryService factoryService, FactoryTemplateLoader factoryTemplateLoader,
													 FactoryConverter factoryConverter
													) {
		this.factoryService = factoryService;
		this.factoryTemplateLoader = factoryTemplateLoader;
		this.factoryConverter = factoryConverter;
	}

	@GetMapping(value = "/")
	public List<FactoryDTO> getFactories() {
		LOG.info("Calling getFactories");
		List<FactoryDTO> factoryDTOS = factoryService.getFactories()
																								 .stream()
																								 .map(factoryConverter::convert)
																								 .collect(Collectors.toList());
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

}
