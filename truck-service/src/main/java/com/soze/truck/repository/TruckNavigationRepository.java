package com.soze.truck.repository;

import com.soze.common.file.FileUtils;
import com.soze.common.json.JsonUtils;
import com.soze.truck.service.TruckNavigation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.*;

@Service
public class TruckNavigationRepository {

	private static final Logger LOG = LoggerFactory.getLogger(TruckNavigationRepository.class);

	private final Map<String, TruckNavigation> navigations = new HashMap<>();

	private final String filename;

	@Autowired
	public TruckNavigationRepository(@Value("${truck.navigation.repository.file}") String filename) {
		this.filename = filename;
	}

	@PostConstruct
	public void setup() throws IOException {
		LOG.info("TruckNavigationRepository init...");
		File file = getFile();
		List<TruckNavigation> navigations = JsonUtils.parseList(file, TruckNavigation.class);
		LOG.info("Found {} navigations", navigations.size());
		for (TruckNavigation navigation : navigations) {
			this.navigations.put(navigation.truckId, navigation);
		}
	}

	/**
	 * Gets {@link TruckNavigation} for a given truck.
	 * If this truck does not have TruckNavigation, creates a new one.
	 */
	public TruckNavigation getTruckNavigation(String truckId) {
		Objects.requireNonNull(truckId);
		return navigations.computeIfAbsent(truckId, TruckNavigation::new);
	}

	public void update(TruckNavigation truckNavigation) {
		navigations.put(truckNavigation.truckId, truckNavigation);
		persistNavigations();
	}

	public void deleteAll() {
		navigations.clear();
		persistNavigations();
	}

	private void persistNavigations() {
		List<TruckNavigation> navigationList = new ArrayList<>(navigations.values());
		LOG.info("Persisting {} navigations", navigationList.size());
		String json = JsonUtils.serialize(navigationList);
		try {
			FileUtils.writeToFile(getFile(), json);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private File getFile() throws IOException {
		return FileUtils.getOrEmptyListFile(filename);
	}

}
