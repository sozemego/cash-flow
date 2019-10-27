package com.soze.factory.event;

import com.soze.common.dto.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Takes care of upgrading events from one version to another.
 */
@Service
public class EventUpcastService {

	private static final Logger LOG = LoggerFactory.getLogger(EventUpcastService.class);

	private final Map<Event.EventType, Function<Event, Event>> upcasts = new HashMap<>();

	@PostConstruct
	public void setup() {
		LOG.info("EventUpcastService init...");

		upcasts.put(Event.EventType.STORAGE_CAPACITY_CHANGED, event -> {
			StorageCapacityChanged storageCapacityChanged = (StorageCapacityChanged) event;
			ResourceStorageCapacityChanged resourceStorageCapacityChanged = new ResourceStorageCapacityChanged();
			resourceStorageCapacityChanged.entityId = storageCapacityChanged.entityId;
			resourceStorageCapacityChanged.timestamp = storageCapacityChanged.timestamp;
			resourceStorageCapacityChanged.version = storageCapacityChanged.version;
			Map<Resource, Integer> capacityChanged = new HashMap<>();
			for (Resource resource : Resource.values()) {
				capacityChanged.put(resource, storageCapacityChanged.change);
			}
			resourceStorageCapacityChanged.capacityChanges = capacityChanged;
			return resourceStorageCapacityChanged;
		});

		upcasts.put(Event.EventType.PRODUCTION_STARTED, event -> {
			ProductionStarted productionStarted = (ProductionStarted) event;
			return new ProductionStarted2(
				productionStarted.entityId, productionStarted.timestamp, productionStarted.version,
				productionStarted.productionStartTime
			);
		});

		upcasts.put(Event.EventType.PRODUCTION_LINE_ADDED, event -> {
			ProductionLineAdded productionLineAdded = (ProductionLineAdded) event;
			ProductionLineAdded2 productionLineAdded2 = new ProductionLineAdded2();
			productionLineAdded2.entityId = productionLineAdded.entityId;
			productionLineAdded2.timestamp = productionLineAdded.timestamp;
			productionLineAdded2.version = productionLineAdded.version;

			productionLineAdded2.time = productionLineAdded.time;
			productionLineAdded2.input = new HashMap<>();
			Map<Resource, Integer> output = new HashMap<>();
			output.put(productionLineAdded.resource, productionLineAdded.count);
			productionLineAdded2.output = output;
			return productionLineAdded2;
		});

		LOG.info("Registered {} upcast functions", upcasts.size());
	}

	public Event upcast(Event event) {
		Event result = event;
		while (upcasts.containsKey(result.getType())) {
			Function<Event, Event> upcast = upcasts.get(result.getType());
			LOG.info("Upcasting {}", result);
			result = upcast.apply(result);
		}
		return result;
	}


}
