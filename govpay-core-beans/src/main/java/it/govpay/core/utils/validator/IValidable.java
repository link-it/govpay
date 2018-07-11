package it.govpay.core.utils.validator;

import org.openspcoop2.generic_project.exception.ValidationException;

public interface IValidable {
	 public void validate() throws ValidationException;
}
