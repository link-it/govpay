/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2017 Link.it srl (http://www.link.it).
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 3, as published by
 * the Free Software Foundation.
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

import it.gov.digitpa.schemas._2011.ws.paa.FaultBean;
import it.govpay.core.exceptions.NdpException.FaultNodo;
import it.govpay.core.utils.GpThreadLocal;
import it.govpay.servizi.commons.EsitoOperazione;
import it.govpay.servizi.v2_3.commons.GpResponse;
import it.govpay.servizi.v2_3.commons.Mittente;

public class GovPayException extends Exception {

	private static final long serialVersionUID = 1L;
	private String[] params;
	private EsitoOperazione codEsito;
	private String causa;
	private FaultBean faultBean;
	
	
	public GovPayException(FaultBean faultBean) {
		super();
		this.faultBean = faultBean;
		
		FaultNodo fault = FaultNodo.valueOf(faultBean.getFaultCode());
		switch (fault) {
		case PPT_WISP_SESSIONE_SCONOSCIUTA:
			this.setCodEsitoOperazione(EsitoOperazione.WISP_000);
			break;
		case PPT_WISP_TIMEOUT_RECUPERO_SCELTA:
			this.setCodEsitoOperazione(EsitoOperazione.WISP_001);
			break;
		default:
			this.setCodEsitoOperazione(EsitoOperazione.NDP_001);
			break;
		}
	}
	
	public GovPayException(String causa, EsitoOperazione codEsito, String ... params) {
		this.params = params;
		this.setCodEsitoOperazione(codEsito);
		this.setCausa(causa);
	}
	
	public GovPayException(String causa, EsitoOperazione codEsito, Throwable e, String ... params) {
		super(e);
		this.params = params;
		this.setCodEsitoOperazione(codEsito);
		this.setCausa(causa);
	}
	
	public GovPayException(Throwable e, String descrizione) {
		super(e);
		this.params = new String[1];
		this.params[0] = descrizione;
		this.setCodEsitoOperazione(EsitoOperazione.INTERNAL);
		this.setCausa(org.apache.commons.lang.exception.ExceptionUtils.getStackTrace(e));
	}
	
	public GovPayException(EsitoOperazione codEsito, String ... params) {
		this.params = params;
		this.setCodEsitoOperazione(codEsito);
	}
	
	public GovPayException(EsitoOperazione codEsito, Throwable e, String ... params) {
		super(e);
		this.params = params;
		this.setCodEsitoOperazione(codEsito);
		this.setCausa(org.apache.commons.lang.exception.ExceptionUtils.getStackTrace(e));
	}
	
	public GovPayException(Exception e) {
		super(e);
		this.setCodEsitoOperazione(EsitoOperazione.INTERNAL);
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

	public void setCodEsitoOperazione(EsitoOperazione codEsito) {
		this.codEsito = codEsito;
	}
	
	public void log(Logger log){
		switch (codEsito) {
		case INTERNAL:
			log.error("[" + codEsito + "] " + getMessage() + (getCausa() != null ? "\n" + getCausa() : ""), this);
		break;
		default:
			log.warn("[" + codEsito + "] " + getMessage() +  (getCausa() != null ? "\n" + getCausa() : ""));
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
		case NDP_001: return "Richiesta rifiutata dal Nodo dei Pagamenti [" + faultBean.getFaultCode() + "] " + faultBean.getFaultString() + (faultBean.getDescription() != null ? ": " + faultBean.getDescription() : "");
		case PAG_000: return "I versamenti di una richiesta di pagamento devono afferire alla stessa stazione";	
		case PAG_001: return "Il canale indicato non supporta il pagamento di piu' versamenti";	
		case PAG_002: return "Il canale indicato non puo' eseguire pagamenti ad iniziativa Ente";
		case PAG_003: return "Il canale indicato non supporta il pagamento di Marca da Bollo Telematica";
		case PAG_004: return "Il tipo di pagamento Addebito Diretto richiede di specificare un Iban di Addebito";
		case PAG_005: return "Il tipo di pagamento On-line Banking e-Payment (OBEP) consente il pagamento di versamenti con al piu' un singolo versamento";
		case PAG_006: return "Il versamento e' in uno stato che non consente il pagamento";
		case PAG_007: return "Il versamento e' scaduto";
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
		case VER_010: return "La verifica del versamento ha dato esito PAA_PAGAMENTO_SCADUTO";
		case VER_011: return "La verifica del versamento ha dato esito PAA_PAGAMENTO_SCONOSCIUTO";
		case VER_012: return "La verifica del versamento ha dato esito PAA_PAGAMENTO_DUPLICATO";
		case VER_013: return "La verifica del versamento ha dato esito PAA_PAGAMENTO_ANNULLATO";
		case VER_014: return "La verifica del versamento e' fallita";
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

	public String getCausa() {
		return causa;
	}

	public void setCausa(String causa) {
		this.causa = causa;
	}

	public FaultBean getFaultBean() {
		return faultBean;
	}

	public void setFaultBean(FaultBean faultBean) {
		this.faultBean = faultBean;
	}
	
	
	public String getDescrizioneEsito() {
		switch (codEsito) {
		case OK: return "Operazione completata con successo";
		case INTERNAL: return "Errore interno";
		case APP_000: return "Richiesta non valida";	
		case APP_001: return "Richiesta non valida";
		case APP_002: return "Richiesta non valida";
		case AUT_000: return "Operazione non autorizzata";	
		case AUT_001: return "Operazione non autorizzata";
		case AUT_002: return "Operazione non autorizzata";
		case DOM_000: return "Richiesta non valida";	
		case DOM_001: return "Richiesta non valida";
		case DOM_002: return "Richiesta non valida";	
		case DOM_003: return "Richiesta non valida";
		case NDP_000: return "Errore di invocazione del Nodo dei Pagamenti";
		case NDP_001: return "Richiesta rifiutata dal Nodo dei Pagamenti";
		case PAG_000: return "Richiesta non valida";	
		case PAG_001: return "Richiesta non valida";	
		case PAG_002: return "Richiesta non valida";
		case PAG_003: return "Richiesta non valida";
		case PAG_004: return "Richiesta non valida";
		case PAG_005: return "Richiesta non valida";
		case PAG_006: return "Richiesta non valida";
		case PAG_007: return "Richiesta non valida";
		case PAG_008: return "Richiesta non valida"; 
		case PAG_009: return "Richiesta non valida"; 
		case PAG_010: return "Richiesta non valida"; 
		case PAG_011: return "Richiesta non valida"; 
		case PRT_000: return "Richiesta non valida";	
		case PRT_001: return "Richiesta non valida";
		case PRT_002: return "Richiesta non valida";
		case PRT_003: return "Richiesta non valida";
		case PRT_004: return "Richiesta non valida"; 
		case PRT_005: return "Richiesta non valida";
		case PSP_000: return "Richiesta non valida";
		case PSP_001: return "Richiesta non valida";
		case RND_000: return "Richiesta non valida";
		case RND_001: return "Richiesta non valida";
		case STA_000: return "Richiesta non valida";	
		case STA_001: return "Richiesta non valida";
		case TRB_000: return "Richiesta non valida";
		case UOP_000: return "Richiesta non valida";
		case UOP_001: return "Richiesta non valida";
		case VER_000: return "Richiesta non valida";
		case VER_001: return "Richiesta non valida";
		case VER_002: return "Richiesta non valida";
		case VER_003: return "Richiesta non valida";
		case VER_004: return "Richiesta non valida";
		case VER_005: return "Richiesta non valida";
		case VER_006: return "Richiesta non valida";
		case VER_007: return "Richiesta non valida";
		case VER_008: return "Richiesta non valida";
		case VER_009: return "Richiesta non valida";
		case VER_010: return "Richiesta non valida";
		case VER_011: return "Richiesta non valida";
		case VER_012: return "Richiesta non valida";
		case VER_013: return "Richiesta non valida";
		case VER_014: return "Richiesta non valida";
		case VER_015: return "Richiesta non valida";
		case VER_016: return "Richiesta non valida";
		case VER_017: return "Richiesta non valida";
		case VER_018: return "Richiesta non valida";
		case VER_019: return "Richiesta non valida";
		case VER_020: return "Richiesta non valida";
		case VER_021: return "Richiesta non valida";
		case VER_022: return "Richiesta non valida";
		case VER_023: return "Richiesta non valida";
		case WISP_000: return "Errore WISP";
		case WISP_001: return "Errore WISP";
		case WISP_002: return "Errore WISP";
		case WISP_003: return "Errore WISP";
		case WISP_004: return "Errore WISP";
		}
		
		return "";
	}

	public GpResponse getWsResponse(GpResponse response, String codMsgDiagnostico, Logger log) {
		if(getFaultBean() == null) {
			response.setMittente(Mittente.GOV_PAY);
			response.setCodEsito(this.getCodEsito().toString());
			response.setDescrizioneEsito(this.getDescrizioneEsito());
			response.setDettaglioEsito(this.getMessage());
			log(log);
			GpThreadLocal.get().log(codMsgDiagnostico, response.getCodEsito().toString(), response.getDescrizioneEsito(), response.getDettaglioEsito());
			
		} else {
			if(faultBean.getId().contains("NodoDeiPagamentiSPC")) 
				response.setMittente(Mittente.NODO_DEI_PAGAMENTI_SPC);
			else 
				response.setMittente(Mittente.PSP);
			response.setCodEsito(faultBean.getFaultCode());
			response.setDescrizioneEsito(faultBean.getFaultString());
			response.setDettaglioEsito(faultBean.getDescription());
		}
		return response;
	}
}
