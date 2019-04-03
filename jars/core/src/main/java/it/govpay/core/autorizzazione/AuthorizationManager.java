package it.govpay.core.autorizzazione;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.utils.Utilities;
import org.openspcoop2.utils.UtilsException;
import org.springframework.security.core.Authentication;

import it.govpay.bd.model.Acl;
import it.govpay.bd.model.Utenza;
import it.govpay.core.autorizzazione.beans.GovpayLdapUserDetails;
import it.govpay.core.autorizzazione.utils.AutorizzazioneUtils;
import it.govpay.core.exceptions.NotAuthenticatedException;
import it.govpay.core.exceptions.NotAuthorizedException;
import it.govpay.model.Acl.Diritti;
import it.govpay.model.Acl.Servizio;
import it.govpay.model.Utenza.TIPO_UTENZA;

public class AuthorizationManager {

	public static final String CODICE_FISCALE_CITTADINO = "cf_cittadino";
	public static final String UTENZA_ANONIMA = "utenzaAnonima";


	public static boolean checkPrincipal(Authentication authentication, String principalToCheck) throws Exception { 
		GovpayLdapUserDetails details = AutorizzazioneUtils.getAuthenticationDetails(authentication);
		Utenza user = details.getUtenza();

		boolean authOk = false;

		if(user.isCheckSubject()) {
			// check tra subject
			authOk = AuthorizationManager.checkSubject(principalToCheck, user.getPrincipal());
		} else {
			authOk = user.getPrincipal().equals(principalToCheck);
		}

		return authOk;
	}

	public static boolean checkSubject(String principalToCheck, String principalFromRequest) throws Exception{
		boolean ok = true;

		Hashtable<String, String> hashSubject = null;
		try {
			principalToCheck = Utilities.formatSubject(principalToCheck);
		}catch(UtilsException e) {
			throw new NotFoundException("L'utenza registrata non e' un subject valido");
		}
		try {
			principalFromRequest = Utilities.formatSubject(principalFromRequest);
			hashSubject = Utilities.getSubjectIntoHashtable(principalFromRequest);
		}catch(UtilsException e) {
			throw new NotFoundException("Utenza" + principalFromRequest + "non autorizzata");
		}
		Enumeration<String> keys = hashSubject.keys();
		while(keys.hasMoreElements()){
			String key = keys.nextElement();
			String value = hashSubject.get(key);
			ok = ok && principalToCheck.contains("/"+Utilities.formatKeySubject(key)+"="+Utilities.formatValueSubject(value)+"/");
		}

		return ok;
	}

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
		String utenza =  AutorizzazioneUtils.getPrincipal(authentication);
		if(utenza != null)
			sb.append("Utenza [").append(utenza).append("] non autorizzata.");
		else
			sb.append("Credenziali non fornite.");
		return new NotAuthenticatedException(sb.toString());
	}

	public static NotAuthorizedException toNotAuthorizedException(Authentication authentication, String codDominio, String codTipoVersamento) {
		GovpayLdapUserDetails details = AutorizzazioneUtils.getAuthenticationDetails(authentication);
		StringBuilder sb = new StringBuilder();

		if(!details.isAbilitato()) {
			sb.append("Utenza [").append(details != null ? details.getIdentificativo() : "NON RICONOSCIUTA").append("] disabilitata");
			return new NotAuthorizedException(sb.toString());
		}

		sb.append("Utenza [").append(details != null ? details.getIdentificativo() : "NON RICONOSCIUTA").append("] non e' autorizzata ad accedere alla risorsa richiesta");

		boolean dominioMsg = false;
		if(StringUtils.isNotEmpty(codDominio)) {
			sb.append(" per il Dominio ["+codDominio+"]");
			dominioMsg = true;
		}

		if(StringUtils.isNotEmpty(codTipoVersamento)) {
			if(dominioMsg)
				sb.append(" e");

			sb.append(" per il TipoPendenza ["+codTipoVersamento+"]");
		}

		sb.append(".");

		return new NotAuthorizedException(sb.toString());
	}

	public static NotAuthorizedException toNotAuthorizedExceptionNessunDominioAutorizzato(Authentication authentication) {
		GovpayLdapUserDetails details = AutorizzazioneUtils.getAuthenticationDetails(authentication);
		StringBuilder sb = new StringBuilder();

		sb.append("L'utenza autenticata [").append(details != null ? details.getIdentificativo() : "NON RICONOSCIUTA").append("] non e' autorizzata ad accedere alla risorsa richiesta per nessun Dominio.");
		return new NotAuthorizedException(sb.toString());
	}

	public static NotAuthorizedException toNotAuthorizedExceptionNessunTipoVersamentoAutorizzato(Authentication authentication) {
		GovpayLdapUserDetails details = AutorizzazioneUtils.getAuthenticationDetails(authentication);
		StringBuilder sb = new StringBuilder();

		sb.append("L'utenza autenticata [").append(details != null ? details.getIdentificativo() : "NON RICONOSCIUTA").append("] non e' autorizzata ad accedere alla risorsa richiesta per nessun Tipo Pendenza.");
		return new NotAuthorizedException(sb.toString());
	}

	public static boolean isAuthorized(Authentication authentication, List<TIPO_UTENZA> tipoUtenza, List<Servizio> servizi, List<Diritti> listaDiritti) {
		GovpayLdapUserDetails details = AutorizzazioneUtils.getAuthenticationDetails(authentication);
		return isAuthorized(details.getUtenza(), tipoUtenza, servizi, listaDiritti);
	}

	public static boolean isAuthorized(Utenza utenza, List<TIPO_UTENZA> tipoUtenza, List<Servizio> servizi, List<Diritti> listaDiritti) {

		// controllo che l'utenza sia di tipo consentito
		if(!tipoUtenza.contains(utenza.getTipoUtenza()))
			return false;

		// controllo abilitazione
		if(!utenza.isAbilitato())
			return false;

		for (Servizio servizio : servizi) {
			for(Acl acl : utenza.getAcls()) {
				if(acl.getServizio().equals(servizio)) {
					for (Diritti dirittoTmp : listaDiritti) {
						if(acl.getListaDiritti().contains(dirittoTmp))
							return true;
					}
				}
			}
		}

		return false;
	}

	public static boolean isAuthorized(Authentication authentication, List<TIPO_UTENZA> tipoUtenza, List<Servizio> servizi, List<Diritti> listaDiritti, String codDominio, String codTipoVersamento) {
		GovpayLdapUserDetails details = AutorizzazioneUtils.getAuthenticationDetails(authentication);
		return isAuthorized(details.getUtenza(), tipoUtenza, servizi, listaDiritti,codDominio, codTipoVersamento);
	}

	public static boolean isAuthorized(Utenza utenza, List<TIPO_UTENZA> tipoUtenza, List<Servizio> servizi, List<Diritti> listaDiritti, String codDominio, String codTipoVersamento) {

		boolean authorized = isAuthorized(utenza,tipoUtenza, servizi, listaDiritti); 

		if(authorized) {
			if(codDominio != null) {
				authorized = authorized && isDominioAuthorized(utenza, codDominio);
			}

			if(codTipoVersamento != null) {
				authorized = authorized && isTipoVersamentoAuthorized(utenza, codTipoVersamento);
			}
		}

		return authorized;
	}
	
	public static boolean isTipoVersamentoDominioAuthorized(Authentication authentication, String codDominio, String codTipoVersamento) {
		GovpayLdapUserDetails details = AutorizzazioneUtils.getAuthenticationDetails(authentication);
		return isTipoVersamentoDominioAuthorized(details.getUtenza(), codDominio, codTipoVersamento);
	}

	public static boolean isTipoVersamentoDominioAuthorized(Utenza utenza, String codDominio, String codTipoVersamento) {
		return isDominioAuthorized(utenza, codDominio) && isTipoVersamentoAuthorized(utenza, codTipoVersamento);
	}

	public static boolean isDominioAuthorized(Authentication authentication, String codDominio) {
		GovpayLdapUserDetails details = AutorizzazioneUtils.getAuthenticationDetails(authentication);
		return isDominioAuthorized(details.getUtenza(), codDominio);
	}

	public static boolean isDominioAuthorized(Utenza utenza, String codDominio) {
		if(utenza.isAutorizzazioneDominiStar() || codDominio == null)
			return true;

		List<String> dominiAutorizzati = utenza.getIdDominio();

		if(dominiAutorizzati == null) 
			return false;

		if(!dominiAutorizzati.isEmpty())
			return dominiAutorizzati.contains(codDominio);

		return true;
	}

	public static boolean isTipoVersamentoAuthorized(Authentication authentication, String codTipoVersamento) {
		GovpayLdapUserDetails details = AutorizzazioneUtils.getAuthenticationDetails(authentication);
		return isTipoVersamentoAuthorized(details.getUtenza(), codTipoVersamento);
	}

	public static boolean isTipoVersamentoAuthorized(Utenza utenza, String codTipoVersamento) {
		if(utenza.isAutorizzazioneTipiVersamentoStar() || codTipoVersamento == null)
			return true;

		List<String> tributiAutorizzati = utenza.getIdTipoVersamento();

		if(tributiAutorizzati == null) 
			return false;

		if(!tributiAutorizzati.isEmpty())
			return tributiAutorizzati.contains(codTipoVersamento);

		return true;
	}

	public static List<Long> getIdTipiVersamentoAutorizzati(Authentication authentication) {
		GovpayLdapUserDetails details = AutorizzazioneUtils.getAuthenticationDetails(authentication);
		return getIdTipiVersamentoAutorizzati(details.getUtenza());
	} 

	public static List<Long> getIdTipiVersamentoAutorizzati(Utenza utenza) {
		if(utenza.isAutorizzazioneTipiVersamentoStar()) {
			return new ArrayList<>();
		} else {
			return utenza.getIdTipiVersamento();
		}
	} 

	public static List<String> getTipiVersamentoAutorizzati(Authentication authentication) {
		GovpayLdapUserDetails details = AutorizzazioneUtils.getAuthenticationDetails(authentication);
		return getTipiVersamentoAutorizzati(details.getUtenza());
	}

	public static List<String> getTipiVersamentoAutorizzati(Utenza utenza) {
		if(utenza.isAutorizzazioneTipiVersamentoStar()) {
			return new ArrayList<>();
		} else {
			return utenza.getIdTipoVersamento();
		}
	}

	public static List<String> getDominiAutorizzati(Authentication authentication) {
		GovpayLdapUserDetails details = AutorizzazioneUtils.getAuthenticationDetails(authentication);
		return getDominiAutorizzati(details.getUtenza());
	}

	public static List<String> getDominiAutorizzati(Utenza utenza) {
		if(utenza.isAutorizzazioneDominiStar()) {
			return new ArrayList<>();
		} else {
			return utenza.getIdDominio();
		}
	}

	public static List<Long> getIdDominiAutorizzati(Authentication authentication) {
		GovpayLdapUserDetails details = AutorizzazioneUtils.getAuthenticationDetails(authentication);
		return getIdDominiAutorizzati(details.getUtenza());
	}

	public static List<Long> getIdDominiAutorizzati(Utenza utenza) {
		if(utenza.isAutorizzazioneDominiStar()) {
			return new ArrayList<>();
		} else {
			return utenza.getIdDomini();
		}
	}

}