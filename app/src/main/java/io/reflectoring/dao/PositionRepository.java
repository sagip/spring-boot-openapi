package io.reflectoring.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface PositionRepository extends CrudRepository<PositionEntity, Long> {
	
	public List<PositionEntity> findByNameContainingIgnoreCaseOrLocationContainingIgnoreCase(String keyword, String name);

}
