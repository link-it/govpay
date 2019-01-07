package it.govpay.pagamento.v2.acl.impl;

import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.ArrayUtils;
import org.openspcoop2.utils.jaxrs.impl.ServiceContext;
import org.openspcoop2.utils.transport.http.HttpRequestMethod;

import it.govpay.core.autorizzazione.utils.AutorizzazioneUtils;
import it.govpay.model.Utenza.TIPO_UTENZA;

public class GpBasicAcl extends BasicAcl {

	TIPO_UTENZA[] tipiUtenza;

	public GpBasicAcl(TIPO_UTENZA[] tipiUtenza, String[] principals, String[] roles, Map<HttpRequestMethod, String[]> resources, Properties pathParams, Properties queryParams, Properties roleParams) {
		super(principals, roles, resources, pathParams, queryParams, roleParams);
		this.tipiUtenza = tipiUtenza;
	}

	@Override
	public boolean isSatisfied(ServiceContext context) {
		
		// Se il tipoUtenza e' impostato controllo che sia come quello del chiamatne
		if(tipiUtenza != null && AutorizzazioneUtils.getAuthenticationDetails(context.getAuthentication()) == null && AutorizzazioneUtils.getAuthenticationDetails(context.getAuthentication()).getTipoUtenza() != null) {
			if(!ArrayUtils.contains(tipiUtenza, AutorizzazioneUtils.getAuthenticationDetails(context.getAuthentication()).getTipoUtenza()))
				return false;
		}

		return super.isSatisfied(context);
	}

}
