package it.govpay.rs.v1.controllers.pagamenti;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.slf4j.Logger;

import it.govpay.core.dao.anagrafica.AclDAO;
import it.govpay.core.dao.anagrafica.dto.DeleteAclDTO;
import it.govpay.core.dao.anagrafica.dto.LeggiAclDTO;
import it.govpay.core.dao.anagrafica.dto.LeggiAclDTOResponse;
import it.govpay.core.dao.anagrafica.dto.ListaAclDTO;
import it.govpay.core.dao.anagrafica.dto.ListaAclDTOResponse;
import it.govpay.core.dao.anagrafica.dto.PostAclDTO;
import it.govpay.core.dao.anagrafica.dto.PostAclDTOResponse;
import it.govpay.core.rs.v1.beans.ACL;
import it.govpay.core.rs.v1.beans.ListaAcl;
import it.govpay.core.rs.v1.beans.base.AclPost;
import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.GpThreadLocal;
import it.govpay.model.IAutorizzato;
import it.govpay.rs.BaseRsService;
import it.govpay.rs.v1.beans.converter.AclConverter;
import net.sf.json.JsonConfig;



public class AclController extends it.govpay.rs.BaseController {

	public AclController(String nomeServizio,Logger log) {
		super(nomeServizio,log);
	}



	public Response aclGET(IAutorizzato user, UriInfo uriInfo, HttpHeaders httpHeaders , Integer pagina, Integer risultatiPerPagina, String ordinamento, String campi, String ruolo, String principal, String servizio) {
		String methodName = "aclGET";  
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
			ListaAclDTO listaAclDTO = new ListaAclDTO(user);

			listaAclDTO.setPagina(pagina);
			listaAclDTO.setLimit(risultatiPerPagina);
			listaAclDTO.setOrderBy(ordinamento);
			listaAclDTO.setRuolo(ruolo);
			listaAclDTO.setPrincipal(principal);
			listaAclDTO.setServizio(servizio);

			// INIT DAO

			AclDAO aclDAO = new AclDAO();

			// CHIAMATA AL DAO

			ListaAclDTOResponse listaACLDTOResponse = aclDAO.listaAcl(listaAclDTO);

			// CONVERT TO JSON DELLA RISPOSTA

			List<it.govpay.core.rs.v1.beans.ACL> results = new ArrayList<it.govpay.core.rs.v1.beans.ACL>();
			for(it.govpay.model.Acl acl: listaACLDTOResponse.getResults()) {
				results.add(AclConverter.toRsModel(acl));
			}

			ListaAcl response = new ListaAcl(results, this.getServicePath(uriInfo),
					listaACLDTOResponse.getTotalResults(), pagina, risultatiPerPagina);

			this.logResponse(uriInfo, httpHeaders, methodName, response.toJSON(campi), 200);
			this.log.info("Esecuzione " + methodName + " completata."); 
			return this.handleResponseOk(Response.status(Status.OK).entity(response.toJSON(campi)),transactionId).build();

		}catch (Exception e) {
			return handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			if(ctx != null) ctx.log();
		}
	}



	public Response aclIdGET(IAutorizzato user, UriInfo uriInfo, HttpHeaders httpHeaders , BigDecimal id) {
		String methodName = "aclGET";  
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

			LeggiAclDTO listaAclDTO = new LeggiAclDTO(user);

			listaAclDTO.setIdAcl(id.longValue());

			// INIT DAO

			AclDAO aclDAO = new AclDAO();

			// CHIAMATA AL DAO

			LeggiAclDTOResponse leggiACLDTOResponse = aclDAO.leggiAcl(listaAclDTO);

			// CONVERT TO JSON DELLA RISPOSTA

			ACL response = AclConverter.toRsModel(leggiACLDTOResponse.getAcl());

			this.logResponse(uriInfo, httpHeaders, methodName, response.toJSON(null), 200);
			this.log.info("Esecuzione " + methodName + " completata."); 
			return this.handleResponseOk(Response.status(Status.OK).entity(response.toJSON(null)),transactionId).build();

		}catch (Exception e) {
			return handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			if(ctx != null) ctx.log();
		}
	}



	public Response aclPOST(IAutorizzato user, UriInfo uriInfo, HttpHeaders httpHeaders , java.io.InputStream is) {
		String methodName = "aclPOST";  
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
			classMap.put("servizio", String.class);
			jsonConfig.setClassMap(classMap);
			AclPost aclRequest= (AclPost) AclPost.parse(jsonRequest, AclPost.class, jsonConfig);

			PostAclDTO putDominioDTO = AclConverter.getPostAclDTO(aclRequest, user); 

			AclDAO aclDAO = new AclDAO();

			PostAclDTOResponse postAclDTOResponse = aclDAO.create(putDominioDTO);

			Status responseStatus = postAclDTOResponse.isCreated() ? Status.CREATED: Status.OK;

			this.logResponse(uriInfo, httpHeaders, methodName, new byte[0], responseStatus.getStatusCode());
			this.log.info("Esecuzione " + methodName + " completata."); 
			return this.handleResponseOk(Response.status(responseStatus),transactionId).build();
		}catch (Exception e) {
			return handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			if(ctx != null) ctx.log();
		}
	}



	public Response aclIdDELETE(IAutorizzato user, UriInfo uriInfo, HttpHeaders httpHeaders , BigDecimal id) {
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

			DeleteAclDTO deleteAclDTO = new DeleteAclDTO(user);

			deleteAclDTO.setIdAcl(id.longValue());

			// INIT DAO

			AclDAO aclDAO = new AclDAO();

			// CHIAMATA AL DAO

			aclDAO.deleteAcl(deleteAclDTO);

			this.logResponse(uriInfo, httpHeaders, methodName, new byte[0], Status.OK.getStatusCode());
			this.log.info("Esecuzione " + methodName + " completata."); 
			return this.handleResponseOk(Response.status(Status.OK),transactionId).build();

		}catch (Exception e) {
			return handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			if(ctx != null) ctx.log();
		}
	}


}


