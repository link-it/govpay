package it.govpay.rs.v1.controllers.base;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;
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
import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.GpThreadLocal;
import it.govpay.model.IAutorizzato;
import it.govpay.rs.BaseRsService;
import it.govpay.rs.v1.beans.FaultBean;
import it.govpay.rs.v1.beans.ListaTipiEntrata;
import it.govpay.rs.v1.beans.TipoEntrata;
import it.govpay.rs.v1.beans.base.FaultBean.CategoriaEnum;
import it.govpay.rs.v1.beans.base.TipoEntrataPost;
import it.govpay.rs.v1.beans.converter.EntrateConverter;
import net.sf.json.JsonConfig;



public class EntrateController extends it.govpay.rs.BaseController {

     public EntrateController(String nomeServizio,Logger log) {
		super(nomeServizio,log);
     }



    public Response entrateIdEntrataPUT(IAutorizzato user, UriInfo uriInfo, HttpHeaders httpHeaders , String idEntrata, java.io.InputStream is) {
    	String methodName = "entrateIdEntrataPUT";  
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
			classMap.put("tipoContabilita", String.class);
			jsonConfig.setClassMap(classMap);
			TipoEntrataPost entrataRequest= (TipoEntrataPost) TipoEntrataPost.parse(jsonRequest, TipoEntrataPost.class, jsonConfig);
			
			PutEntrataDTO putIntermediarioDTO = EntrateConverter.getPutEntrataDTO(entrataRequest, idEntrata, user);
			
			EntrateDAO intermediariDAO = new EntrateDAO();
			
			PutEntrataDTOResponse putIntermediarioDTOResponse = intermediariDAO.createOrUpdateEntrata(putIntermediarioDTO);
			
			Status responseStatus = putIntermediarioDTOResponse.isCreated() ?  Status.CREATED : Status.OK;
			
			this.logResponse(uriInfo, httpHeaders, methodName, new byte[0], responseStatus.getStatusCode());
			this.log.info("Esecuzione " + methodName + " completata."); 
			return Response.status(responseStatus).build();
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



    public Response entrateIdEntrataGET(IAutorizzato user, UriInfo uriInfo, HttpHeaders httpHeaders , String idEntrata) {
    	String methodName = "entrateIdEntrataGET";  
		GpContext ctx = null;
		ByteArrayOutputStream baos= null;
		this.log.info("Esecuzione " + methodName + " in corso..."); 
		try{
			baos = new ByteArrayOutputStream();
			this.logRequest(uriInfo, httpHeaders, methodName, baos);
			
			ctx =  GpThreadLocal.get();
			
			// Parametri - > DTO Input
			
			GetEntrataDTO getEntrataDTO = new GetEntrataDTO(user, idEntrata);
			
			// INIT DAO
			
			EntrateDAO entrateDAO = new EntrateDAO();
			
			// CHIAMATA AL DAO
			
			GetEntrataDTOResponse listaDominiEntrateDTOResponse = entrateDAO.getEntrata(getEntrataDTO);
			
			// CONVERT TO JSON DELLA RISPOSTA
			
			TipoEntrata response = new it.govpay.rs.v1.beans.TipoEntrata(listaDominiEntrateDTOResponse.getTipoTributo());
			
			this.logResponse(uriInfo, httpHeaders, methodName, response.toJSON(null), 200);
			this.log.info("Esecuzione " + methodName + " completata."); 
			return Response.status(Status.OK).entity(response.toJSON(null)).build();
			
		}catch (Exception e) {
			log.error("Errore interno durante la ricerca delle entrate: " + e.getMessage(), e);
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



    public Response entrateGET(IAutorizzato user, UriInfo uriInfo, HttpHeaders httpHeaders , Integer pagina, Integer risultatiPerPagina, String ordinamento, String campi) {
    	String methodName = "entrateGET";  
		GpContext ctx = null;
		ByteArrayOutputStream baos= null;
		this.log.info("Esecuzione " + methodName + " in corso..."); 
		try{
			baos = new ByteArrayOutputStream();
			this.logRequest(uriInfo, httpHeaders, methodName, baos);
			
			ctx =  GpThreadLocal.get();
			
			// Parametri - > DTO Input
			
			FindEntrateDTO findEntrateDTO = new FindEntrateDTO(user);
			findEntrateDTO.setOrderBy(ordinamento);
			
			
			// INIT DAO
			
			EntrateDAO entrateDAO = new EntrateDAO();
			
			// CHIAMATA AL DAO
			
			FindEntrateDTOResponse findEntrateDTOResponse = entrateDAO.findEntrate(findEntrateDTO);
			
			// CONVERT TO JSON DELLA RISPOSTA
			
			ListaTipiEntrata response = new ListaTipiEntrata(findEntrateDTOResponse.getResults().stream().map(t -> new TipoEntrata(t)).collect(Collectors.toList()), 
					uriInfo.getRequestUri(), findEntrateDTOResponse.getTotalResults(), pagina, risultatiPerPagina);
			
			this.logResponse(uriInfo, httpHeaders, methodName, response.toJSON(campi), 200);
			this.log.info("Esecuzione " + methodName + " completata."); 
			return Response.status(Status.OK).entity(response.toJSON(campi)).build();
			
		}catch (Exception e) {
			log.error("Errore interno durante la ricerca delle entrate: " + e.getMessage(), e);
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


