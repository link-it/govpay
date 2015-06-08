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

public class SoggettoModel   {
	
	/**
	 * CodiceFiscale o PartitaIva
	 */
    protected String idFiscale; 
    
    /**
     * Nome e Cognome  o Ragione sociale
     */
    protected String anagrafica; //
    
    /**
     * Indirizzo
     */
    protected String indirizzo;
    
    /**
     * Numero civico
     */    
    protected String civico;
    
    /**
     * Codice avviamento postale
     */
    protected String cap;
    
    /**
     * Comune, località 
     */
    protected String localita;
    
    /**
     * Provincia (e.g. MI)
     */    
    protected String provincia;
    
    /**
     * Nazione (e.g. IT)
     */
    protected String nazione;
    
    /**
     * EMail
     */
    protected String eMail;
    
	public String getIdFiscale() {
		return idFiscale;
	}
	public void setIdFiscale(String identificativoUnivoco) {
		this.idFiscale = identificativoUnivoco;
	}
	public String getAnagrafica() {
		return anagrafica;
	}
	public void setAnagrafica(String anagrafica) {
		this.anagrafica = anagrafica;
	}
	public String geteMail() {
		return eMail;
	}
	public void seteMail(String eMail) {
		this.eMail = eMail;
	}
	public String getIndirizzo() {
		return indirizzo;
	}
	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
	}
	public String getCivico() {
		return civico;
	}
	public void setCivico(String civico) {
		this.civico = civico;
	}
	public String getCap() {
		return cap;
	}
	public void setCap(String cap) {
		this.cap = cap;
	}
	public String getLocalita() {
		return localita;
	}
	public void setLocalita(String localita) {
		this.localita = localita;
	}
	public String getProvincia() {
		return provincia;
	}
	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}
	public String getNazione() {
		return nazione;
	}
	public void setNazione(String nazione) {
		this.nazione = nazione;
	}
}
