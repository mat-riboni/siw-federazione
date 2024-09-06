package it.uniroma3.federazione.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.federazione.model.Squadra;
import it.uniroma3.federazione.repository.SquadraRepository;

@Service
public class SquadraService{
	
	@Autowired
	private SquadraRepository squadraRepository;

	public void save(Squadra squadra) {
		squadraRepository.save(squadra);
	}
	
	public Iterable<Squadra> getAll(){
		return squadraRepository.findAll();
	}
	
	public Squadra findSquadraById(Long id) {
		return squadraRepository.findById(id).get();
	}
	
	public Iterable<Squadra> find5squadre(){
		return squadraRepository.findTop5ByOrderByIdAsc();
	}
	
	
}
