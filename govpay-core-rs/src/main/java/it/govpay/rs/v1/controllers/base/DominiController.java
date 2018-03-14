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

import it.govpay.core.dao.anagrafica.DominiDAO;
import it.govpay.core.dao.anagrafica.dto.FindDominiDTO;
import it.govpay.core.dao.anagrafica.dto.FindDominiDTOResponse;
import it.govpay.core.dao.anagrafica.dto.FindIbanDTO;
import it.govpay.core.dao.anagrafica.dto.FindIbanDTOResponse;
import it.govpay.core.dao.anagrafica.dto.FindTributiDTO;
import it.govpay.core.dao.anagrafica.dto.FindTributiDTOResponse;
import it.govpay.core.dao.anagrafica.dto.FindUnitaOperativeDTO;
import it.govpay.core.dao.anagrafica.dto.FindUnitaOperativeDTOResponse;
import it.govpay.core.dao.anagrafica.dto.GetDominioDTO;
import it.govpay.core.dao.anagrafica.dto.GetDominioDTOResponse;
import it.govpay.core.dao.anagrafica.dto.GetIbanDTO;
import it.govpay.core.dao.anagrafica.dto.GetIbanDTOResponse;
import it.govpay.core.dao.anagrafica.dto.GetTributoDTO;
import it.govpay.core.dao.anagrafica.dto.GetTributoDTOResponse;
import it.govpay.core.dao.anagrafica.dto.GetUnitaOperativaDTO;
import it.govpay.core.dao.anagrafica.dto.GetUnitaOperativaDTOResponse;
import it.govpay.core.dao.anagrafica.dto.PutDominioDTO;
import it.govpay.core.dao.anagrafica.dto.PutDominioDTOResponse;
import it.govpay.core.dao.anagrafica.dto.PutEntrataDominioDTO;
import it.govpay.core.dao.anagrafica.dto.PutEntrataDominioDTOResponse;
import it.govpay.core.dao.anagrafica.dto.PutIbanAccreditoDTO;
import it.govpay.core.dao.anagrafica.dto.PutIbanAccreditoDTOResponse;
import it.govpay.core.dao.anagrafica.dto.PutUnitaOperativaDTO;
import it.govpay.core.dao.anagrafica.dto.PutUnitaOperativaDTOResponse;
import it.govpay.core.dao.anagrafica.exception.DominioNonTrovatoException;
import it.govpay.core.dao.anagrafica.exception.IbanAccreditoNonTrovatoException;
import it.govpay.core.dao.anagrafica.exception.StazioneNonTrovataException;
import it.govpay.core.dao.anagrafica.exception.TipoTributoNonTrovatoException;
import it.govpay.core.dao.anagrafica.exception.TributoNonTrovatoException;
import it.govpay.core.dao.anagrafica.exception.UnitaOperativaNonTrovataException;
import it.govpay.core.rs.v1.beans.Entrata;
import it.govpay.core.rs.v1.beans.Iban;
import it.govpay.core.rs.v1.beans.ListaDomini;
import it.govpay.core.rs.v1.beans.ListaEntrate;
import it.govpay.core.rs.v1.beans.ListaIbanAccredito;
import it.govpay.core.rs.v1.beans.ListaUnitaOperative;
import it.govpay.core.rs.v1.beans.UnitaOperativa;
import it.govpay.core.rs.v1.beans.base.DominioPost;
import it.govpay.core.rs.v1.beans.base.EntrataPost;
import it.govpay.core.rs.v1.beans.base.FaultBean;
import it.govpay.core.rs.v1.beans.base.IbanAccreditoPost;
import it.govpay.core.rs.v1.beans.base.UnitaOperativaPost;
import it.govpay.core.rs.v1.beans.base.FaultBean.CategoriaEnum;
import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.GpThreadLocal;
import it.govpay.model.IAutorizzato;
import it.govpay.rs.BaseRsService;
import it.govpay.rs.v1.beans.converter.DominiConverter;
import net.sf.json.JsonConfig;



public class DominiController extends it.govpay.rs.BaseController {

     public DominiController(String nomeServizio,Logger log) {
		super(nomeServizio,log);
     }



    public Response dominiIdDominioIbanAccreditoIbanAccreditoGET(IAutorizzato user, UriInfo uriInfo, HttpHeaders httpHeaders , String idDominio, String ibanAccredito) {
    	String methodName = "dominiIdDominioIbanAccreditoIbanAccreditoGET";  
		GpContext ctx = null;
		ByteArrayOutputStream baos= null;
		this.log.info("Esecuzione " + methodName + " in corso..."); 
		try{
			baos = new ByteArrayOutputStream();
			this.logRequest(uriInfo, httpHeaders, methodName, baos);
			
			ctx =  GpThreadLocal.get();
			
			// Parametri - > DTO Input
			
			GetIbanDTO getIbanDTO = new GetIbanDTO(user, idDominio, ibanAccredito);
			
			// INIT DAO
			
			DominiDAO dominiDAO = new DominiDAO();
			
			// CHIAMATA AL DAO
			
			GetIbanDTOResponse getDominiIbanDTOResponse = dominiDAO.getIban(getIbanDTO);
			
			// CONVERT TO JSON DELLA RISPOSTA
			
			Iban response = DominiConverter.toIbanRsModel(getDominiIbanDTOResponse.getIbanAccredito());
			
			this.logResponse(uriInfo, httpHeaders, methodName, response.toJSON(null), 200);
			this.log.info("Esecuzione " + methodName + " completata."); 
			return Response.status(Status.OK).entity(response.toJSON(null)).build();
			
		}catch (DominioNonTrovatoException  | IbanAccreditoNonTrovatoException e) {
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
			log.error("Errore interno durante la ricerca dei Domini/IbanAccredito: " + e.getMessage(), e);
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



    public Response dominiIdDominioUnitaOperativeIdUnitaOperativaGET(IAutorizzato user, UriInfo uriInfo, HttpHeaders httpHeaders , String idDominio, String idUnitaOperativa) {
    	String methodName = "dominiIdDominioUnitaOperativeIdUnitaOperativaGET";  
		GpContext ctx = null;
		ByteArrayOutputStream baos= null;
		this.log.info("Esecuzione " + methodName + " in corso..."); 
		try{
			baos = new ByteArrayOutputStream();
			this.logRequest(uriInfo, httpHeaders, methodName, baos);
			
			ctx =  GpThreadLocal.get();
			
			// Parametri - > DTO Input
			
			GetUnitaOperativaDTO getDominioUoDTO = new GetUnitaOperativaDTO(user, idDominio, idUnitaOperativa);
			
			// INIT DAO
			
			DominiDAO dominiDAO = new DominiDAO();
			
			// CHIAMATA AL DAO
			
			GetUnitaOperativaDTOResponse listaDominiUoDTOResponse = dominiDAO.getUnitaOperativa(getDominioUoDTO);
			
			// CONVERT TO JSON DELLA RISPOSTA
			
			UnitaOperativa response = DominiConverter.toUnitaOperativaRsModel(listaDominiUoDTOResponse.getUnitaOperativa());
			
			this.logResponse(uriInfo, httpHeaders, methodName, response.toJSON(null), 200);
			this.log.info("Esecuzione " + methodName + " completata."); 
			return Response.status(Status.OK).entity(response.toJSON(null)).build();
			
		}catch (DominioNonTrovatoException  | UnitaOperativaNonTrovataException e) {
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
			log.error("Errore interno durante la ricerca dei PSP: " + e.getMessage(), e);
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



    public Response dominiGET(IAutorizzato user, UriInfo uriInfo, HttpHeaders httpHeaders , Integer pagina, Integer risultatiPerPagina, String ordinamento, String campi, Boolean abilitato, String idStazione) {
    	String methodName = "dominiGET";  
		GpContext ctx = null;
		ByteArrayOutputStream baos= null;
		this.log.info("Esecuzione " + methodName + " in corso..."); 
		try{
			baos = new ByteArrayOutputStream();
			this.logRequest(uriInfo, httpHeaders, methodName, baos);
			
			ctx =  GpThreadLocal.get();
			
			// Parametri - > DTO Input
			
			FindDominiDTO listaDominiDTO = new FindDominiDTO(user);
			
			listaDominiDTO.setPagina(pagina);
			listaDominiDTO.setLimit(risultatiPerPagina);
			listaDominiDTO.setOrderBy(ordinamento);
			listaDominiDTO.setAbilitato(abilitato);
			listaDominiDTO.setCodStazione(idStazione);
			
			// INIT DAO
			
			DominiDAO dominiDAO = new DominiDAO();
			
			// CHIAMATA AL DAO
			
			FindDominiDTOResponse listaDominiDTOResponse = dominiDAO.findDomini(listaDominiDTO);
			
			// CONVERT TO JSON DELLA RISPOSTA
			
			List<it.govpay.core.rs.v1.beans.Dominio> results = new ArrayList<it.govpay.core.rs.v1.beans.Dominio>();
			for(it.govpay.bd.model.Dominio dominio: listaDominiDTOResponse.getResults()) {
				results.add(DominiConverter.toRsModel(dominio));
			}
			
			ListaDomini response = new ListaDomini(results, this.getServicePath(uriInfo),
					listaDominiDTOResponse.getTotalResults(), pagina, risultatiPerPagina);
			
			this.logResponse(uriInfo, httpHeaders, methodName, response.toJSON(campi), 200);
			this.log.info("Esecuzione " + methodName + " completata."); 
			return Response.status(Status.OK).entity(response.toJSON(campi)).build();
			
		}catch (Exception e) {
			log.error("Errore interno durante la ricerca dei PSP: " + e.getMessage(), e);
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



    public Response dominiIdDominioGET(IAutorizzato user, UriInfo uriInfo, HttpHeaders httpHeaders , String idDominio) {
    	String methodName = "dominiIdDominioGET";  
		GpContext ctx = null;
		ByteArrayOutputStream baos= null;
		this.log.info("Esecuzione " + methodName + " in corso..."); 
		try{
			baos = new ByteArrayOutputStream();
			this.logRequest(uriInfo, httpHeaders, methodName, baos);
			
			ctx =  GpThreadLocal.get();
			
			// Parametri - > DTO Input
			
			GetDominioDTO getDominioDTO = new GetDominioDTO(user, idDominio);

			// INIT DAO
			
			DominiDAO dominiDAO = new DominiDAO();
			
			// CHIAMATA AL DAO
			
			GetDominioDTOResponse listaDominiDTOResponse = dominiDAO.getDominio(getDominioDTO);
			
			// CONVERT TO JSON DELLA RISPOSTA
			
			it.govpay.core.rs.v1.beans.Dominio response = DominiConverter.toRsModel(listaDominiDTOResponse.getDominio());
			
			this.logResponse(uriInfo, httpHeaders, methodName, response.toJSON(null), 200);
			this.log.info("Esecuzione " + methodName + " completata."); 
			return Response.status(Status.OK).entity(response.toJSON(null)).build();
			
		}catch (DominioNonTrovatoException  e) {
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
		}catch (Exception e) {
			log.error("Errore interno durante la ricerca dei PSP: " + e.getMessage(), e);
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



    public Response dominiIdDominioEntrateGET(IAutorizzato user, UriInfo uriInfo, HttpHeaders httpHeaders , String idDominio, Integer pagina, Integer risultatiPerPagina, String ordinamento, String campi, Boolean abilitato) {
    	String methodName = "dominiIdDominioEntrateGET";  
		GpContext ctx = null;
		ByteArrayOutputStream baos= null;
		this.log.info("Esecuzione " + methodName + " in corso..."); 
		try{
			baos = new ByteArrayOutputStream();
			this.logRequest(uriInfo, httpHeaders, methodName, baos);
			
			ctx =  GpThreadLocal.get();
			
			// Parametri - > DTO Input
			
			FindTributiDTO listaDominiEntrateDTO = new FindTributiDTO(user, idDominio);
			
			listaDominiEntrateDTO.setPagina(pagina);
			listaDominiEntrateDTO.setLimit(risultatiPerPagina);
			listaDominiEntrateDTO.setOrderBy(ordinamento);
			listaDominiEntrateDTO.setAbilitato(abilitato);
			
			// INIT DAO
			
			DominiDAO dominiDAO = new DominiDAO();
			
			// CHIAMATA AL DAO
			
			FindTributiDTOResponse listaDominiEntrateDTOResponse = dominiDAO.findTributi(listaDominiEntrateDTO);
			
			// CONVERT TO JSON DELLA RISPOSTA
			
			List<it.govpay.core.rs.v1.beans.Entrata> results = new ArrayList<it.govpay.core.rs.v1.beans.Entrata>();
			for(GetTributoDTOResponse tributo: listaDominiEntrateDTOResponse.getResults()) {
				results.add(DominiConverter.toEntrataRsModel(tributo.getTributo(), tributo.getIbanAccreditoPostale()));
			}
			
			ListaEntrate response = new ListaEntrate(results, this.getServicePath(uriInfo),
					listaDominiEntrateDTOResponse.getTotalResults(), pagina, risultatiPerPagina);
			
			this.logResponse(uriInfo, httpHeaders, methodName, response.toJSON(campi), 200);
			this.log.info("Esecuzione " + methodName + " completata."); 
			return Response.status(Status.OK).entity(response.toJSON(campi)).build();
			
		}catch (DominioNonTrovatoException   e) {
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
		}catch (Exception e) {
			log.error("Errore interno durante la ricerca dei PSP: " + e.getMessage(), e);
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



    public Response dominiIdDominioUnitaOperativeGET(IAutorizzato user, UriInfo uriInfo, HttpHeaders httpHeaders , String idDominio, Integer pagina, Integer risultatiPerPagina, String ordinamento, String campi, Boolean abilitato) {    	
    	String methodName = "dominiIdDominioUnitaOperativeGET";  
		GpContext ctx = null;
		ByteArrayOutputStream baos= null;
		this.log.info("Esecuzione " + methodName + " in corso..."); 
		try{
			baos = new ByteArrayOutputStream();
			this.logRequest(uriInfo, httpHeaders, methodName, baos);
			
			ctx =  GpThreadLocal.get();
			
			// Parametri - > DTO Input
			
			FindUnitaOperativeDTO listaDominiUoDTO = new FindUnitaOperativeDTO(user, idDominio);
			
			listaDominiUoDTO.setPagina(pagina);
			listaDominiUoDTO.setLimit(risultatiPerPagina);
			listaDominiUoDTO.setOrderBy(ordinamento);
			listaDominiUoDTO.setAbilitato(abilitato);
			
			// INIT DAO
			
			DominiDAO dominiDAO = new DominiDAO();
			
			// CHIAMATA AL DAO
			
			FindUnitaOperativeDTOResponse listaDominiUoDTOResponse = dominiDAO.findUnitaOperative(listaDominiUoDTO);
			
			// CONVERT TO JSON DELLA RISPOSTA
			
			List<it.govpay.core.rs.v1.beans.UnitaOperativa> results = new ArrayList<it.govpay.core.rs.v1.beans.UnitaOperativa>();
			for(it.govpay.bd.model.UnitaOperativa uo: listaDominiUoDTOResponse.getResults()) {
				results.add(DominiConverter.toUnitaOperativaRsModel(uo));
			}
			
			ListaUnitaOperative response = new ListaUnitaOperative(results, this.getServicePath(uriInfo),
					listaDominiUoDTOResponse.getTotalResults(), pagina, risultatiPerPagina);
			
			this.logResponse(uriInfo, httpHeaders, methodName, response.toJSON(campi), 200);
			this.log.info("Esecuzione " + methodName + " completata."); 
			return Response.status(Status.OK).entity(response.toJSON(campi)).build();
			
		}catch (DominioNonTrovatoException e) {
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
			log.error("Errore interno durante la ricerca dei PSP: " + e.getMessage(), e);
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



    public Response dominiIdDominioEntrateIdEntrataGET(IAutorizzato user, UriInfo uriInfo, HttpHeaders httpHeaders , String idDominio, String idEntrata) {
    	String methodName = "dominiIdDominioEntrateIdEntrataGET";  
		GpContext ctx = null;
		ByteArrayOutputStream baos= null;
		this.log.info("Esecuzione " + methodName + " in corso..."); 
		try{
			baos = new ByteArrayOutputStream();
			this.logRequest(uriInfo, httpHeaders, methodName, baos);
			
			ctx =  GpThreadLocal.get();
			
			// Parametri - > DTO Input
			
			GetTributoDTO getDominioEntrataDTO = new GetTributoDTO(user, idDominio, idEntrata);
			
			// INIT DAO
			
			DominiDAO dominiDAO = new DominiDAO();
			
			// CHIAMATA AL DAO
			
			GetTributoDTOResponse listaDominiEntrateDTOResponse = dominiDAO.getTributo(getDominioEntrataDTO);
			
			// CONVERT TO JSON DELLA RISPOSTA
			
			Entrata response = DominiConverter.toEntrataRsModel(listaDominiEntrateDTOResponse.getTributo(), listaDominiEntrateDTOResponse.getIbanAccreditoPostale());
			
			this.logResponse(uriInfo, httpHeaders, methodName, response.toJSON(null), 200);
			this.log.info("Esecuzione " + methodName + " completata."); 
			return Response.status(Status.OK).entity(response.toJSON(null)).build();
			
		}catch (DominioNonTrovatoException  | TributoNonTrovatoException e) {
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
			log.error("Errore interno durante la ricerca dei PSP: " + e.getMessage(), e);
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



    public Response dominiIdDominioIbanAccreditoIbanAccreditoPUT(IAutorizzato user, UriInfo uriInfo, HttpHeaders httpHeaders , String idDominio, String ibanAccredito, java.io.InputStream is) {
    	String methodName = "dominiIdDominioUnitaOperativeIdUnitaOperativaPUT";  
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
			IbanAccreditoPost ibanAccreditoRequest= (IbanAccreditoPost) IbanAccreditoPost.parse(jsonRequest, IbanAccreditoPost.class, jsonConfig);
			
			PutIbanAccreditoDTO putibanAccreditoDTO = DominiConverter.getPutIbanAccreditoDTO(ibanAccreditoRequest, idDominio, ibanAccredito, user);
			
			DominiDAO dominiDAO = new DominiDAO();
			
			PutIbanAccreditoDTOResponse putIbanAccreditoDTOResponse = dominiDAO.createOrUpdateIbanAccredito(putibanAccreditoDTO);
			
			Status responseStatus = putIbanAccreditoDTOResponse.isCreated() ?  Status.CREATED : Status.OK;
			
			this.logResponse(uriInfo, httpHeaders, methodName, new byte[0], responseStatus.getStatusCode());
			this.log.info("Esecuzione " + methodName + " completata."); 
			return Response.status(responseStatus).build();
		} catch (DominioNonTrovatoException  | IbanAccreditoNonTrovatoException e) {
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



    public Response dominiIdDominioUnitaOperativeIdUnitaOperativaPUT(IAutorizzato user, UriInfo uriInfo, HttpHeaders httpHeaders , String idDominio, String idUnitaOperativa, java.io.InputStream is) {
    	String methodName = "dominiIdDominioUnitaOperativeIdUnitaOperativaPUT";  
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
			UnitaOperativaPost unitaOperativaRequest= (UnitaOperativaPost) UnitaOperativaPost.parse(jsonRequest, UnitaOperativaPost.class, jsonConfig);
			
			PutUnitaOperativaDTO putUnitaOperativaDTO = DominiConverter.getPutUnitaOperativaDTO(unitaOperativaRequest, idDominio, idUnitaOperativa, user);
			
			DominiDAO dominiDAO = new DominiDAO();
			
			PutUnitaOperativaDTOResponse putUnitaOperativaDTOResponse = dominiDAO.createOrUpdateUnitaOperativa(putUnitaOperativaDTO);
			
			Status responseStatus = putUnitaOperativaDTOResponse.isCreated() ?  Status.CREATED : Status.OK;
			
			this.logResponse(uriInfo, httpHeaders, methodName, new byte[0], responseStatus.getStatusCode());
			this.log.info("Esecuzione " + methodName + " completata."); 
			return Response.status(responseStatus).build();
		} catch (DominioNonTrovatoException  | UnitaOperativaNonTrovataException e) {
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



    public Response dominiIdDominioEntrateIdEntrataPUT(IAutorizzato user, UriInfo uriInfo, HttpHeaders httpHeaders , String idDominio, String idEntrata, java.io.InputStream is) {
    	String methodName = "dominiIdDominioEntrateIdEntrataPUT";  
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
			EntrataPost entrataRequest= (EntrataPost) EntrataPost.parse(jsonRequest, EntrataPost.class, jsonConfig);
			
			PutEntrataDominioDTO putEntrataDTO = DominiConverter.getPutEntrataDominioDTO(entrataRequest, idDominio, idEntrata, user); 
			
			DominiDAO dominiDAO = new DominiDAO();
			
			PutEntrataDominioDTOResponse putEntrataDTOResponse = dominiDAO.createOrUpdateEntrataDominio(putEntrataDTO);
			
			Status responseStatus = putEntrataDTOResponse.isCreated() ?  Status.CREATED : Status.OK;
			
			this.logResponse(uriInfo, httpHeaders, methodName, new byte[0], responseStatus.getStatusCode());
			this.log.info("Esecuzione " + methodName + " completata."); 
			return Response.status(responseStatus).build();
		} catch (DominioNonTrovatoException  | TipoTributoNonTrovatoException | TributoNonTrovatoException | IbanAccreditoNonTrovatoException e) {
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



    public Response dominiIdDominioPUT(IAutorizzato user, UriInfo uriInfo, HttpHeaders httpHeaders , String idDominio, java.io.InputStream is) {
    	String methodName = "dominiIdDominioPUT";  
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
			DominioPost dominioRequest= (DominioPost) DominioPost.parse(jsonRequest, DominioPost.class, jsonConfig);
			
			PutDominioDTO putDominioDTO = DominiConverter.getPutDominioDTO(dominioRequest, idDominio, user); 
			
			DominiDAO dominiDAO = new DominiDAO();
			
			PutDominioDTOResponse putDominioDTOResponse = dominiDAO.createOrUpdate(putDominioDTO);
			
			Status responseStatus = putDominioDTOResponse.isCreated() ?  Status.CREATED : Status.OK;
			
			this.logResponse(uriInfo, httpHeaders, methodName, new byte[0], responseStatus.getStatusCode());
			this.log.info("Esecuzione " + methodName + " completata."); 
			return Response.status(responseStatus).build();
		} catch (DominioNonTrovatoException  | StazioneNonTrovataException  | TipoTributoNonTrovatoException e) {
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



    public Response dominiIdDominioIbanAccreditoGET(IAutorizzato user, UriInfo uriInfo, HttpHeaders httpHeaders , String idDominio, Integer pagina, Integer risultatiPerPagina, String ordinamento, String campi, Boolean abilitato) {
    	String methodName = "dominiIdDominioIbanAccreditoGET";  
		GpContext ctx = null;
		ByteArrayOutputStream baos= null;
		this.log.info("Esecuzione " + methodName + " in corso..."); 
		try{
			baos = new ByteArrayOutputStream();
			this.logRequest(uriInfo, httpHeaders, methodName, baos);
			
			ctx =  GpThreadLocal.get();
			
			// Parametri - > DTO Input
			
			FindIbanDTO listaDominiIbanDTO = new FindIbanDTO(user, idDominio);
			
			listaDominiIbanDTO.setPagina(pagina);
			listaDominiIbanDTO.setLimit(risultatiPerPagina);
			listaDominiIbanDTO.setOrderBy(ordinamento);
			listaDominiIbanDTO.setAbilitato(abilitato);
			
			// INIT DAO
			
			DominiDAO dominiDAO = new DominiDAO();
			
			// CHIAMATA AL DAO
			
			FindIbanDTOResponse listaDominiIbanDTOResponse = dominiDAO.findIban(listaDominiIbanDTO);
			
			// CONVERT TO JSON DELLA RISPOSTA
			
			List<it.govpay.core.rs.v1.beans.Iban> results = new ArrayList<it.govpay.core.rs.v1.beans.Iban>();
			for(it.govpay.bd.model.IbanAccredito ibanAccredito: listaDominiIbanDTOResponse.getResults()) {
				results.add(DominiConverter.toIbanRsModel(ibanAccredito));
			}
			
			ListaIbanAccredito response = new ListaIbanAccredito(results, this.getServicePath(uriInfo),
					listaDominiIbanDTOResponse.getTotalResults(), pagina, risultatiPerPagina);
			
			this.logResponse(uriInfo, httpHeaders, methodName, response.toJSON(campi), 200);
			this.log.info("Esecuzione " + methodName + " completata."); 
			return Response.status(Status.OK).entity(response.toJSON(campi)).build();
			
		}catch (DominioNonTrovatoException e) {
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
			log.error("Errore interno durante la ricerca dei PSP: " + e.getMessage(), e);
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


