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

import it.govpay.ejb.utils.EnumChiaveDescrizione;

import java.math.BigDecimal;
import java.util.Date;


/**
 * 
 * Rappresenta il singolo pagamento incluso in una distinta.
 * Contiene i dati specifici relativi al tracciamento del processo di pagamento 
 * che riguardano un  singolo item della distinta.
 * La classe è legata uno ad uno con la posizione da pagare, nello specifico con una CondizionePagamento che 
 * contiene invece tutti i dati della posizione da pagare.
 * 
 * @author RepettiS
 *
 */
public class PagamentoModel {
	
	
	public enum StatoNotifica {
		ESEGUITO, NOTIFICATO, REGOLATO
	}	
	
	/**
	 *  EnumStatoOperazione del pagamento.
	 */
	public enum EnumStatoPagamento implements EnumChiaveDescrizione{
		/** Eseguito */   			ES("ESEGUITO", "Eseguito"),  
		/** Eseguito sbf */			EF("ESEGUITO SBF", "Eseguito salvo buon fine"),
		/** In corso */ 			IC("IN CORSO", "In corso"),
		/** Non eseguito  */ 		NE("NON ESEGUITO", "Non eseguito"),
		/** In errore  */			IE("IN ERRORE", "In errore"),
		/** Stornato  */		    ST("STORNATO", "Stornato"),
		/** Annullato */			AN("ANNULLATO", "Annullato"),
		/** Annullato operatore */ 	AO("ANNULLATO OPE", "Annullato operatore");

		private String chiave;
		private String descrizione;

		EnumStatoPagamento(String chiave, String descrizione) {
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
	 *  EnumStatoOperazione del pagamento.
	 */
	public enum EnumTipoPagamento {
		/** Pagamento Singolo */  	S,  
		/** Rata */ 				R
	}
	
	/**
	 * Identificativo fisico di questo pagamento
	 */
	private Long idPagamento;
	
	/**
	 * EnumStatoOperazione del singolo pagamento. 
	 */
	private EnumStatoPagamento stato;
	
	/**
	 * Data pagamento
	 */
	private Date dataPagamento;
	
	/**
	 * Identificativo della riscossione specifica assegnato dal PSP.
	 * In NDP corrisponde all'identificativo univoco riscossione.
	 */
	private String idRiscossionePSP;
	
	/**
	 * Legame 1 ad 1 con la condizione pagamento che contiene i dettagli
	 * della posizione da pagare.
	 */
	private String idCondizionePagamento;
	
	/**
	 * Importo realmente versato
	 */
	private BigDecimal importoPagato;

	public Long getIdPagamento() {
		return idPagamento;
	}

	public void setIdPagamento(Long idPagamento) {
		this.idPagamento = idPagamento;
	}

	public EnumStatoPagamento getStato() {
		return stato;
	}

	public void setStato(EnumStatoPagamento stato) {
		this.stato = stato;
	}

	public Date getDataPagamento() {
		return dataPagamento;
	}

	public void setDataPagamento(Date dataPagamento) {
		this.dataPagamento = dataPagamento;
	}

	public String getIdRiscossionePSP() {
		return idRiscossionePSP;
	}

	public void setIdRiscossionePSP(String idRiscossionePSP) {
		this.idRiscossionePSP = idRiscossionePSP;
	}

	public String getIdCondizionePagamento() {
		return idCondizionePagamento;
	}

	public void setIdCondizionePagamento(String idCondizionePagamento) {
		this.idCondizionePagamento = idCondizionePagamento;
	}

	public BigDecimal getImportoPagato() {
		return importoPagato;
	}

	public void setImportoPagato(BigDecimal importoPagato) {
		this.importoPagato = importoPagato;
	}
	
}
