package it.govpay.pagopa.v2.utils;

import it.govpay.pagopa.v2.exception.ValidationException;

public class IuvUtils {
	
	public static String toIuv(String numeroAvviso) throws ValidationException {
		if(numeroAvviso == null)
			return null;

		if(numeroAvviso.length() != 18)
			throw new ValidationException("Numero Avviso [" + numeroAvviso + "] fornito non valido: Consentite 18 cifre trovate ["+numeroAvviso.length()+"].");

		try {
			Long.parseLong(numeroAvviso);
		}catch(Exception e) {
			throw new ValidationException("Numero Avviso [" + numeroAvviso + "] fornito non valido: non e' in formato numerico.");
		}

		if(numeroAvviso.startsWith("0")) // '0' + applicationCode(2) + ref(13) + check(2)
			return numeroAvviso.substring(3);
		else if(numeroAvviso.startsWith("1")) // '1' + reference(17)
			return numeroAvviso.substring(1);
		else if(numeroAvviso.startsWith("2")) // '2' + ref(15) + check(2)
			return numeroAvviso.substring(1);
		else if(numeroAvviso.startsWith("3")) // '3' + segregationCode(2) +  ref(13) + check(2) 
			return numeroAvviso.substring(1);
		else 
			throw new ValidationException("Numero Avviso [" + numeroAvviso + "] fornito non valido: prima cifra non e' [0|1|2|3]");
	}
}