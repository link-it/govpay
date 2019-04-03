package it.govpay.backoffice.v1.controllers;

import java.io.ByteArrayOutputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.io.IOUtils;
import org.openspcoop2.utils.service.context.IContext;
import org.slf4j.Logger;
import org.springframework.security.core.Authentication;

import it.govpay.backoffice.v1.beans.Intermediario;
import it.govpay.backoffice.v1.beans.IntermediarioIndex;
import it.govpay.backoffice.v1.beans.IntermediarioPost;
import it.govpay.backoffice.v1.beans.ListaIntermediari;
import it.govpay.backoffice.v1.beans.ListaStazioni;
import it.govpay.backoffice.v1.beans.Stazione;
import it.govpay.backoffice.v1.beans.StazioneIndex;
import it.govpay.backoffice.v1.beans.StazionePost;
import it.govpay.backoffice.v1.beans.converter.IntermediariConverter;
import it.govpay.backoffice.v1.beans.converter.StazioniConverter;
import it.govpay.core.autorizzazione.AuthorizationManager;
import it.govpay.core.beans.JSONSerializable;
import it.govpay.core.dao.anagrafica.IntermediariDAO;
import it.govpay.core.dao.anagrafica.dto.FindIntermediariDTO;
import it.govpay.core.dao.anagrafica.dto.FindIntermediariDTOResponse;
import it.govpay.core.dao.anagrafica.dto.FindStazioniDTO;
import it.govpay.core.dao.anagrafica.dto.FindStazioniDTOResponse;
import it.govpay.core.dao.anagrafica.dto.GetIntermediarioDTO;
import it.govpay.core.dao.anagrafica.dto.GetIntermediarioDTOResponse;
import it.govpay.core.dao.anagrafica.dto.GetStazioneDTO;
import it.govpay.core.dao.anagrafica.dto.GetStazioneDTOResponse;
import it.govpay.core.dao.anagrafica.dto.PutIntermediarioDTO;
import it.govpay.core.dao.anagrafica.dto.PutIntermediarioDTOResponse;
import it.govpay.core.dao.anagrafica.dto.PutStazioneDTO;
import it.govpay.core.dao.anagrafica.dto.PutStazioneDTOResponse;
import it.govpay.core.utils.GovpayConfig;
import it.govpay.core.utils.GpThreadLocal;
import it.govpay.core.utils.validator.ValidatoreIdentificativi;
import it.govpay.model.Acl.Diritti;
import it.govpay.model.Acl.Servizio;
import it.govpay.model.Utenza.TIPO_UTENZA;

public class IntermediariController extends BaseController {

     public IntermediariController(String nomeServizio,Logger log) {
		super(nomeServizio,log, GovpayConfig.GOVPAY_BACKOFFICE_OPEN_API_FILE_NAME);
     }



    public Response intermediariIdIntermediarioGET(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , String idIntermediario) {
    	String methodName = "intermediariIdIntermediarioGET";  
		IContext ctx = null;
		String transactionId = null;
		ByteArrayOutputStream baos= null;
		this.log.info(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 
		try{
			baos = new ByteArrayOutputStream();
			this.logRequest(uriInfo, httpHeaders, methodName, baos);
			
			ctx =  GpThreadLocal.get();
			transactionId = ctx.getTransactionId();
			
			// autorizzazione sulla API
			AuthorizationManager.isAuthorized(user, Arrays.asList(TIPO_UTENZA.OPERATORE, TIPO_UTENZA.APPLICAZIONE), Arrays.asList(Servizio.ANAGRAFICA_PAGOPA), Arrays.asList(Diritti.LETTURA));
			
			ValidatoreIdentificativi validatoreId = ValidatoreIdentificativi.newInstance();
			validatoreId.validaIdIntermediario("idIntermediario", idIntermediario);
			
			// Parametri - > DTO Input
			
			GetIntermediarioDTO getIntermediarioDTO = new GetIntermediarioDTO(user, idIntermediario);
			
			// INIT DAO
			
			IntermediariDAO intermediariDAO = new IntermediariDAO(false);
			
			// CHIAMATA AL DAO
			
			GetIntermediarioDTOResponse getIntermediarioDTOResponse = intermediariDAO.getIntermediario(getIntermediarioDTO);
			
			
			FindStazioniDTO listaStazioniDTO = new FindStazioniDTO(user);
			
			listaStazioniDTO.setPagina(1);
			listaStazioniDTO.setLimit(25);
			listaStazioniDTO.setCodIntermediario(idIntermediario);
			FindStazioniDTOResponse listaStazioniDTOResponse = intermediariDAO.findStazioni(listaStazioniDTO);
			
			List<StazioneIndex> listaStazioni = new ArrayList<>();
			for(it.govpay.bd.model.Stazione stazione: listaStazioniDTOResponse.getResults()) {
				listaStazioni.add(StazioniConverter.toRsModelIndex(stazione));
			}
			
			// CONVERT TO JSON DELLA RISPOSTA
			Intermediario response = IntermediariConverter.toRsModel(getIntermediarioDTOResponse.getIntermediario());
			
			response.setStazioni(listaStazioni);
			
			this.logResponse(uriInfo, httpHeaders, methodName, response.toJSON(null), 200);
			this.log.info(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
			return this.handleResponseOk(Response.status(Status.OK).entity(response.toJSON(null)),transactionId).build();
			
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.log(ctx);
		}
    }



    public Response intermediariIdIntermediarioStazioniIdStazionePUT(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , String idIntermediario, String idStazione, java.io.InputStream is) {
    	String methodName = "intermediariIdIntermediarioStazioniIdStazionePUT";  
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
			
			// autorizzazione sulla API
			AuthorizationManager.isAuthorized(user, Arrays.asList(TIPO_UTENZA.OPERATORE, TIPO_UTENZA.APPLICAZIONE), Arrays.asList(Servizio.ANAGRAFICA_PAGOPA), Arrays.asList(Diritti.SCRITTURA));
			
			String jsonRequest = baos.toString();
			StazionePost stazioneRequest= JSONSerializable.parse(jsonRequest, StazionePost.class);
			
			
			ValidatoreIdentificativi validatoreId = ValidatoreIdentificativi.newInstance();
			validatoreId.validaIdIntermediario("idIntermediario", idIntermediario);
			validatoreId.validaIdStazione("idStazione", idStazione);
			
			stazioneRequest.validate();
			
			PutStazioneDTO putStazioneDTO = StazioniConverter.getPutStazioneDTO(stazioneRequest, idIntermediario, idStazione, user);
			
			IntermediariDAO intermediariDAO = new IntermediariDAO(false);
			
			PutStazioneDTOResponse putIntermediarioDTOResponse = intermediariDAO.createOrUpdateStazione(putStazioneDTO);
			
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



    public Response intermediariGET(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , Integer pagina, Integer risultatiPerPagina, String ordinamento, String campi, Boolean abilitato) {
    	String methodName = "intermediariGET";  
		IContext ctx = null;
		String transactionId = null;
		ByteArrayOutputStream baos= null;
		this.log.info(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 
		try{
			baos = new ByteArrayOutputStream();
			this.logRequest(uriInfo, httpHeaders, methodName, baos);
			
			ctx =  GpThreadLocal.get();
			transactionId = ctx.getTransactionId();
			
			// autorizzazione sulla API
			AuthorizationManager.isAuthorized(user, Arrays.asList(TIPO_UTENZA.OPERATORE, TIPO_UTENZA.APPLICAZIONE), Arrays.asList(Servizio.ANAGRAFICA_PAGOPA), Arrays.asList(Diritti.LETTURA));
			
			// Parametri - > DTO Input
			
			FindIntermediariDTO listaIntermediariDTO = new FindIntermediariDTO(user);
			
			listaIntermediariDTO.setPagina(pagina);
			listaIntermediariDTO.setLimit(risultatiPerPagina);
			listaIntermediariDTO.setOrderBy(ordinamento);
			listaIntermediariDTO.setAbilitato(abilitato);
			
			// INIT DAO
			
			IntermediariDAO intermediariDAO = new IntermediariDAO(false);
			
			// CHIAMATA AL DAO
			
			FindIntermediariDTOResponse listaIntermediariDTOResponse = intermediariDAO.findIntermediari(listaIntermediariDTO);
			
			// CONVERT TO JSON DELLA RISPOSTA
			
			List<IntermediarioIndex> results = new ArrayList<>();
			for(it.govpay.model.Intermediario intermediario: listaIntermediariDTOResponse.getResults()) {
				results.add(IntermediariConverter.toRsModelIndex(intermediario));
			}
			
			ListaIntermediari response = new ListaIntermediari(results, this.getServicePath(uriInfo),
					listaIntermediariDTOResponse.getTotalResults(), pagina, risultatiPerPagina);
			
			this.logResponse(uriInfo, httpHeaders, methodName, response.toJSON(campi), 200);
			this.log.info(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
			return this.handleResponseOk(Response.status(Status.OK).entity(response.toJSON(campi)),transactionId).build();
			
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.log(ctx);
		}
    }



    public Response intermediariIdIntermediarioPUT(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , String idIntermediario, java.io.InputStream is) {
    	String methodName = "intermediariIdIntermediarioPUT";  
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
			
			// autorizzazione sulla API
			AuthorizationManager.isAuthorized(user, Arrays.asList(TIPO_UTENZA.OPERATORE, TIPO_UTENZA.APPLICAZIONE), Arrays.asList(Servizio.ANAGRAFICA_PAGOPA), Arrays.asList(Diritti.SCRITTURA));
						
			String jsonRequest = baos.toString();
			IntermediarioPost intermediarioRequest= JSONSerializable.parse(jsonRequest, IntermediarioPost.class);
			
			ValidatoreIdentificativi validatoreId = ValidatoreIdentificativi.newInstance();
			validatoreId.validaIdIntermediario("idIntermediario", idIntermediario);
						
			intermediarioRequest.validate();
			
			PutIntermediarioDTO putIntermediarioDTO = IntermediariConverter.getPutIntermediarioDTO(intermediarioRequest, idIntermediario, user);
			
			IntermediariDAO intermediariDAO = new IntermediariDAO(false);
			
			PutIntermediarioDTOResponse putIntermediarioDTOResponse = intermediariDAO.createOrUpdateIntermediario(putIntermediarioDTO);
			
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



    public Response intermediariIdIntermediarioStazioniGET(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , String idIntermediario, Integer pagina, Integer risultatiPerPagina, String ordinamento, String campi, Boolean abilitato) {
    	String methodName = "intermediariIdIntermediarioStazioniGET";  
		IContext ctx = null;
		String transactionId = null;
		ByteArrayOutputStream baos= null;
		this.log.info(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 
		try{
			baos = new ByteArrayOutputStream();
			this.logRequest(uriInfo, httpHeaders, methodName, baos);
			
			ctx =  GpThreadLocal.get();
			transactionId = ctx.getTransactionId();
			
			// autorizzazione sulla API
			AuthorizationManager.isAuthorized(user, Arrays.asList(TIPO_UTENZA.OPERATORE, TIPO_UTENZA.APPLICAZIONE), Arrays.asList(Servizio.ANAGRAFICA_PAGOPA), Arrays.asList(Diritti.LETTURA));
			
			ValidatoreIdentificativi validatoreId = ValidatoreIdentificativi.newInstance();
			validatoreId.validaIdIntermediario("idIntermediario", idIntermediario);
			
			// Parametri - > DTO Input
			
			FindStazioniDTO listaStazioniDTO = new FindStazioniDTO(user);
			
			listaStazioniDTO.setPagina(pagina);
			listaStazioniDTO.setLimit(risultatiPerPagina);
			listaStazioniDTO.setOrderBy(ordinamento);
			listaStazioniDTO.setAbilitato(abilitato);
			listaStazioniDTO.setCodIntermediario(idIntermediario);
			
			// INIT DAO
			
			IntermediariDAO intermediariDAO = new IntermediariDAO(false);
			
			// CHIAMATA AL DAO
			
			FindStazioniDTOResponse listaStazioniDTOResponse = intermediariDAO.findStazioni(listaStazioniDTO);
			
			// CONVERT TO JSON DELLA RISPOSTA
			
			List<StazioneIndex> results = new ArrayList<>();
			for(it.govpay.bd.model.Stazione stazione: listaStazioniDTOResponse.getResults()) {
				results.add(StazioniConverter.toRsModelIndex(stazione));
			}
			
			ListaStazioni response = new ListaStazioni(results, this.getServicePath(uriInfo),
					listaStazioniDTOResponse.getTotalResults(), pagina, risultatiPerPagina);
			
			this.logResponse(uriInfo, httpHeaders, methodName, response.toJSON(campi), 200);
			this.log.info(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
			return this.handleResponseOk(Response.status(Status.OK).entity(response.toJSON(campi)),transactionId).build();
			
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.log(ctx);
		}
    }



    public Response intermediariIdIntermediarioStazioniIdStazioneGET(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , String idIntermediario, String idStazione) {
    	String methodName = "intermediariIdIntermediarioStazioniIdStazioneGET";  
		IContext ctx = null;
		String transactionId = null;
		ByteArrayOutputStream baos= null;
		this.log.info(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 
		try{
			baos = new ByteArrayOutputStream();
			this.logRequest(uriInfo, httpHeaders, methodName, baos);
			
			ctx =  GpThreadLocal.get();
			transactionId = ctx.getTransactionId();
			
			// autorizzazione sulla API
			AuthorizationManager.isAuthorized(user, Arrays.asList(TIPO_UTENZA.OPERATORE, TIPO_UTENZA.APPLICAZIONE), Arrays.asList(Servizio.ANAGRAFICA_PAGOPA), Arrays.asList(Diritti.LETTURA));
			
			ValidatoreIdentificativi validatoreId = ValidatoreIdentificativi.newInstance();
			validatoreId.validaIdIntermediario("idIntermediario", idIntermediario);
			validatoreId.validaIdStazione("idStazione", idStazione);
			
			// Parametri - > DTO Input
			
			GetStazioneDTO getStazioneDTO = new GetStazioneDTO(user, idIntermediario, idStazione);
			
			// INIT DAO
			
			IntermediariDAO intermediariDAO = new IntermediariDAO(false);
			
			// CHIAMATA AL DAO
			
			GetStazioneDTOResponse getStazioneDTOResponse = intermediariDAO.getStazione(getStazioneDTO);
			
			// CONVERT TO JSON DELLA RISPOSTA
			
			Stazione response = StazioniConverter.toRsModel(getStazioneDTOResponse.getStazione(), getStazioneDTOResponse.getDomini());
			
			this.logResponse(uriInfo, httpHeaders, methodName, response.toJSON(null), 200);
			this.log.info(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
			return this.handleResponseOk(Response.status(Status.OK).entity(response.toJSON(null)),transactionId).build();
			
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.log(ctx);
		}
    }


}


