package it.govpay.web.rs.dars.model.statistiche;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import it.govpay.web.rs.dars.model.ElementoCorrelato;
import it.govpay.web.rs.dars.model.InfoForm;

public class PaginaGrafico {

	private Grafico grafico;
	private String titolo;
	private InfoForm infoEsportazione;
	private InfoForm infoRicerca;
	// indica se visualizzare il filtro specifico per date
	private boolean attivaDate;
	private List<ElementoCorrelato> elementiCorrelati;
	
	public PaginaGrafico(String titolo,InfoForm infoEsportazione, InfoForm infoRicerca)  {
		this.titolo = titolo;
		this.infoRicerca = infoRicerca;
		this.infoEsportazione = infoEsportazione;
		this.attivaDate = true;
		this.elementiCorrelati = new ArrayList<ElementoCorrelato>();
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

	public InfoForm getInfoEsportazione() {
		return infoEsportazione;
	}

	public void setInfoEsportazione(InfoForm infoEsportazione) {
		this.infoEsportazione = infoEsportazione;
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
	
	public ElementoCorrelato addElementoCorrelato(String etichetta, URI uri) {
		ElementoCorrelato elemento = new ElementoCorrelato(etichetta, uri);
		this.elementiCorrelati.add(elemento);
		return elemento;
	}
	
	public List<ElementoCorrelato> getElementiCorrelati() {
		return this.elementiCorrelati;
	}
}
