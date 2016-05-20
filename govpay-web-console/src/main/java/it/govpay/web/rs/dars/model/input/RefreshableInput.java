/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2016 Link.it srl (http://www.link.it).
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
package it.govpay.web.rs.dars.model.input;

import it.govpay.web.rs.dars.model.RawParamValue;

import java.net.URI;
import java.util.List;

public abstract class RefreshableInput<T> extends RefreshableParamField<T>{

	public RefreshableInput(String id, String label, int minLength, int maxLength, URI refreshUri, List<RawParamValue> values, FieldType paramType) {
		super(id, label, refreshUri, values,paramType);
		this.minLength = minLength;
		this.maxLength = maxLength;
	}

	private String errorMessage;
	private Integer minLength;
	private Integer maxLength;
	private String pattern;
	
	public void setValidation(String pattern, String errorMessage) {
		this.errorMessage = errorMessage;
		this.pattern = pattern;
	}

	public Integer getMinLength() {
		return this.minLength;
	}

	public Integer getMaxLength() {
		return this.maxLength;
	}

	public String getPattern() {
		return this.pattern;
	}

	public String getErrorMessage() {
		return this.errorMessage;
	}

}
