package it.govpay.rs.v1.controllers.base;

import java.io.ByteArrayOutputStream;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.slf4j.Logger;

import it.govpay.core.dao.anagrafica.DominiDAO;
import it.govpay.core.dao.anagrafica.dto.GetEntrataDTO;
import it.govpay.core.dao.anagrafica.dto.GetEntrataDTOResponse;
import it.govpay.core.dao.anagrafica.dto.GetTributoDTO;
import it.govpay.core.dao.anagrafica.dto.GetTributoDTOResponse;
import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.GpThreadLocal;
import it.govpay.model.IAutorizzato;
import it.govpay.rs.v1.beans.FaultBean;
import it.govpay.rs.v1.beans.Tipoentrata;
import it.govpay.rs.v1.beans.base.FaultBean.CategoriaEnum;



public class EntrateController extends it.govpay.rs.BaseController {

     public EntrateController(String nomeServizio,Logger log) {
		super(nomeServizio,log);
     }



    public Response entrateIdEntrataPUT(IAutorizzato user, UriInfo uriInfo, HttpHeaders httpHeaders , String idEntrata, java.io.InputStream is) {
        return Response.status(Status.INTERNAL_SERVER_ERROR).entity( "Not implemented" ).build();
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
			
			GetEntrataDTO getDominioEntrataDTO = new GetEntrataDTO(user, idEntrata);
			
			// INIT DAO
			
			EntrateDAO dominiDAO = new EntrateDAO();
			
			// CHIAMATA AL DAO
			
			GetEntrataDTOResponse listaDominiEntrateDTOResponse = dominiDAO.getTributo(getDominioEntrataDTO);
			
			// CONVERT TO JSON DELLA RISPOSTA
			
			Tipoentrata response = new it.govpay.rs.v1.beans.Tipoentrata(listaDominiEntrateDTOResponse.getTipoTributo());
			
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
			
			GetTributoDTO getDominioEntrataDTO = new GetTributoDTO(user, idDominio, idEntrata);
			
			// INIT DAO
			
			DominiDAO dominiDAO = new DominiDAO();
			
			// CHIAMATA AL DAO
			
			GetTributoDTOResponse listaDominiEntrateDTOResponse = dominiDAO.getTributo(getDominioEntrataDTO);
			
			// CONVERT TO JSON DELLA RISPOSTA
			
			Tipoentrata response = new Tipoentrata(listaDominiEntrateDTOResponse.getTributo());
			
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


