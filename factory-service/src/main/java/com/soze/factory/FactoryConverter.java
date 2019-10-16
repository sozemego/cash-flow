package com.soze.factory;

import com.soze.common.dto.FactoryDTO;
import com.soze.common.dto.FactoryStorageDTO;
import com.soze.common.dto.ProducerDTO;
import com.soze.common.dto.StorageDTO;
import com.soze.factory.aggregate.Factory;
import com.soze.factory.aggregate.FactoryStorage;
import com.soze.factory.aggregate.GeneralStorage;
import com.soze.factory.aggregate.Producer;
import org.springframework.stereotype.Service;

@Service
public class FactoryConverter {

	public FactoryDTO convert(Factory factory) {
		FactoryDTO factoryDTO = new FactoryDTO();
		factoryDTO.setId(factory.getId().toString());
		factoryDTO.setName(factory.getName());
		factoryDTO.setTexture(factory.getTexture());
		factoryDTO.setCityId(factory.getCityId());

		FactoryStorage factoryStorage = factory.getStorage();
		FactoryStorageDTO factoryStorageDTO = new FactoryStorageDTO(factoryStorage.getCapacities(), factoryStorage.getResources(), factoryStorage.getPrices());
		factoryDTO.setStorage(factoryStorageDTO);

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
