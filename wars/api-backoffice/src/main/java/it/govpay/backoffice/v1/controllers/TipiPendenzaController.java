package it.govpay.backoffice.v1.controllers;


import java.io.ByteArrayOutputStream;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.springframework.security.core.Authentication;

import it.govpay.backoffice.v1.beans.ListaTipiPendenza;
import it.govpay.backoffice.v1.beans.TipoPendenza;
import it.govpay.backoffice.v1.beans.TipoPendenzaPost;
import it.govpay.backoffice.v1.beans.TipoPendenzaTipologia;
import it.govpay.backoffice.v1.beans.converter.TipiPendenzaConverter;
import it.govpay.core.autorizzazione.AuthorizationManager;
import it.govpay.core.beans.JSONSerializable;
import it.govpay.core.dao.anagrafica.TipoPendenzaDAO;
import it.govpay.core.dao.anagrafica.dto.FindTipiPendenzaDTO;
import it.govpay.core.dao.anagrafica.dto.FindTipiPendenzaDTOResponse;
import it.govpay.core.dao.anagrafica.dto.GetTipoPendenzaDTO;
import it.govpay.core.dao.anagrafica.dto.GetTipoPendenzaDTOResponse;
import it.govpay.core.dao.anagrafica.dto.PutTipoPendenzaDTO;
import it.govpay.core.dao.anagrafica.dto.PutTipoPendenzaDTOResponse;
import it.govpay.core.exceptions.NotAuthorizedException;
import it.govpay.core.utils.validator.ValidatoreIdentificativi;
import it.govpay.model.Acl.Diritti;
import it.govpay.model.Acl.Servizio;
import it.govpay.model.TipoVersamento;
import it.govpay.model.Utenza.TIPO_UTENZA;



public class TipiPendenzaController extends BaseController {

     public TipiPendenzaController(String nomeServizio,Logger log) {
    	 super(nomeServizio,log);
     }



    public Response findTipiPendenza(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , Integer pagina, Integer risultatiPerPagina, String ordinamento, String campi, Boolean abilitato, String tipo, Boolean associati, Boolean form, String idTipoPendenza, String descrizione) {
    	String methodName = "findTipiPendenza";  
		String transactionId = this.context.getTransactionId();
		this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 
		try{
			// autorizzazione sulla API
			try {
				this.isAuthorized(user, Arrays.asList(TIPO_UTENZA.OPERATORE, TIPO_UTENZA.APPLICAZIONE), Arrays.asList(Servizio.ANAGRAFICA_CREDITORE), Arrays.asList(Diritti.LETTURA));
			}catch (NotAuthorizedException e) {
				associati = true;
			}
			// Parametri - > DTO Input
			
			FindTipiPendenzaDTO findTipiPendenzaDTO = new FindTipiPendenzaDTO(user);
			findTipiPendenzaDTO.setLimit(risultatiPerPagina);
			findTipiPendenzaDTO.setPagina(pagina);
			findTipiPendenzaDTO.setOrderBy(ordinamento);
			findTipiPendenzaDTO.setAbilitato(abilitato);
			
			if(tipo != null) {
				TipoPendenzaTipologia tipologia = TipoPendenzaTipologia.fromValue(tipo);
				if(tipologia != null) {
					switch (tipologia) {
					case DOVUTO:
						findTipiPendenzaDTO.setTipo(TipoVersamento.Tipo.DOVUTO);
						break;
					case SPONTANEO:
						findTipiPendenzaDTO.setTipo(TipoVersamento.Tipo.SPONTANEO);
						break;
					}
				}
			}
			
			findTipiPendenzaDTO.setForm(form);
			
			if(associati != null && associati) {
				List<Long> idTipiVersamentoAutorizzati = AuthorizationManager.getIdTipiVersamentoAutorizzati(user);
				if(idTipiVersamentoAutorizzati == null)
					throw AuthorizationManager.toNotAuthorizedExceptionNessunTipoVersamentoAutorizzato(user);
				
				findTipiPendenzaDTO.setIdTipiVersamento(idTipiVersamentoAutorizzati);
			}
			findTipiPendenzaDTO.setDescrizione(descrizione);
			findTipiPendenzaDTO.setCodTipoVersamento(idTipoPendenza);
			
			// INIT DAO
			
			TipoPendenzaDAO tipiPendenzaDAO = new TipoPendenzaDAO(false);
			
			// CHIAMATA AL DAO
			
			FindTipiPendenzaDTOResponse findTipiPendenzaDTOResponse = tipiPendenzaDAO.findTipiPendenza(findTipiPendenzaDTO);
			
			// CONVERT TO JSON DELLA RISPOSTA
			
			ListaTipiPendenza response = new ListaTipiPendenza(findTipiPendenzaDTOResponse.getResults().stream().map(t -> TipiPendenzaConverter.toTipoPendenzaRsModel(t)).collect(Collectors.toList()), 
					this.getServicePath(uriInfo), findTipiPendenzaDTOResponse.getTotalResults(), pagina, risultatiPerPagina);
			
			this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
			return this.handleResponseOk(Response.status(Status.OK).entity(response.toJSON(campi)),transactionId).build();
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.log(this.context);
		}
    }



    public Response getTipoPendenza(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , String idTipoPendenza) {
    	String methodName = "getTipoPendenza";  
		String transactionId = this.context.getTransactionId();
		this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 
		try{
			// autorizzazione sulla API
			this.isAuthorized(user, Arrays.asList(TIPO_UTENZA.OPERATORE, TIPO_UTENZA.APPLICAZIONE), Arrays.asList(Servizio.ANAGRAFICA_CREDITORE), Arrays.asList(Diritti.LETTURA));
			
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
			
			this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
			return this.handleResponseOk(Response.status(Status.OK).entity(response.toJSON(null)),transactionId).build();
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.log(this.context);
		}
    }



    public Response addTipoPendenza(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , String idTipoPendenza, java.io.InputStream is) {
    	String methodName = "addTipoPendenza";  
		String transactionId = this.context.getTransactionId();
		this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 
		try(ByteArrayOutputStream baos= new ByteArrayOutputStream();){
			// salvo il json ricevuto
			IOUtils.copy(is, baos);
			
			// autorizzazione sulla API
			this.isAuthorized(user, Arrays.asList(TIPO_UTENZA.OPERATORE, TIPO_UTENZA.APPLICAZIONE), Arrays.asList(Servizio.ANAGRAFICA_CREDITORE), Arrays.asList(Diritti.SCRITTURA));
			
			String jsonRequest = baos.toString();
			TipoPendenzaPost tipoPendenzaRequest= JSONSerializable.parse(jsonRequest, TipoPendenzaPost.class);
			
			ValidatoreIdentificativi validatoreId = ValidatoreIdentificativi.newInstance();
			validatoreId.validaIdTipoVersamento("idTipoPendenza", idTipoPendenza);
			
			tipoPendenzaRequest.validate();
			
			PutTipoPendenzaDTO putIntermediarioDTO = TipiPendenzaConverter.getPutTipoPendenzaDTO(tipoPendenzaRequest, idTipoPendenza, user);
			
			TipoPendenzaDAO intermediariDAO = new TipoPendenzaDAO(false);
			
			PutTipoPendenzaDTOResponse putIntermediarioDTOResponse = intermediariDAO.createOrUpdateTipoPendenza(putIntermediarioDTO);
			
			Status responseStatus = putIntermediarioDTOResponse.isCreated() ?  Status.CREATED : Status.OK;
			
			this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
			return this.handleResponseOk(Response.status(responseStatus),transactionId).build();
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.log(this.context);
		}
    }
}


