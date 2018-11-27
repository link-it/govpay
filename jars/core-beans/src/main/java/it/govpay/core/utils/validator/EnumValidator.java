package it.govpay.core.utils.validator;

import org.openspcoop2.utils.json.ValidationException;

public class EnumValidator {

	private String fieldName;
	private Enum<?> fieldValue;

	protected EnumValidator(String fieldName, Enum<?> fieldValue) { 
		this.fieldName = fieldName;
		this.fieldValue = fieldValue;
	}

	public EnumValidator notNull() throws ValidationException {
		if(this.fieldValue == null) {
			throw new ValidationException("Il campo " + this.fieldName + " non deve essere vuoto.");
		}
		return this;
	}
	
	public EnumValidator isNull() throws ValidationException {
		if(this.fieldValue != null) {
			throw new ValidationException("Il campo " + this.fieldName + " deve essere vuoto.");
		}
		return this;
	}
}
