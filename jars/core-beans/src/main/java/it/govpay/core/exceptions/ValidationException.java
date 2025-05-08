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

import it.govpay.core.beans.Costanti;

/**	
 * Contiene la definizione di una eccezione lanciata dalle funzioni di validazione
 *
 * @author Pintori Giuliano (pintori@link.it)
 */
public class ValidationException extends Exception {

	public ValidationException(String message, Throwable cause)	{
		super(message, cause);
	}
	
	public ValidationException(Throwable cause)	{
		super(cause);
	}
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	public ValidationException() {
		super();
	}
	public ValidationException(String msg) {
		super(msg);
	}

	public ValidationException(String fieldName, String valoreErrato, String valoriAmmessi) {
		super(Costanti.MSG_VALIDATION_EXCEPTION_CODIFICA_INESISTENTE_PER +
				fieldName + Costanti.MSG_VALIDATION_EXCEPTION_VALORE_FORNITO +
				valoreErrato + Costanti.MSG_VALIDATION_EXCEPTION_VALORI_POSSIBILI +
				valoriAmmessi + Costanti.MSG_VALIDATION_EXCEPTION_SUFFIX);
	}
}
