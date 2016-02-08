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

import java.util.ArrayList;
import java.util.List;

import it.govpay.bd.mail.MailTemplateBD;
import it.govpay.bd.model.MailTemplate;
import it.govpay.bd.model.MailTemplate.TipoAllegati;
import it.govpay.test.BasicTest;

import org.testng.Assert;
import org.testng.annotations.Test;

public class MailTemplateTest extends BasicTest {
	
	@Test(groups = {"mail", "mailTemplate"})
	public void inserisciMailTemplate() throws Exception {
		MailTemplate mailTemplate = new MailTemplate();
		mailTemplate.setMittente("from@mittente.com");
		mailTemplate.setTemplateOggetto("Oggetto molto molto molto ${molto} molto molto molto molto molto molto molto molto molto molto molto molto molto molto molto lungo");
		mailTemplate.setTemplateMessaggio("Questo body è volutamente lungo e ripetuto per testare il ${database}. Questo body è volutamente lungo e ripetuto per testare il database. "
					+ "Questo body è volutamente lungo e ripetuto per testare il database. Questo body è volutamente lungo e ripetuto per testare il database. "
					+ "Questo body è volutamente lungo e ripetuto per testare il database. Questo body è volutamente lungo e ripetuto per testare il database. "
					+ "Questo body è volutamente lungo e ripetuto per testare il database. Questo body è volutamente lungo e ripetuto per testare il database. "
					+ "Questo body è volutamente lungo e ripetuto per testare il database. Questo body è volutamente lungo e ripetuto per testare il database.");
		
		List<TipoAllegati> allegati = new ArrayList<MailTemplate.TipoAllegati>();
		allegati.add(TipoAllegati.RPT);
		mailTemplate.setAllegati(allegati);
		MailTemplateBD mailTemplateBD = new MailTemplateBD(bd);
		mailTemplateBD.insertMailTemplate(mailTemplate);
		
		Assert.assertTrue(mailTemplate.getId() != null, "La mailTemplate inserita non ha l'id valorizzato");
		
		MailTemplate eventoLetto = mailTemplateBD.getMailTemplate(mailTemplate.getId());
		Assert.assertTrue(mailTemplate.equals(eventoLetto), "La mailTemplate letta e' diversa da quella inserita");
	}
}
