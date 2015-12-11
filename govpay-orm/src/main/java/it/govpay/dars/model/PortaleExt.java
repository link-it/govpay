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

import java.io.Serializable;
import java.util.List;

import it.govpay.bd.model.Portale;

public class PortaleExt implements Serializable{

	private static final long serialVersionUID = 1L;

	private List<ListaApplicazioniEntry> applicazioni;
	private Portale portale;
	
	public PortaleExt(){
		
	}
	
	public PortaleExt(Portale portale, List<ListaApplicazioniEntry> applicazioni){
		this.setPortale(portale);
		this.setApplicazioni(applicazioni);
	}
	
	public List<ListaApplicazioniEntry> getApplicazioni() {
		return applicazioni;
	}

	public void setApplicazioni(List<ListaApplicazioniEntry> applicazioni) {
		this.applicazioni = applicazioni;
	}

	public Portale getPortale() {
		return portale;
	}

	public void setPortale(Portale portale) {
		this.portale = portale;
	}
}
