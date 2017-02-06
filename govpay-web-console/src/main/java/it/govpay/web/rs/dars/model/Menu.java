package it.govpay.web.rs.dars.model;

import java.util.ArrayList;
import java.util.List;

public class Menu {
	private String titolo;
	private VoceMenu home;
	private List<SezioneMenu> sezioni;
	
	public Menu(String titolo) {
		this.titolo = titolo;
		this.sezioni = new ArrayList<SezioneMenu>();
	}
	
	public String getTitolo() {
		return this.titolo;
	}
	public void setTitolo(String titolo) {
		this.titolo = titolo;
	}
	public List<SezioneMenu> getSezioni() {
		return this.sezioni;
	}
	public void setSezioni(List<SezioneMenu> sezioni) {
		this.sezioni = sezioni;
	}

	public VoceMenu getHome() {
		return this.home;
	}

	public void setHome(VoceMenu home) {
		this.home = home;
	} 
}
