package it.govpay.core.utils.validator;

import org.openspcoop2.generic_project.exception.ValidationException;

public class ObjectValidator {

	private String fieldName;
	private IValidable fieldValue;

	protected ObjectValidator(String fieldName, IValidable fieldValue) { 
		this.fieldName = fieldName;
		this.fieldValue = fieldValue;
	}

	public ObjectValidator notNull() throws ValidationException {
		if(fieldValue == null) {
			throw new ValidationException("Il campo " + fieldName + " non pu\u0048 essere vuoto.");
		}
		return this;
	}
	
	public ObjectValidator validateFields() throws ValidationException {
		if(fieldValue != null) {
			fieldValue.validate();
		}
		return this;
	}

	public ObjectValidator isNull() throws ValidationException {
		if(fieldValue != null) {
			throw new ValidationException("Il campo " + fieldName + " deve essere vuoto.");
		}
		return this;
	}
}
