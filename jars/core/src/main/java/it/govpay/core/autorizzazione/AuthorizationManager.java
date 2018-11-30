package it.govpay.core.autorizzazione;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.utils.Utilities;
import org.openspcoop2.utils.UtilsException;
import org.springframework.security.core.Authentication;

import it.govpay.bd.model.Utenza;
import it.govpay.core.autorizzazione.beans.GovpayLdapUserDetails;
import it.govpay.core.autorizzazione.utils.AutorizzazioneUtils;
import it.govpay.core.exceptions.NotAuthenticatedException;
import it.govpay.core.exceptions.NotAuthorizedException;
import it.govpay.model.Acl;
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
	
	public static NotAuthorizedException toNotAuthorizedException(Authentication authentication, List<Servizio> servizio, List<Diritti> listaDiritti, boolean accessoAnonimo) {
		GovpayLdapUserDetails details = AutorizzazioneUtils.getAuthenticationDetails(authentication);
		StringBuilder sb = new StringBuilder();
		
		StringBuilder sbServizi = new StringBuilder();
		
		for (Diritti diritti : listaDiritti) {
			if(sbServizi.length() >0) 
				sbServizi.append(", ");
			
			sbServizi.append(diritti);
		}
		
		if(details.getTipoUtenza().equals(TIPO_UTENZA.ANONIMO) && !accessoAnonimo) {
			sb.append("Utenza [").append(details != null ? details.getIdentificativo() : "NON RICONOSCIUTA").append("] non puo' accedere in maniera anonima ai Servizi [").append(sbServizi).append("]");
			return new NotAuthorizedException(sb.toString());
		}
		
		StringBuilder sbDiritti = new StringBuilder();
		
		for (Diritti diritti : listaDiritti) {
			if(sbDiritti.length() >0) 
				sbDiritti.append(", ");
			
			sbDiritti.append(diritti);
		}
		
		sb.append("Utenza [").append(details != null ? details.getIdentificativo() : "NON RICONOSCIUTA").append("] non possiede i Diritti [").append(sbDiritti.toString()).append("] necessari per i Servizi [").append(sbServizi).append("]");
		
		return new NotAuthorizedException(sb.toString());

	}
	
	public static NotAuthorizedException toNotAuthorizedException(Authentication authentication, Servizio servizio, List<Diritti> listaDiritti, boolean accessoAnonimo ) {
		GovpayLdapUserDetails details = AutorizzazioneUtils.getAuthenticationDetails(authentication);
		StringBuilder sb = new StringBuilder();
		
		if(details.getTipoUtenza().equals(TIPO_UTENZA.ANONIMO) && !accessoAnonimo) {
			sb.append("Utenza [").append(details != null ? details.getIdentificativo() : "NON RICONOSCIUTA").append("] non puo' accedere in maniera anonima al Servizio [").append(servizio).append("]");
			return new NotAuthorizedException(sb.toString());
		}
		
		StringBuilder sbDiritti = new StringBuilder();
		
		for (Diritti diritti : listaDiritti) {
			if(sbDiritti.length() >0) 
				sbDiritti.append(", ");
			
			sbDiritti.append(diritti);
		}
		
		sb.append("Utenza [").append(details != null ? details.getIdentificativo() : "NON RICONOSCIUTA").append("] non possiede i Diritti [").append(sbDiritti.toString()).append("] necessari per il Servizio [").append(servizio).append("]");
		
		
		return new NotAuthorizedException(sb.toString());
	}
	
	public static NotAuthorizedException toNotAuthorizedException(Authentication authentication, Servizio servizio, List<Diritti> listaDiritti, boolean accessoAnonimo, String codDominio, String codTributo) {
		GovpayLdapUserDetails details = AutorizzazioneUtils.getAuthenticationDetails(authentication);
		StringBuilder sb = new StringBuilder();
		
		if(details.getTipoUtenza().equals(TIPO_UTENZA.ANONIMO) && !accessoAnonimo) {
			sb.append("Utenza [").append(details != null ? details.getIdentificativo() : "NON RICONOSCIUTA").append("] non puo' accedere in maniera anonima al Servizio [").append(servizio).append("]");
			return new NotAuthorizedException(sb.toString());
		}
		
		StringBuilder sbDiritti = new StringBuilder();
		
		for (Diritti diritti : listaDiritti) {
			if(sbDiritti.length() >0) 
				sbDiritti.append(", ");
			
			sbDiritti.append(diritti);
		}
		
		sb.append("Utenza [").append(details != null ? details.getIdentificativo() : "NON RICONOSCIUTA").append("] non possiede i Diritti [").append(sbDiritti.toString()).append("] necessari per il Servizio [").append(servizio).append("]");
		
		
		return new NotAuthorizedException(sb.toString());
	}
	
	public static NotAuthorizedException toNotAuthorizedExceptionNessunDominioAutorizzato(Authentication authentication,Servizio servizio, Diritti diritti) {
		return toNotAuthorizedExceptionNessunDominioAutorizzato(authentication, servizio, Arrays.asList(diritti));
	}
	
	public static NotAuthorizedException toNotAuthorizedExceptionNessunDominioAutorizzato(Authentication authentication,Servizio servizio,List<Diritti> listaDiritti) {
		GovpayLdapUserDetails details = AutorizzazioneUtils.getAuthenticationDetails(authentication);
		StringBuilder sb = new StringBuilder();
		
		StringBuilder sbDiritti = new StringBuilder();
		
		for (Diritti diritti : listaDiritti) {
			if(sbDiritti.length() >0) 
				sbDiritti.append(", ");
			
			sbDiritti.append(diritti);
		}
		
		sb.append("L'utenza autenticata [").append(details != null ? details.getIdentificativo() : "NON RICONOSCIUTA").append("] non possiede i Diritti [").append(sbDiritti.toString()).append("] per nessun Dominio necessari per il Servizio [").append(servizio).append("] ");
		return new NotAuthorizedException(sb.toString());
	}
	
	public static boolean isAuthorizedLettura(Authentication authentication, Servizio servizio, boolean accessoAnonimo) throws NotAuthorizedException {
		return isAuthorized(authentication, servizio, Diritti.LETTURA, accessoAnonimo);
	}
	
	public static boolean isAuthorizedScrittura(Authentication authentication, Servizio servizio, boolean accessoAnonimo)  throws NotAuthorizedException {
		return isAuthorized(authentication, servizio, Diritti.SCRITTURA, accessoAnonimo);
	}
	
	public static boolean isAuthorizedEsecuzione(Authentication authentication, Servizio servizio, boolean accessoAnonimo)  throws NotAuthorizedException {
		return isAuthorized(authentication, servizio, Diritti.ESECUZIONE, accessoAnonimo);
	}
	
	public static boolean isAuthorized(Authentication authentication, Servizio servizio, Diritti diritto, boolean accessoAnonimo) {
		List<Diritti> listaDiritti = new ArrayList<>();
		listaDiritti.add(diritto);
		return isAuthorized(authentication, servizio, listaDiritti, accessoAnonimo);
	}

	public static boolean isAuthorized(Authentication authentication, Servizio servizio, List<Diritti> listaDiritti, boolean accessoAnonimo) {
		GovpayLdapUserDetails details = AutorizzazioneUtils.getAuthenticationDetails(authentication);
		return isAuthorized(details.getUtenza(), details.getTipoUtenza(), servizio, listaDiritti, accessoAnonimo);
	}

	public static boolean isAuthorized(Utenza utenza, TIPO_UTENZA tipoUtenza, Servizio servizio, List<Diritti> listaDiritti, boolean accessoAnonimo) {
		
		// controllo se l'utenza anonima e' abilitata per l'operazione richiesta.
		if(tipoUtenza.equals(TIPO_UTENZA.ANONIMO) && !accessoAnonimo) {
			return false;
		}
		
		for(Acl acl : utenza.getAcls()) {
			
			if(acl.getServizio().equals(servizio)) {
				for (Diritti dirittoTmp : listaDiritti) {
					if(acl.getListaDiritti().contains(dirittoTmp))
						return true;
				}
			}
		}

		return false;
	}
	
	public static boolean isAuthorized(Authentication authentication, Servizio servizio, String codDominio, String codTributo, List<Diritti> listaDiritti) {
		return isAuthorized(authentication, servizio, codDominio, codTributo, listaDiritti, false);
	}
	
	public static boolean isAuthorized(Authentication authentication, Servizio servizio, String codDominio, String codTributo, List<Diritti> listaDiritti, boolean accessoAnonimo) {
		GovpayLdapUserDetails details = AutorizzazioneUtils.getAuthenticationDetails(authentication);
		return isAuthorized(details.getUtenza(), details.getTipoUtenza(), servizio, codDominio, codTributo, listaDiritti, accessoAnonimo);
	}
	
	public static boolean isAuthorized(Utenza utenza, TIPO_UTENZA tipoUtenza, Servizio servizio, String codDominio, String codTributo, List<Diritti> listaDiritti) {
		return isAuthorized(utenza, tipoUtenza, servizio, codDominio, codTributo, listaDiritti, false);
	}
	
	public static boolean isAuthorized(Utenza utenza, TIPO_UTENZA tipoUtenza, Servizio servizio, String codDominio, String codTributo, List<Diritti> listaDiritti, boolean accessoAnonimo) {
		
		boolean authorized = isAuthorized(utenza,tipoUtenza, servizio, listaDiritti, accessoAnonimo); 
		
		if(authorized) {
			switch (tipoUtenza) {
			case CITTADINO:
				// cittadino autorizzato in maniera diversa rispetto ad applicazioni e operatori.	
				break;
			case ANONIMO:
				break;
			case APPLICAZIONE:
			case OPERATORE:
			default:
				if(codDominio != null) {
					List<String> dominiAutorizzati = getDominiAutorizzati(utenza,tipoUtenza, servizio, listaDiritti, accessoAnonimo);
					
					if(dominiAutorizzati == null) 
						return false;
					
					if(!dominiAutorizzati.isEmpty())
						authorized = authorized && dominiAutorizzati.contains(codDominio);
				}
				
				if(codTributo != null) {
					List<String> tributiAutorizzati = getTributiAutorizzati(utenza,tipoUtenza, servizio, listaDiritti, accessoAnonimo);
					
					if(tributiAutorizzati == null) 
						return false;
					
					if(!tributiAutorizzati.isEmpty())
						authorized = authorized && tributiAutorizzati.contains(codTributo);
				}
				break;
			}
		}
		
		return authorized;
	}
	
	public static List<Long> getIdDominiAutorizzati(Authentication authentication, Servizio servizio, Diritti diritto) {
		return getIdDominiAutorizzati(authentication, servizio, diritto, false);
	}
	
	public static List<Long> getIdDominiAutorizzati(Authentication authentication, Servizio servizio, Diritti diritto, boolean accessoAnonimo) {
		List<Diritti> listaDiritti = new ArrayList<>();
		listaDiritti.add(diritto);
		return getIdDominiAutorizzati(authentication, servizio, listaDiritti, accessoAnonimo);
	}
	
	public static List<String> getDominiAutorizzati(Authentication authentication, Servizio servizio, Diritti diritto) {
		return getDominiAutorizzati(authentication,servizio,diritto,false);
	}
	
	public static List<String> getDominiAutorizzati(Authentication authentication, Servizio servizio, Diritti diritto, boolean accessoAnonimo) {
		List<Diritti> listaDiritti = new ArrayList<>();
		listaDiritti.add(diritto);
		return getDominiAutorizzati(authentication, servizio, listaDiritti, accessoAnonimo);
	}

	public static List<String> getTributiAutorizzati(Authentication authentication, Servizio servizio, Diritti diritto, boolean accessoAnonimo) {
		List<Diritti> listaDiritti = new ArrayList<>();
		listaDiritti.add(diritto);
		return getTributiAutorizzati(authentication, servizio, listaDiritti, accessoAnonimo);
	}

	public static List<Long> getIdTributiAutorizzati(Authentication authentication, Servizio servizio, Diritti diritto, boolean accessoAnonimo) {
		List<Diritti> listaDiritti = new ArrayList<>();
		listaDiritti.add(diritto);
		return getIdTributiAutorizzati(authentication, servizio, listaDiritti, accessoAnonimo);
	}
	

	public static List<Long> getIdDominiAutorizzati(Authentication authentication, Servizio servizio, List<Diritti> diritti) {
		return getIdDominiAutorizzati(authentication, servizio, diritti, false);
	}	

	public static List<Long> getIdDominiAutorizzati(Authentication authentication, Servizio servizio, List<Diritti> diritti, boolean accessoAnonimo) {
		if(isAuthorized(authentication, servizio, diritti, accessoAnonimo)) {
			GovpayLdapUserDetails details = AutorizzazioneUtils.getAuthenticationDetails(authentication);
			return details.getUtenza().getIdDomini();
		} else {
			return null;
		}
	}
	
	public static List<String> getDominiAutorizzati(Authentication authentication, Servizio servizio, List<Diritti> diritti) {
		return getDominiAutorizzati(authentication, servizio, diritti, false);
	}
	
	public static List<String> getDominiAutorizzati(Authentication authentication, Servizio servizio, List<Diritti> diritti, boolean accessoAnonimo) {
		GovpayLdapUserDetails details = AutorizzazioneUtils.getAuthenticationDetails(authentication);
		return getDominiAutorizzati(details.getUtenza(), details.getTipoUtenza(), servizio, diritti, accessoAnonimo);
	}
	
	public static List<String> getDominiAutorizzati(Utenza utenza, TIPO_UTENZA tipoUtenza, Servizio servizio, List<Diritti> diritti, boolean accessoAnonimo) {
		if(isAuthorized(utenza,tipoUtenza, servizio, diritti, accessoAnonimo)) {
			return utenza.getIdDominio();
		} else {
			return null;
		}
	}

	public static List<String> getTributiAutorizzati(Authentication authentication, Servizio servizio, List<Diritti> diritti, boolean accessoAnonimo) {
		GovpayLdapUserDetails details = AutorizzazioneUtils.getAuthenticationDetails(authentication);
		return getTributiAutorizzati(details.getUtenza(), details.getTipoUtenza(), servizio, diritti, accessoAnonimo);
	}
	
	public static List<String> getTributiAutorizzati(Utenza utenza, TIPO_UTENZA tipoUtenza, Servizio servizio, List<Diritti> diritti, boolean accessoAnonimo) {
		if(isAuthorized(utenza,tipoUtenza, servizio, diritti, accessoAnonimo)) {
			return utenza.getIdTributo();
		} else {
			return null;
		}
	}

	public static List<Long> getIdTributiAutorizzati(Authentication authentication, Servizio servizio, List<Diritti> diritti, boolean accessoAnonimo) {
		if(isAuthorized(authentication, servizio, diritti, accessoAnonimo)) {
			GovpayLdapUserDetails details = AutorizzazioneUtils.getAuthenticationDetails(authentication);
			return details.getUtenza().getIdTributi();
		} else {
			return null;
		}
	} 
}