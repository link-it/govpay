package it.govpay.core.business;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBException;

import org.apache.commons.lang.StringUtils;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.LoggerWrapperFactory;
import org.openspcoop2.utils.UtilsException;
import org.openspcoop2.utils.mail.MailAttach;
import org.openspcoop2.utils.mail.MailBinaryAttach;
import org.openspcoop2.utils.mail.Sender;
import org.openspcoop2.utils.mail.SenderFactory;
import org.openspcoop2.utils.mail.SenderType;
import org.openspcoop2.utils.resources.Charset;
import org.openspcoop2.utils.service.context.ContextThreadLocal;
import org.slf4j.Logger;
import org.xml.sax.SAXException;

import freemarker.template.TemplateException;
import it.gov.digitpa.schemas._2011.pagamenti.CtRichiestaPagamentoTelematico;
import it.govpay.bd.BasicBD;
import it.govpay.bd.configurazione.model.Mail;
import it.govpay.bd.configurazione.model.MailBatch;
import it.govpay.bd.configurazione.model.MailServer;
import it.govpay.bd.model.Rpt;
import it.govpay.bd.model.TipoVersamentoDominio;
import it.govpay.bd.model.Versamento;
import it.govpay.bd.pagamento.PromemoriaBD;
import it.govpay.core.beans.EsitoOperazione;
import it.govpay.core.business.model.PrintAvvisoDTO;
import it.govpay.core.business.model.PrintAvvisoDTOResponse;
import it.govpay.core.dao.pagamenti.dto.LeggiRicevutaDTO;
import it.govpay.core.dao.pagamenti.dto.LeggiRicevutaDTOResponse;
import it.govpay.core.exceptions.GovPayException;
import it.govpay.core.exceptions.PromemoriaException;
import it.govpay.core.utils.ExceptionUtils;
import it.govpay.core.utils.JaxbUtils;
import it.govpay.core.utils.trasformazioni.Costanti;
import it.govpay.core.utils.trasformazioni.TrasformazioniUtils;
import it.govpay.core.utils.trasformazioni.exception.TrasformazioneException;
import it.govpay.model.Promemoria.TipoPromemoria;

public class Promemoria  extends BasicBD{

	private static Logger log = LoggerWrapperFactory.getLogger(Promemoria.class);

	private Sender senderCommonsMail;

	private String host;
	private int port;
	private String username;
	private String password;
	private String from;

	private Mail configurazionePromemoriaMail;
	private Mail configurazioneRicevutaMail;
	
	

	public Promemoria(BasicBD basicBD) {
		super(basicBD);
		this.senderCommonsMail = SenderFactory.newSender(SenderType.COMMONS_MAIL, log);

		Configurazione configurazioneBD = new Configurazione(this);
		try {
			it.govpay.bd.model.Configurazione configurazione = configurazioneBD.getConfigurazione();
			MailBatch batchSpedizioneEmail = configurazione.getBatchSpedizioneEmail();
			MailServer mailserver = batchSpedizioneEmail.getMailserver();
			
			this.configurazionePromemoriaMail = configurazione.getPromemoriaMail();
			this.configurazioneRicevutaMail = configurazione.getRicevutaMail();
			
			this.host = mailserver.getHost();
			this.port = mailserver.getPort();
			this.username = mailserver.getUsername();
			this.password = mailserver.getPassword();
			this.from = mailserver.getFrom();
			if(mailserver.getReadTimeout() != null)
				this.senderCommonsMail.setReadTimeout(mailserver.getReadTimeout());
			if(mailserver.getConnectionTimeout() != null)
				this.senderCommonsMail.setConnectionTimeout(mailserver.getConnectionTimeout());
			
		} catch (ServiceException e) {
			log.error("Errore durante l'inizializzazione del Promemoria: " + e.getMessage(),e);
		}
	}

	public it.govpay.bd.model.Promemoria creaPromemoriaRicevuta(it.govpay.bd.model.Rpt rpt, Versamento versamento, TipoVersamentoDominio tipoVersamentoDominio) throws ServiceException, GovPayException, JAXBException, SAXException {
		it.govpay.bd.model.Promemoria promemoria = new it.govpay.bd.model.Promemoria(rpt, versamento, TipoPromemoria.RICEVUTA, this);
		this.setRicevutaDestinatari(rpt, versamento, promemoria); 
		if(tipoVersamentoDominio.getPromemoriaRicevutaPdf() != null)
			promemoria.setAllegaPdf(tipoVersamentoDominio.getPromemoriaRicevutaPdf());
		else
			promemoria.setAllegaPdf(this.configurazioneRicevutaMail.isAllegaPdf());
		
		return promemoria;
	}


	public it.govpay.bd.model.Promemoria creaPromemoriaAvviso(Versamento versamento, TipoVersamentoDominio tipoVersamentoDominio) {
		it.govpay.bd.model.Promemoria promemoria = new it.govpay.bd.model.Promemoria(versamento, TipoPromemoria.AVVISO, this);
		promemoria.setDestinatarioTo(versamento.getAnagraficaDebitore().getEmail());
		
		if(tipoVersamentoDominio.getPromemoriaAvvisoPdf() != null)
			promemoria.setAllegaPdf(tipoVersamentoDominio.getPromemoriaAvvisoPdf());
		else
			promemoria.setAllegaPdf(this.configurazionePromemoriaMail.isAllegaPdf());
		
		return promemoria;
	}

	public void setRicevutaDestinatari(it.govpay.bd.model.Rpt rpt, Versamento versamento, it.govpay.bd.model.Promemoria promemoria) throws JAXBException, SAXException {
		String debitore = versamento.getAnagraficaDebitore().getEmail();
		CtRichiestaPagamentoTelematico rptCtRichiestaPagamentoTelematico = JaxbUtils.toRPT(rpt.getXmlRpt(), false);
		String versante = rptCtRichiestaPagamentoTelematico.getSoggettoVersante() != null ? rptCtRichiestaPagamentoTelematico.getSoggettoVersante().getEMailVersante() : null;

		if(versante != null && debitore != null) {
			promemoria.setDestinatarioTo(versante);
			promemoria.setDestinatarioCc(debitore);
		} else if(versante != null && debitore == null) {
			promemoria.setDestinatarioTo(versante);
		} else if(versante == null && debitore != null) {
			promemoria.setDestinatarioTo(debitore);
		} else {
			// nessuna mail
		}
	}

	public String getContentType(Map<String, Object> dynamicMap) {
		if(dynamicMap.containsKey(Costanti.MAP_CONTENT_TYPE_MESSAGGIO_PROMEMORIA))
			return (String) dynamicMap.get(Costanti.MAP_CONTENT_TYPE_MESSAGGIO_PROMEMORIA);

		return Costanti.MAP_CONTENT_TYPE_MESSAGGIO_PROMEMORIA_DEFAULT_VALUE;
	}

	public void inserisciPromemoria(it.govpay.bd.model.Promemoria promemoria) throws ServiceException {
		PromemoriaBD promemoriaBD = new PromemoriaBD(this);
		promemoriaBD.insertPromemoria(promemoria);
		log.debug("Inserimento promemoria Pendenza["+promemoria.getVersamento(this).getCodVersamentoEnte() +"] effettuato.");
	}

	public String valorizzaTemplate(String nomeTrasformazione, Map<String, Object> dynamicMap, String tipoTemplate, String template) throws ServiceException, TrasformazioneException {
		try {
			if(template.startsWith("\""))
				template = template.substring(1);

			if(template.endsWith("\""))
				template = template.substring(0, template.length() - 1);
			byte[] templateBytes = Base64.getDecoder().decode(template.getBytes());
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			TrasformazioniUtils.convertFreeMarkerTemplate(nomeTrasformazione, templateBytes , dynamicMap , baos );
			log.debug("Risultato trasformazione: " + baos.toString());
			log.debug("Risultato trasformazione UTF-8: " + baos.toString(Charset.UTF_8.getValue()));
			return baos.toString();
		} catch (TrasformazioneException e) {
			log.error("Trasformazione tramite template Freemarker completata con errore: " + e.getMessage(), e);
			throw e;
		} catch (UnsupportedEncodingException e) {
			log.error("Trasformazione tramite template Freemarker completata con errore: " + e.getMessage(), e);
			throw new ServiceException(e);
		}
	}

	public String getOggettoAvviso(String tipoTemplate, String templateMessaggio, Versamento versamento, Map<String, Object> dynamicMap)  throws ServiceException, GovPayException{
		String name = "GenerazioneOggettoPromemoriaAvviso";
		try {
			return this.valorizzaTemplate(name, dynamicMap, tipoTemplate, templateMessaggio);
		} catch (TrasformazioneException e) {
			throw new GovPayException(e.getMessage(), EsitoOperazione.PRM_001, e, versamento.getApplicazione(this).getCodApplicazione(), versamento.getCodVersamentoEnte(), e.getMessage());
		}
	}

	public String getMessaggioAvviso(String tipoTemplate, String templateMessaggio, Versamento versamento, Map<String, Object> dynamicMap)  throws ServiceException, GovPayException{
		String name = "GenerazioneMessaggioPromemoriaAvviso";
		try {
			return this.valorizzaTemplate(name, dynamicMap, tipoTemplate, templateMessaggio);
		} catch (TrasformazioneException e) {
			throw new GovPayException(e.getMessage(), EsitoOperazione.PRM_002, e, versamento.getApplicazione(this).getCodApplicazione(), versamento.getCodVersamentoEnte(), e.getMessage());
		}
	}

	public String getOggettoRicevuta(String tipoTemplate, String templateMessaggio, it.govpay.bd.model.Rpt rpt, Versamento versamento, Map<String, Object> dynamicMap)  throws ServiceException, GovPayException{
		String name = "GenerazioneOggettoPromemoriaRicevuta";
		try {
			return this.valorizzaTemplate(name, dynamicMap, tipoTemplate, templateMessaggio);
		} catch (TrasformazioneException e) {
			throw new GovPayException(e.getMessage(), EsitoOperazione.PRM_003, e, versamento.getApplicazione(this).getCodApplicazione(), versamento.getCodVersamentoEnte(), e.getMessage());
		}
	}

	public String getMessaggioRicevuta(String tipoTemplate, String templateMessaggio, it.govpay.bd.model.Rpt rpt, Versamento versamento, Map<String, Object> dynamicMap)  throws ServiceException, GovPayException{
		String name = "GenerazioneMessaggioPromemoriaRicevuta";
		try {
			return this.valorizzaTemplate(name, dynamicMap, tipoTemplate, templateMessaggio);
		} catch (TrasformazioneException e) {
			throw new GovPayException(e.getMessage(), EsitoOperazione.PRM_004, e, versamento.getApplicazione(this).getCodApplicazione(), versamento.getCodVersamentoEnte(), e.getMessage());
		}
	}

	public List<it.govpay.bd.model.Promemoria> findPromemoriaDaSpedire() throws ServiceException{
		PromemoriaBD promemoriaBD = new PromemoriaBD(this);
		List<it.govpay.bd.model.Promemoria> promemoria = promemoriaBD.findPromemoriaDaSpedire();
		return promemoria;
	}

	public void invioPromemoria(it.govpay.bd.model.Promemoria promemoria) { 
		switch (promemoria.getTipo()) {
		case AVVISO:
			this.invioPromemoriaAvviso(promemoria);
			break;
		case RICEVUTA:
			this.invioPromemoriaRicevuta(promemoria); 
			break;
		}
	}



	private void invioPromemoriaAvviso(it.govpay.bd.model.Promemoria promemoria) {
		PromemoriaBD promemoriaBD = new PromemoriaBD(this);
		String errore = "", codApplicazione = "", codVersamentoEnte  = "";
		try {
			Versamento versamento = promemoria.getVersamento(this);
			codApplicazione = versamento.getApplicazione(this).getCodApplicazione();
			codVersamentoEnte = versamento.getCodVersamentoEnte();
			TipoVersamentoDominio tipoVersamentoDominio = versamento.getTipoVersamentoDominio(this);

			if(StringUtils.isEmpty(promemoria.getDestinatarioTo())){
				throw new PromemoriaException("Destinatario messaggio non specificato");
			}

			org.openspcoop2.utils.mail.Mail mail = new org.openspcoop2.utils.mail.Mail();
			mail.setServerHost(this.host);
			mail.setServerPort(this.port);
			if(this.username != null && !this.username.isEmpty() && this.password != null && !this.password.isEmpty()) {
				mail.setUsername(this.username);
				mail.setPassword(this.password);
			}
			mail.setStartTls(false);
			mail.setFrom(this.from);
			mail.setTo(promemoria.getDestinatarioTo());
			if(promemoria.getDestinatarioCc() !=null)
				mail.setCc(Arrays.asList(promemoria.getDestinatarioCc()));

			log.debug("Invio promemoria avviso di pagamento per la pendenza [IDA2A: "+versamento.getApplicazione(this).getCodApplicazione()
					+" , IdPendenza: "+versamento.getCodVersamentoEnte()+ "], al destinatario ["+promemoria.getDestinatarioTo()+"] CC["+(promemoria.getDestinatarioCc() !=null ? promemoria.getDestinatarioCc() : "")+"]");

			boolean inserisciOggetto = promemoria.getOggetto() == null;
			if(promemoria.getOggetto() == null) {
				log.debug("Creazione oggetto del promemoria...");
				try {
					Map<String, Object> dynamicMap = new HashMap<String, Object>();
					TrasformazioniUtils.fillDynamicMapPromemoriaAvviso(log, dynamicMap, ContextThreadLocal.get(), versamento, versamento.getDominio(this));
					String promemoriaAvvisoOggetto = tipoVersamentoDominio.getPromemoriaAvvisoOggetto() != null ? tipoVersamentoDominio.getPromemoriaAvvisoOggetto() : this.configurazionePromemoriaMail.getOggetto();
					String promemoriaAvvisoTipoTemplate = tipoVersamentoDominio.getPromemoriaAvvisoTipo() != null ? tipoVersamentoDominio.getPromemoriaAvvisoTipo() : this.configurazionePromemoriaMail.getTipo(); 
					promemoria.setOggetto(this.getOggettoAvviso(promemoriaAvvisoTipoTemplate, promemoriaAvvisoOggetto, versamento, dynamicMap));
				} catch (Throwable t) {
					throw new PromemoriaException("Oggetto del messaggio non generabile", t);
				}
				log.debug("Creazione oggetto del promemoria completata.");
			}
			mail.setSubject(promemoria.getOggetto());

			boolean inserisciMessaggio = promemoria.getMessaggio() == null;

			if(promemoria.getMessaggio() == null) {
				log.debug("Creazione messaggio del promemoria...");
				try {
					Map<String, Object> dynamicMap = new HashMap<String, Object>();
					TrasformazioniUtils.fillDynamicMapPromemoriaAvviso(log, dynamicMap, ContextThreadLocal.get(), versamento, versamento.getDominio(this));
					String promemoriaAvvisoMessaggio = tipoVersamentoDominio.getPromemoriaAvvisoMessaggio() != null ? tipoVersamentoDominio.getPromemoriaAvvisoMessaggio() : this.configurazionePromemoriaMail.getMessaggio();
					String promemoriaAvvisoTipoTemplate = tipoVersamentoDominio.getPromemoriaAvvisoTipo() != null ? tipoVersamentoDominio.getPromemoriaAvvisoTipo() : this.configurazionePromemoriaMail.getTipo(); 
					promemoria.setMessaggio(this.getMessaggioAvviso(promemoriaAvvisoTipoTemplate, promemoriaAvvisoMessaggio, versamento, dynamicMap));
					promemoria.setContentType(this.getContentType(dynamicMap));
				} catch (Throwable t) {
					throw new PromemoriaException("Corpo del messaggio non generabile", t);
				}
				log.debug("Creazione messaggio del promemoria completata.");
			}
			mail.getBody().setMessage(promemoria.getMessaggio());

			if(promemoria.isAllegaPdf()) {
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

			// salvo il contenuto del promemoria
			if(inserisciMessaggio || inserisciOggetto) {
				log.debug("Salvataggio messaggio e oggetto del promemoria...");
				try {
					promemoriaBD.updateMessaggioPromemoria(promemoria.getId(), promemoria.getOggetto(), promemoria.getMessaggio(), promemoria.getContentType());
				} catch (Throwable t) {
					throw new PromemoriaException("Allegato del messaggio non generabile", t);
				}
				log.debug("Salvataggio messaggio e oggetto del promemoria completato.");
			}

			try {
				log.debug("Spediazione promemoria verso il mail server ["+this.host+"]:["+this.port+"]...");
				this.senderCommonsMail.send(mail, true);
				log.debug("Spediazione promemoria verso il mail server ["+this.host+"]:["+this.port+"] completata.");
				promemoriaBD.updateSpedito(promemoria.getId());
			}catch (UtilsException e) {
				errore = "Errore durante l'invio del promemoria avviso di pagamento per la pendenza [IDA2A: "+versamento.getApplicazione(this).getCodApplicazione()+" , IdPendenza: "+versamento.getCodVersamentoEnte()
				+ "] al destinatario ["+promemoria.getDestinatarioTo()+"] CC["+(promemoria.getDestinatarioCc() !=null ? promemoria.getDestinatarioCc() : "")+"]:"+e.getMessage();
				log.error(errore, e);

				if(ExceptionUtils.existsInnerException(e, javax.mail.internet.AddressException.class)) {
					log.debug("La spedizione del promemoria si e' conclusa con errore che non prevede la rispedizione...");
					promemoriaBD.updateFallita(promemoria.getId(), ExceptionUtils.getInnerException(e, javax.mail.internet.AddressException.class).getMessage());
					log.debug("Salvataggio stato 'fallito' completato con successo");
				} else {
					log.debug("La spedizione del promemoria si e' conclusa con errore, rischedulo la spedizione...");
					long tentativi = promemoria.getTentativiSpedizione() + 1;
					Date today = new Date();
					Date tomorrow = new Date(today.getTime() + (1000 * 60 * 60 * 24));
					Date prossima = new Date(today.getTime() + (tentativi * tentativi * 60 * 1000));

					// Limito la rispedizione al giorno dopo.
					if(prossima.after(tomorrow)) prossima = tomorrow;

					promemoriaBD.updateDaSpedire(promemoria.getId(), errore, tentativi, prossima);
					log.debug("La spedizione del promemoria schedulata con successo.");
				}
			} 
		} catch (PromemoriaException e) {
			log.debug("Errore in gestione promemoria: " + e.getMessage());
			try {
				Throwable innerException = ExceptionUtils.getInnerException(e, TemplateException.class);
				if(innerException != null)
					promemoriaBD.updateFallita(promemoria.getId(), e.getMessage() + ": " + innerException.getMessage());
				else
					promemoriaBD.updateFallita(promemoria.getId(), e.getMessage());
			} catch (ServiceException e1) {
				log.debug("Errore in aggiornamento promemoria [" + codApplicazione + "/"+codVersamentoEnte+ "] fallito: " + e.getMessage());
			}
		} catch (Throwable t) {
			log.error("Errore in gestione promemoria", t);
			try {
				Throwable innerException = ExceptionUtils.getInnerException(t, TemplateException.class);
				if(innerException != null)
					promemoriaBD.updateFallita(promemoria.getId(), t.getMessage() + ": " + innerException.getMessage());
				else
					promemoriaBD.updateFallita(promemoria.getId(), t.getMessage());
			} catch (ServiceException e1) {
				log.debug("Errore in aggiornamento promemoria [" + codApplicazione + "/"+codVersamentoEnte+ "] fallito: " + t.getMessage());
			}
		}
	}

	private void invioPromemoriaRicevuta(it.govpay.bd.model.Promemoria promemoria) {
		PromemoriaBD promemoriaBD = new PromemoriaBD(this);
		String errore = "", codApplicazione = "", codVersamentoEnte  = "";
		try {
			Versamento versamento = promemoria.getVersamento(this);
			codApplicazione = versamento.getApplicazione(this).getCodApplicazione();
			codVersamentoEnte = versamento.getCodVersamentoEnte();
			
			TipoVersamentoDominio tipoVersamentoDominio = versamento.getTipoVersamentoDominio(this);
			Rpt rpt = promemoria.getRpt(this);

			if(StringUtils.isEmpty(promemoria.getDestinatarioTo())){
				throw new PromemoriaException("Destinatario messaggio non specificato");
			}

			org.openspcoop2.utils.mail.Mail mail = new org.openspcoop2.utils.mail.Mail();

			mail.setServerHost(this.host);
			mail.setServerPort(this.port);

			if(this.username != null && !this.username.isEmpty() && this.password != null && !this.password.isEmpty()) {
				mail.setUsername(this.username);
				mail.setPassword(this.password);
			}

			mail.setStartTls(false);

			mail.setFrom(this.from);
			mail.setTo(promemoria.getDestinatarioTo());
			if(promemoria.getDestinatarioCc() !=null)
				mail.setCc(Arrays.asList(promemoria.getDestinatarioCc()));

			log.debug("Invio promemoria ricevuta di pagamento per la pendenza [IDA2A: "+versamento.getApplicazione(this).getCodApplicazione()
					+" , IdPendenza: "+versamento.getCodVersamentoEnte()+ "], al destinatario ["+promemoria.getDestinatarioTo()+"] CC["+(promemoria.getDestinatarioCc() !=null ? promemoria.getDestinatarioCc() : "")+"]");

			boolean inserisciOggetto = promemoria.getOggetto() == null;
			if(promemoria.getOggetto() == null) {
				log.debug("Creazione oggetto del promemoria...");
				try {
				Map<String, Object> dynamicMap = new HashMap<String, Object>();
				TrasformazioniUtils.fillDynamicMapPromemoriaRicevuta(log, dynamicMap, ContextThreadLocal.get(), rpt, versamento, versamento.getDominio(this));
				String promemoriaRicevutaTipoTemplate = tipoVersamentoDominio.getPromemoriaRicevutaTipo() != null ? tipoVersamentoDominio.getPromemoriaRicevutaTipo() : this.configurazioneRicevutaMail.getTipo(); 
				String promemoriaRicevutaOggetto = tipoVersamentoDominio.getPromemoriaRicevutaOggetto() != null ? tipoVersamentoDominio.getPromemoriaRicevutaOggetto() : this.configurazioneRicevutaMail.getOggetto();
				promemoria.setOggetto(this.getOggettoRicevuta(promemoriaRicevutaTipoTemplate, promemoriaRicevutaOggetto, rpt, versamento, dynamicMap));
				} catch (Throwable t) {
					throw new PromemoriaException("Corpo del messaggio non generabile", t);
				}
				log.debug("Creazione oggetto del promemoria completata.");
			}
			mail.setSubject(promemoria.getOggetto());

			boolean inserisciMessaggio = promemoria.getMessaggio() == null;
			if(promemoria.getMessaggio() == null) {
				log.debug("Creazione messaggio del promemoria...");
				try {
					Map<String, Object> dynamicMap = new HashMap<String, Object>();
				TrasformazioniUtils.fillDynamicMapPromemoriaRicevuta(log, dynamicMap, ContextThreadLocal.get(), rpt, versamento, versamento.getDominio(this));
				String promemoriaRicevutaTipoTemplate = tipoVersamentoDominio.getPromemoriaRicevutaTipo() != null ? tipoVersamentoDominio.getPromemoriaRicevutaTipo() : this.configurazioneRicevutaMail.getTipo(); 
				String promemoriaRicevutaMessaggio = tipoVersamentoDominio.getPromemoriaRicevutaMessaggio() != null ? tipoVersamentoDominio.getPromemoriaRicevutaMessaggio() : this.configurazioneRicevutaMail.getMessaggio();
				promemoria.setMessaggio(this.getMessaggioRicevuta(promemoriaRicevutaTipoTemplate, promemoriaRicevutaMessaggio, rpt, versamento, dynamicMap));
				promemoria.setContentType(this.getContentType(dynamicMap));
				} catch (Throwable t) {
					throw new PromemoriaException("Corpo del messaggio non generabile", t);
				}
				log.debug("Creazione messaggio del promemoria completata.");
			}
			mail.getBody().setMessage(promemoria.getMessaggio());


			if(promemoria.isAllegaPdf()) {
				String codDominio = rpt.getCodDominio();
				String iuv = rpt.getIuv();
				String ccp = rpt.getCcp();
				try {
					rpt.getPagamentoPortale(this).getApplicazione(this);
				} catch (NotFoundException e) {	}

				it.govpay.core.business.RicevutaTelematica avvisoBD = new it.govpay.core.business.RicevutaTelematica(this);
				LeggiRicevutaDTO leggiRicevutaDTO = new LeggiRicevutaDTO(null);
				leggiRicevutaDTO.setIdDominio(codDominio);
				leggiRicevutaDTO.setIuv(iuv);
				leggiRicevutaDTO.setCcp(ccp);
				LeggiRicevutaDTOResponse response = avvisoBD.creaPdfRicevuta(leggiRicevutaDTO,rpt);

				String attachmentName = codDominio +"_"+ iuv + "_"+ ccp + ".pdf";
				MailAttach avvisoAttach = new MailBinaryAttach(attachmentName, response.getPdf());

				mail.getBody().getAttachments().add(avvisoAttach );
			}

			// salvo il contenuto del promemoria
			if(inserisciMessaggio || inserisciOggetto) {
				log.debug("Salvataggio messaggio e oggetto del promemoria...");
				promemoriaBD.updateMessaggioPromemoria(promemoria.getId(), promemoria.getOggetto(), promemoria.getMessaggio(), promemoria.getContentType()); 
				log.debug("Salvataggio messaggio e oggetto del promemoria completato.");
			}

			try {
				log.debug("Spediazione promemoria verso il mail server ["+this.host+"]:["+this.port+"]...");
				this.senderCommonsMail.send(mail, true);
				log.debug("Spediazione promemoria verso il mail server ["+this.host+"]:["+this.port+"] completata.");
				promemoriaBD.updateSpedito(promemoria.getId());
			}catch (UtilsException e) {
				errore = "Errore durante l'invio del promemoria ricevuta di pagamento per la pendenza [IDA2A: "+versamento.getApplicazione(this).getCodApplicazione()+" , IdPendenza: "+versamento.getCodVersamentoEnte()
				+ "] al destinatario ["+promemoria.getDestinatarioTo()+"] CC["+(promemoria.getDestinatarioCc() !=null ? promemoria.getDestinatarioCc() : "")+"]:"+e.getMessage();
				log.error(errore, e);

				if(ExceptionUtils.existsInnerException(e, javax.mail.internet.AddressException.class)) {
					log.debug("La spedizione del promemoria si e' conclusa con errore che non prevede la rispedizione...");
					promemoriaBD.updateFallita(promemoria.getId(), errore);
					log.debug("Salvataggio stato 'fallito' completato con successo");
				} else {
					log.debug("La spedizione del promemoria si e' conclusa con errore, rischedulo la spedizione...");
					long tentativi = promemoria.getTentativiSpedizione() + 1;
					Date today = new Date();
					Date tomorrow = new Date(today.getTime() + (1000 * 60 * 60 * 24));
					Date prossima = new Date(today.getTime() + (tentativi * tentativi * 60 * 1000));

					// Limito la rispedizione al giorno dopo.
					if(prossima.after(tomorrow)) prossima = tomorrow;

					promemoriaBD.updateDaSpedire(promemoria.getId(), errore, tentativi, prossima);
					log.debug("La spedizione del promemoria schedulata con successo.");
				}
			}
		} catch (PromemoriaException e) {
			log.debug("Errore in gestione promemoria: " + e.getMessage());
			try {
				Throwable innerException = ExceptionUtils.getInnerException(e, TemplateException.class);
				if(innerException != null)
					promemoriaBD.updateFallita(promemoria.getId(), e.getMessage() + ": " + innerException.getMessage());
				else
					promemoriaBD.updateFallita(promemoria.getId(), e.getMessage());
			} catch (ServiceException e1) {
				log.debug("Errore in aggiornamento promemoria [" + codApplicazione + "/"+codVersamentoEnte+ "] fallito: " + e.getMessage());
			}
		} catch (Throwable t) {
			log.error("Errore in gestione promemoria", t);
			try {
				Throwable innerException = ExceptionUtils.getInnerException(t, TemplateException.class);
				if(innerException != null)
					promemoriaBD.updateFallita(promemoria.getId(), t.getMessage() + ": " + innerException.getMessage());
				else
					promemoriaBD.updateFallita(promemoria.getId(), t.getMessage());
			} catch (ServiceException e1) {
				log.debug("Errore in aggiornamento promemoria [" + codApplicazione + "/"+codVersamentoEnte+ "] fallito: " + t.getMessage());
			}
		}
	}
}
