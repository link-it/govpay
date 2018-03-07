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

import it.govpay.core.dao.anagrafica.UtentiDAO;
import it.govpay.core.dao.anagrafica.dto.DeleteOperatoreDTO;
import it.govpay.core.dao.anagrafica.dto.FindOperatoriDTO;
import it.govpay.core.dao.anagrafica.dto.FindOperatoriDTOResponse;
import it.govpay.core.dao.anagrafica.dto.LeggiOperatoreDTO;
import it.govpay.core.dao.anagrafica.dto.LeggiOperatoreDTOResponse;
import it.govpay.core.dao.anagrafica.dto.PutOperatoreDTO;
import it.govpay.core.dao.anagrafica.dto.PutOperatoreDTOResponse;
import it.govpay.core.dao.anagrafica.exception.DominioNonTrovatoException;
import it.govpay.core.dao.anagrafica.exception.OperatoreNonTrovatoException;
import it.govpay.core.dao.anagrafica.exception.TipoTributoNonTrovatoException;
import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.GpThreadLocal;
import it.govpay.model.IAutorizzato;
import it.govpay.rs.BaseRsService;
import it.govpay.rs.v1.beans.FaultBean;
import it.govpay.rs.v1.beans.ListaOperatori;
import it.govpay.rs.v1.beans.Operatore;
import it.govpay.rs.v1.beans.base.FaultBean.CategoriaEnum;
import it.govpay.rs.v1.beans.base.OperatorePost;
import it.govpay.rs.v1.beans.converter.OperatoriConverter;
import net.sf.json.JsonConfig;



public class OperatoriController extends it.govpay.rs.BaseController {

     public OperatoriController(String nomeServizio,Logger log) {
		super(nomeServizio,log);
     }



    public Response operatoriPrincipalPUT(IAutorizzato user, UriInfo uriInfo, HttpHeaders httpHeaders , String principal, java.io.InputStream is) {
    	String methodName = "operatoriPrincipalPUT";  
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
			OperatorePost operatoreRequest= (OperatorePost) OperatorePost.parse(jsonRequest, OperatorePost.class, jsonConfig);
			
			PutOperatoreDTO putOperatoreDTO = OperatoriConverter.getPutOperatoreDTO(operatoreRequest, principal, user); 
			
			UtentiDAO operatoriDAO = new UtentiDAO();
			
			PutOperatoreDTOResponse putOperatoreDTOResponse = operatoriDAO.createOrUpdate(putOperatoreDTO);
			
			Status responseStatus = putOperatoreDTOResponse.isCreated() ?  Status.CREATED : Status.OK;
			
			this.logResponse(uriInfo, httpHeaders, methodName, new byte[0], responseStatus.getStatusCode());
			this.log.info("Esecuzione " + methodName + " completata."); 
			return Response.status(responseStatus).build();
		} catch (OperatoreNonTrovatoException | DominioNonTrovatoException | TipoTributoNonTrovatoException e) {
			log.error(e.getMessage(), e);
			FaultBean respKo = new FaultBean();
			respKo.setCategoria(CategoriaEnum.OPERAZIONE);
			respKo.setCodice("");
			respKo.setDescrizione(e.getMessage());
			try {
				this.logResponse(uriInfo, httpHeaders, methodName, respKo, 500);
			}catch(Exception e1) {
				log.error("Errore durante il log della risposta", e1);
			}
			return Response.status(Status.NOT_FOUND).entity(respKo).build();
		} catch (Exception e) {
			log.error("Errore interno durante l'esecuzione del metodo "+ methodName + ": " + e.getMessage(), e);
			FaultBean respKo = new FaultBean();
			respKo.setCategoria(CategoriaEnum.INTERNO);
			respKo.setCodice(CategoriaEnum.INTERNO.name());
			respKo.setDescrizione(e.getMessage());
			try {
				this.logResponse(uriInfo, httpHeaders, methodName, respKo.toJSON(null), 500);
			}catch(Exception e1) {
				log.error("Errore durante il log della risposta", e1);
			}
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(respKo.toJSON(null)).build();
		} finally {
			if(ctx != null) ctx.log();
		}
    }



    public Response operatoriPrincipalDELETE(IAutorizzato user, UriInfo uriInfo, HttpHeaders httpHeaders , String principal) {
    	String methodName = "aclIdDELETE";  
		GpContext ctx = null;
		ByteArrayOutputStream baos= null;
		this.log.info("Esecuzione " + methodName + " in corso..."); 
		try{
			baos = new ByteArrayOutputStream();
			this.logRequest(uriInfo, httpHeaders, methodName, baos);

			ctx =  GpThreadLocal.get();

			// Parametri - > DTO Input

			DeleteOperatoreDTO deleteOperatoreDTO = new DeleteOperatoreDTO(user);

			deleteOperatoreDTO.setPrincipal(principal);

			// INIT DAO

			UtentiDAO operatoriDAO = new UtentiDAO();

			// CHIAMATA AL DAO

			operatoriDAO.deleteOperatore(deleteOperatoreDTO);

			this.logResponse(uriInfo, httpHeaders, methodName, new byte[0], Status.OK.getStatusCode());
			this.log.info("Esecuzione " + methodName + " completata."); 
			return Response.status(Status.OK).build();

		} catch (OperatoreNonTrovatoException e) {
			log.error(e.getMessage(), e);
			FaultBean respKo = new FaultBean();
			respKo.setCategoria(CategoriaEnum.OPERAZIONE);
			respKo.setCodice("");
			respKo.setDescrizione(e.getMessage());
			try {
				this.logResponse(uriInfo, httpHeaders, methodName, respKo, Status.NOT_FOUND.getStatusCode());
			}catch(Exception e1) {
				log.error("Errore durante il log della risposta", e1);
			}
			return Response.status(Status.NOT_FOUND).entity(respKo).build();
		}catch (Exception e) {
			log.error("Errore interno durante l'eliminazione della ACL: " + e.getMessage(), e);
			FaultBean respKo = new FaultBean();
			respKo.setCategoria(CategoriaEnum.INTERNO);
			respKo.setCodice("");
			respKo.setDescrizione(e.getMessage());
			try {
				this.logResponse(uriInfo, httpHeaders, methodName, respKo, 500);
			}catch(Exception e1) {
				log.error("Errore durante il log della risposta", e1);
			}
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(respKo).build();
		} finally {
			if(ctx != null) ctx.log();
		}
    }



    public Response operatoriPrincipalGET(IAutorizzato user, UriInfo uriInfo, HttpHeaders httpHeaders , String principal) {
    	String methodName = "intermediariIdIntermediarioGET";  
		GpContext ctx = null;
		ByteArrayOutputStream baos= null;
		this.log.info("Esecuzione " + methodName + " in corso..."); 
		try{
			baos = new ByteArrayOutputStream();
			this.logRequest(uriInfo, httpHeaders, methodName, baos);
			
			ctx =  GpThreadLocal.get();
			
			// Parametri - > DTO Input
			
			LeggiOperatoreDTO leggiOperatoreDTO = new LeggiOperatoreDTO(user);
			leggiOperatoreDTO.setPrincipal(principal);
			
			// INIT DAO
			
			UtentiDAO operatoriDAO = new UtentiDAO();
			
			// CHIAMATA AL DAO
			
			LeggiOperatoreDTOResponse getOperatoreDTOResponse = operatoriDAO.getOperatore(leggiOperatoreDTO);
			
			// CONVERT TO JSON DELLA RISPOSTA
			Operatore response = new it.govpay.rs.v1.beans.Operatore(getOperatoreDTOResponse.getOperatore());
			
			this.logResponse(uriInfo, httpHeaders, methodName, response.toJSON(null), 200);
			this.log.info("Esecuzione " + methodName + " completata."); 
			return Response.status(Status.OK).entity(response.toJSON(null)).build();
			
		}catch (OperatoreNonTrovatoException e) {
			log.error(e.getMessage(), e);
			FaultBean respKo = new FaultBean();
			respKo.setCategoria(CategoriaEnum.OPERAZIONE);
			respKo.setCodice("");
			respKo.setDescrizione(e.getMessage());
			try {
				this.logResponse(uriInfo, httpHeaders, methodName, respKo, 500);
			}catch(Exception e1) {
				log.error("Errore durante il log della risposta", e1);
			}
			return Response.status(Status.NOT_FOUND).entity(respKo).build();
		} catch (Exception e) {
			log.error("Errore interno durante la ricerca deigli Intermediari: " + e.getMessage(), e);
			FaultBean respKo = new FaultBean();
			respKo.setCategoria(CategoriaEnum.INTERNO);
			respKo.setCodice("");
			respKo.setDescrizione(e.getMessage());
			try {
				this.logResponse(uriInfo, httpHeaders, methodName, respKo, 500);
			}catch(Exception e1) {
				log.error("Errore durante il log della risposta", e1);
			}
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(respKo).build();
		} finally {
			if(ctx != null) ctx.log();
		}
    }



    public Response operatoriGET(IAutorizzato user, UriInfo uriInfo, HttpHeaders httpHeaders , Integer pagina, Integer risultatiPerPagina, String ordinamento, String campi, Boolean abilitato) {
    	String methodName = "operatoriGET";  
		GpContext ctx = null;
		ByteArrayOutputStream baos= null;
		this.log.info("Esecuzione " + methodName + " in corso..."); 
		try{
			baos = new ByteArrayOutputStream();
			this.logRequest(uriInfo, httpHeaders, methodName, baos);
			
			ctx =  GpThreadLocal.get();
			
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
			
			List<it.govpay.rs.v1.beans.Operatore> results = new ArrayList<it.govpay.rs.v1.beans.Operatore>();
			for(it.govpay.bd.model.Operatore operatore: listaOperatoriDTOResponse.getResults()) {
				results.add(new it.govpay.rs.v1.beans.Operatore(operatore));
			}
			
			ListaOperatori response = new ListaOperatori(results, uriInfo.getRequestUri(),
					listaOperatoriDTOResponse.getTotalResults(), pagina, risultatiPerPagina);
			
			this.logResponse(uriInfo, httpHeaders, methodName, response.toJSON(campi), 200);
			this.log.info("Esecuzione " + methodName + " completata."); 
			return Response.status(Status.OK).entity(response.toJSON(campi)).build();
			
		}catch (Exception e) {
			log.error("Errore interno durante la ricerca delle Operatori: " + e.getMessage(), e);
			FaultBean respKo = new FaultBean();
			respKo.setCategoria(CategoriaEnum.INTERNO);
			respKo.setCodice("");
			respKo.setDescrizione(e.getMessage());
			try {
				this.logResponse(uriInfo, httpHeaders, methodName, respKo, 500);
			}catch(Exception e1) {
				log.error("Errore durante il log della risposta", e1);
			}
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(respKo).build();
		} finally {
			if(ctx != null) ctx.log();
		}
    }


}


