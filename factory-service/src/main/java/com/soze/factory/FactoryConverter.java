package com.soze.factory;

import com.soze.common.dto.FactoryDTO;
import com.soze.common.dto.ProducerDTO;
import com.soze.common.dto.StorageDTO;
import com.soze.factory.domain.Factory;
import com.soze.factory.domain.Producer;
import org.springframework.stereotype.Service;

@Service
public class FactoryConverter {

	public FactoryDTO convert(Factory factory) {
		FactoryDTO factoryDTO = new FactoryDTO();
		factoryDTO.setId(factory.getId());
		factoryDTO.setTemplateId(factory.getTemplateId());
		factoryDTO.setName(factory.getName());
		factoryDTO.setTexture(factory.getTexture());
		factoryDTO.setCityId(factory.getCityId());

		StorageDTO storageDTO = new StorageDTO();
		storageDTO.setCapacity(factory.getStorage().getCapacity());
		storageDTO.getResources().putAll(factory.getStorage().getResources());
		factoryDTO.setStorage(storageDTO);

		ProducerDTO producerDTO = new ProducerDTO();
		Producer producer = factory.getProducer();
		producerDTO.setProducing(producer.isProducing());
		producerDTO.setProgress(producer.getProgress());
		producerDTO.setTime(producer.getTime());
		producerDTO.setResource(producer.getResource());
		producerDTO.setProductionStartTime(producer.getProductionStartTime());
		factoryDTO.setProducer(producerDTO);

		return factoryDTO;
	}

}
