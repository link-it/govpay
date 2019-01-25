package it.govpay.wc.controller;

import java.io.ByteArrayOutputStream;
import java.net.URI;
import java.text.MessageFormat;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.slf4j.Logger;
import org.springframework.security.core.Authentication;

import it.govpay.core.autorizzazione.utils.AutorizzazioneUtils;
import it.govpay.core.dao.pagamenti.WebControllerDAO;
import it.govpay.core.dao.pagamenti.dto.RedirectDaPspDTO;
import it.govpay.core.dao.pagamenti.dto.RedirectDaPspDTOResponse;
import org.openspcoop2.utils.service.context.IContext;
import it.govpay.core.utils.GpThreadLocal;

public class WcController  extends BaseController {

	public WcController(String nomeServizio, Logger log) {
		super(nomeServizio, log, null, false);
	}

	public Response getPsp(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders, String idSession, String esito) {
		String methodName = "getPsp";  
		IContext ctx = null;
		String transactionId = null;
		ByteArrayOutputStream baos= null;
		this.log.info(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 
		try{
			baos = new ByteArrayOutputStream();
			this.setRequestResponse(this.request, this.response);
			this.logRequest(uriInfo, httpHeaders, methodName, baos);
			
			ctx =  GpThreadLocal.get();
			transactionId = ctx.getTransactionId();
			String principal = AutorizzazioneUtils.getPrincipal(user);
			
			RedirectDaPspDTO redirectDaPspDTO = new RedirectDaPspDTO();
			redirectDaPspDTO.setEsito(esito);
			redirectDaPspDTO.setIdSession(idSession);
			redirectDaPspDTO.setPrincipal(principal);
			
			WebControllerDAO webControllerDAO = new WebControllerDAO();
			
			RedirectDaPspDTOResponse redirectDaPspDTOResponse = webControllerDAO.gestisciRedirectPsp(redirectDaPspDTO);
			
			this.logResponse(uriInfo, httpHeaders, methodName, redirectDaPspDTOResponse, 200);
			
			this.log.info("Esecuzione " + methodName + " completata con redirect verso la URL ["+ redirectDaPspDTOResponse.getLocation() +"].");	
			return this.handleResponseOk(Response.seeOther(new URI(redirectDaPspDTOResponse.getLocation())),transactionId).build();
			
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.log(ctx);
		}
	}
}
