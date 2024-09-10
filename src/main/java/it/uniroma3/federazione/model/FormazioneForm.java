package it.uniroma3.federazione.model;

import java.util.List;

public class FormazioneForm {
	
	private List<Giocatore> titolari;
	
	private List<Giocatore> riserve;
	
	
	

	public List<Giocatore> getTitolari() {
		return titolari;
	}

	public void setTitolari(List<Giocatore> titolari) {
		this.titolari = titolari;
	}

	public List<Giocatore> getRiserve() {
		return riserve;
	}
	public void setRiserve(List<Giocatore> riserve) {
		this.riserve = riserve;
	}
}
