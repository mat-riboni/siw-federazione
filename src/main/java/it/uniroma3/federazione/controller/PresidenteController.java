package it.uniroma3.federazione.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import it.uniroma3.federazione.model.Image;
import it.uniroma3.federazione.service.PresidenteService;

@Controller
public class PresidenteController {

	@Autowired
	PresidenteService presidenteService;
	
	@GetMapping("/immaginePresidente/{id}")
	public ResponseEntity<byte[]> getLogoPresidente(@PathVariable Long id){
		try {
			Image immagine = presidenteService.findPresidenteById(id).getImmagine();
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.parseMediaType(immagine.getMIMEType()));
			return new ResponseEntity<>(immagine.getBytes(), headers, HttpStatus.OK);
		} catch (NullPointerException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@GetMapping("/presidente")
	public String getPresidente(Model model) {
		
		return "redirect:/";
	}
}
