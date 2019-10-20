package com.soze.truck.repository;

import com.soze.truck.domain.TruckNavigation;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TruckNavigationCrudRepository extends CrudRepository<TruckNavigation, Integer> {

	Optional<TruckNavigation> getByTruckId(String truckId);

}
