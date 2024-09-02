package it.uniroma3.federazione.controller;


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

import it.uniroma3.federazione.model.Credentials;
import it.uniroma3.federazione.model.Presidente;
import it.uniroma3.federazione.service.CredentialsService;
import jakarta.validation.Valid;

@Controller
public class AuthController {


	@Autowired 
	private CredentialsService credentialsService;


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
}
