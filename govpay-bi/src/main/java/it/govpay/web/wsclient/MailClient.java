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
package it.govpay.web.wsclient;

import it.govpay.bd.model.Mail;
import it.govpay.exception.GovPayException;
import it.govpay.utils.GovPayConfiguration;

import org.openspcoop2.utils.UtilsException;
import org.openspcoop2.utils.mail.MailAttach;
import org.openspcoop2.utils.mail.MailBinaryAttach;
import org.openspcoop2.utils.mail.Sender;
import org.openspcoop2.utils.mail.SenderFactory;
import org.openspcoop2.utils.mail.SenderType;

public class MailClient {

	private Sender senderCommonsMail;
	
	public MailClient() {
		this.senderCommonsMail = SenderFactory.newSender(SenderType.COMMONS_MAIL, org.apache.log4j.Logger.getLogger(MailClient.class));
		this.senderCommonsMail.setConnectionTimeout(100);
		this.senderCommonsMail.setReadTimeout(5 * 1000);
	}
	public void send(Mail mailToSend, byte[] rpt, byte[] rt) throws UtilsException, GovPayException {

		GovPayConfiguration gpConfig = GovPayConfiguration.newInstance();
		org.openspcoop2.utils.mail.Mail mail = new org.openspcoop2.utils.mail.Mail();
		mail.setServerHost(gpConfig.getMail_serverHost());
		mail.setServerPort(gpConfig.getMail_serverPort());
		mail.setUsername(gpConfig.getMail_username());
		mail.setPassword(gpConfig.getMail_serverHost());
		mail.setStartTls(false);
		
		mail.setFrom(mailToSend.getMittente());
		mail.setTo(mailToSend.getDestinatario());
		if(mailToSend.getCc() != null && !mailToSend.getCc().isEmpty()) {
			mail.setCc(mailToSend.getCc());
		}
		
		mail.setSubject(mailToSend.getOggetto());
		mail.getBody().setMessage(mailToSend.getMessaggio());
		
		if(rpt != null) {
			MailAttach mailAttach = new MailBinaryAttach("tracciatoRPT.xml", rpt);
			mail.getBody().getAttachments().add(mailAttach);
		}
		if(rt != null) {
			MailAttach mailAttach = new MailBinaryAttach("tracciatoRT.xml", rt);
			mail.getBody().getAttachments().add(mailAttach);
		}
		senderCommonsMail.send(mail, true); 

	}
}
