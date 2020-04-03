package it.govpay.backoffice.v1.controllers;


import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.lang.StringUtils;
import org.openspcoop2.generic_project.exception.NotAuthorizedException;
import org.openspcoop2.utils.service.context.ContextThreadLocal;
import org.slf4j.Logger;
import org.springframework.security.core.Authentication;

import it.govpay.backoffice.v1.beans.EntrataPrevistaIndex;
import it.govpay.backoffice.v1.beans.ListaEntratePreviste;
import it.govpay.backoffice.v1.beans.converter.EntrataPrevistaConverter;
import it.govpay.bd.viste.model.EntrataPrevista;
import it.govpay.core.dao.anagrafica.dto.BasicFindRequestDTO;
import it.govpay.core.dao.reportistica.EntratePrevisteDAO;
import it.govpay.core.dao.reportistica.dto.ListaEntratePrevisteDTO;
import it.govpay.core.dao.reportistica.dto.ListaEntratePrevisteDTO.FormatoRichiesto;
import it.govpay.core.dao.reportistica.dto.ListaEntratePrevisteDTOResponse;
import it.govpay.core.utils.SimpleDateFormatUtils;
import it.govpay.model.Acl.Diritti;
import it.govpay.model.Acl.Servizio;
import it.govpay.model.Utenza.TIPO_UTENZA;

public class ReportisticheController extends BaseController {

	public ReportisticheController(String nomeServizio,Logger log) {
		super(nomeServizio,log);
	}

	public Response getReportEntratePreviste(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , Integer pagina, Integer risultatiPerPagina, String idDominio, String dataDa, String dataA) {
		String methodName = "getReportEntratePreviste";
		String transactionId = ContextThreadLocal.get().getTransactionId();
		String accept = MediaType.APPLICATION_JSON;
		if(httpHeaders.getRequestHeaders().containsKey("Accept")) {
			accept = httpHeaders.getRequestHeaders().get("Accept").get(0).toLowerCase();
		}

		try{
			this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 

			// autorizzazione sulla API
			this.isAuthorized(user, Arrays.asList(TIPO_UTENZA.OPERATORE, TIPO_UTENZA.APPLICAZIONE), Arrays.asList(Servizio.RENDICONTAZIONI_E_INCASSI), Arrays.asList(Diritti.LETTURA));

			// Parametri - > DTO Input

			ListaEntratePrevisteDTO listaEntratePrevisteDTO = new ListaEntratePrevisteDTO(user);

			Date dataDaDate = null;
			if(dataDa!=null) {
				dataDaDate = SimpleDateFormatUtils.getDataDaConTimestamp(dataDa, "dataDa", true);
				listaEntratePrevisteDTO.setDataDa(dataDaDate);
			}

			Date dataADate = null;
			if(dataA!=null) {
				dataADate = SimpleDateFormatUtils.getDataAConTimestamp(dataA, "dataA", true);
				listaEntratePrevisteDTO.setDataA(dataADate);
			}

			if(idDominio != null)
				listaEntratePrevisteDTO.setIdDominio(idDominio);

			// INIT DAO

			EntratePrevisteDAO entratePrevisteDAO = new EntratePrevisteDAO(); 

			// CHIAMATA AL DAO

			if(accept.isEmpty() || accept.toLowerCase().contains(MediaType.APPLICATION_JSON)) {

				if(risultatiPerPagina == null) {
					risultatiPerPagina = BasicFindRequestDTO.DEFAULT_LIMIT;
				} 

				listaEntratePrevisteDTO.setLimit(risultatiPerPagina);

				if(pagina == null) {
					pagina = 1;
				}

				listaEntratePrevisteDTO.setPagina(pagina);


				listaEntratePrevisteDTO.setFormato(FormatoRichiesto.JSON);
				listaEntratePrevisteDTO.setOrderBy("-data");

				ListaEntratePrevisteDTOResponse listaEntratePrevisteDTOResponse = entratePrevisteDAO.listaEntrate(listaEntratePrevisteDTO);

				// CONVERT TO JSON DELLA RISPOSTA

				List<EntrataPrevistaIndex> results = new ArrayList<>();
				for(EntrataPrevista entrataPrevista: listaEntratePrevisteDTOResponse.getResults()) {
					EntrataPrevistaIndex rsModel = EntrataPrevistaConverter.toRsModelIndex(entrataPrevista); 
					results.add(rsModel);
				}

				ListaEntratePreviste response = new ListaEntratePreviste(results, this.getServicePath(uriInfo), listaEntratePrevisteDTOResponse.getTotalResults(), pagina, risultatiPerPagina);

				this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
				return this.handleResponseOk(Response.status(Status.OK).entity(response.toJSON(null)),transactionId).build();

			} else if(accept.toLowerCase().contains("application/pdf")) {
				listaEntratePrevisteDTO.setFormato(FormatoRichiesto.PDF);

				ListaEntratePrevisteDTOResponse listaEntratePrevisteDTOResponse = entratePrevisteDAO.listaEntrate(listaEntratePrevisteDTO);

				String dDa = dataDaDate != null ? SimpleDateFormatUtils.newSimpleDateFormatSoloData().format(dataDaDate) : "";
				String dA = dataADate != null ? SimpleDateFormatUtils.newSimpleDateFormatSoloData().format(dataADate) : "";

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

				this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
				return this.handleResponseOk(Response.status(Status.OK).type("application/pdf").entity(b).header("content-disposition", "attachment; filename=\""+pdfEntryName+"\""),transactionId).build();
			} else {
				// formato non accettato
				throw new NotAuthorizedException("Reportistica Entrate Previste non disponibile nel formato richiesto");
			}

		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.log(ContextThreadLocal.get());
		}
	}

}


