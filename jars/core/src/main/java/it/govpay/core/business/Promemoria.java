/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2024 Link.it srl (http://www.link.it).
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 3, as published by
 * the Free Software Foundation.
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
package it.govpay.core.business;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBException;

import org.apache.commons.lang3.StringUtils;
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
import org.openspcoop2.utils.transport.http.SSLConfig;
import org.slf4j.Logger;
import org.xml.sax.SAXException;

import freemarker.template.TemplateException;
import it.gov.digitpa.schemas._2011.pagamenti.CtRichiestaPagamentoTelematico;
import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.model.Documento;
import it.govpay.bd.model.Fr;
import it.govpay.bd.model.Pagamento;
import it.govpay.bd.model.Rendicontazione;
import it.govpay.bd.model.Rpt;
import it.govpay.bd.model.SingoloVersamento;
import it.govpay.bd.model.TipoVersamentoDominio;
import it.govpay.bd.model.Versamento;
import it.govpay.bd.pagamento.PromemoriaBD;
import it.govpay.bd.pagamento.VersamentiBD;
import it.govpay.core.beans.EsitoOperazione;
import it.govpay.core.business.model.PrintAvvisoDTOResponse;
import it.govpay.core.business.model.PrintAvvisoVersamentoDTO;
import it.govpay.core.dao.pagamenti.dto.LeggiRicevutaDTO;
import it.govpay.core.dao.pagamenti.dto.LeggiRicevutaDTOResponse;
import it.govpay.core.exceptions.GovPayException;
import it.govpay.core.exceptions.IOException;
import it.govpay.core.exceptions.PromemoriaException;
import it.govpay.core.exceptions.UnprocessableEntityException;
import it.govpay.core.utils.ExceptionUtils;
import it.govpay.core.utils.SimpleDateFormatUtils;
import it.govpay.core.utils.trasformazioni.Costanti;
import it.govpay.core.utils.trasformazioni.TrasformazioniUtils;
import it.govpay.core.utils.trasformazioni.exception.TrasformazioneException;
import it.govpay.model.Promemoria.TipoPromemoria;
import it.govpay.model.configurazione.AvvisaturaViaMail;
import it.govpay.model.configurazione.MailBatch;
import it.govpay.model.configurazione.MailServer;
import it.govpay.model.configurazione.PromemoriaAvviso;
import it.govpay.model.configurazione.PromemoriaRicevuta;
import it.govpay.model.configurazione.PromemoriaScadenza;
import it.govpay.model.configurazione.SslConfig;
import it.govpay.pagopa.beans.utils.JaxbUtils;

public class Promemoria {

	private static final String ERROR_MSG_ERRORE_IN_AGGIORNAMENTO_PROMEMORIA_0_1_FALLITO_2 = "Errore in aggiornamento promemoria [{}/{}] fallito: {}";

	private static final String DEBUG_MSG_SPEDIZIONE_PROMEMORIA_VERSO_IL_MAIL_SERVER_0_1_COMPLETATA = "Spedizione promemoria verso il mail server [{}]:[{}] completata.";

	private static final String DEBUG_MSG_SPEDIZIONE_PROMEMORIA_VERSO_IL_MAIL_SERVER_0_1 = "Spedizione promemoria verso il mail server [{}]:[{}]...";

	private static Logger log = LoggerWrapperFactory.getLogger(Promemoria.class);

	private Sender senderCommonsMail;

	private String host;
	private int port;
	private String username;
	private String password;
	private String from;

	private PromemoriaAvviso configurazionePromemoriaAvvisoMail;
	private PromemoriaRicevuta configurazionePromemoriaRicevutaMail;
	private PromemoriaScadenza configurazionePromemoriaScadenzaMail;

	private SSLConfig sslConfig;

	private boolean startTls;
	

	public Promemoria() {
		this.senderCommonsMail = SenderFactory.newSender(SenderType.COMMONS_MAIL, log);

		Configurazione configurazioneBD = new Configurazione();
		try {
			it.govpay.bd.model.Configurazione configurazione = configurazioneBD.getConfigurazione();
			MailBatch batchSpedizioneEmail = configurazione.getBatchSpedizioneEmail();
			MailServer mailserver = batchSpedizioneEmail.getMailserver();
			
			if(mailserver == null) { // si sta provando ad 
				throw new ServiceException("MailServer non configurato!");
			}
			
			AvvisaturaViaMail avvisaturaViaMail = configurazione.getAvvisaturaViaMail();
			this.configurazionePromemoriaAvvisoMail = avvisaturaViaMail.getPromemoriaAvviso();
			this.configurazionePromemoriaRicevutaMail = avvisaturaViaMail.getPromemoriaRicevuta();
			this.configurazionePromemoriaScadenzaMail = avvisaturaViaMail.getPromemoriaScadenza();
			
			this.host = mailserver.getHost();
			this.port = mailserver.getPort();
			this.username = mailserver.getUsername();
			this.password = mailserver.getPassword();
			this.from = mailserver.getFrom();
			if(mailserver.getReadTimeout() != null)
				this.senderCommonsMail.setReadTimeout(mailserver.getReadTimeout());
			if(mailserver.getConnectionTimeout() != null)
				this.senderCommonsMail.setConnectionTimeout(mailserver.getConnectionTimeout());
			
			SslConfig sslConfigSistema = mailserver.getSslConfig();
			if(sslConfigSistema != null && sslConfigSistema.isAbilitato()) {
				this.sslConfig = new SSLConfig();
				this.sslConfig.setSslType(sslConfigSistema.getType());
				this.sslConfig.setHostnameVerifier(sslConfigSistema.isHostnameVerifier());
				if(sslConfigSistema.getKeyStore() != null) {
					this.sslConfig.setKeyStoreLocation(sslConfigSistema.getKeyStore().getLocation());
					this.sslConfig.setKeyManagementAlgorithm(sslConfigSistema.getKeyStore().getManagementAlgorithm());
					this.sslConfig.setKeyStorePassword(sslConfigSistema.getKeyStore().getPassword());
					this.sslConfig.setKeyStoreType(sslConfigSistema.getKeyStore().getType());
				}
				
				if(sslConfigSistema.getTrustStore() != null) {
					this.sslConfig.setTrustStoreLocation(sslConfigSistema.getTrustStore().getLocation());
					this.sslConfig.setTrustManagementAlgorithm(sslConfigSistema.getTrustStore().getManagementAlgorithm());
					this.sslConfig.setTrustStorePassword(sslConfigSistema.getTrustStore().getPassword());
					this.sslConfig.setTrustStoreType(sslConfigSistema.getTrustStore().getType());
				}
				
				log.debug("Abilitazione configurazione SSL per comunicazione al mailserver");
			}
			
			this.startTls = mailserver.isStartTls();
			
		} catch (ServiceException e) {
			log.error(MessageFormat.format("Errore durante l''inizializzazione del Promemoria: {0}", e.getMessage()),e);
		} catch (IOException e) {
			log.error(MessageFormat.format("Errore durante l''inizializzazione del Promemoria: {0}", e.getMessage()),e);
		}
	}

	public it.govpay.bd.model.Promemoria creaPromemoriaRicevutaEseguitoSenzaRPT(Versamento versamento, TipoVersamentoDominio tipoVersamentoDominio) {
		it.govpay.bd.model.Promemoria promemoria = new it.govpay.bd.model.Promemoria(null, versamento, TipoPromemoria.RICEVUTA_NO_RPT);
		String debitore = versamento.getAnagraficaDebitore().getEmail();
		promemoria.setDestinatarioTo(debitore);
		if(tipoVersamentoDominio.getAvvisaturaMailPromemoriaRicevutaPdf() != null)
			promemoria.setAllegaPdf(tipoVersamentoDominio.getAvvisaturaMailPromemoriaRicevutaPdf());
		else
			promemoria.setAllegaPdf(this.configurazionePromemoriaRicevutaMail.isAllegaPdf());
		
		return promemoria;
	}
	
	public it.govpay.bd.model.Promemoria creaPromemoriaRicevuta(it.govpay.bd.model.Rpt rpt, Versamento versamento, TipoVersamentoDominio tipoVersamentoDominio) throws ServiceException, GovPayException, JAXBException, SAXException {
		it.govpay.bd.model.Promemoria promemoria = new it.govpay.bd.model.Promemoria(rpt, versamento, TipoPromemoria.RICEVUTA);
		this.setRicevutaDestinatari(rpt, versamento, promemoria); 
		if(tipoVersamentoDominio.getAvvisaturaMailPromemoriaRicevutaPdf() != null)
			promemoria.setAllegaPdf(tipoVersamentoDominio.getAvvisaturaMailPromemoriaRicevutaPdf());
		else
			promemoria.setAllegaPdf(this.configurazionePromemoriaRicevutaMail.isAllegaPdf());
		
		return promemoria;
	}


	public it.govpay.bd.model.Promemoria creaPromemoriaAvviso(Versamento versamento, TipoVersamentoDominio tipoVersamentoDominio, Date dataAvvisatura) throws ServiceException {
		
		it.govpay.bd.model.Promemoria promemoria = null;
		Documento documento = versamento.getDocumento();
		if(documento != null)
			promemoria = new it.govpay.bd.model.Promemoria(documento, TipoPromemoria.AVVISO);
		else
			promemoria = new it.govpay.bd.model.Promemoria(versamento, TipoPromemoria.AVVISO);
		
		promemoria.setDestinatarioTo(versamento.getAnagraficaDebitore().getEmail());
		
		if(tipoVersamentoDominio.getAvvisaturaMailPromemoriaAvvisoPdf() != null)
			promemoria.setAllegaPdf(tipoVersamentoDominio.getAvvisaturaMailPromemoriaAvvisoPdf());
		else
			promemoria.setAllegaPdf(this.configurazionePromemoriaAvvisoMail.isAllegaPdf());
		
		if(dataAvvisatura != null)
			promemoria.setDataProssimaSpedizione(dataAvvisatura);
		
		return promemoria;
	}
	
	public it.govpay.bd.model.Promemoria creaPromemoriaScadenza(Versamento versamento, TipoVersamentoDominio tipoVersamentoDominio, Date dataAvvisatura) throws ServiceException {
		
		it.govpay.bd.model.Promemoria promemoria = null;
		Documento documento = versamento.getDocumento();
		if(documento != null)
			promemoria = new it.govpay.bd.model.Promemoria(documento, TipoPromemoria.SCADENZA);
		else
			promemoria = new it.govpay.bd.model.Promemoria(versamento, TipoPromemoria.SCADENZA);
		
		promemoria.setDestinatarioTo(versamento.getAnagraficaDebitore().getEmail());
		
		if(dataAvvisatura != null)
			promemoria.setDataProssimaSpedizione(dataAvvisatura);
		
		return promemoria;
	}

	public void setRicevutaDestinatari(it.govpay.bd.model.Rpt rpt, Versamento versamento, it.govpay.bd.model.Promemoria promemoria) throws JAXBException, SAXException {
		String debitore = versamento.getAnagraficaDebitore().getEmail();
		String versante = null;
		
		if(rpt != null) {
			switch(rpt.getVersione()) {
				case SANP_230:
				case RPTSANP230_RTV2:
					CtRichiestaPagamentoTelematico rptCtRichiestaPagamentoTelematico = JaxbUtils.toRPT(rpt.getXmlRpt(), false);
					versante = rptCtRichiestaPagamentoTelematico.getSoggettoVersante() != null ? rptCtRichiestaPagamentoTelematico.getSoggettoVersante().getEMailVersante() : null;
					break;
				case SANP_240:
				case RPTV2_RTV1:
				case SANP_321_V2:
				case RPTV1_RTV2:
					break;
			}
		}
		
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

	public String valorizzaTemplate(String nomeTrasformazione, Map<String, Object> dynamicMap, String tipoTemplate, String template) throws ServiceException, TrasformazioneException, UnprocessableEntityException {
		try {
			if(template.startsWith("\""))
				template = template.substring(1);

			if(template.endsWith("\""))
				template = template.substring(0, template.length() - 1);
			byte[] templateBytes = Base64.getDecoder().decode(template.getBytes());
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			TrasformazioniUtils.convertFreeMarkerTemplate(nomeTrasformazione, templateBytes , dynamicMap , baos );
			log.debug(MessageFormat.format("Risultato trasformazione: {0}", baos.toString()));
			log.debug(MessageFormat.format("Risultato trasformazione UTF-8: {0}", baos.toString(Charset.UTF_8.getValue())));
			return baos.toString();
		} catch (TrasformazioneException | UnprocessableEntityException e) {
			log.error(MessageFormat.format("Trasformazione tramite template Freemarker completata con errore: {0}", e.getMessage()), e);
			throw e;
		} catch (UnsupportedEncodingException e) {
			log.error(MessageFormat.format("Trasformazione tramite template Freemarker completata con errore: {0}", e.getMessage()), e);
			throw new ServiceException(e);
		}
	}

	public String getOggettoAvviso(String tipoTemplate, String templateMessaggio, String idA2A, String idPendenza, Map<String, Object> dynamicMap)  throws ServiceException, GovPayException{
		String name = "GenerazioneOggettoPromemoriaAvviso";
		try {
			return this.valorizzaTemplate(name, dynamicMap, tipoTemplate, templateMessaggio);
		} catch (TrasformazioneException | UnprocessableEntityException e) {
			throw new GovPayException(e.getMessage(), EsitoOperazione.PRM_001, e, idA2A, idPendenza, e.getMessage());
		}
	}

	public String getMessaggioAvviso(String tipoTemplate, String templateMessaggio, String idA2A, String idPendenza, Map<String, Object> dynamicMap)  throws ServiceException, GovPayException{
		String name = "GenerazioneMessaggioPromemoriaAvviso";
		try {
			return this.valorizzaTemplate(name, dynamicMap, tipoTemplate, templateMessaggio);
		} catch (TrasformazioneException | UnprocessableEntityException e) {
			throw new GovPayException(e.getMessage(), EsitoOperazione.PRM_002, e, idA2A, idPendenza, e.getMessage());
		}
	}

	public String getOggettoRicevuta(String tipoTemplate, String templateMessaggio, it.govpay.bd.model.Rpt rpt, String idA2A, String idPendenza, Map<String, Object> dynamicMap)  throws ServiceException, GovPayException{
		String name = "GenerazioneOggettoPromemoriaRicevuta";
		try {
			return this.valorizzaTemplate(name, dynamicMap, tipoTemplate, templateMessaggio);
		} catch (TrasformazioneException | UnprocessableEntityException e) {
			throw new GovPayException(e.getMessage(), EsitoOperazione.PRM_003, e, idA2A, idPendenza, e.getMessage());
		}
	}

	public String getMessaggioRicevuta(String tipoTemplate, String templateMessaggio, it.govpay.bd.model.Rpt rpt, String idA2A, String idPendenza, Map<String, Object> dynamicMap)  throws ServiceException, GovPayException{
		String name = "GenerazioneMessaggioPromemoriaRicevuta";
		try {
			return this.valorizzaTemplate(name, dynamicMap, tipoTemplate, templateMessaggio);
		} catch (TrasformazioneException | UnprocessableEntityException e) {
			throw new GovPayException(e.getMessage(), EsitoOperazione.PRM_004, e, idA2A, idPendenza, e.getMessage());
		}
	}
	
	public String getOggettoScadenza(String tipoTemplate, String templateMessaggio, String idA2A, String idPendenza, Map<String, Object> dynamicMap)  throws ServiceException, GovPayException{
		String name = "GenerazioneOggettoPromemoriaScadenza";
		try {
			return this.valorizzaTemplate(name, dynamicMap, tipoTemplate, templateMessaggio);
		} catch (TrasformazioneException | UnprocessableEntityException e) {
			throw new GovPayException(e.getMessage(), EsitoOperazione.PRM_001, e, idA2A, idPendenza, e.getMessage());
		}
	}

	public String getMessaggioScadenza(String tipoTemplate, String templateMessaggio, String idA2A, String idPendenza, Map<String, Object> dynamicMap)  throws ServiceException, GovPayException{
		String name = "GenerazioneMessaggioPromemoriaScadenza";
		try {
			return this.valorizzaTemplate(name, dynamicMap, tipoTemplate, templateMessaggio);
		} catch (TrasformazioneException | UnprocessableEntityException e) {
			throw new GovPayException(e.getMessage(), EsitoOperazione.PRM_002, e, idA2A, idPendenza, e.getMessage());
		}
	}

	public List<it.govpay.bd.model.Promemoria> findPromemoriaDaSpedire(Integer offset, Integer limit) throws ServiceException{
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), true);
		PromemoriaBD promemoriaBD = new PromemoriaBD(configWrapper);
		List<it.govpay.bd.model.Promemoria> promemoria = promemoriaBD.findPromemoriaDaSpedire(offset,limit,true);
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
		case SCADENZA:
			this.invioPromemoriaScadenza(promemoria);
			break;
		case RICEVUTA_NO_RPT:
			this.invioPromemoriaRicevutaPagamentoEseguitoSenzaRPT(promemoria); 
			break;
		}
	}



	private void invioPromemoriaAvviso(it.govpay.bd.model.Promemoria promemoria) {
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), true);
		PromemoriaBD promemoriaBD = new PromemoriaBD(configWrapper);
		String errore = "", codApplicazione = "", codVersamentoEnte  = "";
		try {
			Versamento versamento = promemoria.getVersamento();
			codApplicazione = versamento.getApplicazione(configWrapper).getCodApplicazione();
			codVersamentoEnte = versamento.getCodVersamentoEnte();
			TipoVersamentoDominio tipoVersamentoDominio = versamento.getTipoVersamentoDominio(configWrapper);

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
			mail.setSslConfig(this.sslConfig);
			mail.setStartTls(this.startTls);
			mail.setFrom(this.from);
			mail.setTo(promemoria.getDestinatarioTo());
			if(promemoria.getDestinatarioCc() !=null)
				mail.setCc(Arrays.asList(promemoria.getDestinatarioCc()));

			log.debug(MessageFormat.format("Invio promemoria avviso di pagamento per la pendenza [IDA2A: {0} , IdPendenza: {1}], al destinatario [{2}] CC[{3}]",
					versamento.getApplicazione(configWrapper).getCodApplicazione(), versamento.getCodVersamentoEnte(), promemoria.getDestinatarioTo(), (promemoria.getDestinatarioCc() !=null ? promemoria.getDestinatarioCc() : "")));

			boolean inserisciOggetto = promemoria.getOggetto() == null;
			if(promemoria.getOggetto() == null) {
				log.debug("Creazione oggetto del promemoria...");
				try {
					Map<String, Object> dynamicMap = new HashMap<>();
					TrasformazioniUtils.fillDynamicMapPromemoriaAvviso(log, dynamicMap, ContextThreadLocal.get(), versamento, versamento.getDominio(configWrapper));
					String promemoriaAvvisoOggetto = tipoVersamentoDominio.getAvvisaturaMailPromemoriaAvvisoOggetto() != null ? tipoVersamentoDominio.getAvvisaturaMailPromemoriaAvvisoOggetto() : this.configurazionePromemoriaAvvisoMail.getOggetto();
					String promemoriaAvvisoTipoTemplate = tipoVersamentoDominio.getAvvisaturaMailPromemoriaAvvisoTipo() != null ? tipoVersamentoDominio.getAvvisaturaMailPromemoriaAvvisoTipo() : this.configurazionePromemoriaAvvisoMail.getTipo(); 
					promemoria.setOggetto(this.getOggettoAvviso(promemoriaAvvisoTipoTemplate, promemoriaAvvisoOggetto, versamento.getApplicazione(configWrapper).getCodApplicazione(), versamento.getCodVersamentoEnte(), dynamicMap));
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
					Map<String, Object> dynamicMap = new HashMap<>();
					TrasformazioniUtils.fillDynamicMapPromemoriaAvviso(log, dynamicMap, ContextThreadLocal.get(), versamento, versamento.getDominio(configWrapper));
					String promemoriaAvvisoMessaggio = tipoVersamentoDominio.getAvvisaturaMailPromemoriaAvvisoMessaggio() != null ? tipoVersamentoDominio.getAvvisaturaMailPromemoriaAvvisoMessaggio() : this.configurazionePromemoriaAvvisoMail.getMessaggio();
					String promemoriaAvvisoTipoTemplate = tipoVersamentoDominio.getAvvisaturaMailPromemoriaAvvisoTipo() != null ? tipoVersamentoDominio.getAvvisaturaMailPromemoriaAvvisoTipo() : this.configurazionePromemoriaAvvisoMail.getTipo(); 
					promemoria.setMessaggio(this.getMessaggioAvviso(promemoriaAvvisoTipoTemplate, promemoriaAvvisoMessaggio, versamento.getApplicazione(configWrapper).getCodApplicazione(), versamento.getCodVersamentoEnte(), dynamicMap));
					promemoria.setContentType(this.getContentType(dynamicMap));
				} catch (Throwable t) {
					throw new PromemoriaException("Corpo del messaggio non generabile", t);
				}
				log.debug("Creazione messaggio del promemoria completata.");
			}
			mail.getBody().setMessage(promemoria.getMessaggio());

			if(promemoria.isAllegaPdf()) {
				AvvisoPagamento avvisoPagamento = new AvvisoPagamento();
				PrintAvvisoVersamentoDTO printAvviso = new PrintAvvisoVersamentoDTO();
				printAvviso.setVersamento(versamento);
				printAvviso.setCodDominio(versamento.getDominio(configWrapper).getCodDominio());
				printAvviso.setIuv(versamento.getIuvVersamento());
				printAvviso.setSalvaSuDB(false);
				printAvviso.setSdfDataScadenza(SimpleDateFormatUtils.newSimpleDateFormatGGMMAAAA());
				PrintAvvisoDTOResponse printAvvisoDTOResponse = avvisoPagamento.printAvvisoVersamento(printAvviso);

				String attachmentName = versamento.getDominio(configWrapper).getCodDominio() + "_" + versamento.getNumeroAvviso() + ".pdf";
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
				log.debug(DEBUG_MSG_SPEDIZIONE_PROMEMORIA_VERSO_IL_MAIL_SERVER_0_1, this.host,	this.port);
				this.senderCommonsMail.send(mail, true);
				log.debug(DEBUG_MSG_SPEDIZIONE_PROMEMORIA_VERSO_IL_MAIL_SERVER_0_1_COMPLETATA, this.host, this.port);
				promemoriaBD.updateSpedito(promemoria.getId());
			}catch (UtilsException e) {
				errore = MessageFormat.format("Errore durante l''invio del promemoria avviso di pagamento per la pendenza [IDA2A: {0} , IdPendenza: {1}] al destinatario [{2}] CC[{3}]:{4}",
						versamento.getApplicazione(configWrapper).getCodApplicazione(), versamento.getCodVersamentoEnte(), promemoria.getDestinatarioTo(), (promemoria.getDestinatarioCc() !=null ? promemoria.getDestinatarioCc() : ""), e.getMessage());

				gestisciUtilsException(promemoria, promemoriaBD, errore, e);
			} 
		} catch (PromemoriaException e) {
			gestisciPromemoriaException(promemoria, promemoriaBD, codApplicazione, codVersamentoEnte, e);
		} catch (Throwable t) {
			gestisciThrowable(promemoria, promemoriaBD, codApplicazione, codVersamentoEnte, t);
		}
	}

	private void invioPromemoriaRicevutaPagamentoEseguitoSenzaRPT(it.govpay.bd.model.Promemoria promemoria) {
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), true);
		PromemoriaBD promemoriaBD = new PromemoriaBD(configWrapper);
		String errore = "", codApplicazione = "", codVersamentoEnte  = "";
		try {
			Versamento versamento = promemoria.getVersamento();
			codApplicazione = versamento.getApplicazione(configWrapper).getCodApplicazione();
			codVersamentoEnte = versamento.getCodVersamentoEnte();
			
			TipoVersamentoDominio tipoVersamentoDominio = versamento.getTipoVersamentoDominio(configWrapper);

			if(StringUtils.isEmpty(promemoria.getDestinatarioTo())){
				throw new PromemoriaException("Destinatario messaggio non specificato");
			}
			
			
			// Generazione del pdf della quietanza e spedizione
			QuietanzaPagamento quietanzaPagamento = new QuietanzaPagamento();
			
			VersamentiBD versamentiBD = null;
			SingoloVersamento singoloVersamento = null;
			Pagamento pagamento = null;
			Rendicontazione rendicontazione = null;
			Fr fr = null;
			Rpt rpt = null;
			try {
				versamentiBD = new VersamentiBD(configWrapper);
				
				versamentiBD.setupConnection(configWrapper.getTransactionID());
				
				versamentiBD.setAtomica(false);
				
				List<SingoloVersamento> singoliVersamenti = versamento.getSingoliVersamenti(versamentiBD);
				// in questo caso avremmo solo 1 singolo versamento
				singoloVersamento = singoliVersamenti.get(0);
				List<Pagamento> pagamenti = singoloVersamento.getPagamenti(versamentiBD);
				pagamento = pagamenti.get(0);
				List<Rendicontazione> rendicontazioni = pagamento.getRendicontazioni(versamentiBD);
				rendicontazione = rendicontazioni.get(0);
				fr = rendicontazione.getFr(versamentiBD);
				
				rpt = quietanzaPagamento.creaRPTFromRendicontazione(rendicontazione, pagamento, singoloVersamento, versamento, fr, configWrapper);
			} finally {
				if(versamentiBD != null) {
					versamentiBD.closeConnection();
				}
			}

			org.openspcoop2.utils.mail.Mail mail = new org.openspcoop2.utils.mail.Mail();

			mail.setServerHost(this.host);
			mail.setServerPort(this.port);

			if(this.username != null && !this.username.isEmpty() && this.password != null && !this.password.isEmpty()) {
				mail.setUsername(this.username);
				mail.setPassword(this.password);
			}

			mail.setSslConfig(this.sslConfig);
			mail.setStartTls(this.startTls);

			mail.setFrom(this.from);
			mail.setTo(promemoria.getDestinatarioTo());
			if(promemoria.getDestinatarioCc() !=null)
				mail.setCc(Arrays.asList(promemoria.getDestinatarioCc()));

			log.debug(MessageFormat.format("Invio promemoria ricevuta di pagamento per la pendenza [IDA2A: {0} , IdPendenza: {1}], al destinatario [{2}] CC[{3}]",
					versamento.getApplicazione(configWrapper).getCodApplicazione(), versamento.getCodVersamentoEnte(), promemoria.getDestinatarioTo(), (promemoria.getDestinatarioCc() !=null ? promemoria.getDestinatarioCc() : "")));

			boolean inserisciOggetto = promemoria.getOggetto() == null;
			if(promemoria.getOggetto() == null) {
				log.debug("Creazione oggetto del promemoria...");
				try {
				Map<String, Object> dynamicMap = new HashMap<>();
				TrasformazioniUtils.fillDynamicMapPromemoriaRicevuta(log, dynamicMap, ContextThreadLocal.get(), rpt, versamento, versamento.getDominio(configWrapper));
				String promemoriaRicevutaTipoTemplate = tipoVersamentoDominio.getAvvisaturaMailPromemoriaRicevutaTipo() != null ? tipoVersamentoDominio.getAvvisaturaMailPromemoriaRicevutaTipo() : this.configurazionePromemoriaRicevutaMail.getTipo(); 
				String promemoriaRicevutaOggetto = tipoVersamentoDominio.getAvvisaturaMailPromemoriaRicevutaOggetto() != null ? tipoVersamentoDominio.getAvvisaturaMailPromemoriaRicevutaOggetto() : this.configurazionePromemoriaRicevutaMail.getOggetto();
				promemoria.setOggetto(this.getOggettoRicevuta(promemoriaRicevutaTipoTemplate, promemoriaRicevutaOggetto, rpt, versamento.getApplicazione(configWrapper).getCodApplicazione(), versamento.getCodVersamentoEnte(), dynamicMap));
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
					Map<String, Object> dynamicMap = new HashMap<>();
				TrasformazioniUtils.fillDynamicMapPromemoriaRicevuta(log, dynamicMap, ContextThreadLocal.get(), rpt, versamento, versamento.getDominio(configWrapper));
				String promemoriaRicevutaTipoTemplate = tipoVersamentoDominio.getAvvisaturaMailPromemoriaRicevutaTipo() != null ? tipoVersamentoDominio.getAvvisaturaMailPromemoriaRicevutaTipo() : this.configurazionePromemoriaRicevutaMail.getTipo(); 
				String promemoriaRicevutaMessaggio = tipoVersamentoDominio.getAvvisaturaMailPromemoriaRicevutaMessaggio() != null ? tipoVersamentoDominio.getAvvisaturaMailPromemoriaRicevutaMessaggio() : this.configurazionePromemoriaRicevutaMail.getMessaggio();
				promemoria.setMessaggio(this.getMessaggioRicevuta(promemoriaRicevutaTipoTemplate, promemoriaRicevutaMessaggio, rpt, versamento.getApplicazione(configWrapper).getCodApplicazione(), versamento.getCodVersamentoEnte(), dynamicMap));
				promemoria.setContentType(this.getContentType(dynamicMap));
				} catch (Throwable t) {
					throw new PromemoriaException("Corpo del messaggio non generabile", t);
				}
				log.debug("Creazione messaggio del promemoria completata.");
			}
			mail.getBody().setMessage(promemoria.getMessaggio());


			if(promemoria.isAllegaPdf()) {
				// Generazione del pdf della quietanza e spedizione
				byte[] pdfQuietanzaPagamento = quietanzaPagamento.creaPdfQuietanzaPagamento(rendicontazione, pagamento, singoloVersamento, versamento, fr);
				
				String codDominio = pagamento.getCodDominio();
				String iuv = pagamento.getIuv();
				String ccp = pagamento.getIur();

				String attachmentName = MessageFormat.format("{0}_{1}_{2}.pdf", codDominio, iuv, ccp);
				MailAttach avvisoAttach = new MailBinaryAttach(attachmentName, pdfQuietanzaPagamento);

				mail.getBody().getAttachments().add(avvisoAttach);
			}

			// salvo il contenuto del promemoria
			if(inserisciMessaggio || inserisciOggetto) {
				log.debug("Salvataggio messaggio e oggetto del promemoria...");
				promemoriaBD.updateMessaggioPromemoria(promemoria.getId(), promemoria.getOggetto(), promemoria.getMessaggio(), promemoria.getContentType()); 
				log.debug("Salvataggio messaggio e oggetto del promemoria completato.");
			}

			try {
				log.debug(DEBUG_MSG_SPEDIZIONE_PROMEMORIA_VERSO_IL_MAIL_SERVER_0_1, this.host,	this.port);
				this.senderCommonsMail.send(mail, true);
				log.debug(DEBUG_MSG_SPEDIZIONE_PROMEMORIA_VERSO_IL_MAIL_SERVER_0_1_COMPLETATA, this.host, this.port);
				promemoriaBD.updateSpedito(promemoria.getId());
			}catch (UtilsException e) {
				errore = MessageFormat.format("Errore durante l''invio del promemoria ricevuta di pagamento per la pendenza [IDA2A: {0} , IdPendenza: {1}] al destinatario [{2}] CC[{3}]:{4}",
						versamento.getApplicazione(configWrapper).getCodApplicazione(), versamento.getCodVersamentoEnte(), promemoria.getDestinatarioTo(), (promemoria.getDestinatarioCc() !=null ? promemoria.getDestinatarioCc() : ""), e.getMessage());
				
				gestisciUtilsException(promemoria, promemoriaBD, errore, e);
			}
		} catch (PromemoriaException e) {
			gestisciPromemoriaException(promemoria, promemoriaBD, codApplicazione, codVersamentoEnte, e);
		} catch (Throwable t) {
			gestisciThrowable(promemoria, promemoriaBD, codApplicazione, codVersamentoEnte, t);
		}
	}
	
	private void invioPromemoriaRicevuta(it.govpay.bd.model.Promemoria promemoria) {
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), true);
		PromemoriaBD promemoriaBD = new PromemoriaBD(configWrapper);
		String errore = "", codApplicazione = "", codVersamentoEnte  = "";
		try {
			Versamento versamento = promemoria.getVersamento();
			codApplicazione = versamento.getApplicazione(configWrapper).getCodApplicazione();
			codVersamentoEnte = versamento.getCodVersamentoEnte();
			
			TipoVersamentoDominio tipoVersamentoDominio = versamento.getTipoVersamentoDominio(configWrapper);
			Rpt rpt = promemoria.getRpt();

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

			mail.setSslConfig(this.sslConfig);
			mail.setStartTls(this.startTls);

			mail.setFrom(this.from);
			mail.setTo(promemoria.getDestinatarioTo());
			if(promemoria.getDestinatarioCc() !=null)
				mail.setCc(Arrays.asList(promemoria.getDestinatarioCc()));

			log.debug(MessageFormat.format("Invio promemoria ricevuta di pagamento per la pendenza [IDA2A: {0} , IdPendenza: {1}], al destinatario [{2}] CC[{3}]",
					versamento.getApplicazione(configWrapper).getCodApplicazione(), versamento.getCodVersamentoEnte(), promemoria.getDestinatarioTo(), (promemoria.getDestinatarioCc() !=null ? promemoria.getDestinatarioCc() : "")));

			boolean inserisciOggetto = promemoria.getOggetto() == null;
			if(promemoria.getOggetto() == null) {
				log.debug("Creazione oggetto del promemoria...");
				try {
				Map<String, Object> dynamicMap = new HashMap<>();
				TrasformazioniUtils.fillDynamicMapPromemoriaRicevuta(log, dynamicMap, ContextThreadLocal.get(), rpt, versamento, versamento.getDominio(configWrapper));
				String promemoriaRicevutaTipoTemplate = tipoVersamentoDominio.getAvvisaturaMailPromemoriaRicevutaTipo() != null ? tipoVersamentoDominio.getAvvisaturaMailPromemoriaRicevutaTipo() : this.configurazionePromemoriaRicevutaMail.getTipo(); 
				String promemoriaRicevutaOggetto = tipoVersamentoDominio.getAvvisaturaMailPromemoriaRicevutaOggetto() != null ? tipoVersamentoDominio.getAvvisaturaMailPromemoriaRicevutaOggetto() : this.configurazionePromemoriaRicevutaMail.getOggetto();
				promemoria.setOggetto(this.getOggettoRicevuta(promemoriaRicevutaTipoTemplate, promemoriaRicevutaOggetto, rpt, versamento.getApplicazione(configWrapper).getCodApplicazione(), versamento.getCodVersamentoEnte(), dynamicMap));
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
					Map<String, Object> dynamicMap = new HashMap<>();
				TrasformazioniUtils.fillDynamicMapPromemoriaRicevuta(log, dynamicMap, ContextThreadLocal.get(), rpt, versamento, versamento.getDominio(configWrapper));
				String promemoriaRicevutaTipoTemplate = tipoVersamentoDominio.getAvvisaturaMailPromemoriaRicevutaTipo() != null ? tipoVersamentoDominio.getAvvisaturaMailPromemoriaRicevutaTipo() : this.configurazionePromemoriaRicevutaMail.getTipo(); 
				String promemoriaRicevutaMessaggio = tipoVersamentoDominio.getAvvisaturaMailPromemoriaRicevutaMessaggio() != null ? tipoVersamentoDominio.getAvvisaturaMailPromemoriaRicevutaMessaggio() : this.configurazionePromemoriaRicevutaMail.getMessaggio();
				promemoria.setMessaggio(this.getMessaggioRicevuta(promemoriaRicevutaTipoTemplate, promemoriaRicevutaMessaggio, rpt, versamento.getApplicazione(configWrapper).getCodApplicazione(), versamento.getCodVersamentoEnte(), dynamicMap));
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
//				rpt.getPagamentoPortale().getApplicazione(configWrapper);

				it.govpay.core.business.RicevutaTelematica avvisoBD = new it.govpay.core.business.RicevutaTelematica();
				LeggiRicevutaDTO leggiRicevutaDTO = new LeggiRicevutaDTO(null);
				leggiRicevutaDTO.setIdDominio(codDominio);
				leggiRicevutaDTO.setIuv(iuv);
				leggiRicevutaDTO.setCcp(ccp);
				LeggiRicevutaDTOResponse response = avvisoBD.creaPdfRicevuta(leggiRicevutaDTO,rpt);

				String attachmentName = MessageFormat.format("{0}_{1}_{2}.pdf", codDominio, iuv, ccp);
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
				log.debug(DEBUG_MSG_SPEDIZIONE_PROMEMORIA_VERSO_IL_MAIL_SERVER_0_1, this.host,	this.port);
				this.senderCommonsMail.send(mail, true);
				log.debug(DEBUG_MSG_SPEDIZIONE_PROMEMORIA_VERSO_IL_MAIL_SERVER_0_1_COMPLETATA, this.host, this.port);
				promemoriaBD.updateSpedito(promemoria.getId());
			}catch (UtilsException e) {
				errore = MessageFormat.format("Errore durante l''invio del promemoria ricevuta di pagamento per la pendenza [IDA2A: {0} , IdPendenza: {1}] al destinatario [{2}] CC[{3}]:{4}",
						versamento.getApplicazione(configWrapper).getCodApplicazione(), versamento.getCodVersamentoEnte(), promemoria.getDestinatarioTo(), (promemoria.getDestinatarioCc() !=null ? promemoria.getDestinatarioCc() : ""), e.getMessage());
				
				gestisciUtilsException(promemoria, promemoriaBD, errore, e);
			}
		} catch (PromemoriaException e) {
			gestisciPromemoriaException(promemoria, promemoriaBD, codApplicazione, codVersamentoEnte, e);
		} catch (Throwable t) {
			gestisciThrowable(promemoria, promemoriaBD, codApplicazione, codVersamentoEnte, t);
		}
	}
	
	private void invioPromemoriaScadenza(it.govpay.bd.model.Promemoria promemoria) {
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), true);
		PromemoriaBD promemoriaBD = new PromemoriaBD(configWrapper);
		String errore = "", codApplicazione = "", codVersamentoEnte  = "";
		try {
			Versamento versamento = promemoria.getVersamento();
			codApplicazione = versamento.getApplicazione(configWrapper).getCodApplicazione();
			codVersamentoEnte = versamento.getCodVersamentoEnte();
			TipoVersamentoDominio tipoVersamentoDominio = versamento.getTipoVersamentoDominio(configWrapper); 

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
			mail.setSslConfig(this.sslConfig);
			mail.setStartTls(this.startTls);
			mail.setFrom(this.from);
			mail.setTo(promemoria.getDestinatarioTo());
			if(promemoria.getDestinatarioCc() !=null)
				mail.setCc(Arrays.asList(promemoria.getDestinatarioCc()));

			log.debug(MessageFormat.format("Invio promemoria scadenza avviso di pagamento per la pendenza [IDA2A: {0} , IdPendenza: {1}], al destinatario [{2}] CC[{3}]",
					versamento.getApplicazione(configWrapper).getCodApplicazione(), versamento.getCodVersamentoEnte(), promemoria.getDestinatarioTo(), (promemoria.getDestinatarioCc() !=null ? promemoria.getDestinatarioCc() : "")));

			boolean inserisciOggetto = promemoria.getOggetto() == null;
			if(promemoria.getOggetto() == null) {
				log.debug("Creazione oggetto del promemoria...");
				try {
					Map<String, Object> dynamicMap = new HashMap<>();
					TrasformazioniUtils.fillDynamicMapPromemoriaScadenza(log, dynamicMap, ContextThreadLocal.get(), versamento, versamento.getDominio(configWrapper));
					String promemoriaAvvisoOggetto = tipoVersamentoDominio.getAvvisaturaMailPromemoriaScadenzaOggetto() != null ? tipoVersamentoDominio.getAvvisaturaMailPromemoriaScadenzaOggetto() : this.configurazionePromemoriaScadenzaMail.getOggetto();
					String promemoriaAvvisoTipoTemplate = tipoVersamentoDominio.getAvvisaturaMailPromemoriaScadenzaTipo() != null ? tipoVersamentoDominio.getAvvisaturaMailPromemoriaScadenzaTipo() : this.configurazionePromemoriaScadenzaMail.getTipo(); 
					promemoria.setOggetto(this.getOggettoScadenza(promemoriaAvvisoTipoTemplate, promemoriaAvvisoOggetto, versamento.getApplicazione(configWrapper).getCodApplicazione(), versamento.getCodVersamentoEnte(), dynamicMap));
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
					Map<String, Object> dynamicMap = new HashMap<>();
					TrasformazioniUtils.fillDynamicMapPromemoriaScadenza(log, dynamicMap, ContextThreadLocal.get(), versamento, versamento.getDominio(configWrapper));
					String promemoriaAvvisoMessaggio = tipoVersamentoDominio.getAvvisaturaMailPromemoriaScadenzaMessaggio() != null ? tipoVersamentoDominio.getAvvisaturaMailPromemoriaScadenzaMessaggio() : this.configurazionePromemoriaScadenzaMail.getMessaggio();
					String promemoriaAvvisoTipoTemplate = tipoVersamentoDominio.getAvvisaturaMailPromemoriaScadenzaTipo() != null ? tipoVersamentoDominio.getAvvisaturaMailPromemoriaScadenzaTipo() : this.configurazionePromemoriaScadenzaMail.getTipo(); 
					promemoria.setMessaggio(this.getMessaggioScadenza(promemoriaAvvisoTipoTemplate, promemoriaAvvisoMessaggio, versamento.getApplicazione(configWrapper).getCodApplicazione(), versamento.getCodVersamentoEnte(), dynamicMap));
					promemoria.setContentType(this.getContentType(dynamicMap));
				} catch (Throwable t) {
					throw new PromemoriaException("Corpo del messaggio non generabile", t);
				}
				log.debug("Creazione messaggio del promemoria completata.");
			}
			mail.getBody().setMessage(promemoria.getMessaggio());

			if(promemoria.isAllegaPdf()) {
				AvvisoPagamento avvisoPagamento = new AvvisoPagamento();
				PrintAvvisoVersamentoDTO printAvviso = new PrintAvvisoVersamentoDTO();
				printAvviso.setVersamento(versamento);
				printAvviso.setCodDominio(versamento.getDominio(configWrapper).getCodDominio());
				printAvviso.setIuv(versamento.getIuvVersamento());
				printAvviso.setSalvaSuDB(false);
				printAvviso.setSdfDataScadenza(SimpleDateFormatUtils.newSimpleDateFormatGGMMAAAA());
				PrintAvvisoDTOResponse printAvvisoDTOResponse = avvisoPagamento.printAvvisoVersamento(printAvviso);

				String attachmentName = versamento.getDominio(configWrapper).getCodDominio() + "_" + versamento.getNumeroAvviso() + ".pdf";
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
				log.debug(DEBUG_MSG_SPEDIZIONE_PROMEMORIA_VERSO_IL_MAIL_SERVER_0_1, this.host,	this.port);
				this.senderCommonsMail.send(mail, true);
				log.debug(DEBUG_MSG_SPEDIZIONE_PROMEMORIA_VERSO_IL_MAIL_SERVER_0_1_COMPLETATA, this.host, this.port);
				promemoriaBD.updateSpedito(promemoria.getId());
			}catch (UtilsException e) {
				errore = MessageFormat.format("Errore durante l''invio del promemoria scadenza avviso di pagamento per la pendenza [IDA2A: {0} , IdPendenza: {1}] al destinatario [{2}] CC[{3}]:{4}",
						versamento.getApplicazione(configWrapper).getCodApplicazione(), versamento.getCodVersamentoEnte(), promemoria.getDestinatarioTo(), (promemoria.getDestinatarioCc() !=null ? promemoria.getDestinatarioCc() : ""), e.getMessage());
				gestisciUtilsException(promemoria, promemoriaBD, errore, e);
			} 
		} catch (PromemoriaException e) {
			gestisciPromemoriaException(promemoria, promemoriaBD, codApplicazione, codVersamentoEnte, e);
		} catch (Throwable t) {
			gestisciThrowable(promemoria, promemoriaBD, codApplicazione, codVersamentoEnte, t);
		}
	}
	
	private void gestisciUtilsException(it.govpay.bd.model.Promemoria promemoria, PromemoriaBD promemoriaBD, String errore, UtilsException e) throws ServiceException {
		Throwable innerException = ExceptionUtils.estraiInnerExceptionDaUtilsException(e);
		
		if(innerException != null) {
			log.info(errore, e);
			log.debug("La spedizione del promemoria si e' conclusa con errore che non prevede la rispedizione...");
			promemoriaBD.updateFallita(promemoria.getId(), innerException.getMessage());
			log.debug("Salvataggio stato 'fallito' completato con successo");
		} else {
			log.error(errore, e);
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

	private void gestisciThrowable(it.govpay.bd.model.Promemoria promemoria, PromemoriaBD promemoriaBD, String codApplicazione,
			String codVersamentoEnte, Throwable t) {
		log.error("Errore in gestione promemoria", t);
		try {
			Throwable innerException = ExceptionUtils.getInnerException(t, TemplateException.class);
			if(innerException != null)
				promemoriaBD.updateFallita(promemoria.getId(), t.getMessage() + ": " + innerException.getMessage());
			else
				promemoriaBD.updateFallita(promemoria.getId(), t.getMessage());
		} catch (ServiceException e1) {
			log.debug(ERROR_MSG_ERRORE_IN_AGGIORNAMENTO_PROMEMORIA_0_1_FALLITO_2, codApplicazione, codVersamentoEnte, e1.getMessage());
		}
	}

	private void gestisciPromemoriaException(it.govpay.bd.model.Promemoria promemoria, PromemoriaBD promemoriaBD, String codApplicazione,
			String codVersamentoEnte, PromemoriaException e) {
		log.debug("Errore in gestione promemoria: {}", e.getMessage());
		try {
			Throwable innerException = ExceptionUtils.getInnerException(e, TemplateException.class);
			if(innerException != null)
				promemoriaBD.updateFallita(promemoria.getId(), e.getMessage() + ": " + innerException.getMessage());
			else
				promemoriaBD.updateFallita(promemoria.getId(), e.getMessage());
		} catch (ServiceException e1) {
			log.debug(ERROR_MSG_ERRORE_IN_AGGIORNAMENTO_PROMEMORIA_0_1_FALLITO_2, codApplicazione, codVersamentoEnte, e1.getMessage());
		}
	}

}
