package it.govpay.core.utils.appio;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.openspcoop2.utils.service.context.ContextThreadLocal;
import org.slf4j.Logger;

import it.govpay.bd.configurazione.model.PromemoriaAvvisoBase;
import it.govpay.bd.configurazione.model.PromemoriaScadenza;
import it.govpay.bd.model.TipoVersamentoDominio;
import it.govpay.bd.model.Versamento;
import it.govpay.core.beans.EsitoOperazione;
import it.govpay.core.exceptions.GovPayException;
import it.govpay.core.exceptions.UnprocessableEntityException;
import it.govpay.core.utils.appio.impl.RFC3339DateFormat;
import it.govpay.core.utils.appio.model.MessageContent;
import it.govpay.core.utils.appio.model.NewMessage;
import it.govpay.core.utils.appio.model.PaymentData;
import it.govpay.core.utils.trasformazioni.TrasformazioniUtils;
import it.govpay.core.utils.trasformazioni.exception.TrasformazioneException;

public class AppIOUtils {
	
	public static NewMessage creaNuovoMessaggioAvvisoPagamento(Logger log, Versamento versamento, TipoVersamentoDominio tipoVersamentoDominio, PromemoriaAvvisoBase configurazionePromemoriaAvviso, BigDecimal timeToLive) throws GovPayException {
		String appIOMessaggio = tipoVersamentoDominio.getAvvisaturaAppIoPromemoriaAvvisoMessaggio();
		String appIOOggetto = tipoVersamentoDominio.getAvvisaturaAppIoPromemoriaAvvisoOggetto();
		String appIOTipo = tipoVersamentoDominio.getAvvisaturaAppIoPromemoriaAvvisoTipo();
		
		boolean usaConfigurazioneSistema = true;
		
		if(appIOMessaggio != null && appIOOggetto != null && appIOTipo != null) {
			usaConfigurazioneSistema = false;
		}
		
		if(usaConfigurazioneSistema) {
			appIOMessaggio = configurazionePromemoriaAvviso.getMessaggio();
			appIOOggetto = configurazionePromemoriaAvviso.getOggetto();
			appIOTipo = configurazionePromemoriaAvviso.getTipo();
		}
		
		NewMessage messageWithCF = AppIOUtils.getPostMessage(log, appIOTipo, appIOOggetto, appIOMessaggio, timeToLive, versamento, true);
		return messageWithCF;
	}
	
	public static NewMessage creaNuovoMessaggioScadenzaPagamento(Logger log, Versamento versamento, TipoVersamentoDominio tipoVersamentoDominio, PromemoriaScadenza configurazionePromemoriaScadenza, BigDecimal timeToLive) throws GovPayException {
		String appIOMessaggio = tipoVersamentoDominio.getAvvisaturaAppIoPromemoriaScadenzaMessaggio();
		String appIOOggetto = tipoVersamentoDominio.getAvvisaturaAppIoPromemoriaScadenzaOggetto();
		String appIOTipo = tipoVersamentoDominio.getAvvisaturaAppIoPromemoriaScadenzaTipo();
		
		boolean usaConfigurazioneSistema = true;
		
		if(appIOMessaggio != null && appIOOggetto != null && appIOTipo != null) {
			usaConfigurazioneSistema = false;
		}
		
		if(usaConfigurazioneSistema) {
			appIOMessaggio = configurazionePromemoriaScadenza.getMessaggio();
			appIOOggetto = configurazionePromemoriaScadenza.getOggetto();
			appIOTipo = configurazionePromemoriaScadenza.getTipo();
		}
		
		NewMessage messageWithCF = AppIOUtils.getPostMessage(log, appIOTipo, appIOOggetto, appIOMessaggio, timeToLive, versamento, false);
		return messageWithCF;
	}

	public static NewMessage getPostMessage(Logger log, String tipo, String oggetto, String messaggio, BigDecimal timeToLive, Versamento versamento, boolean includePaymentData) throws GovPayException {
		NewMessage message = new NewMessage();
		
		if(timeToLive != null)
			message.setTimeToLive(timeToLive.intValue());
		
		message.setFiscalCode(versamento.getAnagraficaDebitore().getCodUnivoco());
		MessageContent content = new MessageContent();
		
		String subject = trasformazioneSubject(log, versamento, tipo, oggetto);
		String markdown = trasformazioneMarkdown(log, versamento, tipo, messaggio);
		
		content.setSubject(subject);
		content.setMarkdown(markdown);
		
		Date dueDate = null;
		Date dataScadenza = null;
		if(versamento.getDataScadenza() != null) { 
			dueDate = versamento.getDataScadenza();
			dataScadenza = versamento.getDataScadenza();
		} 
		
		if(versamento.getDataValidita() != null) {
			dueDate = versamento.getDataValidita();
		}
		
		boolean invalid_after_due_date = false;
		if(dueDate != null) {
			RFC3339DateFormat formatter = new RFC3339DateFormat();
			StringBuffer appender = new StringBuffer();
			appender = formatter.format(dueDate, appender , null);
			content.setDueDate(appender.toString());
			
			if(dataScadenza != null) {
				invalid_after_due_date = dueDate.getTime() == versamento.getDataScadenza().getTime();
			}
		}
		
		if(includePaymentData) {
			PaymentData payment_data = new PaymentData();
			
			// importo in centesimi
			int amount = (int)(versamento.getImportoTotale().doubleValue() * 100);
			payment_data.setAmount(amount);
			payment_data.setInvalidAfterDueDate(invalid_after_due_date);
			payment_data.setNoticeNumber(versamento.getNumeroAvviso());
					
			content.setPaymentData(payment_data );
		}
				
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
		} catch (TrasformazioneException | UnprocessableEntityException e) {
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
		} catch (TrasformazioneException | UnprocessableEntityException e) {
			log.error("Generazione del Markdown della Notifica AppIO tramite template freemarker tramite template freemarker completata con errore: " + e.getMessage(), e);
			throw new GovPayException(e.getMessage(), EsitoOperazione.TRASFORMAZIONE, e, e.getMessage());
		}
	}
}
