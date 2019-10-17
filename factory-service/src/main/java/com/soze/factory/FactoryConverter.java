package com.soze.factory;

import com.soze.common.dto.*;
import com.soze.factory.aggregate.Factory;
import com.soze.factory.aggregate.FactoryStorage;
import com.soze.factory.aggregate.GeneralStorage;
import com.soze.factory.aggregate.Producer;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class FactoryConverter {

	public FactoryDTO convert(Factory factory) {
		FactoryDTO factoryDTO = new FactoryDTO();
		factoryDTO.setId(factory.getId().toString());
		factoryDTO.setName(factory.getName());
		factoryDTO.setTexture(factory.getTexture());
		factoryDTO.setCityId(factory.getCityId());

		FactoryStorage factoryStorage = factory.getStorage();
		Map<Resource, StorageSlotDTO> resourceDTOs = new HashMap<>();
		factoryStorage.getResources().forEach((resource, storageSlot) -> {
			resourceDTOs.put(resource, new StorageSlotDTO(resource, storageSlot.getCount(), storageSlot.getCapacity(), storageSlot.getPrice()));
		});
		factoryDTO.setStorage(resourceDTOs);

		ProducerDTO producerDTO = new ProducerDTO();
		Producer producer = factory.getProducer();
		producerDTO.setProducing(producer.isProducing());
		producerDTO.setTime(producer.getTime());
		producerDTO.setResource(producer.getResource().name());
		producerDTO.setProductionStartTime(producer.getProductionStartTime());
		factoryDTO.setProducer(producerDTO);

		return factoryDTO;
	}

}
