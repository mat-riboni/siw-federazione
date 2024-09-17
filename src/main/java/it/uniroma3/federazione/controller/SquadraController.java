package it.uniroma3.federazione.controller;


import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import it.uniroma3.federazione.model.Costanti;
import it.uniroma3.federazione.model.Credentials;
import it.uniroma3.federazione.model.Giocatore;
import it.uniroma3.federazione.model.Image;
import it.uniroma3.federazione.model.Presidente;
import it.uniroma3.federazione.model.Squadra;
import it.uniroma3.federazione.service.CredentialsService;
import it.uniroma3.federazione.service.GiocatoreService;
import it.uniroma3.federazione.service.PresidenteService;
import it.uniroma3.federazione.service.SquadraService;
import jakarta.validation.Valid;

@Controller
public class SquadraController {

	@Autowired
	SquadraService squadraService;

	@Autowired
	CredentialsService credentialsService;

	@Autowired
	GiocatoreService giocatoreService;

	@Autowired
	PresidenteService presidenteService;

	@GetMapping("/") 
	public String index(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication instanceof AnonymousAuthenticationToken) {
			model.addAttribute("isAuthenticated", false);
			model.addAttribute("isPresidente", false);
			model.addAttribute("squadraProprietarioId", null);
			model.addAttribute("squadre", squadraService.getAll());
			return "index.html";
		}
		else {		
			UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Credentials credentials = credentialsService.getCredentialsByUsername(userDetails.getUsername());
			model.addAttribute("isAuthenticated", true);
			model.addAttribute("isPresidente", false);
			model.addAttribute("username", credentials.getUsername());
			model.addAttribute("squadraProprietarioId", null);
			model.addAttribute("squadre", squadraService.getAll());
			if (credentials.getRole().equals(Credentials.ADMIN_ROLE)) {
				return "redirect:/admin";
			}
			if(credentials.getRole().equals(Credentials.PRESIDENTE_ROLE)) {
				model.addAttribute("isAuthenticated", true);
				model.addAttribute("isPresidente", true);
				model.addAttribute("squadre", squadraService.getAll());
				model.addAttribute("squadraProprietarioId",credentials.getPresidente().getSquadra().getId());
				return "index.html";
			}
		}
		return "index.html";
	}

	@GetMapping("/squadra/{id}/immagine")
	public ResponseEntity<byte[]> getLogoSquadra(@PathVariable Long id){
		try {
			Image logo = squadraService.findSquadraById(id).getImmagine();
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.parseMediaType(logo.getMIMEType()));
			return new ResponseEntity<>(logo.getBytes(), headers, HttpStatus.OK);
		} catch (NullPointerException e){
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} 

	}


	@GetMapping("/squadra/{id}")
	public String getSquadra(Model model, @PathVariable Long id) {

		boolean isAdmin = false;
		boolean isPresidente = false;
		Squadra squadra = squadraService.findSquadraById(id);


		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if(!(authentication instanceof AnonymousAuthenticationToken)) {
			UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Credentials credentials = credentialsService.getCredentialsByUsername(userDetails.getUsername());
			if(credentials.getRole().equals(Credentials.ADMIN_ROLE)) {
				isAdmin = true;
			}
			if(squadra.getPresidente().equals(credentials.getPresidente())) {
				isPresidente = true;
			}
		}

		model.addAttribute("titolari", getTitolari(squadra.getGiocatori()));
		model.addAttribute("riserve", getRiserve(squadra.getGiocatori()));
		model.addAttribute("squadra", squadra);
		model.addAttribute("numeroGiocatori", squadra.getGiocatori().size());
		model.addAttribute("errori", false);
		model.addAttribute("isAdmin", isAdmin);
		model.addAttribute("isPresidente", isPresidente);
		model.addAttribute("modificato", new Giocatore());

		return "squadra.html";
	}

	@PostMapping("/admin/modifica_squadra/{id}")
	public String modificaSquadra(@Valid @ModelAttribute Squadra squadra,
			BindingResult bindSquadra,
			@RequestParam(value = "dataDiNascitaPres", required = false) LocalDate dataDiNascitaPres,
			@RequestParam("logo") MultipartFile logo,
			@RequestParam("immaginePres") MultipartFile immaginePres,
			@PathVariable Long id,
			Model model) {

		Squadra squad = squadraService.findSquadraById(id);

		if(bindSquadra.hasErrors()) {


			model.addAttribute("squadra", squad);
			model.addAttribute("titolari", getTitolari(squad.getGiocatori()));
			model.addAttribute("riserve", getRiserve(squad.getGiocatori()));
			model.addAttribute("numeroGiocatori", squad.getGiocatori().size());
			model.addAttribute("errori", true);
			model.addAttribute("isAdmin", true);
			model.addAttribute("isPresidente", false);
			model.addAttribute("modificato", new Giocatore());
			model.addAttribute("nuovo", new Giocatore());


			List<String> errori = new ArrayList<>();

			for(ObjectError e : bindSquadra.getAllErrors()) {
				errori.add(e.getDefaultMessage());
			}
			model.addAttribute("messaggiErrore", errori);

			return "squadra.html";
		}


		modificaSquadra(squadra, dataDiNascitaPres, logo, immaginePres, squad.getGiocatori(), squad.getPresidente(), squad);

		return "redirect:/squadra/" + id;
	}
	
	@PostMapping("/admin/nuova_squadra")
	public String inserisciNuovaSquadra(@Valid @ModelAttribute Credentials credentials,
			BindingResult bindCredentials,
			@Valid @ModelAttribute Presidente pres,
			BindingResult bindPresidente,
			@Valid @ModelAttribute Squadra squadra,
			BindingResult bindSquadra,
			@RequestParam(value = "dataDiNascitaPres", required = false) LocalDate dataDiNascitaPres,
			@RequestParam("logo") MultipartFile logo,
			@RequestParam("immaginePres") MultipartFile immaginePres,
			Model model) {

		if(bindCredentials.hasErrors() || bindPresidente.hasErrors() || bindSquadra.hasErrors()) {
			UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Credentials cred = this.credentialsService.getCredentialsByUsername(userDetails.getUsername());

			List<Squadra> squadre = (List<Squadra>)squadraService.getAll();
			List<Giocatore> giocatori = (List<Giocatore>)giocatoreService.getAll();

			model.addAttribute("squadreRidotte", squadraService.find5squadre());
			model.addAttribute("squadre", squadre);
			model.addAttribute("presidenti", presidenteService.getAll());
			model.addAttribute("username", cred.getUsername());
			model.addAttribute("numeroSquadre", squadre.size());
			model.addAttribute("quota", Costanti.QUOTA_ISCRIZIONE);
			model.addAttribute("soldiRaccolti", squadre.size()*Costanti.QUOTA_ISCRIZIONE);
			model.addAttribute("giocatori", giocatori);
			model.addAttribute("numeroGiocatori", giocatori.size());
			model.addAttribute("pres", new Presidente());
			model.addAttribute("credentials", new Credentials());
			model.addAttribute("nuova", new Squadra());
			model.addAttribute("errori", true);

			List<String> errori = new ArrayList<>();
			for(ObjectError e : bindCredentials.getAllErrors()) {
				errori.add(e.getDefaultMessage());
			}
			for(ObjectError e : bindPresidente.getAllErrors()) {
				errori.add(e.getDefaultMessage());
			}
			for(ObjectError e : bindSquadra.getAllErrors()) {
				errori.add(e.getDefaultMessage());
			}
			model.addAttribute("messaggiErrore", errori);

			return "admin.html";
		}

		buildSquadra(credentials, squadra, pres, dataDiNascitaPres, logo, immaginePres);


		return "redirect:/admin";
	}

	public void buildSquadra(Credentials credentials, Squadra squadra,
			Presidente presidente,LocalDate nascita, 
			MultipartFile logo, MultipartFile immaginePres){

		presidente.setDataNascita(nascita);
		presidente.setSquadra(squadra);
		squadra.setPresidente(presidente);
		squadra.setImmagine(buildImmagine(logo));
		presidente.setImmagine(buildImmagine(immaginePres));
		squadra.setGiocatori(new ArrayList<>());
		credentials.setPresidente(presidente);
		credentials.setRole(Credentials.PRESIDENTE_ROLE);
		squadraService.save(squadra);
		credentialsService.saveCredentials(credentials);

	}

	public void modificaSquadra(Squadra squadra, LocalDate nascita,
			MultipartFile logo, MultipartFile immaginePres,
			List<Giocatore> giocatori, Presidente presidente,
			Squadra vecchia) {
		squadra.setGiocatori(giocatori);
		squadra.getPresidente().setId(presidente.getId());

		if(nascita == null) {
			squadra.getPresidente().setDataNascita(presidente.getDataNascita());
		} else {
			squadra.getPresidente().setDataNascita(nascita);
		}


		try {
			if(logo.getBytes() != null && logo.getBytes().length > 0) {
				squadra.setImmagine(buildImmagine(logo));
			} else {
				try{
					squadra.setImmagine(vecchia.getImmagine());
				} catch (NullPointerException npe) {
					squadra.setImmagine(null);
				}
			}
		} catch (IOException e) {
			try{
				squadra.setImmagine(vecchia.getImmagine());
			} catch (NullPointerException npe) {
				squadra.setImmagine(null);
			}
		}
		
		try {
			if(immaginePres.getBytes() != null && immaginePres.getBytes().length > 0) {
				squadra.getPresidente().setImmagine(buildImmagine(immaginePres));
			} else {
				try{
					squadra.getPresidente().setImmagine(vecchia.getPresidente().getImmagine());
				} catch (NullPointerException npe) {
					squadra.getPresidente().setImmagine(null);
				}
			}
		} catch (IOException e) {
			try{
				squadra.getPresidente().setImmagine(vecchia.getImmagine());
			} catch (NullPointerException npe) {
				squadra.getPresidente().setImmagine(null);
			}
		}



		squadraService.save(squadra);
	}

	public List<Giocatore> getTitolari(List<Giocatore> giocatori){
		List<Giocatore> titolari = new ArrayList<>();
		for(Giocatore g : giocatori) {
			if(g.isTitolare()) titolari.add(g);
		}
		return titolari;
	}

	public List<Giocatore> getRiserve(List<Giocatore> giocatori){
		List<Giocatore> riserve = new ArrayList<>();
		for(Giocatore g : giocatori) {
			if(!g.isTitolare()) {
				riserve.add(g);
			}
		}
		return riserve;
	}

	public static Image buildImmagine(MultipartFile file){
		Image image = new Image();
		try {
			image.setBytes(file.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
		image.setMIMEType(file.getContentType());
		return image;
	}

}
