package it.uniroma3.federazione.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.federazione.model.Credentials;

public interface CredentialsRepository extends CrudRepository<Credentials, Long>{

	public Optional<Credentials> findByUsername(String username);

	public boolean existsByUsername(String username);

}
