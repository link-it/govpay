package it.govpay.wc.ecsp.v1.gw;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URI;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import it.govpay.bd.BasicBD;
import it.govpay.core.dao.pagamenti.WebControllerDAO;
import it.govpay.core.dao.pagamenti.dto.RichiestaWebControllerDTO;
import it.govpay.core.dao.pagamenti.dto.RichiestaWebControllerDTOResponse;
import it.govpay.core.dao.pagamenti.exception.RedirectException;
import it.govpay.core.utils.GovpayConfig;
import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.GpThreadLocal;
import it.govpay.wc.ecsp.BaseRsService;

@Path("/")
public class Gateway extends BaseRsService{

	@POST
	@Path("/v1/gw/{id}")
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes({MediaType.TEXT_HTML})
	public Response post_GW(InputStream is , @Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("id") String idSessione, @QueryParam("action") String action) {
		String methodName = "post_gateway";  
		GpContext ctx = null;
		ByteArrayOutputStream baos= null;
		this.log.info("Esecuzione " + methodName + " in corso..."); 
		String gwErrorLocation = null;
		URI gwErrorLocationURI = null;
		try{
			gwErrorLocation = GovpayConfig.getInstance().getUrlErrorGovpayWC();
			gwErrorLocationURI = new URI(gwErrorLocation);
			
			baos = new ByteArrayOutputStream();
			// salvo il json ricevuto
			copy(is, baos);
			this.logRequest(uriInfo, httpHeaders, methodName, baos);
			
			ctx =  GpThreadLocal.get();
			String principal = this.getPrincipal();
			
			RichiestaWebControllerDTO aggiornaPagamentiPortaleDTO = new RichiestaWebControllerDTO();
			aggiornaPagamentiPortaleDTO.setIdSessione(idSessione);
			aggiornaPagamentiPortaleDTO.setPrincipal(principal);
			aggiornaPagamentiPortaleDTO.setAction(action);

			WebControllerDAO webControllerDAO = new WebControllerDAO(BasicBD.newInstance(ctx.getTransactionId()));
			
			RichiestaWebControllerDTOResponse aggiornaPagamentiPortaleDTOResponse = webControllerDAO.gestisciRichiestaWebController(aggiornaPagamentiPortaleDTO);
		
			this.logResponse(uriInfo, httpHeaders, methodName, aggiornaPagamentiPortaleDTOResponse, 200);
			
			if(aggiornaPagamentiPortaleDTOResponse.getLocation() != null) {
				this.log.info("Esecuzione " + methodName + " completata con redirect verso la URL ["+ aggiornaPagamentiPortaleDTOResponse.getLocation() +"].");	
				return Response.temporaryRedirect(new URI(aggiornaPagamentiPortaleDTOResponse.getLocation())).build();
			} else {
				this.log.info("Esecuzione " + methodName + " completata, html generato correttamente.");	
				return Response.ok(aggiornaPagamentiPortaleDTOResponse.getWispHtml()).build();
			}
		} catch (Exception e) {
			log.error("Errore interno durante l'esecuzione della funzionalita' di gateway: ", e);
			try {
				this.logResponse(uriInfo, httpHeaders, methodName, new byte[0], 500);
			}catch(Exception e1) {
				log.error("Errore durante il log della risposta", e1);
			}
			return Response.temporaryRedirect(gwErrorLocationURI).build();
//			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(respKo).build();
		} finally {
			if(ctx != null) ctx.log();
		}
	}
	
	@GET
	@Path("/v1/gw/{id}")
	@Produces({MediaType.TEXT_HTML})
	public Response get_GW(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @PathParam("id") String idSessione, @QueryParam("action") String action) {
		String methodName = "get_gateway";  
		GpContext ctx = null;
		ByteArrayOutputStream baos= null;
		this.log.info("Esecuzione " + methodName + " in corso..."); 
		String gwErrorLocation = null;
		URI gwErrorLocationURI = null;
		try{
			gwErrorLocation = GovpayConfig.getInstance().getUrlErrorGovpayWC();
			gwErrorLocationURI = new URI(gwErrorLocation);
			
			baos = new ByteArrayOutputStream();
			this.logRequest(uriInfo, httpHeaders, methodName, baos);
			
			ctx =  GpThreadLocal.get();
			String principal = this.getPrincipal();
			
			RichiestaWebControllerDTO aggiornaPagamentiPortaleDTO = new RichiestaWebControllerDTO();
			aggiornaPagamentiPortaleDTO.setIdSessione(idSessione);
			aggiornaPagamentiPortaleDTO.setPrincipal(principal);
			aggiornaPagamentiPortaleDTO.setAction(action);

			WebControllerDAO webControllerDAO = new WebControllerDAO(BasicBD.newInstance(ctx.getTransactionId()));
			
			RichiestaWebControllerDTOResponse aggiornaPagamentiPortaleDTOResponse = webControllerDAO.gestisciRichiestaWebController(aggiornaPagamentiPortaleDTO);
		
			this.logResponse(uriInfo, httpHeaders, methodName, aggiornaPagamentiPortaleDTOResponse, 200);
			
			if(aggiornaPagamentiPortaleDTOResponse.getLocation() != null) {
				this.log.info("Esecuzione " + methodName + " completata con redirect verso la URL ["+ aggiornaPagamentiPortaleDTOResponse.getLocation() +"].");	
				return Response.temporaryRedirect(new URI(aggiornaPagamentiPortaleDTOResponse.getLocation())).build();
			} else {
				this.log.info("Esecuzione " + methodName + " completata, html generato correttamente.");	
				return Response.ok(aggiornaPagamentiPortaleDTOResponse.getWispHtml()).build();
			}
		} catch (RedirectException e) {
			log.error("Esecuzione della funzionalita' di gateway si e' conclusa con un errore: " + e.getMessage() + ", redirect verso la url: " + e.getLocation(), e);
			return Response.temporaryRedirect(e.getURILocation()).build();
		}catch (Exception e) {
			log.error("Errore interno durante l'esecuzione della funzionalita' di gateway: ", e);
			try {
				this.logResponse(uriInfo, httpHeaders, methodName, new byte[0], 500);
			}catch(Exception e1) {
				log.error("Errore durante il log della risposta", e1);
			}
			return Response.temporaryRedirect(gwErrorLocationURI).build();
//			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(respKo).build();
		} finally {
			if(ctx != null) ctx.log();
		}
	}
	
	
	@GET
	@Path("/error")
	@Produces({MediaType.APPLICATION_JSON})
	public Response errorHandler(InputStream is , @Context UriInfo uriInfo, @Context HttpHeaders httpHeaders) {
		String methodName = "Handler Errori";  
		this.log.info("Esecuzione " + methodName + " in corso..."); 
		return Response.ok("Redirect alla url di error: Si e' verificato un errore").build();
	}
}
