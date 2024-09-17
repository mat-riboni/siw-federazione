package it.uniroma3.federazione.controller;

import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import it.uniroma3.federazione.model.Giocatore;
import it.uniroma3.federazione.model.Squadra;
import it.uniroma3.federazione.service.GiocatoreService;
import it.uniroma3.federazione.service.SquadraService;
import jakarta.validation.Valid;

@Controller
public class GiocatoreController {

	@Autowired
	GiocatoreService giocatoreService;
	
	@Autowired
	SquadraService squadraService;
	
	
	@PostMapping("/modifica_giocatore/{squadraId}")
	public String modificaGiocatore(@Valid @ModelAttribute("modificato") Giocatore modificato,
	                                BindingResult bindGiocatore,
	                                @PathVariable Long squadraId,
	                                RedirectAttributes redirectAttributes) {
	    
	    if (bindGiocatore.hasErrors()) {
	        redirectAttributes.addFlashAttribute("dettaglioErrore", buildErroriToString(bindGiocatore));
	        return "redirect:/squadra/" + squadraId;
	    }
	    
	    Squadra squadra = squadraService.findSquadraById(squadraId);
        redirectAttributes.addFlashAttribute("successo", "Operazione effettuata!");
	    giocatoreService.save(modificato);
	    modificato.setSquadra(squadra);
	    giocatoreService.save(modificato);

	    
	    return "redirect:/squadra/" + squadraId;
	}
	
	@PostMapping("/nuovo_giocatore/{squadraId}")
	public String inserisciGiocatore(@Valid @ModelAttribute("nuovo") Giocatore nuovo,
									BindingResult bindGiocatore,
									@PathVariable Long squadraId,
									RedirectAttributes redirectAttributes) {
		if (bindGiocatore.hasErrors()) {
	        redirectAttributes.addFlashAttribute("dettaglioErrore", buildErroriToString(bindGiocatore));
	        return "redirect:/squadra/" + squadraId;
	    }
		
		Squadra squadra = squadraService.findSquadraById(squadraId);
	    nuovo.setSquadra(squadra);
	    nuovo.setTitolare(false);
	    squadra.getGiocatori().add(nuovo);
	    giocatoreService.save(nuovo);
	    redirectAttributes.addFlashAttribute("successo", "Operazione effettuata!");
		
		return "redirect:/squadra/" + squadraId;
	}
	
	@GetMapping("{squadraId}/elimina/{giocatoreId}")
	public String eliminaGiocatore(@PathVariable Long giocatoreId, @PathVariable Long squadraId) {
		Squadra squadra = squadraService.findSquadraById(squadraId);
		List<Giocatore> giocatori = squadra.getGiocatori();
		Iterator<Giocatore> it = giocatori.iterator();
		while(it.hasNext()) {
			if(it.next().getId() == giocatoreId) {
				it.remove();
			}
		}
		squadra.setGiocatori(giocatori);
		squadraService.save(squadra);
		return "redirect:/squadra/" + squadraId;
	}
	
	
	
	public String buildErroriToString(BindingResult result) {
		StringBuilder strb = new StringBuilder();
		for(ObjectError e : result.getAllErrors()) {
			strb.append(e.getDefaultMessage() + ", ");
		}
		return strb.toString();
	}
	
}
