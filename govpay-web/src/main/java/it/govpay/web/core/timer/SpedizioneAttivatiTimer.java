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
package it.govpay.web.core.timer;

import java.util.List;

import javax.ejb.Schedule;
import javax.inject.Inject;
import javax.ejb.Singleton;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;

import it.govpay.ejb.core.ejb.AnagraficaEJB;
import it.govpay.ejb.core.exception.GovPayException;
import it.govpay.ejb.core.model.EnteCreditoreModel;
import it.govpay.ejb.ndp.controller.RptController;

@Singleton
public class SpedizioneAttivatiTimer {

	@Inject
	RptController rptCtrl;

	@Inject
	AnagraficaEJB anagraficaEjb;

	@Inject  
	private transient Logger log;

	@Schedule(hour="*", minute="*/1", persistent=false)
	public void doWork(){
		ThreadContext.put("proc", "SpedizioneAttivati");
    	ThreadContext.put("dom", null);
    	ThreadContext.put("iuv", null);
    	ThreadContext.put("ccp", null);
		//log.info("Attivazione SpedizioneAttivatiTimer.");

		// Per ciascun ente
		List<EnteCreditoreModel> enti = anagraficaEjb.getEntiCreditori();

		for(EnteCreditoreModel ente : enti) {
			//log.info("Gestione pagamenti attivati per il creditore " + ente.getDenominazione() + " ("+ ente.getIdEnteCreditore() + ")");
			try {
				rptCtrl.spedizioneAttivati(ente);
			} catch (GovPayException e) {
				log.error("Spedizione pagamenti attivati fallita: " + e.getTipoException() + ": " + e.getDescrizione(), e);
			} 
		}
	}
}