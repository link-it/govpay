/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2016 Link.it srl (http://www.link.it).
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
package it.govpay.core.exceptions;

import org.apache.logging.log4j.Logger;

import it.govpay.servizi.commons.EsitoOperazione;

public class GovPayException extends Exception {

	private static final long serialVersionUID = 1L;
	private String[] params;
	private EsitoOperazione codEsito;
	
	public GovPayException(EsitoOperazione codEsito, String ... params) {
		this.params = params;
		this.setCodEsito(codEsito);
	}
	
	public GovPayException(EsitoOperazione codEsito, Exception e, String ... params) {
		super(e);
		this.params = params;
		this.setCodEsito(codEsito);
	}
	
	public GovPayException(Exception e) {
		super(e);
		this.setCodEsito(EsitoOperazione.INTERNAL);
	}

	public String[] getParams() {
		return params;
	}

	public void setParams(String[] params) {
		this.params = params;
	}

	public EsitoOperazione getCodEsito() {
		return codEsito;
	}

	public void setCodEsito(EsitoOperazione codEsito) {
		this.codEsito = codEsito;
	}
	
	public void log(Logger log){
		switch (codEsito) {
		case INTERNAL:
			log.error("[" + codEsito + "] " + getMessage(), this);
		break;
		default:
			log.error("[" + codEsito + "] " + getMessage());
			break;
		}
	}
	
	@Override
	public String getMessage() {
		switch (codEsito) {
		case OK: return "Operazione completata con successo";
		case INTERNAL: 
			if(params != null && params.length > 0)
				return params[0];
			else
				return super.getMessage();
		case APP_000: return "Applicazione (" + params[0] + ") inesistente";	
		case APP_001: return "Applicazione (" + params[0] + ") disabilitata";
		case APP_002: return "Applicazione autenticata (" + params[0] + ") diversa dalla chiamante (" + params[1] + ")";
		case AUT_000: return "Principal non fornito";	
		case AUT_001: return "Principal (" + params[0] + ") non associato ad alcuna Applicazione";
		case AUT_002: return "Principal (" + params[0] + ") non associato ad alcun Portale";
		case DOM_000: return "Dominio (" + params[0] + ") inesistente";	
		case DOM_001: return "Dominio (" + params[0] + ") disabilitato";
		case DOM_002: return "Dominio (" + params[0] + ") configurato per la generazione custom degli iuv. Nella richiesta deve essere indicato lo IUV da assegnare.";	
		case DOM_003: return "Dominio (" + params[0] + ") configurato per la generazione centralizzata degli iuv. Nella richiesta non deve essere indicato lo IUV da assegnare.";
		case NDP_000: return (params != null && params.length > 0) ? params[0] : null;
		case NDP_001: return "Richiesta di Pagamento rifiutata dal Nodo dei Pagamenti" + ((params.length > 0) ? ": " + params[0] : "");
		case PAG_000: return "I versamenti di una richiesta di pagamento devono afferire alla stessa stazione";	
		case PAG_001: return "Il canale indicato non supporta il pagamento di piu' versamenti";	
		case PAG_002: return "Il canale indicato non puo' eseguire pagamenti ad iniziativa Ente";
		case PAG_003: return "Il canale indicato non supporta il pagamento di Marca da Bollo Telematica";
		case PAG_004: return "Il tipo di pagamento Addebito Diretto richiede di specificare un Iban di Addebito";
		case PAG_005: return "Il tipo di pagamento On-line Banking e-Payment (OBEP) consente il pagamento di versamenti con al piu' un singolo versamento";
		case PAG_006: return "Il versamento (" + params[1] + ") dell'applicazione (" + params[0] + ") e' in uno stato che non consente il pagamento (" + params[2] + ")";
		case PAG_007: return "Il versamento (" + params[1] + ") dell'applicazione (" + params[0] + ") e' scaduto";
		case PAG_008: return "Transazione di pagamento inesistente"; 
		case PAG_009: return "Pagamento con Identificativo Univoco di Riscossione (" + params[0] + ") e' gia' stato stornato."; 
		case PAG_010: return "Richiesta di storno inesistente."; 
		case PAG_011: return "Nessun pagamento da stornare."; 
		case PRT_000: return "Portale (" + params[0] + ") inesistente";	
		case PRT_001: return "Portale (" + params[0] + ") disabilitato";
		case PRT_002: return "Portale autenticato (" + params[0] + ") diverso dal chiamante (" + params[1] + ")";
		case PRT_003: return "Portale (" + params[0] + ") non autorizzato a pagare il versamento (" + params[2] + ") dell'applicazione (" + params[1] + ")";
		case PRT_004: return "Portale non autorizzato ad operare sulla transazione indicata"; 
		case PRT_005: return "Portale non autorizzato per l'operazione richiesta";
		case PSP_000: return "Canale (" + params[1] + ") del psp (" + params[0] + ") per pagamenti di tipo (" + params[2] + ") inesistente";
		case PSP_001: return "Canale (" + params[1] + ") del psp (" + params[0] + ") per pagamenti di tipo (" + params[2] + ") disabilitato";
		case RND_000: return "Flusso di rendicontazione inesistente";
		case RND_001: return "Applicazione non abilitata all'acquisizione del flusso indicato.";
		case STA_000: return "Stazione (" + params[0] + ") inesistente";	
		case STA_001: return "Stazione (" + params[0] + ") disabilitata";
		case TRB_000: return "Tributo (" + params[1] + ") inesistente per il dominio (" + params[0] + ")";
		case UOP_000: return "Unita' operativa (" + params[0] + ") del dominio (" + params[1] + ") inesistente";
		case UOP_001: return "Unita' operativa (" + params[0] + ") del dominio (" + params[1] + ") disabilitata";
		case VER_000: return "Il versamento (" + params[1] + ") dell'applicazione (" + params[0] + ") non pagabile ad iniziativa PSP";
		case VER_001: return "Il versamento (" + params[1] + ") dell'applicazione (" + params[0] + ") presenta il codSingoloPagamentoEnte (" + params[2] + ") piu' di una volta";
		case VER_002: return "Il versamento (" + params[1] + ") dell'applicazione (" + params[0] + ") ha un importo totale (" + params[3] + ") diverso dalla somma dei singoli importi (" + params[2] + ")";
		case VER_003: return "Il versamento (" + params[1] + ") dell'applicazione (" + params[0] + ") e' in uno stato che non consente l'aggiornamento (" + params[2] + ")";
		case VER_004: return "Il versamento (" + params[1] + ") dell'applicazione (" + params[0] + ") inviato ha un'unita' operativa beneficiaria (" + params[2] + ") diversa da quello originale (" + params[3] + ")";
		case VER_005: return "Il versamento (" + params[1] + ") dell'applicazione (" + params[0] + ") inviato ha un numero di singoli versamenti (" + params[2] + ") diverso da quello originale (" + params[3] + ")";
		case VER_006: return "Il versamento (" + params[1] + ") dell'applicazione (" + params[0] + ") inviato ha un singolo versamento con codSingoloVersamentoEnte (" + params[2] + ") non presente nell'originale";
		case VER_007: return "Il versamento (" + params[1] + ") dell'applicazione (" + params[0] + ") ha il singolo versamento con codSingoloVersamentoEnte (" + params[2] + ") inviato ha un tipo tributo (" + params[3] + ") diverso dall'originale (" + params[4] + ")";
		case VER_008: return "Il versamento non esiste.";
		case VER_009: return "Il versamento (" + params[1] + ") dell'applicazione (" + params[0] + ") e' in uno stato che non consente l'annullamento (" + params[2] + ")";
		case VER_010: return "La verifica del versamento (" + params[1] + ") dell'applicazione (" + params[0] + ") ha dato esito PAA_PAGAMENTO_SCADUTO";
		case VER_011: return "La verifica del versamento (" + params[1] + ") dell'applicazione (" + params[0] + ") ha dato esito PAA_PAGAMENTO_SCONOSCIUTO";
		case VER_012: return "La verifica del versamento (" + params[1] + ") dell'applicazione (" + params[0] + ") ha dato esito PAA_PAGAMENTO_ANNULLATO";
		case VER_013: return "La verifica del versamento (" + params[1] + ") dell'applicazione (" + params[0] + ") ha dato esito PAA_PAGAMENTO_DUPLICATO";
		case VER_014: return "La verifica del versamento (" + params[1] + ") dell'applicazione (" + params[0] + ") e' fallita";
		case VER_015: return "Il versamento (" + params[1] + ") dell'applicazione (" + params[0] + ") esistente e non aggiornabile se AggiornaSeEsiste impostato a false";
		case VER_016: return "Il versamento (" + params[1] + ") dell'applicazione (" + params[0] + ") e' in uno stato che non consente la notifica di pagamento (" + params[2] + ")";
		case VER_017: return "Lo IUV (" + params[0] + ") non e' conforme alle specifiche agid";
		case VER_018: return "Lo IUV (" + params[0] + ") e' gia' associato al versamento (" + params[1] + ")";
		case VER_019: return "Applicazione non autorizzata all'autodeterminazione dei tributi";
		case VER_020: return "Iban di accredito non censito";
		case VER_021: return "Applicazione non autorizzata all'autodeterminazione dei tributi per il dominio indicato";
		case VER_022: return "Applicazione non autorizzata alla gestione del tributo indicato";
		case VER_023: return "Il versamento (" + params[1] + ") dell'applicazione (" + params[0] + ") ha il singolo versamento con codSingoloVersamentoEnte (" + params[2] + ") inviato ha un iban di accredito diverso dall'originale.";
		case WISP_000: return "Sessione di scelta sconosciuta al WISP";
		case WISP_001: return "Sessione di scelta scaduta per timeout al WISP";
		case WISP_002: return "Canale (" + params[1] + ") del Psp (" + params[0] + ") con tipo versamento (" + params[2] + ") scelto non presente in anagrafica Psp";
		case WISP_003: return "Il debitore non ha operato alcuna scelta sul WISP";
		case WISP_004: return "Il debitore ha scelto di pagare dopo tramite avviso di pagamento.";
		}
		
		return "";
	}
}
