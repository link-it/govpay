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

import it.govpay.bd.model.Applicazione;
import it.govpay.bd.model.Stazione;

public class ApplicazioneExt implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Stazione stazione;
	private List<ListaTributiEntry> tributi;
	private Applicazione applicazione;
	
	public ApplicazioneExt(){}
	
	public ApplicazioneExt(Applicazione applicazione,Stazione stazione, List<ListaTributiEntry> tributi){
		this.setApplicazione(applicazione);
		this.setStazione(stazione);
		this.setTributi(tributi);
	}

	public Stazione getStazione() {
		return stazione;
	}

	public void setStazione(Stazione stazione) {
		this.stazione = stazione;
	}

	public List<ListaTributiEntry> getTributi() {
		return tributi;
	}

	public void setTributi(List<ListaTributiEntry> tributi) {
		this.tributi = tributi;
	}

	public Applicazione getApplicazione() {
		return this.applicazione;
	}

	public void setApplicazione(Applicazione applicazione) {
		this.applicazione = applicazione;
	}
	
	

}
