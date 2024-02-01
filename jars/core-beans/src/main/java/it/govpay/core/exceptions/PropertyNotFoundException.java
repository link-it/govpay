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

/**	
 * Contiene la definizione di una eccezione lanciata quando non viene trovata una property obbligatoria nel file di configurazione
 *
 * @author Pintori Giuliano (pintori@link.it)
 */
public class PropertyNotFoundException extends Exception {
	
	private static final long serialVersionUID = 1L;

	public PropertyNotFoundException(String message, Throwable cause)	{
		super(message, cause);
	}
	
	public PropertyNotFoundException(Throwable cause)	{
		super(cause);
	}

	public PropertyNotFoundException() {
		super();
	}
	public PropertyNotFoundException(String msg) {
		super(msg);
	}
}
