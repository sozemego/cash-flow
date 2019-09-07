package com.soze.truck.saga;

import com.soze.common.dto.FactoryDTO;
import com.soze.common.dto.Resource;
import com.soze.common.dto.SellResultDTO;
import com.soze.common.dto.StorageDTO;
import com.soze.common.message.server.StorageContentChanged;
import com.soze.truck.domain.Storage;
import com.soze.truck.domain.Truck;
import com.soze.truck.external.RemoteFactoryService;
import com.soze.truck.service.TruckService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class BuyResourceSaga {

	private static final Logger LOG = LoggerFactory.getLogger(BuyResourceSaga.class);

	private final TruckService truckService;
	private final RemoteFactoryService factoryService;

	private final String truckId;
	private final String factoryId;
	private final Resource resource;
	private final int count;

	public BuyResourceSaga(TruckService truckService, RemoteFactoryService factoryService, String truckId,
												 String factoryId, Resource resource, int count
												) {
		this.truckService = truckService;
		this.factoryService = factoryService;
		this.truckId = truckId;
		this.factoryId = factoryId;
		this.resource = resource;
		this.count = count;
	}

	public void run() {
		LOG.info("Truck with id = {} wants to buy {} {} from factoryId = {}", truckId, resource, count, factoryId);
		Optional<Truck> truckOptional = this.truckService.getTruck(truckId);
		if (!truckOptional.isPresent()) {
			LOG.info("Truck with id = {} does not exist, stopping.", truckId);
			return;
		}

		Truck truck = truckOptional.get();

		Storage truckStorage = truck.getStorage();
		if (!truckStorage.canFit(resource, count)) {
			LOG.info("Truck {} cannot fit {} of {}", truckId, count, resource);
			return;
		}

		Optional<FactoryDTO> factoryOptional = factoryService.getFactory(factoryId);
		if (!factoryOptional.isPresent()) {
			LOG.info("Factory with id = {} does not exist, stopping.", factoryId);
			return;
		}

		FactoryDTO factoryDTO = factoryOptional.get();
		StorageDTO factoryStorage = factoryDTO.getStorage();
		long actualCount = factoryStorage.getResources().get(resource);
		if (actualCount < count) {
			LOG.info("Factory = {} does not have enough of {}. It has {}", factoryId, resource, actualCount);
			return;
		}

		truckStorage.addResource(resource, count);
		Runnable reverseAddResource = () -> {
			LOG.info("Reverse adding {} of resource {} to truck {}", count, resource, truck.getId());
			truckStorage.removeResource(resource, count);
		};

		try {
			SellResultDTO sellResult = factoryService.sell(factoryId, resource.name(), count);
			if (sellResult.getCount() != count) {
				reverseAddResource.run();
				LOG.info("Factory = {} was unable to sell {} of {}", factoryId, count, resource);
				return;
			}
		} catch (Exception e) {
			LOG.info("Exception when getting factory = {} to sell {} of {}", factoryId, count, resource, e);
			reverseAddResource.run();
		}

		LOG.info("Successfully bought {} of {} from {} for truck {}", count, resource, factoryId, truck.getId());

		StorageContentChanged storageContentChanged = new StorageContentChanged(truckId, resource, count);
		truckService.sendToAll(storageContentChanged);
	}
}
