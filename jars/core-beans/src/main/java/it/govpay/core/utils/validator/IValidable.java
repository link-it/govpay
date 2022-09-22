package it.govpay.core.utils.validator;

import it.govpay.core.exceptions.ValidationException;

public interface IValidable {
	 public void validate() throws ValidationException;
}
