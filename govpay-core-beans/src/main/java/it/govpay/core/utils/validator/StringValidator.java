package it.govpay.core.utils.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openspcoop2.utils.json.ValidationException;

public class StringValidator {

	protected String fieldName;
	protected String fieldValue;

	protected StringValidator(String fieldName, String fieldValue) { 
		this.fieldName = fieldName;
		this.fieldValue = fieldValue;
	}

	public StringValidator notNull() throws ValidationException {
		if(this.fieldValue == null) {
			throw new ValidationException("Il campo " + this.fieldName + " non deve essere vuoto.");
		}
		return this;
	}
	
	public StringValidator isNull() throws ValidationException {
		if(this.fieldValue != null) {
			throw new ValidationException("Il campo " + this.fieldName + " deve essere vuoto.");
		}
		return this;
	}

	public StringValidator minLength(int length) throws ValidationException {
		if(this.fieldValue != null && this.fieldValue.length() < length) {
			throw new ValidationException("Il valore [" + this.fieldValue + "] del campo " + this.fieldName + " non rispetta la lungezza minima di" + length + " caratteri.");

		}
		return this;
	}

	public StringValidator maxLength(int length) throws ValidationException {
		if(this.fieldValue != null && this.fieldValue.length() > length) {
			throw new ValidationException("Il valore [" + this.fieldValue + "] del campo " + this.fieldName + " non rispetta la lungezza massima di" + length + " caratteri.");

		}
		return this;
	}
	
	public StringValidator length(int length) throws ValidationException {
		if(this.fieldValue != null && this.fieldValue.length() != length) {
			throw new ValidationException("Il valore [" + this.fieldValue + "] del campo " + this.fieldName + " non rispetta la lunghezza di" + length + " caratteri.");
		}
		return this;
	}

	public StringValidator pattern(String pattern) throws ValidationException {
		if(this.fieldValue != null) {
			Pattern p = Pattern.compile(pattern);
			Matcher m = p.matcher(this.fieldValue);
			if(!m.matches())
				throw new ValidationException("Il valore [" + this.fieldValue + "] del campo " + this.fieldName + " non rispetta il pattern richiesto: " + pattern + "");
		}
		return this;
	}

}
