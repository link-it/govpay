/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2015 Link.it srl (http://www.link.it).
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
package it.govpay.batch;

import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.bd.model.EsitoBase;
import it.govpay.bd.pagamento.EsitiBD;
import it.govpay.exception.GovPayException;
import it.govpay.exception.GovPayException.GovPayExceptionEnum;
import it.govpay.thread.InviaEsitoThread;
import it.govpay.utils.GovPayConfiguration;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.ejb.Schedule;
import javax.ejb.Singleton;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;

@Singleton
public class SpedizioneEsiti {
	
	Logger log = LogManager.getLogger();
	
	@Schedule(hour="*", minute="*/5", persistent=true)
	public void doWork(){
		ThreadContext.put("cmd", "SpedizioneEsiti");
		ThreadContext.put("op", UUID.randomUUID().toString() );
	
		BasicBD bd = null;
		try {
			bd = BasicBD.getInstance();
			GovPayConfiguration properties = GovPayConfiguration.newInstance();
			log.trace("Spedizione degli esiti (max " + properties.getEsiti_limit() + " esiti)" );
			try {
				EsitiBD esitiBD = new EsitiBD(bd);
				
				List<EsitoBase> esiti = esitiBD.findEsitiDaSpedire(0, properties.getEsiti_limit());
				if(esiti.size() == 0) return;
				
				log.info("Trovati ["+esiti.size()+"] esiti da spedire");
				int threadPoolSize = GovPayConfiguration.newInstance().getThreadPoolSize();
				
				ExecutorService executor = Executors.newFixedThreadPool(threadPoolSize);
				for(EsitoBase esito: esiti) {
					InviaEsitoThread sender = new InviaEsitoThread(AnagraficaManager.getApplicazione(bd, esito.getIdApplicazione()), esito.getId());
					executor.execute(sender);
				}
				executor.shutdown();
				while (!executor.isTerminated()) {
					Thread.sleep(500);
				}
			} catch (Exception se) {
				if(bd != null) {
					bd.rollback();
				}
				throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO, "Non è stato possibile spedire gli esiti: " + se.getMessage(), se);
			}

			
		} catch (Exception e) {
			log.info("Spedizione degli esiti fallita", e);
		} finally {
			if(bd != null) bd.closeConnection();
		}
	}
}