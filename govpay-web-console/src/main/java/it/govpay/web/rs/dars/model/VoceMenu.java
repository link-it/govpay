package it.govpay.web.rs.dars.model;

import java.net.URI;

public class VoceMenu {
	
	public static final String VOCE_STATISTICA = "statistica";
	public static final String VOCE_MONITORAGGIO = "monitoraggio";
	public static final String VOCE_ANAGRAFICA = "anagrafica";
	public static final String VOCE_MANUTENZIONE = "manutenzione";
	public static final String VOCE_OPERAZIONIMASSIVE = "operazioniMassive";
	
	private String label;
	private URI uri;
	private boolean avanzato;
	private String tipo;
	
	public VoceMenu(String label, URI uri) {
		this(label, uri, VOCE_MONITORAGGIO, false);
	}

	public VoceMenu(String label, URI uri, boolean avanzato) {
		this(label, uri, VOCE_MONITORAGGIO, avanzato);
	}
	
	public VoceMenu(String label, URI uri, String tipo) {
		this(label, uri,tipo,false); 
	}
	
	public VoceMenu(String label, URI uri, String tipo, boolean avanzato) {
		this.label = label;
		this.uri = uri;
		this.avanzato = avanzato;
		this.tipo= tipo;
	}
	public String getLabel() {
		return this.label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public URI getUri() {
		return this.uri;
	}
	public void setUri(URI uri) {
		this.uri = uri;
	}
	public boolean isAvanzato() {
		return this.avanzato;
	}
	public void setAvanzato(boolean avanzato) {
		this.avanzato = avanzato;
	}

	public String getTipo() {
		return this.tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
}