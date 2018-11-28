package it.govpay.backoffice.v1.controllers;

import java.io.ByteArrayOutputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.springframework.security.core.Authentication;

import eu.medsea.mimeutil.MimeUtil;
import it.govpay.backoffice.v1.beans.ContiAccredito;
import it.govpay.backoffice.v1.beans.ContiAccreditoPost;
import it.govpay.backoffice.v1.beans.DominioPost;
import it.govpay.backoffice.v1.beans.Entrata;
import it.govpay.backoffice.v1.beans.EntrataPost;
import it.govpay.backoffice.v1.beans.ListaContiAccredito;
import it.govpay.backoffice.v1.beans.ListaDomini;
import it.govpay.backoffice.v1.beans.ListaEntrate;
import it.govpay.backoffice.v1.beans.ListaUnitaOperative;
import it.govpay.backoffice.v1.beans.UnitaOperativa;
import it.govpay.backoffice.v1.beans.UnitaOperativaPost;
import it.govpay.backoffice.v1.beans.converter.DominiConverter;
import it.govpay.core.beans.JSONSerializable;
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
import it.govpay.core.utils.GovpayConfig;
import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.GpThreadLocal;

public class DominiController extends BaseController {

     public DominiController(String nomeServizio,Logger log) {
		super(nomeServizio,log, GovpayConfig.GOVPAY_BACKOFFICE_OPEN_API_FILE_NAME);
     }



    public Response dominiIdDominioContiAccreditoIbanAccreditoGET(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , String idDominio, String ibanAccredito) {
    	String methodName = "dominiIdDominioIbanAccreditoIbanAccreditoGET";  
		GpContext ctx = null;
		String transactionId = null;
		ByteArrayOutputStream baos= null;
		this.log.info(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 
		try{
			baos = new ByteArrayOutputStream();
			this.logRequest(uriInfo, httpHeaders, methodName, baos);
			
			ctx =  GpThreadLocal.get();
			transactionId = ctx.getTransactionId();
			
			// Parametri - > DTO Input
			
			GetIbanDTO getIbanDTO = new GetIbanDTO(user, idDominio, ibanAccredito);
			
			// INIT DAO
			
			DominiDAO dominiDAO = new DominiDAO(false);
			
			// CHIAMATA AL DAO
			
			GetIbanDTOResponse getDominiIbanDTOResponse = dominiDAO.getIban(getIbanDTO);
			
			// CONVERT TO JSON DELLA RISPOSTA
			
			ContiAccredito response = DominiConverter.toIbanRsModel(getDominiIbanDTOResponse.getIbanAccredito());
			
			this.logResponse(uriInfo, httpHeaders, methodName, response.toJSON(null), 200);
			this.log.info(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
			return this.handleResponseOk(Response.status(Status.OK).entity(response.toJSON(null)),transactionId).build();
			
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			if(ctx != null) ctx.log();
		}
    }



    public Response dominiIdDominioUnitaOperativeIdUnitaOperativaGET(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , String idDominio, String idUnitaOperativa) {
    	String methodName = "dominiIdDominioUnitaOperativeIdUnitaOperativaGET";  
		GpContext ctx = null;
		String transactionId = null;
		ByteArrayOutputStream baos= null;
		this.log.info(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 
		try{
			baos = new ByteArrayOutputStream();
			this.logRequest(uriInfo, httpHeaders, methodName, baos);
			
			ctx =  GpThreadLocal.get();
			transactionId = ctx.getTransactionId();
			
			// Parametri - > DTO Input
			
			GetUnitaOperativaDTO getDominioUoDTO = new GetUnitaOperativaDTO(user, idDominio, idUnitaOperativa);
			
			// INIT DAO
			
			DominiDAO dominiDAO = new DominiDAO(false);
			
			// CHIAMATA AL DAO
			
			GetUnitaOperativaDTOResponse listaDominiUoDTOResponse = dominiDAO.getUnitaOperativa(getDominioUoDTO);
			
			// CONVERT TO JSON DELLA RISPOSTA
			
			UnitaOperativa response = DominiConverter.toUnitaOperativaRsModel(listaDominiUoDTOResponse.getUnitaOperativa());
			
			this.logResponse(uriInfo, httpHeaders, methodName, response.toJSON(null), 200);
			this.log.info(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
			return this.handleResponseOk(Response.status(Status.OK).entity(response.toJSON(null)),transactionId).build();
			
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			if(ctx != null) ctx.log();
		}
    }



    public Response dominiGET(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , Integer pagina, Integer risultatiPerPagina, String ordinamento, String campi, Boolean abilitato, String idStazione) {
    	String methodName = "dominiGET";  
		GpContext ctx = null;
		String transactionId = null;
		ByteArrayOutputStream baos= null;
		this.log.info(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 
		try{
			baos = new ByteArrayOutputStream();
			this.logRequest(uriInfo, httpHeaders, methodName, baos);
			
			ctx =  GpThreadLocal.get();
			transactionId = ctx.getTransactionId();
			
			// Parametri - > DTO Input
			
			FindDominiDTO listaDominiDTO = new FindDominiDTO(user);
			
			listaDominiDTO.setPagina(pagina);
			listaDominiDTO.setLimit(risultatiPerPagina);
			listaDominiDTO.setOrderBy(ordinamento);
			listaDominiDTO.setAbilitato(abilitato);
			listaDominiDTO.setCodStazione(idStazione);
			
			// INIT DAO
			
			DominiDAO dominiDAO = new DominiDAO(false);
			
			// CHIAMATA AL DAO
			
			FindDominiDTOResponse listaDominiDTOResponse = dominiDAO.findDomini(listaDominiDTO);
			
			// CONVERT TO JSON DELLA RISPOSTA
			
			List<it.govpay.backoffice.v1.beans.DominioIndex> results = new ArrayList<>();
			for(it.govpay.bd.model.Dominio dominio: listaDominiDTOResponse.getResults()) {
				results.add(DominiConverter.toRsModelIndex(dominio));
			}
			
			ListaDomini response = new ListaDomini(results, this.getServicePath(uriInfo),
					listaDominiDTOResponse.getTotalResults(), pagina, risultatiPerPagina);
			
			this.logResponse(uriInfo, httpHeaders, methodName, response.toJSON(campi), 200);
			this.log.info(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
			return this.handleResponseOk(Response.status(Status.OK).entity(response.toJSON(campi)),transactionId).build();
			
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			if(ctx != null) ctx.log();
		}

    }

    public Response dominiIdDominioLogoGET(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders, String idDominio) {
    	String methodName = "getLogo";  
		GpContext ctx = null;
		String transactionId = null;
		ByteArrayOutputStream baos= null;
		this.log.info(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 
		try{
			baos = new ByteArrayOutputStream();
			this.logRequest(uriInfo, httpHeaders, methodName, baos);
			
			ctx =  GpThreadLocal.get();
			transactionId = ctx.getTransactionId();
			
			// Parametri - > DTO Input
			
			GetDominioDTO getDominioDTO = new GetDominioDTO(user, idDominio);

			// INIT DAO
			
			DominiDAO dominiDAO = new DominiDAO(false);
			
			// CHIAMATA AL DAO
			
			byte[] logo = dominiDAO.getLogo(getDominioDTO);
			
			MimeUtil.registerMimeDetector(eu.medsea.mimeutil.detector.MagicMimeMimeDetector.class.getName());
			
			Collection<?> mimeTypes = MimeUtil.getMimeTypes(logo);
			
			String mimeType = MimeUtil.getFirstMimeType(mimeTypes.toString()).toString();
			
			this.logResponse(uriInfo, httpHeaders, methodName, logo, 200);
			this.log.info(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
			ResponseBuilder entity = Response.status(Status.OK).entity(logo);
			entity.header("CacheControl", "max-age: "+ GovpayConfig.getInstance().getCacheLogo().intValue());
			entity.header("Content-Type", mimeType);
			return this.handleResponseOk(entity,transactionId).build();
			
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			if(ctx != null) ctx.log();
		}
    }

    public Response dominiIdDominioGET(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , String idDominio) {
    	String methodName = "dominiIdDominioGET";  
		GpContext ctx = null;
		String transactionId = null;
		ByteArrayOutputStream baos= null;
		this.log.info(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 
		try{
			baos = new ByteArrayOutputStream();
			this.logRequest(uriInfo, httpHeaders, methodName, baos);
			
			ctx =  GpThreadLocal.get();
			transactionId = ctx.getTransactionId();
			
			// Parametri - > DTO Input
			
			GetDominioDTO getDominioDTO = new GetDominioDTO(user, idDominio);

			// INIT DAO
			
			DominiDAO dominiDAO = new DominiDAO(false);
			
			// CHIAMATA AL DAO
			
			GetDominioDTOResponse listaDominiDTOResponse = dominiDAO.getDominio(getDominioDTO);
			
			// CONVERT TO JSON DELLA RISPOSTA
			
			it.govpay.backoffice.v1.beans.Dominio response = DominiConverter.toRsModel(listaDominiDTOResponse.getDominio(), listaDominiDTOResponse.getUo(), listaDominiDTOResponse.getTributi(), listaDominiDTOResponse.getIban());
			
			this.logResponse(uriInfo, httpHeaders, methodName, response.toJSON(null), 200);
			this.log.info(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
			return this.handleResponseOk(Response.status(Status.OK).entity(response.toJSON(null)),transactionId).build();
			
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			if(ctx != null) ctx.log();
		}
    }

    public Response dominiIdDominioEntrateGET(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , String idDominio, Integer pagina, Integer risultatiPerPagina, String ordinamento, String campi, Boolean abilitato) {
    	String methodName = "dominiIdDominioEntrateGET";  
		GpContext ctx = null;
		String transactionId = null;
		ByteArrayOutputStream baos= null;
		this.log.info(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 
		try{
			baos = new ByteArrayOutputStream();
			this.logRequest(uriInfo, httpHeaders, methodName, baos);
			
			ctx =  GpThreadLocal.get();
			transactionId = ctx.getTransactionId();
			
			// Parametri - > DTO Input
			
			FindTributiDTO listaDominiEntrateDTO = new FindTributiDTO(user, idDominio);
			
			listaDominiEntrateDTO.setPagina(pagina);
			listaDominiEntrateDTO.setLimit(risultatiPerPagina);
			listaDominiEntrateDTO.setOrderBy(ordinamento);
			listaDominiEntrateDTO.setAbilitato(abilitato);
			
			// INIT DAO
			
			DominiDAO dominiDAO = new DominiDAO(false);
			
			// CHIAMATA AL DAO
			
			FindTributiDTOResponse listaDominiEntrateDTOResponse = dominiDAO.findTributi(listaDominiEntrateDTO);
			
			// CONVERT TO JSON DELLA RISPOSTA
			
			List<it.govpay.backoffice.v1.beans.Entrata> results = new ArrayList<>();
			for(GetTributoDTOResponse tributo: listaDominiEntrateDTOResponse.getResults()) {
				results.add(DominiConverter.toEntrataRsModel(tributo.getTributo(), tributo.getIbanAppoggio()));
			}
			
			ListaEntrate response = new ListaEntrate(results, this.getServicePath(uriInfo),
					listaDominiEntrateDTOResponse.getTotalResults(), pagina, risultatiPerPagina);
			
			this.logResponse(uriInfo, httpHeaders, methodName, response.toJSON(campi), 200);
			this.log.info(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
			return this.handleResponseOk(Response.status(Status.OK).entity(response.toJSON(campi)),transactionId).build();
			
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			if(ctx != null) ctx.log();
		}
    }



    public Response dominiIdDominioUnitaOperativeGET(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , String idDominio, Integer pagina, Integer risultatiPerPagina, String ordinamento, String campi, Boolean abilitato) {    	
    	String methodName = "dominiIdDominioUnitaOperativeGET";  
		GpContext ctx = null;
		String transactionId = null;
		ByteArrayOutputStream baos= null;
		this.log.info(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 
		try{
			baos = new ByteArrayOutputStream();
			this.logRequest(uriInfo, httpHeaders, methodName, baos);
			
			ctx =  GpThreadLocal.get();
			transactionId = ctx.getTransactionId();
			
			// Parametri - > DTO Input
			
			FindUnitaOperativeDTO listaDominiUoDTO = new FindUnitaOperativeDTO(user, idDominio);
			
			listaDominiUoDTO.setPagina(pagina);
			listaDominiUoDTO.setLimit(risultatiPerPagina);
			listaDominiUoDTO.setOrderBy(ordinamento);
			listaDominiUoDTO.setAbilitato(abilitato);
			
			// INIT DAO
			
			DominiDAO dominiDAO = new DominiDAO(false);
			
			// CHIAMATA AL DAO
			
			FindUnitaOperativeDTOResponse listaDominiUoDTOResponse = dominiDAO.findUnitaOperative(listaDominiUoDTO);
			
			// CONVERT TO JSON DELLA RISPOSTA
			
			List<it.govpay.backoffice.v1.beans.UnitaOperativa> results = new ArrayList<>();
			for(it.govpay.bd.model.UnitaOperativa uo: listaDominiUoDTOResponse.getResults()) {
				results.add(DominiConverter.toUnitaOperativaRsModel(uo));
			}
			
			ListaUnitaOperative response = new ListaUnitaOperative(results, this.getServicePath(uriInfo),
					listaDominiUoDTOResponse.getTotalResults(), pagina, risultatiPerPagina);
			
			this.logResponse(uriInfo, httpHeaders, methodName, response.toJSON(campi), 200);
			this.log.info(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
			return this.handleResponseOk(Response.status(Status.OK).entity(response.toJSON(campi)),transactionId).build();
			
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			if(ctx != null) ctx.log();
		}
    }



    public Response dominiIdDominioEntrateIdEntrataGET(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , String idDominio, String idEntrata) {
    	String methodName = "dominiIdDominioEntrateIdEntrataGET";  
		GpContext ctx = null;
		String transactionId = null;
		ByteArrayOutputStream baos= null;
		this.log.info(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 
		try{
			baos = new ByteArrayOutputStream();
			this.logRequest(uriInfo, httpHeaders, methodName, baos);
			
			ctx =  GpThreadLocal.get();
			transactionId = ctx.getTransactionId();
			
			// Parametri - > DTO Input
			
			GetTributoDTO getDominioEntrataDTO = new GetTributoDTO(user, idDominio, idEntrata);
			
			// INIT DAO
			
			DominiDAO dominiDAO = new DominiDAO(false);
			
			// CHIAMATA AL DAO
			
			GetTributoDTOResponse listaDominiEntrateDTOResponse = dominiDAO.getTributo(getDominioEntrataDTO);
			
			// CONVERT TO JSON DELLA RISPOSTA
			
			Entrata response = DominiConverter.toEntrataRsModel(listaDominiEntrateDTOResponse.getTributo(), listaDominiEntrateDTOResponse.getIbanAppoggio());
			
			this.logResponse(uriInfo, httpHeaders, methodName, response.toJSON(null), 200);
			this.log.info(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
			return this.handleResponseOk(Response.status(Status.OK).entity(response.toJSON(null)),transactionId).build();
			
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			if(ctx != null) ctx.log();
		}
    }



    public Response dominiIdDominioContiAccreditoIbanAccreditoPUT(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , String idDominio, String ibanAccredito, java.io.InputStream is) {
    	String methodName = "dominiIdDominioContiAccreditoIbanAccreditoPUT";  
		GpContext ctx = null;
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
			
			String jsonRequest = baos.toString();
			ContiAccreditoPost ibanAccreditoRequest= JSONSerializable.parse(jsonRequest, ContiAccreditoPost.class);
			
			PutIbanAccreditoDTO putibanAccreditoDTO = DominiConverter.getPutIbanAccreditoDTO(ibanAccreditoRequest, idDominio, ibanAccredito, user);
			
			DominiDAO dominiDAO = new DominiDAO(false);
			
			PutIbanAccreditoDTOResponse putIbanAccreditoDTOResponse = dominiDAO.createOrUpdateIbanAccredito(putibanAccreditoDTO);
			
			Status responseStatus = putIbanAccreditoDTOResponse.isCreated() ?  Status.CREATED : Status.OK;
			
			this.logResponse(uriInfo, httpHeaders, methodName, new byte[0], responseStatus.getStatusCode());
			this.log.info(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
			return this.handleResponseOk(Response.status(responseStatus),transactionId).build();
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			if(ctx != null) ctx.log();
		}
    }



    public Response dominiIdDominioUnitaOperativeIdUnitaOperativaPUT(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , String idDominio, String idUnitaOperativa, java.io.InputStream is) {
    	String methodName = "dominiIdDominioUnitaOperativeIdUnitaOperativaPUT";  
		GpContext ctx = null;
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
			
			String jsonRequest = baos.toString();
			UnitaOperativaPost unitaOperativaRequest= JSONSerializable.parse(jsonRequest, UnitaOperativaPost.class);
			
			PutUnitaOperativaDTO putUnitaOperativaDTO = DominiConverter.getPutUnitaOperativaDTO(unitaOperativaRequest, idDominio, idUnitaOperativa, user);
			
			DominiDAO dominiDAO = new DominiDAO(false);
			
			PutUnitaOperativaDTOResponse putUnitaOperativaDTOResponse = dominiDAO.createOrUpdateUnitaOperativa(putUnitaOperativaDTO);
			
			Status responseStatus = putUnitaOperativaDTOResponse.isCreated() ?  Status.CREATED : Status.OK;
			
			this.logResponse(uriInfo, httpHeaders, methodName, new byte[0], responseStatus.getStatusCode());
			this.log.info(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
			return this.handleResponseOk(Response.status(responseStatus),transactionId).build();
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			if(ctx != null) ctx.log();
		}
    }



    public Response dominiIdDominioEntrateIdEntrataPUT(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , String idDominio, String idEntrata, java.io.InputStream is) {
    	String methodName = "dominiIdDominioEntrateIdEntrataPUT";  
		GpContext ctx = null;
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
			
			String jsonRequest = baos.toString();
			EntrataPost entrataRequest= JSONSerializable.parse(jsonRequest, EntrataPost.class);
			
			PutEntrataDominioDTO putEntrataDTO = DominiConverter.getPutEntrataDominioDTO(entrataRequest, idDominio, idEntrata, user); 
			
			DominiDAO dominiDAO = new DominiDAO(false);
			
			PutEntrataDominioDTOResponse putEntrataDTOResponse = dominiDAO.createOrUpdateEntrataDominio(putEntrataDTO);
			
			Status responseStatus = putEntrataDTOResponse.isCreated() ?  Status.CREATED : Status.OK;
			
			this.logResponse(uriInfo, httpHeaders, methodName, new byte[0], responseStatus.getStatusCode());
			this.log.info(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
			return this.handleResponseOk(Response.status(responseStatus),transactionId).build();
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			if(ctx != null) ctx.log();
		}
    }



    public Response dominiIdDominioPUT(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , String idDominio, java.io.InputStream is) {
    	String methodName = "dominiIdDominioPUT";  
		GpContext ctx = null;
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
			
			String jsonRequest = baos.toString();
			DominioPost dominioRequest= JSONSerializable.parse(jsonRequest, DominioPost.class);
			dominioRequest.validate();
			
			PutDominioDTO putDominioDTO = DominiConverter.getPutDominioDTO(dominioRequest, idDominio, user); 
			
			DominiDAO dominiDAO = new DominiDAO();
			
			PutDominioDTOResponse putDominioDTOResponse = dominiDAO.createOrUpdate(putDominioDTO);
			
			Status responseStatus = putDominioDTOResponse.isCreated() ?  Status.CREATED : Status.OK;
			
			this.logResponse(uriInfo, httpHeaders, methodName, new byte[0], responseStatus.getStatusCode());
			this.log.info(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
			return this.handleResponseOk(Response.status(responseStatus),transactionId).build();
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			if(ctx != null) ctx.log();
		}
    }



    public Response dominiIdDominioContiAccreditoGET(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , String idDominio, Integer pagina, Integer risultatiPerPagina, String ordinamento, String campi, Boolean abilitato) {
    	String methodName = "dominiIdDominioIbanAccreditoGET";  
		GpContext ctx = null;
		String transactionId = null;
		ByteArrayOutputStream baos= null;
		this.log.info(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 
		try{
			baos = new ByteArrayOutputStream();
			this.logRequest(uriInfo, httpHeaders, methodName, baos);
			
			ctx =  GpThreadLocal.get();
			transactionId = ctx.getTransactionId();
			
			// Parametri - > DTO Input
			
			FindIbanDTO listaDominiIbanDTO = new FindIbanDTO(user, idDominio);
			
			listaDominiIbanDTO.setPagina(pagina);
			listaDominiIbanDTO.setLimit(risultatiPerPagina);
			listaDominiIbanDTO.setOrderBy(ordinamento);
			listaDominiIbanDTO.setAbilitato(abilitato);
			
			// INIT DAO
			
			DominiDAO dominiDAO = new DominiDAO(false);
			
			// CHIAMATA AL DAO
			
			FindIbanDTOResponse listaDominiIbanDTOResponse = dominiDAO.findIban(listaDominiIbanDTO);
			
			// CONVERT TO JSON DELLA RISPOSTA
			
			List<it.govpay.backoffice.v1.beans.ContiAccredito> results = new ArrayList<>();
			for(it.govpay.bd.model.IbanAccredito ibanAccredito: listaDominiIbanDTOResponse.getResults()) {
				results.add(DominiConverter.toIbanRsModel(ibanAccredito));
			}
			
			ListaContiAccredito response = new ListaContiAccredito(results, this.getServicePath(uriInfo),
					listaDominiIbanDTOResponse.getTotalResults(), pagina, risultatiPerPagina);
			
			this.logResponse(uriInfo, httpHeaders, methodName, response.toJSON(campi), 200);
			this.log.info(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
			return this.handleResponseOk(Response.status(Status.OK).entity(response.toJSON(campi)),transactionId).build();
			
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			if(ctx != null) ctx.log();
		}
    }

    public Response getLogo(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders, String idDominio) {
    	String methodName = "getLogo";  
		GpContext ctx = null;
		String transactionId = null;
		ByteArrayOutputStream baos= null;
		this.log.info(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 
		try{
			baos = new ByteArrayOutputStream();
			this.logRequest(uriInfo, httpHeaders, methodName, baos);
			
			ctx =  GpThreadLocal.get();
			transactionId = ctx.getTransactionId();
			
			// Parametri - > DTO Input
			
			GetDominioDTO getDominioDTO = new GetDominioDTO(user, idDominio);

			// INIT DAO
			
			DominiDAO dominiDAO = new DominiDAO(false);
			
			// CHIAMATA AL DAO
			
			byte[] logo = dominiDAO.getLogo(getDominioDTO);
			
			MimeUtil.registerMimeDetector(eu.medsea.mimeutil.detector.MagicMimeMimeDetector.class.getName());
			
			Collection<?> mimeTypes = MimeUtil.getMimeTypes(logo);
			
			String mimeType = MimeUtil.getFirstMimeType(mimeTypes.toString()).toString();

			this.logResponse(uriInfo, httpHeaders, methodName, logo, 200);
			this.log.info(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
			ResponseBuilder entity = Response.status(Status.OK).entity(logo);
			entity.header("CacheControl", "max-age: "+ GovpayConfig.getInstance().getCacheLogo().intValue());
			entity.header("Content-Type", mimeType);
			return this.handleResponseOk(entity,transactionId).build();
			
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			if(ctx != null) ctx.log();
		}
    }


}


