package it.govpay.core.utils.validator;

import java.text.MessageFormat;

import it.govpay.core.exceptions.ValidationException;

public class EnumValidator {

	private String fieldName;
	private Enum<?> fieldValue;

	protected EnumValidator(String fieldName, Enum<?> fieldValue) { 
		this.fieldName = fieldName;
		this.fieldValue = fieldValue;
	}

	public EnumValidator notNull() throws ValidationException {
		if(this.fieldValue == null) {
			throw new ValidationException(MessageFormat.format(CostantiValidazione.ENUM_VALIDATOR_ERROR_MSG_IL_CAMPO_0_NON_DEVE_ESSERE_VUOTO, this.fieldName));
		}
		return this;
	}
	
	public EnumValidator isNull() throws ValidationException {
		if(this.fieldValue != null) {
			throw new ValidationException(MessageFormat.format(CostantiValidazione.ENUM_VALIDATOR_ERROR_MSG_IL_CAMPO_0_DEVE_ESSERE_VUOTO, this.fieldName));
		}
		return this;
	}
}
