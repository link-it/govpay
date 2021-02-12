/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2017 Link.it srl (http://www.link.it).
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
package it.govpay.core.utils.thread;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;

import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.LoggerWrapperFactory;
import org.openspcoop2.utils.UtilsException;
import org.openspcoop2.utils.logger.beans.Property;
import org.openspcoop2.utils.logger.beans.context.core.Role;
import org.openspcoop2.utils.mail.MailAttach;
import org.openspcoop2.utils.mail.MailBinaryAttach;
import org.openspcoop2.utils.mail.Sender;
import org.openspcoop2.utils.mail.SenderFactory;
import org.openspcoop2.utils.mail.SenderType;
import org.openspcoop2.utils.serialization.IDeserializer;
import org.openspcoop2.utils.serialization.IOException;
import org.openspcoop2.utils.serialization.ISerializer;
import org.openspcoop2.utils.serialization.SerializationConfig;
import org.openspcoop2.utils.serialization.SerializationFactory;
import org.openspcoop2.utils.serialization.SerializationFactory.SERIALIZATION_TYPE;
import org.openspcoop2.utils.service.context.ContextThreadLocal;
import org.openspcoop2.utils.service.context.IContext;
import org.openspcoop2.utils.service.context.MD5Constants;
import org.openspcoop2.utils.transport.http.SSLConfig;
import org.slf4j.Logger;
import org.slf4j.MDC;

import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.GovpayConfig;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.bd.configurazione.model.Giornale;
import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.TracciatoNotificaPagamenti;
import it.govpay.bd.pagamento.EventiBD;
import it.govpay.bd.pagamento.TracciatiNotificaPagamentiBD;
import it.govpay.core.beans.EsitoOperazione;
import it.govpay.core.exceptions.GovPayException;
import it.govpay.core.utils.EventoContext;
import it.govpay.core.utils.EventoContext.Categoria;
import it.govpay.core.utils.EventoContext.Esito;
import it.govpay.core.utils.ExceptionUtils;
import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.SimpleDateFormatUtils;
import it.govpay.core.utils.client.BasicClient.ClientException;
import it.govpay.model.ConnettoreNotificaPagamenti;
import it.govpay.model.TracciatoNotificaPagamenti.STATO_ELABORAZIONE;
import it.govpay.model.TracciatoNotificaPagamenti.TIPO_TRACCIATO;

public class SpedizioneTracciatoNotificaPagamentiThread implements Runnable {

	public static final String CONNETTORE_NOTIFICA_DISABILITATO = "Connettore Notifica non configurato";
	public static final String TRACCIATO_NOTIFICA_FLUSSO_PAGAMENTI = "TracciatoNotificaPagamenti";
	private static Logger log = LoggerWrapperFactory.getLogger(SpedizioneTracciatoNotificaPagamentiThread.class);
	private TracciatoNotificaPagamenti tracciato;
	private Dominio dominio = null;
	private boolean completed = false;
	private boolean errore = false;
	private ConnettoreNotificaPagamenti connettore = null;
	private IContext ctx = null;
	private Giornale giornale = null;
	private TIPO_TRACCIATO tipoTracciato = null;

	public SpedizioneTracciatoNotificaPagamentiThread(TracciatoNotificaPagamenti tracciato, ConnettoreNotificaPagamenti connettore, IContext ctx) throws ServiceException {
		// Verifico che tutti i campi siano valorizzati
		this.ctx = ctx;
		BDConfigWrapper configWrapper = new BDConfigWrapper(this.ctx.getTransactionId(), true);
		this.tracciato = tracciato;
		this.tipoTracciato = this.tracciato.getTipo();
		this.dominio = this.tracciato.getDominio(configWrapper);
		this.connettore = connettore;
		this.giornale = new it.govpay.core.business.Configurazione().getConfigurazione().getGiornale();
	}

	@Override
	public void run() {
		ContextThreadLocal.set(this.ctx);
		
		IContext ctx = ContextThreadLocal.get();
		GpContext appContext = (GpContext) ctx.getApplicationContext();
		MDC.put(MD5Constants.TRANSACTION_ID, ctx.getTransactionId());
		BDConfigWrapper configWrapper = new BDConfigWrapper(ctx.getTransactionId(), true);
		TracciatiNotificaPagamentiBD tracciatiMyPivotBD = null;
//		NotificaClient client = null;
		ISerializer serializer = null;
		it.govpay.core.beans.tracciati.TracciatoNotificaPagamenti beanDati = null;
		EventoContext eventoContext = new EventoContext();
		try {
			tracciatiMyPivotBD = new TracciatiNotificaPagamentiBD(configWrapper);
			SerializationConfig config = new SerializationConfig();
			config.setDf(SimpleDateFormatUtils.newSimpleDateFormatDataOreMinuti());
			config.setIgnoreNullValues(true);
			IDeserializer deserializer = SerializationFactory.getDeserializer(SERIALIZATION_TYPE.JSON_JACKSON, config);
			serializer = SerializationFactory.getSerializer(SERIALIZATION_TYPE.JSON_JACKSON, config);

			beanDati = (it.govpay.core.beans.tracciati.TracciatoNotificaPagamenti) deserializer.getObject(tracciato.getBeanDati(), it.govpay.core.beans.tracciati.TracciatoNotificaPagamenti.class);
			
			log.info("Spedizione del tracciato " + this.tipoTracciato + " "  + this.tracciato.getNomeFile() +"] al connettore previsto dalla configurazione...");
			
			if(connettore == null || !connettore.isAbilitato()) {
				ctx.getApplicationLogger().log("tracciatoNotificaPagamenti.annullato");
				log.info("Connettore " + this.tipoTracciato + " non configurato per il Dominio [Id: " + this.dominio.getCodDominio() + "]. Spedizione inibita.");
				
				tracciatiMyPivotBD.setupConnection(configWrapper.getTransactionID());
				
				this.tracciato.setStato(STATO_ELABORAZIONE.ERROR_LOAD);
				beanDati.setStepElaborazione(STATO_ELABORAZIONE.ERROR_LOAD.name());
				beanDati.setDescrizioneStepElaborazione("Connettore "+ this.tipoTracciato +" non configurato per il Dominio [Id: " + this.dominio.getCodDominio() + "]. Spedizione inibita.");
				try {
					this.tracciato.setBeanDati(serializer.getObject(beanDati));
				} catch (IOException e1) {}
				
				tracciatiMyPivotBD.updateFineElaborazione(this.tracciato);
				return;
			}
			
			
			eventoContext.setCategoriaEvento(Categoria.INTERFACCIA);
			eventoContext.setRole(Role.CLIENT);
			eventoContext.setDataRichiesta(new Date());
			eventoContext.setCodDominio(dominio.getCodDominio());
			
			String url = null;
			String operationId = null;
			switch (this.connettore.getTipoConnettore()) {
			case EMAIL:
				url = this.connettore.getEmailIndirizzo();
				operationId = appContext.setupNotificaPagamentiClient(TRACCIATO_NOTIFICA_FLUSSO_PAGAMENTI, url);
				appContext.getServerByOperationId(operationId).addGenericProperty(new Property("codDominio", this.dominio.getCodDominio()));
				appContext.getServerByOperationId(operationId).addGenericProperty(new Property("emailIndirizzo", this.connettore.getEmailIndirizzo()));
				appContext.getServerByOperationId(operationId).addGenericProperty(new Property("tipoTracciato", this.tipoTracciato.toString()));
				ctx.getApplicationLogger().log("tracciatoNotificaPagamenti.email");
				ctx.getApplicationLogger().log("tracciatoNotificaPagamenti.spedizione");
				this.inviaTracciatoViaEmail(this.tracciato, this.connettore, this.dominio, tracciatiMyPivotBD, configWrapper, beanDati, serializer, ctx);
				break;
			case FILE_SYSTEM:
				url = this.connettore.getFileSystemPath();
				operationId = appContext.setupNotificaPagamentiClient(TRACCIATO_NOTIFICA_FLUSSO_PAGAMENTI, url);
				appContext.getServerByOperationId(operationId).addGenericProperty(new Property("codDominio", this.dominio.getCodDominio()));
				appContext.getServerByOperationId(operationId).addGenericProperty(new Property("fileSystemPath", this.connettore.getFileSystemPath()));
				appContext.getServerByOperationId(operationId).addGenericProperty(new Property("tipoTracciato", this.tipoTracciato.toString()));
				ctx.getApplicationLogger().log("tracciatoNotificaPagamenti.fileSystem");
				ctx.getApplicationLogger().log("tracciatoNotificaPagamenti.spedizione");
				this.salvaTracciatoSuFileSystem(this.tracciato, this.connettore, this.dominio, tracciatiMyPivotBD, configWrapper, beanDati, serializer, ctx);
				break;
			case WEB_SERVICE:
				url = this.connettore.getUrl();
				operationId = appContext.setupNotificaPagamentiClient(TRACCIATO_NOTIFICA_FLUSSO_PAGAMENTI, url);
				appContext.getServerByOperationId(operationId).addGenericProperty(new Property("codDominio", this.dominio.getCodDominio()));
				appContext.getServerByOperationId(operationId).addGenericProperty(new Property("webServiceUrl", url));
				appContext.getServerByOperationId(operationId).addGenericProperty(new Property("tipoTracciato", this.tipoTracciato.toString()));
				ctx.getApplicationLogger().log("tracciatoNotificaPagamenti.webService");
				ctx.getApplicationLogger().log("tracciatoNotificaPagamenti.spedizione");
				ctx.getApplicationLogger().log("tracciatoNotificaPagamenti.webServiceOk");
				break;
			}
			
			eventoContext.setEsito(Esito.OK);
			log.info("Tracciato " + this.tipoTracciato + " inviato con successo");
		} catch(Exception e) {
			errore = true;
			if(e instanceof GovPayException || e instanceof ClientException)
				log.warn("Errore nella Spedizione del tracciato " + this.tipoTracciato + ": " + e.getMessage());
			else
				log.error("Errore nella Spedizione del tracciato " + this.tipoTracciato + "", e);
			
			if(e instanceof GovPayException) {
				eventoContext.setSottotipoEsito(((GovPayException)e).getCodEsito().toString());
			} else if(e instanceof ClientException) {
				eventoContext.setSottotipoEsito(((ClientException)e).getResponseCode() + "");
			} else {
				eventoContext.setSottotipoEsito(EsitoOperazione.INTERNAL.toString());
			}
			
			eventoContext.setEsito(Esito.FAIL);
			eventoContext.setDescrizioneEsito(e.getMessage());
			eventoContext.setException(e);
		} finally {
			EventiBD eventiBD = new EventiBD(configWrapper);
			try {
				eventiBD.insertEvento(eventoContext.toEventoDTO());
			} catch (ServiceException e) {
				log.error("Errore durante il salvataggio dell'evento: ", e);
			}
			
			if(tracciatiMyPivotBD != null) {
				tracciatiMyPivotBD.closeConnection();
			}
			
			this.completed = true;
			ContextThreadLocal.unset();
		}
	}

	public boolean isCompleted() {
		return this.completed;
	}
	
	public boolean isErrore() {
		return this.errore;
	}
	
	private void inviaTracciatoViaEmail(TracciatoNotificaPagamenti tracciato, ConnettoreNotificaPagamenti connettore, Dominio dominio, TracciatiNotificaPagamentiBD tracciatiMyPivotBD,
			BDConfigWrapper configWrapper, it.govpay.core.beans.tracciati.TracciatoNotificaPagamenti beanDati, ISerializer serializer, IContext ctx ) throws ServiceException {
		it.govpay.model.MailServer mailserver = null;
		
		try {
			mailserver = AnagraficaManager.getConfigurazione(configWrapper).getBatchSpedizioneEmail().getMailserver();
		} catch (NotFoundException nfe) {
			throw new ServiceException("Configurazione mailserver mancante");
		}
		
		String errore = null;
		Sender senderCommonsMail = SenderFactory.newSender(SenderType.COMMONS_MAIL, log);
		
		String host = mailserver.getHost();
		int port = mailserver.getPort();
		String username = mailserver.getUsername();
		String password = mailserver.getPassword();
		String from = mailserver.getFrom();
		if(mailserver.getReadTimeout() != null)
			senderCommonsMail.setReadTimeout(mailserver.getReadTimeout());
		if(mailserver.getConnectionTimeout() != null)
			senderCommonsMail.setConnectionTimeout(mailserver.getConnectionTimeout());
		
		org.openspcoop2.utils.mail.Mail mail = new org.openspcoop2.utils.mail.Mail();
		mail.setServerHost(host);
		mail.setServerPort(port);
		if(username != null && !username.isEmpty() && password != null && !password.isEmpty()) {
			mail.setUsername(username);
			mail.setPassword(password);
		}
		mail.setStartTls(false);
		
		if(it.govpay.core.utils.GovpayConfig.getInstance().isMailServerSSLv3()) {
			SSLConfig sslConfig = new SSLConfig();
			sslConfig.setSslType("SSLv3");
			mail.setSslConfig(sslConfig );
		}
		mail.setFrom(from);
		mail.setTo(connettore.getEmailIndirizzo());
//		if(promemoria.getDestinatarioCc() !=null)
//			mail.setCc(Arrays.asList(promemoria.getDestinatarioCc()));
		
		log.debug("Invio Tracciato " + this.tipoTracciato + " [Nome: "+tracciato.getNomeFile() + "], al destinatario ["+connettore.getEmailIndirizzo()	+"] ...");

		this.impostaOggettoEBodyMail(tracciato, dominio, beanDati, mail);
		
		String attachmentName = tracciato.getNomeFile();
		byte[] blobRawContentuto = tracciatiMyPivotBD.leggiBlobRawContentuto(tracciato.getId(), it.govpay.orm.TracciatoNotificaPagamenti.model().RAW_CONTENUTO);
		
		MailAttach avvisoAttach = new MailBinaryAttach(attachmentName, blobRawContentuto);
		mail.getBody().getAttachments().add(avvisoAttach );
		
		try {
			
			tracciato.setDataCaricamento(new Date());
			log.debug("Spediazione Tracciato " + this.tipoTracciato + " verso il mail server ["+host+"]:["+port+"]...");
			senderCommonsMail.send(mail, true);
			log.debug("Spediazione Tracciato " + this.tipoTracciato + " verso il mail server ["+host+"]:["+port+"] completata.");
			tracciato.setStato(STATO_ELABORAZIONE.FILE_CARICATO);
			beanDati.setStepElaborazione(STATO_ELABORAZIONE.FILE_CARICATO.name());
			beanDati.setDescrizioneStepElaborazione(null);
			tracciato.setDataCompletamento(new Date());
			try {
				ctx.getApplicationLogger().log("tracciatoNotificaPagamenti.emailOk");
			} catch (UtilsException e1) {
				log.error(e1.getMessage(), e1);
			}
		}catch (UtilsException e) {
			errore = "Errore durante l'invio del Tracciato " + this.tipoTracciato + " [Nome: "+tracciato.getNomeFile() 
				+ "], al destinatario ["+connettore.getEmailIndirizzo()	+"]:"+e.getMessage();
			log.error(errore, e);

			if(ExceptionUtils.existsInnerException(e, javax.mail.internet.AddressException.class)) {
				log.debug("La spedizione del Tracciato " + this.tipoTracciato + " si e' conclusa con errore che non prevede la rispedizione...");
				tracciato.setStato(STATO_ELABORAZIONE.ERROR_LOAD);
				tracciato.setDataCompletamento(new Date());
				beanDati.setStepElaborazione(STATO_ELABORAZIONE.ERROR_LOAD.name());
				beanDati.setDescrizioneStepElaborazione(errore);
				log.debug("Salvataggio Tracciato " + this.tipoTracciato + " in stato 'ERROR_LOAD'");
				try {
					ctx.getApplicationLogger().log("tracciatoNotificaPagamenti.emailKo", e.getMessage());
				} catch (UtilsException e1) {
					log.error(e1.getMessage(), e1);
				}
			} else {
				try {
					ctx.getApplicationLogger().log("tracciatoNotificaPagamenti.emailRetryKo", e.getMessage());
				} catch (UtilsException e1) {
					log.error(e1.getMessage(), e1);
				}
				log.debug("La spedizione del Tracciato " + this.tipoTracciato + " si e' conclusa con errore, verra' effettuato un nuovo tentativo durante la prossima esecuzione del Batch di spedizione...");
			}
		} finally {
			tracciatiMyPivotBD.setupConnection(configWrapper.getTransactionID());
			try {
				tracciato.setBeanDati(serializer.getObject(beanDati));
			} catch (IOException e1) {}
			
			tracciatiMyPivotBD.updateFineElaborazione(tracciato);
			
		} 
	}

	private void impostaOggettoEBodyMail(TracciatoNotificaPagamenti tracciato, Dominio dominio,
			it.govpay.core.beans.tracciati.TracciatoNotificaPagamenti beanDati,
			org.openspcoop2.utils.mail.Mail mail) {
		
		String tipoTracciatoString = null;
		String dataInizio = SimpleDateFormatUtils.newSimpleDateFormatSoloData().format(tracciato.getDataRtDa());
		String dataFine = SimpleDateFormatUtils.newSimpleDateFormatSoloData().format(tracciato.getDataRtA());
		switch (this.tipoTracciato) {
		case MYPIVOT:
			tipoTracciatoString = "MyPivot"; 
			break;
		case SECIM:
			tipoTracciatoString = "Secim"; 
			break;
		}
		mail.setSubject("[Govpay] Export pagamenti "+tipoTracciatoString+" al " + dataFine + ".");
		
		StringBuilder  sb = new StringBuilder();
		
		sb.append("Salve,");
		sb.append("\n");
		sb.append("\nin allegato alla presente il tracciato dei pagamenti per l'importazione in ").append(tipoTracciatoString).append(":");
		sb.append("\n");
		sb.append("\nEnte creditore: ").append(dominio.getRagioneSociale());
		sb.append("\nId Dominio: ").append(dominio.getCodDominio());
		sb.append("\nData inizio: ").append(dataInizio);
		sb.append("\nData fine: ").append(dataFine);
		sb.append("\nNumero pagamenti: ").append(beanDati.getNumRtTotali());
		sb.append("\nVersione tracciato: ").append(tracciato.getVersione());
		sb.append("\n");
		sb.append("\nLa seguente comunicazione proviene da un sistema automatico.");
		sb.append("\n");
		sb.append("\nCordiali saluti.");
		
		mail.getBody().setMessage(sb.toString());
	}
	
	private void salvaTracciatoSuFileSystem(TracciatoNotificaPagamenti tracciato, ConnettoreNotificaPagamenti connettore, Dominio dominio2, TracciatiNotificaPagamentiBD tracciatiMyPivotBD,
			BDConfigWrapper configWrapper, it.govpay.core.beans.tracciati.TracciatoNotificaPagamenti beanDati, ISerializer serializer, IContext ctx ) throws ServiceException {
		
		log.debug("Salvataggio Tracciato " + this.tipoTracciato + " [Nome: "+tracciato.getNomeFile() + "], su FileSystem ["+connettore.getFileSystemPath()	+"] ...");
		String errore = null;
		boolean retry = true;
		try {
			
			File directorySalvataggio = new File(connettore.getFileSystemPath());
			
			if(directorySalvataggio.exists()) {
				if(directorySalvataggio.canWrite()) {
					try (FileOutputStream fos = new FileOutputStream(directorySalvataggio.getAbsolutePath() + File.separator + tracciato.getNomeFile());){
						tracciato.setDataCaricamento(new Date());
						byte[] blobRawContentuto = tracciatiMyPivotBD.leggiBlobRawContentuto(tracciato.getId(), it.govpay.orm.TracciatoNotificaPagamenti.model().RAW_CONTENUTO);
						fos.write(blobRawContentuto); 
						fos.flush();
						fos.close();
						log.debug("Salvataggio Tracciato " + this.tipoTracciato + " [Nome: "+tracciato.getNomeFile() + "], su FileSystem ["+connettore.getFileSystemPath()	+"] completato.");
						tracciato.setStato(STATO_ELABORAZIONE.FILE_CARICATO);
						beanDati.setStepElaborazione(STATO_ELABORAZIONE.FILE_CARICATO.name());
						beanDati.setDescrizioneStepElaborazione(null);
						tracciato.setDataCompletamento(new Date());
						try {
							ctx.getApplicationLogger().log("tracciatoNotificaPagamenti.fileSystemOk");
						} catch (UtilsException e1) {
							log.error(e1.getMessage(), e1);
						}
						
					} catch(java.io.IOException e) {
						errore = "Errore durante il salvataggio del Tracciato " + this.tipoTracciato + " [Nome: "+tracciato.getNomeFile() + "], su FileSystem ["+connettore.getFileSystemPath()	+"]:"+e.getMessage();
						log.error(errore, e);
					} finally {
						
					}
				} else {
					errore = "Errore durante il salvataggio del Tracciato " + this.tipoTracciato + " [Nome: "+tracciato.getNomeFile() + "], su FileSystem ["+connettore.getFileSystemPath()	+"]: accesso in scrittura alla directory non consentito.";
					log.debug("Salvataggio Tracciato " + this.tipoTracciato + " [Nome: "+tracciato.getNomeFile() + "], su FileSystem ["+connettore.getFileSystemPath()	+"] accesso in scrittura alla directory non consentito.");
					retry = false;
				}
			} else {
				errore = "Errore durante il salvataggio del Tracciato " + this.tipoTracciato + " [Nome: "+tracciato.getNomeFile() + "], su FileSystem ["+connettore.getFileSystemPath()	+"]: directory non presente.";
				log.debug("Salvataggio Tracciato " + this.tipoTracciato + " [Nome: "+tracciato.getNomeFile() + "], su FileSystem ["+connettore.getFileSystemPath()	+"] directory non presente.");
				retry = false;
			}
			
			if(errore != null) {
				if(!retry) {
					log.debug("Salvataggio Tracciato " + this.tipoTracciato + " [Nome: "+tracciato.getNomeFile() + "], su FileSystem ["+connettore.getFileSystemPath()	+"] si e' concluso con errore che non prevede la rispedizione...");
					tracciato.setStato(STATO_ELABORAZIONE.ERROR_LOAD);
					tracciato.setDataCompletamento(new Date());
					beanDati.setStepElaborazione(STATO_ELABORAZIONE.ERROR_LOAD.name());
					beanDati.setDescrizioneStepElaborazione(errore);
					log.debug("Salvataggio Tracciato " + this.tipoTracciato + " in stato 'ERROR_LOAD'");
					try {
						ctx.getApplicationLogger().log("tracciatoNotificaPagamenti.fileSystemKo", errore);
					} catch (UtilsException e1) {
						log.error(e1.getMessage(), e1);
					}
				} else {
					try {
						ctx.getApplicationLogger().log("tracciatoNotificaPagamenti.fileSystemRetryKo", errore);
					} catch (UtilsException e1) {
						log.error(e1.getMessage(), e1);
					}
					log.debug("Salvataggio Tracciato " + this.tipoTracciato + " [Nome: "+tracciato.getNomeFile() + "], su FileSystem ["+connettore.getFileSystemPath()	+"] si e' concluso con errore, verra' effettuato un nuovo tentativo durante la prossima esecuzione del Batch di spedizione...");
				}
			}
		} finally {
			tracciatiMyPivotBD.setupConnection(configWrapper.getTransactionID());
			try {
				tracciato.setBeanDati(serializer.getObject(beanDati));
			} catch (IOException e1) {}
			
			tracciatiMyPivotBD.updateFineElaborazione(tracciato);
			
		} 
	}
}
