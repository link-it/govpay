/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2023 Link.it srl (http://www.link.it).
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
package it.govpay.core.utils.validator;

import java.text.MessageFormat;

import it.govpay.core.exceptions.ValidationException;

public class BooleanValidator {

	protected String fieldName;
	protected Boolean fieldValue;

	protected BooleanValidator(String fieldName, Boolean fieldValue) { 
		this.fieldName = fieldName;
		this.fieldValue = fieldValue;
	}

	public BooleanValidator notNull() throws ValidationException {
		if(this.fieldValue == null) {
			throw new ValidationException(MessageFormat.format(CostantiValidazione.BOOLEAN_VALIDATOR_ERROR_MSG_IL_CAMPO_0_NON_DEVE_ESSERE_VUOTO, this.fieldName));
		}
		return this;
	}
	
	public BooleanValidator isNull() throws ValidationException {
		if(this.fieldValue != null) {
			throw new ValidationException(MessageFormat.format(CostantiValidazione.BOOLEAN_VALIDATOR_ERROR_MSG_IL_CAMPO_0_DEVE_ESSERE_VUOTO, this.fieldName));
		}
		return this;
	}

}
