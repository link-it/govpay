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
package it.govpay.core.utils.client.exception;

/**
 * Contiene la definizione di una eccezione lanciata dai client di integrazione verso gli applicativ esterni.
 * 
 * @author Giuliano Pintori (pintori@link.it)
 *
 */
public class ClientException extends Exception {
	private static final long serialVersionUID = 1L;
	private Integer responseCode = null;
	private byte[] responseContent = null;

	public ClientException(String message, Exception e, Integer responseCode) {
		this(message, e, responseCode, null);
	}

	public ClientException(Exception e, Integer responseCode) {
		this(e, responseCode, null);
	}

	public ClientException(String string, Integer responseCode) {
		this(string, responseCode, null);
	}

	public ClientException(String message, Exception e) {
		super(message, e);
	}

	public ClientException(Exception e) {
		super(e);
	}

	public ClientException(String string) {
		super(string);
	}

	public ClientException(Exception e, Integer responseCode, byte[] responseContent) {
		super(e);
		this.responseCode = responseCode;
		this.responseContent = responseContent;
	}

	public ClientException(String string, Integer responseCode, byte[] responseContent) {
		super(string);
		this.responseCode = responseCode;
		this.responseContent = responseContent;
	}

	public ClientException(String message, Exception e, Integer responseCode, byte[] responseContent) {
		super(message, e);
		this.responseCode = responseCode;
		this.responseContent = responseContent;
	}

	public Integer getResponseCode() {
		return this.responseCode;
	}

	public byte[] getResponseContent () {
		return this.responseContent;
	}
}
