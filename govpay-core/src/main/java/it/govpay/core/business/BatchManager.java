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
package it.govpay.core.business;


import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.BatchBD;
import it.govpay.core.utils.GovpayConfig;
import it.govpay.model.Batch;

public class BatchManager {
	
	private static Logger log = LogManager.getLogger();

	public static boolean startEsecuzione(BasicBD bd, String codBatch) throws ServiceException {
		
		// Se non ho configurato l'id del cluster, non gestisco i blocchi.
		if(GovpayConfig.getInstance().getClusterId() == null) 
			return true;
		
		try {
			bd.setAutoCommit(false);
			bd.enableSelectForUpdate();
			
			BatchBD batchBD = new BatchBD(bd);
			
			Batch batch = getRunningBatch(batchBD, codBatch);
			
			if(batch == null) {
				// Batch libero. Procedo a configurare il blocco
				batch = new Batch();
				batch.setCodBatch(codBatch);
				batch.setInizio(new Date()); 
				batch.setNodo(GovpayConfig.getInstance().getClusterId());
				batch.setAggiornamento(null); 
				try {
					batchBD.update(batch);
					bd.commit();
					log.debug("Semaforo di concorrenza per il batch " + codBatch + " inserito per il nodo " + GovpayConfig.getInstance().getClusterId() + ".");
					return true;
				} catch (NotFoundException e) {
					batchBD.insert(batch);
					bd.commit();
					log.debug("Semaforo di concorrenza per il batch " + codBatch + " inserito per il nodo " + GovpayConfig.getInstance().getClusterId() + ".");
					return true;
				}
			} else {
				return false;
			}
		} finally {
			bd.setAutoCommit(true);
			bd.disableSelectForUpdate();
		}
	}
	
	private static Batch getRunningBatch(BatchBD batchBD, String codBatch) throws ServiceException {
		Batch batch = null;
		
		// Se non ho configurato l'id del cluster, non gestisco i blocchi.
		if(GovpayConfig.getInstance().getClusterId() == null) 
			return null;
		
		try{
			batch = batchBD.get(codBatch);
		} catch(NotFoundException nfe) {
			// Non c'e' un blocco, quindi non e' in esecuzione
			return null;
		}
		
		if(batch.getNodo() == null) {
			// Non c'e' un blocco, quindi non e' in esecuzione	
			return null;
		} else {
			// C'e' un blocco.
			// Verifico se e' scaduto
			long inizio = batch.getInizio().getTime();
			long aggiornamento = batch.getAggiornamento() != null ? batch.getAggiornamento().getTime() : inizio;
			
			long delay = new Date().getTime() - aggiornamento;
			
			if(delay > GovpayConfig.getInstance().getTimeoutBatch()) {
				log.warn("Individuato timeout del batch " + codBatch + ". La risorsa viene liberata per consentire l'esecuzione del batch.");
				return null;
			} else {
				log.debug("Batch in esecuzione sul nodo " + batch.getNodo() + ".");
				return batch;
			}
		}
	}

	public static void stopEsecuzione(BasicBD bd, String codBatch) {
		
		// Se non ho configurato l'id del cluster, non gestisco i blocchi.
		if(GovpayConfig.getInstance().getClusterId() == null) 
			return;
		try {
			try {
				bd.setAutoCommit(false);
				bd.enableSelectForUpdate();
				BatchBD batchBD = new BatchBD(bd);
				Batch batch = null;
				
				try {
					batch = batchBD.get(codBatch);
					
					// devo verificare che il blocco sia di proprieta' mia
					if(batch.getNodo().equals(GovpayConfig.getInstance().getClusterId())) {
						batch = new Batch();
						batch.setCodBatch(codBatch);
						batch.setInizio(null); 
						batch.setNodo(null);
						batch.setAggiornamento(null); 
						batchBD.update(batch);
						bd.commit();
						log.debug("Semaforo di concorrenza per il batch " + codBatch + " rimosso.");
					} else {
						// blocco non mio. lo lascio fare
						log.warn("Errore nella rimozione del semaforo di concorrenza per il batch " + codBatch + ": semaforo di altro nodo");
						return;
					}
				} catch (NotFoundException nfe) {
					// strano... vabeh...
					log.warn("Errore nella rimozione del semaforo di concorrenza per il batch " + codBatch + ": semaforo non presente");
					return;
				}
			} finally {
				bd.setAutoCommit(true);
				bd.disableSelectForUpdate();
			}
		} catch(ServiceException se) {
			log.error("Errore nella rimozione del semaforo di concorrenza per il batch " + codBatch, se);
		}
	}

}
