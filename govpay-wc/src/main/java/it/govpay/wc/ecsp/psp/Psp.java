package it.govpay.wc.ecsp.psp;

import java.io.ByteArrayOutputStream;
import java.net.URI;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import it.govpay.bd.BasicBD;
import it.govpay.core.dao.pagamenti.WebControllerDAO;
import it.govpay.core.dao.pagamenti.dto.RedirectDaPspDTO;
import it.govpay.core.dao.pagamenti.dto.RedirectDaPspDTOResponse;
import it.govpay.core.dao.pagamenti.exception.PagamentoPortaleNonTrovatoException;
import it.govpay.core.dao.pagamenti.exception.ParametriNonTrovatiException;
import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.GpThreadLocal;
import it.govpay.wc.ecsp.BaseRsService;

@Path("/")
public class Psp extends BaseRsService {

	@GET
	@Path("/psp")
	@Produces({MediaType.APPLICATION_JSON})
	public Response getPsp(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders, @QueryParam("idSession") String idSession, @QueryParam("esito") String esito) {
		String methodName = "get_gateway";  
		GpContext ctx = null;
		ByteArrayOutputStream baos= null;
		this.log.info("Esecuzione " + methodName + " in corso..."); 
		try{
			baos = new ByteArrayOutputStream();
			this.logRequest(uriInfo, httpHeaders, methodName, baos);
			
			ctx =  GpThreadLocal.get();
			String principal = this.getPrincipal();
			
			RedirectDaPspDTO redirectDaPspDTO = new RedirectDaPspDTO();
			redirectDaPspDTO.setEsito(esito);
			redirectDaPspDTO.setIdSession(idSession);
			redirectDaPspDTO.setPrincipal(principal);
			
			WebControllerDAO webControllerDAO = new WebControllerDAO(BasicBD.newInstance(ctx.getTransactionId()));
			
			RedirectDaPspDTOResponse redirectDaPspDTOResponse = webControllerDAO.gestisciRedirectPsp(redirectDaPspDTO);
			
			this.logResponse(uriInfo, httpHeaders, methodName, redirectDaPspDTOResponse, 200);
			
			this.log.info("Esecuzione " + methodName + " completata con redirect verso la URL ["+ redirectDaPspDTOResponse.getLocation() +"].");	
			return Response.temporaryRedirect(new URI(redirectDaPspDTOResponse.getLocation())).build();
			
		} catch (PagamentoPortaleNonTrovatoException e) {
			log.error("Esecuzione della funzionalita' di gateway si e' conclusa con un errore: " + e.getMessage() + ", restiuisco 404 - NotFound", e);
			return Response.status(Status.NOT_FOUND).build();
		} catch (ParametriNonTrovatiException e) {
			log.error("Esecuzione della funzionalita' di gateway si e' conclusa con un errore: " + e.getMessage() + ", redirect verso la url: " + e.getLocation(), e);
			return Response.temporaryRedirect(e.getURILocation()).build();
		}catch (Exception e) {
			log.error("Errore interno durante l'esecuzione della funzionalita' di gateway: ", e);
			try {
				this.logResponse(uriInfo, httpHeaders, methodName, new byte[0], 500);
			}catch(Exception e1) {
				log.error("Errore durante il log della risposta", e1);
			}
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		} finally {
			if(ctx != null) ctx.log();
		}
	}
}
