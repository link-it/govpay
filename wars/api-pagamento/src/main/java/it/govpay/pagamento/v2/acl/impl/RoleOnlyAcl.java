package it.govpay.pagamento.v2.acl.impl;

import java.util.Map;

import org.openspcoop2.utils.transport.http.HttpRequestMethod;

/**
 * Garantisce accesso in base al ruolo utilizzato
 * 
 * @author nardi
 *
 */

public class RoleOnlyAcl extends BasicAcl {
	
	public RoleOnlyAcl(String[] roles) {
		super(null, roles, null, null, null, null);
	}
	
	public RoleOnlyAcl(String[] roles, Map<HttpRequestMethod, String[]> resources) {
		super(null, roles, resources, null, null, null);
	}
	
	
}
