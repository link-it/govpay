package it.govpay.backoffice.v1.controllers;


import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.openspcoop2.generic_project.exception.NotAuthorizedException;
import org.openspcoop2.utils.json.ValidationException;
import org.slf4j.Logger;
import org.springframework.security.core.Authentication;

import it.govpay.backoffice.v1.beans.EntrataPrevistaIndex;
import it.govpay.backoffice.v1.beans.ListaEntratePreviste;
import it.govpay.backoffice.v1.beans.ListaStatisticheQuadrature;
import it.govpay.backoffice.v1.beans.RaggruppamentoStatistica;
import it.govpay.backoffice.v1.beans.StatisticaQuadratura;
import it.govpay.backoffice.v1.beans.converter.EntrataPrevistaConverter;
import it.govpay.backoffice.v1.beans.converter.StatisticaQuadraturaConverter;
import it.govpay.bd.reportistica.statistiche.model.StatisticaRiscossione;
import it.govpay.bd.viste.model.EntrataPrevista;
import it.govpay.core.dao.anagrafica.dto.BasicFindRequestDTO;
import it.govpay.core.dao.reportistica.EntratePrevisteDAO;
import it.govpay.core.dao.reportistica.StatisticaRiscossioniDAO;
import it.govpay.core.dao.reportistica.dto.ListaEntratePrevisteDTO;
import it.govpay.core.dao.reportistica.dto.ListaEntratePrevisteDTO.FormatoRichiesto;
import it.govpay.core.dao.reportistica.dto.ListaEntratePrevisteDTOResponse;
import it.govpay.core.dao.reportistica.dto.ListaRiscossioniDTO;
import it.govpay.core.dao.reportistica.dto.ListaRiscossioniDTO.GROUP_BY;
import it.govpay.core.dao.reportistica.dto.ListaRiscossioniDTOResponse;
import it.govpay.core.utils.SimpleDateFormatUtils;
import it.govpay.model.Acl.Diritti;
import it.govpay.model.Acl.Servizio;
import it.govpay.model.Utenza.TIPO_UTENZA;
import it.govpay.model.reportistica.statistiche.FiltroRiscossioni;

public class ReportisticheController extends BaseController {

	public ReportisticheController(String nomeServizio,Logger log) {
		super(nomeServizio,log);
	}

	public Response getReportEntratePreviste(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , Integer pagina, Integer risultatiPerPagina, String idDominio, String dataDa, String dataA) {
		String methodName = "getReportEntratePreviste";
		String transactionId = this.context.getTransactionId();
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
				dataDaDate = DateUtils.parseDate(dataDa, SimpleDateFormatUtils.datePatternsRest.toArray(new String[0]));
				Calendar c = Calendar.getInstance();
				c.setTime(dataDaDate);
				c.set(Calendar.HOUR_OF_DAY, 0);
				c.set(Calendar.MINUTE, 0);
				c.set(Calendar.SECOND, 0);
				c.set(Calendar.MILLISECOND, 0);

				listaEntratePrevisteDTO.setDataDa(c.getTime());
			}

			Date dataADate = null;
			if(dataA!=null) {
				dataADate = DateUtils.parseDate(dataA, SimpleDateFormatUtils.datePatternsRest.toArray(new String[0]));
				Calendar c = Calendar.getInstance();
				c.setTime(dataADate);
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

				if(risultatiPerPagina == null) {
					listaEntratePrevisteDTO.setLimit(BasicFindRequestDTO.DEFAULT_LIMIT);
				} else {
					listaEntratePrevisteDTO.setLimit(risultatiPerPagina);
				}

				if(pagina == null) {
					listaEntratePrevisteDTO.setPagina(1);
				} else {
					listaEntratePrevisteDTO.setPagina(pagina);
				}


				listaEntratePrevisteDTO.setFormato(FormatoRichiesto.JSON);
				listaEntratePrevisteDTO.setOrderBy("-data");

				ListaEntratePrevisteDTOResponse listaEntratePrevisteDTOResponse = entratePrevisteDAO.listaEntrate(listaEntratePrevisteDTO);

				// CONVERT TO JSON DELLA RISPOSTA

				List<EntrataPrevistaIndex> results = new ArrayList<>();
				for(EntrataPrevista entrataPrevista: listaEntratePrevisteDTOResponse.getResults()) {
					EntrataPrevistaIndex rsModel = EntrataPrevistaConverter.toRsModelIndex(entrataPrevista); 
					results.add(rsModel);
				}

				ListaEntratePreviste response = new ListaEntratePreviste(results, this.getServicePath(uriInfo), listaEntratePrevisteDTOResponse.getTotalResults(), listaEntratePrevisteDTO.getPagina(), listaEntratePrevisteDTO.getLimit());

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
			this.log(this.context);
		}
	}



	public Response reportisticheQuadratureGET(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , Integer pagina, Integer risultatiPerPagina, 
				String dataDa, String dataA, String idDominio, String idUnita, String idTipoPendenza, String direzione, String divisione, List<String> gruppi) {
		String methodName = "getReportEntratePreviste";
		String transactionId = this.context.getTransactionId();

		try{
			this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 

			// autorizzazione sulla API
			this.isAuthorized(user, Arrays.asList(TIPO_UTENZA.OPERATORE, TIPO_UTENZA.APPLICAZIONE), Arrays.asList(Servizio.RENDICONTAZIONI_E_INCASSI), Arrays.asList(Diritti.LETTURA));

			// Parametri - > DTO Input

			ListaRiscossioniDTO listaRiscossioniDTO = new ListaRiscossioniDTO(user);
			
			FiltroRiscossioni filtro = new FiltroRiscossioni();
			
			if(risultatiPerPagina == null) {
				listaRiscossioniDTO.setLimit(BasicFindRequestDTO.DEFAULT_LIMIT);
			} else {
				listaRiscossioniDTO.setLimit(risultatiPerPagina);
			}

			if(pagina == null) {
				listaRiscossioniDTO.setPagina(1);
			} else {
				listaRiscossioniDTO.setPagina(pagina);
			}

			Date dataDaDate = null;
			if(dataDa!=null) {
				dataDaDate = DateUtils.parseDate(dataDa, SimpleDateFormatUtils.datePatternsRest.toArray(new String[0]));
				Calendar c = Calendar.getInstance();
				c.setTime(dataDaDate);
				c.set(Calendar.HOUR_OF_DAY, 0);
				c.set(Calendar.MINUTE, 0);
				c.set(Calendar.SECOND, 0);
				c.set(Calendar.MILLISECOND, 0);

				filtro.setDataDa(c.getTime());
			}

			Date dataADate = null;
			if(dataA!=null) {
				dataADate = DateUtils.parseDate(dataA, SimpleDateFormatUtils.datePatternsRest.toArray(new String[0]));
				Calendar c = Calendar.getInstance();
				c.setTime(dataADate);
				c.set(Calendar.HOUR_OF_DAY, 25); 
				c.set(Calendar.MINUTE, 59);
				c.set(Calendar.SECOND, 59);
				c.set(Calendar.MILLISECOND, 999);

				filtro.setDataA(c.getTime());
			}
			
			filtro.setCodDominio(idDominio);
			filtro.setCodUo(idUnita);
			filtro.setCodTipoVersamento(idTipoPendenza);
			filtro.setDirezione(direzione);
			filtro.setDivisione(divisione);
			
			if(gruppi != null && gruppi.size() >0) {
				List<GROUP_BY> groupBy = new ArrayList<ListaRiscossioniDTO.GROUP_BY>();
				for (String gruppoString : gruppi) {
					RaggruppamentoStatistica gruppo = RaggruppamentoStatistica.fromValue(gruppoString);
					if(gruppo != null) {
						GROUP_BY gruppoToAdd = null;
						
						switch (gruppo) {
						case APPLICAZIONE:
							gruppoToAdd = GROUP_BY.APPLICAZIONE;
							break;
						case DIREZIONE:
							gruppoToAdd = GROUP_BY.DIR;
							break;
						case DIVISIONE:
							gruppoToAdd = GROUP_BY.DIV;
							break;
						case DOMINIO:
							gruppoToAdd = GROUP_BY.DOMINIO;
							break;
						case TIPO_PENDENZA:
							gruppoToAdd = GROUP_BY.TIPO_PENDENZA;
							break;
						case UNITA_OPERATIVA:
							gruppoToAdd = GROUP_BY.UO;
							break;
						}
						
						if(groupBy.contains(gruppoToAdd))
							throw new ValidationException("Il gruppo [" + gruppoString + "] e' stato indicato piu' di una volta.");
						
						groupBy.add(gruppoToAdd);
						
					} else {
						throw new ValidationException("Codifica inesistente per gruppo. Valore fornito [" + gruppoString + "] valori possibili " + ArrayUtils.toString(RaggruppamentoStatistica.values()));
					}
				}
				listaRiscossioniDTO.setGroupBy(groupBy);
			} else {
				throw new ValidationException("Indicare almeno un gruppo");
			}
			
			listaRiscossioniDTO.setFiltro(filtro);
			
			// INIT DAO

			StatisticaRiscossioniDAO statisticaRiscossioniDAO = new StatisticaRiscossioniDAO(); 
	
			// CHIAMATA AL DAO
			
			ListaRiscossioniDTOResponse listaRiscossioniDTOResponse = statisticaRiscossioniDAO.listaRiscossioni(listaRiscossioniDTO);
			
			// CONVERT TO JSON DELLA RISPOSTA

			List<StatisticaQuadratura> results = new ArrayList<>();
			for(StatisticaRiscossione entrataPrevista: listaRiscossioniDTOResponse.getResults()) {
				StatisticaQuadratura rsModel = StatisticaQuadraturaConverter.toRsModelIndex(entrataPrevista); 
				results.add(rsModel);
			} 

			ListaStatisticheQuadrature response = new ListaStatisticheQuadrature(results, this.getServicePath(uriInfo), listaRiscossioniDTOResponse.getTotalResults(), listaRiscossioniDTO.getPagina(), listaRiscossioniDTO.getLimit());

			this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
			return this.handleResponseOk(Response.status(Status.OK).entity(response.toJSON(null)),transactionId).build();
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.log(this.context);
		}
	}


}


