package com.soze.truck.saga;

import com.soze.common.dto.*;
import com.soze.common.message.server.StorageContentChanged;
import com.soze.truck.domain.Storage;
import com.soze.truck.domain.Truck;
import com.soze.truck.external.RemoteFactoryService;
import com.soze.truck.external.RemotePlayerService;
import com.soze.truck.repository.TruckRepository;
import com.soze.truck.service.TruckService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class BuyResourceSaga {

	private static final Logger LOG = LoggerFactory.getLogger(BuyResourceSaga.class);

	private final TruckService truckService;
	private final TruckRepository truckRepository;
	private final RemoteFactoryService factoryService;
	private final RemotePlayerService playerService;

	private final String truckId;
	private final String factoryId;
	private final Resource resource;
	private final int count;

	public BuyResourceSaga(TruckService truckService, TruckRepository truckRepository, RemoteFactoryService factoryService,
												 RemotePlayerService playerService, String truckId, String factoryId, Resource resource, int count
												) {
		this.truckService = truckService;
		this.truckRepository = truckRepository;
		this.factoryService = factoryService;
		this.playerService = playerService;
		this.truckId = truckId;
		this.factoryId = factoryId;
		this.resource = resource;
		this.count = count;
	}

	public void run() {
		LOG.info("Truck with id = {} wants to buy {} {} from factoryId = {}", truckId, resource, count, factoryId);
		if (count == 0) {
			LOG.info("Cannot buy 0 of any resource");
			return;
		}
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

		long cost = count * 5; // for now assume static cost
		PlayerDTO playerDTO = playerService.getPlayer();
		if (playerDTO.getCash() < cost) {
			LOG.info("Player {}-{} does not have enough cash [required - {}, actual - {}]!", playerDTO.getId(), playerDTO.getName(), cost, playerDTO.getCash());
			return;
		}

		TransferResultDTO transferResultDTO = playerService.transfer(-cost);
		if (transferResultDTO.getAmountTransferred() != -cost) {
			LOG.info("Could not transfer required amount from player, transferred = {}", transferResultDTO.getAmountTransferred());
			return;
		}

		Runnable reverseTransfer = () -> {
			LOG.info("Reverse transferring amount = {} from player", cost);
			playerService.transfer(cost);
		};

		truckStorage.addResource(resource, count);
		truckRepository.update(truck);
		Runnable reverseAddResource = () -> {
			LOG.info("Reverse adding {} of resource {} to truck {}", count, resource, truck.getId());
			truckStorage.removeResource(resource, count);
			truckRepository.update(truck);
		};

		try {
			SellResultDTO sellResult = factoryService.sell(factoryId, resource.name(), count);
			if (sellResult.getCount() != count) {
				reverseAddResource.run();
				reverseTransfer.run();
				LOG.info("Factory = {} was unable to sell {} of {}", factoryId, count, resource);
				return;
			}
		} catch (Exception e) {
			LOG.info("Exception when getting factory = {} to sell {} of {}", factoryId, count, resource, e);
			reverseAddResource.run();
			reverseTransfer.run();
		}

		LOG.info("Successfully bought {} of {} from {} for truck {}", count, resource, factoryId, truck.getId());

		StorageContentChanged storageContentChanged = new StorageContentChanged(truckId, resource, count);
		truckService.sendToAll(storageContentChanged);
	}
}
