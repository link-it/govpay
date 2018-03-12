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
import it.govpay.core.dao.eventi.EventiDAO;
import it.govpay.core.dao.eventi.dto.ListaEventiDTO;
import it.govpay.core.dao.eventi.dto.ListaEventiDTOResponse;
import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.GpThreadLocal;
import it.govpay.model.IAutorizzato;
import it.govpay.rs.v1.beans.ListaEventi;
import it.govpay.rs.v1.beans.base.FaultBean;
import it.govpay.rs.v1.beans.base.FaultBean.CategoriaEnum;



public class EventiController extends it.govpay.rs.BaseController {

     public EventiController(String nomeServizio,Logger log) {
		super(nomeServizio,log);
     }



    public Response eventiGET(IAutorizzato user, UriInfo uriInfo, HttpHeaders httpHeaders , Integer pagina, Integer risultatiPerPagina, String idDominio, String iuv) {
    	String methodName = "eventiGET";  
		GpContext ctx = null;
		ByteArrayOutputStream baos= null;
		this.log.info("Esecuzione " + methodName + " in corso..."); 
		try{
			baos = new ByteArrayOutputStream();
			this.logRequest(uriInfo, httpHeaders, methodName, baos);
			
			ctx =  GpThreadLocal.get();
			
			// Parametri - > DTO Input
			
			ListaEventiDTO listaEventiDTO = new ListaEventiDTO(user);
			
			listaEventiDTO.setPagina(pagina);
			listaEventiDTO.setLimit(risultatiPerPagina);
			listaEventiDTO.setIdDominio(idDominio);
			listaEventiDTO.setIuv(iuv);
			
			// INIT DAO
			
			EventiDAO pspDAO = new EventiDAO(BasicBD.newInstance(ctx.getTransactionId()));
			
			// CHIAMATA AL DAO
			
			ListaEventiDTOResponse listaEventiDTOResponse = pspDAO.listaEventi(listaEventiDTO);
			
			// CONVERT TO JSON DELLA RISPOSTA
			
			List<it.govpay.rs.v1.beans.Evento> results = new ArrayList<it.govpay.rs.v1.beans.Evento>();
			for(it.govpay.model.Evento evento: listaEventiDTOResponse.getResults()) {
				results.add(new it.govpay.rs.v1.beans.Evento(evento));
			}
			
			ListaEventi response = new ListaEventi(results, uriInfo.getRequestUri(),
					listaEventiDTOResponse.getTotalResults(), pagina, risultatiPerPagina);
			
			this.logResponse(uriInfo, httpHeaders, methodName, response.toJSON(null), 200);
			this.log.info("Esecuzione " + methodName + " completata."); 
			return Response.status(Status.OK).entity(response.toJSON(null)).build();
			
		}catch (Exception e) {
			log.error("Errore interno durante la ricerca degli eventi: " + e.getMessage(), e);
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


