package it.govpay.rs.v1.controllers.base;

import java.io.ByteArrayOutputStream;
import java.util.stream.Collectors;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.slf4j.Logger;

import it.govpay.core.dao.anagrafica.EntrateDAO;
import it.govpay.core.dao.anagrafica.dto.FindEntrateDTO;
import it.govpay.core.dao.anagrafica.dto.FindEntrateDTOResponse;
import it.govpay.core.dao.anagrafica.dto.GetEntrataDTO;
import it.govpay.core.dao.anagrafica.dto.GetEntrataDTOResponse;
import it.govpay.core.dao.anagrafica.dto.PutEntrataDTO;
import it.govpay.core.dao.anagrafica.dto.PutEntrataDTOResponse;
import it.govpay.core.rs.v1.beans.base.ListaTipiEntrata;
import it.govpay.core.rs.v1.beans.base.TipoEntrata;
import it.govpay.core.rs.v1.beans.base.TipoEntrataPost;
import it.govpay.core.utils.GovpayConfig;
import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.GpThreadLocal;
import it.govpay.model.IAutorizzato;
import it.govpay.rs.BaseRsService;
import it.govpay.rs.v1.beans.converter.EntrateConverter;



public class EntrateController extends it.govpay.rs.BaseController {

     public EntrateController(String nomeServizio,Logger log) {
		super(nomeServizio,log, GovpayConfig.GOVPAY_BACKOFFICE_OPEN_API_FILE_NAME);
     }



    public Response entrateIdEntrataPUT(IAutorizzato user, UriInfo uriInfo, HttpHeaders httpHeaders , String idEntrata, java.io.InputStream is) {
    	String methodName = "entrateIdEntrataPUT";  
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
			TipoEntrataPost entrataRequest= (TipoEntrataPost) TipoEntrataPost.parse(jsonRequest, TipoEntrataPost.class);
			
			PutEntrataDTO putIntermediarioDTO = EntrateConverter.getPutEntrataDTO(entrataRequest, idEntrata, user);
			
			EntrateDAO intermediariDAO = new EntrateDAO();
			
			PutEntrataDTOResponse putIntermediarioDTOResponse = intermediariDAO.createOrUpdateEntrata(putIntermediarioDTO);
			
			Status responseStatus = putIntermediarioDTOResponse.isCreated() ?  Status.CREATED : Status.OK;
			
			this.logResponse(uriInfo, httpHeaders, methodName, new byte[0], responseStatus.getStatusCode());
			this.log.info("Esecuzione " + methodName + " completata."); 
			return this.handleResponseOk(Response.status(responseStatus),transactionId).build();
		}catch (Exception e) {
			return handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			if(ctx != null) ctx.log();
		}
    }



    public Response entrateIdEntrataGET(IAutorizzato user, UriInfo uriInfo, HttpHeaders httpHeaders , String idEntrata) {
    	String methodName = "entrateIdEntrataGET";  
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
			
			GetEntrataDTO getEntrataDTO = new GetEntrataDTO(user, idEntrata);
			
			// INIT DAO
			
			EntrateDAO entrateDAO = new EntrateDAO();
			
			// CHIAMATA AL DAO
			
			GetEntrataDTOResponse listaDominiEntrateDTOResponse = entrateDAO.getEntrata(getEntrataDTO);
			
			// CONVERT TO JSON DELLA RISPOSTA
			
			TipoEntrata response = EntrateConverter.toTipoEntrataRsModel(listaDominiEntrateDTOResponse.getTipoTributo());
			
			this.logResponse(uriInfo, httpHeaders, methodName, response.toJSON(null), 200);
			this.log.info("Esecuzione " + methodName + " completata."); 
			return this.handleResponseOk(Response.status(Status.OK).entity(response.toJSON(null)),transactionId).build();
			
		}catch (Exception e) {
			return handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			if(ctx != null) ctx.log();
		}
    }



    public Response entrateGET(IAutorizzato user, UriInfo uriInfo, HttpHeaders httpHeaders , Integer pagina, Integer risultatiPerPagina, String ordinamento, String campi) {
    	String methodName = "entrateGET";  
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
			
			FindEntrateDTO findEntrateDTO = new FindEntrateDTO(user);
			findEntrateDTO.setOrderBy(ordinamento);
			
			
			// INIT DAO
			
			EntrateDAO entrateDAO = new EntrateDAO();
			
			// CHIAMATA AL DAO
			
			FindEntrateDTOResponse findEntrateDTOResponse = entrateDAO.findEntrate(findEntrateDTO);
			
			// CONVERT TO JSON DELLA RISPOSTA
			
			ListaTipiEntrata response = new ListaTipiEntrata(findEntrateDTOResponse.getResults().stream().map(t -> EntrateConverter.toTipoEntrataRsModel(t)).collect(Collectors.toList()), 
					this.getServicePath(uriInfo), findEntrateDTOResponse.getTotalResults(), pagina, risultatiPerPagina);
			
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


