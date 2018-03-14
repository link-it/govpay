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

import it.govpay.core.dao.anagrafica.IntermediariDAO;
import it.govpay.core.dao.anagrafica.dto.FindIntermediariDTO;
import it.govpay.core.dao.anagrafica.dto.FindIntermediariDTOResponse;
import it.govpay.core.dao.anagrafica.dto.FindStazioniDTO;
import it.govpay.core.dao.anagrafica.dto.FindStazioniDTOResponse;
import it.govpay.core.dao.anagrafica.dto.GetIntermediarioDTO;
import it.govpay.core.dao.anagrafica.dto.GetIntermediarioDTOResponse;
import it.govpay.core.dao.anagrafica.dto.GetStazioneDTO;
import it.govpay.core.dao.anagrafica.dto.GetStazioneDTOResponse;
import it.govpay.core.dao.anagrafica.dto.PutIntermediarioDTO;
import it.govpay.core.dao.anagrafica.dto.PutIntermediarioDTOResponse;
import it.govpay.core.dao.anagrafica.dto.PutStazioneDTO;
import it.govpay.core.dao.anagrafica.dto.PutStazioneDTOResponse;
import it.govpay.core.dao.anagrafica.exception.IntermediarioNonTrovatoException;
import it.govpay.core.dao.anagrafica.exception.StazioneNonTrovataException;
import it.govpay.core.rs.v1.beans.Intermediario;
import it.govpay.core.rs.v1.beans.ListaIntermediari;
import it.govpay.core.rs.v1.beans.ListaStazioni;
import it.govpay.core.rs.v1.beans.Stazione;
import it.govpay.core.rs.v1.beans.base.FaultBean;
import it.govpay.core.rs.v1.beans.base.FaultBean.CategoriaEnum;
import it.govpay.core.rs.v1.beans.base.IntermediarioPost;
import it.govpay.core.rs.v1.beans.base.StazionePost;
import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.GpThreadLocal;
import it.govpay.model.IAutorizzato;
import it.govpay.rs.BaseRsService;
import it.govpay.rs.v1.beans.converter.IntermediariConverter;
import it.govpay.rs.v1.beans.converter.StazioniConverter;
import net.sf.json.JsonConfig;



public class IntermediariController extends it.govpay.rs.BaseController {

     public IntermediariController(String nomeServizio,Logger log) {
		super(nomeServizio,log);
     }



    public Response intermediariIdIntermediarioGET(IAutorizzato user, UriInfo uriInfo, HttpHeaders httpHeaders , String idIntermediario) {
    	String methodName = "intermediariIdIntermediarioGET";  
		GpContext ctx = null;
		ByteArrayOutputStream baos= null;
		this.log.info("Esecuzione " + methodName + " in corso..."); 
		try{
			baos = new ByteArrayOutputStream();
			this.logRequest(uriInfo, httpHeaders, methodName, baos);
			
			ctx =  GpThreadLocal.get();
			
			// Parametri - > DTO Input
			
			GetIntermediarioDTO getIntermediarioDTO = new GetIntermediarioDTO(user, idIntermediario);
			
			// INIT DAO
			
			IntermediariDAO intermediariDAO = new IntermediariDAO();
			
			// CHIAMATA AL DAO
			
			GetIntermediarioDTOResponse getIntermediarioDTOResponse = intermediariDAO.getIntermediario(getIntermediarioDTO);
			
			// CONVERT TO JSON DELLA RISPOSTA
			Intermediario response = IntermediariConverter.toRsModel(getIntermediarioDTOResponse.getIntermediario());
			
			this.logResponse(uriInfo, httpHeaders, methodName, response.toJSON(null), 200);
			this.log.info("Esecuzione " + methodName + " completata."); 
			return Response.status(Status.OK).entity(response.toJSON(null)).build();
			
		}catch (IntermediarioNonTrovatoException e) {
			log.error(e.getMessage(), e);
			FaultBean respKo = new FaultBean();
			respKo.setCategoria(CategoriaEnum.OPERAZIONE);
			respKo.setCodice("");
			respKo.setDescrizione(e.getMessage());
			try {
				this.logResponse(uriInfo, httpHeaders, methodName, respKo.toJSON(null), Status.NOT_FOUND.getStatusCode());
			}catch(Exception e1) {
				log.error("Errore durante il log della risposta", e1);
			}
			return Response.status(Status.NOT_FOUND).entity(respKo.toJSON(null)).build();
		} catch (Exception e) {
			log.error("Errore interno durante la ricerca deigli Intermediari: " + e.getMessage(), e);
			FaultBean respKo = new FaultBean();
			respKo.setCategoria(CategoriaEnum.INTERNO);
			respKo.setCodice("");
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



    public Response intermediariIdIntermediarioStazioniIdStazionePUT(IAutorizzato user, UriInfo uriInfo, HttpHeaders httpHeaders , String idIntermediario, String idStazione, java.io.InputStream is) {
    	String methodName = "intermediariIdIntermediarioStazioniIdStazionePUT";  
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
			StazionePost inrtermediarioRequest= (StazionePost) StazionePost.parse(jsonRequest, StazionePost.class, jsonConfig);
			
			PutStazioneDTO putIntermediarioDTO = StazioniConverter.getPutStazioneDTO(inrtermediarioRequest, idIntermediario, idStazione, user);
			
			IntermediariDAO intermediariDAO = new IntermediariDAO();
			
			PutStazioneDTOResponse putIntermediarioDTOResponse = intermediariDAO.createOrUpdateStazione(putIntermediarioDTO);
			
			Status responseStatus = putIntermediarioDTOResponse.isCreated() ?  Status.CREATED : Status.OK;
			
			this.logResponse(uriInfo, httpHeaders, methodName, new byte[0], responseStatus.getStatusCode());
			this.log.info("Esecuzione " + methodName + " completata."); 
			return Response.status(responseStatus).build();
		} catch (IntermediarioNonTrovatoException e) {
			log.error(e.getMessage(), e);
			FaultBean respKo = new FaultBean();
			respKo.setCategoria(CategoriaEnum.OPERAZIONE);
			respKo.setCodice("");
			respKo.setDescrizione(e.getMessage());
			try {
				this.logResponse(uriInfo, httpHeaders, methodName, respKo.toJSON(null), Status.NOT_FOUND.getStatusCode());
			}catch(Exception e1) {
				log.error("Errore durante il log della risposta", e1);
			}
			return Response.status(Status.NOT_FOUND).entity(respKo.toJSON(null)).build();
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



    public Response intermediariGET(IAutorizzato user, UriInfo uriInfo, HttpHeaders httpHeaders , Integer pagina, Integer risultatiPerPagina, String ordinamento, String campi, Boolean abilitato) {
    	String methodName = "intermediariGET";  
		GpContext ctx = null;
		ByteArrayOutputStream baos= null;
		this.log.info("Esecuzione " + methodName + " in corso..."); 
		try{
			baos = new ByteArrayOutputStream();
			this.logRequest(uriInfo, httpHeaders, methodName, baos);
			
			ctx =  GpThreadLocal.get();
			
			// Parametri - > DTO Input
			
			FindIntermediariDTO listaIntermediariDTO = new FindIntermediariDTO(user);
			
			listaIntermediariDTO.setPagina(pagina);
			listaIntermediariDTO.setLimit(risultatiPerPagina);
			listaIntermediariDTO.setOrderBy(ordinamento);
			listaIntermediariDTO.setAbilitato(abilitato);
			
			// INIT DAO
			
			IntermediariDAO intermediariDAO = new IntermediariDAO();
			
			// CHIAMATA AL DAO
			
			FindIntermediariDTOResponse listaIntermediariDTOResponse = intermediariDAO.findIntermediari(listaIntermediariDTO);
			
			// CONVERT TO JSON DELLA RISPOSTA
			
			List<it.govpay.core.rs.v1.beans.Intermediario> results = new ArrayList<it.govpay.core.rs.v1.beans.Intermediario>();
			for(it.govpay.model.Intermediario intermediario: listaIntermediariDTOResponse.getResults()) {
				results.add(IntermediariConverter.toRsModel(intermediario));
			}
			
			ListaIntermediari response = new ListaIntermediari(results, uriInfo.getRequestUri(),
					listaIntermediariDTOResponse.getTotalResults(), pagina, risultatiPerPagina);
			
			this.logResponse(uriInfo, httpHeaders, methodName, response.toJSON(campi), 200);
			this.log.info("Esecuzione " + methodName + " completata."); 
			return Response.status(Status.OK).entity(response.toJSON(campi)).build();
			
		}catch (Exception e) {
			log.error("Errore interno durante la ricerca dei Intermediari: " + e.getMessage(), e);
			FaultBean respKo = new FaultBean();
			respKo.setCategoria(CategoriaEnum.INTERNO);
			respKo.setCodice("");
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



    public Response intermediariIdIntermediarioPUT(IAutorizzato user, UriInfo uriInfo, HttpHeaders httpHeaders , String idIntermediario, java.io.InputStream is) {
    	String methodName = "intermediariIdIntermediarioPUT";  
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
			IntermediarioPost inrtermediarioRequest= (IntermediarioPost) IntermediarioPost.parse(jsonRequest, IntermediarioPost.class, jsonConfig);
			
			PutIntermediarioDTO putIntermediarioDTO = IntermediariConverter.getPutIntermediarioDTO(inrtermediarioRequest, idIntermediario, user);
			
			IntermediariDAO intermediariDAO = new IntermediariDAO();
			
			PutIntermediarioDTOResponse putIntermediarioDTOResponse = intermediariDAO.createOrUpdateIntermediario(putIntermediarioDTO);
			
			Status responseStatus = putIntermediarioDTOResponse.isCreated() ?  Status.CREATED : Status.OK;
			
			this.logResponse(uriInfo, httpHeaders, methodName, new byte[0], responseStatus.getStatusCode());
			this.log.info("Esecuzione " + methodName + " completata."); 
			return Response.status(responseStatus).build();
		} catch (IntermediarioNonTrovatoException e) {
			log.error(e.getMessage(), e);
			FaultBean respKo = new FaultBean();
			respKo.setCategoria(CategoriaEnum.OPERAZIONE);
			respKo.setCodice("");
			respKo.setDescrizione(e.getMessage());
			try {
				this.logResponse(uriInfo, httpHeaders, methodName, respKo.toJSON(null), Status.NOT_FOUND.getStatusCode());
			}catch(Exception e1) {
				log.error("Errore durante il log della risposta", e1);
			}
			return Response.status(Status.NOT_FOUND).entity(respKo.toJSON(null)).build();
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



    public Response intermediariIdIntermediarioStazioniGET(IAutorizzato user, UriInfo uriInfo, HttpHeaders httpHeaders , String idIntermediario, Integer pagina, Integer risultatiPerPagina, String ordinamento, String campi, Boolean abilitato) {
    	String methodName = "intermediariIdIntermediarioStazioniGET";  
		GpContext ctx = null;
		ByteArrayOutputStream baos= null;
		this.log.info("Esecuzione " + methodName + " in corso..."); 
		try{
			baos = new ByteArrayOutputStream();
			this.logRequest(uriInfo, httpHeaders, methodName, baos);
			
			ctx =  GpThreadLocal.get();
			
			// Parametri - > DTO Input
			
			FindStazioniDTO listaStazioniDTO = new FindStazioniDTO(user);
			
			listaStazioniDTO.setPagina(pagina);
			listaStazioniDTO.setLimit(risultatiPerPagina);
			listaStazioniDTO.setOrderBy(ordinamento);
			listaStazioniDTO.setAbilitato(abilitato);
			listaStazioniDTO.setCodIntermediario(idIntermediario);
			
			// INIT DAO
			
			IntermediariDAO intermediariDAO = new IntermediariDAO();
			
			// CHIAMATA AL DAO
			
			FindStazioniDTOResponse listaStazioniDTOResponse = intermediariDAO.findStazioni(listaStazioniDTO);
			
			// CONVERT TO JSON DELLA RISPOSTA
			
			List<it.govpay.core.rs.v1.beans.Stazione> results = new ArrayList<it.govpay.core.rs.v1.beans.Stazione>();
			for(it.govpay.bd.model.Stazione stazione: listaStazioniDTOResponse.getResults()) {
				results.add(StazioniConverter.toRsModel(stazione));
			}
			
			ListaStazioni response = new ListaStazioni(results, uriInfo.getRequestUri(),
					listaStazioniDTOResponse.getTotalResults(), pagina, risultatiPerPagina);
			
			this.logResponse(uriInfo, httpHeaders, methodName, response.toJSON(campi), 200);
			this.log.info("Esecuzione " + methodName + " completata."); 
			return Response.status(Status.OK).entity(response.toJSON(campi)).build();
			
		}catch (Exception e) {
			log.error("Errore interno durante la ricerca delle Stazioni: " + e.getMessage(), e);
			FaultBean respKo = new FaultBean();
			respKo.setCategoria(CategoriaEnum.INTERNO);
			respKo.setCodice("");
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



    public Response intermediariIdIntermediarioStazioniIdStazioneGET(IAutorizzato user, UriInfo uriInfo, HttpHeaders httpHeaders , String idIntermediario, String idStazione) {
    	String methodName = "intermediariIdIntermediarioStazioniIdStazioneGET";  
		GpContext ctx = null;
		ByteArrayOutputStream baos= null;
		this.log.info("Esecuzione " + methodName + " in corso..."); 
		try{
			baos = new ByteArrayOutputStream();
			this.logRequest(uriInfo, httpHeaders, methodName, baos);
			
			ctx =  GpThreadLocal.get();
			
			// Parametri - > DTO Input
			
			GetStazioneDTO getStazioneDTO = new GetStazioneDTO(user, idIntermediario, idStazione);
			
			// INIT DAO
			
			IntermediariDAO intermediariDAO = new IntermediariDAO();
			
			// CHIAMATA AL DAO
			
			GetStazioneDTOResponse getStazioneDTOResponse = intermediariDAO.getStazione(getStazioneDTO);
			
			// CONVERT TO JSON DELLA RISPOSTA
			
			Stazione response = StazioniConverter.toRsModel(getStazioneDTOResponse.getStazione());
			
			this.logResponse(uriInfo, httpHeaders, methodName, response.toJSON(null), 200);
			this.log.info("Esecuzione " + methodName + " completata."); 
			return Response.status(Status.OK).entity(response.toJSON(null)).build();
			
		}catch (StazioneNonTrovataException e) {
			log.error(e.getMessage(), e);
			FaultBean respKo = new FaultBean();
			respKo.setCategoria(CategoriaEnum.OPERAZIONE);
			respKo.setCodice("");
			respKo.setDescrizione(e.getMessage());
			try {
				this.logResponse(uriInfo, httpHeaders, methodName, respKo.toJSON(null), Status.NOT_FOUND.getStatusCode());
			}catch(Exception e1) {
				log.error("Errore durante il log della risposta", e1);
			}
			return Response.status(Status.NOT_FOUND).entity(respKo.toJSON(null)).build();
		} catch (Exception e) {
			log.error("Errore interno durante la ricerca della Stazione: " + e.getMessage(), e);
			FaultBean respKo = new FaultBean();
			respKo.setCategoria(CategoriaEnum.INTERNO);
			respKo.setCodice("");
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


}


