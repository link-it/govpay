package it.govpay.rs.v1.controllers.base;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.slf4j.Logger;

import it.govpay.core.dao.pagamenti.RuoliDAO;
import it.govpay.core.dao.pagamenti.dto.LeggiRuoloDTO;
import it.govpay.core.dao.pagamenti.dto.LeggiRuoloDTOResponse;
import it.govpay.core.dao.pagamenti.dto.ListaRuoliDTO;
import it.govpay.core.dao.pagamenti.dto.ListaRuoliDTOResponse;
import it.govpay.core.rs.v1.beans.base.AclPost;
import it.govpay.core.rs.v1.beans.base.ListaAcl;
import it.govpay.core.rs.v1.beans.base.ListaRuoli;
import it.govpay.core.rs.v1.beans.base.Ruolo;
import it.govpay.core.utils.GovpayConfig;
import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.GpThreadLocal;
import it.govpay.model.Acl;
import it.govpay.model.IAutorizzato;
import it.govpay.rs.BaseController;
import it.govpay.rs.v1.beans.converter.AclConverter;
import it.govpay.rs.v1.beans.converter.RuoliConverter;



public class RuoliController extends BaseController {

     public RuoliController(String nomeServizio,Logger log) {
 		super(nomeServizio,log, GovpayConfig.GOVPAY_PAGAMENTI_OPEN_API_FILE_NAME);
     }

    public Response ruoliGET(IAutorizzato user, UriInfo uriInfo, HttpHeaders httpHeaders , Integer pagina, Integer risultatiPerPagina) {
		String methodName = "ruoliGET";  
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

			ListaRuoliDTO listaRptDTO = new ListaRuoliDTO(user);
			listaRptDTO.setPagina(pagina);
			listaRptDTO.setLimit(risultatiPerPagina);

			// INIT DAO

			RuoliDAO rptDAO = new RuoliDAO();

			// CHIAMATA AL DAO

			ListaRuoliDTOResponse listaRptDTOResponse = rptDAO.listaRuoli(listaRptDTO);

			// CONVERT TO JSON DELLA RISPOSTA
			List<Ruolo> results = new ArrayList<Ruolo>();
			for(String leggiRuoloDtoResponse: listaRptDTOResponse.getResults()) {
				results.add(RuoliConverter.toRsModel(leggiRuoloDtoResponse));
			}
			ListaRuoli response = new ListaRuoli(results, this.getServicePath(uriInfo), listaRptDTOResponse.getTotalResults(), pagina, risultatiPerPagina);

			this.logResponse(uriInfo, httpHeaders, methodName, response.toJSON(null), 200);
			this.log.info("Esecuzione " + methodName + " completata."); 
			return this.handleResponseOk(Response.status(Status.OK).entity(response.toJSON(null)),transactionId).build();
		}catch (Exception e) {
			return handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			if(ctx != null) ctx.log();
		}
    }



    public Response ruoliIdRuoloGET(IAutorizzato user, UriInfo uriInfo, HttpHeaders httpHeaders , String idRuolo) {
		String methodName = "ruoliIdRuoloGET";  
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

			LeggiRuoloDTO leggiRuoloDTO = new LeggiRuoloDTO(user);

			
			// INIT DAO

			RuoliDAO rptDAO = new RuoliDAO();

			// CHIAMATA AL DAO

			LeggiRuoloDTOResponse listaRptDTOResponse = rptDAO.leggiRuoli(leggiRuoloDTO);

			// CONVERT TO JSON DELLA RISPOSTA
			List<AclPost> results = new ArrayList<AclPost>();
			for(Acl leggiRuoloDtoResponse: listaRptDTOResponse.getResults()) {
				results.add(AclConverter.toRsModel(leggiRuoloDtoResponse));
			}
			ListaAcl response = new ListaAcl(results, this.getServicePath(uriInfo), listaRptDTOResponse.getTotalResults(), 1, results.size());

			this.logResponse(uriInfo, httpHeaders, methodName, response.toJSON(null), 200);
			this.log.info("Esecuzione " + methodName + " completata."); 
			return this.handleResponseOk(Response.status(Status.OK).entity(response.toJSON(null)),transactionId).build();
		}catch (Exception e) {
			return handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			if(ctx != null) ctx.log();
		}
    }



    public Response ruoliIdRuoloPUT(IAutorizzato user, UriInfo uriInfo, HttpHeaders httpHeaders , String idRuolo, java.io.InputStream is) {
        return Response.status(Status.INTERNAL_SERVER_ERROR).entity( "Not implemented" ).build();
    }


}


