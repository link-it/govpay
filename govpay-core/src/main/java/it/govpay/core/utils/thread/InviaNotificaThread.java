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

import it.govpay.bd.BasicBD;
import it.govpay.bd.pagamento.NotificheBD;
import it.govpay.bd.model.Notifica;
import it.govpay.bd.model.Notifica.StatoSpedizione;
import it.govpay.core.exceptions.GovPayException;
import it.govpay.core.utils.client.BasicClient.ClientException;
import it.govpay.core.utils.client.NotificaClient;

import java.util.Date;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.openspcoop2.generic_project.exception.ServiceException;

public class InviaNotificaThread implements Runnable {

	private static Logger log = LogManager.getLogger();
	private Notifica notifica;
	private boolean completed = false;

	public InviaNotificaThread(Notifica notifica, BasicBD bd) throws ServiceException {
		// Verifico che tutti i campi siano valorizzati
		this.notifica = notifica;
		notifica.getApplicazione(bd);
		if(notifica.getIdRpt() != null) {
			notifica.getRpt(bd).getVersamento(bd);
			notifica.getRpt(bd).getCanale(bd);
			notifica.getRpt(bd).getPsp(bd);
			notifica.getRpt(bd).getPagamenti(bd);
		} else {
			notifica.getRr(bd);
			notifica.getRr(bd).getRpt(bd);
			notifica.getRr(bd).getRpt(bd).getVersamento(bd);
			notifica.getRr(bd).getRpt(bd).getCanale(bd);
			notifica.getRr(bd).getRpt(bd).getPsp(bd);
			notifica.getRr(bd).getRpt(bd).getPagamenti(bd);
		}
	}

	@Override
	public void run() {
		String codOperazione = UUID.randomUUID().toString();
		ThreadContext.put("op",  codOperazione);
		BasicBD bd = null;
		try {
			log.info("Spedizione della notifica [idNotifica: " + notifica.getId() +"] all'applicazione [CodApplicazione: " + notifica.getApplicazione(null).getCodApplicazione() + "]");
			NotificaClient client = new NotificaClient(notifica.getApplicazione(bd));
			client.invoke(notifica);
			notifica.setStato(StatoSpedizione.SPEDITO);
			notifica.setDescrizioneStato(null);
			notifica.setDataAggiornamento(new Date());
			bd = BasicBD.newInstance();
			NotificheBD notificheBD = new NotificheBD(bd);
			notificheBD.updateSpedito(notifica.getId());
			log.info("Notifica consegnata con successo");
		} catch(Exception e) {
			if(e instanceof GovPayException || e instanceof ClientException)
				log.error("Errore nella consegna della notifica: " + e);
			else
				log.error("Errore nella consegna della notifica", e);
			try {
				if(bd == null)
					bd = BasicBD.newInstance();
				long tentativi = notifica.getTentativiSpedizione() + 1;
				NotificheBD notificheBD = new NotificheBD(bd);
				Date prossima = new Date(new Date().getTime() + (tentativi * tentativi * 60 * 1000));
				notificheBD.updateDaSpedire(notifica.getId(), e.getMessage(), tentativi, prossima);
			} catch (Exception ee) {
				// Andato male l'aggiornamento. Non importa, verra' rispedito.
			}
		} finally {
			completed = true;
			if(bd != null){
				bd.closeConnection();
			}
		}
	}

	public boolean isCompleted() {
		return completed;
	}
}
