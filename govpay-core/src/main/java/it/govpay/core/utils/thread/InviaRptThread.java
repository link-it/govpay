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


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.logger.beans.Property;

import it.gov.digitpa.schemas._2011.ws.paa.FaultBean;
import it.govpay.bd.BasicBD;
import it.govpay.bd.pagamento.NotificheBD;
import it.govpay.bd.pagamento.RptBD;
import it.govpay.core.business.model.Risposta;
import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.GpThreadLocal;
import it.govpay.core.utils.RptUtils;
import it.govpay.core.utils.client.BasicClient.ClientException;
import it.govpay.core.utils.client.NodoClient.Azione;
import it.govpay.bd.model.Notifica;
import it.govpay.bd.model.Rpt;
import it.govpay.model.Notifica.TipoNotifica;
import it.govpay.model.Rpt.StatoRpt;

public class InviaRptThread implements Runnable {
	
	private Rpt rpt;
	private static Logger log = LogManager.getLogger();
	
	public InviaRptThread(Rpt rpt, BasicBD bd) throws ServiceException {
		this.rpt = rpt;
		this.rpt.getIntermediario(bd);
		this.rpt.getStazione(bd);
	}
	
	@Override
	public void run() {
		BasicBD bd = null;
		GpContext ctx = null;
		
		try {
			ctx = new GpContext(rpt.getIdTransazioneRpt());
			GpThreadLocal.set(ctx);
			ThreadContext.put("cmd", "InviaRptThread");
			ThreadContext.put("op", ctx.getTransactionId());
			
			ctx.setupNodoClient(this.rpt.getStazione(bd).getCodStazione(), rpt.getCodDominio(), Azione.nodoInviaRPT);
			
			log.info("Spedizione RPT al Nodo [CodMsgRichiesta: " + rpt.getCodMsgRichiesta() + "]");
			
			ctx.getContext().getRequest().addGenericProperty(new Property("codDominio", rpt.getCodDominio()));
			ctx.getContext().getRequest().addGenericProperty(new Property("iuv", rpt.getIuv()));
			ctx.getContext().getRequest().addGenericProperty(new Property("ccp", rpt.getCcp()));
			
			ctx.log("pagamento.invioRptAttivata");
				
			Risposta risposta = RptUtils.inviaRPT(rpt, bd);

			if(bd == null) {
				bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());
			}
			
			RptBD rptBD = new RptBD(bd);
			
			// Prima di procedere allo'aggiornamento dello stato verifico che nel frattempo non sia arrivato una RT
			this.rpt = rptBD.getRpt(rpt.getId());
			if(rpt.getStato().equals(StatoRpt.RT_ACCETTATA_PA)) {
				// E' arrivata l'RT nel frattempo. Non aggiornare.
				log.info("RPT inviata, ma nel frattempo e' arrivata l'RT. Non aggiorno lo stato");
				ctx.log("pagamento.invioRptAttivataRTricevuta");
				return;
			}
				
			
			if(!risposta.getEsito().equals("OK") && !risposta.getFaultBean(0).getFaultCode().equals("PPT_RPT_DUPLICATA")) {
				// RPT rifiutata dal Nodo
				// Aggiorno lo stato e loggo l'errore
				FaultBean fb = risposta.getFaultBean(0);
				String descrizione = null; 
				if(fb != null)
					descrizione = fb.getFaultCode() + ": " + fb.getFaultString();
				rptBD.updateRpt(rpt.getId(), StatoRpt.RPT_RIFIUTATA_NODO, descrizione, null, null);
				log.error("RPT rifiutata dal nodo con fault " + descrizione);
				ctx.log("pagamento.invioRptAttivataKo", fb.getFaultCode(), fb.getFaultString(), fb.getDescription() != null ? fb.getDescription() : "[-- Nessuna descrizione --]");
			} else {
				// RPT accettata dal Nodo
				// Invio la notifica e aggiorno lo stato
				Notifica notifica = new Notifica(rpt, TipoNotifica.ATTIVAZIONE, bd);
				NotificheBD notificheBD = new NotificheBD(bd);
				
				
				bd.setAutoCommit(false);
				rptBD.updateRpt(rpt.getId(), StatoRpt.RPT_ACCETTATA_NODO, null, null, null);
				notificheBD.insertNotifica(notifica);
				bd.commit();
				
				ThreadExecutorManager.getClientPoolExecutor().execute(new InviaNotificaThread(notifica, bd));
				log.info("RPT inviata correttamente al nodo");
				ctx.log("pagamento.invioRptAttivataOk");
			}
		} catch (ClientException e) {
			log.error("Errore nella spedizione della RPT", e);
			ctx.log("pagamento.invioRptAttivataFail", e.getMessage());
		} catch (Exception e) {
			log.warn("Errore interno nella spedizione della RPT: " + e);
			ctx.log("pagamento.invioRptAttivataFail", e.getMessage());
			if(bd != null) bd.rollback();
		} finally {
			if(ctx != null) ctx.log();
			if(bd != null) bd.closeConnection();
		}
	}
}
