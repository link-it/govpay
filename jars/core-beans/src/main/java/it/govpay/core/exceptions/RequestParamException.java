/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2026 Link.it srl (http://www.link.it).
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

public class RequestParamException extends BaseExceptionV1 {

	private static final long serialVersionUID = 1L;
	
	public enum FaultType {
		PARAMETRO_ORDERBY_NON_VALIDO("100001","Uno dei parametri di ordinamento non e' valido.");
		
		private String faultSubCode;
		private String description;
		
		FaultType(String faultSubCode, String description) {
			this.faultSubCode = faultSubCode;
			this.description = description;
		}
		
		public String getFaultSubCode(){
			return this.faultSubCode;
		}
		
		public String getDescription(){
			return this.description;
		}
	}
	
	public RequestParamException(FaultType faultType, String details) {
		super("Errore nella valorizzazione dei parametri della richiesta", faultType.faultSubCode, details, CategoriaEnum.RICHIESTA);
	}
	
	public RequestParamException(String cause) {
		super("Errore nella valorizzazione dei parametri della richiesta", "4220000", cause, CategoriaEnum.RICHIESTA);
	}
	
	@Override
	public int getTransportErrorCode() {
		return 422;
	}

}
