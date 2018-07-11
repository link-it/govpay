package it.govpay.core.utils.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openspcoop2.generic_project.exception.ValidationException;

public class StringValidator {

	protected String fieldName;
	protected String fieldValue;

	protected StringValidator(String fieldName, String fieldValue) { 
		this.fieldName = fieldName;
		this.fieldValue = fieldValue;
	}

	public StringValidator notNull() throws ValidationException {
		if(fieldValue == null) {
			throw new ValidationException("Il campo " + fieldName + " non pu\u0048 essere vuoto.");
		}
		return this;
	}
	
	public StringValidator isNull() throws ValidationException {
		if(fieldValue != null) {
			throw new ValidationException("Il campo " + fieldName + " deve essere vuoto.");
		}
		return this;
	}

	public StringValidator minLength(int length) throws ValidationException {
		if(fieldValue != null && fieldValue.length() < length) {
			throw new ValidationException("La lunghezza del campo " + fieldName + " non pu\u0048 essere inferiore a " + length + " caratteri.");
		}
		return this;
	}

	public StringValidator maxLength(int length) throws ValidationException {
		if(fieldValue != null && fieldValue.length() > length) {
			throw new ValidationException("La lunghezza del campo " + fieldName + " non pu\u0048 essere superiore a " + length + " caratteri.");
		}
		return this;
	}
	
	public StringValidator length(int length) throws ValidationException {
		if(fieldValue != null && fieldValue.length() != length) {
			throw new ValidationException("La lunghezza del campo " + fieldName + " deve essere uguale a " + length + " caratteri.");
		}
		return this;
	}

	public StringValidator pattern(String pattern) throws ValidationException {
		if(fieldValue != null) {
			Pattern p = Pattern.compile("pattern");
			Matcher m = p.matcher(fieldValue);
			if(!m.matches())
				throw new ValidationException("Il valore del campo " + fieldName + " non rispetta il pattern richiesto: " + pattern + "");
		}
		return this;
	}

}
