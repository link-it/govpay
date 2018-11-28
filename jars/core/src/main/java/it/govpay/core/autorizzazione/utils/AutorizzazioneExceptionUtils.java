package it.govpay.core.autorizzazione.utils;

import org.springframework.security.core.Authentication;

import it.govpay.core.exceptions.NotAuthenticatedException;
import it.govpay.core.exceptions.NotAuthorizedException;

public class AutorizzazioneExceptionUtils {

	public static NotAuthorizedException toNotAuthorizedException(Authentication authentication) {
		StringBuilder sb = new StringBuilder();
		String utenza = AutorizzazioneUtils.getPrincipal(authentication);
		if(utenza != null)
			sb.append("Utenza [").append(utenza).append("] non autorizzata.");
		else
			sb.append("Credenziali non fornite.");
		return new NotAuthorizedException(sb.toString());
	}
	
	public static NotAuthenticatedException toNotAuthenticatedException(Authentication authentication) {
		StringBuilder sb = new StringBuilder();
		String utenza = AutorizzazioneUtils.getPrincipal(authentication);
		if(utenza != null)
			sb.append("Utenza [").append(utenza).append("] non autorizzata.");
		else
			sb.append("Credenziali non fornite.");
		return new NotAuthenticatedException(sb.toString());
	}
}
