package it.govpay.core.utils.validator;

import org.openspcoop2.generic_project.exception.ValidationException;

public class BooleanValidator {

	protected String fieldName;
	protected Boolean fieldValue;

	protected BooleanValidator(String fieldName, Boolean fieldValue) { 
		this.fieldName = fieldName;
		this.fieldValue = fieldValue;
	}

	public BooleanValidator notNull() throws ValidationException {
		if(fieldValue == null) {
			throw new ValidationException("Il campo " + fieldName + " non deve essere vuoto.");
		}
		return this;
	}
	
	public BooleanValidator isNull() throws ValidationException {
		if(fieldValue != null) {
			throw new ValidationException("Il campo " + fieldName + " deve essere vuoto.");
		}
		return this;
	}

}
