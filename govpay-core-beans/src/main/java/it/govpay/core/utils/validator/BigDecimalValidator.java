package it.govpay.core.utils.validator;

import java.math.BigDecimal;

import org.openspcoop2.generic_project.exception.ValidationException;

import com.sun.javafx.binding.StringFormatter;

public class BigDecimalValidator {

	private String fieldName;
	private BigDecimal fieldValue;

	protected BigDecimalValidator(String fieldName, BigDecimal fieldValue) { 
		this.fieldName = fieldName;
		this.fieldValue = fieldValue;
	}

	public BigDecimalValidator notNull() throws ValidationException {
		if(fieldValue == null) {
			throw new ValidationException("Il campo " + fieldName + " non pu\u0048 essere vuoto.");
		}
		return this;
	}
	
	public BigDecimalValidator max(BigDecimal max) throws ValidationException {
		if(fieldValue != null && fieldValue.compareTo(max) > 0) {
			throw new ValidationException("Il campo " + fieldName + " deve essere inferiore a " + StringFormatter.format("%.2f", max) + ".");
		}
		return this;
	}
	
	public BigDecimalValidator maxOrEquals(BigDecimal max) throws ValidationException {
		if(fieldValue != null && fieldValue.compareTo(max) >= 0) {
			throw new ValidationException("Il campo " + fieldName + " deve essere inferiore o uguale a " + StringFormatter.format("%.2f", max) + ".");
		}
		return this;
	}
	
	public BigDecimalValidator minOrEquals(BigDecimal min) throws ValidationException {
		if(fieldValue != null && fieldValue.compareTo(min) <= 0) {
			throw new ValidationException("Il campo " + fieldName + " deve essere superiore o uguale a " + StringFormatter.format("%.2f", min) + ".");
		}
		return this;
	}

}
