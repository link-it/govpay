package it.govpay.web.rs.dars.model;

import java.util.Map;

public class Lingua {
	private String lingua;
	private Map<String, String> etichette ;
	
	public Lingua(String lingua, Map<String, String> etichette){
		this.lingua = lingua;
		this.etichette = etichette;
	}
	public String getLingua() {
		return lingua;
	}
	public void setLingua(String lingua) {
		this.lingua = lingua;
	}
	public Map<String, String> getEtichette() {
		return etichette;
	}
	public void setEtichette(Map<String, String> etichette) {
		this.etichette = etichette;
	}
}
