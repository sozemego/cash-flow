package com.soze.truck.repository;

import com.soze.truck.domain.Player;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PlayerRepository extends CrudRepository<Player, UUID> {

}
