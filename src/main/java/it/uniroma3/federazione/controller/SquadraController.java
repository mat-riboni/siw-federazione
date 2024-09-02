package it.uniroma3.federazione.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import it.uniroma3.federazione.model.Credentials;
import it.uniroma3.federazione.service.CredentialsService;
import it.uniroma3.federazione.service.SquadraService;

@Controller
public class SquadraController {

	@Autowired
	SquadraService squadraService;
	
	@Autowired
	CredentialsService credentialsService;

	@GetMapping("/") 
	public String index(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication instanceof AnonymousAuthenticationToken) {
			model.addAttribute("isAuthenticated", false);
			model.addAttribute("squadre", squadraService.getAll());
			return "index.html";
		}
		else {		
			UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Credentials credentials = credentialsService.getCredentialsByUsername(userDetails.getUsername());
			model.addAttribute("isAuthenticated", true);
			model.addAttribute("username", credentials.getUsername());
			if (credentials.getRole().equals(Credentials.ADMIN_ROLE)) {
				return "redirect:/admin";
			}
			if(credentials.getRole().equals(Credentials.PRESIDENTE_ROLE)) {
				return "redirect:/presidente";
			}
		}
		return "index.html";
	}

}
