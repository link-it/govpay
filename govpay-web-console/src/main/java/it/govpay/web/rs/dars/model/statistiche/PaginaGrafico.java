package it.govpay.web.rs.dars.model.statistiche;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.govpay.web.rs.dars.model.ElementoCorrelato;
import it.govpay.web.rs.dars.model.InfoForm;
import it.govpay.web.rs.dars.model.input.ParamField;

public class PaginaGrafico {

	private Grafico grafico;
	private String titolo;
	private InfoForm infoEsportazione;
	private InfoForm infoRicerca;
	private Map<String, ParamField<?>> infoGrafico;
	// indica se visualizzare il filtro specifico per date
	private boolean attivaDate;
	private List<ElementoCorrelato> elementiCorrelati;
	
	public PaginaGrafico(String titolo,InfoForm infoEsportazione, InfoForm infoRicerca, Map<String, ParamField<?>> infoGrafico)  {
		this.titolo = titolo;
		this.infoRicerca = infoRicerca;
		this.infoGrafico = infoGrafico;
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

	public Map<String, ParamField<?>> getInfoGrafico() {
		return infoGrafico;
	}

	public void setInfoGrafico(Map<String, ParamField<?>> infoGrafico) {
		this.infoGrafico = infoGrafico;
	}
	
	
}
