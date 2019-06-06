package it.govpay.rs.v2.acl;

import org.openspcoop2.utils.service.context.IContext;

public interface Acl {
	
	public static final String WILDCARD = "*";
	
	public boolean isSatisfied(IContext context);
	
}
