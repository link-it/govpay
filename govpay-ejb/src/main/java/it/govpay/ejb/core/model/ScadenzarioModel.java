/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2015 Link.it srl (http://www.link.it).
 * Copyright (c) 2014-2015 TAS S.p.A. (http://www.tasgroup.it).
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
package it.govpay.ejb.core.model;

/**
 * Rappresenta uno Scadenzario. 
 * Lo scadenzario e' associato ad uno o piu SIL.
 */

public class ScadenzarioModel {
	
	// chiave dello scadenzario
	private String idEnte;
	private String idSystem;

	private String idStazione;

	private String descrizione;
    private ConnettoreModel connettoreNotifica;
    private ConnettoreModel connettoreVerifica;
    
	public ConnettoreModel getConnettoreNotifica() {
		return connettoreNotifica;
	}
	
	public void setConnettoreNotifica(ConnettoreModel connettoreNotifica) {
		this.connettoreNotifica = connettoreNotifica;
	}
	
	public ConnettoreModel getConnettoreVerifica() {
		return connettoreVerifica;
	}
	
	public void setConnettoreVerifica(ConnettoreModel connettoreVerifica) {
		this.connettoreVerifica = connettoreVerifica;
	}
	
	public String getDescrizione() {
		return descrizione;
	}
	
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}
	
	public String getIdEnte() {
		return idEnte;
	}

	public void setIdEnte(String idEnte) {
		this.idEnte = idEnte;
	}

	public String getIdSystem() {
		return idSystem;
	}

	public void setIdSystem(String idSystem) {
		this.idSystem = idSystem;
	}

	public String getIdStazione() {
		return idStazione;
	}

	public void setIdStazione(String idStazione) {
		this.idStazione = idStazione;
	}
	
}
