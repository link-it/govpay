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

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.MessageFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import it.govpay.core.exceptions.ValidationException;

public class StringValidator {

	protected String fieldName;
	protected String fieldValue;

	protected StringValidator(String fieldName, String fieldValue) { 
		this.fieldName = fieldName;
		this.fieldValue = fieldValue;
	}

	public StringValidator notNull() throws ValidationException {
		if(this.fieldValue == null) {
			throw new ValidationException(MessageFormat.format(CostantiValidazione.STRING_VALIDATOR_ERROR_MSG_IL_CAMPO_0_NON_DEVE_ESSERE_VUOTO, this.fieldName));
		}
		return this;
	}
	
	public StringValidator isNull() throws ValidationException {
		if(this.fieldValue != null) {
			throw new ValidationException(MessageFormat.format(CostantiValidazione.STRING_VALIDATOR_ERROR_MSG_IL_CAMPO_0_DEVE_ESSERE_VUOTO, this.fieldName));
		}
		return this;
	}

	public StringValidator minLength(int length) throws ValidationException {
		if(this.fieldValue != null && this.fieldValue.length() < length) {
			throw new ValidationException(MessageFormat.format(CostantiValidazione.STRING_VALIDATOR_ERROR_MSG_IL_VALORE_0_DEL_CAMPO_1_NON_RISPETTA_LA_LUNGHEZZA_MINIMA_DI_2_CARATTERI,
					this.fieldValue, this.fieldName, length));

		}
		return this;
	}

	public StringValidator maxLength(int length) throws ValidationException {
		if(this.fieldValue != null && this.fieldValue.length() > length) {
			throw new ValidationException(MessageFormat.format(CostantiValidazione.STRING_VALIDATOR_ERROR_MSG_IL_VALORE_0_DEL_CAMPO_1_NON_RISPETTA_LA_LUNGHEZZA_MASSIMA_DI_2_CARATTERI,
					this.fieldValue, this.fieldName, length));

		}
		return this;
	}
	
	public StringValidator length(int length) throws ValidationException {
		if(this.fieldValue != null && this.fieldValue.length() != length) {
			throw new ValidationException(MessageFormat.format(CostantiValidazione.STRING_VALIDATOR_ERROR_MSG_IL_VALORE_0_DEL_CAMPO_1_NON_RISPETTA_LA_LUNGHEZZA_DI_2_CARATTERI,
					this.fieldValue, this.fieldName, length));
		}
		return this;
	}

	public StringValidator pattern(String pattern) throws ValidationException {
		if(this.fieldValue != null) {
			Pattern p = Pattern.compile(pattern);
			Matcher m = p.matcher(this.fieldValue);
			if(!m.matches())
				throw new ValidationException(MessageFormat.format(CostantiValidazione.STRING_VALIDATOR_ERROR_MSG_IL_VALORE_0_DEL_CAMPO_1_NON_RISPETTA_IL_PATTERN_RICHIESTO_2,
						this.fieldValue, this.fieldName, pattern));
		}
		return this;
	}

	public StringValidator isUrl() throws ValidationException {
		if(this.fieldValue != null) {
			try {
				new URI(this.fieldValue).toURL();
			} catch (MalformedURLException | URISyntaxException e) {
				throw new ValidationException(MessageFormat.format(CostantiValidazione.STRING_VALIDATOR_ERROR_MSG_IL_CAMPO_0_NON_CONTIENE_UNA_URL_VALIDA, this.fieldName));
			}
		}
		return this;
	}
}
