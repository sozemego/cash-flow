package com.soze.world.service;

import com.soze.world.domain.City;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CityRepository extends CrudRepository<City, String> {

	Optional<City> getByName(String name);

}
