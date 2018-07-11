package it.govpay.core.utils.validator;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAmount;
import java.util.Date;

import org.openspcoop2.generic_project.exception.ValidationException;

public class DateValidator {

	private String fieldName;
	private LocalDate fieldValue;
	private DateTimeFormatter formatter;

	protected DateValidator(String fieldName, Date fieldValue) { 
		this.fieldName = fieldName;
		this.fieldValue = fieldValue == null ? null : fieldValue.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		this.formatter = DateTimeFormatter.ISO_LOCAL_DATE;
	}

	public DateValidator notNull() throws ValidationException {
		if(fieldValue == null) {
			throw new ValidationException("Il campo " + fieldName + " non pu\u0048 essere vuoto.");
		}
		return this;
	}
	
	public DateValidator after(LocalDate date) throws ValidationException {
		if(fieldValue != null && fieldValue.isBefore(date)) {
			throw new ValidationException("Il campo " + fieldName + " deve avere una data successiva a " + formatter.format(date) + ".");
		}
		return this;
	}
	
	public DateValidator before(LocalDate date) throws ValidationException {
		if(fieldValue != null && fieldValue.isAfter(date)) {
			throw new ValidationException("Il campo " + fieldName + " deve avere una data precedente a " + formatter.format(date) + ".");
		}
		return this;
	}
	
	public DateValidator inside(TemporalAmount temporalAmount) throws ValidationException {
		if(fieldValue != null && (fieldValue.isAfter(LocalDate.now().plus(temporalAmount)) || fieldValue.isBefore(LocalDate.now().minus(temporalAmount)))) {
			throw new ValidationException("Il campo " + fieldName + " deve avere una data entro " + temporalAmount + ".");
		}
		return this;
	}
	
	public DateValidator outside(TemporalAmount temporalAmount) throws ValidationException {
		if(fieldValue != null && !(fieldValue.isAfter(LocalDate.now().plus(temporalAmount)) || fieldValue.isBefore(LocalDate.now().minus(temporalAmount)))) {
			throw new ValidationException("Il campo " + fieldName + " deve avere una data oltre " + temporalAmount + ".");
		}
		return this;
	}
	
}
