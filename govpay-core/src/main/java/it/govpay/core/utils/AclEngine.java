package it.govpay.core.utils;

import java.util.ArrayList;
import java.util.List;

import it.govpay.model.Acl;
import it.govpay.model.Acl.Diritti;
import it.govpay.model.Acl.Servizio;
import it.govpay.model.IAutorizzato;
import it.govpay.bd.model.Utenza;

public class AclEngine {
	
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
		boolean authorized = false;
		for(Acl acl : user.getAcls()) {
			
			if(acl.getServizio().equals(servizio)) {
				for (Diritti dirittoTmp : listaDiritti) {
					if(acl.getListaDiritti().contains(dirittoTmp)) {
						 authorized = true;
						 break;
					}
				}
			}
		}
		
		if(authorized) {
			if(codDominio != null)
				authorized = authorized && user.getIdDominio().contains(codDominio);
	
			if(codTributo != null)
				authorized = authorized && user.getIdTributo().contains(codTributo);
		}
		
		return authorized;
	}

	public static List<Long> getIdDominiAutorizzati(Utenza utenza, Servizio servizio, List<Diritti> diritti) {
		if(isAuthorized(utenza, servizio, diritti)) {
			return utenza.getIdDomini();
		} else {
			return new ArrayList<>();
		}
	}

	public static List<String> getDominiAutorizzati(Utenza utenza, Servizio servizio, List<Diritti> diritti) {
		if(isAuthorized(utenza, servizio, diritti)) {
			return utenza.getIdDominio();
		} else {
			return new ArrayList<>();
		}
	}

	public static List<String> getTributiAutorizzati(Utenza utenza, Servizio servizio, List<Diritti> diritti) {
		if(isAuthorized(utenza, servizio, diritti)) {
			return utenza.getIdTributo();
		} else {
			return new ArrayList<>();
		}
	}

	public static List<Long> getIdTributiAutorizzati(Utenza utenza, Servizio servizio, List<Diritti> diritti) {
		if(isAuthorized(utenza, servizio, diritti)) {
			return utenza.getIdTributi();
		} else {
			return new ArrayList<>();
		}
	} 
}