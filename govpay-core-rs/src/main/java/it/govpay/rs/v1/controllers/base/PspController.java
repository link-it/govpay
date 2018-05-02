package it.govpay.rs.v1.controllers.base;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.slf4j.Logger;

import it.govpay.core.dao.anagrafica.PspDAO;
import it.govpay.core.dao.anagrafica.dto.LeggiCanaleDTO;
import it.govpay.core.dao.anagrafica.dto.LeggiCanaleDTOResponse;
import it.govpay.core.dao.anagrafica.dto.LeggiPspDTO;
import it.govpay.core.dao.anagrafica.dto.LeggiPspDTOResponse;
import it.govpay.core.dao.anagrafica.dto.ListaCanaliDTO;
import it.govpay.core.dao.anagrafica.dto.ListaCanaliDTOResponse;
import it.govpay.core.dao.anagrafica.dto.ListaPspDTO;
import it.govpay.core.dao.anagrafica.dto.ListaPspDTOResponse;
import it.govpay.core.rs.v1.beans.ListaCanali;
import it.govpay.core.rs.v1.beans.ListaPsp;
import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.GpThreadLocal;
import it.govpay.model.Canale.TipoVersamento;
import it.govpay.model.IAutorizzato;
import it.govpay.rs.v1.beans.converter.PspConverter;



public class PspController extends it.govpay.rs.BaseController {

     public PspController(String nomeServizio,Logger log) {
		super(nomeServizio,log);
     }

    public Response pspIdPspCanaliGET(IAutorizzato user, UriInfo uriInfo, HttpHeaders httpHeaders , String idPsp, Integer pagina, Integer risultatiPerPagina, String ordinamento, String campi, Boolean abilitato, String modello, String tipoVersamento) {
    	String methodName = "pspIdPspCanaliGET";  
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
			
			ListaCanaliDTO listaCanaliDTO = new ListaCanaliDTO(user);
			
			listaCanaliDTO.setPagina(pagina);
			listaCanaliDTO.setLimit(risultatiPerPagina);
			listaCanaliDTO.setOrderBy(ordinamento);
			listaCanaliDTO.setAbilitato(abilitato);
			listaCanaliDTO.setModello(modello);
			listaCanaliDTO.setTipoVersamento(tipoVersamento);
			listaCanaliDTO.setIdPsp(idPsp);
			
			// INIT DAO
			
			PspDAO pspDAO = new PspDAO();
			
			// CHIAMATA AL DAO
			
			ListaCanaliDTOResponse listaDTOResponse = pspDAO.listaCanali(listaCanaliDTO);
			
			// CONVERT TO JSON DELLA RISPOSTA
			
			List<it.govpay.core.rs.v1.beans.Canale> results = new ArrayList<it.govpay.core.rs.v1.beans.Canale>();
			for(LeggiCanaleDTOResponse elem: listaDTOResponse.getResults()) {
				results.add(PspConverter.toCanaleRsModel(elem.getCanale(), elem.getPsp()));
			}
			
			ListaCanali response = new ListaCanali(results, this.getServicePath(uriInfo),
					listaDTOResponse.getTotalResults(), pagina, risultatiPerPagina);
			
			this.logResponse(uriInfo, httpHeaders, methodName, response.toJSON(campi), 200);
			this.log.info("Esecuzione " + methodName + " completata."); 
			return this.handleResponseOk(Response.status(Status.OK).entity(response.toJSON(campi)),transactionId).build();
			
		}catch (Exception e) {
			return handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			if(ctx != null) ctx.log();
		}

    }

    public Response pspIdPspCanaliIdCanaleTipoVersamentoGET(IAutorizzato user, UriInfo uriInfo, HttpHeaders httpHeaders , String idPsp, String idCanale, String tipoVersamento) {
    	String methodName = "pspIdPspCanaliIdCanaleGET";  
		GpContext ctx = null;
		String transactionId = null;
		ByteArrayOutputStream baos = null;
		this.log.info("Esecuzione " + methodName + " in corso..."); 
			
		try{
			baos = new ByteArrayOutputStream();
			this.logRequest(uriInfo, httpHeaders, methodName, baos);
			
			ctx =  GpThreadLocal.get();
			transactionId = ctx.getTransactionId();
			
			LeggiCanaleDTO leggiPspDTO = new LeggiCanaleDTO(user);
			leggiPspDTO.setIdPsp(idPsp);
			leggiPspDTO.setIdCanale(idCanale);
			leggiPspDTO.setTipoVersamento(TipoVersamento.toEnum(tipoVersamento));
			
			PspDAO pspDAO = new PspDAO(); 
			
			LeggiCanaleDTOResponse leggiPspDTOResponse = pspDAO.leggiCanale(leggiPspDTO);
			
			it.govpay.core.rs.v1.beans.Canale response = PspConverter.toCanaleRsModel(leggiPspDTOResponse.getCanale(), leggiPspDTOResponse.getPsp());
			return this.handleResponseOk(Response.status(Status.OK).entity(response.toJSON(null)),transactionId).build();
			
		}catch (Exception e) {
			return handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			if(ctx != null) ctx.log();
		}
    }



    public Response pspIdPspGET(IAutorizzato user, UriInfo uriInfo, HttpHeaders httpHeaders , String idPsp) {
    	String methodName = "pspIdPspGET";  
		GpContext ctx = null;
		String transactionId = null;
		ByteArrayOutputStream baos= null;
		this.log.info("Esecuzione " + methodName + " in corso..."); 
			
		try{
			baos = new ByteArrayOutputStream();
			this.logRequest(uriInfo, httpHeaders, methodName, baos);
			
			ctx =  GpThreadLocal.get();
			transactionId = ctx.getTransactionId();
			
			LeggiPspDTO leggiPspDTO = new LeggiPspDTO(user);
			leggiPspDTO.setIdPsp(idPsp);
			
			PspDAO pspDAO = new PspDAO(); 
			
			LeggiPspDTOResponse leggiPspDTOResponse = pspDAO.leggiPsp(leggiPspDTO);
			
			it.govpay.core.rs.v1.beans.Psp response = PspConverter.toRsModel(leggiPspDTOResponse.getPsp());
			return this.handleResponseOk(Response.status(Status.OK).entity(response.toJSON(null)),transactionId).build();
			
		}catch (Exception e) {
			return handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			if(ctx != null) ctx.log();
		}
    }



    public Response pspGET(IAutorizzato user, UriInfo uriInfo, HttpHeaders httpHeaders , Integer pagina, Integer risultatiPerPagina, String ordinamento, String campi, Boolean abilitato, Boolean bollo, Boolean storno) {
    	String methodName = "pspGET";  
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
			
			ListaPspDTO listaPspDTO = new ListaPspDTO(user);
			
			listaPspDTO.setPagina(pagina);
			listaPspDTO.setLimit(risultatiPerPagina);
			listaPspDTO.setOrderBy(ordinamento);
			listaPspDTO.setAbilitato(abilitato);
			listaPspDTO.setBollo(bollo);
			listaPspDTO.setStorno(storno);
			
			// INIT DAO
			
			PspDAO pspDAO = new PspDAO();
			
			// CHIAMATA AL DAO
			
			ListaPspDTOResponse listaPspDTOResponse = pspDAO.listaPsp(listaPspDTO);
			
			// CONVERT TO JSON DELLA RISPOSTA
			
			List<it.govpay.core.rs.v1.beans.Psp> results = new ArrayList<it.govpay.core.rs.v1.beans.Psp>();
			for(it.govpay.bd.model.Psp psp: listaPspDTOResponse.getResults()) {
				results.add(PspConverter.toRsModel(psp));
			}
			
			ListaPsp response = new ListaPsp(results, this.getServicePath(uriInfo),
					listaPspDTOResponse.getTotalResults(), pagina, risultatiPerPagina);
			
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


