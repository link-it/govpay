package it.govpay.core.utils.validator;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAmount;
import java.util.Date;

import it.govpay.core.exceptions.ValidationException;

public class DateValidator {

	private String fieldName;
	private LocalDate fieldValue;
	private DateTimeFormatter formatter;

	protected DateValidator(String fieldName, Date fieldValue) { 
		this.fieldName = fieldName;
		this.fieldValue = fieldValue == null ? null : fieldValue.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		this.formatter = DateTimeFormatter.ISO_LOCAL_DATE;
	}
	
	public DateValidator isValid() throws ValidationException {
		if(this.fieldValue != null) {
			try {
				this.formatter.format(this.fieldValue);
			}catch(DateTimeException e) {
				throw new ValidationException("Il campo " + this.fieldName + " non contiene una data valida.");
			}
		}
		return this;
	}

	public DateValidator notNull() throws ValidationException {
		if(this.fieldValue == null) {
			throw new ValidationException("Il campo " + this.fieldName + " non deve essere vuoto.");
		}
		return this;
	}
	
	public DateValidator isNull() throws ValidationException {
		if(this.fieldValue != null) {
			throw new ValidationException("Il campo " + this.fieldName + " deve essere vuoto.");
		}
		return this;
	}
	
	public DateValidator after(LocalDate date) throws ValidationException {
		if(this.fieldValue != null && this.fieldValue.isBefore(date)) {
			throw new ValidationException("Il campo " + this.fieldName + " deve avere una data successiva a " + this.formatter.format(date) + ".");
		}
		return this;
	}
	
	public DateValidator before(LocalDate date) throws ValidationException {
		if(this.fieldValue != null && this.fieldValue.isAfter(date)) {
			throw new ValidationException("Il campo " + this.fieldName + " deve avere una data precedente a " + this.formatter.format(date) + ".");
		}
		return this;
	}
	
	public DateValidator inside(TemporalAmount temporalAmount) throws ValidationException {
		if(this.fieldValue != null && (this.fieldValue.isAfter(LocalDate.now().plus(temporalAmount)) || this.fieldValue.isBefore(LocalDate.now().minus(temporalAmount)))) {
			throw new ValidationException("Il campo " + this.fieldName + " deve avere una data entro " + temporalAmount + ".");
		}
		return this;
	}
	
	public DateValidator insideDays(long days) throws ValidationException {
		if(this.fieldValue != null && (this.fieldValue.isAfter(LocalDate.now().plusDays(days)) || this.fieldValue.isBefore(LocalDate.now().minusDays(days)))) {
			throw new ValidationException("Il campo " + this.fieldName + " deve avere una data entro " + days + " giorni .");
		}
		return this;
	}
	
	public DateValidator outside(TemporalAmount temporalAmount) throws ValidationException {
		if(this.fieldValue != null && !(this.fieldValue.isAfter(LocalDate.now().plus(temporalAmount)) || this.fieldValue.isBefore(LocalDate.now().minus(temporalAmount)))) {
			throw new ValidationException("Il campo " + this.fieldName + " deve avere una data oltre " + temporalAmount + ".");
		}
		return this;
	}
	
}
