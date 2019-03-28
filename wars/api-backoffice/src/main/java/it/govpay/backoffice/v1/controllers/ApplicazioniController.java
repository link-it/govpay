package it.govpay.backoffice.v1.controllers;

import java.io.ByteArrayOutputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.io.IOUtils;
import org.openspcoop2.utils.service.context.IContext;
import org.slf4j.Logger;
import org.springframework.security.core.Authentication;

import it.govpay.backoffice.utils.validazione.semantica.ApplicazioneValidator;
import it.govpay.backoffice.v1.beans.Applicazione;
import it.govpay.backoffice.v1.beans.ApplicazionePost;
import it.govpay.backoffice.v1.beans.ListaApplicazioni;
import it.govpay.backoffice.v1.beans.converter.ApplicazioniConverter;
import it.govpay.core.beans.JSONSerializable;
import it.govpay.core.dao.anagrafica.ApplicazioniDAO;
import it.govpay.core.dao.anagrafica.dto.FindApplicazioniDTO;
import it.govpay.core.dao.anagrafica.dto.FindApplicazioniDTOResponse;
import it.govpay.core.dao.anagrafica.dto.GetApplicazioneDTO;
import it.govpay.core.dao.anagrafica.dto.GetApplicazioneDTOResponse;
import it.govpay.core.dao.anagrafica.dto.PutApplicazioneDTO;
import it.govpay.core.dao.anagrafica.dto.PutApplicazioneDTOResponse;
import it.govpay.core.dao.pagamenti.dto.ApplicazionePatchDTO;
import it.govpay.core.utils.GovpayConfig;
import it.govpay.core.utils.GpThreadLocal;
import it.govpay.core.utils.validator.ValidatoreIdentificativi;

public class ApplicazioniController extends BaseController {
	
	public static final String AUTODETERMINAZIONE_TIPI_PENDENZA_VALUE= "autodeterminazione";
	public static final String AUTODETERMINAZIONE_TIPI_PENDENZA_LABEL= "Autodeterminazione delle Pendenze";
	
	public static final String AUTORIZZA_TIPI_PENDENZA_STAR = "*";
	public static final String AUTORIZZA_TIPI_PENDENZA_STAR_LABEL= "Tutte";
	public static final String AUTORIZZA_DOMINI_STAR = "*";
	public static final String AUTORIZZA_DOMINI_STAR_LABEL= "Tutti";

     public ApplicazioniController(String nomeServizio,Logger log) {
		super(nomeServizio,log, GovpayConfig.GOVPAY_BACKOFFICE_OPEN_API_FILE_NAME);
     }



    public Response applicazioniIdA2AGET(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , String idA2A) {
    	String methodName = "applicazioniIdA2AGET";  
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
			validatoreId.validaIdApplicazione("idA2A", idA2A);
			
			// Parametri - > DTO Input
			
			GetApplicazioneDTO getApplicazioneDTO = new GetApplicazioneDTO(user, idA2A);
			
			// INIT DAO
			
			ApplicazioniDAO applicazioniDAO = new ApplicazioniDAO(false);
			
			// CHIAMATA AL DAO
			
			GetApplicazioneDTOResponse getApplicazioneDTOResponse = applicazioniDAO.getApplicazione(getApplicazioneDTO);
			
			// CONVERT TO JSON DELLA RISPOSTA
			
			Applicazione response = ApplicazioniConverter.toRsModel(getApplicazioneDTOResponse.getApplicazione());
			
			this.logResponse(uriInfo, httpHeaders, methodName, response.toJSON(null), 200);
			this.log.info(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
			return this.handleResponseOk(Response.status(Status.OK).entity(response.toJSON(null)),transactionId).header(this.transactionIdHeaderName, ctx.getTransactionId()).build();
			
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.log(ctx);
		}
    }


    @SuppressWarnings("unchecked")
	public Response applicazioniIdA2APATCH(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , java.io.InputStream is, String idA2A) {
    	String methodName = "applicazioniIdA2APATCH";  
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
					op.setOp(it.govpay.backoffice.v1.beans.PatchOp.OpEnum.fromValue((String) map.get("op")));
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
			

			this.logResponse(uriInfo, httpHeaders, methodName, response.toJSON(null), 200);
			this.log.info(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
			return this.handleResponseOk(Response.status(Status.OK).entity(response.toJSON(null)),transactionId).build();
			
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.log(ctx);
		}
    }


    public Response applicazioniIdA2APUT(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , String idA2A, java.io.InputStream is) {
    	String methodName = "applicazioniIdApplicazionePUT";  
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
			
			ValidatoreIdentificativi validatoreId = ValidatoreIdentificativi.newInstance();
			validatoreId.validaIdApplicazione("idA2A", idA2A);
			
			String jsonRequest = baos.toString();
			ApplicazionePost applicazioneRequest= JSONSerializable.parse(jsonRequest, ApplicazionePost.class);
			
			applicazioneRequest.validate();
			
			PutApplicazioneDTO putApplicazioneDTO = ApplicazioniConverter.getPutApplicazioneDTO(applicazioneRequest, idA2A, user); 
			
			new ApplicazioneValidator(putApplicazioneDTO.getApplicazione()).validate();
			
			ApplicazioniDAO applicazioniDAO = new ApplicazioniDAO(false);
			
			PutApplicazioneDTOResponse putApplicazioneDTOResponse = applicazioniDAO.createOrUpdate(putApplicazioneDTO);
			
			Status responseStatus = putApplicazioneDTOResponse.isCreated() ?  Status.CREATED : Status.OK;
			
			this.logResponse(uriInfo, httpHeaders, methodName, new byte[0], responseStatus.getStatusCode());
			this.log.info(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
			return this.handleResponseOk(Response.status(responseStatus),transactionId).build();
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.log(ctx);
		}
    }



    public Response applicazioniGET(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , Integer pagina, Integer risultatiPerPagina, String ordinamento, String campi, Boolean abilitato) {
    	String methodName = "applicazioniGET";  
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
			
			FindApplicazioniDTO listaDominiDTO = new FindApplicazioniDTO(user);
			
			listaDominiDTO.setPagina(pagina);
			listaDominiDTO.setLimit(risultatiPerPagina);
			listaDominiDTO.setOrderBy(ordinamento);
			listaDominiDTO.setAbilitato(abilitato);
			
			// INIT DAO
			
			ApplicazioniDAO applicazioniDAO = new ApplicazioniDAO(false);
			
			// CHIAMATA AL DAO
			
			FindApplicazioniDTOResponse listaApplicazioniDTOResponse = applicazioniDAO.findApplicazioni(listaDominiDTO);
			
			// CONVERT TO JSON DELLA RISPOSTA
			
			List<it.govpay.backoffice.v1.beans.Applicazione> results = new ArrayList<>();
			for(it.govpay.bd.model.Applicazione applicazione: listaApplicazioniDTOResponse.getResults()) {
				results.add(ApplicazioniConverter.toRsModel(applicazione));
			}
			
			ListaApplicazioni response = new ListaApplicazioni(results, this.getServicePath(uriInfo),
					listaApplicazioniDTOResponse.getTotalResults(), pagina, risultatiPerPagina);
			
			this.logResponse(uriInfo, httpHeaders, methodName, response.toJSON(campi), 200);
			this.log.info(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName));
			return this.handleResponseOk(Response.status(Status.OK).entity(response.toJSON(campi)),transactionId).build();
			
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.log(ctx);
		}
    }


}


