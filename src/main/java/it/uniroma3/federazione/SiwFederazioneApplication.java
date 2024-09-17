package it.uniroma3.federazione;

import org.springframework.boot.SpringApplication;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import it.uniroma3.federazione.service.CredentialsService;
import it.uniroma3.federazione.service.SquadraService;

@SpringBootApplication
public class SiwFederazioneApplication{

	@Autowired
	SquadraService squadraService;
	
	@Autowired
	CredentialsService credentialsService;
	
	public static void main(String[] args) {
		SpringApplication.run(SiwFederazioneApplication.class, args);
	}


		
		
}
