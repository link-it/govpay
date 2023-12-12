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
import java.util.List;

import it.govpay.core.exceptions.ValidationException;

public class ObjectListValidator {

	private String fieldName;
	private List<? extends IValidable> fieldValue;

	protected ObjectListValidator(String fieldName, List<? extends IValidable> fieldValue) { 
		this.fieldName = fieldName;
		this.fieldValue = fieldValue;
	}

	public ObjectListValidator notNull() throws ValidationException {
		if(this.fieldValue == null) {
			throw new ValidationException(MessageFormat.format(CostantiValidazione.OBJECT_LIST_VALIDATOR_ERROR_MSG_IL_CAMPO_0_NON_DEVE_ESSERE_VUOTO, this.fieldName));
		}
		return this;
	}
	
	public ObjectListValidator notEmpty() throws ValidationException {
		if(this.fieldValue != null && this.fieldValue.isEmpty()) {
			throw new ValidationException(MessageFormat.format(CostantiValidazione.OBJECT_LIST_VALIDATOR_ERROR_MSG_IL_CAMPO_0_NON_DEVE_ESSERE_VUOTO, this.fieldName));
		}
		return this;
	}
	
	public ObjectListValidator isNull() throws ValidationException {
		if(this.fieldValue != null && !this.fieldValue.isEmpty()) {
			throw new ValidationException(MessageFormat.format(CostantiValidazione.OBJECT_LIST_VALIDATOR_ERROR_MSG_IL_CAMPO_0_DEVE_ESSERE_VUOTO, this.fieldName));
		}
		return this;
	}
	
	public ObjectListValidator minItems(long min) throws ValidationException {
		if(this.fieldValue.size() < min) {
			throw new ValidationException(MessageFormat.format(CostantiValidazione.OBJECT_LIST_VALIDATOR_ERROR_MSG_IL_CAMPO_0_DEVE_AVERE_ALMENO_1_ELEMENTI, this.fieldName, min));
		}
		return this;
	}
	
	public ObjectListValidator maxItems(long max) throws ValidationException {
		if(this.fieldValue.size() > max) {
			throw new ValidationException(MessageFormat.format(CostantiValidazione.OBJECT_LIST_VALIDATOR_ERROR_MSG_IL_CAMPO_0_DEVE_AVERE_MASSIMO_1_ELEMENTI, this.fieldName, max));
		}
		return this;
	}
	
	public ObjectListValidator validateObjects() throws ValidationException {
		if(this.fieldValue != null) {
			for (int i = 0; i < this.fieldValue.size(); i++) {
				IValidable v = this.fieldValue.get(i);
				if(v == null)
					throw new ValidationException(MessageFormat.format(CostantiValidazione.OBJECT_LIST_VALIDATOR_ERROR_MSG_L_ELEMENTO_IN_POSIZIONE_0_DEL_CAMPO_1_E_VUOTO, (i),
							this.fieldName));
					
				v.validate();
			}
		}
		return this;
	}
}
