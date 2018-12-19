package it.govpay.pagamento.v2.api.impl;

import it.govpay.bd.model.PagamentoPortale;
import it.govpay.bd.model.Rpt;
import it.govpay.core.dao.pagamenti.PendenzeDAO;
import it.govpay.core.dao.pagamenti.dto.LeggiPendenzaDTO;
import it.govpay.core.dao.pagamenti.dto.LeggiPendenzaDTOResponse;
import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.GpThreadLocal;
import it.govpay.pagamento.v2.api.*;
import it.govpay.pagamento.v2.beans.Avviso;
import it.govpay.pagamento.v2.beans.Pendenza;
import it.govpay.pagamento.v2.beans.Pendenze;
import it.govpay.pagamento.v2.beans.StatoPendenza;
import it.govpay.pagamento.v2.beans.converter.PendenzeConverter;

import org.openspcoop2.utils.jaxrs.impl.AuthorizationManager;
import org.openspcoop2.utils.jaxrs.impl.BaseImpl;
import org.openspcoop2.utils.jaxrs.impl.ServiceContext;
import org.openspcoop2.utils.jaxrs.impl.AuthorizationConfig;

import java.io.ByteArrayOutputStream;
import java.util.List;

import javax.ws.rs.core.UriBuilder;

import org.openspcoop2.utils.jaxrs.fault.FaultCode;
/**
 * GovPay - API Pagamento
 */
public class PendenzeApiServiceImpl extends BaseImpl implements PendenzeApi {
	
	public static UriBuilder basePath = UriBuilder.fromPath("/pendenze");

	public PendenzeApiServiceImpl(){
		super(org.slf4j.LoggerFactory.getLogger(PendenzeApiServiceImpl.class));
	}

	private AuthorizationConfig getAuthorizationConfig() throws Exception{
		// TODO: Implement ...
		throw new Exception("NotImplemented");
	}

    /**
     * Elenco delle pendenze
     *
     * Fornisce la lista delle pendenze filtrata ed ordinata.
     *
     */
	@Override
    public Pendenze findPendenze(String idDominio, String iuv, String idA2A, String idPendenza, Long offset, Integer limit, String fields, String sort, String idDebitore, StatoPendenza statoPendenza, String idSessionePortale) {
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
    
    /**
     * Dettaglio di una pendenza per identificativo
     *
     * Acquisisce il dettaglio di una pendenza, comprensivo dei dati di pagamento.
     *
     */
	@Override
    public Pendenza getPendenza(String idA2A, String idPendenza) {
		ServiceContext context = this.getContext();
		try {
			context.getLogger().debug("Invocazione in corso ...");     

			AuthorizationManager.authorize(context, getAuthorizationConfig());
			context.getLogger().debug("Autorizzazione completata con successo");     
                        
			LeggiPendenzaDTO leggiPendenzaDTO = new LeggiPendenzaDTO(context.getAuthentication());
			leggiPendenzaDTO.setCodA2A(idA2A);
			leggiPendenzaDTO.setCodPendenza(idPendenza);
			leggiPendenzaDTO.setInfoIncasso(true); 
			
			PendenzeDAO pendenzeDAO = new PendenzeDAO(); 
			
			LeggiPendenzaDTOResponse ricevutaDTOResponse = pendenzeDAO.leggiPendenza(leggiPendenzaDTO);

			List<PagamentoPortale> pagamenti = null; // TODO
			List<Rpt> transazioni = null; // TODO
			
			Pendenza pendenza = PendenzeConverter.toPendenza(ricevutaDTOResponse.getVersamentoIncasso(), pagamenti, transazioni); 

        
			context.getLogger().debug("Invocazione completata con successo");
        return pendenza;
     
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

