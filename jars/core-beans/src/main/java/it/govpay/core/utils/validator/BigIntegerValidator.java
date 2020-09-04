package it.govpay.core.utils.validator;

import java.math.BigInteger;

import org.openspcoop2.utils.json.ValidationException;


public class BigIntegerValidator {

	private String fieldName;
	private BigInteger fieldValue;

	protected BigIntegerValidator(String fieldName, BigInteger fieldValue) { 
		this.fieldName = fieldName;
		this.fieldValue = fieldValue;
	}
	
	public BigIntegerValidator notNull() throws ValidationException {
		if(this.fieldValue == null) {
			throw new ValidationException("Il campo " + this.fieldName + " non deve essere vuoto.");
		}
		return this;
	}
	
	public BigIntegerValidator isNull() throws ValidationException {
		if(this.fieldValue != null) {
			throw new ValidationException("Il campo " + this.fieldName + " deve essere vuoto.");
		}
		return this;
	}
	
	public BigIntegerValidator max(BigInteger max) throws ValidationException {
		if(this.fieldValue != null && this.fieldValue.compareTo(max) > 0) {
			throw new ValidationException("Il campo " + this.fieldName + " deve essere inferiore a " + max + ".");
		}
		return this;
	}
	
	public BigIntegerValidator min(BigInteger min) throws ValidationException {
		if(this.fieldValue != null && this.fieldValue.compareTo(min) < 0) {
			throw new ValidationException("Il campo " + this.fieldName + " deve essere superiore a " + min + ".");
		}
		return this;
	}

	
	public BigIntegerValidator maxOrEquals(BigInteger max) throws ValidationException {
		if(this.fieldValue != null && this.fieldValue.compareTo(max) >= 0) {
			throw new ValidationException("Il campo " + this.fieldName + " deve essere inferiore o uguale a " + max + ".");
		}
		return this;
	}
	
	public BigIntegerValidator minOrEquals(BigInteger min) throws ValidationException {
		if(this.fieldValue != null && this.fieldValue.compareTo(min) <= 0) {
			throw new ValidationException("Il campo " + this.fieldName + " deve essere superiore o uguale a " + min + ".");
		}
		return this;
	}

}
