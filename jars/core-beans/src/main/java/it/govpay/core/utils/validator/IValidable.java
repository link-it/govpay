package it.govpay.core.utils.validator;

import org.openspcoop2.utils.json.ValidationException;

public interface IValidable {
	 public void validate() throws ValidationException;
}
