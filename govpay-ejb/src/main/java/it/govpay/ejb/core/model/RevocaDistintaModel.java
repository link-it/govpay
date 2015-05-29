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

import java.math.BigDecimal;
import java.util.Date;

import it.govpay.ejb.core.utils.EnumChiaveDescrizione;

public class RevocaDistintaModel {
	
	/**
	 * EnumStatoOperazione della distinta
	 */
	public enum EnumStatoRevoca implements EnumChiaveDescrizione {
		
		IN_CORSO("IN CORSO", "In corso"), 
		ESEGUITO("ESEGUITO", "Eseguito"),
		PARZIALMENTE_ESEGUITO("PARZ. ESEGUITO", "Parzialmente eseguito"),
		ESEGUITO_SBF("ESEGUITO SBF", "Eseguito salvo buon fine"),
		NON_ESEGUITO("NON ESEGUITO", "Non eseguito"),
		IN_ERRORE("IN ERRORE", "In errore");
		
		private String chiave;
		private String descrizione;

		EnumStatoRevoca(String chiave, String descrizione) {
			this.chiave = chiave;
			this.descrizione = descrizione;
		}

		public String getChiave() {
			return chiave;
		}
		public String getDescrizione() {
			return descrizione;
		}

	}
	
	/**
	 * Id Fisico della richiesta revoca su database
	 */
	private Long idRevocaDistinta;	
	
	/**
	 * Id Fisico della distinta associata
	 */
	private Long idDistinta;	
	
	/**
	 * Codice Transazione assegnato alla revoca.
	 */
	private String codTransazione;
	
	/**
	 * Codice Transazione assegnato alla revoca.
	 */
	private EnumStatoRevoca statoRevoca;
	
	/**
	 * Causale della revoca.
	 */
	private String causale;
	
	/**
	 * Causale della revoca.
	 */
	private String causaleErrore;
	
	/**
	 * Importo revocato.
	 */
	private BigDecimal importoRevocato;
	
	/**
	 * Data di richiesta di revoca.
	 */
	private Date dataRichiesta;
	
	/**
	 * Data di ricezione dell'esito.
	 */
	private Date dataRisposta;
	

	public Long getIdRevocaDistinta() {
		return idRevocaDistinta;
	}

	public void setIdRevocaDistinta(Long idRevocaDistinta) {
		this.idRevocaDistinta = idRevocaDistinta;
	}

	public Long getIdDistinta() {
		return idDistinta;
	}

	public void setIdDistinta(Long idDistinta) {
		this.idDistinta = idDistinta;
	}

	public String getCodTransazione() {
		return codTransazione;
	}

	public void setCodTransazione(String codTransazione) {
		this.codTransazione = codTransazione;
	}

	public String getCausale() {
		return causale;
	}

	public void setCausale(String causale) {
		this.causale = causale;
	}

	public Date getDataRichiesta() {
		return dataRichiesta;
	}

	public void setDataRichiesta(Date dataRichiesta) {
		this.dataRichiesta = dataRichiesta;
	}

	public Date getDataRisposta() {
		return dataRisposta;
	}

	public void setDataRisposta(Date dataRisposta) {
		this.dataRisposta = dataRisposta;
	}

	public EnumStatoRevoca getStatoRevoca() {
		return statoRevoca;
	}

	public void setStatoRevoca(EnumStatoRevoca statoRevoca) {
		this.statoRevoca = statoRevoca;
	}

	public BigDecimal getImportoRevocato() {
		return importoRevocato;
	}

	public void setImportoRevocato(BigDecimal importoRevocato) {
		this.importoRevocato = importoRevocato;
	}

	public String getCausaleErrore() {
		return causaleErrore;
	}

	public void setCausaleErrore(String causaleErrore) {
		this.causaleErrore = causaleErrore;
	}

}
