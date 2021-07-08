/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2017 Link.it srl (http://www.link.it).
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


import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.LoggerWrapperFactory;
import org.openspcoop2.utils.UtilsException;
import org.openspcoop2.utils.logger.beans.Property;
import org.openspcoop2.utils.service.context.ContextThreadLocal;
import org.openspcoop2.utils.service.context.IContext;
import org.openspcoop2.utils.service.context.MD5Constants;
import org.slf4j.Logger;
import org.slf4j.MDC;

import gov.telematici.pagamenti.ws.rpt.FaultBean;
import gov.telematici.pagamenti.ws.rpt.NodoInviaRPT;
import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.configurazione.model.Giornale;
import it.govpay.bd.model.Applicazione;
import it.govpay.bd.model.Notifica;
import it.govpay.bd.model.PagamentoPortale;
import it.govpay.bd.model.Rpt;
import it.govpay.bd.model.Stazione;
import it.govpay.bd.model.Versamento;
import it.govpay.bd.pagamento.EventiBD;
import it.govpay.bd.pagamento.RptBD;
import it.govpay.core.beans.EsitoOperazione;
import it.govpay.core.business.model.Risposta;
import it.govpay.core.exceptions.GovPayException;
import it.govpay.core.utils.EventoContext.Esito;
import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.RptUtils;
import it.govpay.core.utils.client.exception.ClientException;
import it.govpay.core.utils.client.NodoClient;
import it.govpay.core.utils.client.NodoClient.Azione;
import it.govpay.model.Intermediario;
import it.govpay.model.Notifica.TipoNotifica;
import it.govpay.model.Rpt.StatoRpt;

public class InviaRptThread implements Runnable {

	private Rpt rpt;
	private static Logger log = LoggerWrapperFactory.getLogger(InviaRptThread.class);
	private IContext ctx = null;
	private Giornale giornale;
	private Intermediario intermediario = null;
	private Stazione stazione = null;
	private Applicazione applicazione = null;
	private PagamentoPortale pagamentoPortale = null;
	private Versamento versamento = null;

	public InviaRptThread(Rpt rpt, IContext ctx) throws ServiceException {
		this.ctx = ctx;
		BDConfigWrapper configWrapper = new BDConfigWrapper(this.ctx.getTransactionId(), true);
		this.rpt = rpt;
		this.intermediario = this.rpt.getIntermediario(configWrapper);
		this.stazione = this.rpt.getStazione(configWrapper);
		this.giornale = new it.govpay.core.business.Configurazione().getConfigurazione().getGiornale();
		this.versamento = this.rpt.getVersamento(configWrapper);
		this.applicazione = this.versamento.getApplicazione(configWrapper);
		this.pagamentoPortale = this.rpt.getPagamentoPortale(configWrapper);
	}

	@Override
	public void run() {
		ContextThreadLocal.set(this.ctx);
		//		BasicBD bd = null;
		IContext ctx = ContextThreadLocal.get();
		GpContext appContext = (GpContext) ctx.getApplicationContext();
		MDC.put(MD5Constants.TRANSACTION_ID, ctx.getTransactionId());
		NodoClient client = null;
		BDConfigWrapper configWrapper = new BDConfigWrapper(this.ctx.getTransactionId(), true);
		RptBD rptBD = null;
		try {
			String operationId = appContext.setupNodoClient(this.stazione.getCodStazione(), this.rpt.getCodDominio(), Azione.nodoInviaRPT);
			log.info("Id Server: [" + operationId + "]");
			log.info("Spedizione RPT al Nodo [CodMsgRichiesta: " + this.rpt.getCodMsgRichiesta() + "]");

			appContext.getServerByOperationId(operationId).addGenericProperty(new Property("codDominio", this.rpt.getCodDominio()));
			appContext.getServerByOperationId(operationId).addGenericProperty(new Property("iuv", this.rpt.getIuv()));
			appContext.getServerByOperationId(operationId).addGenericProperty(new Property("ccp", this.rpt.getCcp()));

			ctx.getApplicationLogger().log("pagamento.invioRptAttivata");

			client = new it.govpay.core.utils.client.NodoClient(this.intermediario, operationId, this.giornale);
			// salvataggio id Rpt/ versamento/ pagamento
			client.getEventoCtx().setCodDominio(this.rpt.getCodDominio());
			client.getEventoCtx().setIuv(this.rpt.getIuv());
			client.getEventoCtx().setCcp(this.rpt.getCcp());
			client.getEventoCtx().setIdA2A(this.applicazione.getCodApplicazione());
			client.getEventoCtx().setIdPendenza(this.versamento.getCodVersamentoEnte());
			if(this.pagamentoPortale != null)
				client.getEventoCtx().setIdPagamento(this.pagamentoPortale.getIdSessione());

			RptUtils.popolaEventoCooperazione(client, this.rpt, this.intermediario, this.stazione);

			NodoInviaRPT inviaRPT = new NodoInviaRPT();
			inviaRPT.setIdentificativoCanale(this.rpt.getCodCanale());
			inviaRPT.setIdentificativoIntermediarioPSP(this.rpt.getCodIntermediarioPsp());
			inviaRPT.setIdentificativoPSP(this.rpt.getCodPsp());
			inviaRPT.setPassword(this.stazione.getPassword());
			inviaRPT.setRpt(this.rpt.getXmlRpt());

			Risposta risposta = new it.govpay.core.business.model.Risposta(client.nodoInviaRPT(this.intermediario, this.stazione, this.rpt, inviaRPT)); 

			rptBD = new RptBD(configWrapper);

			// Prima di procedere allo'aggiornamento dello stato verifico che nel frattempo non sia arrivato una RT
			this.rpt = rptBD.getRpt(this.rpt.getId(), true);
			if(this.rpt.getStato().equals(StatoRpt.RT_ACCETTATA_PA)) {
				// E' arrivata l'RT nel frattempo. Non aggiornare.
				log.info("RPT inviata, ma nel frattempo e' arrivata l'RT. Non aggiorno lo stato");
				ctx.getApplicationLogger().log("pagamento.invioRptAttivataRTricevuta");
				return;
			}
			
			// riapro la connessione
			rptBD.setupConnection(configWrapper.getTransactionID());
			
			rptBD.setAtomica(false); // connessione deve restare aperta

			if(!risposta.getEsito().equals("OK") && !risposta.getFaultBean().getFaultCode().equals("PPT_RPT_DUPLICATA")) {
				// RPT rifiutata dal Nodo
				// Loggo l'errore ma lascio lo stato invariato. 
				// v3.1: Perche' non cambiare lo stato a fronte di un rifiuto? Lo aggiorno e evito la rispedizione.
				// Redo: Perche' e' difficile capire se e' un errore temporaneo o meno. Essendo un'attivazione di RPT, non devo smettere di riprovare.
				FaultBean fb = risposta.getFaultBean();
				String descrizione = null; 
				if(fb != null)
					descrizione = fb.getFaultCode() + ": " + fb.getFaultString();
				rptBD.updateRpt(this.rpt.getId(), null, descrizione, null, null,null);
				log.warn("RPT rifiutata dal nodo con fault " + descrizione);
				ctx.getApplicationLogger().log("pagamento.invioRptAttivataKo", fb.getFaultCode(), fb.getFaultString(), fb.getDescription() != null ? fb.getDescription() : "[-- Nessuna descrizione --]");
				if(client != null) {
					client.getEventoCtx().setSottotipoEsito(fb.getFaultCode());
					client.getEventoCtx().setEsito(Esito.KO);
					client.getEventoCtx().setDescrizioneEsito(descrizione);
				}
			} else {
				// RPT accettata dal Nodo
				// Invio la notifica e aggiorno lo stato
				Notifica notifica = new Notifica(this.rpt, TipoNotifica.ATTIVAZIONE, configWrapper);
				it.govpay.core.business.Notifica notificaBD = new it.govpay.core.business.Notifica();


				rptBD.setAutoCommit(false);
				
				rptBD.updateRpt(this.rpt.getId(), StatoRpt.RPT_ACCETTATA_NODO, null, null, null,null);
				boolean schedulaThreadInvio = notificaBD.inserisciNotifica(notifica, rptBD);
				
				rptBD.commit();
				
				rptBD.setAutoCommit(true);

				if(schedulaThreadInvio)
					ThreadExecutorManager.getClientPoolExecutorNotifica().execute(new InviaNotificaThread(notifica, ctx));
				log.info("RPT inviata correttamente al nodo");
				ctx.getApplicationLogger().log("pagamento.invioRptAttivataOk");
				client.getEventoCtx().setEsito(Esito.OK);
			}
		} catch (ClientException e) {
			log.error("Errore nella spedizione della RPT", e);
			if(client != null) {
				client.getEventoCtx().setSottotipoEsito(e.getResponseCode() + "");
				client.getEventoCtx().setEsito(Esito.FAIL);
				client.getEventoCtx().setDescrizioneEsito(e.getMessage());
			}	
			try {
				ctx.getApplicationLogger().log("pagamento.invioRptAttivataFail", e.getMessage());
			} catch (UtilsException e1) {
				log.error("Errore durante il log dell'operazione: " + e.getMessage(), e);
			}
		} catch (NotFoundException | ServiceException | GovPayException | UtilsException e) {
			log.error("Errore nella spedizione della RPT", e);
			if(client != null) {
				if(e instanceof GovPayException) {
					client.getEventoCtx().setSottotipoEsito(((GovPayException)e).getCodEsito().toString());
				} else {
					client.getEventoCtx().setSottotipoEsito(EsitoOperazione.INTERNAL.toString());
				}
				client.getEventoCtx().setEsito(Esito.FAIL);
				client.getEventoCtx().setDescrizioneEsito(e.getMessage());
				client.getEventoCtx().setException(e);
			}	
			try {
				ctx.getApplicationLogger().log("pagamento.invioRptAttivataFail", e.getMessage());
			} catch (UtilsException e1) {
				log.error("Errore durante il log dell'operazione: " + e.getMessage(), e);
			}
			try {
			if(rptBD != null && !rptBD.isClosed() && !rptBD.isAutoCommit()) 
				rptBD.rollback();
			} catch (ServiceException e1) {
				log.error("Errore: " + e1.getMessage(), e1);
			}
		} finally {
			if(rptBD != null)
				rptBD.closeConnection();

			if(client != null && client.getEventoCtx().isRegistraEvento()) {
				try {
					EventiBD eventiBD = new EventiBD(configWrapper);
					eventiBD.insertEvento(client.getEventoCtx().toEventoDTO());

				} catch (ServiceException e) {
					log.error("Errore: " + e.getMessage(), e);
				}
			}
			ContextThreadLocal.unset();
		}
	}
}
