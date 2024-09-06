package it.uniroma3.federazione.repository;


import org.springframework.data.repository.CrudRepository;

import it.uniroma3.federazione.model.Squadra;

public interface SquadraRepository extends CrudRepository<Squadra, Long> {

	public Iterable<Squadra> findTop5ByOrderByIdAsc();
	
}
