package it.govpay.rs.v1.controllers.base;

import java.io.ByteArrayOutputStream;
import java.util.List;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.slf4j.Logger;

//import it.govpay.core.dao.anagrafica.dto.FindIntermediariDTO;
//import it.govpay.core.dao.anagrafica.dto.FindIntermediariDTOResponse;
import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.GpThreadLocal;
import it.govpay.model.Ruolo;
import it.govpay.rs.v1.beans.ListaDomini;
//import it.govpay.rs.v1.beans.ListaIntermediari;
import it.govpay.rs.v1.beans.base.FaultBean;
import it.govpay.rs.v1.beans.base.FaultBean.CategoriaEnum;



public class IntermediariController extends it.govpay.rs.BaseController {

     public IntermediariController(String nomeServizio,Logger log) {
		super(nomeServizio,log);
     }



    public Response intermediariIdIntermediarioGET(String principal, List<Ruolo> listaRuoli, UriInfo uriInfo, HttpHeaders httpHeaders , String idIntermediario) {
        return Response.status(Status.INTERNAL_SERVER_ERROR).entity( "Not implemented" ).build();
    }



    public Response intermediariIdIntermediarioStazioniIdStazionePUT(String principal, List<Ruolo> listaRuoli, UriInfo uriInfo, HttpHeaders httpHeaders , String idIntermediario, String idStazione, java.io.InputStream is) {
        return Response.status(Status.INTERNAL_SERVER_ERROR).entity( "Not implemented" ).build();
    }



    public Response intermediariGET(String principal, List<Ruolo> listaRuoli, UriInfo uriInfo, HttpHeaders httpHeaders , Integer pagina, Integer risultatiPerPagina, String ordinamento, String campi, Boolean abilitato) {
    	String methodName = "intermediariGET";  
		GpContext ctx = null;
		ByteArrayOutputStream baos= null;
		this.log.info("Esecuzione " + methodName + " in corso..."); 
		try{
			baos = new ByteArrayOutputStream();
			this.logRequest(uriInfo, httpHeaders, methodName, baos);
			
			ctx =  GpThreadLocal.get();
			
			// Parametri - > DTO Input
			
//			FindIntermediariDTO listaIntermediariDTO = new FindIntermediariDTO(null); //TODO IAutorizzato
//			
//			listaIntermediariDTO.setPagina(pagina);
//			listaIntermediariDTO.setLimit(risultatiPerPagina);
//			listaIntermediariDTO.setOrderBy(ordinamento);
//			listaIntermediariDTO.setAbilitato(abilitato);
//			listaIntermediariDTO.setCodStazione(idStazione);
//			
//			// INIT DAO
//			
//			IntermediariDAO intermediariDAO = new IntermediariDAO();
//			
//			// CHIAMATA AL DAO
//			
//			FindIntermediariDTOResponse listaIntermediariDTOResponse = intermediariDAO.findIntermediari(listaIntermediariDTO);
//			
//			// CONVERT TO JSON DELLA RISPOSTA
//			
//			List<it.govpay.rs.v1.beans.Intermediario> results = new ArrayList<it.govpay.rs.v1.beans.Intermediario>();
//			for(it.govpay.bd.model.Intermediario intermediario: listaIntermediariDTOResponse.getResults()) {
//				results.add(new it.govpay.rs.v1.beans.Intermediario(intermediario));
//			}
//			
			ListaDomini response = null;// new ListaIntermediari(results, uriInfo.getRequestUri(),
//					listaIntermediariDTOResponse.getTotalResults(), pagina, risultatiPerPagina);
			
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


    public Response intermediariIdIntermediarioPUT(String principal, List<Ruolo> listaRuoli, UriInfo uriInfo, HttpHeaders httpHeaders , String idIntermediario, java.io.InputStream is) {
        return Response.status(Status.INTERNAL_SERVER_ERROR).entity( "Not implemented" ).build();
    }



    public Response intermediariIdIntermediarioStazioniGET(String principal, List<Ruolo> listaRuoli, UriInfo uriInfo, HttpHeaders httpHeaders , Integer pagina, Integer risultatiPerPagina, String ordinamento, String campi, Boolean abilitato) {
        return Response.status(Status.INTERNAL_SERVER_ERROR).entity( "Not implemented" ).build();
    }



    public Response intermediariIdIntermediarioStazioniIdStazioneGET(String principal, List<Ruolo> listaRuoli, UriInfo uriInfo, HttpHeaders httpHeaders , String idIntermediario, String idStazione) {
        return Response.status(Status.INTERNAL_SERVER_ERROR).entity( "Not implemented" ).build();
    }


}


