/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2015 Link.it srl (http://www.link.it).
 * Copyright (c) 2014-2015 TAS S.p.A. (http://www.tasgroup.it).
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
package it.govpay.web.timer;

import java.util.List;

import javax.ejb.Schedule;
import javax.inject.Inject;
import javax.ejb.Singleton;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;

import it.govpay.ejb.controller.AnagraficaEJB;
import it.govpay.ejb.controller.DistintaEJB;
import it.govpay.ejb.exception.GovPayException;
import it.govpay.ejb.model.EnteCreditoreModel;
import it.govpay.ejb.model.ScadenzarioModel;
import it.govpay.web.controller.PagamentiController;

@Singleton
public class NotificaTimer {

	@Inject
	PagamentiController pagamentiCtrl;

	@Inject
	AnagraficaEJB anagraficaEjb;

	@Inject
	DistintaEJB distintaEjb;

	@Inject  
	private transient Logger log;

	@Schedule(hour="*/4", persistent=false)
	public void doWork(){
		ThreadContext.put("proc", "SpedizioneNotifiche");
    	ThreadContext.put("dom", null);
    	ThreadContext.put("iuv", null);
    	ThreadContext.put("ccp", null);
		log.info("Attivazione del NotificaTimer.");

		// Per ciascun ente
		List<EnteCreditoreModel> enti = anagraficaEjb.getEntiCreditori();

		for(EnteCreditoreModel ente : enti) {
			ThreadContext.put("dom", ente.getIdFiscale());
			log.info("Spedizione delle Notifiche");
			
			List<ScadenzarioModel> scadenzari = anagraficaEjb.getScadenzari(ente.getIdEnteCreditore());
			for(ScadenzarioModel scadenzario : scadenzari) {
				try {
					pagamentiCtrl.inviaNotifiche(ente.getIdEnteCreditore(), scadenzario);
				} catch (GovPayException e) {
					log.error("Invio delle notifiche fallito: " + e.getTipoException() + ": " + e.getDescrizione(), e);
				}
			}
		}
	}

}