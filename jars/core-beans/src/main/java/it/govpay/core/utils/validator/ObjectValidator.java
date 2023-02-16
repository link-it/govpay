package it.govpay.core.utils.validator;

import java.text.MessageFormat;

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
			throw new ValidationException(MessageFormat.format(CostantiValidazione.OBJECT_VALIDATOR_ERROR_MSG_IL_CAMPO_0_NON_DEVE_ESSERE_VUOTO, this.fieldName));
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
			throw new ValidationException(MessageFormat.format(CostantiValidazione.OBJECT_VALIDATOR_ERROR_MSG_IL_CAMPO_0_DEVE_ESSERE_VUOTO, this.fieldName));
		}
		return this;
	}
}
