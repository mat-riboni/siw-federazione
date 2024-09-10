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
public class SiwFederazioneApplication implements CommandLineRunner {

	@Autowired
	SquadraService squadraService;
	
	@Autowired
	CredentialsService credentialsService;
	
	public static void main(String[] args) {
		SpringApplication.run(SiwFederazioneApplication.class, args);
	}
	
	@Override
	public void run(String... args) throws Exception {
		for(int i = 0; i < 10; i++) {
			Squadra squadra = new Squadra();
			squadra.setName("Squadra "+i);
			squadra.setAnno(2000+i);
			squadra.setSede("Roma");
			List<Giocatore> giocatori = new ArrayList<>();
			for(int j = 0; j < 9; j++) {
				Giocatore giocatore = new Giocatore();
				giocatore.setNome("Sergio");
				giocatore.setCognome(Integer.toString(j));
				giocatore.setDataNascita(LocalDate.now());
				giocatore.setLuogoNascita("Roma");
				giocatore.setNumeroMaglia(j);
				giocatore.setRuolo("Ala piccola");
				giocatore.setSquadra(squadra);
				if(j <= 4) {
					giocatore.setTitolare(true);
				} else {
					giocatore.setTitolare(false);
				}
				giocatori.add(giocatore);
			}
			squadra.setGiocatori(giocatori);
			Presidente presidente = new Presidente();
			presidente.setNome("Sergio");
			presidente.setCognome("Allevi");
			presidente.setCodiceFiscale("LLVSRG72E19H501W");
			presidente.setDataNascita(LocalDate.of(1970, 4, 12));
			presidente.setLuogoNascita("Genova");
			presidente.setSquadra(squadra);
			squadra.setPresidente(presidente);
			Credentials cred = new Credentials();
			cred.setUsername("pres" + i);
			cred.setPassword(Integer.toString(i));
			cred.setPresidente(presidente);
			cred.setRole(Credentials.PRESIDENTE_ROLE);
			squadraService.save(squadra);
			credentialsService.saveCredentials(cred);

		}
		Credentials admin = new Credentials();
		admin.setUsername("adminProva");
		admin.setPassword("12");
		admin.setRole(Credentials.ADMIN_ROLE);
		credentialsService.saveCredentials(admin);
	}
	
	

}
