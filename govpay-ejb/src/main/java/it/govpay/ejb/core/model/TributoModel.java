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
 * Rappresenta un tipo debito gestito dall'ente creditore
 * @author RepettiS
 *
 */
public class TributoModel {
	
	public enum EnumStatoTributo {
		/** Attivo */ A,
		/** Disattivo */ D
	}

	/**
	 * Identificativo della categoria del tributo
	 * (e.g. PrestazioniSanitarie)
	 */
	private String idTributo; 
	
	/**
	 * Chiave fisica dell'ente creditore che gestisce il tributo
	 */
	private String idEnteCreditore;
	
	/**
	 * Codice del sistema informativo locale dell'ente creditore, applicazione o sottosistema che gestisce il tributo.
	 */
	private String idSistema; 
	
	/**
	 * Codice specifico del tipo debito noto all'ente creditore:
	  e.g. TICKET_SANITARIO_CUP
	 */
	private String codiceTributo; 
	private String descrizione;
	private String ibanAccredito;
	private EnumStatoTributo stato;
	
	
	
	public String getIdTributo() {
		return idTributo;
	}

	public void setIdTributo(String idTributo) {
		this.idTributo = idTributo;
	}

	public String getIdEnteCreditore() {
		return idEnteCreditore;
	}

	public void setIdEnteCreditore(String idEnteCreditore) {
		this.idEnteCreditore = idEnteCreditore;
	}

	public String getIdSistema() {
		return idSistema;
	}

	public void setIdSistema(String idSistema) {
		this.idSistema = idSistema;
	}

	public String getCodiceTributo() {
		return codiceTributo;
	}

	public void setCodiceTributo(String codiceTributo) {
		this.codiceTributo = codiceTributo;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public EnumStatoTributo getStato() {
		return stato;
	}

	public void setStato(EnumStatoTributo stato) {
		this.stato = stato;
	}

	public String getIbanAccredito() {
		return ibanAccredito;
	}

	public void setIbanAccredito(String ibanAccredito) {
		this.ibanAccredito = ibanAccredito;
	} 

}
