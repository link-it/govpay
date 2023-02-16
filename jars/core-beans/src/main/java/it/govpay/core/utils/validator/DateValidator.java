package it.govpay.core.utils.validator;

import java.text.MessageFormat;
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
				throw new ValidationException(MessageFormat.format(CostantiValidazione.DATE_VALIDATOR_ERROR_MSG_IL_CAMPO_0_NON_CONTIENE_UNA_DATA_VALIDA, this.fieldName));
			}
		}
		return this;
	}

	public DateValidator notNull() throws ValidationException {
		if(this.fieldValue == null) {
			throw new ValidationException(MessageFormat.format(CostantiValidazione.DATE_VALIDATOR_ERROR_MSG_IL_CAMPO_0_NON_DEVE_ESSERE_VUOTO, this.fieldName));
		}
		return this;
	}
	
	public DateValidator isNull() throws ValidationException {
		if(this.fieldValue != null) {
			throw new ValidationException(MessageFormat.format(CostantiValidazione.DATE_VALIDATOR_ERROR_MSG_IL_CAMPO_0_DEVE_ESSERE_VUOTO, this.fieldName));
		}
		return this;
	}
	
	public DateValidator after(LocalDate date) throws ValidationException {
		if(this.fieldValue != null && this.fieldValue.isBefore(date)) {
			throw new ValidationException(MessageFormat.format(CostantiValidazione.DATE_VALIDATOR_ERROR_MSG_IL_CAMPO_0_DEVE_AVERE_UNA_DATA_SUCCESSIVA_A_1, this.fieldName, this.formatter.format(date)));
		}
		return this;
	}
	
	public DateValidator before(LocalDate date) throws ValidationException {
		if(this.fieldValue != null && this.fieldValue.isAfter(date)) {
			throw new ValidationException(MessageFormat.format(CostantiValidazione.DATE_VALIDATOR_ERROR_MSG_IL_CAMPO_0_DEVE_AVERE_UNA_DATA_PRECEDENTE_A_1, this.fieldName, this.formatter.format(date)));
		}
		return this;
	}
	
	public DateValidator inside(TemporalAmount temporalAmount) throws ValidationException {
		if(this.fieldValue != null && (this.fieldValue.isAfter(LocalDate.now().plus(temporalAmount)) || this.fieldValue.isBefore(LocalDate.now().minus(temporalAmount)))) {
			throw new ValidationException(MessageFormat.format(CostantiValidazione.DATE_VALIDATOR_ERROR_MSG_IL_CAMPO_0_DEVE_AVERE_UNA_DATA_ENTRO_1, this.fieldName, temporalAmount));
		}
		return this;
	}
	
	public DateValidator insideDays(long days) throws ValidationException {
		if(this.fieldValue != null && (this.fieldValue.isAfter(LocalDate.now().plusDays(days)) || this.fieldValue.isBefore(LocalDate.now().minusDays(days)))) {
			throw new ValidationException(MessageFormat.format(CostantiValidazione.DATE_VALIDATOR_ERROR_MSG_IL_CAMPO_0_DEVE_AVERE_UNA_DATA_ENTRO_1_GIORNI, this.fieldName, days));
		}
		return this;
	}
	
	public DateValidator outside(TemporalAmount temporalAmount) throws ValidationException {
		if(this.fieldValue != null && !(this.fieldValue.isAfter(LocalDate.now().plus(temporalAmount)) || this.fieldValue.isBefore(LocalDate.now().minus(temporalAmount)))) {
			throw new ValidationException(MessageFormat.format(CostantiValidazione.DATE_VALIDATOR_ERROR_MSG_IL_CAMPO_0_DEVE_AVERE_UNA_DATA_OLTRE_1, this.fieldName, temporalAmount));
		}
		return this;
	}
	
}
