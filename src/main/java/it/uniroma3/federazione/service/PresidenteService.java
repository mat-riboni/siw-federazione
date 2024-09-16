package it.uniroma3.federazione.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.federazione.model.Presidente;
import it.uniroma3.federazione.repository.PresidenteRepository;

@Service
public class PresidenteService {
	
	@Autowired
	PresidenteRepository presidenteRepository;
	
	public Iterable<Presidente> getAll(){
		return presidenteRepository.findAll();
	}
	
	public Presidente findPresidenteById(Long id) {
		return presidenteRepository.findById(id).get();
	}

	public void save(Presidente presidente) {
		presidenteRepository.save(presidente);
		
	}
	
}
