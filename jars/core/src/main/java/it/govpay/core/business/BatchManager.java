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


import java.text.MessageFormat;
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
		log.debug(MessageFormat.format("Verifico possibilita di avviare il batch {0}", codBatch));

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
				log.debug(MessageFormat.format("Semaforo {0} verde!!! Imposto rosso [{1}]", codBatch, GovpayConfig.getInstance().getClusterId()));
				batch = new Batch();
				batch.setCodBatch(codBatch);
				batch.setInizio(new Date()); 
				batch.setNodo(GovpayConfig.getInstance().getClusterId());
				batch.setAggiornamento(null); 
				try {
					batchBD.update(batch);
					batchBD.commit();
					log.debug(MessageFormat.format("Impostato semaforo rosso per il batch {0} inserito per il nodo {1}.", codBatch,	GovpayConfig.getInstance().getClusterId()));
					return true;
				} catch (NotFoundException e) {
					batchBD.insert(batch);
					batchBD.commit();
					log.debug(MessageFormat.format("Impostato semaforo rosso per il batch {0} inserito per il nodo {1}.", codBatch,	GovpayConfig.getInstance().getClusterId()));
					return true;
				}
			} else {
				log.debug(MessageFormat.format("Semaforo rosso impostato dal nodo [{0}]. Esecuzione interrotta sul nodo [{1}]",	batch.getNodo(), GovpayConfig.getInstance().getClusterId()));
				batchBD.commit();
				return false;
			}
		} finally {
			if(batchBD != null) {
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
			log.trace(MessageFormat.format("lettura batch {0} in corso...", codBatch));
			batch = batchBD.get(codBatch);
			log.trace(MessageFormat.format("lettura batch {0} completata", codBatch));
		} catch(NotFoundException nfe) {
			// Non c'e' un blocco, quindi non e' in esecuzione
			log.trace(MessageFormat.format("lettura batch {0} completata, blocco non trovato, batch non in esecuzione", codBatch));
			return null;
		}

		if(batch.getNodo() == null) {
			// Non c'e' un blocco, quindi non e' in esecuzione	
			log.trace(MessageFormat.format("lettura batch {0} blocco non trovato, batch non in esecuzione", codBatch));
			return null;
		} else {
			log.trace(MessageFormat.format("lettura batch {0} blocco presente, verifica timeout esecuzione", codBatch));
			// C'e' un blocco.
			// Verifico se e' scaduto
			long inizio = batch.getInizio().getTime();
			long aggiornamento = batch.getAggiornamento() != null ? batch.getAggiornamento().getTime() : inizio;

			long delay = new Date().getTime() - aggiornamento;

			if(delay > GovpayConfig.getInstance().getTimeoutBatch()) {
				log.warn(MessageFormat.format("Individuato timeout del batch {0}. La risorsa viene liberata per consentire l''esecuzione del batch.", codBatch));
				return null;
			} else {
				log.debug(MessageFormat.format("Batch {0} in esecuzione sul nodo {1}.", codBatch, batch.getNodo()));
				return batch;
			}
		}
	}

	public static void stopEsecuzione(BDConfigWrapper configWrapper, String codBatch) {
		BatchBD batchBD = null;
		log.debug("Stop esecuzione batch " + codBatch);
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
					log.debug(MessageFormat.format("Semaforo di concorrenza per il batch {0} rimosso.", codBatch));
				} else {
					// blocco non mio. lo lascio fare
					log.warn(MessageFormat.format("Errore nella rimozione del semaforo di concorrenza per il batch {0}: semaforo di altro nodo", codBatch));
					batchBD.commit();
					return;
				}
			} catch (NotFoundException nfe) {
				// strano... vabeh...
				log.debug(MessageFormat.format("stop esecuzione batch {0} errore nella rimozione del semaforo di concorrenza: semaforo non presente.",	codBatch));
				log.warn(MessageFormat.format("Errore nella rimozione del semaforo di concorrenza per il batch {0}: semaforo non presente",	codBatch));
				return;
			}
		} catch(Throwable se) {
			log.error(MessageFormat.format("Errore nella rimozione del semaforo di concorrenza per il batch {0}", codBatch), se);
		} finally {
			if(batchBD != null) {
				// chiusura connessione
				batchBD.closeConnection();
			}
		}
	}

	public static void aggiornaEsecuzione(BDConfigWrapper configWrapper, String codBatch) throws ServiceException {
		aggiornaEsecuzione(configWrapper, codBatch, null);
	}

	public static void aggiornaEsecuzione(BDConfigWrapper configWrapper, String codBatch, String msg) throws ServiceException {
		if(msg == null) {
			log.trace(MessageFormat.format("aggiorna esecuzione batch {0}", codBatch));
		} else {
			log.trace(MessageFormat.format("aggiorna esecuzione batch {0}: {1}", codBatch, msg));
		}

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

			if(batch != null && GovpayConfig.getInstance().getClusterId().equals(batch.getNodo())) {
				log.trace(MessageFormat.format("aggiorna esecuzione batch {0} aggiornamento in corso...", codBatch));
				batch.setAggiornamento(new Date()); 
				batchBD.update(batch);
				batchBD.commit();
				log.debug(MessageFormat.format("Aggiornato semaforo rosso per il batch {0} inserito per il nodo {1}.", codBatch, GovpayConfig.getInstance().getClusterId()));
				return;
			} else {
				log.trace(MessageFormat.format("aggiorna esecuzione batch {0}: non trovato eseguo solo il commit.", codBatch));
				batchBD.commit();
			}
		} catch (NotFoundException e) {
			log.error(MessageFormat.format("Errore nell''aggiornamento del semaforo di concorrenza per il batch {0}", codBatch), e);
			return;
		}  finally {
			if(batchBD != null) {
				// chiusura connessione
				batchBD.closeConnection();
			}
		}
	}
}
