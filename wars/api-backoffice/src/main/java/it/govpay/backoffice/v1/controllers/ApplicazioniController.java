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
import org.apache.commons.lang.StringUtils;
import org.openspcoop2.utils.json.ValidationException;
import org.openspcoop2.utils.service.context.ContextThreadLocal;
import org.slf4j.Logger;
import org.springframework.security.core.Authentication;

import it.govpay.backoffice.utils.validazione.semantica.ApplicazioneValidator;
import it.govpay.backoffice.v1.beans.Applicazione;
import it.govpay.backoffice.v1.beans.ApplicazionePost;
import it.govpay.backoffice.v1.beans.ListaApplicazioni;
import it.govpay.backoffice.v1.beans.PatchOp.OpEnum;
import it.govpay.backoffice.v1.beans.converter.ApplicazioniConverter;
import it.govpay.core.beans.Costanti;
import it.govpay.core.beans.JSONSerializable;
import it.govpay.core.dao.anagrafica.ApplicazioniDAO;
import it.govpay.core.dao.anagrafica.dto.FindApplicazioniDTO;
import it.govpay.core.dao.anagrafica.dto.FindApplicazioniDTOResponse;
import it.govpay.core.dao.anagrafica.dto.GetApplicazioneDTO;
import it.govpay.core.dao.anagrafica.dto.GetApplicazioneDTOResponse;
import it.govpay.core.dao.anagrafica.dto.PutApplicazioneDTO;
import it.govpay.core.dao.anagrafica.dto.PutApplicazioneDTOResponse;
import it.govpay.core.dao.anagrafica.exception.DominioNonTrovatoException;
import it.govpay.core.dao.anagrafica.exception.RuoloNonTrovatoException;
import it.govpay.core.dao.anagrafica.exception.TipoVersamentoNonTrovatoException;
import it.govpay.core.dao.anagrafica.exception.UnitaOperativaNonTrovataException;
import it.govpay.core.dao.pagamenti.dto.ApplicazionePatchDTO;
import it.govpay.core.exceptions.NotAuthorizedException;
import it.govpay.core.exceptions.UnprocessableEntityException;
import it.govpay.core.utils.validator.ValidatorFactory;
import it.govpay.core.utils.validator.ValidatoreIdentificativi;
import it.govpay.core.utils.validator.ValidatoreUtils;
import it.govpay.model.Acl.Diritti;
import it.govpay.model.Acl.Servizio;
import it.govpay.model.Utenza.TIPO_UTENZA;

public class ApplicazioniController extends BaseController {

	public static final String AUTODETERMINAZIONE_TIPI_PENDENZA_VALUE= "autodeterminazione";
	public static final String AUTODETERMINAZIONE_TIPI_PENDENZA_LABEL= "Autodeterminazione delle Pendenze";

	public static final String AUTORIZZA_TIPI_PENDENZA_STAR = "*";
	public static final String AUTORIZZA_TIPI_PENDENZA_STAR_LABEL= "Tutti";
	public static final String AUTORIZZA_DOMINI_STAR = "*";
	public static final String AUTORIZZA_DOMINI_STAR_LABEL= "Tutti";
	public static final String AUTORIZZA_UO_STAR = "*";



	public ApplicazioniController(String nomeServizio,Logger log) {
		super(nomeServizio,log);
	}



	public Response getApplicazione(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , String idA2A) {
		String methodName = "getApplicazione";  
		String transactionId = ContextThreadLocal.get().getTransactionId();
		this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 
		try{
			// autorizzazione sulla API
			this.isAuthorized(user, Arrays.asList(TIPO_UTENZA.OPERATORE, TIPO_UTENZA.APPLICAZIONE), Arrays.asList(Servizio.ANAGRAFICA_APPLICAZIONI), Arrays.asList(Diritti.LETTURA));

			// Validazione ID
			ValidatoreIdentificativi validatoreId = ValidatoreIdentificativi.newInstance();
			validatoreId.validaIdApplicazione("idA2A", idA2A);

			// Parametri - > DTO Input
			GetApplicazioneDTO getApplicazioneDTO = new GetApplicazioneDTO(user, idA2A);
			ApplicazioniDAO applicazioniDAO = new ApplicazioniDAO(false);
			// CHIAMATA AL DAO

			GetApplicazioneDTOResponse getApplicazioneDTOResponse = applicazioniDAO.getApplicazione(getApplicazioneDTO);

			// CONVERT TO JSON DELLA RISPOSTA

			Applicazione response = ApplicazioniConverter.toRsModel(getApplicazioneDTOResponse.getApplicazione());

			this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
			return this.handleResponseOk(Response.status(Status.OK).entity(response.toJSON(null)),transactionId).header(this.transactionIdHeaderName, transactionId).build();

		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.log(ContextThreadLocal.get());
		}
	}


	@SuppressWarnings("unchecked")
	public Response updateApplicazione(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , java.io.InputStream is, String idA2A) {
		String methodName = "updateApplicazione";  
		String transactionId = ContextThreadLocal.get().getTransactionId();
		this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 
		try(ByteArrayOutputStream baos= new ByteArrayOutputStream();){
			// salvo il json ricevuto
			IOUtils.copy(is, baos);

			// autorizzazione sulla API
			this.isAuthorized(user, Arrays.asList(TIPO_UTENZA.OPERATORE, TIPO_UTENZA.APPLICAZIONE), Arrays.asList(Servizio.ANAGRAFICA_APPLICAZIONI), Arrays.asList(Diritti.SCRITTURA));

			ValidatoreIdentificativi validatoreId = ValidatoreIdentificativi.newInstance();
			validatoreId.validaIdApplicazione("idA2A", idA2A);

			String jsonRequest = baos.toString();

			ApplicazionePatchDTO verificaPagamentoDTO = new ApplicazionePatchDTO(user);
			verificaPagamentoDTO.setCodApplicazione(idA2A);

			List<it.govpay.backoffice.v1.beans.PatchOp> lstOp = new ArrayList<>();

			try {
				List<java.util.LinkedHashMap<?,?>> lst = JSONSerializable.parse(jsonRequest, List.class);
				for(java.util.LinkedHashMap<?,?> map: lst) {
					it.govpay.backoffice.v1.beans.PatchOp op = new it.govpay.backoffice.v1.beans.PatchOp();
					String opText = (String) map.get("op");
					OpEnum opFromValue = OpEnum.fromValue(opText);

					if(StringUtils.isNotEmpty(opText) && opFromValue == null)
						throw new ValidationException("Il campo op non e' valido.");

					op.setOp(opFromValue);
					op.setPath((String) map.get("path"));
					op.setValue(map.get("value"));
					op.validate();
					lstOp.add(op);
				}
			} catch (Exception e) {
				lstOp = JSONSerializable.parse(jsonRequest, List.class);
			}

			verificaPagamentoDTO.setOp(it.govpay.backoffice.v1.beans.converter.PatchOpConverter.toModel(lstOp) );

			ApplicazioniDAO applicazioniDAO = new ApplicazioniDAO(false);

			GetApplicazioneDTOResponse pagamentoPortaleDTOResponse = applicazioniDAO.patch(verificaPagamentoDTO);

			Applicazione response = ApplicazioniConverter.toRsModel(pagamentoPortaleDTOResponse.getApplicazione());

			this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
			return this.handleResponseOk(Response.status(Status.OK).entity(response.toJSON(null)),transactionId).build();
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.log(ContextThreadLocal.get());
		}
	}


	public Response addApplicazione(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , String idA2A, java.io.InputStream is) {
		String methodName = "addApplicazione";  
		String transactionId = ContextThreadLocal.get().getTransactionId();
		this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 
		try(ByteArrayOutputStream baos= new ByteArrayOutputStream();){
			// salvo il json ricevuto
			IOUtils.copy(is, baos);

			// autorizzazione sulla API
			this.isAuthorized(user, Arrays.asList(TIPO_UTENZA.OPERATORE, TIPO_UTENZA.APPLICAZIONE), Arrays.asList(Servizio.ANAGRAFICA_APPLICAZIONI), Arrays.asList(Diritti.SCRITTURA));

			ValidatoreIdentificativi validatoreId = ValidatoreIdentificativi.newInstance();
			validatoreId.validaIdApplicazione("idA2A", idA2A);

			String jsonRequest = baos.toString();
			ApplicazionePost applicazioneRequest= JSONSerializable.parse(jsonRequest, ApplicazionePost.class);

			
			applicazioneRequest.validate();

			PutApplicazioneDTO putApplicazioneDTO = ApplicazioniConverter.getPutApplicazioneDTO(applicazioneRequest, idA2A, user); 
		
			try {
				new ApplicazioneValidator(putApplicazioneDTO.getApplicazione()).validate();
			} catch(ValidationException e) {
				throw new UnprocessableEntityException(e.getMessage());
			}

			ApplicazioniDAO applicazioniDAO = new ApplicazioniDAO(false);

			PutApplicazioneDTOResponse putApplicazioneDTOResponse =  null;
			try {
				putApplicazioneDTOResponse = applicazioniDAO.createOrUpdate(putApplicazioneDTO);
			} catch(DominioNonTrovatoException | TipoVersamentoNonTrovatoException | UnitaOperativaNonTrovataException | RuoloNonTrovatoException e) {
				throw new UnprocessableEntityException(e.getDetails());
			}

			Status responseStatus = putApplicazioneDTOResponse.isCreated() ?  Status.CREATED : Status.OK;

			this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
			return this.handleResponseOk(Response.status(responseStatus),transactionId).build();
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.log(ContextThreadLocal.get());
		}
	}



	public Response findApplicazioni(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , Integer pagina, Integer risultatiPerPagina, String ordinamento, String campi, Boolean abilitato, String idA2A, String principal, Boolean metadatiPaginazione, Boolean maxRisultati) {
		String methodName = "findApplicazioni";  
		String transactionId = ContextThreadLocal.get().getTransactionId();
		this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 
		this.setMaxRisultati(maxRisultati, metadatiPaginazione, true); 
		try{
			boolean associati = false;
			// autorizzazione sulla API
			try {
				this.isAuthorized(user, Arrays.asList(TIPO_UTENZA.OPERATORE, TIPO_UTENZA.APPLICAZIONE), Arrays.asList(Servizio.ANAGRAFICA_APPLICAZIONI), Arrays.asList(Diritti.LETTURA));
			}catch (NotAuthorizedException e) {
				associati = !associati;
			}
			
			ValidatorFactory vf = ValidatorFactory.newInstance();
			ValidatoreUtils.validaRisultatiPerPagina(vf, Costanti.PARAMETRO_RISULTATI_PER_PAGINA, risultatiPerPagina);

			// Parametri - > DTO Input

			FindApplicazioniDTO listaDominiDTO = new FindApplicazioniDTO(user);

			listaDominiDTO.setLimit(risultatiPerPagina);
			listaDominiDTO.setPagina(pagina);
			listaDominiDTO.setOrderBy(ordinamento);
			listaDominiDTO.setAbilitato(abilitato);
			listaDominiDTO.setPrincipal(principal);
			listaDominiDTO.setCodApplicazione(idA2A);
			
			listaDominiDTO.setEseguiCount(metadatiPaginazione);
			listaDominiDTO.setEseguiCountConLimit(maxRisultati);

			// INIT DAO

			ApplicazioniDAO applicazioniDAO = new ApplicazioniDAO(false);

			// CHIAMATA AL DAO

			FindApplicazioniDTOResponse listaApplicazioniDTOResponse = applicazioniDAO.findApplicazioni(listaDominiDTO);

			// CONVERT TO JSON DELLA RISPOSTA

			List<it.govpay.backoffice.v1.beans.ApplicazioneIndex> results = new ArrayList<>();
			for(it.govpay.bd.model.Applicazione applicazione: listaApplicazioniDTOResponse.getResults()) {
				results.add(ApplicazioniConverter.toRsModelIndex(applicazione));
			}

			ListaApplicazioni response = new ListaApplicazioni(results, this.getServicePath(uriInfo),
					listaApplicazioniDTOResponse.getTotalResults(), pagina, risultatiPerPagina, this.maxRisultatiBigDecimal);

			this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName));
			return this.handleResponseOk(Response.status(Status.OK).entity(response.toJSON(campi)),transactionId).build();

		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.log(ContextThreadLocal.get());
		}
	}


}


