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
package it.govpay.core.exception;

import java.time.LocalDateTime;

import it.govpay.pagopa.v2.utils.DateUtils;

public class VersamentoScadutoException extends VersamentoException {
	
	private static final long serialVersionUID = 1L;
	
	public VersamentoScadutoException(String codApplicazione, String codVersamentoEnte, String bundlekey, String codUnivocoDebitore,
			String codDominio, String iuv, LocalDateTime dataScadenza) {
		super(codApplicazione, codVersamentoEnte, bundlekey, codUnivocoDebitore, codDominio, iuv, dataScadenza != null ? "Versamento scaduto in data " + DateUtils.newSimpleDateFormat("dd/MM/yyyy").format(dataScadenza) : null);
	}
	
	public VersamentoScadutoException(String codApplicazione, String codVersamentoEnte, String bundlekey, String codUnivocoDebitore,
			String codDominio, String iuv, String message) {
		super(codApplicazione, codVersamentoEnte, bundlekey, codUnivocoDebitore, codDominio, iuv, message);
	}

}
