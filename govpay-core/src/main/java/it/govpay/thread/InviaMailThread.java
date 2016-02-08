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
package it.govpay.thread;

import it.govpay.bd.BasicBD;
import it.govpay.bd.mail.MailBD;
import it.govpay.bd.model.Mail;
import it.govpay.bd.model.Mail.StatoSpedizione;
import it.govpay.bd.pagamento.TracciatiBD;
import it.govpay.web.wsclient.MailClient;

import java.util.Date;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;

public class InviaMailThread implements Runnable {

	private static Logger log = LogManager.getLogger();

	private Mail mail;
	private Map<String, String> context;

	public InviaMailThread(Mail mail) {
		this.mail = mail;
	}

	public InviaMailThread(Mail mail, Map<String, String> context) {
		this.mail = mail;
		this.context = context;
	}

	@Override
	public void run() {
		if(this.context != null)
			for(String key : this.context.keySet()) ThreadContext.put(key,  this.context.get(key));

		log.info("Spedisco la mail [IdMail: "+this.mail.getId()+"]");
		BasicBD bd = null;
		try {
			bd = BasicBD.newInstance();
		} catch (Exception e) {
			log.error("Impossibile stabilire una connessione con il database", e);
			return;
		}
		MailBD mailBD = null;
		TracciatiBD tracciatoBD = null;
		boolean inviato = false;
		try {
			bd.setAutoCommit(false);
			mailBD = new MailBD(bd);
			tracciatoBD = new TracciatiBD(bd);
			byte[] rpt = this.mail.getIdTracciatoRPT() != null ? tracciatoBD.getTracciato(this.mail.getIdTracciatoRPT()) : null;
			byte[] rt = this.mail.getIdTracciatoRT() != null ? tracciatoBD.getTracciato(this.mail.getIdTracciatoRT()) : null;
			
			MailClient client = new MailClient();
			try {
				client.send(this.mail, rpt, rt);
				inviato = true;
			} catch (Exception e) {
				log.error("Errore durante la spedizione della this.mail [idMail: " + this.mail.getId() + "]: " + e.getMessage(), e);
				return;
			}
		} catch(Exception e) {
			log.error("Errore durante la spedizione della this.mail [idMail: " + this.mail.getId() + "]: " + e.getMessage(), e);
		} finally {
			try {
				this.mail.setDataOraUltimaSpedizione(new Date());
				if(inviato) {
					this.mail.setStatoSpedizione(StatoSpedizione.SPEDITA);
				} else {
					this.mail.setStatoSpedizione(StatoSpedizione.IN_RISPEDIZIONE);
					
					Long tentativi = this.mail.getTentativiRispedizione() != null ? this.mail.getTentativiRispedizione() : 0;
					this.mail.setTentativiRispedizione(tentativi + 1);
					this.mail.setDataOraUltimaSpedizione(new Date());
				}
				mailBD.updateMail(this.mail);
				mailBD.commit();
			} catch (Exception e) {
				log.error("Errore durante la spedizione della this.mail [idMail: " + this.mail.getId() + "]: " + e.getMessage(), e);
			}
			if(mailBD != null){
				mailBD.closeConnection();
			}
		}
	}

}
