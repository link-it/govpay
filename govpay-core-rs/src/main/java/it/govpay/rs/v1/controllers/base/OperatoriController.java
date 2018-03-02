package it.govpay.rs.v1.controllers.base;

import java.io.ByteArrayOutputStream;
import java.util.stream.Collectors;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.slf4j.Logger;

import it.govpay.core.dao.anagrafica.dto.FindOperatoriDTO;
import it.govpay.core.dao.anagrafica.dto.FindOperatoriDTOResponse;
import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.GpThreadLocal;
import it.govpay.model.IAutorizzato;
import it.govpay.rs.v1.beans.FaultBean;
import it.govpay.rs.v1.beans.ListaTipiEntrata;
import it.govpay.rs.v1.beans.Tipoentrata;
import it.govpay.rs.v1.beans.base.FaultBean.CategoriaEnum;



public class OperatoriController extends it.govpay.rs.BaseController {

     public OperatoriController(String nomeServizio,Logger log) {
		super(nomeServizio,log);
     }



    public Response operatoriPrincipalPUT(IAutorizzato user, UriInfo uriInfo, HttpHeaders httpHeaders , String principal, java.io.InputStream is) {
        return Response.status(Status.INTERNAL_SERVER_ERROR).entity( "Not implemented" ).build();
    }



    public Response operatoriPrincipalDELETE(IAutorizzato user, UriInfo uriInfo, HttpHeaders httpHeaders , String principal) {
        return Response.status(Status.INTERNAL_SERVER_ERROR).entity( "Not implemented" ).build();
    }



    public Response operatoriPrincipalGET(IAutorizzato user, UriInfo uriInfo, HttpHeaders httpHeaders , String principal) {
        return Response.status(Status.INTERNAL_SERVER_ERROR).entity( "Not implemented" ).build();
    }



    public Response operatoriGET(IAutorizzato user, UriInfo uriInfo, HttpHeaders httpHeaders , Integer pagina, Integer risultatiPerPagina, String ordinamento, String campi, Boolean abilitato) {
        return Response.status(Status.INTERNAL_SERVER_ERROR).entity( "Not implemented" ).build();
//    	String methodName = "operatoriGET";  
//		GpContext ctx = null;
//		ByteArrayOutputStream baos= null;
//		this.log.info("Esecuzione " + methodName + " in corso..."); 
//		try{
//			baos = new ByteArrayOutputStream();
//			this.logRequest(uriInfo, httpHeaders, methodName, baos);
//			
//			ctx =  GpThreadLocal.get();
//			
//			// Parametri - > DTO Input
//			
//			FindOperatoriDTO findOperatoriDTO = new FindOperatoriDTO(user);
//			findOperatoriDTO.setOrderBy(ordinamento);
//			
//			
//			// INIT DAO
//			
//			OperatoriDAO operatoriDAO = new OperatoriDAO();
//			
//			// CHIAMATA AL DAO
//			
//			FindOperatoriDTOResponse findOperatoriDTOResponse = operatoriDAO.findOperatori(findOperatoriDTO);
//			
//			// CONVERT TO JSON DELLA RISPOSTA
//			
//			ListaOperatori response = new ListaOperatori(findOperatoriDTOResponse.getResults().stream().map(t -> new Tipoentrata(t)).collect(Collectors.toList()), 
//					uriInfo.getRequestUri(), findOperatoriDTOResponse.getTotalResults(), pagina, risultatiPerPagina);
//			
//			this.logResponse(uriInfo, httpHeaders, methodName, response.toJSON(campi), 200);
//			this.log.info("Esecuzione " + methodName + " completata."); 
//			return Response.status(Status.OK).entity(response.toJSON(campi)).build();
//			
//		}catch (Exception e) {
//			log.error("Errore interno durante la ricerca degli Operatori: " + e.getMessage(), e);
//			FaultBean respKo = new FaultBean();
//			respKo.setCategoria(CategoriaEnum.INTERNO);
//			respKo.setCodice("");
//			respKo.setDescrizione(e.getMessage());
//			try {
//				this.logResponse(uriInfo, httpHeaders, methodName, respKo, 500);
//			}catch(Exception e1) {
//				log.error("Errore durante il log della risposta", e1);
//			}
//			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(respKo).build();
//		} finally {
//			if(ctx != null) ctx.log();
//		}
    }


}


