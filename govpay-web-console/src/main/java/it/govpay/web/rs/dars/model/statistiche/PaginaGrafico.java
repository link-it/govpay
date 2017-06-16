package it.govpay.web.rs.dars.model.statistiche;

import java.net.URI;

import it.govpay.web.rs.dars.model.InfoForm;

public class PaginaGrafico {

	private Grafico grafico;
	private String titolo;
	private URI esportazione;
	private InfoForm infoRicerca;
	// indica se visualizzare il filtro specifico per date
	private boolean attivaDate;
	
	public PaginaGrafico(String titolo,URI esportazione, InfoForm infoRicerca)  {
		this.titolo = titolo;
		this.infoRicerca = infoRicerca;
		this.esportazione = esportazione;
		this.attivaDate = true;
	}

	public Grafico getGrafico() {
		return grafico;
	}

	public void setGrafico(Grafico grafico) {
		this.grafico = grafico;
	}

	public String getTitolo() {
		return titolo;
	}

	public void setTitolo(String titolo) {
		this.titolo = titolo;
	}

	public URI getEsportazione() {
		return esportazione;
	}

	public void setEsportazione(URI esportazione) {
		this.esportazione = esportazione;
	}

	public InfoForm getInfoRicerca() {
		return infoRicerca;
	}

	public void setInfoRicerca(InfoForm infoRicerca) {
		this.infoRicerca = infoRicerca;
	}

	public boolean isAttivaDate() {
		return attivaDate;
	}

	public void setAttivaDate(boolean attivaDate) {
		this.attivaDate = attivaDate;
	}
	
}
