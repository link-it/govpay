package it.govpay.wc.controller;

import java.net.URI;
import java.text.MessageFormat;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.slf4j.Logger;
import org.springframework.security.core.Authentication;

import it.govpay.core.dao.pagamenti.WebControllerDAO;
import it.govpay.core.dao.pagamenti.dto.RedirectDaPspDTO;
import it.govpay.core.dao.pagamenti.dto.RedirectDaPspDTOResponse;

public class WcController  extends BaseController {

	public WcController(String nomeServizio, Logger log) {
		super(nomeServizio, log);
	}

	public Response getPsp(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders, String idSession, String esito) {
		String methodName = "getPsp";  
		String transactionId = this.context.getTransactionId();
		this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 
		try{
//			this.setRequestResponse(this.request, this.response);
			
			RedirectDaPspDTO redirectDaPspDTO = new RedirectDaPspDTO();
			redirectDaPspDTO.setEsito(esito);
			redirectDaPspDTO.setIdSession(idSession);
			WebControllerDAO webControllerDAO = new WebControllerDAO();
			
			RedirectDaPspDTOResponse redirectDaPspDTOResponse = webControllerDAO.gestisciRedirectPsp(redirectDaPspDTO);
			
			this.log.info("Esecuzione " + methodName + " completata con redirect verso la URL ["+ redirectDaPspDTOResponse.getLocation() +"].");	
			return this.handleResponseOk(Response.seeOther(new URI(redirectDaPspDTOResponse.getLocation())),transactionId).build();
			
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.log(this.context);
		}
	}
}
