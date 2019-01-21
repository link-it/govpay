package it.govpay.pagamento.v2.api.impl;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import org.apache.commons.lang.StringUtils;
import org.openspcoop2.generic_project.exception.NotAuthorizedException;
import org.openspcoop2.utils.jaxrs.fault.FaultCode;
import org.openspcoop2.utils.jaxrs.impl.BaseImpl;
import org.openspcoop2.utils.jaxrs.impl.ServiceContext;
import org.openspcoop2.utils.transport.http.HttpRequestMethod;

import it.govpay.core.dao.anagrafica.dto.GetAvvisoDTO;
import it.govpay.core.dao.anagrafica.dto.GetAvvisoDTO.FormatoAvviso;
import it.govpay.core.dao.anagrafica.dto.GetAvvisoDTOResponse;
import it.govpay.core.dao.pagamenti.AvvisiDAO;
import it.govpay.model.Utenza.TIPO_UTENZA;
import it.govpay.pagamento.v2.acl.Acl;
import it.govpay.pagamento.v2.acl.AuthorizationRules;
import it.govpay.pagamento.v2.acl.impl.TipoUtenzaOnlyAcl;
import it.govpay.pagamento.v2.api.AvvisiApi;
import it.govpay.pagamento.v2.beans.Avviso;
import it.govpay.pagamento.v2.beans.converter.PendenzeConverter;
/**
 * GovPay - API Pagamento
 */
@Path("/")
public class AvvisiApiServiceImpl extends BaseImpl implements AvvisiApi {
	
	public static UriBuilder basePath = UriBuilder.fromPath("/avvisi");

	public AvvisiApiServiceImpl(){
		super(org.slf4j.LoggerFactory.getLogger(AvvisiApiServiceImpl.class));
	}

	private AuthorizationRules getAuthorizationRules() throws Exception{
		AuthorizationRules ac = new AuthorizationRules();
		
		/*
		 * Utenti anonimi possono chiamare:
		 * - getAvviso - per ottenere le informazioni di un avviso o della pendenza ad esso associata
		 */
		{
			TIPO_UTENZA[] tipiUtenza = { TIPO_UTENZA.ANONIMO };
			
			Map<HttpRequestMethod, String[]> resources = new HashMap<HttpRequestMethod, String[]>();
			{
				String[] location = { "/avvisi/{idDominio}/{numeroAvviso}" };
				resources.put(HttpRequestMethod.GET, location);
			}
			
			Acl acl = new TipoUtenzaOnlyAcl(tipiUtenza, resources);
			ac.addAcl(acl);
		}
		/*
		 * Utenti CITTADINO e APPLICAZIONE possono chiamare tutte le operazioni:
		 */
		{
			TIPO_UTENZA[] tipiUtenza = { TIPO_UTENZA.CITTADINO, TIPO_UTENZA.APPLICAZIONE };
			Acl acl = new TipoUtenzaOnlyAcl(tipiUtenza);
			ac.addAcl(acl);
		}
		
		return ac;
	}

    /**
     * Avviso di pagamento
     *
     * Fornisce un avviso di pagamento o la pendenza ad esso associata
     *
     */
	@Override
	@Path("/avvisi/{idDominio}/{iuv}")
	@Produces({ "application/json", "application/problem+json" })
    public Avviso getAvviso(String idDominio, String iuv, String idDebitore) {
		ServiceContext context = this.getContext();
		try {
			context.getLogger().info("Invocazione in corso ...");     
			getAuthorizationRules().authorize(context);
			context.getLogger().debug("Autorizzazione completata con successo");      
			
			String accept = "";
			if(StringUtils.isNotEmpty(this.getContext().getServletRequest().getHeader("Accept"))) {
				accept = this.getContext().getServletRequest().getHeader("Accept");
			}
			
			GetAvvisoDTO getAvvisoDTO = new GetAvvisoDTO(context.getAuthentication(), idDominio, iuv);
			getAvvisoDTO.setAccessoAnonimo(true);
			getAvvisoDTO.setCfDebitore(idDebitore);
			
			AvvisiDAO avvisiDAO = new AvvisiDAO();
			
			// TODO quale formato restituire?
			if(accept.toLowerCase().contains("application/pdf")) {
				getAvvisoDTO.setFormato(FormatoAvviso.PDF);
				GetAvvisoDTOResponse getAvvisoDTOResponse = avvisiDAO.getAvviso(getAvvisoDTO);
				
				throw new NotAuthorizedException("Avviso di pagamento non disponibile nel formato richiesto");
//				return this.handleResponseOk(Response.status(Status.OK).type("application/pdf").entity(getAvvisoDTOResponse.getAvvisoPdf()).header("content-disposition", "attachment; filename=\""+getAvvisoDTOResponse.getFilenameAvviso()+"\""),transactionId).build();
			} else if(accept.toLowerCase().contains(MediaType.APPLICATION_JSON)) {
				getAvvisoDTO.setFormato(FormatoAvviso.JSON);
				GetAvvisoDTOResponse getAvvisoDTOResponse = avvisiDAO.getAvviso(getAvvisoDTO);
				Avviso avviso = PendenzeConverter.toAvviso(getAvvisoDTOResponse.getVersamento(), getAvvisoDTOResponse.getDominio(), getAvvisoDTOResponse.getBarCode(), getAvvisoDTOResponse.getQrCode());
				context.getLogger().debug("Invocazione completata con successo");
				return avviso;
			} else {
				// formato non accettato
				throw new NotAuthorizedException("Avviso di pagamento non disponibile nel formato richiesto");
			}
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
	@GET
	@Path("/avvisi/{idDominio}/{iuv}")
	@Produces({ "application/pdf" })
	public byte[] getAvvisoPdf(String idDominio, String iuv, String idDebitore) {
		ServiceContext context = this.getContext();
		try {
			context.getLogger().info("Invocazione in corso ...");     
			getAuthorizationRules().authorize(context);
			context.getLogger().debug("Autorizzazione completata con successo");      
			
			String accept = "";
			if(StringUtils.isNotEmpty(this.getContext().getServletRequest().getHeader("Accept"))) {
				accept = this.getContext().getServletRequest().getHeader("Accept");
			}
			
			GetAvvisoDTO getAvvisoDTO = new GetAvvisoDTO(context.getAuthentication(), idDominio, iuv);
			getAvvisoDTO.setAccessoAnonimo(true);
			getAvvisoDTO.setCfDebitore(idDebitore);
			
			AvvisiDAO avvisiDAO = new AvvisiDAO();
			
			// TODO quale formato restituire?
			if(accept.toLowerCase().contains("application/pdf")) {
				getAvvisoDTO.setFormato(FormatoAvviso.PDF);
				GetAvvisoDTOResponse getAvvisoDTOResponse = avvisiDAO.getAvviso(getAvvisoDTO);
				context.getLogger().debug("Invocazione completata con successo");
				
				return getAvvisoDTOResponse.getAvvisoPdf();
				
//				return this.handleResponseOk(Response.status(Status.OK).type("application/pdf").entity(getAvvisoDTOResponse.getAvvisoPdf()).header("content-disposition", "attachment; filename=\""+getAvvisoDTOResponse.getFilenameAvviso()+"\""),transactionId).build();
			} else if(accept.toLowerCase().contains(MediaType.APPLICATION_JSON)) {
				getAvvisoDTO.setFormato(FormatoAvviso.JSON);
				GetAvvisoDTOResponse getAvvisoDTOResponse = avvisiDAO.getAvviso(getAvvisoDTO);
				Avviso avviso = PendenzeConverter.toAvviso(getAvvisoDTOResponse.getVersamento(), getAvvisoDTOResponse.getDominio(), getAvvisoDTOResponse.getBarCode(), getAvvisoDTOResponse.getQrCode());
				context.getLogger().debug("Invocazione completata con successo");
				throw new NotAuthorizedException("Avviso di pagamento non disponibile nel formato richiesto");
			} else {
				// formato non accettato
				throw new NotAuthorizedException("Avviso di pagamento non disponibile nel formato richiesto");
			}
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

