package com.soze.truck.repository;

import com.soze.truck.domain.Truck;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class TruckRepository {

	private static final Logger LOG = LoggerFactory.getLogger(TruckRepository.class);

	private final TruckCrudRepository crudRepository;

	@Autowired
	public TruckRepository(TruckCrudRepository crudRepository) {
		this.crudRepository = crudRepository;
	}

	public void addTruck(Truck truck) {
		LOG.info("Adding truck {}", truck);
		crudRepository.save(truck);
	}

	public List<Truck> getTrucks() {
		Iterable<Truck> iterable = crudRepository.findAll();
		List<Truck> trucks = new ArrayList<>();
		iterable.forEach(trucks::add);
		return trucks;
	}

	public Optional<Truck> findTruckById(UUID id) {
		Objects.requireNonNull(id);
		return crudRepository.findById(id);
	}

	public void update(Truck truck) {
		LOG.info("Updating truck {}", truck);
		Objects.requireNonNull(truck);
		crudRepository.save(truck);
	}

	public void deleteAll() {
		crudRepository.deleteAll();
	}

	public List<Truck> findByPlayerId(UUID playerId) {
		return crudRepository.findByPlayerId(playerId);
	}

}
