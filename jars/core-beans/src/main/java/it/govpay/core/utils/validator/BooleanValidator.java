package it.govpay.core.utils.validator;

import java.text.MessageFormat;

import it.govpay.core.exceptions.ValidationException;

public class BooleanValidator {

	protected String fieldName;
	protected Boolean fieldValue;

	protected BooleanValidator(String fieldName, Boolean fieldValue) { 
		this.fieldName = fieldName;
		this.fieldValue = fieldValue;
	}

	public BooleanValidator notNull() throws ValidationException {
		if(this.fieldValue == null) {
			throw new ValidationException(MessageFormat.format(CostantiValidazione.BOOLEAN_VALIDATOR_ERROR_MSG_IL_CAMPO_0_NON_DEVE_ESSERE_VUOTO, this.fieldName));
		}
		return this;
	}
	
	public BooleanValidator isNull() throws ValidationException {
		if(this.fieldValue != null) {
			throw new ValidationException(MessageFormat.format(CostantiValidazione.BOOLEAN_VALIDATOR_ERROR_MSG_IL_CAMPO_0_DEVE_ESSERE_VUOTO, this.fieldName));
		}
		return this;
	}

}
