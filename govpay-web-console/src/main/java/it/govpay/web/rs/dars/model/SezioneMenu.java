package it.govpay.web.rs.dars.model;

import java.util.ArrayList;
import java.util.List;

public class SezioneMenu {
	private String titolo;
	private List<VoceMenu> vociMenu;
	
	public SezioneMenu(String titolo) {
		this.titolo = titolo;
		this.vociMenu = new ArrayList<VoceMenu>();
	}
	
	public String getTitolo() {
		return this.titolo;
	}
	public void setTitolo(String titolo) {
		this.titolo = titolo;
	}
	public List<VoceMenu> getVociMenu() {
		return this.vociMenu;
	}
	public void setVociMenu(List<VoceMenu> vociMenu) {
		this.vociMenu = vociMenu;
	}
}