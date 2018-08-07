package it.govpay.rs.v1.controllers.base;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.openspcoop2.generic_project.exception.ServiceException;
import org.slf4j.Logger;

import it.govpay.core.dao.anagrafica.ApplicazioniDAO;
import it.govpay.core.dao.anagrafica.dto.FindApplicazioniDTO;
import it.govpay.core.dao.anagrafica.dto.FindApplicazioniDTOResponse;
import it.govpay.core.dao.anagrafica.dto.GetApplicazioneDTO;
import it.govpay.core.dao.anagrafica.dto.GetApplicazioneDTOResponse;
import it.govpay.core.dao.anagrafica.dto.PutApplicazioneDTO;
import it.govpay.core.dao.anagrafica.dto.PutApplicazioneDTOResponse;
import it.govpay.core.dao.pagamenti.dto.ApplicazionePatchDTO;
import it.govpay.core.rs.v1.beans.base.Applicazione;
import it.govpay.core.rs.v1.beans.base.ApplicazionePost;
import it.govpay.core.rs.v1.beans.base.ListaApplicazioni;
import it.govpay.core.rs.v1.beans.base.PatchOp;
import it.govpay.core.rs.v1.beans.base.PatchOp.OpEnum;
import it.govpay.core.utils.GovpayConfig;
import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.GpThreadLocal;
import it.govpay.model.IAutorizzato;
import it.govpay.rs.BaseRsService;
import it.govpay.rs.v1.beans.converter.ApplicazioniConverter;



public class ApplicazioniController extends it.govpay.rs.BaseController {
	
	public static final String AUTODETERMINAZIONE_TRIBUTI_VALUE= "autodeterminazione";
	public static final String AUTODETERMINAZIONE_TRIBUTI_LABEL= "Autodeterminazione delle Entrate";

     public ApplicazioniController(String nomeServizio,Logger log) {
		super(nomeServizio,log, GovpayConfig.GOVPAY_BACKOFFICE_OPEN_API_FILE_NAME);
     }



    public Response applicazioniIdA2AGET(IAutorizzato user, UriInfo uriInfo, HttpHeaders httpHeaders , String idA2A) {
    	String methodName = "applicazioniIdA2AGET";  
		GpContext ctx = null;
		String transactionId = null;
		ByteArrayOutputStream baos= null;
		this.log.info("Esecuzione " + methodName + " in corso..."); 
		try{
			baos = new ByteArrayOutputStream();
			this.logRequest(uriInfo, httpHeaders, methodName, baos);
			
			ctx =  GpThreadLocal.get();
			transactionId = ctx.getTransactionId();
			
			// Parametri - > DTO Input
			
			GetApplicazioneDTO getApplicazioneDTO = new GetApplicazioneDTO(user, idA2A);
			
			// INIT DAO
			
			ApplicazioniDAO applicazioniDAO = new ApplicazioniDAO(false);
			
			// CHIAMATA AL DAO
			
			GetApplicazioneDTOResponse getApplicazioneDTOResponse = applicazioniDAO.getApplicazione(getApplicazioneDTO);
			
			// CONVERT TO JSON DELLA RISPOSTA
			
			Applicazione response = ApplicazioniConverter.toRsModel(getApplicazioneDTOResponse.getApplicazione());
			
			this.logResponse(uriInfo, httpHeaders, methodName, response.toJSON(null), 200);
			this.log.info("Esecuzione " + methodName + " completata."); 
			return this.handleResponseOk(Response.status(Status.OK).entity(response.toJSON(null)),transactionId).header(this.transactionIdHeaderName, ctx.getTransactionId()).build();
			
		}catch (Exception e) {
			return handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			if(ctx != null) ctx.log();
		}
    }


    public Response applicazioniIdA2APATCH(IAutorizzato user, UriInfo uriInfo, HttpHeaders httpHeaders , java.io.InputStream is, String idA2A) {
    	String methodName = "applicazioniIdA2APATCH";  
		GpContext ctx = null;
		String transactionId = null;
		ByteArrayOutputStream baos= null;
		this.log.info("Esecuzione " + methodName + " in corso..."); 
		try{
			baos = new ByteArrayOutputStream();
			// salvo il json ricevuto
			BaseRsService.copy(is, baos);
			this.logRequest(uriInfo, httpHeaders, methodName, baos);
			
			ctx =  GpThreadLocal.get();
			transactionId = ctx.getTransactionId();
			
			String jsonRequest = baos.toString();

			ApplicazionePatchDTO verificaPagamentoDTO = new ApplicazionePatchDTO(user);
			verificaPagamentoDTO.setCodApplicazione(idA2A);
			
			List<PatchOp> lstOp = new ArrayList<>();
			
			try {
				List<java.util.LinkedHashMap<?,?>> lst = PatchOp.parse(jsonRequest, List.class);
				for(java.util.LinkedHashMap<?,?> map: lst) {
					PatchOp op = new PatchOp();
					op.setOp(OpEnum.fromValue((String) map.get("op")));
					op.setPath((String) map.get("path"));
					op.setValue(map.get("value"));
					op.validate();
					lstOp.add(op);
				}
			} catch (ServiceException e) {
				PatchOp op = PatchOp.parse(jsonRequest);
				op.validate();
				lstOp.add(op);
			}
			
			
			verificaPagamentoDTO.setOp(lstOp );

			ApplicazioniDAO applicazioniDAO = new ApplicazioniDAO(false);
			
			GetApplicazioneDTOResponse pagamentoPortaleDTOResponse = applicazioniDAO.patch(verificaPagamentoDTO);
			
			Applicazione response = ApplicazioniConverter.toRsModel(pagamentoPortaleDTOResponse.getApplicazione());
			

			this.logResponse(uriInfo, httpHeaders, methodName, response.toJSON(null), 200);
			this.log.info("Esecuzione " + methodName + " completata."); 
			return this.handleResponseOk(Response.status(Status.OK).entity(response.toJSON(null)),transactionId).build();
			
		}catch (Exception e) {
			return handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			if(ctx != null) ctx.log();
		}
    }


    public Response applicazioniIdA2APUT(IAutorizzato user, UriInfo uriInfo, HttpHeaders httpHeaders , String idA2A, java.io.InputStream is) {
    	String methodName = "applicazioniIdApplicazionePUT";  
		GpContext ctx = null;
		String transactionId = null;
		ByteArrayOutputStream baos= null;
		this.log.info("Esecuzione " + methodName + " in corso..."); 
		try{
			baos = new ByteArrayOutputStream();
			// salvo il json ricevuto
			BaseRsService.copy(is, baos);
			this.logRequest(uriInfo, httpHeaders, methodName, baos);
			
			ctx =  GpThreadLocal.get();
			transactionId = ctx.getTransactionId();
			
			String jsonRequest = baos.toString();
			ApplicazionePost applicazioneRequest= (ApplicazionePost) ApplicazionePost.parse(jsonRequest, ApplicazionePost.class);
			applicazioneRequest.validate();
			
			PutApplicazioneDTO putApplicazioneDTO = ApplicazioniConverter.getPutApplicazioneDTO(applicazioneRequest, idA2A, user); 
			
			ApplicazioniDAO applicazioniDAO = new ApplicazioniDAO(false);
			
			PutApplicazioneDTOResponse putApplicazioneDTOResponse = applicazioniDAO.createOrUpdate(putApplicazioneDTO);
			
			Status responseStatus = putApplicazioneDTOResponse.isCreated() ?  Status.CREATED : Status.OK;
			
			this.logResponse(uriInfo, httpHeaders, methodName, new byte[0], responseStatus.getStatusCode());
			this.log.info("Esecuzione " + methodName + " completata."); 
			return this.handleResponseOk(Response.status(responseStatus),transactionId).build();
		}catch (Exception e) {
			return handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			if(ctx != null) ctx.log();
		}
    }



    public Response applicazioniGET(IAutorizzato user, UriInfo uriInfo, HttpHeaders httpHeaders , Integer pagina, Integer risultatiPerPagina, String ordinamento, String campi, Boolean abilitato) {
    	String methodName = "applicazioniGET";  
		GpContext ctx = null;
		String transactionId = null;
		ByteArrayOutputStream baos= null;
		this.log.info("Esecuzione " + methodName + " in corso..."); 
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
			
			List<it.govpay.core.rs.v1.beans.base.Applicazione> results = new ArrayList<it.govpay.core.rs.v1.beans.base.Applicazione>();
			for(it.govpay.bd.model.Applicazione applicazione: listaApplicazioniDTOResponse.getResults()) {
				results.add(ApplicazioniConverter.toRsModel(applicazione));
			}
			
			ListaApplicazioni response = new ListaApplicazioni(results, this.getServicePath(uriInfo),
					listaApplicazioniDTOResponse.getTotalResults(), pagina, risultatiPerPagina);
			
			this.logResponse(uriInfo, httpHeaders, methodName, response.toJSON(campi), 200);
			this.log.info("Esecuzione " + methodName + " completata."); 
			return this.handleResponseOk(Response.status(Status.OK).entity(response.toJSON(campi)),transactionId).build();
			
		}catch (Exception e) {
			return handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			if(ctx != null) ctx.log();
		}
    }


}


