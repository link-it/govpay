package it.govpay.core.utils.validator;

import java.util.List;

import org.openspcoop2.generic_project.exception.ValidationException;

public class ObjectListValidator {

	private String fieldName;
	private List<? extends IValidable> fieldValue;

	protected ObjectListValidator(String fieldName, List<? extends IValidable> fieldValue) { 
		this.fieldName = fieldName;
		this.fieldValue = fieldValue;
	}

	public ObjectListValidator notNull() throws ValidationException {
		if(this.fieldValue == null) {
			throw new ValidationException("Il campo " + this.fieldName + " non deve essere vuoto.");
		}
		return this;
	}
	
	public ObjectListValidator isNull() throws ValidationException {
		if(this.fieldValue != null) {
			throw new ValidationException("Il campo " + this.fieldName + " deve essere vuoto.");
		}
		return this;
	}
	
	public ObjectListValidator minItems(long min) throws ValidationException {
		if(this.fieldValue.size() < min) {
			throw new ValidationException("Il campo " + this.fieldName + " deve avere almeno " + min + " elementi.");
		}
		return this;
	}
	
	public ObjectListValidator maxItems(long max) throws ValidationException {
		if(this.fieldValue.size() > max) {
			throw new ValidationException("Il campo " + this.fieldName + " deve avere massimo " + max + " elementi.");
		}
		return this;
	}
	
	public ObjectListValidator validateObjects() throws ValidationException {
		if(this.fieldValue != null) {
			for(IValidable v : this.fieldValue) {
				v.validate();
			}
		}
		return this;
	}
}
