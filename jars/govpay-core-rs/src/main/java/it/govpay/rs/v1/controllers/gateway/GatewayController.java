package it.govpay.rs.v1.controllers.gateway;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URI;
import java.text.MessageFormat;
import java.util.List;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.apache.http.Consts;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.slf4j.Logger;

import it.govpay.core.dao.pagamenti.WebControllerDAO;
import it.govpay.core.dao.pagamenti.dto.RichiestaWebControllerDTO;
import it.govpay.core.dao.pagamenti.dto.RichiestaWebControllerDTOResponse;
import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.GpThreadLocal;
import it.govpay.model.IAutorizzato;
import it.govpay.rs.BaseController;
import it.govpay.rs.BaseRsService;

public class GatewayController extends BaseController {

	private static final String LOG_MSG_ESECUZIONE_0_COMPLETATA_HTML_GENERATO_CORRETTAMENTE = "Esecuzione {0} completata, html generato correttamente.";
	private static final String LOG_MSG_ESECUZIONE_0_COMPLETATA_CON_REDIRECT_VERSO_LA_URL_1 = "Esecuzione {0} completata con redirect verso la URL [{1}].";


	public GatewayController(String nomeServizio, Logger log) {
		super(nomeServizio, log, null, false);
	}

	
	public Response post_GW(IAutorizzato user, InputStream is, UriInfo uriInfo, HttpHeaders httpHeaders, String idSessione, String action) {
		String methodName = "post_GW";  
		GpContext ctx = null;
		String transactionId = null;
		this.log.info(MessageFormat.format(it.govpay.rs.BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 
		ByteArrayOutputStream baos= null;
		try{
			baos = new ByteArrayOutputStream();
			// salvo il json ricevuto
			BaseRsService.copy(is, baos);
			this.setRequestResponse(this.request, this.response);
			this.logRequest(uriInfo, httpHeaders, methodName, baos);
			
			String principal = user != null ? user.getPrincipal() : null;
			
			List<NameValuePair> parametriBody = URLEncodedUtils.parse(baos.toString(), Consts.UTF_8);
			
			RichiestaWebControllerDTO aggiornaPagamentiPortaleDTO = new RichiestaWebControllerDTO();
			aggiornaPagamentiPortaleDTO.setIdSessione(idSessione);
			aggiornaPagamentiPortaleDTO.setPrincipal(principal);
			aggiornaPagamentiPortaleDTO.setAction(action);
			aggiornaPagamentiPortaleDTO.setParametriBody(parametriBody);
			
			this.log.trace("Parametri ricevuti: \n" + aggiornaPagamentiPortaleDTO.toString()); 
			
			ctx =  GpThreadLocal.get();
			transactionId = ctx.getTransactionId();

			WebControllerDAO webControllerDAO = new WebControllerDAO();
			
			RichiestaWebControllerDTOResponse aggiornaPagamentiPortaleDTOResponse = webControllerDAO.gestisciRichiestaWebController(aggiornaPagamentiPortaleDTO);
		
			this.logResponse(uriInfo, httpHeaders, methodName, aggiornaPagamentiPortaleDTOResponse, 200);
			
			if(aggiornaPagamentiPortaleDTOResponse.getLocation() != null) {
				this.log.info(MessageFormat.format(LOG_MSG_ESECUZIONE_0_COMPLETATA_CON_REDIRECT_VERSO_LA_URL_1, methodName, aggiornaPagamentiPortaleDTOResponse.getLocation()));	
				return this.handleResponseOk(Response.seeOther(new URI(aggiornaPagamentiPortaleDTOResponse.getLocation())),transactionId).build();
			} else {
				this.log.info(MessageFormat.format(LOG_MSG_ESECUZIONE_0_COMPLETATA_HTML_GENERATO_CORRETTAMENTE, methodName));	
				return this.handleResponseOk(Response.ok(aggiornaPagamentiPortaleDTOResponse.getWispHtml()),transactionId).build();
			}
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			if(ctx != null) ctx.log();
		}
	}
	
	
	public Response get_GW(IAutorizzato user, UriInfo uriInfo, HttpHeaders httpHeaders, String idSessione, String action, String idDominio,
			String keyPA, String keyWISP, String type) {
		String methodName = "get_GW";  
		GpContext ctx = null;
		String transactionId = null;
		this.log.info(MessageFormat.format(it.govpay.rs.BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 
		try{
			String principal = user != null ? user.getPrincipal() : null;
			
			RichiestaWebControllerDTO aggiornaPagamentiPortaleDTO = new RichiestaWebControllerDTO();
			aggiornaPagamentiPortaleDTO.setIdSessione(idSessione);
			aggiornaPagamentiPortaleDTO.setPrincipal(principal);
			aggiornaPagamentiPortaleDTO.setAction(action);
			aggiornaPagamentiPortaleDTO.setType(type);
			aggiornaPagamentiPortaleDTO.setWispDominio(idDominio);
			aggiornaPagamentiPortaleDTO.setWispKeyPA(keyPA);
			aggiornaPagamentiPortaleDTO.setWispKeyWisp(keyWISP);
			this.setRequestResponse(this.request, this.response);
			this.logRequest(uriInfo, httpHeaders, methodName, aggiornaPagamentiPortaleDTO.toString().getBytes());
			
			ctx =  GpThreadLocal.get();
			transactionId = ctx.getTransactionId();

			WebControllerDAO webControllerDAO = new WebControllerDAO();
			
			RichiestaWebControllerDTOResponse aggiornaPagamentiPortaleDTOResponse = webControllerDAO.gestisciRichiestaWebController(aggiornaPagamentiPortaleDTO);
		
			this.logResponse(uriInfo, httpHeaders, methodName, aggiornaPagamentiPortaleDTOResponse, 200);
			
			if(aggiornaPagamentiPortaleDTOResponse.getLocation() != null) {
				this.log.info(MessageFormat.format(LOG_MSG_ESECUZIONE_0_COMPLETATA_CON_REDIRECT_VERSO_LA_URL_1, methodName, aggiornaPagamentiPortaleDTOResponse.getLocation()));	
				return this.handleResponseOk(Response.seeOther(new URI(aggiornaPagamentiPortaleDTOResponse.getLocation())),transactionId).build();
			} else {
				this.log.info(MessageFormat.format(LOG_MSG_ESECUZIONE_0_COMPLETATA_HTML_GENERATO_CORRETTAMENTE, methodName));	
				return this.handleResponseOk(Response.ok(aggiornaPagamentiPortaleDTOResponse.getWispHtml()),transactionId).build();
			}
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			if(ctx != null) ctx.log();
		}
	}
}
