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

import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.LoggerWrapperFactory;
import org.slf4j.Logger;

import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.anagrafica.BatchBD;
import it.govpay.core.utils.GovpayConfig;
import it.govpay.model.Batch;

public class BatchManager {

	private static Logger log = LoggerWrapperFactory.getLogger(BatchManager.class);

	public static boolean startEsecuzione(BDConfigWrapper configWrapper, String codBatch) throws ServiceException {
		log.debug("Verifico possibilita di avviare il batch " + codBatch);

		// Se non ho configurato l'id del cluster, non gestisco i blocchi.
		if(GovpayConfig.getInstance().getClusterId() == null) {
			log.debug("ClusterId non impostato. Gestione concorrenza non abilitata. Avvio batch consentito");
			return true;
		}
		BatchBD batchBD = null;

		try {
			batchBD = new BatchBD(configWrapper);

			// inizializzo connessione
			batchBD.setupConnection(configWrapper.getTransactionID());

			// imposto autocommit a false
			batchBD.setAutoCommit(false);

			// setto enableselectforupdate
			batchBD.enableSelectForUpdate();

			// gestione esplicita della chiusura della connessione
			batchBD.setAtomica(false); 

			// lettura stato batch
			Batch batch = getRunningBatch(batchBD, codBatch);

			if(batch == null) {
				// Batch libero. Procedo a configurare il blocco
				log.debug("Semaforo " + codBatch + " verde!!! Imposto rosso [" + GovpayConfig.getInstance().getClusterId() + "]");
				batch = new Batch();
				batch.setCodBatch(codBatch);
				batch.setInizio(new Date()); 
				batch.setNodo(GovpayConfig.getInstance().getClusterId());
				batch.setAggiornamento(null); 
				try {
					batchBD.update(batch);
					batchBD.commit();
					log.debug("Impostato semaforo rosso per il batch " + codBatch + " inserito per il nodo " + GovpayConfig.getInstance().getClusterId() + ".");
					return true;
				} catch (NotFoundException e) {
					batchBD.insert(batch);
					batchBD.commit();
					log.debug("Impostato semaforo rosso per il batch " + codBatch + " inserito per il nodo " + GovpayConfig.getInstance().getClusterId() + ".");
					return true;
				}
			} else {
				log.debug("Semaforo rosso impostato dal nodo [" + batch.getNodo() + "]. Esecuzione interrotta sul nodo [" + GovpayConfig.getInstance().getClusterId() + "]");
				batchBD.commit();
				return false;
			}
		} finally {
			if(batchBD != null) {
				try {
					// disabilito selectforupdate
					batchBD.disableSelectForUpdate();
				} catch (ServiceException e) {
					log.error("Errore " +e.getMessage() , e);
				}

				try {
					// reset autocommit
					batchBD.setAutoCommit(true);
				} catch (ServiceException e) {
					log.error("Errore " +e.getMessage() , e);
				}

				// chiusura connessione
				batchBD.closeConnection();
			}
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
				log.debug("Batch " + codBatch + " in esecuzione sul nodo " + batch.getNodo() + ".");
				return batch;
			}
		}
	}

	public static void stopEsecuzione(BDConfigWrapper configWrapper, String codBatch) {
		BatchBD batchBD = null;
		try {		
			// Se non ho configurato l'id del cluster, non gestisco i blocchi.
			if(GovpayConfig.getInstance().getClusterId() == null) {
				log.debug("ClusterId non impostato. Gestione concorrenza non abilitata. Rimozione semaforo inibita");
				return;
			}

			try {
				batchBD = new BatchBD(configWrapper);

				// inizializzo connessione
				batchBD.setupConnection(configWrapper.getTransactionID());

				// imposto autocommit a false
				batchBD.setAutoCommit(false);

				// setto enableselectforupdate
				batchBD.enableSelectForUpdate();

				// gestione esplicita della chiusura della connessione
				batchBD.setAtomica(false); 

				Batch batch = null;

				batch = batchBD.get(codBatch);

				// devo verificare che il blocco sia di mia proprieta'
				if(GovpayConfig.getInstance().getClusterId().equals(batch.getNodo())) {
					batch = new Batch();
					batch.setCodBatch(codBatch);
					batch.setInizio(null); 
					batch.setNodo(null);
					batch.setAggiornamento(null); 
					batchBD.update(batch);
					batchBD.commit();
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
		} catch(Throwable se) {
			log.error("Errore nella rimozione del semaforo di concorrenza per il batch " + codBatch, se);
		} finally {
			if(batchBD != null) {
				try {
					// disabilito selectforupdate
					batchBD.disableSelectForUpdate();
				} catch (ServiceException e) {
					log.error("Errore " +e.getMessage() , e);
				}

				try {
					// reset autocommit
					batchBD.setAutoCommit(true);
				} catch (ServiceException e) {
					log.error("Errore " +e.getMessage() , e);
				}

				// chiusura connessione
				batchBD.closeConnection();
			}
		}
	}


	public static void aggiornaEsecuzione(BDConfigWrapper configWrapper, String codBatch) throws ServiceException {
		log.debug("Aggiorno il batch " + codBatch);

		// Se non ho configurato l'id del cluster, non gestisco i blocchi.
		if(GovpayConfig.getInstance().getClusterId() == null) {
			log.debug("ClusterId non impostato. Gestione concorrenza non abilitata. Aggiornamento non necessario");
			return;
		}
		
		BatchBD batchBD = null;

		try {
			batchBD = new BatchBD(configWrapper);

			// inizializzo connessione
			batchBD.setupConnection(configWrapper.getTransactionID());

			// imposto autocommit a false
			batchBD.setAutoCommit(false);

			// setto enableselectforupdate
			batchBD.enableSelectForUpdate();

			// gestione esplicita della chiusura della connessione
			batchBD.setAtomica(false); 

			Batch batch = getRunningBatch(batchBD, codBatch);

			if(batch != null && GovpayConfig.getInstance().getClusterId() == batch.getNodo()) {
				batch.setAggiornamento(new Date()); 
				batchBD.update(batch);
				batchBD.commit();
				log.debug("Aggiornato semaforo rosso per il batch " + codBatch + " inserito per il nodo " + GovpayConfig.getInstance().getClusterId() + ".");
				return;
			}
		} catch (NotFoundException e) {
			log.error("Errore nell'aggiornamento del semaforo di concorrenza per il batch " + codBatch, e);
			return;
		}  finally {
			if(batchBD != null) {
				try {
					// disabilito selectforupdate
					batchBD.disableSelectForUpdate();
				} catch (ServiceException e) {
					log.error("Errore " +e.getMessage() , e);
				}

				try {
					// reset autocommit
					batchBD.setAutoCommit(true);
				} catch (ServiceException e) {
					log.error("Errore " +e.getMessage() , e);
				}

				// chiusura connessione
				batchBD.closeConnection();
			}
		}
	}
}
