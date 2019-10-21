package com.soze.truck.repository;

import com.soze.truck.domain.TruckNavigation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Repository
@Transactional
public class TruckNavigationRepository {

	private static final Logger LOG = LoggerFactory.getLogger(TruckNavigationRepository.class);
	private final TruckNavigationCrudRepository crudRepository;

	@PersistenceContext
	private EntityManager em;

	@Autowired
	public TruckNavigationRepository(TruckNavigationCrudRepository crudRepository) {
		this.crudRepository = crudRepository;
	}

	/**
	 * Gets {@link TruckNavigation} for a given truck.
	 * If this truck does not have TruckNavigation, creates a new one.
	 */
	public TruckNavigation getTruckNavigation(UUID truckId) {
		Objects.requireNonNull(truckId);
		Optional<TruckNavigation> navigationOptional = crudRepository.getByTruckId(truckId);
		if (navigationOptional.isPresent()) {
			return navigationOptional.get();
		}
		TruckNavigation truckNavigation = new TruckNavigation(truckId);
		return crudRepository.save(truckNavigation);
	}

	public void update(TruckNavigation truckNavigation) {
		crudRepository.save(truckNavigation);
	}

	public void deleteAll() {
		crudRepository.deleteAll();
	}

}
