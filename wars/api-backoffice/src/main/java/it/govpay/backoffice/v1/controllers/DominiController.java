package it.govpay.backoffice.v1.controllers;

import java.io.ByteArrayOutputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.springframework.security.core.Authentication;

import eu.medsea.mimeutil.MimeUtil;
import it.govpay.backoffice.utils.validazione.semantica.DominioValidator;
import it.govpay.backoffice.v1.beans.ContiAccredito;
import it.govpay.backoffice.v1.beans.ContiAccreditoPost;
import it.govpay.backoffice.v1.beans.DominioPost;
import it.govpay.backoffice.v1.beans.Entrata;
import it.govpay.backoffice.v1.beans.EntrataPost;
import it.govpay.backoffice.v1.beans.ListaContiAccredito;
import it.govpay.backoffice.v1.beans.ListaDomini;
import it.govpay.backoffice.v1.beans.ListaEntrate;
import it.govpay.backoffice.v1.beans.ListaTipiPendenzaDominio;
import it.govpay.backoffice.v1.beans.ListaUnitaOperative;
import it.govpay.backoffice.v1.beans.TipoPendenzaDominio;
import it.govpay.backoffice.v1.beans.TipoPendenzaDominioPost;
import it.govpay.backoffice.v1.beans.TipoPendenzaTipologia;
import it.govpay.backoffice.v1.beans.UnitaOperativa;
import it.govpay.backoffice.v1.beans.UnitaOperativaPost;
import it.govpay.backoffice.v1.beans.converter.DominiConverter;
import it.govpay.bd.model.IdUnitaOperativa;
import it.govpay.core.autorizzazione.AuthorizationManager;
import it.govpay.core.beans.JSONSerializable;
import it.govpay.core.dao.anagrafica.DominiDAO;
import it.govpay.core.dao.anagrafica.dto.FindDominiDTO;
import it.govpay.core.dao.anagrafica.dto.FindDominiDTOResponse;
import it.govpay.core.dao.anagrafica.dto.FindIbanDTO;
import it.govpay.core.dao.anagrafica.dto.FindIbanDTOResponse;
import it.govpay.core.dao.anagrafica.dto.FindTipiPendenzaDominioDTO;
import it.govpay.core.dao.anagrafica.dto.FindTipiPendenzaDominioDTOResponse;
import it.govpay.core.dao.anagrafica.dto.FindTributiDTO;
import it.govpay.core.dao.anagrafica.dto.FindTributiDTOResponse;
import it.govpay.core.dao.anagrafica.dto.FindUnitaOperativeDTO;
import it.govpay.core.dao.anagrafica.dto.FindUnitaOperativeDTOResponse;
import it.govpay.core.dao.anagrafica.dto.GetDominioDTO;
import it.govpay.core.dao.anagrafica.dto.GetDominioDTOResponse;
import it.govpay.core.dao.anagrafica.dto.GetIbanDTO;
import it.govpay.core.dao.anagrafica.dto.GetIbanDTOResponse;
import it.govpay.core.dao.anagrafica.dto.GetTipoPendenzaDominioDTO;
import it.govpay.core.dao.anagrafica.dto.GetTipoPendenzaDominioDTOResponse;
import it.govpay.core.dao.anagrafica.dto.GetTributoDTO;
import it.govpay.core.dao.anagrafica.dto.GetTributoDTOResponse;
import it.govpay.core.dao.anagrafica.dto.GetUnitaOperativaDTO;
import it.govpay.core.dao.anagrafica.dto.GetUnitaOperativaDTOResponse;
import it.govpay.core.dao.anagrafica.dto.PutDominioDTO;
import it.govpay.core.dao.anagrafica.dto.PutDominioDTOResponse;
import it.govpay.core.dao.anagrafica.dto.PutEntrataDominioDTO;
import it.govpay.core.dao.anagrafica.dto.PutEntrataDominioDTOResponse;
import it.govpay.core.dao.anagrafica.dto.PutIbanAccreditoDTO;
import it.govpay.core.dao.anagrafica.dto.PutIbanAccreditoDTOResponse;
import it.govpay.core.dao.anagrafica.dto.PutTipoPendenzaDominioDTO;
import it.govpay.core.dao.anagrafica.dto.PutTipoPendenzaDominioDTOResponse;
import it.govpay.core.dao.anagrafica.dto.PutUnitaOperativaDTO;
import it.govpay.core.dao.anagrafica.dto.PutUnitaOperativaDTOResponse;
import it.govpay.core.dao.anagrafica.exception.TipoTributoNonTrovatoException;
import it.govpay.core.dao.anagrafica.exception.TipoVersamentoNonTrovatoException;
import it.govpay.core.exceptions.NotAuthorizedException;
import it.govpay.core.exceptions.UnprocessableEntityException;
import it.govpay.core.utils.GovpayConfig;
import it.govpay.core.utils.validator.ValidatoreIdentificativi;
import it.govpay.model.TipoVersamento;
import it.govpay.model.Acl.Diritti;
import it.govpay.model.Acl.Servizio;
import it.govpay.model.Utenza.TIPO_UTENZA;

public class DominiController extends BaseController {

	public DominiController(String nomeServizio,Logger log) {
		super(nomeServizio,log);
	}


	public Response findDomini(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , Integer pagina, Integer risultatiPerPagina, String campi, Boolean abilitato, String ordinamento, String idStazione, Boolean associati) {
		String methodName = "findDomini";  
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

			FindDominiDTO listaDominiDTO = new FindDominiDTO(user);

			listaDominiDTO.setLimit(risultatiPerPagina);
			listaDominiDTO.setPagina(pagina);
			listaDominiDTO.setOrderBy(ordinamento);
			listaDominiDTO.setAbilitato(abilitato);
			listaDominiDTO.setCodStazione(idStazione);
			if(associati != null && associati) {
				List<Long> idDominiAutorizzati = AuthorizationManager.getIdDominiAutorizzati(user);
				if(idDominiAutorizzati == null)
					throw AuthorizationManager.toNotAuthorizedExceptionNessunDominioAutorizzato(user);

				listaDominiDTO.setIdDomini(idDominiAutorizzati);
			}

			// INIT DAO

			DominiDAO dominiDAO = new DominiDAO(false);

			// CHIAMATA AL DAO

			FindDominiDTOResponse listaDominiDTOResponse = dominiDAO.findDomini(listaDominiDTO);

			// CONVERT TO JSON DELLA RISPOSTA

			List<it.govpay.backoffice.v1.beans.DominioIndex> results = new ArrayList<>();
			for(it.govpay.bd.model.Dominio dominio: listaDominiDTOResponse.getResults()) {
				results.add(DominiConverter.toRsModelIndex(dominio));
			}

			ListaDomini response = new ListaDomini(results, this.getServicePath(uriInfo),
					listaDominiDTOResponse.getTotalResults(), pagina, risultatiPerPagina);

			this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
			return this.handleResponseOk(Response.status(Status.OK).entity(response.toJSON(campi)),transactionId).build();

		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.log(this.context);
		}

	}



	public Response findContiAccredito(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , String idDominio, Integer pagina, Integer risultatiPerPagina, String ordinamento, String campi, Boolean abilitato) {
		String methodName = "findContiAccredito";  
		String transactionId = this.context.getTransactionId();
		this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 
		try{
			// autorizzazione sulla API
			this.isAuthorized(user, Arrays.asList(TIPO_UTENZA.OPERATORE, TIPO_UTENZA.APPLICAZIONE), Arrays.asList(Servizio.ANAGRAFICA_CREDITORE), Arrays.asList(Diritti.LETTURA));

			ValidatoreIdentificativi validatoreId = ValidatoreIdentificativi.newInstance();
			validatoreId.validaIdDominio("idDominio", idDominio);

			// Parametri - > DTO Input

			FindIbanDTO listaDominiIbanDTO = new FindIbanDTO(user, idDominio);

			listaDominiIbanDTO.setLimit(risultatiPerPagina);
			listaDominiIbanDTO.setPagina(pagina);
			listaDominiIbanDTO.setOrderBy(ordinamento);
			listaDominiIbanDTO.setAbilitato(abilitato);

			// INIT DAO

			DominiDAO dominiDAO = new DominiDAO(false);

			// CHIAMATA AL DAO

			FindIbanDTOResponse listaDominiIbanDTOResponse = dominiDAO.findIban(listaDominiIbanDTO);

			// CONVERT TO JSON DELLA RISPOSTA

			List<it.govpay.backoffice.v1.beans.ContiAccredito> results = new ArrayList<>();
			for(it.govpay.bd.model.IbanAccredito ibanAccredito: listaDominiIbanDTOResponse.getResults()) {
				results.add(DominiConverter.toIbanRsModel(ibanAccredito));
			}

			ListaContiAccredito response = new ListaContiAccredito(results, this.getServicePath(uriInfo),
					listaDominiIbanDTOResponse.getTotalResults(), pagina, risultatiPerPagina);

			this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
			return this.handleResponseOk(Response.status(Status.OK).entity(response.toJSON(campi)),transactionId).build();

		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.log(this.context);
		}
	}

	public Response getContiAccredito(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , String idDominio, String ibanAccredito) {
		String methodName = "getContiAccredito";  
		String transactionId = this.context.getTransactionId();
		this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 
		try{
			// autorizzazione sulla API
			this.isAuthorized(user, Arrays.asList(TIPO_UTENZA.OPERATORE, TIPO_UTENZA.APPLICAZIONE), Arrays.asList(Servizio.ANAGRAFICA_CREDITORE), Arrays.asList(Diritti.LETTURA));

			ValidatoreIdentificativi validatoreId = ValidatoreIdentificativi.newInstance();
			validatoreId.validaIdDominio("idDominio", idDominio);
			validatoreId.validaIdIbanAccredito("ibanAccredito", ibanAccredito);

			// Parametri - > DTO Input

			GetIbanDTO getIbanDTO = new GetIbanDTO(user, idDominio, ibanAccredito);

			// INIT DAO

			DominiDAO dominiDAO = new DominiDAO(false);

			// CHIAMATA AL DAO

			GetIbanDTOResponse getDominiIbanDTOResponse = dominiDAO.getIban(getIbanDTO);

			// CONVERT TO JSON DELLA RISPOSTA

			ContiAccredito response = DominiConverter.toIbanRsModel(getDominiIbanDTOResponse.getIbanAccredito());

			this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
			return this.handleResponseOk(Response.status(Status.OK).entity(response.toJSON(null)),transactionId).build();

		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.log(this.context);
		}
	}

	public Response addContiAccredito(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , String idDominio, String ibanAccredito, java.io.InputStream is) {
		String methodName = "addContiAccredito";  
		String transactionId = this.context.getTransactionId();
		this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 
		try(ByteArrayOutputStream baos= new ByteArrayOutputStream();){
			// salvo il json ricevuto
			IOUtils.copy(is, baos);
			// autorizzazione sulla API
			this.isAuthorized(user, Arrays.asList(TIPO_UTENZA.OPERATORE, TIPO_UTENZA.APPLICAZIONE), Arrays.asList(Servizio.ANAGRAFICA_CREDITORE), Arrays.asList(Diritti.SCRITTURA));

			String jsonRequest = baos.toString();
			ContiAccreditoPost ibanAccreditoRequest= JSONSerializable.parse(jsonRequest, ContiAccreditoPost.class);

			ValidatoreIdentificativi validatoreId = ValidatoreIdentificativi.newInstance();
			validatoreId.validaIdDominio("idDominio", idDominio);
			validatoreId.validaIdIbanAccredito("ibanAccredito", ibanAccredito);

			ibanAccreditoRequest.validate();

			PutIbanAccreditoDTO putibanAccreditoDTO = DominiConverter.getPutIbanAccreditoDTO(ibanAccreditoRequest, idDominio, ibanAccredito, user);

			DominiDAO dominiDAO = new DominiDAO(false);

			PutIbanAccreditoDTOResponse putIbanAccreditoDTOResponse = dominiDAO.createOrUpdateIbanAccredito(putibanAccreditoDTO);

			Status responseStatus = putIbanAccreditoDTOResponse.isCreated() ?  Status.CREATED : Status.OK;

			this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
			return this.handleResponseOk(Response.status(responseStatus),transactionId).build();
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.log(this.context);
		}
	}

	public Response findEntrate(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , String idDominio, Integer pagina, Integer risultatiPerPagina, String ordinamento, String campi, Boolean abilitato) {
		String methodName = "findEntrate";  
		String transactionId = this.context.getTransactionId();
		this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 
		try{
			// autorizzazione sulla API
			this.isAuthorized(user, Arrays.asList(TIPO_UTENZA.OPERATORE, TIPO_UTENZA.APPLICAZIONE), Arrays.asList(Servizio.ANAGRAFICA_CREDITORE), Arrays.asList(Diritti.LETTURA));

			ValidatoreIdentificativi validatoreId = ValidatoreIdentificativi.newInstance();
			validatoreId.validaIdDominio("idDominio", idDominio);

			// Parametri - > DTO Input

			FindTributiDTO listaDominiEntrateDTO = new FindTributiDTO(user, idDominio);

			listaDominiEntrateDTO.setLimit(risultatiPerPagina);
			listaDominiEntrateDTO.setPagina(pagina);
			listaDominiEntrateDTO.setOrderBy(ordinamento);
			listaDominiEntrateDTO.setAbilitato(abilitato);

			// INIT DAO

			DominiDAO dominiDAO = new DominiDAO(false);

			// CHIAMATA AL DAO

			FindTributiDTOResponse listaDominiEntrateDTOResponse = dominiDAO.findTributi(listaDominiEntrateDTO);

			// CONVERT TO JSON DELLA RISPOSTA

			List<it.govpay.backoffice.v1.beans.Entrata> results = new ArrayList<>();
			for(GetTributoDTOResponse tributo: listaDominiEntrateDTOResponse.getResults()) {
				results.add(DominiConverter.toEntrataRsModel(tributo));
			}

			ListaEntrate response = new ListaEntrate(results, this.getServicePath(uriInfo),
					listaDominiEntrateDTOResponse.getTotalResults(), pagina, risultatiPerPagina);

			this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
			return this.handleResponseOk(Response.status(Status.OK).entity(response.toJSON(campi)),transactionId).build();

		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.log(this.context);
		}
	}

	public Response getEntrata(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , String idDominio, String idEntrata) {
		String methodName = "getEntrata";  
		String transactionId = this.context.getTransactionId();
		this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 
		try{
			// autorizzazione sulla API
			this.isAuthorized(user, Arrays.asList(TIPO_UTENZA.OPERATORE, TIPO_UTENZA.APPLICAZIONE), Arrays.asList(Servizio.ANAGRAFICA_CREDITORE), Arrays.asList(Diritti.LETTURA));

			ValidatoreIdentificativi validatoreId = ValidatoreIdentificativi.newInstance();
			validatoreId.validaIdDominio("idDominio", idDominio);
			validatoreId.validaIdEntrata("idEntrata", idEntrata);

			// Parametri - > DTO Input

			GetTributoDTO getDominioEntrataDTO = new GetTributoDTO(user, idDominio, idEntrata);

			// INIT DAO

			DominiDAO dominiDAO = new DominiDAO(false);

			// CHIAMATA AL DAO

			GetTributoDTOResponse listaDominiEntrateDTOResponse = dominiDAO.getTributo(getDominioEntrataDTO);

			// CONVERT TO JSON DELLA RISPOSTA

			Entrata response = DominiConverter.toEntrataRsModel(listaDominiEntrateDTOResponse);

			this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
			return this.handleResponseOk(Response.status(Status.OK).entity(response.toJSON(null)),transactionId).build();

		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.log(this.context);
		}
	}

	public Response addEntrata(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , String idDominio, String idEntrata, java.io.InputStream is) {
		String methodName = "addEntrata";  
		String transactionId = this.context.getTransactionId();
		this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 
		try(ByteArrayOutputStream baos= new ByteArrayOutputStream();){
			// salvo il json ricevuto
			IOUtils.copy(is, baos);

			// autorizzazione sulla API
			this.isAuthorized(user, Arrays.asList(TIPO_UTENZA.OPERATORE, TIPO_UTENZA.APPLICAZIONE), Arrays.asList(Servizio.ANAGRAFICA_CREDITORE), Arrays.asList(Diritti.SCRITTURA));

			String jsonRequest = baos.toString();
			EntrataPost entrataRequest= JSONSerializable.parse(jsonRequest, EntrataPost.class);

			ValidatoreIdentificativi validatoreId = ValidatoreIdentificativi.newInstance();
			validatoreId.validaIdDominio("idDominio", idDominio);
			validatoreId.validaIdEntrata("idEntrata", idEntrata);

			entrataRequest.validate();

			PutEntrataDominioDTO putEntrataDTO = DominiConverter.getPutEntrataDominioDTO(entrataRequest, idDominio, idEntrata, user); 

			DominiDAO dominiDAO = new DominiDAO(false);
			PutEntrataDominioDTOResponse putEntrataDTOResponse = null;
			try {
				putEntrataDTOResponse = dominiDAO.createOrUpdateEntrataDominio(putEntrataDTO);
			} catch(TipoTributoNonTrovatoException e) {
				throw new UnprocessableEntityException("Il tipo entrata indicato non e' disponibile per il dominio.");
			}

			Status responseStatus = putEntrataDTOResponse.isCreated() ?  Status.CREATED : Status.OK;

			this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
			return this.handleResponseOk(Response.status(responseStatus),transactionId).build();
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.log(this.context);
		}
	}

	public Response getDominio(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , String idDominio) {
		String methodName = "getDominio";  
		String transactionId = this.context.getTransactionId();
		this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 
		try{
			// autorizzazione sulla API
			this.isAuthorized(user, Arrays.asList(TIPO_UTENZA.OPERATORE, TIPO_UTENZA.APPLICAZIONE), Arrays.asList(Servizio.ANAGRAFICA_CREDITORE), Arrays.asList(Diritti.LETTURA));

			ValidatoreIdentificativi validatoreId = ValidatoreIdentificativi.newInstance();
			validatoreId.validaIdDominio("idDominio", idDominio);

			// Parametri - > DTO Input

			GetDominioDTO getDominioDTO = new GetDominioDTO(user, idDominio);

			// INIT DAO

			DominiDAO dominiDAO = new DominiDAO(false);

			// CHIAMATA AL DAO

			GetDominioDTOResponse listaDominiDTOResponse = dominiDAO.getDominio(getDominioDTO);

			// CONVERT TO JSON DELLA RISPOSTA

			it.govpay.backoffice.v1.beans.Dominio response = DominiConverter.toRsModel(listaDominiDTOResponse.getDominio(), listaDominiDTOResponse.getUo(), listaDominiDTOResponse.getTributi(), listaDominiDTOResponse.getIban(), listaDominiDTOResponse.getTipiVersamentoDominio());

			this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
			return this.handleResponseOk(Response.status(Status.OK).entity(response.toJSON(null)),transactionId).build();

		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.log(this.context);
		}
	}

	public Response addDominio(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , String idDominio, java.io.InputStream is) {
		String methodName = "addDominio";  
		String transactionId = this.context.getTransactionId();
		this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 
		try(ByteArrayOutputStream baos= new ByteArrayOutputStream();){
			// salvo il json ricevuto
			IOUtils.copy(is, baos);

			// autorizzazione sulla API
			this.isAuthorized(user, Arrays.asList(TIPO_UTENZA.OPERATORE, TIPO_UTENZA.APPLICAZIONE), Arrays.asList(Servizio.ANAGRAFICA_CREDITORE), Arrays.asList(Diritti.SCRITTURA));

			String jsonRequest = baos.toString();
			DominioPost dominioRequest= JSONSerializable.parse(jsonRequest, DominioPost.class);

			ValidatoreIdentificativi validatoreId = ValidatoreIdentificativi.newInstance();
			validatoreId.validaIdDominio("idDominio", idDominio);

			dominioRequest.validate();

			PutDominioDTO putDominioDTO = DominiConverter.getPutDominioDTO(dominioRequest, idDominio, user); 

			new DominioValidator(putDominioDTO.getDominio()).validazioneSemantica();

			DominiDAO dominiDAO = new DominiDAO(false);

			PutDominioDTOResponse putDominioDTOResponse = dominiDAO.createOrUpdate(putDominioDTO);

			Status responseStatus = putDominioDTOResponse.isCreated() ?  Status.CREATED : Status.OK;

			this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
			return this.handleResponseOk(Response.status(responseStatus),transactionId).build();
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.log(this.context);
		}
	}

	public Response findTipiPendenza(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , String idDominio, Integer pagina, Integer risultatiPerPagina, String ordinamento, String campi, Boolean abilitato, String tipo, Boolean associati, Boolean form) {
		String methodName = "findTipiPendenza";  
		String transactionId = this.context.getTransactionId();
		this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 
		try{
			try {
				// autorizzazione sulla API
				this.isAuthorized(user, Arrays.asList(TIPO_UTENZA.OPERATORE, TIPO_UTENZA.APPLICAZIONE), Arrays.asList(Servizio.ANAGRAFICA_CREDITORE), Arrays.asList(Diritti.LETTURA));
			}catch (NotAuthorizedException e) {
				associati = true;
			}
			
			ValidatoreIdentificativi validatoreId = ValidatoreIdentificativi.newInstance();
			validatoreId.validaIdDominio("idDominio", idDominio);
			
			if(associati != null && associati) {
				List<String> codDominiAutorizzati = AuthorizationManager.getDominiAutorizzati(user);
				if(codDominiAutorizzati == null)
					throw AuthorizationManager.toNotAuthorizedExceptionNessunDominioAutorizzato(user);
				if(!codDominiAutorizzati.isEmpty() && !codDominiAutorizzati.contains(idDominio)) {
					throw AuthorizationManager.toNotAuthorizedException(user, idDominio, null);
				}
			}
			
			// Parametri - > DTO Input

			FindTipiPendenzaDominioDTO findTipiPendenzaDominioDTO = new FindTipiPendenzaDominioDTO(user);

			findTipiPendenzaDominioDTO.setLimit(risultatiPerPagina);
			findTipiPendenzaDominioDTO.setPagina(pagina);
			findTipiPendenzaDominioDTO.setOrderBy(ordinamento);
			findTipiPendenzaDominioDTO.setCodDominio(idDominio);
			findTipiPendenzaDominioDTO.setAbilitato(abilitato);
			
			if(tipo != null) {
				TipoPendenzaTipologia tipologia = TipoPendenzaTipologia.fromValue(tipo);
				if(tipologia != null) {
					switch (tipologia) {
					case DOVUTO:
						findTipiPendenzaDominioDTO.setTipo(TipoVersamento.Tipo.DOVUTO);
						break;
					case SPONTANEO:
						findTipiPendenzaDominioDTO.setTipo(TipoVersamento.Tipo.SPONTANEO); 
						break;
					}
				}
			}
			
			findTipiPendenzaDominioDTO.setForm(form); 
			
			if(associati != null && associati) {
				List<Long> idTipiVersamentoAutorizzati = AuthorizationManager.getIdTipiVersamentoAutorizzati(user);
				if(idTipiVersamentoAutorizzati == null)
					throw AuthorizationManager.toNotAuthorizedExceptionNessunTipoVersamentoAutorizzato(user);

				findTipiPendenzaDominioDTO.setIdTipiVersamento(idTipiVersamentoAutorizzati);
			}
			// INIT DAO

			DominiDAO dominiDAO = new DominiDAO(false);

			// CHIAMATA AL DAO

			FindTipiPendenzaDominioDTOResponse findTipiPendenzaDominioDTOResponse = dominiDAO.findTipiPendenza(findTipiPendenzaDominioDTO);

			// CONVERT TO JSON DELLA RISPOSTA

			List<it.govpay.backoffice.v1.beans.TipoPendenzaDominio> results = new ArrayList<>();
			for(GetTipoPendenzaDominioDTOResponse tipoVersamentoDominio: findTipiPendenzaDominioDTOResponse.getResults()) {
				results.add(DominiConverter.toTipoPendenzaRsModel(tipoVersamentoDominio));
			}

			ListaTipiPendenzaDominio response = new ListaTipiPendenzaDominio(results, this.getServicePath(uriInfo),
					findTipiPendenzaDominioDTOResponse.getTotalResults(), pagina, risultatiPerPagina);

			this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
			return this.handleResponseOk(Response.status(Status.OK).entity(response.toJSON(campi)),transactionId).build();

		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.log(this.context);
		}
	}

	public Response getTipoPendenza(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , String idDominio, String idTipoPendenza) {
		String methodName = "getTipoPendenza";  
		String transactionId = this.context.getTransactionId();
		this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 
		try{
			// autorizzazione sulla API
			this.isAuthorized(user, Arrays.asList(TIPO_UTENZA.OPERATORE, TIPO_UTENZA.APPLICAZIONE), Arrays.asList(Servizio.ANAGRAFICA_CREDITORE), Arrays.asList(Diritti.LETTURA));

			ValidatoreIdentificativi validatoreId = ValidatoreIdentificativi.newInstance();
			validatoreId.validaIdDominio("idDominio", idDominio);
			validatoreId.validaIdTipoVersamento("idTipoPendenza", idTipoPendenza);

			// Parametri - > DTO Input

			GetTipoPendenzaDominioDTO getTipoPendenzaDominioDTO = new GetTipoPendenzaDominioDTO(user, idDominio, idTipoPendenza);

			// INIT DAO

			DominiDAO dominiDAO = new DominiDAO(false);

			// CHIAMATA AL DAO

			GetTipoPendenzaDominioDTOResponse getTipoPendenzaDominioDTOResponse = dominiDAO.getTipoPendenza(getTipoPendenzaDominioDTO); 

			// CONVERT TO JSON DELLA RISPOSTA

			TipoPendenzaDominio response = DominiConverter.toTipoPendenzaRsModel(getTipoPendenzaDominioDTOResponse);

			this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
			return this.handleResponseOk(Response.status(Status.OK).entity(response.toJSON(null)),transactionId).build();

		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.log(this.context);
		}
	}

	public Response addTipoPendenza(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , String idDominio, String idTipoPendenza, java.io.InputStream is) {
		String methodName = "addTipoPendenza";  
		String transactionId = this.context.getTransactionId();
		this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 
		try(ByteArrayOutputStream baos= new ByteArrayOutputStream();){
			// salvo il json ricevuto
			IOUtils.copy(is, baos);

			// autorizzazione sulla API
			this.isAuthorized(user, Arrays.asList(TIPO_UTENZA.OPERATORE, TIPO_UTENZA.APPLICAZIONE), Arrays.asList(Servizio.ANAGRAFICA_CREDITORE), Arrays.asList(Diritti.SCRITTURA));

			String jsonRequest = baos.toString();
			TipoPendenzaDominioPost tipoPendenzaRequest= JSONSerializable.parse(jsonRequest, TipoPendenzaDominioPost.class);

			ValidatoreIdentificativi validatoreId = ValidatoreIdentificativi.newInstance();
			validatoreId.validaIdDominio("idDominio", idDominio);
			validatoreId.validaIdTipoVersamento("idTipoPendenza", idTipoPendenza);

			tipoPendenzaRequest.validate();

			PutTipoPendenzaDominioDTO putTipoPendenzaDominioDTO = DominiConverter.getPutTipoPendenzaDominioDTO(tipoPendenzaRequest, idDominio, idTipoPendenza, user); 

			DominiDAO dominiDAO = new DominiDAO(false);
			PutTipoPendenzaDominioDTOResponse putTipoPendenzaDominioDTOResponse = null;
			try {
				putTipoPendenzaDominioDTOResponse = dominiDAO.createOrUpdateTipoPendenzaDominio(putTipoPendenzaDominioDTO);
			} catch(TipoVersamentoNonTrovatoException e) {
				throw new UnprocessableEntityException("Il tipo pendenza indicato non e' disponibile per il dominio.");
			}

			Status responseStatus = putTipoPendenzaDominioDTOResponse.isCreated() ?  Status.CREATED : Status.OK; 

			this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
			return this.handleResponseOk(Response.status(responseStatus),transactionId).build();
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.log(this.context);
		}
	}

	public Response findUnitaOperative(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , String idDominio, Integer pagina, Integer risultatiPerPagina, String ordinamento, String campi, Boolean abilitato, Boolean associati) {    	
		String methodName = "findUnitaOperative";  
		String transactionId = this.context.getTransactionId();
		this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 
		try{
			try {
				// autorizzazione sulla API
				this.isAuthorized(user, Arrays.asList(TIPO_UTENZA.OPERATORE, TIPO_UTENZA.APPLICAZIONE), Arrays.asList(Servizio.ANAGRAFICA_CREDITORE), Arrays.asList(Diritti.LETTURA));
			}catch (NotAuthorizedException e) {
				associati = true;
			}

			ValidatoreIdentificativi validatoreId = ValidatoreIdentificativi.newInstance();
			validatoreId.validaIdDominio("idDominio", idDominio);

			// Parametri - > DTO Input

			FindUnitaOperativeDTO listaDominiUoDTO = new FindUnitaOperativeDTO(user, idDominio);

			listaDominiUoDTO.setLimit(risultatiPerPagina);
			listaDominiUoDTO.setPagina(pagina);
			listaDominiUoDTO.setOrderBy(ordinamento);
			listaDominiUoDTO.setAbilitato(abilitato);
			if(associati != null && associati) {
				List<IdUnitaOperativa> idUnitaOperative = AuthorizationManager.getUoAutorizzate(user, idDominio);
				if(idUnitaOperative == null)
					throw AuthorizationManager.toNotAuthorizedExceptionNessunaUOAutorizzata(user);

				listaDominiUoDTO.setUnitaOperative(idUnitaOperative);
				listaDominiUoDTO.setAssociati(associati);
			}

			// INIT DAO

			DominiDAO dominiDAO = new DominiDAO(false);

			// CHIAMATA AL DAO

			FindUnitaOperativeDTOResponse listaDominiUoDTOResponse = dominiDAO.findUnitaOperative(listaDominiUoDTO);

			// CONVERT TO JSON DELLA RISPOSTA

			List<it.govpay.backoffice.v1.beans.UnitaOperativa> results = new ArrayList<>();
			for(it.govpay.bd.model.UnitaOperativa uo: listaDominiUoDTOResponse.getResults()) {
				results.add(DominiConverter.toUnitaOperativaRsModel(uo));
			}

			ListaUnitaOperative response = new ListaUnitaOperative(results, this.getServicePath(uriInfo),
					listaDominiUoDTOResponse.getTotalResults(), pagina, risultatiPerPagina);

			this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
			return this.handleResponseOk(Response.status(Status.OK).entity(response.toJSON(campi)),transactionId).build();

		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.log(this.context);
		}
	}

	public Response getUnitaOperativa(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , String idDominio, String idUnitaOperativa) {
		String methodName = "getUnitaOperativa";  
		String transactionId = this.context.getTransactionId();
		this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 
		try{
			// autorizzazione sulla API
			this.isAuthorized(user, Arrays.asList(TIPO_UTENZA.OPERATORE, TIPO_UTENZA.APPLICAZIONE), Arrays.asList(Servizio.ANAGRAFICA_CREDITORE), Arrays.asList(Diritti.LETTURA));

			ValidatoreIdentificativi validatoreId = ValidatoreIdentificativi.newInstance();
			validatoreId.validaIdDominio("idDominio", idDominio);
			validatoreId.validaIdUO("idUnitaOperativa", idUnitaOperativa);

			// Parametri - > DTO Input

			GetUnitaOperativaDTO getDominioUoDTO = new GetUnitaOperativaDTO(user, idDominio, idUnitaOperativa);

			// INIT DAO

			DominiDAO dominiDAO = new DominiDAO(false);

			// CHIAMATA AL DAO

			GetUnitaOperativaDTOResponse listaDominiUoDTOResponse = dominiDAO.getUnitaOperativa(getDominioUoDTO);

			// CONVERT TO JSON DELLA RISPOSTA

			UnitaOperativa response = DominiConverter.toUnitaOperativaRsModel(listaDominiUoDTOResponse.getUnitaOperativa());

			this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
			return this.handleResponseOk(Response.status(Status.OK).entity(response.toJSON(null)),transactionId).build();

		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.log(this.context);
		}
	}

	public Response addUnitaOperativa(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , String idDominio, String idUnitaOperativa, java.io.InputStream is) {
		String methodName = "addUnitaOperativa";  
		String transactionId = this.context.getTransactionId();
		this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 
		try(ByteArrayOutputStream baos= new ByteArrayOutputStream();){
			// salvo il json ricevuto
			IOUtils.copy(is, baos);

			// autorizzazione sulla API
			this.isAuthorized(user, Arrays.asList(TIPO_UTENZA.OPERATORE, TIPO_UTENZA.APPLICAZIONE), Arrays.asList(Servizio.ANAGRAFICA_CREDITORE), Arrays.asList(Diritti.SCRITTURA));

			String jsonRequest = baos.toString();
			UnitaOperativaPost unitaOperativaRequest= JSONSerializable.parse(jsonRequest, UnitaOperativaPost.class);

			ValidatoreIdentificativi validatoreId = ValidatoreIdentificativi.newInstance();
			validatoreId.validaIdDominio("idDominio", idDominio);
			validatoreId.validaIdUO("idUnitaOperativa", idUnitaOperativa);

			unitaOperativaRequest.validate();

			PutUnitaOperativaDTO putUnitaOperativaDTO = DominiConverter.getPutUnitaOperativaDTO(unitaOperativaRequest, idDominio, idUnitaOperativa, user);

			DominiDAO dominiDAO = new DominiDAO(false);

			PutUnitaOperativaDTOResponse putUnitaOperativaDTOResponse = dominiDAO.createOrUpdateUnitaOperativa(putUnitaOperativaDTO);

			Status responseStatus = putUnitaOperativaDTOResponse.isCreated() ?  Status.CREATED : Status.OK;

			this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
			return this.handleResponseOk(Response.status(responseStatus),transactionId).build();
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.log(this.context);
		}
	}

	public Response getLogo(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders, String idDominio) {
		String methodName = "getLogo";  
		String transactionId = this.context.getTransactionId();
		this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 
		try{
			// autorizzazione sulla API
			this.isAuthorized(user, Arrays.asList(TIPO_UTENZA.OPERATORE, TIPO_UTENZA.APPLICAZIONE), Arrays.asList(Servizio.ANAGRAFICA_CREDITORE), Arrays.asList(Diritti.LETTURA));

			ValidatoreIdentificativi validatoreId = ValidatoreIdentificativi.newInstance();
			validatoreId.validaIdDominio("idDominio", idDominio);

			// Parametri - > DTO Input

			GetDominioDTO getDominioDTO = new GetDominioDTO(user, idDominio);

			// INIT DAO

			DominiDAO dominiDAO = new DominiDAO(false);

			// CHIAMATA AL DAO

			byte[] logo = dominiDAO.getLogo(getDominioDTO);

			MimeUtil.registerMimeDetector(eu.medsea.mimeutil.detector.MagicMimeMimeDetector.class.getName());

			Collection<?> mimeTypes = MimeUtil.getMimeTypes(logo);

			String mimeType = MimeUtil.getFirstMimeType(mimeTypes.toString()).toString();

			this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
			ResponseBuilder entity = Response.status(Status.OK).entity(logo);
			entity.header("CacheControl", "max-age: "+ GovpayConfig.getInstance().getCacheLogo().intValue());
			entity.header("Content-Type", mimeType);
			return this.handleResponseOk(entity,transactionId).build();

		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.log(this.context);
		}
	}
}


