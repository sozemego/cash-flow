package com.soze.factory.controller;

import com.soze.common.dto.FactoryDTO;
import com.soze.common.dto.Resource;
import com.soze.factory.FactoryConverter;
import com.soze.factory.client.FactoryServiceClient;
import com.soze.factory.domain.Factory;
import com.soze.factory.domain.SellResult;
import com.soze.factory.service.FactoryService;
import com.soze.factory.service.FactoryTemplateLoader;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.nio.file.Files;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@Api(value = "Factory")
public class FactoryController implements FactoryServiceClient {

	private static final Logger LOG = LoggerFactory.getLogger(FactoryController.class);

	private final FactoryService factoryService;
	private final FactoryTemplateLoader factoryTemplateLoader;
	private final FactoryConverter factoryConverter;
	private final HttpServletResponse response;

	@Autowired
	public FactoryController(FactoryService factoryService, FactoryTemplateLoader factoryTemplateLoader,
													 FactoryConverter factoryConverter, HttpServletResponse response
													) {
		this.factoryService = factoryService;
		this.factoryTemplateLoader = factoryTemplateLoader;
		this.factoryConverter = factoryConverter;
		this.response = response;
	}

	@GetMapping(value = "/")
	public List<FactoryDTO> getFactories() {
		LOG.info("Calling getFactories");
		List<FactoryDTO> factoryDTOS = factoryService.getFactories().stream().map(factoryConverter::convert).collect(
			Collectors.toList());
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
		Optional<Factory> factoryOptional = factoryService.getFactoryById(factoryId);
		if (!factoryOptional.isPresent()) {
			response.setStatus(HttpStatus.NOT_FOUND.value());
			return null;
		}
		return factoryConverter.convert(factoryOptional.get());
	}

	public SellResult sell(@RequestParam("factoryId") String factoryId, @RequestParam("resource") String resourceStr,
												 @RequestParam("count") Integer count
												) {
		LOG.info("Called /sell endpoint, factoryId = {}, resource = {}, count = {}", factoryId, resourceStr, count);
		Resource resource = Resource.valueOf(resourceStr);
		try {
			factoryService.sell(factoryId, resource, count);
			return new SellResult(factoryId, resource, count);
		} catch (Exception e) {
			LOG.warn("Exception when trying to sell resource", e);
			return new SellResult(factoryId, resource, 0);
		}

	}

}
