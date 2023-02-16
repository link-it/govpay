package it.govpay.core.utils.validator;

import java.net.MalformedURLException;
import java.net.URL;
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
				new URL(this.fieldValue);
			} catch (MalformedURLException e) {
				throw new ValidationException(MessageFormat.format(CostantiValidazione.STRING_VALIDATOR_ERROR_MSG_IL_CAMPO_0_NON_CONTIENE_UNA_URL_VALIDA, this.fieldName));
			}
		}
		return this;
	}
}
