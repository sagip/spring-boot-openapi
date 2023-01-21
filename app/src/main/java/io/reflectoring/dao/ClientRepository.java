package io.reflectoring.dao;


import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

public interface ClientRepository extends CrudRepository<ClientEntity, Long> { 
	
	ClientEntity findByUuid(UUID uuid);
	ClientEntity findByEmail(String email);
}
