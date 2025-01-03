/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2025 Link.it srl (http://www.link.it).
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

/**
 * Contiene la definizione di una eccezione lanciata in caso di lettura di una Pendenza non valida sintatticamente.
 * 
 *  @author Pintori Giuliano (pintori@link.it)
 *
 */
public class VersamentoNonValidoException extends VersamentoException {

	private static final long serialVersionUID = 1L;

	public VersamentoNonValidoException() {
		super();
	}
	
	public VersamentoNonValidoException(String codApplicazione, String codVersamentoEnte, String bundlekey, String codUnivocoDebitore,
			String codDominio, String iuv, String message) {
		super(codApplicazione, codVersamentoEnte, bundlekey, codUnivocoDebitore, codDominio, iuv, message);
	}

}
