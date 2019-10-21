package com.soze.truck.repository;

import com.soze.truck.domain.Truck;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TruckCrudRepository extends CrudRepository<Truck, UUID> {

}
