/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2025 Link.it srl (http://www.link.it).
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 3, as published by
 * the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package it.govpay.pendenze.v2.controller;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.lang3.ArrayUtils;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.service.context.ContextThreadLocal;
import org.slf4j.Logger;
import org.springframework.security.core.Authentication;

import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.model.Rpt;
import it.govpay.bd.model.Versamento;
import it.govpay.core.autorizzazione.AuthorizationManager;
import it.govpay.core.autorizzazione.beans.GovpayLdapUserDetails;
import it.govpay.core.autorizzazione.utils.AutorizzazioneUtils;
import it.govpay.core.beans.Costanti;
import it.govpay.core.dao.pagamenti.RptDAO;
import it.govpay.core.dao.pagamenti.dto.LeggiRicevutaDTO;
import it.govpay.core.dao.pagamenti.dto.LeggiRicevutaDTO.FormatoRicevuta;
import it.govpay.core.dao.pagamenti.dto.LeggiRicevutaDTOResponse;
import it.govpay.core.dao.pagamenti.dto.LeggiRptDTO;
import it.govpay.core.dao.pagamenti.dto.LeggiRptDTOResponse;
import it.govpay.core.dao.pagamenti.dto.ListaRptDTO;
import it.govpay.core.dao.pagamenti.dto.ListaRptDTOResponse;
import it.govpay.core.exceptions.NotAuthorizedException;
import it.govpay.core.exceptions.ValidationException;
import it.govpay.core.utils.GovpayConfig;
import it.govpay.core.utils.MessaggiPagoPARptUtils;
import it.govpay.core.utils.MessaggiPagoPARtUtils;
import it.govpay.core.utils.SimpleDateFormatUtils;
import it.govpay.core.utils.validator.ValidatorFactory;
import it.govpay.core.utils.validator.ValidatoreIdentificativi;
import it.govpay.core.utils.validator.ValidatoreUtils;
import it.govpay.model.Acl.Diritti;
import it.govpay.model.Acl.Servizio;
import it.govpay.model.Rpt.EsitoPagamento;
import it.govpay.model.Utenza.TIPO_UTENZA;
import it.govpay.pendenze.v1.controller.BaseController;
import it.govpay.pendenze.v2.beans.EsitoRpp;
import it.govpay.pendenze.v2.beans.ListaRpp;
import it.govpay.pendenze.v2.beans.PendenzaIndex;
import it.govpay.pendenze.v2.beans.Rpp;
import it.govpay.pendenze.v2.beans.RppIndex;
import it.govpay.pendenze.v2.beans.converter.PendenzeConverter;
import it.govpay.pendenze.v2.beans.converter.RptConverter;

public class RppController extends BaseController {

	public RppController(String nomeServizio,Logger log) {
		super(nomeServizio,log);
	}

	public Response rppGET(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , Integer pagina, Integer risultatiPerPagina, String ordinamento, String campi, String dataRptDa, String dataRptA, String dataRtDa, String dataRtA, String idDominio, String iuv, String ccp, String idA2A, String idPendenza, String idDebitore, String esitoPagamento, String idPagamento, Boolean metadatiPaginazione, Boolean maxRisultati) {
		String methodName = "rppGET";
		String transactionId = ContextThreadLocal.get().getTransactionId();
		this.logDebug(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName);
		try{
			// autorizzazione sulla API
			this.isAuthorized(user, Arrays.asList(TIPO_UTENZA.APPLICAZIONE), Arrays.asList(Servizio.API_PENDENZE), Arrays.asList(Diritti.LETTURA));

			ValidatorFactory vf = ValidatorFactory.newInstance();
			ValidatoreUtils.validaRisultatiPerPagina(vf, Costanti.PARAMETRO_RISULTATI_PER_PAGINA, risultatiPerPagina);

			// Parametri - > DTO Input

			ListaRptDTO listaRptDTO = new ListaRptDTO(user);
			listaRptDTO.setLimit(risultatiPerPagina);
			listaRptDTO.setPagina(pagina);

			ValidatoreIdentificativi validatoreId = ValidatoreIdentificativi.newInstance();

			if(esitoPagamento != null) {
				EsitoRpp esitoRPT = EsitoRpp.fromValue(esitoPagamento);

				EsitoPagamento esitoPagamentoModel = null;
				if(esitoRPT != null) {
					switch (esitoRPT) {
					case DECORRENZA_PARZIALE:
						esitoPagamentoModel = EsitoPagamento.DECORRENZA_TERMINI_PARZIALE;
 						break;
					case DECORRENZA:
						esitoPagamentoModel = EsitoPagamento.DECORRENZA_TERMINI;
						break;
					case ESEGUITO:
						esitoPagamentoModel = EsitoPagamento.PAGAMENTO_ESEGUITO;
						break;
					case ESEGUITO_PARZIALE:
						esitoPagamentoModel = EsitoPagamento.PAGAMENTO_PARZIALMENTE_ESEGUITO;
						break;
					case IN_CORSO:
						esitoPagamentoModel = EsitoPagamento.IN_CORSO;
						break;
					case NON_ESEGUITO:
						esitoPagamentoModel = EsitoPagamento.PAGAMENTO_NON_ESEGUITO;
						break;
					case RIFIUTATO:
						esitoPagamentoModel = EsitoPagamento.RIFIUTATO;
						break;
					}
				} else {
					throw new ValidationException("Codifica inesistente per esito. Valore fornito [" + esitoPagamento
							+ "] valori possibili " + ArrayUtils.toString(EsitoRpp.values()));
				}

				listaRptDTO.setEsitoPagamento(esitoPagamentoModel);
			}

			if(idDominio != null) {
				validatoreId.validaIdDominio("idDominio", idDominio);
				listaRptDTO.setIdDominio(idDominio);
			}
			if(iuv != null)
				listaRptDTO.setIuv(iuv);
			if(ccp != null)
				listaRptDTO.setCcp(ccp);

			if(idA2A != null) {
				validatoreId.validaIdApplicazione("idA2A", idA2A);
				listaRptDTO.setIdA2A(idA2A);
			}

			if(idPendenza != null) {
				validatoreId.validaIdPendenza("idPendenza", idPendenza);
				listaRptDTO.setIdPendenza(idPendenza);
			}

			if(idPagamento != null)
				listaRptDTO.setIdPagamento(idPagamento);

			if(ordinamento != null)
				listaRptDTO.setOrderBy(ordinamento);

			// dat RPT
			if(dataRptDa!=null) {
				Date dataDaDate = SimpleDateFormatUtils.getDataDaConTimestamp(dataRptDa, "dataRptDa");
				listaRptDTO.setDataDa(dataDaDate);
			}

			if(dataRptA!=null) {
				Date dataADate = SimpleDateFormatUtils.getDataAConTimestamp(dataRptA, "dataRptA");
				listaRptDTO.setDataA(dataADate);
			}

			// data RT
			if(dataRtDa!=null) {
				Date dataDaDate = SimpleDateFormatUtils.getDataDaConTimestamp(dataRtDa, "dataRtDa");
				listaRptDTO.setDataRtDa(dataDaDate);
			}


			if(dataRtA!=null) {
				Date dataADate = SimpleDateFormatUtils.getDataAConTimestamp(dataRtA, "dataRtA");
				listaRptDTO.setDataRtA(dataADate);
			}

			listaRptDTO.setIdDebitore(idDebitore);

			// se sei una applicazione allora vedi i pagamenti che hai caricato
			GovpayLdapUserDetails userDetails = AutorizzazioneUtils.getAuthenticationDetails(listaRptDTO.getUser());
			if(userDetails.getTipoUtenza().equals(TIPO_UTENZA.APPLICAZIONE)) {
				listaRptDTO.setIdA2A(userDetails.getApplicazione().getCodApplicazione());
			}

			listaRptDTO.setEseguiCount(metadatiPaginazione);
			listaRptDTO.setEseguiCountConLimit(maxRisultati);

			RptDAO rptDAO = new RptDAO();

			// CHIAMATA AL DAO

			ListaRptDTOResponse listaRptDTOResponse = rptDAO.listaRpt(listaRptDTO);

			// CONVERT TO JSON DELLA RISPOSTA
			List<RppIndex> results = new ArrayList<>();
			for(LeggiRptDTOResponse leggiRptDtoResponse: listaRptDTOResponse.getResults()) {
				results.add(RptConverter.toRsModelIndex(leggiRptDtoResponse.getRpt(),leggiRptDtoResponse.getVersamento(),leggiRptDtoResponse.getApplicazione()));
			}
			ListaRpp response = new ListaRpp(results, this.getServicePath(uriInfo), listaRptDTOResponse.getTotalResults(), pagina, risultatiPerPagina);

			this.logDebug(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName);
			return this.handleResponseOk(Response.status(Status.OK).entity(response.toJSON(campi)),transactionId).build();
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.logContext(ContextThreadLocal.get());
		}
	}



	public Response rppIdDominioIuvCcpRtGET(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , String idDominio, String iuv, String ccp) {
		String methodName = "rppIdDominioIuvCcpRtGET";
		String transactionId = ContextThreadLocal.get().getTransactionId();
		this.logDebug(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName);

		String accept = MediaType.APPLICATION_JSON;
		if(httpHeaders.getRequestHeaders().containsKey("Accept")) {
			accept = httpHeaders.getRequestHeaders().get("Accept").get(0).toLowerCase();
		}

		try{
			// autorizzazione sulla API
			this.isAuthorized(user, Arrays.asList(TIPO_UTENZA.APPLICAZIONE), Arrays.asList(Servizio.API_PENDENZE), Arrays.asList(Diritti.LETTURA));

			ValidatoreIdentificativi validatoreId = ValidatoreIdentificativi.newInstance();
			validatoreId.validaIdDominio("idDominio", idDominio);

			LeggiRicevutaDTO leggiPagamentoPortaleDTO = new LeggiRicevutaDTO(user);
			leggiPagamentoPortaleDTO.setIdDominio(idDominio);
			leggiPagamentoPortaleDTO.setIuv(iuv);
			ccp = ccp.contains("%") ? URLDecoder.decode(ccp,"UTF-8") : ccp;
			leggiPagamentoPortaleDTO.setCcp(ccp);

			// controllo che il dominio sia autorizzato
//			if(!AuthorizationManager.isDominioAuthorized(leggiPagamentoPortaleDTO.getUser(), idDominio)) {
//				throw AuthorizationManager.toNotAuthorizedException(leggiPagamentoPortaleDTO.getUser(),idDominio, null);
//			}

			RptDAO ricevuteDAO = new RptDAO();

			LeggiRicevutaDTOResponse ricevutaDTOResponse = null;

			boolean retrocompatibilitaMessaggiPagoPAV1 = GovpayConfig.getInstance().isConversioneMessaggiPagoPAV2NelFormatoV1();
			String mediaType = null;
			Object messaggioRT = null;

			if(accept.toLowerCase().contains(MediaType.APPLICATION_OCTET_STREAM)) {
				leggiPagamentoPortaleDTO.setFormato(FormatoRicevuta.RAW);
				mediaType = MediaType.APPLICATION_OCTET_STREAM;
			} else if(accept.toLowerCase().contains("application/pdf")) {
				leggiPagamentoPortaleDTO.setFormato(FormatoRicevuta.PDF);
				mediaType = "application/pdf";
			} else if(accept.toLowerCase().contains(MediaType.APPLICATION_JSON)) {
				leggiPagamentoPortaleDTO.setFormato(FormatoRicevuta.JSON);
				mediaType = MediaType.APPLICATION_JSON;
			} else { //XML
				leggiPagamentoPortaleDTO.setFormato(FormatoRicevuta.XML);
				mediaType = MediaType.TEXT_XML;
			}

			// lettura della RT uguale per tutte le casistiche
			ricevutaDTOResponse = ricevuteDAO.leggiRt(leggiPagamentoPortaleDTO);

			checkAutorizzazioniUtenza(leggiPagamentoPortaleDTO.getUser(), ricevutaDTOResponse.getRpt());

			Rpt rpt = ricevutaDTOResponse.getRpt();
			String rtPdfEntryName = null;
			if(accept.toLowerCase().contains(MediaType.APPLICATION_OCTET_STREAM)) {
				messaggioRT = new String(ricevutaDTOResponse.getRpt().getXmlRt());
			} else if(accept.toLowerCase().contains("application/pdf")) {
				messaggioRT = ricevutaDTOResponse.getPdf();
				rtPdfEntryName = idDominio +"_"+ iuv + "_"+ ccp + ".pdf";
			} else if(accept.toLowerCase().contains(MediaType.APPLICATION_JSON)) {
				messaggioRT = MessaggiPagoPARtUtils.getMessaggioRT(rpt, FormatoRicevuta.JSON, retrocompatibilitaMessaggiPagoPAV1);
			} else { //XML
				messaggioRT = MessaggiPagoPARtUtils.getMessaggioRT(rpt, FormatoRicevuta.XML, retrocompatibilitaMessaggiPagoPAV1);
			}

			ResponseBuilder entity = Response.status(Status.OK).type(mediaType).entity(messaggioRT);

			if(accept.toLowerCase().contains("application/pdf")) {
				entity.header("content-disposition", "attachment; filename=\""+rtPdfEntryName+"\"");
			}

			this.logDebug(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName);
			return this.handleResponseOk(entity,transactionId).build();

		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.logContext(ContextThreadLocal.get());
		}
	}



	public Response rppIdDominioIuvCcpRptGET(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , String idDominio, String iuv, String ccp) {
		String methodName = "rppIdDominioIuvCcpRtGET";
		String transactionId = ContextThreadLocal.get().getTransactionId();
		this.logDebug(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName);
		try{
			String accept = MediaType.APPLICATION_JSON;
			if(httpHeaders.getRequestHeaders().containsKey("Accept")) {
				accept = httpHeaders.getRequestHeaders().get("Accept").get(0).toLowerCase();
			}
			// autorizzazione sulla API
			this.isAuthorized(user, Arrays.asList(TIPO_UTENZA.APPLICAZIONE), Arrays.asList(Servizio.API_PENDENZE), Arrays.asList(Diritti.LETTURA));

			ValidatoreIdentificativi validatoreId = ValidatoreIdentificativi.newInstance();
			validatoreId.validaIdDominio("idDominio", idDominio);

			LeggiRptDTO leggiRptDTO = new LeggiRptDTO(user);
			leggiRptDTO.setIdDominio(idDominio);
			leggiRptDTO.setIuv(iuv);
			ccp = ccp.contains("%") ? URLDecoder.decode(ccp,"UTF-8") : ccp;
			leggiRptDTO.setCcp(ccp);

			// controllo che il dominio sia autorizzato
//			if(!AuthorizationManager.isDominioAuthorized(leggiRptDTO.getUser(), idDominio)) {
//				throw AuthorizationManager.toNotAuthorizedException(leggiRptDTO.getUser(),idDominio, null);
//			}

			RptDAO ricevuteDAO = new RptDAO();

			LeggiRptDTOResponse leggiRptDTOResponse = ricevuteDAO.leggiRpt(leggiRptDTO);

			checkAutorizzazioniUtenza(leggiRptDTO.getUser(), leggiRptDTOResponse.getRpt());

			// controllo che il dominio sia autorizzato
//			if(!AuthorizationManager.isDominioAuthorized(user, leggiRptDTOResponse.getDominio().getCodDominio())) {
//				throw AuthorizationManager.toNotAuthorizedException(user, leggiRptDTOResponse.getDominio().getCodDominio(), null);
//			}

			boolean retrocompatibilitaMessaggiPagoPAV1 = GovpayConfig.getInstance().isConversioneMessaggiPagoPAV2NelFormatoV1();
			String mediaType = null;
			Object messaggioRPT = null;
			if(accept.toLowerCase().contains(MediaType.APPLICATION_JSON)) {
				mediaType = MediaType.APPLICATION_JSON;
				messaggioRPT = MessaggiPagoPARptUtils.getMessaggioRPT(leggiRptDTOResponse.getRpt(), FormatoRicevuta.JSON, retrocompatibilitaMessaggiPagoPAV1);
			} else {
				mediaType = MediaType.TEXT_XML;
				messaggioRPT = MessaggiPagoPARptUtils.getMessaggioRPT(leggiRptDTOResponse.getRpt(), FormatoRicevuta.XML, retrocompatibilitaMessaggiPagoPAV1);
			}

			this.logDebug(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName);
			return this.handleResponseOk(Response.status(Status.OK).type(mediaType).entity(messaggioRPT),transactionId).build();
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.logContext(ContextThreadLocal.get());
		}
	}



	public Response rppIdDominioIuvCcpGET(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , String idDominio, String iuv, String ccp) {
		String methodName = "rppIdDominioIuvCcpGET";
		String transactionId = ContextThreadLocal.get().getTransactionId();
		this.logDebug(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName);

		try{
			// autorizzazione sulla API
			this.isAuthorized(user, Arrays.asList(TIPO_UTENZA.APPLICAZIONE), Arrays.asList(Servizio.API_PENDENZE), Arrays.asList(Diritti.LETTURA));

			ValidatoreIdentificativi validatoreId = ValidatoreIdentificativi.newInstance();
			validatoreId.validaIdDominio("idDominio", idDominio);

			LeggiRptDTO leggiRptDTO = new LeggiRptDTO(user);
			leggiRptDTO.setIdDominio(idDominio);
			leggiRptDTO.setIuv(iuv);
			ccp = ccp.contains("%") ? URLDecoder.decode(ccp,"UTF-8") : ccp;
			leggiRptDTO.setCcp(ccp);

//			// controllo che il dominio sia autorizzato
//			if(!AuthorizationManager.isDominioAuthorized(leggiRptDTO.getUser(), idDominio)) {
//				throw AuthorizationManager.toNotAuthorizedException(leggiRptDTO.getUser(),idDominio, null);
//			}

			RptDAO ricevuteDAO = new RptDAO();

			LeggiRptDTOResponse leggiRptDTOResponse = ricevuteDAO.leggiRpt(leggiRptDTO);

			checkAutorizzazioniUtenza(leggiRptDTO.getUser(), leggiRptDTOResponse.getRpt());

//			// controllo che il dominio sia autorizzato
//			if(!AuthorizationManager.isDominioAuthorized(user, leggiRptDTOResponse.getDominio().getCodDominio())) {
//				throw AuthorizationManager.toNotAuthorizedException(user, leggiRptDTOResponse.getDominio().getCodDominio(), null);
//			}

			Rpp response =  RptConverter.toRsModel(leggiRptDTOResponse.getRpt(),leggiRptDTOResponse.getVersamento(),leggiRptDTOResponse.getApplicazione());

			PendenzaIndex pendenza = PendenzeConverter.toRsIndexModel(leggiRptDTOResponse.getVersamento());

			response.setPendenza(pendenza);

			return this.handleResponseOk(Response.status(Status.OK).entity(response.toJSON(null)),transactionId).build();
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.logContext(ContextThreadLocal.get());
		}
	}

	private void checkAutorizzazioniUtenza(Authentication user, Rpt rpt) throws ServiceException, NotFoundException, NotAuthorizedException {
		GovpayLdapUserDetails details = AutorizzazioneUtils.getAuthenticationDetails(user);

		// se sei una applicazione allora vedi i pagamenti che hai caricato
		if(details.getTipoUtenza().equals(TIPO_UTENZA.APPLICAZIONE)) {

			Versamento versamento = rpt.getVersamento();
			BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), true);

			if(versamento.getApplicazione(configWrapper) == null ||
					!versamento.getApplicazione(configWrapper).getCodApplicazione().equals(details.getApplicazione().getCodApplicazione())) {
				throw AuthorizationManager.toNotAuthorizedException(user, "la transazione riferisce una pendenza che non appartiene all'applicazione chiamante");
			}
		}
	}
}
