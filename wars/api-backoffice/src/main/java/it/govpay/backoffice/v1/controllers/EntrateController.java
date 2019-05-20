package it.govpay.backoffice.v1.controllers;

import java.io.ByteArrayOutputStream;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.stream.Collectors;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.springframework.security.core.Authentication;

import it.govpay.backoffice.v1.beans.ListaTipiEntrata;
import it.govpay.backoffice.v1.beans.TipoEntrata;
import it.govpay.backoffice.v1.beans.TipoEntrataPost;
import it.govpay.backoffice.v1.beans.converter.EntrateConverter;
import it.govpay.core.beans.JSONSerializable;
import it.govpay.core.dao.anagrafica.EntrateDAO;
import it.govpay.core.dao.anagrafica.dto.FindEntrateDTO;
import it.govpay.core.dao.anagrafica.dto.FindEntrateDTOResponse;
import it.govpay.core.dao.anagrafica.dto.GetEntrataDTO;
import it.govpay.core.dao.anagrafica.dto.GetEntrataDTOResponse;
import it.govpay.core.dao.anagrafica.dto.PutEntrataDTO;
import it.govpay.core.dao.anagrafica.dto.PutEntrataDTOResponse;
import it.govpay.core.utils.validator.ValidatoreIdentificativi;
import it.govpay.model.Acl.Diritti;
import it.govpay.model.Acl.Servizio;
import it.govpay.model.Utenza.TIPO_UTENZA;

public class EntrateController extends BaseController {

     public EntrateController(String nomeServizio,Logger log) {
    	 super(nomeServizio,log);
     }



    public Response entrateIdEntrataPUT(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , String idEntrata, java.io.InputStream is) {
    	String methodName = "entrateIdEntrataPUT";  
		String transactionId = this.context.getTransactionId();
		this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 
		try(ByteArrayOutputStream baos= new ByteArrayOutputStream();){
			// salvo il json ricevuto
			IOUtils.copy(is, baos);
			
			// autorizzazione sulla API
			this.isAuthorized(user, Arrays.asList(TIPO_UTENZA.OPERATORE, TIPO_UTENZA.APPLICAZIONE), Arrays.asList(Servizio.ANAGRAFICA_CREDITORE), Arrays.asList(Diritti.SCRITTURA));
			
			String jsonRequest = baos.toString();
			TipoEntrataPost entrataRequest= JSONSerializable.parse(jsonRequest, TipoEntrataPost.class);
			
			ValidatoreIdentificativi validatoreId = ValidatoreIdentificativi.newInstance();
			validatoreId.validaIdEntrata("idEntrata", idEntrata);
			
			entrataRequest.validate();
			
			PutEntrataDTO putIntermediarioDTO = EntrateConverter.getPutEntrataDTO(entrataRequest, idEntrata, user);
			
			EntrateDAO intermediariDAO = new EntrateDAO(false);
			
			PutEntrataDTOResponse putIntermediarioDTOResponse = intermediariDAO.createOrUpdateEntrata(putIntermediarioDTO);
			
			Status responseStatus = putIntermediarioDTOResponse.isCreated() ?  Status.CREATED : Status.OK;
			
			this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
			return this.handleResponseOk(Response.status(responseStatus),transactionId).build();
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.log(this.context);
		}
    }



    public Response entrateIdEntrataGET(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , String idEntrata) {
    	String methodName = "entrateIdEntrataGET";  
		String transactionId = this.context.getTransactionId();
		this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 
		try{
			// autorizzazione sulla API
			this.isAuthorized(user, Arrays.asList(TIPO_UTENZA.OPERATORE, TIPO_UTENZA.APPLICAZIONE), Arrays.asList(Servizio.ANAGRAFICA_CREDITORE), Arrays.asList(Diritti.LETTURA));
			
			ValidatoreIdentificativi validatoreId = ValidatoreIdentificativi.newInstance();
			validatoreId.validaIdEntrata("idEntrata", idEntrata);
			
			// Parametri - > DTO Input
			
			GetEntrataDTO getEntrataDTO = new GetEntrataDTO(user, idEntrata);
			
			// INIT DAO
			
			EntrateDAO entrateDAO = new EntrateDAO(false);
			
			// CHIAMATA AL DAO
			
			GetEntrataDTOResponse listaDominiEntrateDTOResponse = entrateDAO.getEntrata(getEntrataDTO);
			
			// CONVERT TO JSON DELLA RISPOSTA
			
			TipoEntrata response = EntrateConverter.toTipoEntrataRsModel(listaDominiEntrateDTOResponse.getTipoTributo());
			
			this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
			return this.handleResponseOk(Response.status(Status.OK).entity(response.toJSON(null)),transactionId).build();
			
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.log(this.context);
		}
    }



    public Response entrateGET(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , Integer pagina, Integer risultatiPerPagina, String ordinamento, String campi) {
    	String methodName = "entrateGET";  
		String transactionId = this.context.getTransactionId();
		this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 
		try{
			// autorizzazione sulla API
			this.isAuthorized(user, Arrays.asList(TIPO_UTENZA.OPERATORE, TIPO_UTENZA.APPLICAZIONE), Arrays.asList(Servizio.ANAGRAFICA_CREDITORE), Arrays.asList(Diritti.LETTURA));
			
			// Parametri - > DTO Input
			
			FindEntrateDTO findEntrateDTO = new FindEntrateDTO(user);
			findEntrateDTO.setLimit(risultatiPerPagina);
			findEntrateDTO.setPagina(pagina);
			findEntrateDTO.setOrderBy(ordinamento);
			
			// INIT DAO
			
			EntrateDAO entrateDAO = new EntrateDAO(false);
			
			// CHIAMATA AL DAO
			
			FindEntrateDTOResponse findEntrateDTOResponse = entrateDAO.findEntrate(findEntrateDTO);
			
			// CONVERT TO JSON DELLA RISPOSTA
			
			ListaTipiEntrata response = new ListaTipiEntrata(findEntrateDTOResponse.getResults().stream().map(t -> EntrateConverter.toTipoEntrataRsModel(t)).collect(Collectors.toList()), 
					this.getServicePath(uriInfo), findEntrateDTOResponse.getTotalResults(), pagina, risultatiPerPagina);
			
			this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
			return this.handleResponseOk(Response.status(Status.OK).entity(response.toJSON(campi)),transactionId).build();
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.log(this.context);
		}
    }


}


