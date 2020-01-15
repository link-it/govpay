package it.govpay.core.utils.appio;

import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import org.openspcoop2.utils.service.context.ContextThreadLocal;
import org.slf4j.Logger;

import it.govpay.bd.BasicBD;
import it.govpay.bd.configurazione.model.MessageAppIO;
import it.govpay.bd.model.Versamento;
import it.govpay.core.beans.EsitoOperazione;
import it.govpay.core.exceptions.GovPayException;
import it.govpay.core.utils.appio.model.MessageContent;
import it.govpay.core.utils.appio.model.NewMessage;
import it.govpay.core.utils.appio.model.PaymentData;
//import it.govpay.core.utils.appio.model_old.MessageContent;
//import it.govpay.core.utils.appio.model_old.MessageWithCF;
//import it.govpay.core.utils.appio.model_old.PaymentData;
import it.govpay.core.utils.trasformazioni.TrasformazioniUtils;
import it.govpay.core.utils.trasformazioni.exception.TrasformazioneException;

public class AppIOUtils {

	public static NewMessage getPostMessage(Logger log, MessageAppIO appIOMessage, Versamento versamento, BasicBD bd) throws GovPayException {
		NewMessage message = new NewMessage();
		
		if(appIOMessage.getTimeToLive() != null)
			message.setTimeToLive(appIOMessage.getTimeToLive().intValue());
		
		message.setFiscalCode(versamento.getAnagraficaDebitore().getCodUnivoco());
		MessageContent content = new MessageContent();
		
		String subject = trasformazioneSubject(log, versamento, appIOMessage.getTipo(), appIOMessage.getSubject());
		String markdown = trasformazioneMarkdown(log, versamento, appIOMessage.getTipo(), appIOMessage.getBody());
		
		content.setSubject(subject);
		content.setMarkdown(markdown);
		
//		if(versamento.getDataScadenza() != null) { TODO
//			content.setDueDate(versamento.getDataScadenza());
//		} else if(versamento.getDataValidita() != null) {
//			content.setDueDate(versamento.getDataValidita());
//		} else {
//			// do nothing
//		}
		boolean invalid_after_due_date = false;
//		if(content.getDueDate() != null && versamento.getDataScadenza() != null) {
//			invalid_after_due_date = content.getDueDate().getTime() == versamento.getDataScadenza().getTime();
//		}
		
		PaymentData payment_data = new PaymentData();
		
		// importo in centesimi
		int amount = versamento.getImportoTotale().intValue() * 100;
		payment_data.setAmount(amount);
		payment_data.setInvalidAfterDueDate(invalid_after_due_date);
		payment_data.setNoticeNumber(versamento.getNumeroAvviso());
				
		content.setPaymentData(payment_data );
				
		message.setContent(content);
		
		return message;
	}
	
	
	public static String trasformazioneSubject(Logger log, Versamento versamento, String tipoTemplate, String templateTrasformazione) throws GovPayException {
		log.debug("Generazione del Subject della Notifica AppIO tramite template freemarker ...");
		String name = "TrasformazioneSubjectAppIO";
		try {
			if(templateTrasformazione.startsWith("\""))
				templateTrasformazione = templateTrasformazione.substring(1);

			if(templateTrasformazione.endsWith("\""))
				templateTrasformazione = templateTrasformazione.substring(0, templateTrasformazione.length() - 1);

			byte[] template = Base64.getDecoder().decode(templateTrasformazione.getBytes());
			
			//log.debug("Template: "+ new String(template) );
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			Map<String, Object> dynamicMap = new HashMap<String, Object>();
			TrasformazioniUtils.fillDynamicMapSubjectMessageAppIO(log, dynamicMap, ContextThreadLocal.get(), versamento);
			TrasformazioniUtils.convertFreeMarkerTemplate(name, template , dynamicMap , baos );
			// assegno il json trasformato
			log.debug("Generazione del Subject della Notifica AppIO tramite template freemarker completata con successo.");

			// TODO togliere
			log.debug(baos.toString());

			return baos.toString();
		} catch (TrasformazioneException e) {
			log.error("Generazione del Subject della Notifica AppIO tramite template freemarker tramite template freemarker completata con errore: " + e.getMessage(), e);
			throw new GovPayException(e.getMessage(), EsitoOperazione.TRASFORMAZIONE, e, e.getMessage());
		}
	}
	
	public static String trasformazioneMarkdown(Logger log, Versamento versamento, String tipoTemplate, String templateTrasformazione) throws GovPayException {
		log.debug("Generazione del Markdown della Notifica AppIO tramite template freemarker ...");
		String name = "TrasformazioneMarkdownAppIO";
		try {
			if(templateTrasformazione.startsWith("\""))
				templateTrasformazione = templateTrasformazione.substring(1);

			if(templateTrasformazione.endsWith("\""))
				templateTrasformazione = templateTrasformazione.substring(0, templateTrasformazione.length() - 1);

			byte[] template = Base64.getDecoder().decode(templateTrasformazione.getBytes());
			
			//log.debug("Template: "+ new String(template) );
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			Map<String, Object> dynamicMap = new HashMap<String, Object>();
			TrasformazioniUtils.fillDynamicMapMarkdownMessageAppIO(log, dynamicMap, ContextThreadLocal.get(), versamento);
			TrasformazioniUtils.convertFreeMarkerTemplate(name, template , dynamicMap , baos );
			// assegno il json trasformato
			log.debug("Generazione del Markdown della Notifica AppIO tramite template freemarker completata con successo.");

			// TODO togliere
			log.debug(baos.toString());

			return baos.toString();
		} catch (TrasformazioneException e) {
			log.error("Generazione del Markdown della Notifica AppIO tramite template freemarker tramite template freemarker completata con errore: " + e.getMessage(), e);
			throw new GovPayException(e.getMessage(), EsitoOperazione.TRASFORMAZIONE, e, e.getMessage());
		}
	}
}
