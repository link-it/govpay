package it.govpay.backoffice.v1.controllers;


import java.io.ByteArrayOutputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.lang.StringUtils;
import org.openspcoop2.generic_project.exception.NotAuthorizedException;
import org.slf4j.Logger;

import it.govpay.backoffice.v1.beans.converter.EntrataPrevistaConverter;
import it.govpay.bd.viste.model.EntrataPrevista;
import it.govpay.core.dao.reportistica.EntratePrevisteDAO;
import it.govpay.core.dao.reportistica.dto.ListaEntratePrevisteDTO;
import it.govpay.core.dao.reportistica.dto.ListaEntratePrevisteDTO.FormatoRichiesto;
import it.govpay.core.dao.reportistica.dto.ListaEntratePrevisteDTOResponse;
import it.govpay.backoffice.v1.beans.EntrataPrevistaIndex;
import it.govpay.backoffice.v1.beans.ListaEntratePreviste;
import it.govpay.core.utils.GovpayConfig;
import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.GpThreadLocal;
import it.govpay.core.utils.SimpleDateFormatUtils;
import it.govpay.model.IAutorizzato;

public class ReportisticheController extends BaseController {

     public ReportisticheController(String nomeServizio,Logger log) {
    	 super(nomeServizio,log, GovpayConfig.GOVPAY_BACKOFFICE_OPEN_API_FILE_NAME);
     }

    public Response reportisticheEntratePrevisteGET(IAutorizzato user, UriInfo uriInfo, HttpHeaders httpHeaders , Integer pagina, Integer risultatiPerPagina, String idDominio, String dataDa, String dataA) {
    	GpContext ctx = null;
		String transactionId = null;

		String methodName = "reportisticheEntratePrevisteGET";
		
		String accept = "";
		if(httpHeaders.getRequestHeaders().containsKey("Accept")) {
			accept = httpHeaders.getRequestHeaders().get("Accept").get(0).toLowerCase();
		}
		
		try(ByteArrayOutputStream baos= new ByteArrayOutputStream();){
			this.log.info(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 

			this.logRequest(uriInfo, httpHeaders, methodName, baos);

			ctx =  GpThreadLocal.get();
			transactionId = ctx.getTransactionId();

			// Parametri - > DTO Input

			ListaEntratePrevisteDTO listaEntratePrevisteDTO = new ListaEntratePrevisteDTO(user);

			if(dataDa != null) {
				Calendar c = Calendar.getInstance();
				c.setTime(SimpleDateFormatUtils.newSimpleDateFormatSoloData().parse(dataDa));
				c.set(Calendar.HOUR_OF_DAY, 0);
				c.set(Calendar.MINUTE, 0);
				c.set(Calendar.SECOND, 0);
				c.set(Calendar.MILLISECOND, 0);
				
				listaEntratePrevisteDTO.setDataDa(c.getTime());
			}
			if(dataA != null) {
				Calendar c = Calendar.getInstance();
				c.setTime(SimpleDateFormatUtils.newSimpleDateFormatSoloData().parse(dataA));
				c.set(Calendar.HOUR_OF_DAY, 25);
				c.set(Calendar.MINUTE, 59);
				c.set(Calendar.SECOND, 59);
				c.set(Calendar.MILLISECOND, 999);
				
				listaEntratePrevisteDTO.setDataA(c.getTime());
			}
			if(idDominio != null)
				listaEntratePrevisteDTO.setIdDominio(idDominio);
		 
			// INIT DAO

			EntratePrevisteDAO entratePrevisteDAO = new EntratePrevisteDAO(); 

			// CHIAMATA AL DAO
			
			if(accept.isEmpty() || accept.toLowerCase().contains(MediaType.APPLICATION_JSON)) {
				
				if(pagina == null) {
					listaEntratePrevisteDTO.setPagina(1);
				}
				
				if(risultatiPerPagina == null) {
					listaEntratePrevisteDTO.setLimit(ListaEntratePrevisteDTO.DEFAULT_LIMIT);
				}
				
				listaEntratePrevisteDTO.setFormato(FormatoRichiesto.JSON);

				ListaEntratePrevisteDTOResponse listaEntratePrevisteDTOResponse = entratePrevisteDAO.listaEntrate(listaEntratePrevisteDTO);
	
				// CONVERT TO JSON DELLA RISPOSTA
	
				List<EntrataPrevistaIndex> results = new ArrayList<>();
				for(EntrataPrevista entrataPrevista: listaEntratePrevisteDTOResponse.getResults()) {
					EntrataPrevistaIndex rsModel = EntrataPrevistaConverter.toRsModelIndex(entrataPrevista); 
					results.add(rsModel);
				}
	
				ListaEntratePreviste response = new ListaEntratePreviste(results, this.getServicePath(uriInfo), listaEntratePrevisteDTOResponse.getTotalResults(), listaEntratePrevisteDTO.getPagina(), listaEntratePrevisteDTO.getLimit());
	
				this.logResponse(uriInfo, httpHeaders, methodName, response.toJSON(null), 200);
				this.log.info(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
				return this.handleResponseOk(Response.status(Status.OK).entity(response.toJSON(null)),transactionId).build();
			
			} else if(accept.toLowerCase().contains("application/pdf")) {
				listaEntratePrevisteDTO.setFormato(FormatoRichiesto.PDF);
				
				ListaEntratePrevisteDTOResponse listaEntratePrevisteDTOResponse = entratePrevisteDAO.listaEntrate(listaEntratePrevisteDTO);
				
				String dDa = dataDa != null ? SimpleDateFormatUtils.newSimpleDateFormatSoloData().format(dataDa) : "";
				String dA = dataA != null ? SimpleDateFormatUtils.newSimpleDateFormatSoloData().format(dataA) : "";
				
				StringBuilder sb = new StringBuilder();
				sb.append("Prospetto_Riscossioni");
				
				if(StringUtils.isNotEmpty(idDominio))
					sb.append("_").append(idDominio);
				
				if(StringUtils.isNotEmpty(dDa))
					sb.append("_").append(dDa);
				
				if(StringUtils.isNotEmpty(dA))
					sb.append("_").append(dA);
				
				sb.append(".pdf");
				
				String pdfEntryName = sb.toString();
				byte[] b = listaEntratePrevisteDTOResponse.getPdf(); 

				this.logResponse(uriInfo, httpHeaders, methodName, b, 200);
				this.log.info(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
				return this.handleResponseOk(Response.status(Status.OK).type("application/pdf").entity(b).header("content-disposition", "attachment; filename=\""+pdfEntryName+"\""),transactionId).build();
			} else {
				// formato non accettato
				throw new NotAuthorizedException("Reportistica Entrate Previste non disponibile nel formato richiesto");
			}

		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			if(ctx != null) ctx.log();
		}
    }


}


