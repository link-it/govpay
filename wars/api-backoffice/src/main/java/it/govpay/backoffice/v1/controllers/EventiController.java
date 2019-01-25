package it.govpay.backoffice.v1.controllers;

import java.io.ByteArrayOutputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.openspcoop2.utils.serialization.SerializationConfig;
import org.openspcoop2.utils.service.context.IContext;
import org.slf4j.Logger;
import org.springframework.security.core.Authentication;

import it.govpay.backoffice.v1.beans.Evento;
import it.govpay.backoffice.v1.beans.ListaEventi;
import it.govpay.backoffice.v1.beans.converter.EventiConverter;
import it.govpay.core.dao.eventi.EventiDAO;
import it.govpay.core.dao.eventi.dto.ListaEventiDTO;
import it.govpay.core.dao.eventi.dto.ListaEventiDTOResponse;
import it.govpay.core.utils.GovpayConfig;
import it.govpay.core.utils.GpThreadLocal;
import it.govpay.core.utils.SimpleDateFormatUtils;



public class EventiController extends BaseController {

     public EventiController(String nomeServizio,Logger log) {
		super(nomeServizio,log, GovpayConfig.GOVPAY_BACKOFFICE_OPEN_API_FILE_NAME);
     }



    public Response eventiGET(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , Integer pagina, Integer risultatiPerPagina, String idDominio, String iuv, String idA2A, String idPendenza) {
    	String methodName = "eventiGET";  
		IContext ctx = null;
		String transactionId = null;
		ByteArrayOutputStream baos= null;
		this.log.info(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 
		try{
			baos = new ByteArrayOutputStream();
			this.logRequest(uriInfo, httpHeaders, methodName, baos);
			
			ctx =  GpThreadLocal.get();
			transactionId = ctx.getTransactionId();
			
			// Parametri - > DTO Input
			
			ListaEventiDTO listaEventiDTO = new ListaEventiDTO(user);
			
			listaEventiDTO.setPagina(pagina);
			listaEventiDTO.setLimit(risultatiPerPagina);
			listaEventiDTO.setIdDominio(idDominio);
			listaEventiDTO.setIuv(iuv);
			listaEventiDTO.setIdA2A(idA2A);
			listaEventiDTO.setIdPendenza(idPendenza);
			
			// INIT DAO
			
			EventiDAO pspDAO = new EventiDAO();
			
			// CHIAMATA AL DAO
			
			ListaEventiDTOResponse listaEventiDTOResponse = pspDAO.listaEventi(listaEventiDTO);
			
			// CONVERT TO JSON DELLA RISPOSTA
			
			List<Evento> results = new ArrayList<>();
			for(it.govpay.bd.model.Evento evento: listaEventiDTOResponse.getResults()) {
				results.add(EventiConverter.toRsModel(evento));
			}
			
			ListaEventi response = new ListaEventi(results, this.getServicePath(uriInfo),
					listaEventiDTOResponse.getTotalResults(), pagina, risultatiPerPagina);
			
			SerializationConfig serializationConfig = new SerializationConfig();
			serializationConfig.setExcludes(Arrays.asList("jsonIdFilter"));
			serializationConfig.setDf(SimpleDateFormatUtils.newSimpleDateFormatDataOreMinuti());
			
			this.logResponse(uriInfo, httpHeaders, methodName, response.toJSON(null,serializationConfig), 200);
			this.log.info(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
			return this.handleResponseOk(Response.status(Status.OK).entity(response.toJSON(null,serializationConfig)),transactionId).build();
			
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.log(ctx);
		}
    }


}


