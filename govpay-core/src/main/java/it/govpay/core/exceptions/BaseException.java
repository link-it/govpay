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

public abstract class BaseException extends Exception {
	
	transient private static final long serialVersionUID = 1L;
	private String code;
	private String message;
	private String details;
	
	public BaseException(String message) {
		super();
		this.code = this.getClass().getName();
		this.message = message;
	}
	
	public BaseException(String message, Throwable cause) {
		super(cause);
		this.code = this.getClass().getSimpleName();
		this.message = message;
	}
	
	public BaseException(String message, String subCode, String description) {
		this(message);
		this.details = "(#"+subCode+") " + description;
		this.code = subCode;
	}
	
	public BaseException(String message, String subCode, String description, Throwable cause) {
		this(message, cause);
		this.details = "(#"+subCode+") " + description;
		this.code = subCode;
	}

	public String getCode() {
		return this.code;
	}

	@Override
	public String getMessage() {
		return this.message;
	}

	public String getDetails() {
		return this.details;
	}

	public abstract int getTransportErrorCode();
	
}
