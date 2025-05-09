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
package it.govpay.core.utils.thread;


import java.text.MessageFormat;

import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.LoggerWrapperFactory;
import org.openspcoop2.utils.UtilsException;
import org.openspcoop2.utils.logger.beans.Property;
import org.openspcoop2.utils.service.context.ContextThreadLocal;
import org.openspcoop2.utils.service.context.IContext;
import org.openspcoop2.utils.service.context.MD5Constants;
import org.slf4j.Logger;
import org.slf4j.MDC;

import it.gov.pagopa.bizeventsservice.model.CtReceiptModelResponse;
import it.gov.pagopa.pagopa_api.pa.pafornode.PaSendRTV2Request;
import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.Rendicontazione;
import it.govpay.bd.model.SingoloVersamento;
import it.govpay.bd.model.Stazione;
import it.govpay.bd.model.Versamento;
import it.govpay.bd.pagamento.EventiBD;
import it.govpay.core.beans.EsitoOperazione;
import it.govpay.core.beans.EventoContext;
import it.govpay.core.beans.EventoContext.Componente;
import it.govpay.core.beans.EventoContext.Esito;
import it.govpay.core.exceptions.GovPayException;
import it.govpay.core.exceptions.IOException;
import it.govpay.core.exceptions.NdpException;
import it.govpay.core.exceptions.NdpException.FaultPa;
import it.govpay.core.utils.CtReceiptV2Converter;
import it.govpay.core.utils.CtReceiptV2Utils;
import it.govpay.core.utils.EventoUtils;
import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.client.RecuperaRTNodoClient;
import it.govpay.core.utils.client.exception.ClientException;
import it.govpay.core.utils.client.exception.ClientInitializeException;
import it.govpay.model.Intermediario;
import it.govpay.model.configurazione.Giornale;
import it.govpay.model.eventi.DatiPagoPA;

public class RecuperaRTThread implements Runnable {
	
	private static final String MSG_ERRORE_RICEVUTA_COD_DOMINIO_0_IUR_1_NON_RECUPERATA_2 = "Errore durante il recupero della ricevuta [CodDominio: {0}, IUV: {1}, IUR: {2}]: {3}";
	private static final String MSG_DIAGNOSTICO_RECUPERO_RT_RECUPERO_RT_AVVIO_KEY = "recuperoRT.avvioThread";
	private static final String MSG_DIAGNOSTICO_RECUPERO_RT_RECUPERO_RT_OK_KEY = "recuperoRT.Ok";
	private static final String MSG_DIAGNOSTICO_RECUPERO_RT_RECUPERO_RT_FAIL_KEY = "recuperoRT.Fail";

	private static final String ERROR_MSG_ERRORE_NEL_RECUPERO_RT_COD_DOMINIO_IUR = "Errore nel recupero della ricevuta [CodDominio: {0}, IUV: {1}, Iur: {2}]";
	private static final String ERROR_MSG_ERRORE_INIT_CLIENT_RECUPERO_RT_COD_DOMINIO_IUR = "Errore durante la init del client di recupero RT [CodDominio: {0}, IUV: {1}, Iur: {2}]";
	private static final String ERROR_MSG_ERRORE_DURANTE_IL_LOG_DELL_OPERAZIONE_0 = "Errore durante il log dell''operazione: {0}";
	
	private static Logger log = LoggerWrapperFactory.getLogger(RecuperaRTThread.class);
	private Rendicontazione rendicontazione;
	private Dominio dominio = null;
	private boolean completed = false;
	private boolean errore = false;
	private String esitoOperazione = null;
	
	private IContext ctx = null;
	private Giornale giornale;
	private Intermediario intermediario = null;
	private Stazione stazione = null;

	public RecuperaRTThread(Dominio dominio, Rendicontazione rendicontazione, IContext ctx) throws ServiceException, IOException {
		this.ctx = ctx;
		BDConfigWrapper configWrapper = new BDConfigWrapper(this.ctx.getTransactionId(), true);
		this.rendicontazione = rendicontazione;
		this.dominio = dominio;
		this.stazione = this.dominio.getStazione();
		this.intermediario = this.stazione.getIntermediario(configWrapper);
		this.giornale = new it.govpay.core.business.Configurazione().getConfigurazione().getGiornale();
	}

	@Override
	public void run() {
		ContextThreadLocal.set(this.ctx);
		IContext cctx = ContextThreadLocal.get();
		GpContext appContext = (GpContext) cctx.getApplicationContext();
		MDC.put(MD5Constants.TRANSACTION_ID, cctx.getTransactionId());
		RecuperaRTNodoClient client = null;
		BDConfigWrapper configWrapper = new BDConfigWrapper(this.ctx.getTransactionId(), true);
		EventoContext eventoCtx = new EventoContext(Componente.API_PAGOPA);
		String iur = this.rendicontazione.getIur();
		String iuv = this.rendicontazione.getIuv();
		String codDominio = this.dominio.getCodDominio();
		try {
			SingoloVersamento singoloVersamento = this.rendicontazione.getSingoloVersamento(configWrapper);
			if(singoloVersamento != null) {
				Versamento versamento = singoloVersamento.getVersamento(configWrapper);
				eventoCtx.setIdPendenza(versamento.getCodVersamentoEnte());
				eventoCtx.setIdA2A(versamento.getApplicazione(configWrapper).getCodApplicazione());
			}
			
			String operationId = appContext.setupNodoClient(this.stazione.getCodStazione(), codDominio, EventoContext.APIPAGOPA_TIPOEVENTO_GETORGANIZATIONRECEIPTIUR);
			log.info("Id Server: [{}]", operationId);
			log.info("Recupero RT da PagoPA [CodDominio: {}, IUV: {}, IUR: {}]", codDominio, iuv, iur);

			appContext.getServerByOperationId(operationId).addGenericProperty(new Property("codDominio", codDominio));
			appContext.getServerByOperationId(operationId).addGenericProperty(new Property("iuv", iuv));
			appContext.getServerByOperationId(operationId).addGenericProperty(new Property("ccp", iur));

			cctx.getApplicationLogger().log(MSG_DIAGNOSTICO_RECUPERO_RT_RECUPERO_RT_AVVIO_KEY, codDominio, iur);

			// salvataggio dati transazione
			eventoCtx.setCodDominio(codDominio);
			eventoCtx.setIuv(iuv);
			eventoCtx.setCcp(iur);

			RecuperaRTThread.popolaEventoCooperazione(this.dominio, this.intermediario, this.stazione, eventoCtx);
			
			client = new RecuperaRTNodoClient(this.intermediario, operationId, this.giornale, eventoCtx);
			
			CtReceiptModelResponse ctReceiptModelResponse = client.recuperaRT(codDominio, iur, cctx.getTransactionId(), EventoContext.APIPAGOPA_TIPOEVENTO_GETORGANIZATIONRECEIPTIUR);
			
			// Conversione risposta
			PaSendRTV2Request ctRt = CtReceiptV2Converter.toPaSendRTV2Request(this.intermediario.getCodIntermediario(), this.stazione.getCodStazione(), codDominio, ctReceiptModelResponse);

			// Acquisizione
			CtReceiptV2Utils.acquisisciRT(codDominio, iuv, ctRt , true);
			
			log.info("Recupero RT da PagoPA [CodDominio: {}, IUV: {}, IUR: {}], completato con successo", codDominio, iuv, iur);
			eventoCtx.setEsito(Esito.OK);
			this.esitoOperazione = "Ricevuta [CodDominio: "+codDominio+", IUR: "+iur+"] recuperata con successo.";
			cctx.getApplicationLogger().log(MSG_DIAGNOSTICO_RECUPERO_RT_RECUPERO_RT_OK_KEY);
		} catch (ClientException e) {
			this.errore = true;
			this.esitoOperazione = MessageFormat.format(MSG_ERRORE_RICEVUTA_COD_DOMINIO_0_IUR_1_NON_RECUPERATA_2, codDominio, iuv, iur, e.getMessage());
			log.error(MessageFormat.format(ERROR_MSG_ERRORE_NEL_RECUPERO_RT_COD_DOMINIO_IUR, codDominio, iuv, iur), e);
			if(client != null) {
				eventoCtx.setSottotipoEsito(e.getResponseCode() + "");
				eventoCtx.setEsito(Esito.FAIL);
				eventoCtx.setDescrizioneEsito(e.getMessage());
			}	
			try {
				cctx.getApplicationLogger().log(MSG_DIAGNOSTICO_RECUPERO_RT_RECUPERO_RT_FAIL_KEY, codDominio, iuv, iur, e.getMessage());
			} catch (UtilsException e1) {
				log.error(MessageFormat.format(ERROR_MSG_ERRORE_DURANTE_IL_LOG_DELL_OPERAZIONE_0, e.getMessage()), e);
			}
		} catch (NdpException e) {
			this.errore = true;
			this.esitoOperazione = MessageFormat.format(MSG_ERRORE_RICEVUTA_COD_DOMINIO_0_IUR_1_NON_RECUPERATA_2, codDominio, iuv, iur, e.getMessage());
			log.error(MessageFormat.format(ERROR_MSG_ERRORE_NEL_RECUPERO_RT_COD_DOMINIO_IUR, codDominio, iuv, iur), e);
			if(eventoCtx != null) {
				if(e.getFaultCode().equals(FaultPa.PAA_SYSTEM_ERROR.name())) {
					eventoCtx.setEsito(Esito.FAIL);
				} else { 
					eventoCtx.setEsito(Esito.KO);
				}
				eventoCtx.setDescrizioneEsito(e.getDescrizione());
				eventoCtx.setSottotipoEsito(e.getFaultCode());
				eventoCtx.setException(e);
			}	
			try {
				cctx.getApplicationLogger().log(MSG_DIAGNOSTICO_RECUPERO_RT_RECUPERO_RT_FAIL_KEY, codDominio, iuv, iur, e.getMessage());
			} catch (UtilsException e1) {
				log.error(MessageFormat.format(ERROR_MSG_ERRORE_DURANTE_IL_LOG_DELL_OPERAZIONE_0, e.getMessage()), e);
			}
		} catch (ServiceException | GovPayException | UtilsException | IOException e) {
			this.errore = true;
			this.esitoOperazione = MessageFormat.format(MSG_ERRORE_RICEVUTA_COD_DOMINIO_0_IUR_1_NON_RECUPERATA_2, codDominio, iuv, iur, e.getMessage());
			log.error(MessageFormat.format(ERROR_MSG_ERRORE_NEL_RECUPERO_RT_COD_DOMINIO_IUR, codDominio, iuv, iur), e);
			if(client != null) {
				if(e instanceof GovPayException) {
					eventoCtx.setSottotipoEsito(((GovPayException)e).getCodEsito().toString());
				} else {
					eventoCtx.setSottotipoEsito(EsitoOperazione.INTERNAL.toString());
				}
				eventoCtx.setEsito(Esito.FAIL);
				eventoCtx.setDescrizioneEsito(e.getMessage());
				eventoCtx.setException(e);
			}	
			try {
				cctx.getApplicationLogger().log(MSG_DIAGNOSTICO_RECUPERO_RT_RECUPERO_RT_FAIL_KEY, codDominio, iuv, iur, e.getMessage());
			} catch (UtilsException e1) {
				log.error(MessageFormat.format(ERROR_MSG_ERRORE_DURANTE_IL_LOG_DELL_OPERAZIONE_0, e.getMessage()), e);
			}
		} catch (ClientInitializeException e) {
			this.errore = true;
			this.esitoOperazione = MessageFormat.format(MSG_ERRORE_RICEVUTA_COD_DOMINIO_0_IUR_1_NON_RECUPERATA_2, codDominio, iuv, iur, e.getMessage());
			log.error(MessageFormat.format(ERROR_MSG_ERRORE_INIT_CLIENT_RECUPERO_RT_COD_DOMINIO_IUR, codDominio, iuv, iur), e);
			if(eventoCtx != null) {
				eventoCtx.setSottotipoEsito(EsitoOperazione.INTERNAL.toString());
				eventoCtx.setEsito(Esito.FAIL);
				eventoCtx.setDescrizioneEsito(e.getMessage());
				eventoCtx.setException(e);
			}	
			try {
				cctx.getApplicationLogger().log(MSG_DIAGNOSTICO_RECUPERO_RT_RECUPERO_RT_FAIL_KEY, codDominio, iuv, iur, e.getMessage());
			} catch (UtilsException e1) {
				log.error(MessageFormat.format(ERROR_MSG_ERRORE_DURANTE_IL_LOG_DELL_OPERAZIONE_0, e.getMessage()), e);
			}
		} finally {
			if(eventoCtx != null && eventoCtx.isRegistraEvento()) {
				try {
					EventiBD eventiBD = new EventiBD(configWrapper);
					eventiBD.insertEvento(EventoUtils.toEventoDTO(eventoCtx,log));

				} catch (ServiceException e) {
					log.error("Errore: " + e.getMessage(), e);
				}
			}
			this.completed = true;
			ContextThreadLocal.unset();
		}
	}
	
	public boolean isCompleted() {
		return this.completed;
	}
	
	public boolean isErrore() {
		return this.errore;
	}
	
	public String getEsitoOperazione() {
		return esitoOperazione;
	}
	
	public static void popolaEventoCooperazione(Dominio dominio, Intermediario intermediario, Stazione stazione, EventoContext eventoContext) {
		DatiPagoPA datiPagoPA = new DatiPagoPA();
		datiPagoPA.setCodStazione(stazione.getCodStazione());
		datiPagoPA.setCodIntermediario(intermediario.getCodIntermediario());
		datiPagoPA.setErogatore(it.govpay.model.Evento.NDP);
		datiPagoPA.setFruitore(intermediario.getCodIntermediario());
		datiPagoPA.setCodDominio(dominio.getCodDominio());
		eventoContext.setDatiPagoPA(datiPagoPA);
	}
}
