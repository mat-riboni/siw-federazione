package it.uniroma3.federazione.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import it.uniroma3.federazione.model.Giocatore;
import it.uniroma3.federazione.model.GiocatoreFormazione;
import it.uniroma3.federazione.model.Squadra;
import it.uniroma3.federazione.service.SquadraService;

@RestController
public class RestfulController {

	@Autowired
	SquadraService squadraService;
	
	@PostMapping("/presidente/squadra/{id}/salva_formazione")
	public ResponseEntity<?> salvaFormazione(@PathVariable Long id,
											 @RequestBody List<GiocatoreFormazione> formazione){
		Squadra squadra = squadraService.findSquadraById(id);
		
		try {
            for (GiocatoreFormazione giocatore : formazione) {
                Long idGiocatore = giocatore.getId();
                boolean isTitolare = giocatore.isTitolare();
                for(Giocatore g : squadra.getGiocatori()) {
                	if(idGiocatore == g.getId()) {
                		g.setTitolare(isTitolare);
                	}
                }
                
            }
            squadraService.save(squadra);

            return ResponseEntity.ok().body("Formazione salvata con successo");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Errore nel salvataggio della formazione");
        }
	}
	
}
