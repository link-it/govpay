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
package it.govpay.test.web.business;

import it.govpay.bd.mail.MailBD;
import it.govpay.bd.model.Mail;
import it.govpay.bd.model.Mail.StatoSpedizione;
import it.govpay.bd.model.Mail.TipoMail;
import it.govpay.test.web.BasicTest;
import it.govpay.utils.GovPayConfiguration;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

@Test(groups = {"business", "mail"})
public class MailTest extends BasicTest {

	@BeforeClass
	public void setupDB() throws Exception {
		reInitDataBase();
	}

	@Test(groups = {"business", "mail"})
	public void notificaMail() throws Exception {
		
		MailBD mailBD = new MailBD(bd);
		Mail mail = new Mail();
		mail.setTipoMail(TipoMail.NOTIFICA_RPT);
		mail.setBundleKey(1l);
		mail.setMittente("info@link.it");
		mail.setDestinatario("info@link.it");
		mail.setOggetto("Email di prova per batch notifica");
		mail.setMessaggio("Email di prova per batch notifica");
		mail.setStatoSpedizione(StatoSpedizione.DA_SPEDIRE);
		
		mailBD.insertMail(mail);
		
		it.govpay.business.Mail not = new it.govpay.business.Mail(bd);
		for(int i = 0; i < GovPayConfiguration.newInstance().getMail_maxRetries() + 2; i++){
			not.notificaMail();
		}
		
		
		Mail mail2 = mailBD.getMail(mail.getId());
		Assert.assertEquals(mail2.getStatoSpedizione(), StatoSpedizione.SPEDITA, "Lo Stato Spedizione della mail non e' quello atteso dopo l'esecuzione del batch");
		
	}

	@Test(groups = {"business", "notificaMail"})
	public void notificaMailCasoErroreRispedizione() throws Exception {
		
		MailBD mailBD = new MailBD(bd);
		Mail mail = new Mail();
		mail.setTipoMail(TipoMail.NOTIFICA_RPT);
		mail.setBundleKey(1l);
		mail.setMittente("mailnonvalida");
		mail.setDestinatario("info@link.it");
		mail.setOggetto("Email di prova per batch notifica");
		mail.setMessaggio("Email di prova per batch notifica");
		mail.setStatoSpedizione(StatoSpedizione.DA_SPEDIRE);
		
		mailBD.insertMail(mail);
		
		it.govpay.business.Mail not = new it.govpay.business.Mail(bd);
		for(int i = 0; i < GovPayConfiguration.newInstance().getMail_maxRetries() + 2; i++){
			not.notificaMail();
		}
		
		
		Mail mail2 = mailBD.getMail(mail.getId());
		Assert.assertEquals(mail2.getTentativiRispedizione().intValue(), GovPayConfiguration.newInstance().getMail_maxRetries(), "I tentativi di rispedizione della mail non sono quelli attesi");
		Assert.assertEquals(mail2.getStatoSpedizione(), StatoSpedizione.IN_RISPEDIZIONE, "Lo Stato Spedizione della mail non e' quello atteso dopo l'esecuzione del batch");
	}

//	@Test(groups = {"business", "mail"})
//	public void inserisciMailDaTemplate() throws Exception {
//		setupEnte();
//		
//		String bodyRpt =  "Body con placeholder: ${importoTotale} ${IUV}";
//		String oggetto = "Oggetto senza placeholder.";
//		MailTemplateBD mailTemplateBD = new MailTemplateBD(bd);
//		MailTemplate mailTemplateRPT = new MailTemplate();
//		mailTemplateRPT.setMittente("info@link.it");
//		mailTemplateRPT.setTemplateOggetto(oggetto);
//		mailTemplateRPT.setTemplateMessaggio(bodyRpt);
//
//		mailTemplateBD.insertMailTemplate(mailTemplateRPT);
//
//		String bodyRt = "Body con placeholder: ${importoTotale} ${IUV} ${importoPagato} ${esito}";
//		MailTemplate mailTemplateRT = new MailTemplate();
//		mailTemplateRT.setMittente("info@link.it");
//		mailTemplateRT.setTemplateOggetto(oggetto);
//		mailTemplateRT.setTemplateMessaggio(bodyRt);
//		
//		mailTemplateBD.insertMailTemplate(mailTemplateRT);
//
//		enteAA.setIdMailTemplateRPT(mailTemplateRPT.getId());
//		enteAA.setIdMailTemplateRT(mailTemplateRT.getId());
//		
//		EntiBD enteBD = new EntiBD(bd);
//		
//		enteBD.updateEnte(enteAA);
//
//		RptPlaceholder rptP = new RptPlaceholder();
//		rptP.setImportoTotale("importoTotale");
//		rptP.setIUV("IUV");
//		
//		RtPlaceholder rtP = new RtPlaceholder();
//		rtP.setImportoTotale("importoTotale");
//		rtP.setIUV("IUV");
//		rtP.setEsito("esito");
//		rtP.setImportoPagato("importoPagato");
//		
//		
//		it.govpay.business.Mail mailBusiness = new it.govpay.business.Mail(bd);
//		
//		
//		
//		NotificaParameters paramsRPT = new NotificaParameters();
//		paramsRPT.setIdEnte(enteAA.getId());
//		paramsRPT.setDestinatario("info@link.it");
//		paramsRPT.setValori(rptP);
//		
//		mailBusiness.generaNotificaRPT(paramsRPT);
//		
//		NotificaParameters paramsRT = new NotificaParameters();
//		paramsRT.setEnte(enteAA);
//		paramsRT.setDestinatario("info@link.it");
//		paramsRT.setValori(rtP);
//		
//
//		mailBusiness.generaNotificaRT(paramsRT);
//		
//		MailBD mailBD = new MailBD(bd);
//		
//		List<Mail> findAllMail = mailBD.findAll(mailBD.newFilter());
//		
//		String bodyRptGenerato = bodyRpt.replace("$", "").replace("{", "").replace("}", "");
//		String bodyRtGenerato = bodyRt.replace("$", "").replace("{", "").replace("}", "");
//		
//		for(Mail mail: findAllMail) {
//			if(bodyRptGenerato.equals(mail.getMessaggio())) {
//				Assert.assertEquals(StatoSpedizione.DA_SPEDIRE, mail.getStatoSpedizione(), "Lo stato spedizione non e' quello atteso");
//				Assert.assertEquals(oggetto, mail.getOggetto(), "L'oggetto non e' quello atteso");
//			}
//			if(bodyRtGenerato.equals(mail.getMessaggio())) {
//				Assert.assertEquals(StatoSpedizione.DA_SPEDIRE, mail.getStatoSpedizione(), "Lo stato spedizione non e' quello atteso");
//				Assert.assertEquals(oggetto, mail.getOggetto(), "L'oggetto non e' quello atteso");
//			}
//		}
//		
//	}

}
