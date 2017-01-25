/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2017 Link.it srl (http://www.link.it).
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 3, as published by
 * the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package it.govpay.web.rs.dars.model;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class Elenco {
	
	private List<Elemento> elenco;
	private Elemento intestazione;
	private boolean tabella;
	private long totaleRisultati;
	private String titolo;
	private URI esportazione;
	private URI cancellazione;
	private InfoForm infoCreazione;
	private InfoForm infoRicerca;
	private boolean filtro;
	
	public Elenco(String titolo, InfoForm infoRicerca, InfoForm infoCreazione, long totaleRisultati, URI esportazione, URI cancellazione) {
		this(titolo, infoRicerca, infoCreazione, totaleRisultati, esportazione, cancellazione, false, null);
	}
	
	public Elenco(String titolo, InfoForm infoRicerca, InfoForm infoCreazione, long totaleRisultati, URI esportazione, URI cancellazione,boolean tabella, Elemento intestazione) {
		this.totaleRisultati = totaleRisultati;
		this.titolo = titolo;
		this.infoRicerca = infoRicerca;
		this.infoCreazione = infoCreazione;
		this.cancellazione = cancellazione;
		this.esportazione = esportazione;
		this.elenco = (new ArrayList<Elemento>());
		this.intestazione = intestazione;
		this.tabella = tabella;
		this.setFiltro(false);
	}
	
	public InfoForm getInfoRicerca() {
		return this.infoRicerca;
	}

	public List<Elemento> getElenco() {
		return this.elenco;
	}

	public long getTotaleRisultati() {
		return this.totaleRisultati;
	}
	
	public String getTitolo() {
		return this.titolo;
	}
	
	public URI getEsportazione() {
		return this.esportazione;
	}
	
	public URI getCancellazione() {
		return this.cancellazione;
	}

	public InfoForm getInfoCreazione() {
		return this.infoCreazione;
	}

	public boolean isFiltro() {
		return this.filtro;
	}

	public void setFiltro(boolean filtro) {
		this.filtro = filtro;
	}

	public Elemento getIntestazione() {
		return intestazione;
	}

	public boolean isTabella() {
		return tabella;
	}
	
	

}
