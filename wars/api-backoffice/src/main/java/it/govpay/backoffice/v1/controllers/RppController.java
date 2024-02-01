/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2024 Link.it srl (http://www.link.it).
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
package it.govpay.backoffice.v1.controllers;

import java.io.ByteArrayOutputStream;
import java.net.URLDecoder;
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
import javax.xml.bind.JAXBException;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.service.context.ContextThreadLocal;
import org.slf4j.Logger;
import org.springframework.security.core.Authentication;
import org.xml.sax.SAXException;

import it.gov.digitpa.schemas._2011.pagamenti.CtRicevutaTelematica;
import it.gov.digitpa.schemas._2011.pagamenti.CtRichiestaPagamentoTelematico;
import it.gov.pagopa.pagopa_api.pa.pafornode.PaGetPaymentRes;
import it.gov.pagopa.pagopa_api.pa.pafornode.PaGetPaymentV2Response;
import it.gov.pagopa.pagopa_api.pa.pafornode.PaSendRTReq;
import it.gov.pagopa.pagopa_api.pa.pafornode.PaSendRTV2Request;
import it.govpay.backoffice.v1.beans.EsitoRpt;
import it.govpay.backoffice.v1.beans.ListaRpp;
import it.govpay.backoffice.v1.beans.PatchOp;
import it.govpay.backoffice.v1.beans.PatchOp.OpEnum;
import it.govpay.backoffice.v1.beans.PendenzaIndex;
import it.govpay.backoffice.v1.beans.Rpp;
import it.govpay.backoffice.v1.beans.RppIndex;
import it.govpay.backoffice.v1.beans.converter.PatchOpConverter;
import it.govpay.backoffice.v1.beans.converter.PendenzeConverter;
import it.govpay.backoffice.v1.beans.converter.RptConverter;
import it.govpay.core.autorizzazione.AuthorizationManager;
import it.govpay.core.beans.Costanti;
import it.govpay.core.beans.JSONSerializable;
import it.govpay.core.dao.pagamenti.RptDAO;
import it.govpay.core.dao.pagamenti.dto.LeggiRicevutaDTO;
import it.govpay.core.dao.pagamenti.dto.LeggiRicevutaDTO.FormatoRicevuta;
import it.govpay.core.dao.pagamenti.dto.LeggiRicevutaDTOResponse;
import it.govpay.core.dao.pagamenti.dto.LeggiRptDTO;
import it.govpay.core.dao.pagamenti.dto.LeggiRptDTOResponse;
import it.govpay.core.dao.pagamenti.dto.ListaRptDTO;
import it.govpay.core.dao.pagamenti.dto.ListaRptDTOResponse;
import it.govpay.core.dao.pagamenti.dto.PatchRptDTO;
import it.govpay.core.dao.pagamenti.dto.PatchRptDTOResponse;
import it.govpay.core.exceptions.IOException;
import it.govpay.core.exceptions.ValidationException;
import it.govpay.core.utils.MessaggiPagoPAUtils;
import it.govpay.core.utils.SimpleDateFormatUtils;
import it.govpay.core.utils.validator.ValidatorFactory;
import it.govpay.core.utils.validator.ValidatoreIdentificativi;
import it.govpay.core.utils.validator.ValidatoreUtils;
import it.govpay.model.Acl.Diritti;
import it.govpay.model.Acl.Servizio;
import it.govpay.model.Rpt.EsitoPagamento;
import it.govpay.model.Utenza.TIPO_UTENZA;
import it.govpay.pagopa.beans.utils.JaxbUtils;

public class RppController extends BaseController {

	public RppController(String nomeServizio,Logger log) {
		super(nomeServizio,log);
	}

	public Response findRpps(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , Integer pagina, Integer risultatiPerPagina, String ordinamento, String campi, String idDominio, String iuv, String ccp, String idA2A, String idPendenza, String esito, String idPagamento, String idDebitore, String dataRptDa, String dataRptA, String dataRtDa, String dataRtA, List<String> direzione, List<String> divisione, String tassonomia, String idUnita, String idTipoPendenza, String anagraficaDebitore, Boolean metadatiPaginazione, Boolean maxRisultati, Boolean retrocompatibilitaMessaggiPagoPAV1) {
		String methodName = "findRpps";
		String transactionId = ContextThreadLocal.get().getTransactionId();
		this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName));
		this.setMaxRisultati(maxRisultati);
		try{
			// autorizzazione sulla API
			this.isAuthorized(user, Arrays.asList(TIPO_UTENZA.OPERATORE, TIPO_UTENZA.APPLICAZIONE), Arrays.asList(Servizio.PENDENZE), Arrays.asList(Diritti.LETTURA));

			ValidatorFactory vf = ValidatorFactory.newInstance();
			ValidatoreUtils.validaRisultatiPerPagina(vf, Costanti.PARAMETRO_RISULTATI_PER_PAGINA, risultatiPerPagina);

			// Parametri - > DTO Input

			ListaRptDTO listaRptDTO = new ListaRptDTO(user);
			listaRptDTO.setLimit(risultatiPerPagina);
			listaRptDTO.setPagina(pagina);
			listaRptDTO.setEseguiCount(metadatiPaginazione);
			listaRptDTO.setEseguiCountConLimit(maxRisultati);

			if(esito != null) {
				EsitoRpt esitoRPT = EsitoRpt.fromValue(esito);

				if (esitoRPT != null) {
					EsitoPagamento esitoPagamento = null;
					switch(esitoRPT) {
					case DECORENNZA_PARZIALE:
						esitoPagamento = EsitoPagamento.DECORRENZA_TERMINI_PARZIALE;
						break;
					case DECORRENZA:
						esitoPagamento = EsitoPagamento.DECORRENZA_TERMINI;
						break;
					case ESEGUITO:
						esitoPagamento = EsitoPagamento.PAGAMENTO_ESEGUITO;
						break;
					case ESEGUITO_PARZIALE:
						esitoPagamento = EsitoPagamento.PAGAMENTO_PARZIALMENTE_ESEGUITO;
						break;
					case IN_CORSO:
						esitoPagamento = EsitoPagamento.IN_CORSO;
						break;
					case NON_ESEGUITO:
						esitoPagamento = EsitoPagamento.PAGAMENTO_NON_ESEGUITO;
						break;
					case RIFIUTATO:
						esitoPagamento = EsitoPagamento.RIFIUTATO;
						break;
					}
					listaRptDTO.setEsitoPagamento(esitoPagamento);
				} else {
					throw new ValidationException("Codifica inesistente per esito. Valore fornito [" + esito
							+ "] valori possibili " + ArrayUtils.toString(EsitoRpt.values()));
				}
			}
			if(idDominio != null)
				listaRptDTO.setIdDominio(idDominio);
			if(iuv != null)
				listaRptDTO.setIuv(iuv);
			if(ccp != null)
				listaRptDTO.setCcp(ccp);
			if(idA2A != null)
				listaRptDTO.setIdA2A(idA2A);
			if(idPendenza != null)
				listaRptDTO.setIdPendenza(idPendenza);

			if(idPagamento != null)
				listaRptDTO.setIdPagamento(idPagamento);

			if(ordinamento != null)
				listaRptDTO.setOrderBy(ordinamento);

			if(idDebitore != null)
				listaRptDTO.setIdDebitore(idDebitore);

			listaRptDTO.setIdUnita(idUnita);
			listaRptDTO.setIdTipoPendenza(idTipoPendenza);
			listaRptDTO.setDirezione(direzione);
			listaRptDTO.setDivisione(divisione);
			listaRptDTO.setTassonomia(tassonomia);
			listaRptDTO.setAnagraficaDebitore(anagraficaDebitore);

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

			// INIT DAO

			RptDAO rptDAO = new RptDAO();

			// Autorizzazione sui domini
			List<String> domini = AuthorizationManager.getDominiAutorizzati(user);
			if(domini == null) {
				throw AuthorizationManager.toNotAuthorizedExceptionNessunDominioAutorizzato(user);
			}
			listaRptDTO.setCodDomini(domini);

			// Autorizzazione sulle uo
			//			List<IdUnitaOperativa> uo = AuthorizationManager.getUoAutorizzate(user);
			//			if(uo == null) {
			//				throw AuthorizationManager.toNotAuthorizedExceptionNessunaUOAutorizzata(user);
			//			}
			//			listaRptDTO.setUnitaOperative(uo);

			ListaRptDTOResponse listaRptDTOResponse = rptDAO.listaRpt(listaRptDTO);

			// CONVERT TO JSON DELLA RISPOSTA
			List<RppIndex> results = new ArrayList<>();
			for(LeggiRptDTOResponse leggiRptDtoResponse: listaRptDTOResponse.getResults()) {
				results.add(RptConverter.toRsModelIndex(leggiRptDtoResponse.getRpt(), retrocompatibilitaMessaggiPagoPAV1));
			}
			ListaRpp response = new ListaRpp(results, this.getServicePath(uriInfo), listaRptDTOResponse.getTotalResults(), pagina, risultatiPerPagina, this.maxRisultatiBigDecimal);

			this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName));
			return this.handleResponseOk(Response.status(Status.OK).entity(response.toJSON(campi)),transactionId).build();
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.logContext(ContextThreadLocal.get());
		}
	}

	public Response getRpp(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , String idDominio, String iuv, String ccp, Boolean retrocompatibilitaMessaggiPagoPAV1) {
		String methodName = "getRpp";
		String transactionId = ContextThreadLocal.get().getTransactionId();
		this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName));

		try{
			// autorizzazione sulla API
			this.isAuthorized(user, Arrays.asList(TIPO_UTENZA.OPERATORE, TIPO_UTENZA.APPLICAZIONE), Arrays.asList(Servizio.PENDENZE), Arrays.asList(Diritti.LETTURA));

			ValidatoreIdentificativi validatoreId = ValidatoreIdentificativi.newInstance();
			validatoreId.validaIdDominio("idDominio", idDominio);

			LeggiRptDTO leggiRptDTO = new LeggiRptDTO(user);
			leggiRptDTO.setIdDominio(idDominio);
			leggiRptDTO.setIuv(iuv);
			ccp = ccp.contains("%") ? URLDecoder.decode(ccp,"UTF-8") : ccp;
			leggiRptDTO.setCcp(ccp);

			// controllo che il dominio sia autorizzato
			if(!AuthorizationManager.isDominioAuthorized(leggiRptDTO.getUser(), idDominio)) {
				throw AuthorizationManager.toNotAuthorizedException(leggiRptDTO.getUser(),idDominio, null);
			}

			RptDAO ricevuteDAO = new RptDAO();

			LeggiRptDTOResponse leggiRptDTOResponse = ricevuteDAO.leggiRpt(leggiRptDTO);

			Rpp response =  RptConverter.toRsModel(leggiRptDTOResponse.getRpt(), retrocompatibilitaMessaggiPagoPAV1);

			// controllo che il dominio sia autorizzato
			if(!AuthorizationManager.isDominioAuthorized(user, leggiRptDTOResponse.getDominio().getCodDominio())) {
				throw AuthorizationManager.toNotAuthorizedException(user, leggiRptDTOResponse.getDominio().getCodDominio(), null);
			}

			PendenzaIndex pendenza = PendenzeConverter.toRsModelIndex(leggiRptDTOResponse.getVersamento());

			response.setPendenza(pendenza);

			return this.handleResponseOk(Response.status(Status.OK).entity(response.toJSON(null)),transactionId).build();
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.logContext(ContextThreadLocal.get());
		}
	}

	@SuppressWarnings("unchecked")
	public Response updateRpp(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , java.io.InputStream is, String idDominio, String iuv, String ccp) {
		String methodName = "updateRpp";
		String transactionId = ContextThreadLocal.get().getTransactionId();
		this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName));

		try(ByteArrayOutputStream baos= new ByteArrayOutputStream();){
			// salvo il json ricevuto
			IOUtils.copy(is, baos);

			// autorizzazione sulla API
			this.isAuthorized(user, Arrays.asList(TIPO_UTENZA.OPERATORE, TIPO_UTENZA.APPLICAZIONE), Arrays.asList(Servizio.PENDENZE), Arrays.asList(Diritti.SCRITTURA));

			ValidatoreIdentificativi validatoreId = ValidatoreIdentificativi.newInstance();
			validatoreId.validaIdDominio("idDominio", idDominio);

			RptDAO rptDAO = new RptDAO();

			PatchRptDTO patchRptDTO = new PatchRptDTO(user);
			patchRptDTO.setIdDominio(idDominio);
			patchRptDTO.setIuv(iuv);
			ccp = ccp.contains("%") ? URLDecoder.decode(ccp,"UTF-8") : ccp;
			patchRptDTO.setCcp(ccp);

			String jsonRequest = baos.toString();

			List<PatchOp> lstOp = new ArrayList<>();

			try {
				List<java.util.LinkedHashMap<?,?>> lst = JSONSerializable.parse(jsonRequest, List.class);
				for(java.util.LinkedHashMap<?,?> map: lst) {
					PatchOp op = new PatchOp();
					String opText = (String) map.get("op");
					OpEnum opFromValue = OpEnum.fromValue(opText);

					if(StringUtils.isNotEmpty(opText) && opFromValue == null)
						throw new ValidationException("Il campo op non e' valido.");

					op.setOp(opFromValue);
					op.setPath((String) map.get("path"));
					op.setValue(map.get("value"));
					op.validate();
					lstOp.add(op);
				}
			} catch (IOException e) {
				lstOp = JSONSerializable.parse(jsonRequest, List.class);
				//				PatchOp op = PatchOp.parse(jsonRequest);
				//				op.validate();
				//				lstOp.add(op);
			}

			patchRptDTO.setOp(PatchOpConverter.toModel(lstOp));


			PatchRptDTOResponse patchRptDTOResponse = rptDAO.patch(patchRptDTO);

			Rpp response =  RptConverter.toRsModel(patchRptDTOResponse.getRpt());

			PendenzaIndex pendenza = PendenzeConverter.toRsModelIndex(patchRptDTOResponse.getVersamento());

			response.setPendenza(pendenza);

			this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName));
			return this.handleResponseOk(Response.status(Status.OK).entity(response.toJSON(null)),transactionId).build();
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.logContext(ContextThreadLocal.get());
		}
	}

	public Response getRpt(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , String idDominio, String iuv, String ccp, Boolean retrocompatibilitaMessaggiPagoPAV1) {
		String methodName = "getRpt";
		String transactionId = ContextThreadLocal.get().getTransactionId();
		this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName));

		try{
			String accept = MediaType.APPLICATION_JSON;
			if(httpHeaders.getRequestHeaders().containsKey("Accept")) {
				accept = httpHeaders.getRequestHeaders().get("Accept").get(0).toLowerCase();
			}

			// autorizzazione sulla API
			this.isAuthorized(user, Arrays.asList(TIPO_UTENZA.OPERATORE, TIPO_UTENZA.APPLICAZIONE), Arrays.asList(Servizio.PENDENZE), Arrays.asList(Diritti.LETTURA));

			ValidatoreIdentificativi validatoreId = ValidatoreIdentificativi.newInstance();
			validatoreId.validaIdDominio("idDominio", idDominio);

			LeggiRptDTO leggiRptDTO = new LeggiRptDTO(user);
			leggiRptDTO.setIdDominio(idDominio);
			leggiRptDTO.setIuv(iuv);
			ccp = ccp.contains("%") ? URLDecoder.decode(ccp,"UTF-8") : ccp;
			leggiRptDTO.setCcp(ccp);

			// controllo che il dominio sia autorizzato
			if(!AuthorizationManager.isDominioAuthorized(leggiRptDTO.getUser(), idDominio)) {
				throw AuthorizationManager.toNotAuthorizedException(leggiRptDTO.getUser(),idDominio, null);
			}

			RptDAO ricevuteDAO = new RptDAO();

			LeggiRptDTOResponse leggiRptDTOResponse = ricevuteDAO.leggiRpt(leggiRptDTO);

			// controllo che il dominio sia autorizzato
			if(!AuthorizationManager.isDominioAuthorized(user, leggiRptDTOResponse.getDominio().getCodDominio())) {
				throw AuthorizationManager.toNotAuthorizedException(user, leggiRptDTOResponse.getDominio().getCodDominio(), null);
			}

			return creaMessaggioRpt(retrocompatibilitaMessaggiPagoPAV1, methodName, transactionId, accept,
					leggiRptDTOResponse);
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.logContext(ContextThreadLocal.get());
		}
	}

	private Response creaMessaggioRpt(Boolean retrocompatibilitaMessaggiPagoPAV1, String methodName, String transactionId,
			String accept, LeggiRptDTOResponse leggiRptDTOResponse)
					throws JAXBException, SAXException, ServiceException {
		if(accept.toLowerCase().contains(MediaType.APPLICATION_JSON)) {
			switch (leggiRptDTOResponse.getRpt().getVersione()) {
			case SANP_230:
				CtRichiestaPagamentoTelematico rpt = JaxbUtils.toRPT(leggiRptDTOResponse.getRpt().getXmlRpt(), false);
				this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName));
				return this.handleResponseOk(Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(rpt),transactionId).build();
			case SANP_240:
				PaGetPaymentRes paGetPaymentRes = JaxbUtils.toPaGetPaymentRes_RPT(leggiRptDTOResponse.getRpt().getXmlRpt(), false);
				if(retrocompatibilitaMessaggiPagoPAV1) {
					CtRichiestaPagamentoTelematico ctRpt2 = MessaggiPagoPAUtils.toCtRichiestaPagamentoTelematico(paGetPaymentRes, leggiRptDTOResponse.getRpt());
					this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName));
					return this.handleResponseOk(Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(ctRpt2),transactionId).build();
				} else {
					this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName));
					return this.handleResponseOk(Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(paGetPaymentRes.getData()),transactionId).build();
				}
			case SANP_321_V2:
				PaGetPaymentV2Response paGetPaymentV2Response = JaxbUtils.toPaGetPaymentV2Response_RPT(leggiRptDTOResponse.getRpt().getXmlRpt(), false);
				if(retrocompatibilitaMessaggiPagoPAV1) {
					CtRichiestaPagamentoTelematico ctRpt2 = MessaggiPagoPAUtils.toCtRichiestaPagamentoTelematico(paGetPaymentV2Response, leggiRptDTOResponse.getRpt());
					this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName));
					return this.handleResponseOk(Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(ctRpt2),transactionId).build();
				}else {
					this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName));
					return this.handleResponseOk(Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(paGetPaymentV2Response.getData()),transactionId).build();
				}
			}

			CtRichiestaPagamentoTelematico rpt = JaxbUtils.toRPT(leggiRptDTOResponse.getRpt().getXmlRpt(), false);
			this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName));
			return this.handleResponseOk(Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(rpt),transactionId).build();
		}else {
			switch (leggiRptDTOResponse.getRpt().getVersione()) {
			case SANP_230:
				this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName));
				return this.handleResponseOk(Response.status(Status.OK).type(MediaType.TEXT_XML).entity(leggiRptDTOResponse.getRpt().getXmlRpt()),transactionId).build();
			case SANP_240:
				if(retrocompatibilitaMessaggiPagoPAV1) {
					PaGetPaymentRes paGetPaymentRes = JaxbUtils.toPaGetPaymentRes_RPT(leggiRptDTOResponse.getRpt().getXmlRpt(), false);
					CtRichiestaPagamentoTelematico ctRpt2 = MessaggiPagoPAUtils.toCtRichiestaPagamentoTelematico(paGetPaymentRes, leggiRptDTOResponse.getRpt());
					this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName));
					return this.handleResponseOk(Response.status(Status.OK).type(MediaType.TEXT_XML).entity(JaxbUtils.toByte(ctRpt2)),transactionId).build();
				} else {
					this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName));
					return this.handleResponseOk(Response.status(Status.OK).type(MediaType.TEXT_XML).entity(leggiRptDTOResponse.getRpt().getXmlRpt()),transactionId).build();
				}
			case SANP_321_V2:
				if(retrocompatibilitaMessaggiPagoPAV1) {
					PaGetPaymentV2Response paGetPaymentV2Response = JaxbUtils.toPaGetPaymentV2Response_RPT(leggiRptDTOResponse.getRpt().getXmlRpt(), false);
					CtRichiestaPagamentoTelematico ctRpt2 = MessaggiPagoPAUtils.toCtRichiestaPagamentoTelematico(paGetPaymentV2Response, leggiRptDTOResponse.getRpt());
					this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName));
					return this.handleResponseOk(Response.status(Status.OK).type(MediaType.TEXT_XML).entity(JaxbUtils.toByte(ctRpt2)),transactionId).build();
				} else {
					this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName));
					return this.handleResponseOk(Response.status(Status.OK).type(MediaType.TEXT_XML).entity(leggiRptDTOResponse.getRpt().getXmlRpt()),transactionId).build();
				}
			}
			this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName));
			return this.handleResponseOk(Response.status(Status.OK).type(MediaType.TEXT_XML).entity(leggiRptDTOResponse.getRpt().getXmlRpt()),transactionId).build();
		}
	}

	public Response getRt(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , String idDominio, String iuv, String ccp, Boolean retrocompatibilitaMessaggiPagoPAV1) {
		String methodName = "getRt";
		String transactionId = ContextThreadLocal.get().getTransactionId();
		this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName));

		String accept = MediaType.APPLICATION_JSON;
		if(httpHeaders.getRequestHeaders().containsKey("Accept")) {
			accept = httpHeaders.getRequestHeaders().get("Accept").get(0).toLowerCase();
		}

		try{
			// autorizzazione sulla API
			this.isAuthorized(user, Arrays.asList(TIPO_UTENZA.OPERATORE, TIPO_UTENZA.APPLICAZIONE), Arrays.asList(Servizio.PENDENZE), Arrays.asList(Diritti.LETTURA));

			ValidatoreIdentificativi validatoreId = ValidatoreIdentificativi.newInstance();
			validatoreId.validaIdDominio("idDominio", idDominio);

			LeggiRicevutaDTO leggiPagamentoPortaleDTO = new LeggiRicevutaDTO(user);
			leggiPagamentoPortaleDTO.setIdDominio(idDominio);
			leggiPagamentoPortaleDTO.setIuv(iuv);
			ccp = ccp.contains("%") ? URLDecoder.decode(ccp,"UTF-8") : ccp;
			leggiPagamentoPortaleDTO.setCcp(ccp);

			// controllo che il dominio sia autorizzato
			if(!AuthorizationManager.isDominioAuthorized(user, idDominio)) {
				throw AuthorizationManager.toNotAuthorizedException(user,idDominio, null);
			}

			RptDAO ricevuteDAO = new RptDAO();

			LeggiRicevutaDTOResponse ricevutaDTOResponse = null;

			if(accept.toLowerCase().contains(MediaType.APPLICATION_OCTET_STREAM)) {
				leggiPagamentoPortaleDTO.setFormato(FormatoRicevuta.RAW);
				ricevutaDTOResponse = ricevuteDAO.leggiRt(leggiPagamentoPortaleDTO);

				// controllo che il dominio sia autorizzato
				if(!AuthorizationManager.isDominioAuthorized(user, ricevutaDTOResponse.getDominio().getCodDominio())) {
					throw AuthorizationManager.toNotAuthorizedException(user, ricevutaDTOResponse.getDominio().getCodDominio(), null);
				}

				this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName));
				return this.handleResponseOk(Response.status(Status.OK).type(MediaType.APPLICATION_OCTET_STREAM).entity(new String(ricevutaDTOResponse.getRpt().getXmlRt())),transactionId).build();
			} else {
				if(accept.toLowerCase().contains("application/pdf")) {
					leggiPagamentoPortaleDTO.setFormato(FormatoRicevuta.PDF);
					ricevutaDTOResponse = ricevuteDAO.leggiRt(leggiPagamentoPortaleDTO);
					String rtPdfEntryName = idDominio +"_"+ iuv + "_"+ ccp + ".pdf";
					byte[] b = ricevutaDTOResponse.getPdf();

					// controllo che il dominio sia autorizzato
					if(!AuthorizationManager.isDominioAuthorized(user, ricevutaDTOResponse.getDominio().getCodDominio())) {
						throw AuthorizationManager.toNotAuthorizedException(user, ricevutaDTOResponse.getDominio().getCodDominio(), null);
					}

					this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName));
					return this.handleResponseOk(Response.status(Status.OK).type("application/pdf").entity(b).header("content-disposition", "attachment; filename=\""+rtPdfEntryName+"\""),transactionId).build();
				} else if(accept.toLowerCase().contains(MediaType.APPLICATION_JSON)) {
					leggiPagamentoPortaleDTO.setFormato(FormatoRicevuta.JSON);
					ricevutaDTOResponse = ricevuteDAO.leggiRt(leggiPagamentoPortaleDTO);

					// controllo che il dominio sia autorizzato
					if(!AuthorizationManager.isDominioAuthorized(user, ricevutaDTOResponse.getDominio().getCodDominio())) {
						throw AuthorizationManager.toNotAuthorizedException(user, ricevutaDTOResponse.getDominio().getCodDominio(), null);
					}

					switch (ricevutaDTOResponse.getRpt().getVersione()) {
					case SANP_230:
						CtRicevutaTelematica rt = JaxbUtils.toRT(ricevutaDTOResponse.getRpt().getXmlRt(), false);
						this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName));
						return this.handleResponseOk(Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(rt),transactionId).build();
					case SANP_240:
						PaSendRTReq paSendRTReq = JaxbUtils.toPaSendRTReq_RT(ricevutaDTOResponse.getRpt().getXmlRt(), false);
						if(retrocompatibilitaMessaggiPagoPAV1) {
							CtRicevutaTelematica ctRt2 = MessaggiPagoPAUtils.toCtRicevutaTelematica(paSendRTReq, ricevutaDTOResponse.getRpt());
							this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName));
							this.handleResponseOk(Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(ctRt2),transactionId).build();
						} else { 
							this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName));
							return this.handleResponseOk(Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(paSendRTReq.getReceipt()),transactionId).build();
						}
					case SANP_321_V2:
						PaSendRTV2Request paSendRTV2Request = JaxbUtils.toPaSendRTV2Request_RT(ricevutaDTOResponse.getRpt().getXmlRt(), false);
						if(retrocompatibilitaMessaggiPagoPAV1) {
							CtRicevutaTelematica ctRt2 = MessaggiPagoPAUtils.toCtRicevutaTelematica(paSendRTV2Request, ricevutaDTOResponse.getRpt());
							this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName));
							return this.handleResponseOk(Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(ctRt2),transactionId).build();
						} else {
							this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName));
							return this.handleResponseOk(Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(paSendRTV2Request.getReceipt()),transactionId).build();
						}
					}

					CtRicevutaTelematica rt = JaxbUtils.toRT(ricevutaDTOResponse.getRpt().getXmlRt(), false);
					this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName));
					return this.handleResponseOk(Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(rt),transactionId).build();
				} else {
					leggiPagamentoPortaleDTO.setFormato(FormatoRicevuta.XML);
					ricevutaDTOResponse = ricevuteDAO.leggiRt(leggiPagamentoPortaleDTO);

					// controllo che il dominio sia autorizzato
					if(!AuthorizationManager.isDominioAuthorized(user, ricevutaDTOResponse.getDominio().getCodDominio())) {
						throw AuthorizationManager.toNotAuthorizedException(user, ricevutaDTOResponse.getDominio().getCodDominio(), null);
					}

					switch (ricevutaDTOResponse.getRpt().getVersione()) {
					case SANP_230:
						this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName));
						return this.handleResponseOk(Response.status(Status.OK).type(MediaType.TEXT_XML).entity(ricevutaDTOResponse.getRpt().getXmlRt()),transactionId).build();
					case SANP_240:
						if(retrocompatibilitaMessaggiPagoPAV1) {
							PaSendRTReq paSendRTReq = JaxbUtils.toPaSendRTReq_RT(ricevutaDTOResponse.getRpt().getXmlRt(), false);
							CtRicevutaTelematica ctRt2 = MessaggiPagoPAUtils.toCtRicevutaTelematica(paSendRTReq, ricevutaDTOResponse.getRpt());
							this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName));
							return this.handleResponseOk(Response.status(Status.OK).type(MediaType.TEXT_XML).entity(JaxbUtils.toByte(ctRt2)),transactionId).build();
						} else {
							this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName));
							return this.handleResponseOk(Response.status(Status.OK).type(MediaType.TEXT_XML).entity(ricevutaDTOResponse.getRpt().getXmlRt()),transactionId).build();
						}
					case SANP_321_V2:
						if(retrocompatibilitaMessaggiPagoPAV1) {
							PaSendRTV2Request paSendRTV2Request = JaxbUtils.toPaSendRTV2Request_RT(ricevutaDTOResponse.getRpt().getXmlRt(), false);
							CtRicevutaTelematica ctRt2 = MessaggiPagoPAUtils.toCtRicevutaTelematica(paSendRTV2Request, ricevutaDTOResponse.getRpt());
							this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName));
							return this.handleResponseOk(Response.status(Status.OK).type(MediaType.TEXT_XML).entity(JaxbUtils.toByte(ctRt2)),transactionId).build();
						} else {
							this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName));
							return this.handleResponseOk(Response.status(Status.OK).type(MediaType.TEXT_XML).entity(ricevutaDTOResponse.getRpt().getXmlRt()),transactionId).build();
						}
					}

					this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName));
					return this.handleResponseOk(Response.status(Status.OK).type(MediaType.TEXT_XML).entity(ricevutaDTOResponse.getRpt().getXmlRt()),transactionId).build();
				}
			}
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.logContext(ContextThreadLocal.get());
		}
	}
}
