package it.govpay.core.business;

import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.LoggerWrapperFactory;
import org.openspcoop2.utils.mail.MailAttach;
import org.openspcoop2.utils.mail.MailBinaryAttach;
import org.openspcoop2.utils.mail.Sender;
import org.openspcoop2.utils.mail.SenderFactory;
import org.openspcoop2.utils.mail.SenderType;
import org.openspcoop2.utils.service.context.ContextThreadLocal;
import org.slf4j.Logger;

import it.govpay.bd.BasicBD;
import it.govpay.bd.model.TipoVersamentoDominio;
import it.govpay.bd.model.Versamento;
import it.govpay.bd.pagamento.PromemoriaBD;
import it.govpay.core.beans.EsitoOperazione;
import it.govpay.core.business.model.PrintAvvisoDTO;
import it.govpay.core.business.model.PrintAvvisoDTOResponse;
import it.govpay.core.exceptions.GovPayException;
import it.govpay.core.utils.GovpayConfig;
import it.govpay.core.utils.trasformazioni.TrasformazioniUtils;
import it.govpay.core.utils.trasformazioni.exception.TrasformazioneException;
import it.govpay.model.Promemoria.TipoPromemoria;

public class Promemoria  extends BasicBD{

	private static final String MAIL_SERVER_HOST = "host";
	private static final String MAIL_SERVER_PORT = "port";
	private static final String MAIL_SERVER_USERNAME = "username";
	private static final String MAIL_SERVER_PASSWORD = "password";
	private static final String MAIL_SERVER_FROM = "from";

	private static Logger log = LoggerWrapperFactory.getLogger(Promemoria.class);

	private Sender senderCommonsMail;

	private String host;
	private int port;
	private String username;
	private String password;
	private String from;

	public Promemoria(BasicBD basicBD) {
		super(basicBD);
		this.senderCommonsMail = SenderFactory.newSender(SenderType.COMMONS_MAIL, log);
		this.senderCommonsMail.setConnectionTimeout(100);
		this.senderCommonsMail.setReadTimeout(5 * 1000);

		Properties promemoriaProperties = GovpayConfig.getInstance().getInvioPromemoriaProperties();
		this.host = promemoriaProperties.getProperty(MAIL_SERVER_HOST);
		this.port = Integer.parseInt(promemoriaProperties.getProperty(MAIL_SERVER_PORT));
		this.username = promemoriaProperties.getProperty(MAIL_SERVER_USERNAME);
		this.password = promemoriaProperties.getProperty(MAIL_SERVER_PASSWORD);
		this.from = promemoriaProperties.getProperty(MAIL_SERVER_FROM);

	}
	
	
	public it.govpay.bd.model.Promemoria creaPromemoriaEmail(Versamento versamento, TipoVersamentoDominio tipoVersamentoDominio) throws ServiceException, GovPayException {
		it.govpay.bd.model.Promemoria promemoria = new it.govpay.bd.model.Promemoria(versamento, TipoPromemoria.EMAIL, this);
		promemoria.setDebitoreEmail(versamento.getAnagraficaDebitore().getEmail());
		promemoria.setOggetto(this.getOggetto(tipoVersamentoDominio.getPromemoriaOggetto(), versamento));
		promemoria.setMessaggio(this.getMessaggio(tipoVersamentoDominio.getPromemoriaMessaggio(), versamento));
		promemoria.setAllegaAvviso(tipoVersamentoDominio.isPromemoriaAvviso());
		return promemoria;
	}


	public void inserisciPromemoria(it.govpay.bd.model.Promemoria promemoria) throws ServiceException {
		PromemoriaBD promemoriaBD = new PromemoriaBD(this);
		promemoriaBD.insertPromemoria(promemoria);
		log.debug("Inserimento promemoria Pendenza["+promemoria.getVersamento(this).getCodVersamentoEnte() +"] effettuato.");
	}

	public String valorizzaTemplate(String nomeTrasformazione, String template, Versamento versamento) throws ServiceException, TrasformazioneException {
		try {
			if(template.startsWith("\""))
				template = template.substring(1);

			if(template.endsWith("\""))
				template = template.substring(0, template.length() - 1);
			byte[] templateBytes = Base64.getDecoder().decode(template.getBytes());
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			Map<String, Object> dynamicMap = new HashMap<String, Object>();
			TrasformazioniUtils.fillDynamicMapPromemoria(log, dynamicMap, ContextThreadLocal.get(), versamento, versamento.getDominio(this));
			TrasformazioniUtils.convertFreeMarkerTemplate(nomeTrasformazione, templateBytes , dynamicMap , baos );
			return baos.toString();
		} catch (TrasformazioneException e) {
			log.error("Trasformazione tramite template Freemarker completata con errore: " + e.getMessage(), e);
			throw e;
		}
	}
	
	public String getOggetto(String templateMessaggio, Versamento versamento)  throws ServiceException, GovPayException{
		String name = "GenerazioneOggettoPromemoria";
		try {
			return this.valorizzaTemplate(name, templateMessaggio, versamento);
		} catch (TrasformazioneException e) {
			throw new GovPayException(e.getMessage(), EsitoOperazione.PRM_001, e, versamento.getApplicazione(this).getCodApplicazione(), versamento.getCodVersamentoEnte(), e.getMessage());
		}
	}

	public String getMessaggio(String templateMessaggio, Versamento versamento)  throws ServiceException, GovPayException{
		String name = "GenerazioneMessaggioPromemoria";
		try {
			return this.valorizzaTemplate(name, templateMessaggio, versamento);
		} catch (TrasformazioneException e) {
			throw new GovPayException(e.getMessage(), EsitoOperazione.PRM_002, e, versamento.getApplicazione(this).getCodApplicazione(), versamento.getCodVersamentoEnte(), e.getMessage());
		}
	}

	public List<it.govpay.bd.model.Promemoria> findPromemoriaDaSpedire() throws ServiceException{
		PromemoriaBD promemoriaBD = new PromemoriaBD(this);
		List<it.govpay.bd.model.Promemoria> promemoria = promemoriaBD.findPromemoriaDaSpedire();
		return promemoria;
	}

	public void invioPromemoria(it.govpay.bd.model.Promemoria promemoria) throws ServiceException {
		PromemoriaBD promemoriaBD = new PromemoriaBD(this);
		Versamento versamento = promemoria.getVersamento(this);
		boolean ok = true;
		String errore  = "";

		org.openspcoop2.utils.mail.Mail mail = new org.openspcoop2.utils.mail.Mail();

		mail.setServerHost(this.host);
		mail.setServerPort(this.port);

		if(this.username != null && !this.username.isEmpty() && this.password != null && !this.password.isEmpty()) {
			mail.setUsername(this.username);
			mail.setPassword(this.password);
		}

		mail.setStartTls(false);

		mail.setFrom(this.from);
		mail.setTo(promemoria.getDebitoreEmail());

		mail.setSubject(promemoria.getOggetto());
		mail.getBody().setMessage(promemoria.getMessaggio());

		if(promemoria.isAllegaAvviso()) {
			AvvisoPagamento avvisoPagamento = new AvvisoPagamento(this);
			PrintAvvisoDTO printAvviso = new PrintAvvisoDTO();
			printAvviso.setVersamento(versamento);
			printAvviso.setCodDominio(versamento.getDominio(this).getCodDominio());
			printAvviso.setIuv(versamento.getIuvVersamento());
			PrintAvvisoDTOResponse printAvvisoDTOResponse = avvisoPagamento.printAvviso(printAvviso);

			String attachmentName = versamento.getDominio(this).getCodDominio() + "_" + versamento.getNumeroAvviso() + ".pdf";
			MailAttach avvisoAttach = new MailBinaryAttach(attachmentName, printAvvisoDTOResponse.getAvviso().getPdf());

			mail.getBody().getAttachments().add(avvisoAttach );
		}
		try {
			this.senderCommonsMail.send(mail, true);
		}catch (Exception e) {
			errore = "Errore durante l'invio del promemoria per la pendenza [IDA2A: "+versamento.getApplicazione(this).getCodApplicazione()+" , IdPendenza: "+versamento.getCodVersamentoEnte()+ "] al destinatario ["+promemoria.getDebitoreEmail()+"]:"+e.getMessage();
			log.warn(errore);
			ok = false;
		}
		
		if(!ok) {
			long tentativi = promemoria.getTentativiSpedizione() + 1;
			Date today = new Date();
			Date tomorrow = new Date(today.getTime() + (1000 * 60 * 60 * 24));
			Date prossima = new Date(today.getTime() + (tentativi * tentativi * 60 * 1000));
			
			// Limito la rispedizione al giorno dopo.
			if(prossima.after(tomorrow)) prossima = tomorrow;
			
			promemoriaBD.updateDaSpedire(promemoria.getId(), errore, tentativi, prossima);
		} else {
			promemoriaBD.updateSpedito(promemoria.getId());
		}
	}
}
