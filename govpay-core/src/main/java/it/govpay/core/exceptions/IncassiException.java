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
	public static final String faultString = "Richiesta di incasso non processabile";
	
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
		
		
		private String faultSubCode;
		private String description;
		
		FaultType(String faultSubCode, String description) {
			this.faultSubCode = faultSubCode;
			this.description = description;
		}
		
		public String getFaultSubCode(){
			return faultSubCode;
		}
		
		public String getDescription(){
			return description;
		}
	}
	
	public IncassiException(FaultType faultType, String description) {
		super(faultString, faultType.getFaultSubCode(), description);
	}

	@Override
	public int getTransportErrorCode() {
		return 422;
	}
	
}
