package it.govpay.pagamento.v2.api.impl;

import javax.ws.rs.core.UriBuilder;

import org.openspcoop2.utils.jaxrs.fault.FaultCode;
import org.openspcoop2.utils.jaxrs.impl.AuthorizationConfig;
import org.openspcoop2.utils.jaxrs.impl.AuthorizationManager;
import org.openspcoop2.utils.jaxrs.impl.BaseImpl;
import org.openspcoop2.utils.jaxrs.impl.ServiceContext;

import it.govpay.pagamento.v2.api.AvvisiApi;
import it.govpay.pagamento.v2.beans.Avviso;
/**
 * GovPay - API Pagamento
 */
public class AvvisiApiServiceImpl extends BaseImpl implements AvvisiApi {
	
	public static UriBuilder basePath = UriBuilder.fromPath("/avvisi");

	public AvvisiApiServiceImpl(){
		super(org.slf4j.LoggerFactory.getLogger(AvvisiApiServiceImpl.class));
	}

	private AuthorizationConfig getAuthorizationConfig() throws Exception{
		// TODO: Implement ...
		throw new Exception("NotImplemented");
	}

    /**
     * Avviso di pagamento
     *
     * Fornisce un avviso di pagamento o la pendenza ad esso associata
     *
     */
	@Override
    public Avviso getAvviso(String idDominio, String iuv, String idDebitore) {
		ServiceContext context = this.getContext();
		try {
			context.getLogger().debug("Invocazione in corso ...");     

			AuthorizationManager.authorize(context, getAuthorizationConfig());
			context.getLogger().debug("Autorizzazione completata con successo");     
                        
        // TODO: Implement...
        
			context.getLogger().debug("Invocazione completata con successo");
        return null;
     
		}
		catch(javax.ws.rs.WebApplicationException e) {
			context.getLogger().error("Invocazione terminata con errore '4xx': %s",e, e.getMessage());
			throw e;
		}
		catch(Throwable e) {
			context.getLogger().error("Invocazione terminata con errore: %s",e, e.getMessage());
			throw FaultCode.ERRORE_INTERNO.toException(e);
		}
    }
    
}

