package it.govpay.pagamento.v2.acl.impl;

import org.openspcoop2.utils.service.context.IContext;

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
	public boolean isSatisfied(IContext context) {
		return true;
	}

}
