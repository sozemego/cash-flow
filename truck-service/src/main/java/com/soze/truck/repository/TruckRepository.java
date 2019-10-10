package com.soze.truck.repository;

import com.soze.common.json.JsonUtils;
import com.soze.truck.domain.Truck;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TruckRepository {

	private static final Logger LOG = LoggerFactory.getLogger(TruckRepository.class);

	private final String fileName;
	private final Map<String, Truck> trucks = new ConcurrentHashMap<>();

	@Autowired
	public TruckRepository(@Value("${truck.repository.file}") String fileName) {
		this.fileName = fileName;
	}

	@PostConstruct
	public void setup() {
		LOG.info("Truck repository init... loading trucks from {}", fileName);
		File file = getFile();
		List<Truck> trucks = JsonUtils.parseList(file, Truck.class);
		LOG.info("Loaded {} trucks from {}", trucks.size(), file);
		for (Truck truck : trucks) {
			this.trucks.put(truck.getId(), truck);
		}
		LOG.info("Truck repository initialized");
	}


	public void addTruck(Truck truck) {
		LOG.info("Adding truck {}", truck);
		trucks.put(truck.getId(), truck);
		persistTrucks();
	}

	public List<Truck> getTrucks() {
		return new ArrayList<>(trucks.values());
	}

	public Optional<Truck> findTruckById(String id) {
		Objects.requireNonNull(id);
		return Optional.ofNullable(trucks.get(id));
	}

	public void update(Truck truck) {
		LOG.info("Updating truck {}", truck);
		Objects.requireNonNull(truck);
		trucks.put(truck.getId(), truck);
		persistTrucks();
	}

	public void deleteAll() {
		trucks.clear();
		persistTrucks();
	}

	private void persistTrucks() {
		List<Truck> trucks = getTrucks();
		LOG.info("Persisting {} trucks", trucks.size());
		String payload = JsonUtils.serialize(trucks);
		try {
			FileUtils.writeStringToFile(getFile(), payload, Charset.defaultCharset());
		} catch (Exception e) {
			LOG.warn("", e);
		}
		LOG.info("Persisted trucks");
	}

	private File getFile() {
		File file = FileUtils.getFile(fileName);
		try {
			FileUtils.touch(file);
			long size = FileUtils.sizeOf(file);
			if (size == 0) {
				FileUtils.writeStringToFile(file, "[]", Charset.defaultCharset());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return file;
	}

}
