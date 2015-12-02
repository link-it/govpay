/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2015 Link.it srl (http://www.link.it).
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

package it.govpay.bd.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Rapppresenta un ente creditore
 */
public class Dominio extends BasicModel {
	private static final long serialVersionUID = 1L;
	
	private Long id; 
	private long idStazione; 
	private String codDominio;
	private String ragioneSociale;
	private String gln;
	private String pluginClass;
	private boolean abilitato;
	private List<Disponibilita> disponibilita = new ArrayList<Disponibilita>();
	
	public Dominio (){}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCodDominio() {
		return codDominio;
	}

	public void setCodDominio(String codDominio) {
		this.codDominio = codDominio;
	}

	public String getGln() {
		return gln;
	}

	public void setGln(String gln) {
		this.gln = gln;
	}

	
	@Override
	public boolean equals(Object obj) {
		Dominio dominio = null;
		if(obj instanceof Dominio) {
			dominio = (Dominio) obj;
		} else {
			return false;
		}
		
		boolean equal =
				equals(codDominio, dominio.getCodDominio()) &&
				equals(gln, dominio.getGln()) &&
				equals(ragioneSociale, dominio.getRagioneSociale()) &&
				equals(disponibilita, dominio.getDisponibilita()) &&
				equals(pluginClass, dominio.getPluginClass()) &&
				equals(abilitato, dominio.isAbilitato()) &&
				idStazione == dominio.getIdStazione();
		
		return equal;
	}

	public String getRagioneSociale() {
		return ragioneSociale;
	}

	public void setRagioneSociale(String ragioneSociale) {
		this.ragioneSociale = ragioneSociale;
	}

	public List<Disponibilita> getDisponibilita() {
		return disponibilita;
	}

	public void setDisponibilita(List<Disponibilita> disponibilita) {
		this.disponibilita = disponibilita;
	}

	public String getPluginClass() {
		return pluginClass;
	}

	public void setPluginClass(String pluginClass) {
		this.pluginClass = pluginClass;
	}

	public boolean isAbilitato() {
		return abilitato;
	}

	public void setAbilitato(boolean abilitato) {
		this.abilitato = abilitato;
	}

	public long getIdStazione() {
		return idStazione;
	}

	public void setIdStazione(long idStazione) {
		this.idStazione = idStazione;
	}


}

