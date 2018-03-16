package it.govpay.rs.v1.controllers.gateway;

import java.io.ByteArrayOutputStream;
import java.net.URI;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.slf4j.Logger;

import it.govpay.core.dao.pagamenti.WebControllerDAO;
import it.govpay.core.dao.pagamenti.dto.RedirectDaPspDTO;
import it.govpay.core.dao.pagamenti.dto.RedirectDaPspDTOResponse;
import it.govpay.core.dao.pagamenti.exception.PagamentoPortaleNonTrovatoException;
import it.govpay.core.dao.pagamenti.exception.ParametriNonTrovatiException;
import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.GpThreadLocal;
import it.govpay.model.IAutorizzato;
import it.govpay.rs.BaseController;

public class PspController  extends BaseController {

	public PspController(String nomeServizio, Logger log) {
		super(nomeServizio, log);
	}

	public Response getPsp(IAutorizzato user, UriInfo uriInfo, HttpHeaders httpHeaders, String idSession, String esito) {
		String methodName = "getPsp";  
		GpContext ctx = null;
		ByteArrayOutputStream baos= null;
		this.log.info("Esecuzione " + methodName + " in corso..."); 
		try{
			baos = new ByteArrayOutputStream();
			this.setRequestResponse(this.request, this.response);
			this.logRequest(uriInfo, httpHeaders, methodName, baos);
			
			ctx =  GpThreadLocal.get();
			String principal = user != null ? user.getPrincipal() : null;
			
			RedirectDaPspDTO redirectDaPspDTO = new RedirectDaPspDTO();
			redirectDaPspDTO.setEsito(esito);
			redirectDaPspDTO.setIdSession(idSession);
			redirectDaPspDTO.setPrincipal(principal);
			
			WebControllerDAO webControllerDAO = new WebControllerDAO();
			
			RedirectDaPspDTOResponse redirectDaPspDTOResponse = webControllerDAO.gestisciRedirectPsp(redirectDaPspDTO);
			
			this.logResponse(uriInfo, httpHeaders, methodName, redirectDaPspDTOResponse, 200);
			
			this.log.info("Esecuzione " + methodName + " completata con redirect verso la URL ["+ redirectDaPspDTOResponse.getLocation() +"].");	
			return Response.seeOther(new URI(redirectDaPspDTOResponse.getLocation())).build();
			
		} catch (PagamentoPortaleNonTrovatoException e) {
			log.error("Esecuzione della funzionalita' di gateway si e' conclusa con un errore: " + e.getMessage() + ", restiuisco 404 - NotFound", e);
			return Response.status(Status.NOT_FOUND).build();
		} catch (ParametriNonTrovatiException e) {
			log.error("Esecuzione della funzionalita' di gateway si e' conclusa con un errore: " + e.getMessage() + ", redirect verso la url: " + e.getLocation(), e);
			return Response.seeOther(e.getURILocation()).build();
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
