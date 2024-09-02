package it.uniroma3.federazione.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.federazione.model.Credentials;
import it.uniroma3.federazione.repository.CredentialsRepository;

@Service
public class CredentialsService {

	@Autowired
	CredentialsRepository credentialsRepository;

	public void saveCredentials(Credentials credentials) {
		credentialsRepository.save(credentials);
	}

	public Credentials getCredentialsByUsername(String username) {
		return credentialsRepository.findByUsername(username).get();
	}
	
}
