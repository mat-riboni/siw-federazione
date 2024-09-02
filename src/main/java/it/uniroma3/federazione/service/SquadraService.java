package it.uniroma3.federazione.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.federazione.model.Squadra;
import it.uniroma3.federazione.repository.SquadraRepository;

@Service
public class SquadraService{
	
	@Autowired
	private SquadraRepository squadraRepository;

	public Iterable<Squadra> getAll(){
		return squadraRepository.findAll();
	}
	
}
