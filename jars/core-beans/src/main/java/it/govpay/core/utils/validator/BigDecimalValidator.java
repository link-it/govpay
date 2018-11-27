package it.govpay.core.utils.validator;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import org.openspcoop2.utils.json.ValidationException;


public class BigDecimalValidator {

	private String fieldName;
	private BigDecimal fieldValue;
	private DecimalFormat df;

	protected BigDecimalValidator(String fieldName, BigDecimal fieldValue) { 
		this.fieldName = fieldName;
		this.fieldValue = fieldValue;
		this.df = new DecimalFormat("#0.##");
	}

	public BigDecimalValidator notNull() throws ValidationException {
		if(this.fieldValue == null) {
			throw new ValidationException("Il campo " + this.fieldName + " non deve essere vuoto.");
		}
		return this;
	}
	
	public BigDecimalValidator isNull() throws ValidationException {
		if(this.fieldValue != null) {
			throw new ValidationException("Il campo " + this.fieldName + " deve essere vuoto.");
		}
		return this;
	}
	
	public BigDecimalValidator max(BigDecimal max) throws ValidationException {
		if(this.fieldValue != null && this.fieldValue.compareTo(max) > 0) {
			throw new ValidationException("Il campo " + this.fieldName + " deve essere inferiore a " + this.df.format(max) + ".");
		}
		return this;
	}
	
	public BigDecimalValidator min(BigDecimal min) throws ValidationException {
		if(this.fieldValue != null && this.fieldValue.compareTo(min) <= 0) {
			throw new ValidationException("Il campo " + this.fieldName + " deve essere superiore a " + this.df.format(min) + ".");
		}
		return this;
	}

	
	public BigDecimalValidator maxOrEquals(BigDecimal max) throws ValidationException {
		if(this.fieldValue != null && this.fieldValue.compareTo(max) >= 0) {
			throw new ValidationException("Il campo " + this.fieldName + " deve essere inferiore o uguale a " + this.df.format(max) + ".");
		}
		return this;
	}
	
	public BigDecimalValidator minOrEquals(BigDecimal min) throws ValidationException {
		if(this.fieldValue != null && this.fieldValue.compareTo(min) <= 0) {
			throw new ValidationException("Il campo " + this.fieldName + " deve essere superiore o uguale a " + this.df.format(min) + ".");
		}
		return this;
	}

}
