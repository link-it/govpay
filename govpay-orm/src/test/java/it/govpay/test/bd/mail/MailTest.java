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
package it.govpay.test.bd.mail;

import it.govpay.bd.mail.MailBD;
import it.govpay.bd.model.Mail;
import it.govpay.bd.model.Mail.StatoSpedizione;
import it.govpay.bd.model.Mail.TipoMail;
import it.govpay.test.BasicTest;

import java.util.ArrayList;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

public class MailTest extends BasicTest {
	
	@Test(groups = {"mail"})
	public void inserisciMail() throws Exception {
		Mail mail = new Mail();
		
		mail.setMittente("from@mittente.com");
		mail.setDestinatario("to@destinatario.com");
		mail.setTipoMail(TipoMail.NOTIFICA_RPT);
		mail.setBundleKey(1l);
		List<String> ccList = new ArrayList<String>();
		ccList.add("cc@destinatario.com");
		ccList.add("cc2@destinatario.com");
		mail.setCc(ccList);
		mail.setOggetto("Oggetto molto molto molto molto molto molto molto molto molto molto molto molto molto molto molto molto molto molto molto lungo");
		mail.setMessaggio("Questo body è volutamente lungo e ripetuto per testare il database. Questo body è volutamente lungo e ripetuto per testare il database. "
					+ "Questo body è volutamente lungo e ripetuto per testare il database. Questo body è volutamente lungo e ripetuto per testare il database. "
					+ "Questo body è volutamente lungo e ripetuto per testare il database. Questo body è volutamente lungo e ripetuto per testare il database. "
					+ "Questo body è volutamente lungo e ripetuto per testare il database. Questo body è volutamente lungo e ripetuto per testare il database. "
					+ "Questo body è volutamente lungo e ripetuto per testare il database. Questo body è volutamente lungo e ripetuto per testare il database.");
		mail.setStatoSpedizione(StatoSpedizione.DA_SPEDIRE);
		
		MailBD mailBD = new MailBD(bd);
		mailBD.insertMail(mail);
		
		Assert.assertTrue(mail.getId() != null, "La mail inserita non ha l'id valorizzato");
		
		Mail eventoLetto = mailBD.getMail(mail.getId());
		Assert.assertTrue(mail.equals(eventoLetto), "La mail letta e' diversa da quella inserita");
	}

	@Test(groups = {"mail"}, dependsOnMethods ={"inserisciMail"})
	public void cercaMail() throws Exception {
		Mail mail = new Mail();
		mail.setMittente("from@mittente.com");
		mail.setDestinatario("to@destinatario.com");
		mail.setTipoMail(TipoMail.NOTIFICA_RPT);
		mail.setBundleKey(1l);
		mail.setOggetto("Oggetto molto molto molto molto molto molto molto molto molto molto molto molto molto molto molto molto molto molto molto lungo");
		mail.setMessaggio("Questo body è volutamente lungo e ripetuto per testare il database. Questo body è volutamente lungo e ripetuto per testare il database. "
					+ "Questo body è volutamente lungo e ripetuto per testare il database. Questo body è volutamente lungo e ripetuto per testare il database. "
					+ "Questo body è volutamente lungo e ripetuto per testare il database. Questo body è volutamente lungo e ripetuto per testare il database. "
					+ "Questo body è volutamente lungo e ripetuto per testare il database. Questo body è volutamente lungo e ripetuto per testare il database. "
					+ "Questo body è volutamente lungo e ripetuto per testare il database. Questo body è volutamente lungo e ripetuto per testare il database.");
		mail.setStatoSpedizione(StatoSpedizione.DA_SPEDIRE);
		
		MailBD mailBD = new MailBD(bd);
		mailBD.insertMail(mail);
		
		List<Mail> mailLst = mailBD.findAll(mailBD.newFilter());
		Assert.assertTrue(mailLst.contains(mail), "La lista restituita non contiene la mail precedentemente inserita");
	}
}
