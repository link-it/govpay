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

import org.slf4j.Logger;

import it.gov.digitpa.schemas._2011.ws.paa.FaultBean;
import it.govpay.core.utils.GpThreadLocal;
import it.govpay.core.utils.client.BasicClient.ClientException;
import it.govpay.bd.model.Nota.TipoNota;
import it.govpay.core.rs.v1.costanti.EsitoOperazione;
import it.govpay.servizi.v2_3.commons.GpResponse;
import it.govpay.servizi.v2_3.commons.Mittente;

public class GovPayException extends Exception {

	private static final long serialVersionUID = 1L;
	private String[] params;
	private EsitoOperazione codEsito;
	private String causa;
	private FaultBean faultBean;
	private Object param;
		
	
	public GovPayException(FaultBean faultBean) {
		super();
		this.faultBean = faultBean;
		this.setCodEsitoOperazione(EsitoOperazione.NDP_001);
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
		return this.params;
	}

	public void setParams(String[] params) {
		this.params = params;
	}

	public EsitoOperazione getCodEsito() {
		return this.codEsito;
	}

	public void setCodEsitoOperazione(EsitoOperazione codEsito) {
		this.codEsito = codEsito;
	}
	
	public void log(Logger log){
		switch (this.codEsito) {
		case INTERNAL:
			log.error("[" + this.codEsito + "] " + this.getMessage() + (this.getCausa() != null ? "\n" + this.getCausa() : ""), this);
		break;
		default:
			log.warn("[" + this.codEsito + "] " + this.getMessage() +  (this.getCausa() != null ? "\n" + this.getCausa() : ""));
			break;
		}
	}
	
	
	
	@Override
	public String getMessage() {
		switch (this.codEsito) {
		case OK: return "Operazione completata con successo";
		case INTERNAL: 
			if(this.params != null && this.params.length > 0)
				return this.params[0];
			else
				return super.getMessage();
		case APP_000: return "Applicazione (" + this.params[0] + ") inesistente";	
		case APP_001: return "Applicazione (" + this.params[0] + ") disabilitata";
		case APP_002: return "Applicazione autenticata (" + this.params[0] + ") diversa dalla chiamante (" + this.params[1] + ")";
		case AUT_000: return "Principal non fornito";	
		case AUT_001: return "Principal (" + this.params[0] + ") non associato ad alcuna Applicazione";
		case AUT_002: return "Principal (" + this.params[0] + ") non associato ad alcun Portale";
		case DOM_000: return "Dominio (" + this.params[0] + ") inesistente";	
		case DOM_001: return "Dominio (" + this.params[0] + ") disabilitato";
		case DOM_002: return "Dominio (" + this.params[0] + ") configurato per la generazione custom degli iuv. Nella richiesta deve essere indicato lo IUV da assegnare.";	
		case DOM_003: return "Dominio (" + this.params[0] + ") configurato per la generazione centralizzata degli iuv. Nella richiesta non deve essere indicato lo IUV da assegnare.";
		case NDP_000: return (this.params != null && this.params.length > 0) ? this.params[0] : null;
		case NDP_001: return "Richiesta rifiutata dal Nodo dei Pagamenti [" + this.faultBean.getFaultCode() + "] " + this.faultBean.getFaultString() + (this.faultBean.getDescription() != null ? ": " + this.faultBean.getDescription() : "");
		case PAG_000: return "I versamenti di una richiesta di pagamento devono afferire alla stessa stazione";	
		case PAG_001: return "Il canale indicato non supporta il pagamento di piu' versamenti";	
		case PAG_002: return "Il canale indicato non puo' eseguire pagamenti ad iniziativa Ente";
		case PAG_003: return "Il canale indicato non supporta il pagamento di Marca da Bollo Telematica";
		case PAG_004: return "Il tipo di pagamento Addebito Diretto richiede di specificare un Iban di Addebito";
		case PAG_005: return "Il tipo di pagamento On-line Banking e-Payment (OBEP) consente il pagamento di versamenti con al piu' un singolo versamento";
		case PAG_006: return "Il versamento e' in uno stato che non consente il pagamento";
		case PAG_007: return "Richiesto pagamento di una pendenza [Applicazione:" + this.params[0] + " IdPendenza:"+ this.params[1] +" ] con data scadenza decorsa il "+ this.params[2]+".";
		case PAG_008: return "Transazione di pagamento inesistente"; 
		case PAG_009: return "Pagamento con Identificativo Univoco di Riscossione (" + this.params[0] + ") e' gia' stato stornato."; 
		case PAG_010: return "Richiesta di storno inesistente."; 
		case PAG_011: return "Nessun pagamento da stornare."; 
		case PAG_012: return "Richiesto pagamento di una pendenza [Applicazione:" + this.params[0] + " IdPendenza:"+ this.params[1] +" ] con data validita' decorsa il "+ this.params[2]+".";
		case PAG_013: return "Impossibile indirizzare il Portale di Pagamento: non e' stata fornita una URL di ritorno in fase di richiesta. IdCarrello " + this.params[0]; 
		case PRT_000: return "Portale (" + this.params[0] + ") inesistente";	
		case PRT_001: return "Portale (" + this.params[0] + ") disabilitato";
		case PRT_002: return "Portale autenticato (" + this.params[0] + ") diverso dal chiamante (" + this.params[1] + ")";
		case PRT_003: return "Portale (" + this.params[0] + ") non autorizzato a pagare il versamento (" + this.params[2] + ") dell'applicazione (" + this.params[1] + ")";
		case PRT_004: return "Portale non autorizzato ad operare sulla transazione indicata"; 
		case PRT_005: return "Portale non autorizzato per l'operazione richiesta";
		case PSP_000: return "Canale (" + this.params[1] + ") del psp (" + this.params[0] + ") per pagamenti di tipo (" + this.params[2] + ") inesistente";
		case PSP_001: return "Canale (" + this.params[1] + ") del psp (" + this.params[0] + ") per pagamenti di tipo (" + this.params[2] + ") disabilitato";
		case RND_000: return "Flusso di rendicontazione inesistente";
		case RND_001: return "Applicazione non abilitata all'acquisizione del flusso indicato.";
		case STA_000: return "Stazione (" + this.params[0] + ") inesistente";	
		case STA_001: return "Stazione (" + this.params[0] + ") disabilitata";
		case TRB_000: return "Tributo (" + this.params[1] + ") inesistente per il dominio (" + this.params[0] + ")";
		case UOP_000: return "Unita' operativa (" + this.params[0] + ") del dominio (" + this.params[1] + ") inesistente";
		case UOP_001: return "Unita' operativa (" + this.params[0] + ") del dominio (" + this.params[1] + ") disabilitata";
		case VER_000: return "Il versamento (" + this.params[1] + ") dell'applicazione (" + this.params[0] + ") non pagabile ad iniziativa PSP";
		case VER_001: return "Il versamento (" + this.params[1] + ") dell'applicazione (" + this.params[0] + ") presenta il codSingoloPagamentoEnte (" + this.params[2] + ") piu' di una volta";
		case VER_002: return "Il versamento (" + this.params[1] + ") dell'applicazione (" + this.params[0] + ") ha un importo totale (" + this.params[3] + ") diverso dalla somma dei singoli importi (" + this.params[2] + ")";
		case VER_003: return "Il versamento (" + this.params[1] + ") dell'applicazione (" + this.params[0] + ") e' in uno stato che non consente l'aggiornamento (" + this.params[2] + ")";
		case VER_004: return "Il versamento (" + this.params[1] + ") dell'applicazione (" + this.params[0] + ") inviato ha un'unita' operativa beneficiaria (" + this.params[2] + ") diversa da quello originale (" + this.params[3] + ")";
		case VER_005: return "Il versamento (" + this.params[1] + ") dell'applicazione (" + this.params[0] + ") inviato ha un numero di singoli versamenti (" + this.params[2] + ") diverso da quello originale (" + this.params[3] + ")";
		case VER_006: return "Il versamento (" + this.params[1] + ") dell'applicazione (" + this.params[0] + ") inviato ha un singolo versamento con codSingoloVersamentoEnte (" + this.params[2] + ") non presente nell'originale";
		case VER_007: return "Il versamento (" + this.params[1] + ") dell'applicazione (" + this.params[0] + ") ha il singolo versamento con codSingoloVersamentoEnte (" + this.params[2] + ") inviato ha un tipo tributo (" + this.params[3] + ") diverso dall'originale (" + this.params[4] + ")";
		case VER_008: return "Il versamento non esiste.";
		case VER_009: return "Il versamento (" + this.params[1] + ") dell'applicazione (" + this.params[0] + ") e' in uno stato che non consente l'annullamento (" + this.params[2] + ")";
		case VER_010: return "La verifica del versamento ha dato esito PAA_PAGAMENTO_SCADUTO";
		case VER_011: return "La verifica del versamento ha dato esito PAA_PAGAMENTO_SCONOSCIUTO";
		case VER_012: return "La verifica del versamento ha dato esito PAA_PAGAMENTO_DUPLICATO";
		case VER_013: return "La verifica del versamento ha dato esito PAA_PAGAMENTO_ANNULLATO";
		case VER_014: return "La richiesta di verifica pendenza [Applicazione:" + this.params[0] + " IdPendenza:"+ this.params[1] +" ] per decorrenza della data validita' e' fallita: "+ this.params[2];
		case VER_015: return "Il versamento (" + this.params[1] + ") dell'applicazione (" + this.params[0] + ") esistente e non aggiornabile se AggiornaSeEsiste impostato a false";
		case VER_016: return "Il versamento (" + this.params[1] + ") dell'applicazione (" + this.params[0] + ") e' in uno stato che non consente la notifica di pagamento (" + this.params[2] + ")";
		case VER_017: return "Lo IUV (" + this.params[0] + ") non e' conforme alle specifiche agid";
		case VER_018: return "Lo IUV (" + this.params[0] + ") e' gia' associato al versamento (" + this.params[1] + ")";
		case VER_019: return "Applicazione non autorizzata all'autodeterminazione dei tributi";
		case VER_020: return "Iban di accredito non censito";
		case VER_021: return "Applicazione non autorizzata all'autodeterminazione dei tributi per il dominio indicato";
		case VER_022: return "Applicazione non autorizzata alla gestione del tributo indicato";
		case VER_023: return "Il versamento (" + this.params[1] + ") dell'applicazione (" + this.params[0] + ") ha il singolo versamento con codSingoloVersamentoEnte (" + this.params[2] + ") inviato ha un iban di accredito diverso dall'originale.";
		case WISP_000: return "Sessione di scelta sconosciuta al WISP";
		case WISP_001: return "Sessione di scelta scaduta per timeout al WISP";
		case WISP_002: return "Canale (" + this.params[1] + ") del Psp (" + this.params[0] + ") con tipo versamento (" + this.params[2] + ") scelto non presente in anagrafica Psp";
		case WISP_003: return "Il debitore non ha operato alcuna scelta sul WISP";
		case WISP_004: return "Il debitore ha scelto di pagare dopo tramite avviso di pagamento.";
		
		// aggiunti nella versione 3.0.x
		case APP_003: return "Applicazione (" + this.params[0] + ") non autorizzata a pagare il versamento (IdA2A:" + this.params[1] +", Id:"+ this.params[2] +")";
		case APP_004: return "Applicazione non autorizzata ad operare sulla transazione indicata";
		case APP_005: return "Applicazione non autorizzata per l'operazione richiesta";
		case VER_024: return "Il versamento (IdA2A:" + this.params[0] +", Id:"+ this.params[1] +") ha un numero avviso ("+ this.params[2] +") diverso dall'originale ("+ this.params[3] +").";
		case VER_025: return "Il versamento (IdA2A:" + this.params[0] + ", Id:" + this.params[1] + ") ha un numero avviso ("+this.params[4]+") gia' utilizzato dal versamento (IdA2A:" + this.params[2] + ", Id:" + this.params[3] + ").";
		case VER_026: return "Lo IUV (" + this.params[0] + ") non e' conforme alle specifiche agid, application code (" + this.params[1] + ") non valido per la stazione (" + this.params[2] + ")";
		case VER_027: return "Lo IUV (" + this.params[0] + ") non e' conforme alle specifiche agid, segregaton code (" + this.params[1] + ") non valido per il dominio (" + this.params[2] + ")";
		case VER_028: return "Il prefisso IUV di tipo numerico generato ("+this.params[0]+") per il versamento (IdA2A:"+this.params[1]+" Id:"+this.params[2]+") non e' valido rispetto alla configurazione prevista per il dominio ("+this.params[3]+", prefix:"+this.params[4]+").";
		case VER_029: return "Il prefisso IUV di tipo ISO11694 generato ("+this.params[0]+") per il versamento (IdA2A:"+this.params[1]+" Id:"+this.params[2]+") non e' valido rispetto alla configurazione prevista per il dominio ("+this.params[3]+", prefix:"+this.params[4]+").";
		case VER_030: return "Lo IUV generato (" + this.params[0] + ") non rispetta il pattern previsto dall'applicazione (IdA2A:"+this.params[1]+").";
		
		default:
			break;
		}
		
		return "";
	}

	public String getCausa() {
		return this.causa;
	}

	public void setCausa(String causa) {
		this.causa = causa;
	}

	public FaultBean getFaultBean() {
		return this.faultBean;
	}

	public void setFaultBean(FaultBean faultBean) {
		this.faultBean = faultBean;
	}
	
	public String getCodEsitoV3() {
		switch (this.codEsito) {
		case NDP_000: return "PAA_NODO_INDISPONIBILE";
		default:
			return this.codEsito.name();
		}
	}
	
	public String getMessageV3() {
		switch (this.codEsito) {
		case NDP_000: 
			if(super.getCause() != null && super.getCause() instanceof ClientException)
				return super.getCause().getMessage();
			else 
				return super.getMessage();
		default:
			return this.getMessage();
		}
	}
	
	public int getStatusCode() {
		switch (this.getCategoria()) {
		case PAGOPA: return 502;
		default:
			return 500;
		}
	}
	
	
	public String getDescrizioneEsito() {
		switch (this.codEsito) {
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
		case NDP_000: return "Servizi pagoPA non disponibili.";
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
		case PAG_012: return "Richiesta non valida"; 
		case PAG_013: return "Richiesta non valida"; 
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
		case VER_014: return "Impossibile aggiornare la pendenza scaduta.";
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
		
		// aggiunti nella versione 3.0.x
		case APP_003: return "Richiesta non valida";
		case APP_004: return "Richiesta non valida";
		case APP_005: return "Richiesta non valida";
		case VER_024: return "Richiesta non valida";
		case VER_025: return "Richiesta non valida";
		case VER_026: return "Richiesta non valida";
		case VER_027: return "Richiesta non valida";
		case VER_028: return "Richiesta non valida";
		case VER_029: return "Richiesta non valida";
		case VER_030: return "Richiesta non valida";
		}
		
		return "";
	}
	
	public enum CategoriaEnum {
	    AUTORIZZAZIONE, RICHIESTA, OPERAZIONE, PAGOPA, INTERNO;
	}
	
	public CategoriaEnum getCategoria() {
		switch (this.codEsito) {
		case INTERNAL: return CategoriaEnum.INTERNO;
		case AUT_000: 	
		case AUT_001: 
		case AUT_002: return CategoriaEnum.AUTORIZZAZIONE;
		case NDP_000: 
		case NDP_001: return CategoriaEnum.PAGOPA; 
		case VER_014: return CategoriaEnum.OPERAZIONE;
		default: return CategoriaEnum.RICHIESTA;
		}
	}
	
	public TipoNota getTipoNota() {
		switch (this.codEsito) {
		case OK: return TipoNota.SISTEMA_INFO;
		default: return TipoNota.SISTEMA_FATAL;
		}
	}
	
	public String getMessageNota() {
		switch (this.codEsito) {
		case NDP_001: 
			return "[" + this.faultBean.getFaultCode() + "] " + this.faultBean.getFaultString() + (this.faultBean.getDescription() != null ? ": " + this.faultBean.getDescription() : "");
		default:
			return this.getMessageV3();
		}
	}

	public GpResponse getWsResponse(GpResponse response, String codMsgDiagnostico, Logger log) {
		if(this.getFaultBean() == null) {
			response.setMittente(Mittente.GOV_PAY);
			response.setCodEsito(this.getCodEsito() != null ? this.getCodEsito().toString() : "");
			response.setDescrizioneEsito(this.getDescrizioneEsito() != null ? this.getDescrizioneEsito() : "");
			response.setDettaglioEsito(this.getMessage());
			this.log(log);
			GpThreadLocal.get().log(codMsgDiagnostico, response.getCodEsito().toString(), response.getDescrizioneEsito(), response.getDettaglioEsito());
			
		} else {
			if(this.faultBean.getId().contains("NodoDeiPagamentiSPC")) 
				response.setMittente(Mittente.NODO_DEI_PAGAMENTI_SPC);
			else 
				response.setMittente(Mittente.PSP);
			response.setCodEsito(this.faultBean.getFaultCode() != null ? this.faultBean.getFaultCode() : "");
			response.setDescrizioneEsito(this.faultBean.getFaultString() != null ? this.faultBean.getFaultString() : "");
			response.setDettaglioEsito(this.faultBean.getDescription());
		}
		return response;
	}

	public Object getParam() {
		return this.param;
	}

	public void setParam(Object param) {
		this.param = param;
	}

}
