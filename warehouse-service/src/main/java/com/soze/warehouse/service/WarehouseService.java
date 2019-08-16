package com.soze.warehouse.service;

import com.soze.warehouse.domain.Warehouse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Service
public class WarehouseService {

	private static final Logger LOG = LoggerFactory.getLogger(WarehouseService.class);

	private final WarehouseTemplateLoader warehouseTemplateLoader;

	private final List<Warehouse> warehouses = new ArrayList<>();

	@Autowired
	public WarehouseService(WarehouseTemplateLoader warehouseTemplateLoader) {
		this.warehouseTemplateLoader = warehouseTemplateLoader;
	}

	@PostConstruct
	public void setup() {
		Warehouse warehouse1 = warehouseTemplateLoader.constructWarehouseByTemplateId("WAREHOUSE");
		addWarehouse(warehouse1);
		Warehouse warehouse2 = warehouseTemplateLoader.constructWarehouseByTemplateId("WAREHOUSE");
		addWarehouse(warehouse2);
	}

	public void addWarehouse(Warehouse warehouse) {
		LOG.info("Adding warehouse = {} with templateId = {}", warehouse.getId(), warehouse.getTemplateId());
		warehouses.add(warehouse);
	}

}
