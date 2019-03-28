package it.govpay.backoffice.v1.controllers;


import java.io.ByteArrayOutputStream;
import java.text.MessageFormat;
import java.util.stream.Collectors;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.io.IOUtils;
import org.openspcoop2.utils.service.context.IContext;
import org.slf4j.Logger;
import org.springframework.security.core.Authentication;

import it.govpay.backoffice.v1.beans.ListaTipiPendenza;
import it.govpay.backoffice.v1.beans.TipoPendenza;
import it.govpay.backoffice.v1.beans.TipoPendenzaPost;
import it.govpay.backoffice.v1.beans.converter.TipiPendenzaConverter;
import it.govpay.core.beans.JSONSerializable;
import it.govpay.core.dao.anagrafica.TipoPendenzaDAO;
import it.govpay.core.dao.anagrafica.dto.FindTipiPendenzaDTO;
import it.govpay.core.dao.anagrafica.dto.FindTipiPendenzaDTOResponse;
import it.govpay.core.dao.anagrafica.dto.GetTipoPendenzaDTO;
import it.govpay.core.dao.anagrafica.dto.GetTipoPendenzaDTOResponse;
import it.govpay.core.dao.anagrafica.dto.PutTipoPendenzaDTO;
import it.govpay.core.dao.anagrafica.dto.PutTipoPendenzaDTOResponse;
import it.govpay.core.utils.GovpayConfig;
import it.govpay.core.utils.GpThreadLocal;
import it.govpay.core.utils.validator.ValidatoreIdentificativi;



public class TipiPendenzaController extends BaseController {

     public TipiPendenzaController(String nomeServizio,Logger log) {
    	 super(nomeServizio,log, GovpayConfig.GOVPAY_BACKOFFICE_OPEN_API_FILE_NAME);
     }



    public Response tipiPendenzaGET(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , Integer pagina, Integer risultatiPerPagina, String ordinamento, String campi) {
    	String methodName = "tipiPendenzaGET";  
		IContext ctx = null;
		String transactionId = null;
		ByteArrayOutputStream baos= null;
		this.log.info(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 
		try{
			baos = new ByteArrayOutputStream();
			this.logRequest(uriInfo, httpHeaders, methodName, baos);
			
			ctx =  GpThreadLocal.get();
			transactionId = ctx.getTransactionId();
			
			// Parametri - > DTO Input
			
			FindTipiPendenzaDTO findTipiPendenzaDTO = new FindTipiPendenzaDTO(user);
			findTipiPendenzaDTO.setOrderBy(ordinamento);
			
			
			// INIT DAO
			
			TipoPendenzaDAO tipiPendenzaDAO = new TipoPendenzaDAO(false);
			
			// CHIAMATA AL DAO
			
			FindTipiPendenzaDTOResponse findTipiPendenzaDTOResponse = tipiPendenzaDAO.findTipiPendenza(findTipiPendenzaDTO);
			
			// CONVERT TO JSON DELLA RISPOSTA
			
			ListaTipiPendenza response = new ListaTipiPendenza(findTipiPendenzaDTOResponse.getResults().stream().map(t -> TipiPendenzaConverter.toTipoPendenzaRsModel(t)).collect(Collectors.toList()), 
					this.getServicePath(uriInfo), findTipiPendenzaDTOResponse.getTotalResults(), pagina, risultatiPerPagina);
			
			this.logResponse(uriInfo, httpHeaders, methodName, response.toJSON(campi), 200);
			this.log.info(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
			return this.handleResponseOk(Response.status(Status.OK).entity(response.toJSON(campi)),transactionId).build();
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.log(ctx);
		}
    }



    public Response tipiPendenzaIdTipoPendenzaGET(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , String idTipoPendenza) {
    	String methodName = "tipiPendenzaIdTipoPendenzaGET";  
		IContext ctx = null;
		String transactionId = null;
		ByteArrayOutputStream baos= null;
		this.log.info(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 
		try{
			baos = new ByteArrayOutputStream();
			this.logRequest(uriInfo, httpHeaders, methodName, baos);
			
			ctx =  GpThreadLocal.get();
			transactionId = ctx.getTransactionId();
			
			ValidatoreIdentificativi validatoreId = ValidatoreIdentificativi.newInstance();
			validatoreId.validaIdTipoVersamento("idTipoPendenza", idTipoPendenza);
			
			// Parametri - > DTO Input
			
			GetTipoPendenzaDTO getTipoPendenzaDTO = new GetTipoPendenzaDTO(user, idTipoPendenza);
			
			// INIT DAO
			
			TipoPendenzaDAO tipiPendenzaDAO = new TipoPendenzaDAO(false);
			
			// CHIAMATA AL DAO
			
			GetTipoPendenzaDTOResponse listaDominiTipiPendenzaDTOResponse = tipiPendenzaDAO.getTipoPendenza(getTipoPendenzaDTO);
			
			// CONVERT TO JSON DELLA RISPOSTA
			
			TipoPendenza response = TipiPendenzaConverter.toTipoPendenzaRsModel(listaDominiTipiPendenzaDTOResponse.getTipoVersamento());
			
			this.logResponse(uriInfo, httpHeaders, methodName, response.toJSON(null), 200);
			this.log.info(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
			return this.handleResponseOk(Response.status(Status.OK).entity(response.toJSON(null)),transactionId).build();
			
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.log(ctx);
		}
    }



    public Response tipiPendenzaIdTipoPendenzaPUT(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , String idTipoPendenza, java.io.InputStream is) {
    	String methodName = "tipiPendenzaIdTipoPendenzaPUT";  
		IContext ctx = null;
		String transactionId = null;
		ByteArrayOutputStream baos= null;
		this.log.info(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 
		try{
			baos = new ByteArrayOutputStream();
			// salvo il json ricevuto
			IOUtils.copy(is, baos);
			this.logRequest(uriInfo, httpHeaders, methodName, baos);
			
			ctx =  GpThreadLocal.get();
			transactionId = ctx.getTransactionId();
			
			String jsonRequest = baos.toString();
			TipoPendenzaPost tipoPendenzaRequest= JSONSerializable.parse(jsonRequest, TipoPendenzaPost.class);
			
			ValidatoreIdentificativi validatoreId = ValidatoreIdentificativi.newInstance();
			validatoreId.validaIdTipoVersamento("idTipoPendenza", idTipoPendenza);
			
			tipoPendenzaRequest.validate();
			
			PutTipoPendenzaDTO putIntermediarioDTO = TipiPendenzaConverter.getPutTipoPendenzaDTO(tipoPendenzaRequest, idTipoPendenza, user);
			
			TipoPendenzaDAO intermediariDAO = new TipoPendenzaDAO(false);
			
			PutTipoPendenzaDTOResponse putIntermediarioDTOResponse = intermediariDAO.createOrUpdateTipoPendenza(putIntermediarioDTO);
			
			Status responseStatus = putIntermediarioDTOResponse.isCreated() ?  Status.CREATED : Status.OK;
			
			this.logResponse(uriInfo, httpHeaders, methodName, new byte[0], responseStatus.getStatusCode());
			this.log.info(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
			return this.handleResponseOk(Response.status(responseStatus),transactionId).build();
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.log(ctx);
		}
    }


}


