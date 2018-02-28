package it.govpay.core.utils;

import java.util.Arrays;
import java.util.List;

import it.govpay.model.Acl;
import it.govpay.model.Acl.Diritti;
import it.govpay.model.Acl.Servizio;
import it.govpay.model.IAutorizzato;

public class AclEngine {
	
	public static boolean isAuthorized(IAutorizzato user, Servizio servizio, Diritti diritto) {
		return isAuthorized(user, servizio, diritto);
	}

	public static boolean isAuthorized(IAutorizzato user, Servizio servizio, Diritti ... diritti) {
		List<Diritti> listaDiritti = Arrays.asList(diritti);
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
}
