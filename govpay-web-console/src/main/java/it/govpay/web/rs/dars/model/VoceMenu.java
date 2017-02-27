package it.govpay.web.rs.dars.model;

import java.net.URI;

public class VoceMenu {
	private String label;
	private URI uri;
	private boolean avanzato;
	
	public VoceMenu(String label, URI uri, boolean avanzato) {
		this.label = label;
		this.uri = uri;
		this.avanzato = avanzato;
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
}