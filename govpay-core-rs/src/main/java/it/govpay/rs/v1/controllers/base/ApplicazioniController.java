package it.govpay.rs.v1.controllers.base;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.slf4j.Logger;

import it.govpay.core.dao.anagrafica.ApplicazioniDAO;
import it.govpay.core.dao.anagrafica.dto.FindApplicazioniDTO;
import it.govpay.core.dao.anagrafica.dto.FindApplicazioniDTOResponse;
import it.govpay.core.dao.anagrafica.dto.GetApplicazioneDTO;
import it.govpay.core.dao.anagrafica.dto.GetApplicazioneDTOResponse;
import it.govpay.core.dao.anagrafica.dto.PutApplicazioneDTO;
import it.govpay.core.dao.anagrafica.dto.PutApplicazioneDTOResponse;
import it.govpay.core.rs.v1.beans.Applicazione;
import it.govpay.core.rs.v1.beans.ListaApplicazioni;
import it.govpay.core.rs.v1.beans.base.ApplicazionePost;
import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.GpThreadLocal;
import it.govpay.model.IAutorizzato;
import it.govpay.rs.BaseRsService;
import it.govpay.rs.v1.beans.converter.ApplicazioniConverter;
import net.sf.json.JsonConfig;



public class ApplicazioniController extends it.govpay.rs.BaseController {

     public ApplicazioniController(String nomeServizio,Logger log) {
		super(nomeServizio,log);
     }



    public Response applicazioniIdA2AGET(IAutorizzato user, UriInfo uriInfo, HttpHeaders httpHeaders , String idA2A) {
    	String methodName = "applicazioniIdA2AGET";  
		GpContext ctx = null;
		ByteArrayOutputStream baos= null;
		this.log.info("Esecuzione " + methodName + " in corso..."); 
		try{
			baos = new ByteArrayOutputStream();
			this.logRequest(uriInfo, httpHeaders, methodName, baos);
			
			ctx =  GpThreadLocal.get();
			
			// Parametri - > DTO Input
			
			GetApplicazioneDTO getApplicazioneDTO = new GetApplicazioneDTO(user, idA2A);
			
			// INIT DAO
			
			ApplicazioniDAO applicazioniDAO = new ApplicazioniDAO();
			
			// CHIAMATA AL DAO
			
			GetApplicazioneDTOResponse getApplicazioneDTOResponse = applicazioniDAO.getApplicazione(getApplicazioneDTO);
			
			// CONVERT TO JSON DELLA RISPOSTA
			
			Applicazione response = ApplicazioniConverter.toRsModel(getApplicazioneDTOResponse.getApplicazione());
			
			this.logResponse(uriInfo, httpHeaders, methodName, response.toJSON(null), 200);
			this.log.info("Esecuzione " + methodName + " completata."); 
			return Response.status(Status.OK).entity(response.toJSON(null)).build();
			
		}catch (Exception e) {
			return handleException(uriInfo, httpHeaders, methodName, e);
		} finally {
			if(ctx != null) ctx.log();
		}
    }



    public Response applicazioniIdA2APUT(IAutorizzato user, UriInfo uriInfo, HttpHeaders httpHeaders , String idA2A, java.io.InputStream is) {
    	String methodName = "applicazioniIdApplicazionePUT";  
		GpContext ctx = null;
		ByteArrayOutputStream baos= null;
		this.log.info("Esecuzione " + methodName + " in corso..."); 
		try{
			baos = new ByteArrayOutputStream();
			// salvo il json ricevuto
			BaseRsService.copy(is, baos);
			this.logRequest(uriInfo, httpHeaders, methodName, baos);
			
			ctx =  GpThreadLocal.get();
			
			String jsonRequest = baos.toString();
			JsonConfig jsonConfig = new JsonConfig();
			Map<String, Class<?>> classMap = new HashMap<String, Class<?>>();
			jsonConfig.setClassMap(classMap);
			ApplicazionePost applicazioneRequest= (ApplicazionePost) ApplicazionePost.parse(jsonRequest, ApplicazionePost.class, jsonConfig);
			
			PutApplicazioneDTO putApplicazioneDTO = ApplicazioniConverter.getPutApplicazioneDTO(applicazioneRequest, idA2A, user); 
			
			ApplicazioniDAO applicazioniDAO = new ApplicazioniDAO();
			
			PutApplicazioneDTOResponse putApplicazioneDTOResponse = applicazioniDAO.createOrUpdate(putApplicazioneDTO);
			
			Status responseStatus = putApplicazioneDTOResponse.isCreated() ?  Status.CREATED : Status.OK;
			
			this.logResponse(uriInfo, httpHeaders, methodName, new byte[0], responseStatus.getStatusCode());
			this.log.info("Esecuzione " + methodName + " completata."); 
			return Response.status(responseStatus).build();
		}catch (Exception e) {
			return handleException(uriInfo, httpHeaders, methodName, e);
		} finally {
			if(ctx != null) ctx.log();
		}
    }



    public Response applicazioniGET(IAutorizzato user, UriInfo uriInfo, HttpHeaders httpHeaders , Integer pagina, Integer risultatiPerPagina, String ordinamento, String campi, Boolean abilitato) {
    	String methodName = "applicazioniGET";  
		GpContext ctx = null;
		ByteArrayOutputStream baos= null;
		this.log.info("Esecuzione " + methodName + " in corso..."); 
		try{
			baos = new ByteArrayOutputStream();
			this.logRequest(uriInfo, httpHeaders, methodName, baos);
			
			ctx =  GpThreadLocal.get();
			
			// Parametri - > DTO Input
			
			FindApplicazioniDTO listaDominiDTO = new FindApplicazioniDTO(user);
			
			listaDominiDTO.setPagina(pagina);
			listaDominiDTO.setLimit(risultatiPerPagina);
			listaDominiDTO.setOrderBy(ordinamento);
			listaDominiDTO.setAbilitato(abilitato);
			
			// INIT DAO
			
			ApplicazioniDAO applicazioniDAO = new ApplicazioniDAO();
			
			// CHIAMATA AL DAO
			
			FindApplicazioniDTOResponse listaApplicazioniDTOResponse = applicazioniDAO.findApplicazioni(listaDominiDTO);
			
			// CONVERT TO JSON DELLA RISPOSTA
			
			List<it.govpay.core.rs.v1.beans.Applicazione> results = new ArrayList<it.govpay.core.rs.v1.beans.Applicazione>();
			for(it.govpay.bd.model.Applicazione applicazione: listaApplicazioniDTOResponse.getResults()) {
				results.add(ApplicazioniConverter.toRsModel(applicazione));
			}
			
			ListaApplicazioni response = new ListaApplicazioni(results, this.getServicePath(uriInfo),
					listaApplicazioniDTOResponse.getTotalResults(), pagina, risultatiPerPagina);
			
			this.logResponse(uriInfo, httpHeaders, methodName, response.toJSON(campi), 200);
			this.log.info("Esecuzione " + methodName + " completata."); 
			return Response.status(Status.OK).entity(response.toJSON(campi)).build();
			
		}catch (Exception e) {
			return handleException(uriInfo, httpHeaders, methodName, e);
		} finally {
			if(ctx != null) ctx.log();
		}
    }


}


