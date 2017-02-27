package it.govpay.web.rs.dars.model;

import java.net.URI;

public class About {
	private String titolo;
	private String versione;
	private String build;
	private URI projectPage;
	private URI manualeUso;
	private String copyright;
	private URI licenza;
	
	public URI getProjectPage() {
		return this.projectPage;
	}
	public void setProjectPage(URI projectPage) {
		this.projectPage = projectPage;
	}
	public URI getManualeUso() {
		return this.manualeUso;
	}
	public void setManualeUso(URI manualeUso) {
		this.manualeUso = manualeUso;
	}
	public String getVersione() {
		return this.versione;
	}
	public void setVersione(String versione) {
		this.versione = versione;
	}
	public String getBuild() {
		return this.build;
	}
	public void setBuild(String build) {
		this.build = build;
	}
	public URI getLicenza() {
		return this.licenza;
	}
	public void setLicenza(URI licenza) {
		this.licenza = licenza;
	}
	public String getTitolo() {
		return this.titolo;
	}
	public void setTitolo(String titolo) {
		this.titolo = titolo;
	}
	public String getCopyright() {
		return this.copyright;
	}
	public void setCopyright(String copyright) {
		this.copyright = copyright;
	}
}