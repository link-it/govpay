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

import java.util.ArrayList;
import java.util.List;

import it.govpay.web.utils.ConsoleProperties;

public class Elenco {
	
	private List<Elemento> elenco;
	private long totaleRisultati;
	private String titolo;
	private InfoForm infoCancellazione;
	private InfoForm infoEsportazione;
	private InfoForm infoCreazione;
	private InfoForm infoRicerca;
	private boolean filtro;
	private int numeroMassimoElementiExport;
	private String simpleSearchPlaceHolder;
	
	public Elenco(String titolo, InfoForm infoRicerca, InfoForm infoCreazione, long totaleRisultati, InfoForm esportazione, InfoForm infoCancellazione) {
		this(titolo, infoRicerca, infoCreazione, totaleRisultati, esportazione, infoCancellazione, null);
	}
	
	public Elenco(String titolo, InfoForm infoRicerca, InfoForm infoCreazione, long totaleRisultati, InfoForm infoEsportazione, InfoForm infoCancellazione,String simpleSearchPlaceHolder) {
		this.totaleRisultati = totaleRisultati;
		this.titolo = titolo;
		this.infoRicerca = infoRicerca;
		this.infoCreazione = infoCreazione;
		this.infoCancellazione = infoCancellazione;
		this.infoEsportazione = infoEsportazione;
		this.elenco = (new ArrayList<Elemento>());
		this.setFiltro(false);
		this.numeroMassimoElementiExport = ConsoleProperties.getInstance().getNumeroMassimoElementiExport();
		this.simpleSearchPlaceHolder = simpleSearchPlaceHolder;
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
	
	public InfoForm getInfoEsportazione() {
		return this.infoEsportazione;
	}

	public InfoForm getInfoCancellazione() {
		return this.infoCancellazione;
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

	public int getNumeroMassimoElementiExport() {
		return this.numeroMassimoElementiExport;
	}

	public void setNumeroMassimoElementiExport(int numeroMassimoElementiExport) {
		this.numeroMassimoElementiExport = numeroMassimoElementiExport;
	}

	public String getSimpleSearchPlaceHolder() {
		return simpleSearchPlaceHolder;
	}

	public void setSimpleSearchPlaceHolder(String simpleSearchPlaceHolder) {
		this.simpleSearchPlaceHolder = simpleSearchPlaceHolder;
	}
	
}
