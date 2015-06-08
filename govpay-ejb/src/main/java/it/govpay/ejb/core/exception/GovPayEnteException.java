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

import it.govpay.ejb.core.exception.GovPayException;
import it.govpay.rs.ErroreEnte;

public class GovPayEnteException extends GovPayException {

	public enum EnteFaultCode {
		 PAGAMENTO_SCONOSCIUTO,
		 PAGAMENTO_DUPLICATO,
		 PAGAMENTO_ANNULLATO,
		 PAGAMENTO_SCADUTO,
		 PAGAMENTO_IN_CORSO,
		 RS_ERRORE_RETE,
		 RS_ERRORE_HTTP,
		 RS_RISPOSTA_SCONOSCIUTA;
	}

	private static final long serialVersionUID = 1L;
	
	private EnteFaultCode faultCode;
	
	public GovPayEnteException(ErroreEnte errore) {
		super(GovPayExceptionEnum.ERRORE_ENTE_WEB, errore.getDescrizione());
		this.faultCode = EnteFaultCode.valueOf(errore.getCodiceErrore());
	}
	
	public GovPayEnteException(EnteFaultCode faultCode, String descrizione) {
		super(GovPayExceptionEnum.ERRORE_ENTE_WEB, descrizione);
		this.faultCode = faultCode;
	}
	
	public GovPayEnteException(EnteFaultCode faultCode, Throwable t) {
		super(GovPayExceptionEnum.ERRORE_ENTE_WEB, t);
		this.faultCode = faultCode;
	}

	public EnteFaultCode getFaultCode() {
		return faultCode;
	}

}
