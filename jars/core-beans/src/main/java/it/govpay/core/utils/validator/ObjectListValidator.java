package it.govpay.core.utils.validator;

import java.text.MessageFormat;
import java.util.List;

import it.govpay.core.exceptions.ValidationException;

public class ObjectListValidator {

	private String fieldName;
	private List<? extends IValidable> fieldValue;

	protected ObjectListValidator(String fieldName, List<? extends IValidable> fieldValue) { 
		this.fieldName = fieldName;
		this.fieldValue = fieldValue;
	}

	public ObjectListValidator notNull() throws ValidationException {
		if(this.fieldValue == null) {
			throw new ValidationException(MessageFormat.format(CostantiValidazione.OBJECT_LIST_VALIDATOR_ERROR_MSG_IL_CAMPO_0_NON_DEVE_ESSERE_VUOTO, this.fieldName));
		}
		return this;
	}
	
	public ObjectListValidator notEmpty() throws ValidationException {
		if(this.fieldValue != null && this.fieldValue.isEmpty()) {
			throw new ValidationException(MessageFormat.format(CostantiValidazione.OBJECT_LIST_VALIDATOR_ERROR_MSG_IL_CAMPO_0_NON_DEVE_ESSERE_VUOTO, this.fieldName));
		}
		return this;
	}
	
	public ObjectListValidator isNull() throws ValidationException {
		if(this.fieldValue != null && !this.fieldValue.isEmpty()) {
			throw new ValidationException(MessageFormat.format(CostantiValidazione.OBJECT_LIST_VALIDATOR_ERROR_MSG_IL_CAMPO_0_DEVE_ESSERE_VUOTO, this.fieldName));
		}
		return this;
	}
	
	public ObjectListValidator minItems(long min) throws ValidationException {
		if(this.fieldValue.size() < min) {
			throw new ValidationException(MessageFormat.format(CostantiValidazione.OBJECT_LIST_VALIDATOR_ERROR_MSG_IL_CAMPO_0_DEVE_AVERE_ALMENO_1_ELEMENTI, this.fieldName, min));
		}
		return this;
	}
	
	public ObjectListValidator maxItems(long max) throws ValidationException {
		if(this.fieldValue.size() > max) {
			throw new ValidationException(MessageFormat.format(CostantiValidazione.OBJECT_LIST_VALIDATOR_ERROR_MSG_IL_CAMPO_0_DEVE_AVERE_MASSIMO_1_ELEMENTI, this.fieldName, max));
		}
		return this;
	}
	
	public ObjectListValidator validateObjects() throws ValidationException {
		if(this.fieldValue != null) {
			for (int i = 0; i < this.fieldValue.size(); i++) {
				IValidable v = this.fieldValue.get(i);
				if(v == null)
					throw new ValidationException(MessageFormat.format(CostantiValidazione.OBJECT_LIST_VALIDATOR_ERROR_MSG_L_ELEMENTO_IN_POSIZIONE_0_DEL_CAMPO_1_E_VUOTO, (i),
							this.fieldName));
					
				v.validate();
			}
		}
		return this;
	}
}
