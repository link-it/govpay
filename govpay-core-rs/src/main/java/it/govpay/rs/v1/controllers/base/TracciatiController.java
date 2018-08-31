package it.govpay.rs.v1.controllers.base;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.slf4j.Logger;

import it.govpay.core.dao.pagamenti.TracciatiDAO;
import it.govpay.core.dao.pagamenti.dto.LeggiTracciatoDTO;
import it.govpay.core.dao.pagamenti.dto.ListaTracciatiDTO;
import it.govpay.core.dao.pagamenti.dto.ListaTracciatiDTOResponse;
import it.govpay.core.rs.v1.beans.base.ListaTracciati;
import it.govpay.core.utils.GovpayConfig;
import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.GpThreadLocal;
import it.govpay.model.IAutorizzato;
import it.govpay.bd.model.Tracciato;
import it.govpay.model.Tracciato.TIPO_TRACCIATO;
import it.govpay.rs.BaseController;
import it.govpay.rs.v1.beans.converter.TracciatiConverter;



public class TracciatiController extends BaseController {

     public TracciatiController(String nomeServizio,Logger log) {
		super(nomeServizio,log, GovpayConfig.GOVPAY_PAGAMENTI_OPEN_API_FILE_NAME);
     }



    public Response tracciatiGET(IAutorizzato user, UriInfo uriInfo, HttpHeaders httpHeaders , Integer pagina, Integer risultatiPerPagina) {
		String methodName = "tracciatiGET";  
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

			ListaTracciatiDTO listaTracciatiDTO = new ListaTracciatiDTO(user);
			listaTracciatiDTO.setPagina(pagina);
			listaTracciatiDTO.setLimit(risultatiPerPagina);
			List<TIPO_TRACCIATO> tipo = new ArrayList<>();
			tipo.add(TIPO_TRACCIATO.AV);
			tipo.add(TIPO_TRACCIATO.AV_ESITO);
			listaTracciatiDTO.setTipoTracciato(tipo); 

			// INIT DAO

			TracciatiDAO tracciatiDAO = new TracciatiDAO();

			// CHIAMATA AL DAO

			ListaTracciatiDTOResponse listaTracciatiDTOResponse = tracciatiDAO.listaTracciati(listaTracciatiDTO);

			// CONVERT TO JSON DELLA RISPOSTA
			List<it.govpay.core.rs.v1.beans.base.Tracciato> results = new ArrayList<it.govpay.core.rs.v1.beans.base.Tracciato>();
			for(Tracciato tracciato: listaTracciatiDTOResponse.getResults()) {
				results.add(TracciatiConverter.toRsModel(tracciato));
			}
			ListaTracciati response = new ListaTracciati(results, this.getServicePath(uriInfo), listaTracciatiDTOResponse.getTotalResults(), pagina, risultatiPerPagina);

			this.logResponse(uriInfo, httpHeaders, methodName, response.toJSON(null), 200);
			this.log.info("Esecuzione " + methodName + " completata."); 
			return this.handleResponseOk(Response.status(Status.OK).entity(response.toJSON(null)),transactionId).build();
		}catch (Exception e) {
			return handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			if(ctx != null) ctx.log();
		}
    }



    public Response tracciatiIdGET(IAutorizzato user, UriInfo uriInfo, HttpHeaders httpHeaders , Long id) {
		String methodName = "tracciatiIdGET";  
		GpContext ctx = null;
		String transactionId = null;
		ByteArrayOutputStream baos= null;
		this.log.info("Esecuzione " + methodName + " in corso..."); 


		try{
			baos = new ByteArrayOutputStream();
			this.logRequest(uriInfo, httpHeaders, methodName, baos);

			ctx =  GpThreadLocal.get();
			transactionId = ctx.getTransactionId();

			LeggiTracciatoDTO leggiTracciatoDTO = new LeggiTracciatoDTO(user);
			leggiTracciatoDTO.setId(id);

			TracciatiDAO tracciatiDAO = new TracciatiDAO(); 

			Tracciato tracciato = tracciatiDAO.leggiTracciato(leggiTracciatoDTO);

			it.govpay.core.rs.v1.beans.base.Tracciato rsModel = TracciatiConverter.toRsModel(tracciato);
			return this.handleResponseOk(Response.status(Status.OK).entity(rsModel.toJSON(null)),transactionId).build();
		}catch (Exception e) {
			return handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			if(ctx != null) ctx.log();
		} 
    }



    public Response tracciatiIdRichiestaGET(IAutorizzato user, UriInfo uriInfo, HttpHeaders httpHeaders , Long id) {
		String methodName = "tracciatiIdRichiestaGET";  
		GpContext ctx = null;
		String transactionId = null;
		ByteArrayOutputStream baos= null;
		this.log.info("Esecuzione " + methodName + " in corso..."); 


		try{
			baos = new ByteArrayOutputStream();
			this.logRequest(uriInfo, httpHeaders, methodName, baos);

			ctx =  GpThreadLocal.get();
			transactionId = ctx.getTransactionId();

			LeggiTracciatoDTO leggiTracciatoDTO = new LeggiTracciatoDTO(user);
			leggiTracciatoDTO.setId(id);

			TracciatiDAO tracciatiDAO = new TracciatiDAO(); 

			byte[] richiesta = tracciatiDAO.leggiRichiestaTracciato(leggiTracciatoDTO);
			return this.handleResponseOk(Response.status(Status.OK).entity(richiesta),transactionId).build();
		}catch (Exception e) {
			return handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			if(ctx != null) ctx.log();
		} 
    }



    public Response tracciatiIdRispostaGET(IAutorizzato user, UriInfo uriInfo, HttpHeaders httpHeaders , Long id) {
		String methodName = "tracciatiIdRispostaGET";  
		GpContext ctx = null;
		String transactionId = null;
		ByteArrayOutputStream baos= null;
		this.log.info("Esecuzione " + methodName + " in corso..."); 


		try{
			baos = new ByteArrayOutputStream();
			this.logRequest(uriInfo, httpHeaders, methodName, baos);

			ctx =  GpThreadLocal.get();
			transactionId = ctx.getTransactionId();

			LeggiTracciatoDTO leggiTracciatoDTO = new LeggiTracciatoDTO(user);
			leggiTracciatoDTO.setId(id);

			TracciatiDAO tracciatiDAO = new TracciatiDAO(); 

			byte[] esito = tracciatiDAO.leggiEsitoTracciato(leggiTracciatoDTO);
			return this.handleResponseOk(Response.status(Status.OK).entity(esito),transactionId).build();
		}catch (Exception e) {
			return handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			if(ctx != null) ctx.log();
		} 
    }


}


