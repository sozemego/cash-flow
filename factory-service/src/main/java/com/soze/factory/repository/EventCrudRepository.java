package com.soze.factory.repository;

import com.soze.factory.store.EventEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EventCrudRepository extends CrudRepository<EventEntity, UUID> {

	@Query(value = "SELECT * FROM factory.factory_event WHERE event ->> 'entityId' = ?1", nativeQuery = true)
	List<EventEntity> getByEntityId(String entityId);

}
