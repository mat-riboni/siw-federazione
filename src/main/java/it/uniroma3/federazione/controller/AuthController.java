package it.uniroma3.federazione.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import it.uniroma3.federazione.model.Costanti;
import it.uniroma3.federazione.model.Credentials;
import it.uniroma3.federazione.model.Giocatore;
import it.uniroma3.federazione.model.Presidente;
import it.uniroma3.federazione.model.Squadra;
import it.uniroma3.federazione.service.CredentialsService;
import it.uniroma3.federazione.service.GiocatoreService;
import it.uniroma3.federazione.service.PresidenteService;
import it.uniroma3.federazione.service.SquadraService;
import jakarta.validation.Valid;

@Controller
public class AuthController {


	@Autowired 
	CredentialsService credentialsService;

	@Autowired
	SquadraService squadraService;
	
	@Autowired
	PresidenteService presidenteService; 
	
	@Autowired
	GiocatoreService giocatoreService;

	@Autowired
	PasswordEncoder encoder;

	@GetMapping("/register")
	public String getRegisterTemplate(Model model) {
		model.addAttribute("presidente", new Presidente());
		model.addAttribute("credentials", new Credentials());
		return "registrationForm.html";
	}

	@PostMapping("/register")
	public String registerUtente(
			@Valid @ModelAttribute("presidente") Presidente presidente, 
			BindingResult bindingResultPresidente,
			@Valid @ModelAttribute("credentials") Credentials credentials,
			BindingResult bindingResultCredentials,
			Model model) {

		if(bindingResultPresidente.hasErrors() || bindingResultCredentials.hasErrors()) {
			return "registrationForm.html";
		}


		credentials.setRole(Credentials.PRESIDENTE_ROLE);
		credentials.setPresidente(presidente);
		credentials.setPassword(encoder.encode(credentials.getPassword()));
		credentialsService.saveCredentials(credentials);
		return "redirect:/login";
	}


	@GetMapping("/login")
	public String getLoginForm() {
		return "login.html";
	}

	@GetMapping("/success")
	public String getHome() {
		UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Credentials credentials = this.credentialsService.getCredentialsByUsername(userDetails.getUsername());

		if(credentials.getRole().equals(Credentials.PRESIDENTE_ROLE)) {
			return "redirect:/presidente";
		} else {
			return "redirect:/admin";
		}
	}
	
	@GetMapping("/admin")
	public String getAdminHome(Model model) {
		
		UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Credentials credentials = this.credentialsService.getCredentialsByUsername(userDetails.getUsername());
		
		List<Squadra> squadre = (List<Squadra>)squadraService.getAll();
		List<Giocatore> giocatori = (List<Giocatore>)giocatoreService.getAll();
		
		model.addAttribute("squadreRidotte", squadraService.find5squadre());
		model.addAttribute("squadre", squadre);
		model.addAttribute("presidenti", presidenteService.getAll());
		model.addAttribute("username", credentials.getUsername());
		model.addAttribute("numeroSquadre", squadre.size());
		model.addAttribute("quota", Costanti.QUOTA_ISCRIZIONE);
		model.addAttribute("soldiRaccolti", squadre.size()*Costanti.QUOTA_ISCRIZIONE);
		model.addAttribute("giocatori", giocatori);
		model.addAttribute("numeroGiocatori", giocatori.size());
		model.addAttribute("pres", new Presidente());
		model.addAttribute("credentials", new Credentials());
		model.addAttribute("nuova", new Squadra());
		model.addAttribute("errori", false);
	
		return "admin.html";
	}
	
	@PostMapping("/search")
	public String registerAdmin() {
		
		if(!credentialsService.existByUsername("admin")) {
			Credentials admin = new Credentials();
			admin.setUsername("admin");
			admin.setPassword("admin");
			admin.setRole(Credentials.ADMIN_ROLE);
			credentialsService.saveCredentials(admin);
		}
		
		return "redirect:/";
	}
}
