package it.govpay.rs.v1.controllers.base;

import java.io.ByteArrayOutputStream;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.slf4j.Logger;

import it.govpay.core.dao.anagrafica.dto.GetAvvisoDTO;
import it.govpay.core.dao.anagrafica.dto.GetAvvisoDTOResponse;
import it.govpay.core.dao.pagamenti.AvvisiDAO;
import it.govpay.core.dao.pagamenti.exception.AvvisoNonTrovatoException;
import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.GpThreadLocal;
import it.govpay.model.IAutorizzato;
import it.govpay.rs.v1.beans.base.Avviso;
import it.govpay.rs.v1.beans.base.FaultBean;
import it.govpay.rs.v1.beans.base.FaultBean.CategoriaEnum;



public class AvvisiController extends it.govpay.rs.BaseController {

     public AvvisiController(String nomeServizio,Logger log) {
		super(nomeServizio,log);
     }



    public Response avvisiIdDominioIuvGET(IAutorizzato user, UriInfo uriInfo, HttpHeaders httpHeaders , String idDominio, String iuv) {
        return Response.status(Status.INTERNAL_SERVER_ERROR).entity( "Not implemented" ).build();
//    	String methodName = "dominiIdDominioAvvisoAccreditoAvvisoAccreditoGET";  
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
//			GetAvvisoDTO getAvvisoDTO = new GetAvvisoDTO(user, idDominio, iuv);
//			
//			// INIT DAO
//			
//			AvvisiDAO dominiDAO = new AvvisiDAO();
//			
//			// CHIAMATA AL DAO
//			
//			GetAvvisoDTOResponse getAvvisoDTOResponse = dominiDAO.getAvviso(getAvvisoDTO);
//			
//			// CONVERT TO JSON DELLA RISPOSTA
//			
//			Avviso response = null;//new it.govpay.rs.v1.beans.Avviso(getAvvisoDTOResponse.getAvviso());
//			
//			this.logResponse(uriInfo, httpHeaders, methodName, response.toJSON(null), 200);
//			this.log.info("Esecuzione " + methodName + " completata."); 
//			return Response.status(Status.OK).entity(response.toJSON(null)).build();
//			
//		}catch (AvvisoNonTrovatoException e) {
//			log.error(e.getMessage(), e);
//			FaultBean respKo = new FaultBean();
//			respKo.setCategoria(CategoriaEnum.OPERAZIONE);
//			respKo.setCodice("");
//			respKo.setDescrizione(e.getMessage());
//			try {
//				this.logResponse(uriInfo, httpHeaders, methodName, respKo, 500);
//			}catch(Exception e1) {
//				log.error("Errore durante il log della risposta", e1);
//			}
//			return Response.status(Status.NOT_FOUND).entity(respKo).build();
//		} catch (Exception e) {
//			log.error("Errore interno durante la ricerca degli avvisi: " + e.getMessage(), e);
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



    public Response avvisiIdDominioPOST(IAutorizzato user, UriInfo uriInfo, HttpHeaders httpHeaders , String idDominio, java.io.InputStream is) {
        return Response.status(Status.INTERNAL_SERVER_ERROR).entity( "Not implemented" ).build();
    }


}


