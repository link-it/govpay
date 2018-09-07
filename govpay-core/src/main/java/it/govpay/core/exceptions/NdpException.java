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

public class NdpException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public enum FaultNodo {
		PPT_AUTENTICAZIONE,
		PPT_AUTORIZZAZIONE,
		PPT_CANALE_DISABILITATO,
		PPT_CANALE_ERR_PARAM_PAG_IMM,
		PPT_CANALE_ERRORE,
		PPT_CANALE_ERRORE_RESPONSE,
		PPT_CANALE_INDISPONIBILE,
		PPT_CANALE_IRRAGGIUNGIBILE,
		PPT_CANALE_NONRISOLVIBILE,
		PPT_CANALE_SCONOSCIUTO,
		PPT_CANALE_SERVIZIO_NONATTIVO,
		PPT_CANALE_TIMEOUT,
		PPT_CODIFICA_PSP_SCONOSCIUTA,
		PPT_DOMINIO_DISABILITATO,
		PPT_DOMINIO_SCONOSCIUTO,
		PPT_ERRORE_EMESSO_DA_PAA,
		PPT_ERRORE_FORMATO_BUSTA_FIRMATA,
		PPT_FIRMA_INDISPONIBILE,
		PPT_IBAN_NON_CENSITO,
		PPT_ID_CARRELLO_DUPLICATO,
		PPT_ID_FLUSSO_SCONOSCIUTO,
		PPT_OPER_NON_STORNABILE,
		PPT_PSP_DISABILITATO,
		PPT_PSP_SCONOSCIUTO,
		PPT_RPT_DUPLICATA,
		PPT_RPT_SCONOSCIUTA,
		PPT_RT_NONDISPONIBILE,
		PPT_RT_SCONOSCIUTA,
		PPT_SEMANTICA,
		PPT_SINTASSI_EXTRAXSD,
		PPT_SINTASSI_XSD,
		PPT_STAZIONE_INT_PA_DISABILITATA,
		PPT_STAZIONE_INT_PA_IRRAGGIUNGIBILE,
		PPT_STAZIONE_INT_PA_SCONOSCIUTA,
		PPT_STAZIONE_INT_PA_SERVIZIO_NONATTIVO,
		PPT_SUPERAMENTOSOGLIA,
		PPT_SYSTEM_ERROR,
		PPT_TIPOFIRMA_SCONOSCIUTO,
		PPT_WISP_SESSIONE_SCONOSCIUTA,
		PPT_WISP_TIMEOUT_RECUPERO_SCELTA;
	}
	
	public enum FaultPa {
		PAA_ATTIVA_RPT_IMPORTO_NON_VALIDO("L'importo del pagamento in attesa non \u00E8 congruente con il dato indicato dal PSP"),
		PAA_ER_DUPLICATA("La ER \u00E8 gi\u00E0 stata accettata."),
		PAA_ERRORE_FORMATO_BUSTA_FIRMATA("Formato busta di firma errato o non corrispondente al tipoFirma."),
		PAA_FIRMA_ERRATA("Errore di firma."),
		PAA_FIRMA_INDISPONIBILE("Impossibile firmare."),
		PAA_ID_DOMINIO_ERRATO("La PAA non corrisponde al Dominio indicato."),
		PAA_ID_INTERMEDIARIO_ERRATO("Identificativo intermediario non corrispondente."),
		PAA_PAGAMENTO_ANNULLATO("Pagamento in attesa risulta annullato all'Ente Creditore."),
		PAA_PAGAMENTO_DUPLICATO("Pagamento in attesa risulta concluso all'Ente Creditore."),
		PAA_PAGAMENTO_IN_CORSO("Pagamento in attesa risulta in corso all'Ente Creditore."),
		PAA_PAGAMENTO_SCADUTO("Pagamento in attesa risulta scaduto all'Ente Creditore."),
		PAA_PAGAMENTO_SCONOSCIUTO("Pagamento in attesa risulta sconosciuto all’Ente Creditore."),
		PAA_RPT_SCONOSCIUTA("La RPT risulta sconosciuta."),
		PAA_RT_DUPLICATA("La RT \u00E8 gi\u00E0 stata accettata."),
		PAA_SEMANTICA("Errore semantico."),
		PAA_SINTASSI_EXTRAXSD("Errore di sintassi extra XSD."),
		PAA_SINTASSI_XSD("Errore di sintassi XSD."),
		PAA_STAZIONE_INT_ERRATA("Stazione intermediario non corrispondente."),
		PAA_SYSTEM_ERROR("Errore generico."),
		PAA_TIPOFIRMA_SCONOSCIUTO("Il campo tipoFirma non corrisponde ad alcun valore previsto.");
		
		private String fault;
		
		private FaultPa(String faultString) {
			this.fault = faultString;
		}
		
		public String getFaultString() {
			return this.fault;
		}
	}
	
	private String faultCode;
	private String faultString;
	private String descrizione;
	private String codDominio;
	
	public NdpException(FaultPa fault, String codDominio) {
		this(fault.name(), fault.getFaultString(), null, codDominio, null);
	}
	public NdpException(FaultPa fault, String description, String codDominio) {
		this(fault.name(), fault.getFaultString(), description, codDominio, null);
	}
	
	public NdpException(FaultPa fault, String description, String codDominio, Exception e) {
		this(fault.name(), fault.getFaultString(), description, codDominio, e);
	}
	
	public NdpException(String faultCode, String faultString, String description, String codDominio, Exception e) {
		super(e);
		this.setFaultCode(faultCode);
		this.setFaultString(faultString);
		this.setDescrizione(description);
		this.setCodDominio(codDominio);
	}
	
	public String getDescrizione() {
		return this.descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public String getCodDominio() {
		return this.codDominio;
	}

	public void setCodDominio(String codDominio) {
		this.codDominio = codDominio;
	}

	public String getFaultCode() {
		return this.faultCode;
	}

	public void setFaultCode(String faultCode) {
		this.faultCode = faultCode;
	}

	public String getFaultString() {
		return this.faultString;
	}

	public void setFaultString(String faultString) {
		this.faultString = faultString;
	}
	
	
	
}
