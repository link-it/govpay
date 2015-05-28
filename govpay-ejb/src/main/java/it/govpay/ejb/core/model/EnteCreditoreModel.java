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
package it.govpay.ejb.model;

import java.util.List;

/**
 * 
 * Rapppresenta un ente creditore
 * @author RepettiS
 *
 */
public class EnteCreditoreModel {
	
	/**
	 * EnumStatoOperazione dell'ente nel sistema (Attivo, Disattivo)
	 *
	 */
	public enum EnumStato {
		/** Attivo */ A,
		/** Disattivo */ D
	}
	
	/**
	 * Id  fisico dell'ente creditore
	 */
	private String idEnteCreditore; 
	
	/**
	 * Identificativo univoco mnemonico dell'ente creditore
	 */
	private String identificativoUnivoco;
	
	/**
	 * Identificativo fiscale dell'ente creditore
	 */
	private String idFiscale; 
	
	/**
	 * Ragione sociale dell'ente creditore che compare nelle ricevute
	 */	
	private String denominazione; 

	/**
	 * Indirizzo dell'ente creditore che compare nelle ricevute
	 */		
	private String indirizzo;
	
	private String civico;
	
	/**
	 * Località dell'ente creditore che compare nelle ricevute
	 */		
	private String localita;
	private String cap;

	/**
	 * Provincia dell'ente creditore  che compare nelle ricevute (e.g. PI)
	 */	
    private String provincia;

	/**
	 * EnumStatoOperazione dell'ente creditore (attivo/ non attivo)
	 */	
    private EnumStato stato;
    
    private String numTelefono;
    private String numFax;

	/**
	 * Ragione sociale dell'ente creditore
	 */	
    private List<TributoModel> tributiGestiti; //TributiEnte
    
	public String getIdEnteCreditore() {
		return idEnteCreditore;
	}

	public void setIdEnteCreditore(String idEnteCreditore) {
		this.idEnteCreditore = idEnteCreditore;
	}

	public String getIdentificativoUnivoco() {
		return identificativoUnivoco;
	}

	public void setIdentificativoUnivoco(String identificativoUnivoco) {
		this.identificativoUnivoco = identificativoUnivoco;
	}

	public String getIdFiscale() {
		return idFiscale;
	}

	public void setIdFiscale(String idFiscale) {
		this.idFiscale = idFiscale;
	}

	public String getDenominazione() {
		return denominazione;
	}

	public void setDenominazione(String denominazione) {
		this.denominazione = denominazione;
	}

	public String getIndirizzo() {
		return indirizzo;
	}

	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
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

	public EnumStato getStato() {
		return stato;
	}

	public void setStato(EnumStato stato) {
		this.stato = stato;
	}

	public List<TributoModel> getTributiGestiti() {
		return tributiGestiti;
	}

	public void setTributiGestiti(List<TributoModel> tributiGestiti) {
		this.tributiGestiti = tributiGestiti;
	}

	public String getNumTelefono() {
		return numTelefono;
	}

	public void setNumTelefono(String numTelefono) {
		this.numTelefono = numTelefono;
	}

	public String getNumFax() {
		return numFax;
	}

	public void setNumFax(String numFax) {
		this.numFax = numFax;
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
	
}

