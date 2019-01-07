package it.govpay.pagamento.v2.acl.impl;

import org.openspcoop2.utils.jaxrs.impl.ServiceContext;

/**
 * Garantisce accesso per qualsiasi richiesta
 * 
 * @author nardi
 *
 */

public class GrantAllAcl extends BasicAcl {
	
	public GrantAllAcl() {
		super(null, null, null, null, null, null);
	}

	@Override
	public boolean isSatisfied(ServiceContext context) {
		return true;
	}

}
