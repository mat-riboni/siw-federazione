package it.uniroma3.federazione;

import org.springframework.boot.SpringApplication;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import it.uniroma3.federazione.model.Credentials;
import it.uniroma3.federazione.model.Giocatore;
import it.uniroma3.federazione.model.Presidente;
import it.uniroma3.federazione.model.Squadra;
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
