package it.uniroma3.federazione.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import it.uniroma3.federazione.model.Credentials;
import it.uniroma3.federazione.repository.CredentialsRepository;

@Service
public class CredentialsService {

	@Autowired
	CredentialsRepository credentialsRepository;
	
	@Autowired
	PasswordEncoder encoder;

	public void saveCredentials(Credentials credentials) {
		credentials.setPassword(encoder.encode(credentials.getPassword()));
		credentialsRepository.save(credentials);
	}

	public Credentials getCredentialsByUsername(String username) {
		return credentialsRepository.findByUsername(username).get();
	}
	
    public boolean existByUsername(String username) {
        return credentialsRepository.existsByUsername(username);
    }
	

	
}
