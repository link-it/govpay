package it.govpay.core.utils.validator;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

import org.openspcoop2.utils.json.ValidationException;


public class BigDecimalValidator {

	private String fieldName;
	private BigDecimal fieldValue;
	private DecimalFormat df;

	protected BigDecimalValidator(String fieldName, BigDecimal fieldValue) { 
		this.fieldName = fieldName;
		this.fieldValue = fieldValue;
		this.df = (DecimalFormat) NumberFormat.getNumberInstance(Locale.ENGLISH);
	}
	
	public BigDecimalValidator checkDecimalDigits() throws ValidationException {
		if(this.fieldValue != null) {
			String value = String.valueOf(this.fieldValue.doubleValue());
			DecimalFormatSymbols symbols = this.df.getDecimalFormatSymbols();
			
			int i = value.lastIndexOf(symbols.getDecimalSeparator());
			if(i != -1) {
	//			System.out.println("Il campo " + this.fieldName + " contiene un valore non valido: " + value + " has "+value.substring(i + 1).length()+" digits after dot");
				if(value.substring(i + 1).length() > 2) {
					throw new ValidationException("Il campo " + this.fieldName + " contiene un valore non valido.");
				} 
			}
		}
		return this;
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
		if(this.fieldValue != null && this.fieldValue.compareTo(min) < 0) {
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
