package it.govpay.core.utils.validator;

import it.govpay.core.exceptions.ValidationException;

public class ObjectValidator {

	private String fieldName;
	private IValidable fieldValue;

	protected ObjectValidator(String fieldName, IValidable fieldValue) { 
		this.fieldName = fieldName;
		this.fieldValue = fieldValue;
	}

	public ObjectValidator notNull() throws ValidationException {
		if(this.fieldValue == null) {
			throw new ValidationException("Il campo " + this.fieldName + " non deve essere vuoto.");
		}
		return this;
	}
	
	public ObjectValidator validateFields() throws ValidationException {
		if(this.fieldValue != null) {
			this.fieldValue.validate();
		}
		return this;
	}

	public ObjectValidator isNull() throws ValidationException {
		if(this.fieldValue != null) {
			throw new ValidationException("Il campo " + this.fieldName + " deve essere vuoto.");
		}
		return this;
	}
}
