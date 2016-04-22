/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2016 Link.it srl (http://www.link.it).
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
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
	private long totaleRisultati;
	private String titolo;
	private URI esportazione;
	private URI cancellazione;
	private InfoForm infoCreazione;
	private InfoForm infoRicerca;
	private boolean filtro;
	
	public Elenco(String titolo, InfoForm infoRicerca, InfoForm infoCreazione, long totaleRisultati, URI esportazione, URI cancellazione) {
		this.totaleRisultati = totaleRisultati;
		this.titolo = titolo;
		this.infoRicerca = infoRicerca;
		this.infoCreazione = infoCreazione;
		this.cancellazione = cancellazione;
		this.esportazione = esportazione;
		this.elenco = (new ArrayList<Elemento>());
		this.setFiltro(false);
	}
	
	public InfoForm getInfoRicerca() {
		return infoRicerca;
	}

	public List<Elemento> getElenco() {
		return elenco;
	}

	public long getTotaleRisultati() {
		return totaleRisultati;
	}
	
	public String getTitolo() {
		return titolo;
	}
	
	public URI getEsportazione() {
		return esportazione;
	}
	
	public URI getCancellazione() {
		return cancellazione;
	}

	public InfoForm getInfoCreazione() {
		return infoCreazione;
	}

	public boolean isFiltro() {
		return filtro;
	}

	public void setFiltro(boolean filtro) {
		this.filtro = filtro;
	}

}
