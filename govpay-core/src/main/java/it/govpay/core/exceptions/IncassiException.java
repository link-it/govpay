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


public class IncassiException extends BaseException {

	private static final long serialVersionUID = 1L;
	
	public enum FaultType {
		CAUSALE_NON_VALIDA("021401","Il formato della causale non e' conforme alle specifiche AgID."),
		PAGAMENTO_NON_TROVATO("021402","Pagamento non trovato."),
		PAGAMENTO_NON_IDENTIFICATO("021403","Pagamento non identificato."),
		FR_ANOMALA("021404","Rendicontazione anomala."),
		IDF_NON_TROVATO("021405","Flusso rendicontazione non trovato."),
		PAGAMENTO_GIA_INCASSATO("021406","Pagamento gia incassato."),
		IMPORTO_ERRATO("021407","Importo errato."),
		ERRORE_SINTASSI("021408","Riscontrato errore di sintassi nella richiesta."),
		DUPLICATO("021409","TRN gia' incassato."),
		DOMINIO_INESISTENTE("021410","Dominio inesistente.");
		
		
		private String faultCode;
		private String faultString;
		
		FaultType(String faultCode, String faultString) {
			this.faultCode = faultCode;
			this.faultString = faultString;
		}
		
		public String getFaultCode(){
			return faultCode;
		}
		
		public String getFaultString(){
			return faultString;
		}
	}
	
	private FaultType faultType;
	private String description;
	
	public IncassiException(FaultType faultType, String description) {
		super();
		this.faultType = faultType;
		this.description = description;
	}
	
	public String getFaultCode(){
		return faultType.getFaultCode();
	}
	
	public String getFaultString(){
		return faultType.getFaultString();
	}
	
	public String getDescription(){
		return description;
	}
}
