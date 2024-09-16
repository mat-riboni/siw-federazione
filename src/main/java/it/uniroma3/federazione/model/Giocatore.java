package it.uniroma3.federazione.model;

import java.time.LocalDate;
import java.util.Objects;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

@Entity
public class Giocatore {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank(message = "Il ruolo è obbligatorio")
	private String ruolo;
	
	@NotBlank(message = "Il nome è obbligatorio")
	private String nome;
	
	@NotBlank(message = "Il cognome è obbligatorio")
	private String cognome;
	
	@NotBlank(message = "Il luogo di nascita è obbligatorio")
	private String luogoNascita;
	
	private LocalDate dataNascita;
	
	@Max(value = 99, message = "Il numero non può essere maggiore di 99")
	@Min(value = 0, message = "Il numero non può essere minore di zero")
	private int numeroMaglia;
	
	private boolean titolare;
	
	@ManyToOne(cascade = CascadeType.REFRESH)
	@JoinColumn(name = "squadra_id")
	private Squadra squadra;
	
	public int getNumeroMaglia() {
		return numeroMaglia;
	}

	public void setNumeroMaglia(int numeroMaglia) {
		this.numeroMaglia = numeroMaglia;
	}

	public boolean isTitolare() {
		return titolare;
	}

	public void setTitolare(boolean titolare) {
		this.titolare = titolare;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRuolo() {
		return ruolo;
	}

	public void setRuolo(String ruolo) {
		this.ruolo = ruolo;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCognome() {
		return cognome;
	}

	public void setCognome(String congome) {
		this.cognome = congome;
	}

	public String getLuogoNascita() {
		return luogoNascita;
	}

	public void setLuogoNascita(String luogoNascita) {
		this.luogoNascita = luogoNascita;
	}

	public LocalDate getDataNascita() {
		return dataNascita;
	}

	public void setDataNascita(LocalDate dataNascita) {
		this.dataNascita = dataNascita;
	}

	public Squadra getSquadra() {
		return squadra;
	}

	public void setSquadra(Squadra squadra) {
		this.squadra = squadra;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Giocatore other = (Giocatore) obj;
		return Objects.equals(id, other.id);
	}
	
	
	
	
	
}
