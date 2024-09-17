package it.uniroma3.federazione.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.federazione.model.Giocatore;
import it.uniroma3.federazione.repository.GiocatoreRepository;

@Service
public class GiocatoreService {
	
	@Autowired
	GiocatoreRepository giocatoreRepository;
	
	public Iterable<Giocatore> getAll(){
		return giocatoreRepository.findAll();
	}
	
	public void save(Giocatore giocatore) {
		giocatoreRepository.save(giocatore);
	}

	public void elimina(Long giocatoreId) {
		giocatoreRepository.deleteById(giocatoreId);
	}

}
