package it.govpay.rs.v1.controllers.pagamenti;

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

import it.govpay.core.dao.anagrafica.UtentiDAO;
import it.govpay.core.dao.anagrafica.dto.DeleteOperatoreDTO;
import it.govpay.core.dao.anagrafica.dto.FindOperatoriDTO;
import it.govpay.core.dao.anagrafica.dto.FindOperatoriDTOResponse;
import it.govpay.core.dao.anagrafica.dto.LeggiOperatoreDTO;
import it.govpay.core.dao.anagrafica.dto.LeggiOperatoreDTOResponse;
import it.govpay.core.dao.anagrafica.dto.PutOperatoreDTO;
import it.govpay.core.dao.anagrafica.dto.PutOperatoreDTOResponse;
import it.govpay.core.rs.v1.beans.ListaOperatori;
import it.govpay.core.rs.v1.beans.Operatore;
import it.govpay.core.rs.v1.beans.base.OperatorePost;
import it.govpay.core.utils.GovpayConfig;
import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.GpThreadLocal;
import it.govpay.model.IAutorizzato;
import it.govpay.rs.BaseRsService;
import it.govpay.rs.v1.beans.converter.OperatoriConverter;
import net.sf.json.JsonConfig;



public class OperatoriController extends it.govpay.rs.BaseController {

     public OperatoriController(String nomeServizio,Logger log) {
		super(nomeServizio,log, GovpayConfig.GOVPAY_PAGAMENTI_OPEN_API_FILE);
     }



    public Response operatoriPrincipalPUT(IAutorizzato user, UriInfo uriInfo, HttpHeaders httpHeaders , String principal, java.io.InputStream is) {
    	String methodName = "operatoriPrincipalPUT";  
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
			JsonConfig jsonConfig = new JsonConfig();
			Map<String, Class<?>> classMap = new HashMap<String, Class<?>>();
			jsonConfig.setClassMap(classMap);
			OperatorePost operatoreRequest= (OperatorePost) OperatorePost.parse(jsonRequest, OperatorePost.class, jsonConfig);
			
			PutOperatoreDTO putOperatoreDTO = OperatoriConverter.getPutOperatoreDTO(operatoreRequest, principal, user); 
			
			UtentiDAO operatoriDAO = new UtentiDAO();
			
			PutOperatoreDTOResponse putOperatoreDTOResponse = operatoriDAO.createOrUpdate(putOperatoreDTO);
			
			Status responseStatus = putOperatoreDTOResponse.isCreated() ?  Status.CREATED : Status.OK;
			
			this.logResponse(uriInfo, httpHeaders, methodName, new byte[0], responseStatus.getStatusCode());
			this.log.info("Esecuzione " + methodName + " completata."); 
			return this.handleResponseOk(Response.status(responseStatus),transactionId).build();
		}catch (Exception e) {
			return handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			if(ctx != null) ctx.log();
		}
    }



    public Response operatoriPrincipalDELETE(IAutorizzato user, UriInfo uriInfo, HttpHeaders httpHeaders , String principal) {
    	String methodName = "aclIdDELETE";  
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

			DeleteOperatoreDTO deleteOperatoreDTO = new DeleteOperatoreDTO(user);

			deleteOperatoreDTO.setPrincipal(principal);

			// INIT DAO

			UtentiDAO operatoriDAO = new UtentiDAO();

			// CHIAMATA AL DAO

			operatoriDAO.deleteOperatore(deleteOperatoreDTO);

			this.logResponse(uriInfo, httpHeaders, methodName, new byte[0], Status.OK.getStatusCode());
			this.log.info("Esecuzione " + methodName + " completata."); 
			return this.handleResponseOk(Response.status(Status.OK),transactionId).build();

		}catch (Exception e) {
			return handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			if(ctx != null) ctx.log();
		}
    }



    public Response operatoriPrincipalGET(IAutorizzato user, UriInfo uriInfo, HttpHeaders httpHeaders , String principal) {
    	String methodName = "intermediariIdIntermediarioGET";  
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
			
			LeggiOperatoreDTO leggiOperatoreDTO = new LeggiOperatoreDTO(user);
			leggiOperatoreDTO.setPrincipal(principal);
			
			// INIT DAO
			
			UtentiDAO operatoriDAO = new UtentiDAO();
			
			// CHIAMATA AL DAO
			
			LeggiOperatoreDTOResponse getOperatoreDTOResponse = operatoriDAO.getOperatore(leggiOperatoreDTO);
			
			// CONVERT TO JSON DELLA RISPOSTA
			Operatore response = OperatoriConverter.toRsModel(getOperatoreDTOResponse.getOperatore());
			
			this.logResponse(uriInfo, httpHeaders, methodName, response.toJSON(null), 200);
			this.log.info("Esecuzione " + methodName + " completata."); 
			return this.handleResponseOk(Response.status(Status.OK).entity(response.toJSON(null)),transactionId).build();
			
		}catch (Exception e) {
			return handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			if(ctx != null) ctx.log();
		}
    }



    public Response operatoriGET(IAutorizzato user, UriInfo uriInfo, HttpHeaders httpHeaders , Integer pagina, Integer risultatiPerPagina, String ordinamento, String campi, Boolean abilitato) {
    	String methodName = "operatoriGET";  
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
			
			FindOperatoriDTO listaDominiDTO = new FindOperatoriDTO(user);
			
			listaDominiDTO.setPagina(pagina);
			listaDominiDTO.setLimit(risultatiPerPagina);
			listaDominiDTO.setOrderBy(ordinamento);
			listaDominiDTO.setAbilitato(abilitato);
			listaDominiDTO.setOrderBy(ordinamento); 
			
			// INIT DAO
			
			UtentiDAO operatoriDAO = new UtentiDAO();
			
			// CHIAMATA AL DAO
			
			FindOperatoriDTOResponse listaOperatoriDTOResponse = operatoriDAO.findOperatori(listaDominiDTO);
			
			// CONVERT TO JSON DELLA RISPOSTA
			
			List<it.govpay.core.rs.v1.beans.Operatore> results = new ArrayList<it.govpay.core.rs.v1.beans.Operatore>();
			for(it.govpay.bd.model.Operatore operatore: listaOperatoriDTOResponse.getResults()) {
				results.add(OperatoriConverter.toRsModel(operatore));
			}
			
			ListaOperatori response = new ListaOperatori(results, this.getServicePath(uriInfo),
					listaOperatoriDTOResponse.getTotalResults(), pagina, risultatiPerPagina);
			
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


