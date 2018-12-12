package it.govpay.pagamento.v2.acl;

import java.util.ArrayList;
import java.util.List;

import org.openspcoop2.generic_project.exception.NotAuthorizedException;
import org.openspcoop2.utils.jaxrs.impl.ServiceContext;

public class AuthorizationRules {
	
	public AuthorizationRules() {
		this.alcs = new ArrayList<>();
	}
	
	private List<Acl> alcs;
	
	public void addAcl(Acl acl) {
		this.alcs.add(acl);
	}
	
	/**
	 * Il metodo valuta se una ACL autorizza la richiesta e la restituisce, 
	 * altrimenti lancia l'eccezione di richiesta non autorizzata
	 * 
	 * @return
	 * @throws NotAuthorizedException
	 */
	
	public Acl authorize(ServiceContext context) throws NotAuthorizedException {
		for(Acl acl : alcs) {
			if(acl.isSatisfied(context)) return acl;
		}
		throw new NotAuthorizedException();
	}

}
