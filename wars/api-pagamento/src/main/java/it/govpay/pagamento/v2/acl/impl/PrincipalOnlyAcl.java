package it.govpay.pagamento.v2.acl.impl;

import java.util.List;
import java.util.Map;

import org.openspcoop2.utils.transport.http.HttpRequestMethod;

/**
 * Garantisce accesso in base al principal utilizzato
 * 
 * @author nardi
 *
 */
public class PrincipalOnlyAcl extends BasicAcl {
	
	public PrincipalOnlyAcl(List<String> principals) {
		super(principals, null, null, null, null, null);
	}
	
	public PrincipalOnlyAcl(List<String> principals, Map<HttpRequestMethod, List<String>> resources) {
		super(principals, null, resources, null, null, null);
	}

}
