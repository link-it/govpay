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
import it.govpay.core.dao.anagrafica.RuoliDAO;
import it.govpay.core.dao.anagrafica.dto.FindRuoliDTO;
import it.govpay.core.dao.anagrafica.dto.FindRuoliDTOResponse;
import it.govpay.core.dao.anagrafica.dto.GetRuoloDTO;
import it.govpay.core.dao.anagrafica.dto.GetRuoloDTOResponse;
import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.GpThreadLocal;
import it.govpay.model.Ruolo;
import it.govpay.rs.v1.beans.ListaRuoli;
import it.govpay.rs.v1.beans.base.FaultBean;
import it.govpay.rs.v1.beans.base.FaultBean.CategoriaEnum;



public class RuoliController extends it.govpay.rs.BaseController {

     public RuoliController(String nomeServizio,Logger log) {
		super(nomeServizio,log);
     }



    public Response ruoliIdRuoloPUT(String principal, List<Ruolo> listaRuoli, UriInfo uriInfo, HttpHeaders httpHeaders , String idRuolo, java.io.InputStream is) {
        return Response.status(Status.INTERNAL_SERVER_ERROR).entity( "Not implemented" ).build();
    }



    public Response ruoliGET(String principal, List<Ruolo> listaRuoli, UriInfo uriInfo, HttpHeaders httpHeaders , Integer pagina, Integer risultatiPerPagina, Boolean abilitato) {
    	String methodName = "ruoliGET";  
		GpContext ctx = null;
		ByteArrayOutputStream baos= null;
		this.log.info("Esecuzione " + methodName + " in corso..."); 
		try{
			baos = new ByteArrayOutputStream();
			this.logRequest(uriInfo, httpHeaders, methodName, baos);
			
			ctx =  GpThreadLocal.get();
			
			// Parametri - > DTO Input

			String ordinamento = null; //TODO ordinamento
			String campi = null;//TODO aggiungere

			FindRuoliDTO listaRuoliDTO = new FindRuoliDTO(null); //TODO IAutorizzato
			
			listaRuoliDTO.setPagina(pagina);
			listaRuoliDTO.setLimit(risultatiPerPagina);
			listaRuoliDTO.setOrderBy(ordinamento);
			listaRuoliDTO.setAbilitato(abilitato);
			
			// INIT DAO
			
			RuoliDAO dominiDAO = new RuoliDAO(BasicBD.newInstance(ctx.getTransactionId()));
			
			// CHIAMATA AL DAO
			
			FindRuoliDTOResponse listaRuoliDTOResponse = dominiDAO.findRuoli(listaRuoliDTO);
			
			// CONVERT TO JSON DELLA RISPOSTA
			
			List<it.govpay.rs.v1.beans.Ruolo> results = new ArrayList<it.govpay.rs.v1.beans.Ruolo>();
			for(it.govpay.model.Ruolo dominio: listaRuoliDTOResponse.getResults()) {
				results.add(new it.govpay.rs.v1.beans.Ruolo(dominio));
			}
			
			ListaRuoli response = new ListaRuoli(results, uriInfo.getRequestUri(),
					listaRuoliDTOResponse.getTotalResults(), pagina, risultatiPerPagina);
			
			this.logResponse(uriInfo, httpHeaders, methodName, response.toJSON(campi), 200);
			this.log.info("Esecuzione " + methodName + " completata."); 
			return Response.status(Status.OK).entity(response.toJSON(campi)).build();
			
		}catch (Exception e) {
			log.error("Errore interno durante la ricerca dei Ruoli: " + e.getMessage(), e);
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



    public Response ruoliIdRuoloGET(String principal, List<Ruolo> listaRuoli, UriInfo uriInfo, HttpHeaders httpHeaders , String idRuolo) {
    	String methodName = "ruoliIdRuoloGET";  
		GpContext ctx = null;
		ByteArrayOutputStream baos= null;
		this.log.info("Esecuzione " + methodName + " in corso..."); 
		try{
			baos = new ByteArrayOutputStream();
			this.logRequest(uriInfo, httpHeaders, methodName, baos);
			
			ctx =  GpThreadLocal.get();
			
			// Parametri - > DTO Input

			GetRuoloDTO getRuoloDTO = new GetRuoloDTO(null, idRuolo); //TODO IAutorizzato
			
			// INIT DAO
			
			RuoliDAO dominiDAO = new RuoliDAO(BasicBD.newInstance(ctx.getTransactionId()));
			
			// CHIAMATA AL DAO
			
			GetRuoloDTOResponse getRuoloDTOResponse = dominiDAO.getRuolo(getRuoloDTO);
			
			// CONVERT TO JSON DELLA RISPOSTA
			
			it.govpay.rs.v1.beans.Ruolo response = new it.govpay.rs.v1.beans.Ruolo(getRuoloDTOResponse.getRuolo());
			
			this.logResponse(uriInfo, httpHeaders, methodName, response.toJSON(null), 200);
			this.log.info("Esecuzione " + methodName + " completata."); 
			return Response.status(Status.OK).entity(response.toJSON(null)).build();
			
		}catch (Exception e) {
			log.error("Errore interno durante la ricerca dei Ruoli: " + e.getMessage(), e);
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


