/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2024 Link.it srl (http://www.link.it).
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

import java.io.Serializable;

import it.govpay.core.beans.EsitoOperazione;
import it.govpay.core.utils.client.exception.ClientException;
import it.govpay.model.Evento.EsitoEvento;

/**
 * Eccezione lanciata in caso di errore durante i processi interni al sistema.
 * Il dettaglio dell'errore rilevato viene specificato attraverso il field codEsito di tipo {@link EsitoOperazione}.  
 * 
 * @author Pintori Giuliano (pintori@link.it)
 *
 */
public class GovPayException extends Exception {

	private static final String DESCRIZIONE_ESITO_RICHIESTA_RIFIUTATA_DAL_NODO_DEI_PAGAMENTI = "Richiesta rifiutata dal Nodo dei Pagamenti";
	private static final String DESCRIZIONE_ESITO_SERVIZI_PAGO_PA_NON_DISPONIBILI = "Servizi pagoPA non disponibili.";
	private static final String DESCRIZIONE_ESITO_IMPOSSIBILE_ACQUISIRE_I_DATI_AGGIORNATI_DELLA_PENDENZA = "Impossibile acquisire i dati aggiornati della pendenza.";
	private static final String DESCRIZIONE_ESITO_ERRORE_DURANTE_LA_GENERAZIONE_DEL_PROMEMORIA_SCADENZA_PAGAMENTO = "Errore durante la generazione del promemoria scadenza pagamento";
	private static final String DESCRIZIONE_ESITO_ERRORE_DURANTE_LA_GENERAZIONE_DEL_PROMEMORIA_RICEVUTA_PAGAMENTO = "Errore durante la generazione del promemoria ricevuta pagamento";
	private static final String DESCRIZIONE_ESITO_ERRORE_DURANTE_LA_VALIDAZIONE = "Errore durante la validazione";
	private static final String DESCRIZIONE_ESITO_ERRORE_DURANTE_LA_TRASFORMAZIONE = "Errore durante la trasformazione";
	private static final String DESCRIZIONE_ESITO_ERRORE_DURANTE_LA_GENERAZIONE_DEL_PROMEMORIA_AVVISO_PAGAMENTO = "Errore durante la generazione del promemoria avviso pagamento";
	private static final String DESCRIZIONE_ESITO_ERRORE_WISP = "Errore WISP";
	private static final String DESCRIZIONE_ESITO_OPERAZIONE_NON_AUTORIZZATA = "Operazione non autorizzata";
	private static final String DESCRIZIONE_ESITO_RICHIESTA_NON_VALIDA = "Richiesta non valida";
	private static final String DESCRIZIONE_ESITO_ERRORE_INTERNO = "Errore interno";
	private static final String DESCRIZIONE_ESITO_OPERAZIONE_COMPLETATA_CON_SUCCESSO = "Operazione completata con successo";
	private static final long serialVersionUID = 1L;
	private String[] params;
	private EsitoOperazione codEsito;
	private String causa;
	private FaultBean faultBean;
	private final Serializable param;
		
	private GovPayException() {this.param = null;}
	
	public GovPayException(FaultBean faultBean) {
		super();
		this.param = null;
		this.faultBean = faultBean;
		this.setCodEsitoOperazione(EsitoOperazione.NDP_001);
	}
	
	public GovPayException(String causa, EsitoOperazione codEsito, String ... params) {
		this.params = params;
		this.setCodEsitoOperazione(codEsito);
		this.setCausa(causa);
		this.param = null;
	}
	
	public GovPayException(String causa, EsitoOperazione codEsito, Throwable e, String ... params) {
		super(e);
		this.params = params;
		this.setCodEsitoOperazione(codEsito);
		this.setCausa(causa);
		this.param = null;
	}
	
	public GovPayException(Throwable e, String descrizione) {
		super(e);
		this.params = new String[1];
		this.params[0] = descrizione;
		this.setCodEsitoOperazione(EsitoOperazione.INTERNAL);
		this.setCausa(org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace(e));
		this.param = null;
	}
	
	public GovPayException(EsitoOperazione codEsito, String ... params) {
		this.params = params;
		this.setCodEsitoOperazione(codEsito);
		this.param = null;
	}
	
	public GovPayException(EsitoOperazione codEsito, Throwable e, String ... params) {
		super(e);
		this.params = params;
		this.setCodEsitoOperazione(codEsito);
		this.setCausa(org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace(e));
		this.param = null;
	}
	
	public GovPayException(Throwable e) {
		super(e);
		this.setCodEsitoOperazione(EsitoOperazione.INTERNAL);
		this.param = null;
	}
	
	public GovPayException(GovPayException e, Serializable param) {
		super(e);
		this.param = param;
		this.causa = e.causa;
		this.codEsito = e.codEsito;
		this.faultBean = e.faultBean;
		this.params = e.params;
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
	
	@Override
	public String getMessage() {
		switch (this.codEsito) {
		case OK: return DESCRIZIONE_ESITO_OPERAZIONE_COMPLETATA_CON_SUCCESSO;
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
		case PAG_000: return "Le pendenze di una richiesta di pagamento devono afferire alla stessa stazione";	
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
		case VER_000: return "La pendenza (IdA2A:" + this.params[0] + ", Id:" + this.params[1] + ") non pagabile ad iniziativa PSP"; 
		case VER_001: return "La pendenza (IdA2A:" + this.params[0] + ", Id:" + this.params[1] + ") presenta il codSingoloVersamentoEnte (" + this.params[2] + ") piu' di una volta";
		case VER_002: return "La pendenza (IdA2A:" + this.params[0] + ", Id:" + this.params[1] + ") ha un importo totale (" + this.params[3] + ") diverso dalla somma dei singoli importi (" + this.params[2] + ")";
		case VER_003: return "La pendenza (IdA2A:" + this.params[0] + ", Id:" + this.params[1] + ") e' in uno stato che non consente l'aggiornamento (" + this.params[2] + ")";
		case VER_004: return "La pendenza (IdA2A:" + this.params[0] + ", Id:" + this.params[1] + ") presenta un beneficiario (" + this.params[2] + " / "+ this.params[3] + ") diverso da quello originale (" + this.params[4] + " / "+ this.params[5] +")";
		case VER_005: return "La pendenza (IdA2A:" + this.params[0] + ", Id:" + this.params[1] + ") inviata ha un numero di voci (" + this.params[2] + ") diverso da quello originale (" + this.params[3] + ")";
		case VER_006: return "La pendenza (IdA2A:" + this.params[0] + ", Id:" + this.params[1] + ") inviata non contiene una voce con codSingoloVersamentoEnte (" + this.params[2] + ") presente nell'originale";
		case VER_007: return "La pendenza (IdA2A:" + this.params[0] + ", Id:" + this.params[1] + ") ha il singolo versamento con codSingoloVersamentoEnte (" + this.params[2] + ") inviato ha un tipo tributo (" + this.params[3] + ") diverso dall'originale (" + this.params[4] + ")";
		case VER_008: return "La pendenza non esiste.";
		case VER_009: return "La pendenza (IdA2A:" + this.params[0] + ", Id:" + this.params[1] + ") e' in uno stato che non consente l'annullamento (" + this.params[2] + ")";
		case VER_010: return "La verifica della pendenza ha dato esito PAA_PAGAMENTO_SCADUTO";
		case VER_011: return "La verifica della pendenza ha dato esito PAA_PAGAMENTO_SCONOSCIUTO";
		case VER_012: return "La verifica della pendenza ha dato esito PAA_PAGAMENTO_DUPLICATO";
		case VER_013: return "La verifica della pendenza ha dato esito PAA_PAGAMENTO_ANNULLATO";
		case VER_014: return "La richiesta di verifica pendenza (" + this.params[0] + "/" + this.params[1] + ") per decorrenza della data validita' e' fallita: "+ this.params[2];
		case VER_015: return "La pendenza (IdA2A:" + this.params[0] + ", Id:" + this.params[1] + ") esistente e non aggiornabile se AggiornaSeEsiste impostato a false";
		case VER_016: return "La pendenza (IdA2A:" + this.params[0] + ", Id:" + this.params[1] + ") e' in uno stato che non consente la notifica di pagamento (" + this.params[2] + ")";
		case VER_017: return "Lo IUV (" + this.params[0] + ") non e' conforme alle specifiche agid";
		case VER_018: return "Lo IUV (" + this.params[0] + ") e' gia' associato al versamento (" + this.params[1] + ")";
		case VER_019: return "Applicazione non autorizzata all'autodeterminazione dei tipi pendenza";
		case VER_020: return "Iban di accredito (" + this.params[1] + ") non censito per il dominio (" + this.params[0] + ")";
		case VER_021: return "Applicazione non autorizzata all'autodeterminazione dei tipi pendenza per il dominio indicato";
		case VER_022: return "Applicazione non autorizzata alla gestione del tipo pendenza indicato";
		case VER_023: return "La pendenza (IdA2A:" + this.params[0] + ", Id:" + this.params[1] + ") ha la voce (" + this.params[2] + ") con un iban di accredito diverso dall'originale.";
		case WISP_000: return "Sessione di scelta sconosciuta al WISP";
		case WISP_001: return "Sessione di scelta scaduta per timeout al WISP";
		case WISP_002: return "Canale (" + this.params[1] + ") del Psp (" + this.params[0] + ") con tipo versamento (" + this.params[2] + ") scelto non presente in anagrafica Psp";
		case WISP_003: return "Il debitore non ha operato alcuna scelta sul WISP";
		case WISP_004: return "Il debitore ha scelto di pagare dopo tramite avviso di pagamento.";
		
		// aggiunti nella versione 3.0.x
		case APP_003: return "Applicazione (" + this.params[0] + ") non autorizzata a pagare la pendenza (IdA2A:" + this.params[1] +", Id:"+ this.params[2] +")";
		case APP_004: return "Applicazione non autorizzata ad operare sulla transazione indicata";
		case APP_005: return "Applicazione non autorizzata per l'operazione richiesta";
		case VER_024: return "La pendenza (IdA2A:" + this.params[0] + ", Id:" + this.params[1] + ") ha un numero avviso ("+ this.params[2] +") diverso dall'originale ("+ this.params[3] +").";
		case VER_025: return "La pendenza (IdA2A:" + this.params[0] + ", Id:" + this.params[1] + ") ha un numero avviso ("+this.params[4]+") gia' utilizzato (IdA2A:" + this.params[2] + ", Id:" + this.params[3] + ").";
		case VER_026: return "Lo IUV (" + this.params[0] + ") non e' conforme alle specifiche agid, application code (" + this.params[1] + ") non valido per la stazione (" + this.params[2] + ")";
		case VER_027: return "Lo IUV (" + this.params[0] + ") non e' conforme alle specifiche agid, segregaton code (" + this.params[1] + ") non valido per il dominio (" + this.params[2] + ")";
		case VER_028: return "Il prefisso IUV di tipo numerico generato ("+this.params[0]+") per la pendenza (IdA2A:"+this.params[1]+" Id:"+this.params[2]+") non e' valido rispetto alla configurazione prevista per il dominio ("+this.params[3]+", prefix:"+this.params[4]+").";
		case VER_029: return "Il prefisso IUV di tipo ISO11694 generato ("+this.params[0]+") per la pendenza (IdA2A:"+this.params[1]+" Id:"+this.params[2]+") non e' valido rispetto alla configurazione prevista per il dominio ("+this.params[3]+", prefix:"+this.params[4]+").";
		case VER_030: return "Lo IUV generato (" + this.params[0] + ") non rispetta il pattern previsto dall'applicazione (IdA2A:"+this.params[1]+", pattern: "+this.params[2]+").";
		case CIT_001: return "L'utente cittadino (" +this.params[0] + ") non e' autorizzato ad effettuare il pagamento della pendenza (IdA2A:"+this.params[1]+" Id:"+this.params[2]+") senza specificare l'entrata da pagare.";
		case CIT_002: return "L'utente cittadino (" +this.params[0] + ") non e' autorizzato ad effettuare il pagamento della pendenza (IdA2A:"+this.params[1]+" Id:"+this.params[2]+") il tipo pendenza ("+ this.params[3] +") non e' abilitato ai pagamenti spontanei.";
		case CIT_003: return "L'utente cittadino (" +this.params[0] + ") non e' autorizzato ad effettuare il pagamento della pendenza (IdA2A:"+this.params[1]+" Id:"+this.params[2]+") per il soggetto debitore ("+ this.params[3] +").";
		case CIT_004: return "L'utente cittadino (" +this.params[0] + ") non e' autorizzato ad effettuare il pagamento della pendenza (IdA2A:"+this.params[1]+" Id:"+this.params[2]+") per il soggetto versante ("+ this.params[3] +").";
		case PAG_014: return "Il pagamento (IdDominio:" + this.params[0] + ", IUV:" + this.params[1] + ", CCP:" + this.params[2] + ") non e' eseguibile poiche' risulta in corso all'Ente Creditore";
		case APP_006: return "Nessuna applicazione trovata per gestire lo IUV ("+this.params[0]+") per il dominio ("+this.params[1]+")";
		case UAN_001: return "Non e' possibile effettuare il pagamento della pendenza (IdA2A:"+this.params[0]+" Id:"+this.params[1]+") in maniera anonima senza specificare l'entrata da pagare.";
		case UAN_002: return "Non e' possibile effettuare il pagamento della pendenza (IdA2A:"+this.params[0]+" Id:"+this.params[1]+") in maniera anonima poiche' il tipo pendenza ("+ this.params[2] +") non e' abilitato ai pagamenti spontanei.";
		case TRB_001: return "Tributo (" + this.params[1] + ") disabilitato per il dominio (" + this.params[0] + ")";
		case VER_031: return "Non e' possibile indicare il numero avviso per una pendenza di tipo multivoce se una delle voci e' una Marca da Bollo Telematica.";
		case VER_032: return "Iban di accredito (" + this.params[1] + ") disabilitato per il dominio (" + this.params[0] + ")";
		case VER_033: return "Iban di appoggio (" + this.params[1] + ") non censito per il dominio (" + this.params[0] + ")";
		case VER_034: return "Iban di appoggio (" + this.params[1] + ") disabilitato per il dominio (" + this.params[0] + ")";
		case TRASFORMAZIONE: return this.params[0];
		case VAL_000: return "Impossibile caricare la factory di validazione: " + this.params[0] + ".";
		case VAL_001: return "Lo schema indicato per la validazione non e' valido: " + this.params[0] + ".";
		case VAL_002: return "Errore interno durante la validazione: " + this.params[0] + "."; 
		case VAL_003: return "La validazione del risultato della trasformazione si e' conclusa con un errore: " + this.params[0] + "."; 
		case TVR_000: return "Tipo pendenza (" + this.params[0] + ") inesistente";	
		case TVR_001: return "Tipo pendenza (" + this.params[0] + ") disabilitato";
		case TVD_000: return "Tipo pendenza (" + this.params[0] + ") del dominio (" + this.params[1] + ") inesistente";
		case TVD_001: return "Tipo pendenza (" + this.params[0] + ") del dominio (" + this.params[1] + ") disabilitato";
		case PRM_001: return "La generazione dell'oggetto per il promemoria avviso per la della pendenza (IdA2A:"+this.params[0]+" Id:"+this.params[1]+") si e' conclusa con un errore: " + this.params[2] + ".";
		case PRM_002: return "La generazione del messaggio per il promemoria avviso per la della pendenza (IdA2A:"+this.params[0]+" Id:"+this.params[1]+") si e' conclusa con un errore: " + this.params[2] + ".";
		case PRM_003: return "La generazione dell'oggetto per il promemoria ricevuta per la della pendenza (IdA2A:"+this.params[0]+" Id:"+this.params[1]+") si e' conclusa con un errore: " + this.params[2] + ".";
		case PRM_004: return "La generazione del messaggio per il promemoria ricevuta per la della pendenza (IdA2A:"+this.params[0]+" Id:"+this.params[1]+") si e' conclusa con un errore: " + this.params[2] + ".";
		case PRM_005: return "La generazione dell'oggetto per il promemoria scadenza per la della pendenza (IdA2A:"+this.params[0]+" Id:"+this.params[1]+") si e' conclusa con un errore: " + this.params[2] + ".";
		case PRM_006: return "La generazione del messaggio per il promemoria scadenza per la della pendenza (IdA2A:"+this.params[0]+" Id:"+this.params[1]+") si e' conclusa con un errore: " + this.params[2] + ".";
	
		// Aggiunti nella versione 3.5.x
		case VER_035: return "La voce ("+this.params[0]+") della pendenza (IdA2A:" + this.params[1] + ", Id:" + this.params[2] + ") ha un importo (" + this.params[3] + ") diverso dalla somma dei singoli importi definiti nella lista delle contabilita' (" + this.params[4] + ")";
		case VER_036: return "Iban di accredito (" + this.params[0] + ") non univoco all'interno del sistema.";
		case VER_037: return "Iban di appoggio (" + this.params[0] + ") non univoco all'interno del sistema.";
		case VER_038: return "La pendenza (IdA2A:"+this.params[0]+" Id:"+this.params[1]+") e' di tipo multibeneficiario non consentito per pagamenti spontanei.";
		case VER_039: return "Il carattere iniziale del numero avviso ("+ this.params[2]+") indicato per la pendenza (IdA2A:"+this.params[0]+" Id:"+this.params[1]+") non coincide con l'aux digit ("+this.params[3]+") impostato per l'Ente Creditore ("+this.params[4]+").";
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
		switch (this.codEsito) {
		case OK: return 200; // "Operazione completata con successo"
		case INTERNAL: return 500; // "Errore interno"
		case APP_000: return 400; // "Richiesta non valida"	
		case APP_001: return 400; // "Richiesta non valida"
		case APP_002: return 400; // "Richiesta non valida"
		case AUT_000: return 403; // "Operazione non autorizzata"	
		case AUT_001: return 403; // "Operazione non autorizzata"
		case AUT_002: return 403; // "Operazione non autorizzata"
		case DOM_000: return 422; // "Richiesta non valida"	
		case DOM_001: return 422; // "Richiesta non valida"
		case DOM_002: return 500; // "Richiesta non valida"	
		case DOM_003: return 500; // "Richiesta non valida"
		case NDP_000: return 502; // "Servizi pagoPA non disponibili."
		case NDP_001: return 500; // "Richiesta rifiutata dal Nodo dei Pagamenti"
		case PAG_000: return 400; // "Richiesta non valida"	
		case PAG_001: return 500; // "Richiesta non valida"	
		case PAG_002: return 500; // "Richiesta non valida"
		case PAG_003: return 500; // "Richiesta non valida"
		case PAG_004: return 500; // "Richiesta non valida"
		case PAG_005: return 500; // "Richiesta non valida"
		case PAG_006: return 422; // "Richiesta non valida"
		case PAG_007: return 422; // "Richiesta non valida"
		case PAG_008: return 422; // "Richiesta non valida" 
		case PAG_009: return 422; // "Richiesta non valida" 
		case PAG_010: return 422; // "Richiesta non valida" 
		case PAG_011: return 422; // "Richiesta non valida" 
		case PAG_012: return 422; // "Richiesta non valida" 
		case PAG_013: return 422; // "Richiesta non valida" 
		case PRT_000: return 500; // "Richiesta non valida"	
		case PRT_001: return 500; // "Richiesta non valida"
		case PRT_002: return 500; // "Richiesta non valida"
		case PRT_003: return 500; // "Richiesta non valida"
		case PRT_004: return 500; // "Richiesta non valida" 
		case PRT_005: return 500; // "Richiesta non valida"
		case PSP_000: return 500; // "Richiesta non valida"
		case PSP_001: return 500; // "Richiesta non valida"
		case RND_000: return 500; // "Richiesta non valida"
		case RND_001: return 500; // "Richiesta non valida"
		case STA_000: return 500; // "Richiesta non valida"	
		case STA_001: return 500; // "Richiesta non valida"
		case TRB_000: return 422; // "Richiesta non valida"
		case UOP_000: return 422; // "Richiesta non valida"
		case UOP_001: return 422; // "Richiesta non valida"
		case VER_000: return 422; // "Richiesta non valida"
		case VER_001: return 422; // "Richiesta non valida"
		case VER_002: return 422; // "Richiesta non valida"
		case VER_003: return 422; // "Richiesta non valida"
		case VER_004: return 422; // "Richiesta non valida"
		case VER_005: return 422; // "Richiesta non valida"
		case VER_006: return 422; // "Richiesta non valida"
		case VER_007: return 422; // "Richiesta non valida"
		case VER_008: return 422; // "Richiesta non valida"
		case VER_009: return 422; // "Richiesta non valida"
		case VER_010: return 422; // "Richiesta non valida"
		case VER_011: return 422; // "Richiesta non valida"
		case VER_012: return 422; // "Richiesta non valida"
		case VER_013: return 422; // "Richiesta non valida"
		case VER_014: return 422; // "Impossibile aggiornare la pendenza scaduta."
		case VER_015: return 422; // "Richiesta non valida"
		case VER_016: return 422; // "Richiesta non valida"
		case VER_017: return 422; // "Richiesta non valida"
		case VER_018: return 422; // "Richiesta non valida"
		case VER_019: return 422; // "Richiesta non valida"
		case VER_020: return 422; // "Richiesta non valida"
		case VER_021: return 422; // "Richiesta non valida"
		case VER_022: return 422; // "Richiesta non valida"
		case VER_023: return 422; // "Richiesta non valida"
		case WISP_000: return 500; // "Errore WISP"
		case WISP_001: return 500; // "Errore WISP"
		case WISP_002: return 500; //  "Errore WISP"
		case WISP_003: return 500; // "Errore WISP"
		case WISP_004: return 500; // "Errore WISP"
		
		// Aggiunti nella versione 3.0.x
		case APP_003: return 403; // "Richiesta non valida"
		case APP_004: return 403; // "Richiesta non valida"
		case APP_005: return 403; // "Richiesta non valida"
		case VER_024: return 422; // "Richiesta non valida"
		case VER_025: return 422; // "Richiesta non valida"
		case VER_026: return 422; // "Richiesta non valida"
		case VER_027: return 422; // "Richiesta non valida"
		case VER_028: return 422; // "Richiesta non valida"
		case VER_029: return 422; // "Richiesta non valida"
		case VER_030: return 422; // "Richiesta non valida"
		case CIT_001: return 422; // "Richiesta non valida"
		case CIT_002: return 422; // "Richiesta non valida"
		case CIT_003: return 422; // "Richiesta non valida"
		case CIT_004: return 422; // "Richiesta non valida"
		case PAG_014: return 422; // "Richiesta non valida"
		case APP_006: return 422; // "Richiesta non valida"
		case UAN_001: return 422; // "Richiesta non valida"
		case UAN_002: return 422; // "Richiesta non valida"
		case TRB_001: return 422; // "Richiesta non valida"
		case VER_031: return 422; // "Richiesta non valida"
		case VER_032: return 422; // "Richiesta non valida"
		case VER_033: return 422; // "Richiesta non valida"
		case VER_034: return 422; // "Richiesta non valida"
		
		// Aggiunti nella versione 3.1.x
		case TRASFORMAZIONE: return 500; // "Errore interno"
		case VAL_000: return 500; // "Errore interno"
		case VAL_001: return 500; // "Errore interno"
		case VAL_002: return 500; // "Errore interno"
		case VAL_003: return 500; // "Errore interno"
		case TVD_000: return 422; // "Richiesta non valida"
		case TVD_001: return 422; // "Richiesta non valida"
		case TVR_000: return 422; // "Richiesta non valida"
		case TVR_001: return 422; // "Richiesta non valida"
		case PRM_001: return 500; // "Errore interno"
		case PRM_002: return 500; // "Errore interno"
		case PRM_003: return 500; // "Errore interno"
		case PRM_004: return 500; // "Errore interno"
		case PRM_005: return 500; // "Errore interno"
		case PRM_006: return 500; // "Errore interno"
		
		// Aggiunti nella versione 3.5.x
		case VER_035: return 422; // "Richiesta non valida"
		case VER_036: return 422; // "Richiesta non valida"
		case VER_037: return 422; // "Richiesta non valida"
		case VER_038: return 422; // "Richiesta non valida"
		case VER_039: return 422; // "Richiesta non valida"
		}
		
		return 500;
	}
	
	
	public String getDescrizioneEsito() {
		switch (this.codEsito) {
		case OK: return DESCRIZIONE_ESITO_OPERAZIONE_COMPLETATA_CON_SUCCESSO;
		case INTERNAL: return DESCRIZIONE_ESITO_ERRORE_INTERNO;
		case APP_000: return DESCRIZIONE_ESITO_RICHIESTA_NON_VALIDA;	
		case APP_001: return DESCRIZIONE_ESITO_RICHIESTA_NON_VALIDA;
		case APP_002: return DESCRIZIONE_ESITO_RICHIESTA_NON_VALIDA;
		case AUT_000: return DESCRIZIONE_ESITO_OPERAZIONE_NON_AUTORIZZATA;	
		case AUT_001: return DESCRIZIONE_ESITO_OPERAZIONE_NON_AUTORIZZATA;
		case AUT_002: return DESCRIZIONE_ESITO_OPERAZIONE_NON_AUTORIZZATA;
		case DOM_000: return DESCRIZIONE_ESITO_RICHIESTA_NON_VALIDA;	
		case DOM_001: return DESCRIZIONE_ESITO_RICHIESTA_NON_VALIDA;
		case DOM_002: return DESCRIZIONE_ESITO_RICHIESTA_NON_VALIDA;	
		case DOM_003: return DESCRIZIONE_ESITO_RICHIESTA_NON_VALIDA;
		case NDP_000: return DESCRIZIONE_ESITO_SERVIZI_PAGO_PA_NON_DISPONIBILI;
		case NDP_001: return DESCRIZIONE_ESITO_RICHIESTA_RIFIUTATA_DAL_NODO_DEI_PAGAMENTI;
		case PAG_000: return DESCRIZIONE_ESITO_RICHIESTA_NON_VALIDA;	
		case PAG_001: return DESCRIZIONE_ESITO_RICHIESTA_NON_VALIDA;	
		case PAG_002: return DESCRIZIONE_ESITO_RICHIESTA_NON_VALIDA;
		case PAG_003: return DESCRIZIONE_ESITO_RICHIESTA_NON_VALIDA;
		case PAG_004: return DESCRIZIONE_ESITO_RICHIESTA_NON_VALIDA;
		case PAG_005: return DESCRIZIONE_ESITO_RICHIESTA_NON_VALIDA;
		case PAG_006: return DESCRIZIONE_ESITO_RICHIESTA_NON_VALIDA;
		case PAG_007: return DESCRIZIONE_ESITO_RICHIESTA_NON_VALIDA;
		case PAG_008: return DESCRIZIONE_ESITO_RICHIESTA_NON_VALIDA; 
		case PAG_009: return DESCRIZIONE_ESITO_RICHIESTA_NON_VALIDA; 
		case PAG_010: return DESCRIZIONE_ESITO_RICHIESTA_NON_VALIDA; 
		case PAG_011: return DESCRIZIONE_ESITO_RICHIESTA_NON_VALIDA; 
		case PAG_012: return DESCRIZIONE_ESITO_RICHIESTA_NON_VALIDA; 
		case PAG_013: return DESCRIZIONE_ESITO_RICHIESTA_NON_VALIDA; 
		case PRT_000: return DESCRIZIONE_ESITO_RICHIESTA_NON_VALIDA;	
		case PRT_001: return DESCRIZIONE_ESITO_RICHIESTA_NON_VALIDA;
		case PRT_002: return DESCRIZIONE_ESITO_RICHIESTA_NON_VALIDA;
		case PRT_003: return DESCRIZIONE_ESITO_RICHIESTA_NON_VALIDA;
		case PRT_004: return DESCRIZIONE_ESITO_RICHIESTA_NON_VALIDA; 
		case PRT_005: return DESCRIZIONE_ESITO_RICHIESTA_NON_VALIDA;
		case PSP_000: return DESCRIZIONE_ESITO_RICHIESTA_NON_VALIDA;
		case PSP_001: return DESCRIZIONE_ESITO_RICHIESTA_NON_VALIDA;
		case RND_000: return DESCRIZIONE_ESITO_RICHIESTA_NON_VALIDA;
		case RND_001: return DESCRIZIONE_ESITO_RICHIESTA_NON_VALIDA;
		case STA_000: return DESCRIZIONE_ESITO_RICHIESTA_NON_VALIDA;	
		case STA_001: return DESCRIZIONE_ESITO_RICHIESTA_NON_VALIDA;
		case TRB_000: return DESCRIZIONE_ESITO_RICHIESTA_NON_VALIDA;
		case UOP_000: return DESCRIZIONE_ESITO_RICHIESTA_NON_VALIDA;
		case UOP_001: return DESCRIZIONE_ESITO_RICHIESTA_NON_VALIDA;
		case VER_000: return DESCRIZIONE_ESITO_RICHIESTA_NON_VALIDA;
		case VER_001: return DESCRIZIONE_ESITO_RICHIESTA_NON_VALIDA;
		case VER_002: return DESCRIZIONE_ESITO_RICHIESTA_NON_VALIDA;
		case VER_003: return DESCRIZIONE_ESITO_RICHIESTA_NON_VALIDA;
		case VER_004: return DESCRIZIONE_ESITO_RICHIESTA_NON_VALIDA;
		case VER_005: return DESCRIZIONE_ESITO_RICHIESTA_NON_VALIDA;
		case VER_006: return DESCRIZIONE_ESITO_RICHIESTA_NON_VALIDA;
		case VER_007: return DESCRIZIONE_ESITO_RICHIESTA_NON_VALIDA;
		case VER_008: return DESCRIZIONE_ESITO_RICHIESTA_NON_VALIDA;
		case VER_009: return DESCRIZIONE_ESITO_RICHIESTA_NON_VALIDA;
		case VER_010: return DESCRIZIONE_ESITO_RICHIESTA_NON_VALIDA;
		case VER_011: return DESCRIZIONE_ESITO_RICHIESTA_NON_VALIDA;
		case VER_012: return DESCRIZIONE_ESITO_RICHIESTA_NON_VALIDA;
		case VER_013: return DESCRIZIONE_ESITO_RICHIESTA_NON_VALIDA;
		case VER_014: return DESCRIZIONE_ESITO_IMPOSSIBILE_ACQUISIRE_I_DATI_AGGIORNATI_DELLA_PENDENZA;
		case VER_015: return DESCRIZIONE_ESITO_RICHIESTA_NON_VALIDA;
		case VER_016: return DESCRIZIONE_ESITO_RICHIESTA_NON_VALIDA;
		case VER_017: return DESCRIZIONE_ESITO_RICHIESTA_NON_VALIDA;
		case VER_018: return DESCRIZIONE_ESITO_RICHIESTA_NON_VALIDA;
		case VER_019: return DESCRIZIONE_ESITO_RICHIESTA_NON_VALIDA;
		case VER_020: return DESCRIZIONE_ESITO_RICHIESTA_NON_VALIDA;
		case VER_021: return DESCRIZIONE_ESITO_RICHIESTA_NON_VALIDA;
		case VER_022: return DESCRIZIONE_ESITO_RICHIESTA_NON_VALIDA;
		case VER_023: return DESCRIZIONE_ESITO_RICHIESTA_NON_VALIDA;
		case WISP_000: return DESCRIZIONE_ESITO_ERRORE_WISP;
		case WISP_001: return DESCRIZIONE_ESITO_ERRORE_WISP;
		case WISP_002: return DESCRIZIONE_ESITO_ERRORE_WISP;
		case WISP_003: return DESCRIZIONE_ESITO_ERRORE_WISP;
		case WISP_004: return DESCRIZIONE_ESITO_ERRORE_WISP;
		
		// Aggiunti nella versione 3.0.x
		case APP_003: return DESCRIZIONE_ESITO_RICHIESTA_NON_VALIDA;
		case APP_004: return DESCRIZIONE_ESITO_RICHIESTA_NON_VALIDA;
		case APP_005: return DESCRIZIONE_ESITO_RICHIESTA_NON_VALIDA;
		case VER_024: return DESCRIZIONE_ESITO_RICHIESTA_NON_VALIDA;
		case VER_025: return DESCRIZIONE_ESITO_RICHIESTA_NON_VALIDA;
		case VER_026: return DESCRIZIONE_ESITO_RICHIESTA_NON_VALIDA;
		case VER_027: return DESCRIZIONE_ESITO_RICHIESTA_NON_VALIDA;
		case VER_028: return DESCRIZIONE_ESITO_RICHIESTA_NON_VALIDA;
		case VER_029: return DESCRIZIONE_ESITO_RICHIESTA_NON_VALIDA;
		case VER_030: return DESCRIZIONE_ESITO_RICHIESTA_NON_VALIDA;
		case CIT_001: return DESCRIZIONE_ESITO_RICHIESTA_NON_VALIDA;
		case CIT_002: return DESCRIZIONE_ESITO_RICHIESTA_NON_VALIDA;
		case CIT_003: return DESCRIZIONE_ESITO_RICHIESTA_NON_VALIDA;
		case CIT_004: return DESCRIZIONE_ESITO_RICHIESTA_NON_VALIDA;
		case PAG_014: return DESCRIZIONE_ESITO_RICHIESTA_NON_VALIDA;
		case APP_006: return DESCRIZIONE_ESITO_RICHIESTA_NON_VALIDA;
		case UAN_001: return DESCRIZIONE_ESITO_RICHIESTA_NON_VALIDA;
		case UAN_002: return DESCRIZIONE_ESITO_RICHIESTA_NON_VALIDA;
		case TRB_001: return DESCRIZIONE_ESITO_RICHIESTA_NON_VALIDA;	
		case VER_031: return DESCRIZIONE_ESITO_RICHIESTA_NON_VALIDA;
		case VER_032: return DESCRIZIONE_ESITO_RICHIESTA_NON_VALIDA;
		case VER_033: return DESCRIZIONE_ESITO_RICHIESTA_NON_VALIDA;
		case VER_034: return DESCRIZIONE_ESITO_RICHIESTA_NON_VALIDA;
		
		// Aggiunti nella versione 3.1.x
		case TRASFORMAZIONE: return DESCRIZIONE_ESITO_ERRORE_DURANTE_LA_TRASFORMAZIONE;
		case VAL_000: return  DESCRIZIONE_ESITO_ERRORE_DURANTE_LA_VALIDAZIONE; 
		case VAL_001: return  DESCRIZIONE_ESITO_ERRORE_DURANTE_LA_VALIDAZIONE; 
		case VAL_002: return  DESCRIZIONE_ESITO_ERRORE_DURANTE_LA_VALIDAZIONE; 
		case VAL_003: return  DESCRIZIONE_ESITO_ERRORE_DURANTE_LA_TRASFORMAZIONE; 
		case TVD_000: return DESCRIZIONE_ESITO_RICHIESTA_NON_VALIDA;
		case TVD_001: return DESCRIZIONE_ESITO_RICHIESTA_NON_VALIDA;
		case TVR_000: return DESCRIZIONE_ESITO_RICHIESTA_NON_VALIDA;
		case TVR_001: return DESCRIZIONE_ESITO_RICHIESTA_NON_VALIDA;
		case PRM_001: return  DESCRIZIONE_ESITO_ERRORE_DURANTE_LA_GENERAZIONE_DEL_PROMEMORIA_AVVISO_PAGAMENTO; 
		case PRM_002: return  DESCRIZIONE_ESITO_ERRORE_DURANTE_LA_GENERAZIONE_DEL_PROMEMORIA_AVVISO_PAGAMENTO; 
		case PRM_003: return  DESCRIZIONE_ESITO_ERRORE_DURANTE_LA_GENERAZIONE_DEL_PROMEMORIA_RICEVUTA_PAGAMENTO; 
		case PRM_004: return  DESCRIZIONE_ESITO_ERRORE_DURANTE_LA_GENERAZIONE_DEL_PROMEMORIA_RICEVUTA_PAGAMENTO; 
		case PRM_005: return  DESCRIZIONE_ESITO_ERRORE_DURANTE_LA_GENERAZIONE_DEL_PROMEMORIA_SCADENZA_PAGAMENTO; 
		case PRM_006: return  DESCRIZIONE_ESITO_ERRORE_DURANTE_LA_GENERAZIONE_DEL_PROMEMORIA_SCADENZA_PAGAMENTO; 
		
		// Aggiunti nella versione 3.5.x
		case VER_035: return DESCRIZIONE_ESITO_RICHIESTA_NON_VALIDA;
		case VER_036: return DESCRIZIONE_ESITO_RICHIESTA_NON_VALIDA;
		case VER_037: return DESCRIZIONE_ESITO_RICHIESTA_NON_VALIDA;
		case VER_038: return DESCRIZIONE_ESITO_RICHIESTA_NON_VALIDA;
		
		// Aggiunti nella versione 3.6.x
		case VER_039: return DESCRIZIONE_ESITO_RICHIESTA_NON_VALIDA;
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
		case VAL_000:
		case VAL_001:
		case VAL_003: return CategoriaEnum.INTERNO;
		default: return CategoriaEnum.RICHIESTA;
		}
	}
	
	public EsitoEvento getTipoNota() {
		switch (this.codEsito) {
		case OK: return EsitoEvento.OK;
		default: return EsitoEvento.FAIL;
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

	public Serializable getParam() {
		return this.param;
	}
	
	public static FaultBean createFaultBean() {
		return new GovPayException().new FaultBean();
	}

	public class FaultBean implements Serializable {
		
		private static final long serialVersionUID = 1L;

	    private String faultCode;
	    private String faultString;
	    private String id;
	    private String description;
	    private Integer serial;
	    private String originalFaultCode;
	    private String originalFaultString;
	    private String originalDescription;
	    
		public String getFaultCode() {
			return faultCode;
		}
		public void setFaultCode(String faultCode) {
			this.faultCode = faultCode;
		}
		public String getFaultString() {
			return faultString;
		}
		public void setFaultString(String faultString) {
			this.faultString = faultString;
		}
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getDescription() {
			return description;
		}
		public void setDescription(String description) {
			this.description = description;
		}
		public Integer getSerial() {
			return serial;
		}
		public void setSerial(Integer serial) {
			this.serial = serial;
		}
		public String getOriginalFaultCode() {
			return originalFaultCode;
		}
		public void setOriginalFaultCode(String originalFaultCode) {
			this.originalFaultCode = originalFaultCode;
		}
		public String getOriginalFaultString() {
			return originalFaultString;
		}
		public void setOriginalFaultString(String originalFaultString) {
			this.originalFaultString = originalFaultString;
		}
		public String getOriginalDescription() {
			return originalDescription;
		}
		public void setOriginalDescription(String originalDescription) {
			this.originalDescription = originalDescription;
		}
	}
}
