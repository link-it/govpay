/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2016 Link.it srl (http://www.link.it).
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
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
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.logger.beans.proxy.Service;

import it.gov.digitpa.schemas._2011.ws.paa.FaultBean;
import it.govpay.bd.BasicBD;
import it.govpay.bd.model.Notifica;
import it.govpay.bd.model.Notifica.TipoNotifica;
import it.govpay.bd.model.Rpt;
import it.govpay.bd.model.Rpt.StatoRpt;
import it.govpay.bd.pagamento.NotificheBD;
import it.govpay.bd.pagamento.RptBD;
import it.govpay.core.business.model.Risposta;
import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.GpThreadLocal;
import it.govpay.core.utils.RptUtils;

public class InviaRptThread implements Runnable {
	
	private Rpt rpt;
	private static Logger log = LogManager.getLogger();
	
	public InviaRptThread(Rpt rpt, BasicBD bd) throws ServiceException {
		this.rpt = rpt;
		try {
			this.rpt.getIntermediario(bd);
			this.rpt.getStazione(bd);
		} catch (NotFoundException e) {
			throw new ServiceException(e);
		}
	}
	
	@Override
	public void run() {
		BasicBD bd = null;
		GpContext ctx = null;
		
		try {
			ctx = new GpContext();
			ThreadContext.put("cmd", "InviaRptThread");
			ThreadContext.put("op", ctx.getTransactionId());
			Service service = new Service();
			service.setName("InviaRpt");
			ctx.getTransaction().setService(service);
			GpThreadLocal.set(ctx);
			
			log.info("Spedizione RPT al Nodo [CodMsgRichiesta: " + rpt.getCodMsgRichiesta() + "]");
			
			Risposta risposta = RptUtils.inviaRPT(rpt, bd);

			if(bd == null) {
				bd = BasicBD.newInstance();
			}
			
			RptBD rptBD = new RptBD(bd);
			if(!risposta.getEsito().equals("OK")) {
				// RPT rifiutata dal Nodo
				// Aggiorno lo stato e loggo l'errore
				FaultBean fb = risposta.getFaultBean(0);
				String descrizione = null; 
				if(fb != null)
					descrizione = fb.getFaultCode() + ": " + fb.getFaultString();
				rptBD.updateRpt(rpt.getId(), StatoRpt.RPT_RIFIUTATA_NODO, descrizione, null, null);
				log.error("RPT rifiutata dal nodo con fault " + descrizione);
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
			}
		} catch (Exception e) {
			// ERRORE DI RETE. Non so se la RPT e' stata effettivamente consegnata.
			log.error("Errore di rete nella spedizione della RPT: " + e);
			bd.rollback();
			bd.closeConnection();
			return;
		} finally {
			if(ctx != null) ctx.log();
		}
	}
}
