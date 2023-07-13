package it.govpay.core.utils.validator;

import java.math.BigInteger;
import java.text.MessageFormat;

import it.govpay.core.exceptions.ValidationException;


public class BigIntegerValidator {
	
	private String fieldName;
	private BigInteger fieldValue;

	protected BigIntegerValidator(String fieldName, BigInteger fieldValue) { 
		this.fieldName = fieldName;
		this.fieldValue = fieldValue;
	}
	
	public BigIntegerValidator notNull() throws ValidationException {
		if(this.fieldValue == null) {
			throw new ValidationException(MessageFormat.format(CostantiValidazione.BIG_INTEGER_VALIDATOR_ERROR_MSG_IL_CAMPO_0_NON_DEVE_ESSERE_VUOTO, this.fieldName));
		}
		return this;
	}
	
	public BigIntegerValidator isNull() throws ValidationException {
		if(this.fieldValue != null) {
			throw new ValidationException(MessageFormat.format(CostantiValidazione.BIG_INTEGER_VALIDATOR_ERROR_MSG_IL_CAMPO_0_DEVE_ESSERE_VUOTO, this.fieldName));
		}
		return this;
	}
	
	public BigIntegerValidator max(BigInteger max) throws ValidationException {
		if(this.fieldValue != null && this.fieldValue.compareTo(max) > 0) {
			throw new ValidationException(MessageFormat.format(CostantiValidazione.BIG_INTEGER_VALIDATOR_ERROR_MSG_IL_CAMPO_0_DEVE_ESSERE_INFERIORE_A_1, this.fieldName, max));
		}
		return this;
	}
	
	public BigIntegerValidator min(BigInteger min) throws ValidationException {
		if(this.fieldValue != null && this.fieldValue.compareTo(min) < 0) {
			throw new ValidationException(MessageFormat.format(CostantiValidazione.BIG_INTEGER_VALIDATOR_ERROR_MSG_IL_CAMPO_0_DEVE_ESSERE_SUPERIORE_A_1, this.fieldName, min));
		}
		return this;
	}

	
	public BigIntegerValidator maxOrEquals(BigInteger max) throws ValidationException {
		if(this.fieldValue != null && this.fieldValue.compareTo(max) >= 0) {
			throw new ValidationException(MessageFormat.format(CostantiValidazione.BIG_INTEGER_VALIDATOR_ERROR_MSG_IL_CAMPO_0_DEVE_ESSERE_INFERIORE_O_UGUALE_A_1, this.fieldName, max));
		}
		return this;
	}
	
	public BigIntegerValidator minOrEquals(BigInteger min) throws ValidationException {
		if(this.fieldValue != null && this.fieldValue.compareTo(min) <= 0) {
			throw new ValidationException(MessageFormat.format(CostantiValidazione.BIG_INTEGER_VALIDATOR_ERROR_MSG_IL_CAMPO_0_DEVE_ESSERE_SUPERIORE_O_UGUALE_A_1, this.fieldName, min));
		}
		return this;
	}

}
