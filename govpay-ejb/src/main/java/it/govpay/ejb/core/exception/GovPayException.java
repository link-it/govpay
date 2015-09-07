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
package it.govpay.ejb.core.exception;

public class GovPayException extends Exception {

	private static final long serialVersionUID = 1L;

	public enum GovPayExceptionEnum {
		IUV_DUPLICATO ("KOGP0001"), // Uno degli IUV inviati e' gia' stato usato in un'altro pagamento (no descrizione, no fault)
		IUV_NON_TROVATO ("KOGP0002"), // La richiesta eseguita non riferisce nessun pagamento presente in base dati
		PSP_NON_TROVATO ("KOGP0003"), // Il psp indicato non e' presente in base dati
		ENTE_NON_TROVATO ("KOGP0013"), 
		ERRORE_INTERNO ("KOGP0004"), // Errore DB o eccezione non prevista (exception valorizzata)
		ERRORE_CONFIGURAZIONE ("KOGP0005"), // Parametro configurazione mancante o valorizzato male (in descrizione il nome parametro)
		BENEFICIARIO_NON_TROVATO ("KOGP0006"), // Il codice fiscale del beneficiario non e' censito nell'anagrafica (in descrizione il codice fiscale non trovato)
		ERRORE_VALIDAZIONE_NDP ("KOGP0007"),  // Il pagamento non rispetta i vincoli del Nodo dei Pagamenti (descrizione con dettaglio sulla violazione)
		ERRORE_VALIDAZIONE ("KOGP0008"),// Il pagamento non rispetta i vincoli comune a tutti i canali di pagamento
		ERRORE_NDP ("KOGP0009"), // L'invocazione del Nodo dei Pagamenti ha ritornato un errore applicativo (descrizione popolata con ${faultCode}: ${faultString} 
		ERRORE_NDP_WEB ("KOGP0010"),// L'invocazione del Nodo dei Pagamenti ha avuto un errore di connessione (exception valorizzata)
		ERRORE_ENTE_WEB ("KOGP0011"),// L'invocazione dei servizi dell'Ente (notifica o verifica) ha ritornato un errore 
		STORNO_NON_CONSENTITO ("KOGP0012"), // Lo storno per il pagamento richiesto non puo' essere richiesto. motivazione in descrizione.
		IUSV_DUPLICATO ("KOGP0014");// Uno degli IUSV inviati e' stato gia' pagato e' in corso;
		private String errorCode;

		GovPayExceptionEnum(String errorCode) {
			this.errorCode = errorCode;
		}

		public String getErrorCode() {
			return errorCode;
		}

		public void setErrorCode(String errorCode) {
			this.errorCode = errorCode;
		}

		
	}
	
	private GovPayExceptionEnum tipoException;
	private String descrizione;
	
	public GovPayException(GovPayExceptionEnum tipoException, String descrizione) {
		super(descrizione);
		this.tipoException = tipoException;
		this.descrizione = descrizione;
	}
	
	public GovPayException(GovPayExceptionEnum tipoException, String descrizione, Throwable t) {
		super(descrizione, t);
		this.tipoException = tipoException;
		this.descrizione = descrizione;
	}
	
	public GovPayException(GovPayExceptionEnum tipoException, Throwable t) {
		super(t);
		this.tipoException = tipoException;
	}
	
	public GovPayException(GovPayExceptionEnum tipoException) {
		this(tipoException, "");
	}
	
	public GovPayExceptionEnum getTipoException() {
		return tipoException;
	}

	public String getDescrizione() {
		return descrizione;
	}

}
