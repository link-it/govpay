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
package it.govpay.core.utils.validator;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.util.Locale;

import it.govpay.core.exceptions.ValidationException;


public class BigDecimalValidator {
	
	private String fieldName;
	private BigDecimal fieldValue;
	private DecimalFormat df;

	protected BigDecimalValidator(String fieldName, BigDecimal fieldValue) { 
		this.fieldName = fieldName;
		this.fieldValue = fieldValue;
		this.df = (DecimalFormat) NumberFormat.getNumberInstance(Locale.ENGLISH);
	}
	
	public BigDecimalValidator checkDecimalDigits() throws ValidationException {
		if(this.fieldValue != null && this.fieldValue.scale() > 2) {
			throw new ValidationException(MessageFormat.format(CostantiValidazione.BIG_DECIMAL_VALIDATOR_ERROR_MSG_IL_CAMPO_0_NON_DEVE_SUPERARE_LE_2_CIFRE_DECIMALI, this.fieldName));
		}
		return this;
	}

	public BigDecimalValidator notNull() throws ValidationException {
		if(this.fieldValue == null) {
			throw new ValidationException(MessageFormat.format(CostantiValidazione.BIG_DECIMAL_VALIDATOR_ERROR_MSG_IL_CAMPO_0_NON_DEVE_ESSERE_VUOTO, this.fieldName));
		}
		return this;
	}
	
	public BigDecimalValidator isNull() throws ValidationException {
		if(this.fieldValue != null) {
			throw new ValidationException(MessageFormat.format(CostantiValidazione.BIG_DECIMAL_VALIDATOR_ERROR_MSG_IL_CAMPO_0_DEVE_ESSERE_VUOTO, this.fieldName));
		}
		return this;
	}
	
	public BigDecimalValidator max(BigDecimal max) throws ValidationException {
		if(this.fieldValue != null && this.fieldValue.compareTo(max) > 0) {
			throw new ValidationException(MessageFormat.format(CostantiValidazione.BIG_DECIMAL_VALIDATOR_ERROR_MSG_IL_CAMPO_0_DEVE_ESSERE_INFERIORE_A_1, this.fieldName, this.df.format(max)));
		}
		return this;
	}
	
	public BigDecimalValidator min(BigDecimal min) throws ValidationException {
		if(this.fieldValue != null && this.fieldValue.compareTo(min) < 0) {
			throw new ValidationException(MessageFormat.format(CostantiValidazione.BIG_DECIMAL_VALIDATOR_ERROR_MSG_IL_CAMPO_0_DEVE_ESSERE_SUPERIORE_A_1, this.fieldName, this.df.format(min)));
		}
		return this;
	}

	
	public BigDecimalValidator maxOrEquals(BigDecimal max) throws ValidationException {
		if(this.fieldValue != null && this.fieldValue.compareTo(max) >= 0) {
			throw new ValidationException(MessageFormat.format(CostantiValidazione.BIG_DECIMAL_VALIDATOR_ERROR_MSG_IL_CAMPO_0_DEVE_ESSERE_INFERIORE_O_UGUALE_A_1, this.fieldName, this.df.format(max)));
		}
		return this;
	}
	
	public BigDecimalValidator minOrEquals(BigDecimal min) throws ValidationException {
		if(this.fieldValue != null && this.fieldValue.compareTo(min) <= 0) {
			throw new ValidationException(MessageFormat.format(CostantiValidazione.BIG_DECIMAL_VALIDATOR_ERROR_MSG_IL_CAMPO_0_DEVE_ESSERE_SUPERIORE_O_UGUALE_A_1, this.fieldName, this.df.format(min)));
		}
		return this;
	}
	
	public BigDecimalValidator totalDigits(int digits) throws ValidationException {
		if(this.fieldValue != null && this.fieldValue.precision() > digits) {
			throw new ValidationException(MessageFormat.format(CostantiValidazione.BIG_DECIMAL_VALIDATOR_ERROR_MSG_IL_CAMPO_0_NON_DEVE_SUPERARE_LE_1_CIFRE, this.fieldName, digits));
		}
		return this;
	}

}
