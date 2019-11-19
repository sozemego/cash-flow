package com.soze.truck.saga;

import com.soze.common.dto.*;
import com.soze.common.message.server.StorageContentChanged;
import com.soze.truck.domain.Storage;
import com.soze.truck.domain.Truck;
import com.soze.truck.external.RemoteFactoryService;
import com.soze.truck.external.RemotePlayerService;
import com.soze.truck.repository.TruckRepository;
import com.soze.truck.ws.SessionRegistry;
import com.soze.truck.service.TruckService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.UUID;

public class SellResourceSaga {

	private static final Logger LOG = LoggerFactory.getLogger(SellResourceSaga.class);

	private final TruckService truckService;
	private final TruckRepository truckRepository;
	private final RemoteFactoryService factoryService;
	private final RemotePlayerService playerService;
	private final SessionRegistry sessionRegistry;

	private final UUID truckId;
	private final Resource resource;
	private final int count;
	private final String factoryId;

	public SellResourceSaga(TruckService truckService, TruckRepository truckRepository,
													RemoteFactoryService factoryService, RemotePlayerService playerService,
													SessionRegistry sessionRegistry, UUID truckId, Resource resource, int count, String factoryId
												 ) {
		this.truckService = truckService;
		this.truckRepository = truckRepository;
		this.factoryService = factoryService;
		this.playerService = playerService;
		this.sessionRegistry = sessionRegistry;
		this.truckId = truckId;
		this.resource = resource;
		this.count = count;
		this.factoryId = factoryId;
	}


	public void run() {
		LOG.info("Truck with id = {} wants to sell {} {} to factoryId = {}", truckId, resource, count, factoryId);
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
		if (!truckStorage.hasResource(resource, count)) {
			LOG.info("Truck {} does not have {} of {}", truckId, count, resource);
			return;
		}

		Optional<FactoryDTO> factoryOptional = factoryService.getFactory(factoryId);
		if (!factoryOptional.isPresent()) {
			LOG.info("Factory with id = {} does not exist, stopping.", factoryId);
			return;
		}

		FactoryDTO factoryDTO = factoryOptional.get();
		StorageSlotDTO storageSlotDTO = factoryDTO.getStorage().get(resource);
		int remainingCapacity = storageSlotDTO == null ? 0 : storageSlotDTO.getCapacity() - storageSlotDTO.getCount();
		if (remainingCapacity < count) {
			LOG.info("Factory {} cannot fit {} of {}", factoryId, count, resource);
			return;
		}

		int price = storageSlotDTO == null ? 0 :  storageSlotDTO.getPrice();

		long moneyGain = count * price;

		TransferResultDTO transferResultDTO = playerService.transfer(moneyGain);
		if (transferResultDTO.getAmountTransferred() != moneyGain) {
			LOG.info("Could not transfer required amount to player, transferred = {}", transferResultDTO.getAmountTransferred());
			return;
		}

		Runnable reverseTransfer = () -> {
			LOG.info("Reverse transferring amount = {} from player", moneyGain);
			playerService.transfer(-moneyGain);
		};

		truckStorage.removeResource(resource, count);
//		truckRepository.update(truck);
		Runnable reverseRemoveResource = () -> {
			LOG.info("Reverse removing {} of resource {} from truck {}", count, resource, truck.getId());
			truckStorage.addResource(resource, count);
//			truckRepository.update(truck);
		};

		try {
			BuyResultDTO buyResult = factoryService.buy(factoryId, resource.name(), count);
			if (buyResult.getCount() != count) {
				reverseTransfer.run();
				reverseRemoveResource.run();
				LOG.info("Factory = {} was unable to buy {} of {}", factoryId, count, resource);
				return;
			}
		} catch (Exception e) {
			LOG.info("Exception when getting factory = {} to buy {} of {}", factoryId, count, resource, e);
			reverseTransfer.run();
			reverseRemoveResource.run();
		}

		truckRepository.update(truck);
		LOG.info("Successfully bought {} of {} from {} for truck {}", count, resource, factoryId, truck.getId());

		StorageContentChanged storageContentChanged = new StorageContentChanged(truckId.toString(), resource, -count);
		sessionRegistry.sendToAll(storageContentChanged);
	}

}
