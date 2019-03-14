package it.govpay.core.utils.validator;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import org.openspcoop2.utils.json.ValidationException;


public class DoubleValidator {

	private String fieldName;
	private Double fieldValue;
	private DecimalFormat df;

	protected DoubleValidator(String fieldName, Double fieldValue) { 
		this.fieldName = fieldName;
		this.fieldValue = fieldValue;
		this.df = new DecimalFormat("#0.##");
	}
	
	public DoubleValidator checkDecimalDigits() throws ValidationException {
		
		notNull(); // check not null di default
		
		String value = String.valueOf(this.fieldValue.doubleValue());
		DecimalFormatSymbols symbols = this.df.getDecimalFormatSymbols();
		
		int i = value.lastIndexOf(symbols.getDecimalSeparator());
		if(i != -1) {
//			System.out.println("Il campo " + this.fieldName + " contiene un valore non valido: " + value + " has "+value.substring(i + 1).length()+" digits after dot");
			if(value.substring(i + 1).length() > 2) {
				throw new ValidationException("Il campo " + this.fieldName + " contiene un valore non valido.");
			} 
		}
		return this;
	}

	public DoubleValidator notNull() throws ValidationException {
		if(this.fieldValue == null) {
			throw new ValidationException("Il campo " + this.fieldName + " non deve essere vuoto.");
		}
		return this;
	}
	
	public DoubleValidator isNull() throws ValidationException {
		if(this.fieldValue != null) {
			throw new ValidationException("Il campo " + this.fieldName + " deve essere vuoto.");
		}
		return this;
	}
	
	public DoubleValidator max(Double max) throws ValidationException {
		if(this.fieldValue != null && this.fieldValue.compareTo(max) > 0) {
			throw new ValidationException("Il campo " + this.fieldName + " deve essere inferiore a " + this.df.format(max) + ".");
		}
		return this;
	}
	
	public DoubleValidator min(Double min) throws ValidationException {
		if(this.fieldValue != null && this.fieldValue.compareTo(min) <= 0) {
			throw new ValidationException("Il campo " + this.fieldName + " deve essere superiore a " + this.df.format(min) + ".");
		}
		return this;
	}

	
	public DoubleValidator maxOrEquals(Double max) throws ValidationException {
		if(this.fieldValue != null && this.fieldValue.compareTo(max) >= 0) {
			throw new ValidationException("Il campo " + this.fieldName + " deve essere inferiore o uguale a " + this.df.format(max) + ".");
		}
		return this;
	}
	
	public DoubleValidator minOrEquals(Double min) throws ValidationException {
		if(this.fieldValue != null && this.fieldValue.compareTo(min) <= 0) {
			throw new ValidationException("Il campo " + this.fieldName + " deve essere superiore o uguale a " + this.df.format(min) + ".");
		}
		return this;
	}

}
