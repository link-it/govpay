package it.govpay.rs.v3.acl.impl;

import java.util.Map;

import org.openspcoop2.utils.transport.http.HttpRequestMethod;

/**
 * Garantisce accesso in base al principal utilizzato
 * 
 * @author nardi
 *
 */
public class PrincipalOnlyAcl extends BasicAcl {
	
	public PrincipalOnlyAcl(String[] principals) {
		super(principals, null, null, null, null, null);
	}
	
	public PrincipalOnlyAcl(String[] principals, Map<HttpRequestMethod, String[]> resources) {
		super(principals, null, resources, null, null, null);
	}

}
