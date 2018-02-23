package it.govpay.rs.v1.controllers.base;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.slf4j.Logger;

import it.govpay.bd.BasicBD;
import it.govpay.core.dao.anagrafica.PspDAO;
import it.govpay.core.dao.anagrafica.dto.LeggiPspDTO;
import it.govpay.core.dao.anagrafica.dto.LeggiPspDTOResponse;
import it.govpay.core.dao.anagrafica.dto.ListaCanaliDTO;
import it.govpay.core.dao.anagrafica.dto.ListaCanaliDTOResponse;
import it.govpay.core.dao.anagrafica.dto.ListaPspDTO;
import it.govpay.core.dao.anagrafica.dto.ListaPspDTOResponse;
import it.govpay.core.dao.anagrafica.exception.PspNonTrovatoException;
import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.GpThreadLocal;
import it.govpay.model.Ruolo;
import it.govpay.rs.v1.beans.ListaCanali;
import it.govpay.rs.v1.beans.ListaPsp;
import it.govpay.rs.v1.beans.base.FaultBean;
import it.govpay.rs.v1.beans.base.FaultBean.CategoriaEnum;



public class PspController extends it.govpay.rs.BaseController {
	
	public PspController(String nomeServizio,Logger log) {
		super(nomeServizio,log);
	}


    public Response pspGET(String principal, List<Ruolo> listaRuoli, UriInfo uriInfo, HttpHeaders httpHeaders , Integer pagina , Integer risultatiPerPagina , String ordinamento , String campi , Boolean abilitato , Boolean bollo , Boolean storno ) {
    	String methodName = "pspGET";  
		GpContext ctx = null;
		ByteArrayOutputStream baos= null;
		this.log.info("Esecuzione " + methodName + " in corso..."); 
		try{
			baos = new ByteArrayOutputStream();
			this.logRequest(uriInfo, httpHeaders, methodName, baos);
			
			ctx =  GpThreadLocal.get();
			
			// Parametri - > DTO Input
			
			ListaPspDTO listaPspDTO = new ListaPspDTO(null); //TODO IAutorizzato
			
			listaPspDTO.setPagina(pagina);
			listaPspDTO.setLimit(risultatiPerPagina);
			listaPspDTO.setOrderBy(ordinamento);
			listaPspDTO.setAbilitato(abilitato);
			listaPspDTO.setBollo(bollo);
			listaPspDTO.setStorno(storno);
			
			// INIT DAO
			
			PspDAO pspDAO = new PspDAO(BasicBD.newInstance(ctx.getTransactionId()));
			
			// CHIAMATA AL DAO
			
			ListaPspDTOResponse listaPspDTOResponse = pspDAO.listaPsp(listaPspDTO);
			
			// CONVERT TO JSON DELLA RISPOSTA
			
			List<it.govpay.rs.v1.beans.Psp> results = new ArrayList<it.govpay.rs.v1.beans.Psp>();
			for(it.govpay.bd.model.Psp psp: listaPspDTOResponse.getResults()) {
				results.add(new it.govpay.rs.v1.beans.Psp(psp));
			}
			
			ListaPsp response = new ListaPsp(results, uriInfo.getRequestUri(),
					listaPspDTOResponse.getTotalResults(), pagina, risultatiPerPagina);
			
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
				this.logResponse(uriInfo, httpHeaders, methodName, respKo, 500);
			}catch(Exception e1) {
				log.error("Errore durante il log della risposta", e1);
			}
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(respKo).build();
		} finally {
			if(ctx != null) ctx.log();
		}
    }


/*  
    public Response pspGET(String principal, List<Ruolo> listaRuoli, UriInfo uriInfo, HttpHeaders httpHeaders , Integer pagina, Integer risultatiPerPagina, String ordinamento, String campi, Boolean abilitato, Boolean bollo, Boolean storno) {
        return Response.status(Status.INTERNAL_SERVER_ERROR).entity( "Not implemented" ).build();
    }
*/



    public Response pspIdPspCanaliGET(String principal, List<Ruolo> listaRuoli, UriInfo uriInfo, HttpHeaders httpHeaders , Integer pagina, Integer risultatiPerPagina, String ordinamento, String campi, Boolean abilitato, String modello, String tipoVersamento) {
    	String methodName = "pspGET";  
		GpContext ctx = null;
		ByteArrayOutputStream baos= null;
		this.log.info("Esecuzione " + methodName + " in corso..."); 
		try{
			baos = new ByteArrayOutputStream();
			this.logRequest(uriInfo, httpHeaders, methodName, baos);
			
			ctx =  GpThreadLocal.get();
			
			// Parametri - > DTO Input
			
			ListaCanaliDTO listaPspDTO = new ListaCanaliDTO(null); //TODO IAutorizzato
			
			listaPspDTO.setPagina(pagina);
			listaPspDTO.setLimit(risultatiPerPagina);
			listaPspDTO.setOrderBy(ordinamento);
			listaPspDTO.setAbilitato(abilitato);
//			listaPspDTO.setBollo(bollo);
//			listaPspDTO.setStorno(storno);
			
			// INIT DAO
			
			PspDAO pspDAO = new PspDAO(BasicBD.newInstance(ctx.getTransactionId()));
			
			// CHIAMATA AL DAO
			
			ListaCanaliDTOResponse listaPspDTOResponse = pspDAO.listaCanali(listaPspDTO);
			
			// CONVERT TO JSON DELLA RISPOSTA
			
			List<it.govpay.rs.v1.beans.Canale> results = new ArrayList<it.govpay.rs.v1.beans.Canale>();
			for(it.govpay.bd.model.Canale psp: listaPspDTOResponse.getResults()) {
				results.add(new it.govpay.rs.v1.beans.Canale(psp, pspDAO));
			}
			
			ListaCanali response = new ListaCanali(results, uriInfo.getRequestUri(),
					listaPspDTOResponse.getTotalResults(), pagina, risultatiPerPagina);
			
			this.logResponse(uriInfo, httpHeaders, methodName, response.toJSON(campi), 200);
			this.log.info("Esecuzione " + methodName + " completata."); 
			return Response.status(Status.OK).entity(response.toJSON(campi)).build();
			
		}catch (Exception e) {
			log.error("Errore interno durante la ricerca dei canali: " + e.getMessage(), e);
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



/*  
    public Response pspIdPspCanaliGET(String principal, List<Ruolo> listaRuoli, UriInfo uriInfo, HttpHeaders httpHeaders , Integer pagina, Integer risultatiPerPagina, String ordinamento, String campi, Boolean abilitato, ModelloPagamento modello, TipoVersamento abilitato2) {
        return Response.status(Status.INTERNAL_SERVER_ERROR).entity( "Not implemented" ).build();
    }
*/


    public Response pspIdPspCanaliIdCanaleGET(String principal, List<Ruolo> listaRuoli, UriInfo uriInfo, HttpHeaders httpHeaders , String idPsp, String idCanale) {
        return Response.status(Status.INTERNAL_SERVER_ERROR).entity( "Not implemented" ).build();
    }


/*  
    public Response pspIdPspCanaliIdCanaleGET(String principal, List<Ruolo> listaRuoli, UriInfo uriInfo, HttpHeaders httpHeaders , String idPsp, String idCanale) {
        return Response.status(Status.INTERNAL_SERVER_ERROR).entity( "Not implemented" ).build();
    }
*/


    public Response pspIdPspGET(String principal, List<Ruolo> listaRuoli, UriInfo uriInfo, HttpHeaders httpHeaders , String idPsp ) {
    	String methodName = "pspIdPspGET";  
		GpContext ctx = null;
		ByteArrayOutputStream baos= null;
		this.log.info("Esecuzione " + methodName + " in corso..."); 
			
		try{
			baos = new ByteArrayOutputStream();
			this.logRequest(uriInfo, httpHeaders, methodName, baos);
			
			ctx =  GpThreadLocal.get();
			
			LeggiPspDTO leggiPspDTO = new LeggiPspDTO(null); //TODO IAutorizzato
			leggiPspDTO.setIdPsp(idPsp);
			
			PspDAO pspDAO = new PspDAO(BasicBD.newInstance(ctx.getTransactionId())); 
			
			LeggiPspDTOResponse leggiPspDTOResponse = pspDAO.leggiPsp(leggiPspDTO);
			
			it.govpay.rs.v1.beans.Psp response = new it.govpay.rs.v1.beans.Psp(leggiPspDTOResponse.getPsp());
			return Response.status(Status.OK).entity(response.toJSON(null)).build();
			
		}catch (PspNonTrovatoException e) {
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
		}catch (Exception e) {
			log.error("Errore interno durante la " + methodName, e);
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


/*  
    public Response pspIdPspGET(String principal, List<Ruolo> listaRuoli, UriInfo uriInfo, HttpHeaders httpHeaders , String idPsp) {
        return Response.status(Status.INTERNAL_SERVER_ERROR).entity( "Not implemented" ).build();
    }
*/


}


