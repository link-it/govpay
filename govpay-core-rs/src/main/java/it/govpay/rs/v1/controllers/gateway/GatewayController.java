package it.govpay.rs.v1.controllers.gateway;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URI;
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

	public GatewayController(String nomeServizio, Logger log) {
		super(nomeServizio, log, false);
	}

	
	public Response post_GW(IAutorizzato user, InputStream is, UriInfo uriInfo, HttpHeaders httpHeaders, String idSessione, String action) {
		String methodName = "post_GW";  
		GpContext ctx = null;
		String transactionId = null;
		this.log.info("Esecuzione " + methodName + " in corso..."); 
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
				this.log.info("Esecuzione " + methodName + " completata con redirect verso la URL ["+ aggiornaPagamentiPortaleDTOResponse.getLocation() +"].");	
				return this.handleResponseOk(Response.seeOther(new URI(aggiornaPagamentiPortaleDTOResponse.getLocation())),transactionId).build();
			} else {
				this.log.info("Esecuzione " + methodName + " completata, html generato correttamente.");	
				return this.handleResponseOk(Response.ok(aggiornaPagamentiPortaleDTOResponse.getWispHtml()),transactionId).build();
			}
		}catch (Exception e) {
			return handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			if(ctx != null) ctx.log();
		}
	}
	
	
	public Response get_GW(IAutorizzato user, UriInfo uriInfo, HttpHeaders httpHeaders, String idSessione, String action, String idDominio,
			String keyPA, String keyWISP, String type) {
		String methodName = "get_GW";  
		GpContext ctx = null;
		String transactionId = null;
		this.log.info("Esecuzione " + methodName + " in corso..."); 
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
				this.log.info("Esecuzione " + methodName + " completata con redirect verso la URL ["+ aggiornaPagamentiPortaleDTOResponse.getLocation() +"].");	
				return this.handleResponseOk(Response.seeOther(new URI(aggiornaPagamentiPortaleDTOResponse.getLocation())),transactionId).build();
			} else {
				this.log.info("Esecuzione " + methodName + " completata, html generato correttamente.");	
				return this.handleResponseOk(Response.ok(aggiornaPagamentiPortaleDTOResponse.getWispHtml()),transactionId).build();
			}
		}catch (Exception e) {
			return handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			if(ctx != null) ctx.log();
		}
	}
}
