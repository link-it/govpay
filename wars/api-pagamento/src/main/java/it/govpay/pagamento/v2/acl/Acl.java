package it.govpay.pagamento.v2.acl;

import org.openspcoop2.utils.jaxrs.impl.ServiceContext;

public interface Acl {
	
	public static final String WILDCARD = "*";
	
	public boolean isSatisfied(ServiceContext context);
	
}
