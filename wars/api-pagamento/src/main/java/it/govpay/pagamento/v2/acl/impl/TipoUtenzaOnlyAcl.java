package it.govpay.pagamento.v2.acl.impl;

import java.util.Map;

import org.openspcoop2.utils.transport.http.HttpRequestMethod;

import it.govpay.model.Utenza.TIPO_UTENZA;

/**
 * Garantisce accesso in base al ruolo utilizzato
 * 
 * @author nardi
 *
 */

public class TipoUtenzaOnlyAcl extends GpBasicAcl {
	
	public TipoUtenzaOnlyAcl(TIPO_UTENZA[] tipiUtenza) {
		super(tipiUtenza, null, null, null, null, null, null);
	}
	
	public TipoUtenzaOnlyAcl(TIPO_UTENZA[] tipiUtenza, Map<HttpRequestMethod, String[]> resources) {
		super(tipiUtenza, null, null, resources, null, null, null);
	}
	
}
