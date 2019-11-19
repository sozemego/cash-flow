package com.soze.truck.service;

import com.soze.common.dto.StorageDTO;
import com.soze.common.dto.TruckDTO;
import com.soze.common.dto.TruckNavigationDTO;
import com.soze.truck.domain.Truck;
import com.soze.truck.domain.TruckNavigation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TruckConverter {

	private final TruckNavigationService truckNavigationService;

	@Autowired
	public TruckConverter(TruckNavigationService truckNavigationService) {
		this.truckNavigationService = truckNavigationService;
	}

	public TruckDTO convert(Truck truck) {
		TruckDTO truckDTO = new TruckDTO();
		truckDTO.setId(truck.getId().toString());
		truckDTO.setPlayerId(truck.getPlayerId().toString());
		truckDTO.setName(truck.getName());
		truckDTO.setSpeed(truck.getSpeed());
		truckDTO.setTemplateId(truck.getTemplateId());
		truckDTO.setTexture(truck.getTexture());

		StorageDTO storageDTO = new StorageDTO();
		storageDTO.setCapacity(truck.getStorage().getCapacity());
		storageDTO.getResources().putAll(truck.getStorage().getResources());
		truckDTO.setStorage(storageDTO);

		TruckNavigation navigation = truckNavigationService.getTruckNavigation(truck.getId());
		TruckNavigationDTO navigationDTO = convert(navigation);
		truckDTO.setNavigation(navigationDTO);

		return truckDTO;
	}

	public TruckNavigationDTO convert(TruckNavigation navigation) {
		TruckNavigationDTO truckNavigationDTO = new TruckNavigationDTO();
		truckNavigationDTO.setArrivalTime(navigation.arrivalTime);
		truckNavigationDTO.setCurrentCityId(navigation.currentCityId);
		truckNavigationDTO.setNextCityId(navigation.nextCityId);
		truckNavigationDTO.setStartTime(navigation.startTime);
		return truckNavigationDTO;
	}

}
