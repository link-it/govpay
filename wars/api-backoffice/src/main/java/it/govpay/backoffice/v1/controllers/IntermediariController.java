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
import org.openspcoop2.utils.service.context.ContextThreadLocal;
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
import it.govpay.core.beans.Costanti;
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
import it.govpay.core.utils.validator.ValidatorFactory;
import it.govpay.core.utils.validator.ValidatoreIdentificativi;
import it.govpay.core.utils.validator.ValidatoreUtils;
import it.govpay.model.Acl.Diritti;
import it.govpay.model.Acl.Servizio;
import it.govpay.model.Utenza.TIPO_UTENZA;

public class IntermediariController extends BaseController {

     public IntermediariController(String nomeServizio,Logger log) {
		super(nomeServizio,log);
     }



    public Response getIntermediario(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , String idIntermediario) {
    	String methodName = "getIntermediario";  
		String transactionId = ContextThreadLocal.get().getTransactionId();
		this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 
		try{
			// autorizzazione sulla API
			this.isAuthorized(user, Arrays.asList(TIPO_UTENZA.OPERATORE, TIPO_UTENZA.APPLICAZIONE), Arrays.asList(Servizio.ANAGRAFICA_PAGOPA), Arrays.asList(Diritti.LETTURA));
			
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
			
			this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
			return this.handleResponseOk(Response.status(Status.OK).entity(response.toJSON(null)),transactionId).build();
			
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.log(ContextThreadLocal.get());
		}
    }



    public Response addStazione(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , String idIntermediario, String idStazione, java.io.InputStream is) {
    	String methodName = "addStazione";  
		String transactionId = ContextThreadLocal.get().getTransactionId();
		this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 
		try(ByteArrayOutputStream baos= new ByteArrayOutputStream();){
			// salvo il json ricevuto
			IOUtils.copy(is, baos);

			// autorizzazione sulla API
			this.isAuthorized(user, Arrays.asList(TIPO_UTENZA.OPERATORE, TIPO_UTENZA.APPLICAZIONE), Arrays.asList(Servizio.ANAGRAFICA_PAGOPA), Arrays.asList(Diritti.SCRITTURA));
			
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
			
			this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
			return this.handleResponseOk(Response.status(responseStatus),transactionId).build();
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.log(ContextThreadLocal.get());
		}
    }



    public Response findIntermediari(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , Integer pagina, Integer risultatiPerPagina, String ordinamento, String campi, Boolean abilitato, Boolean metadatiPaginazione, Boolean maxRisultati) {
    	String methodName = "findIntermediari";  
		String transactionId = ContextThreadLocal.get().getTransactionId();
		this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 
		try{
			// autorizzazione sulla API
			this.isAuthorized(user, Arrays.asList(TIPO_UTENZA.OPERATORE, TIPO_UTENZA.APPLICAZIONE), Arrays.asList(Servizio.ANAGRAFICA_PAGOPA), Arrays.asList(Diritti.LETTURA));
			
			ValidatorFactory vf = ValidatorFactory.newInstance();
			ValidatoreUtils.validaRisultatiPerPagina(vf, Costanti.PARAMETRO_RISULTATI_PER_PAGINA, risultatiPerPagina);
			
			// Parametri - > DTO Input
			
			FindIntermediariDTO listaIntermediariDTO = new FindIntermediariDTO(user);
			
			listaIntermediariDTO.setLimit(risultatiPerPagina);
			listaIntermediariDTO.setPagina(pagina);
			listaIntermediariDTO.setOrderBy(ordinamento);
			listaIntermediariDTO.setAbilitato(abilitato);
			
			listaIntermediariDTO.setEseguiCount(metadatiPaginazione);
			listaIntermediariDTO.setEseguiCountConLimit(maxRisultati);
			
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
			
			this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
			return this.handleResponseOk(Response.status(Status.OK).entity(response.toJSON(campi)),transactionId).build();
			
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.log(ContextThreadLocal.get());
		}
    }



    public Response addIntermediario(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , String idIntermediario, java.io.InputStream is) {
    	String methodName = "addIntermediario";  
		String transactionId = ContextThreadLocal.get().getTransactionId();
		this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 
		try(ByteArrayOutputStream baos= new ByteArrayOutputStream();){
			// salvo il json ricevuto
			IOUtils.copy(is, baos);

			// autorizzazione sulla API
			this.isAuthorized(user, Arrays.asList(TIPO_UTENZA.OPERATORE, TIPO_UTENZA.APPLICAZIONE), Arrays.asList(Servizio.ANAGRAFICA_PAGOPA), Arrays.asList(Diritti.SCRITTURA));
						
			String jsonRequest = baos.toString();
			IntermediarioPost intermediarioRequest= JSONSerializable.parse(jsonRequest, IntermediarioPost.class);
			
			ValidatoreIdentificativi validatoreId = ValidatoreIdentificativi.newInstance();
			validatoreId.validaIdIntermediario("idIntermediario", idIntermediario);
						
			intermediarioRequest.validate();
			
			PutIntermediarioDTO putIntermediarioDTO = IntermediariConverter.getPutIntermediarioDTO(intermediarioRequest, idIntermediario, user);
			
			IntermediariDAO intermediariDAO = new IntermediariDAO(false);
			
			PutIntermediarioDTOResponse putIntermediarioDTOResponse = intermediariDAO.createOrUpdateIntermediario(putIntermediarioDTO);
			
			Status responseStatus = putIntermediarioDTOResponse.isCreated() ?  Status.CREATED : Status.OK;
			
			this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
			return this.handleResponseOk(Response.status(responseStatus),transactionId).build();
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.log(ContextThreadLocal.get());
		}
    }

    public Response findStazioni(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , String idIntermediario, Integer pagina, Integer risultatiPerPagina, String ordinamento, String campi, Boolean abilitato, Boolean metadatiPaginazione, Boolean maxRisultati) {
    	String methodName = "findStazioni";  
		String transactionId = ContextThreadLocal.get().getTransactionId();
		this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 
		try{
			// autorizzazione sulla API
			this.isAuthorized(user, Arrays.asList(TIPO_UTENZA.OPERATORE, TIPO_UTENZA.APPLICAZIONE), Arrays.asList(Servizio.ANAGRAFICA_PAGOPA), Arrays.asList(Diritti.LETTURA));
			
			ValidatoreIdentificativi validatoreId = ValidatoreIdentificativi.newInstance();
			validatoreId.validaIdIntermediario("idIntermediario", idIntermediario);
			
			ValidatorFactory vf = ValidatorFactory.newInstance();
			ValidatoreUtils.validaRisultatiPerPagina(vf, Costanti.PARAMETRO_RISULTATI_PER_PAGINA, risultatiPerPagina);
			
			// Parametri - > DTO Input
			
			FindStazioniDTO listaStazioniDTO = new FindStazioniDTO(user);

			listaStazioniDTO.setLimit(risultatiPerPagina);
			listaStazioniDTO.setPagina(pagina);
			listaStazioniDTO.setOrderBy(ordinamento);
			listaStazioniDTO.setAbilitato(abilitato);
			listaStazioniDTO.setCodIntermediario(idIntermediario);
			
			listaStazioniDTO.setEseguiCount(metadatiPaginazione);
			listaStazioniDTO.setEseguiCountConLimit(maxRisultati);
			
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
			
			this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
			return this.handleResponseOk(Response.status(Status.OK).entity(response.toJSON(campi)),transactionId).build();
			
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.log(ContextThreadLocal.get());
		}
    }



    public Response getStazione(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , String idIntermediario, String idStazione) {
    	String methodName = "getStazione";  
		String transactionId = ContextThreadLocal.get().getTransactionId();
		this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 
		try{
			// autorizzazione sulla API
			this.isAuthorized(user, Arrays.asList(TIPO_UTENZA.OPERATORE, TIPO_UTENZA.APPLICAZIONE), Arrays.asList(Servizio.ANAGRAFICA_PAGOPA), Arrays.asList(Diritti.LETTURA));
			
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
			
			this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
			return this.handleResponseOk(Response.status(Status.OK).entity(response.toJSON(null)),transactionId).build();
			
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.log(ContextThreadLocal.get());
		}
    }


}


