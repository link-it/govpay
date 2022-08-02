package it.govpay.gde.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class InternalException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public InternalException(Throwable e) {
		super("Errore durante l'esecuzione dell'operazione: " + e.getLocalizedMessage() , e);
	}
}

