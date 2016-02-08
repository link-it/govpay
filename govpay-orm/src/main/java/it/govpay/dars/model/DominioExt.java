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
package it.govpay.dars.model;

import java.util.ArrayList;
import java.util.List;

import it.govpay.bd.model.ContoAccredito;
import it.govpay.bd.model.Disponibilita;
import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.IbanAccredito;
import it.govpay.bd.model.Stazione;
import it.govpay.bd.model.TabellaControparti;

public class DominioExt extends Dominio{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Stazione stazione = null;
	private List<Disponibilita> indisponibilita = new ArrayList<Disponibilita>();
	private List<IbanAccredito> ibanAccredito = new ArrayList<IbanAccredito>();
	private List<TabellaControparti> controparti = new ArrayList<TabellaControparti>();
	private List<ContoAccredito> contiAccredito = new ArrayList<ContoAccredito>();

	public DominioExt(){}
	
	public Stazione getStazione() {
		return stazione;
	}

	public void setStazione(Stazione stazione) {
		this.stazione = stazione;
	}

	public List<Disponibilita> getIndisponibilita() {
		return indisponibilita;
	}

	public void setIndisponibilita(List<Disponibilita> indisponibilita) {
		this.indisponibilita = indisponibilita;
	}

	public List<IbanAccredito> getIbanAccredito() {
		return ibanAccredito;
	}

	public void setIbanAccredito(List<IbanAccredito> ibanAccredito) {
		this.ibanAccredito = ibanAccredito;
	}

	public List<TabellaControparti> getControparti() {
		return controparti;
	}

	public void setControparti(List<TabellaControparti> controparti) {
		this.controparti = controparti;
	}

	public List<ContoAccredito> getContiAccredito() {
		return contiAccredito;
	}

	public void setContiAccredito(List<ContoAccredito> contiAccredito) {
		this.contiAccredito = contiAccredito;
	}

}
