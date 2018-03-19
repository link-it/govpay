package it.govpay.core.utils;

import java.util.ArrayList;
import java.util.List;

import it.govpay.model.Acl;
import it.govpay.model.Acl.Diritti;
import it.govpay.model.Acl.Servizio;
import it.govpay.model.IAutorizzato;
import it.govpay.bd.model.Utenza;
import it.govpay.core.exceptions.NotAuthenticatedException;
import it.govpay.core.exceptions.NotAuthorizedException;

public class AclEngine {
	
	public static NotAuthorizedException toNotAuthorizedException(IAutorizzato user) {
		StringBuilder sb = new StringBuilder();
		sb.append("Utenza [").append(user != null ? user.getPrincipal() : "NON RICONOSCIUTA ] non autorizzata.");
		return new NotAuthorizedException(sb.toString());
	}
	
	public static NotAuthenticatedException toNotAuthenticatedException(IAutorizzato user) {
		StringBuilder sb = new StringBuilder();
		sb.append("Utenza [").append(user != null ? user.getPrincipal() : "NON RICONOSCIUTA ] non autorizzata.");
		return new NotAuthenticatedException(sb.toString());
	}
	
	public static NotAuthorizedException toNotAuthorizedException(IAutorizzato user, Servizio servizio, List<Diritti> listaDiritti ) {
		StringBuilder sb = new StringBuilder();
		
		StringBuilder sbDiritti = new StringBuilder();
		
		for (Diritti diritti : listaDiritti) {
			if(sbDiritti.length() >0) 
				sbDiritti.append(", ");
			
			sbDiritti.append(diritti);
		}
		
		sb.append("Utenza [").append(user != null ? user.getPrincipal() : "NON RICONOSCIUTA").append("] non possiede i Diritti [").append(sbDiritti.toString()).append("] necessari per il Servizio [").append(servizio).append("]");
		
		
		return new NotAuthorizedException(sb.toString());
	}
	
	public static NotAuthorizedException toNotAuthorizedException(IAutorizzato user, Servizio servizio, List<Diritti> listaDiritti, String codDominio, String codTributo) {
		StringBuilder sb = new StringBuilder();
		
		StringBuilder sbDiritti = new StringBuilder();
		
		for (Diritti diritti : listaDiritti) {
			if(sbDiritti.length() >0) 
				sbDiritti.append(", ");
			
			sbDiritti.append(diritti);
		}
		
		sb.append("Utenza [").append(user != null ? user.getPrincipal() : "NON RICONOSCIUTA").append("] non possiede i Diritti [").append(sbDiritti.toString()).append("] necessari per il Servizio [").append(servizio).append("]");
		
		
		return new NotAuthorizedException(sb.toString());
	}
	
	public static boolean isAuthorizedLettura(IAutorizzato user, Servizio servizio) throws NotAuthorizedException {
		return isAuthorized(user, servizio, Diritti.LETTURA);
	}
	
	public static boolean isAuthorizedScrittura(IAutorizzato user, Servizio servizio)  throws NotAuthorizedException {
		return isAuthorized(user, servizio, Diritti.SCRITTURA);
	}
	
	public static boolean isAuthorizedEsecuzione(IAutorizzato user, Servizio servizio)  throws NotAuthorizedException {
		return isAuthorized(user, servizio, Diritti.ESECUZIONE);
	}
	
	public static boolean isAuthorized(IAutorizzato user, Servizio servizio, Diritti diritto) {
		List<Diritti> listaDiritti = new ArrayList<Diritti>();
		listaDiritti.add(diritto);
		return isAuthorized(user, servizio, listaDiritti);
	}

	public static boolean isAuthorized(IAutorizzato user, Servizio servizio, List<Diritti> listaDiritti) {
		for(Acl acl : user.getAcls()) {
			
			if(acl.getServizio().equals(servizio)) {
				for (Diritti dirittoTmp : listaDiritti) {
					if(acl.getListaDiritti().contains(dirittoTmp))
						return true;
				}
			}
		}

		return false;
	}
	
	public static boolean isAuthorized(IAutorizzato user, Servizio servizio, String codDominio, String codTributo, List<Diritti> listaDiritti) {
		boolean authorized = isAuthorized(user, servizio, listaDiritti); 
		
		if(authorized) {
			if(codDominio != null) {
				List<String> dominiAutorizzati = getDominiAutorizzati((Utenza) user, servizio, listaDiritti);
				
				if(dominiAutorizzati == null) 
					return false;
				
				if(!dominiAutorizzati.isEmpty())
					authorized = authorized && dominiAutorizzati.contains(codDominio);
			}
			
			if(codTributo != null) {
				List<String> tributiAutorizzati = getTributiAutorizzati((Utenza) user, servizio, listaDiritti);
				
				if(tributiAutorizzati == null) 
					return false;
				
				if(!tributiAutorizzati.isEmpty())
					authorized = authorized && tributiAutorizzati.contains(codTributo);
			}
		}
		
		return authorized;
	}
	
	public static List<Long> getIdDominiAutorizzati(Utenza utenza, Servizio servizio, Diritti diritto) {
		List<Diritti> listaDiritti = new ArrayList<Diritti>();
		listaDiritti.add(diritto);
		return getIdDominiAutorizzati(utenza, servizio, listaDiritti);
	}
	
	public static List<String> getDominiAutorizzati(Utenza utenza, Servizio servizio, Diritti diritto) {
		List<Diritti> listaDiritti = new ArrayList<Diritti>();
		listaDiritti.add(diritto);
		return getDominiAutorizzati(utenza, servizio, listaDiritti);
	}

	public static List<String> getTributiAutorizzati(Utenza utenza, Servizio servizio, Diritti diritto) {
		List<Diritti> listaDiritti = new ArrayList<Diritti>();
		listaDiritti.add(diritto);
		return getTributiAutorizzati(utenza, servizio, listaDiritti);
	}

	public static List<Long> getIdTributiAutorizzati(Utenza utenza, Servizio servizio, Diritti diritto) {
		List<Diritti> listaDiritti = new ArrayList<Diritti>();
		listaDiritti.add(diritto);
		return getIdTributiAutorizzati(utenza, servizio, listaDiritti);
	}
	

	public static List<Long> getIdDominiAutorizzati(Utenza utenza, Servizio servizio, List<Diritti> diritti) {
		if(isAuthorized(utenza, servizio, diritti)) {
			return utenza.getIdDomini();
		} else {
			return null;
		}
	}
	
	public static List<String> getDominiAutorizzati(Utenza utenza, Servizio servizio, List<Diritti> diritti) {
		if(isAuthorized(utenza, servizio, diritti)) {
			return utenza.getIdDominio();
		} else {
			return null;
		}
	}

	public static List<String> getTributiAutorizzati(Utenza utenza, Servizio servizio, List<Diritti> diritti) {
		if(isAuthorized(utenza, servizio, diritti)) {
			return utenza.getIdTributo();
		} else {
			return null;
		}
	}

	public static List<Long> getIdTributiAutorizzati(Utenza utenza, Servizio servizio, List<Diritti> diritti) {
		if(isAuthorized(utenza, servizio, diritti)) {
			return utenza.getIdTributi();
		} else {
			return null;
		}
	} 
}