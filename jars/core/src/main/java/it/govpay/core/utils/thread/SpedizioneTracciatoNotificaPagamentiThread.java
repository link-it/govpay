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
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
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
import org.openspcoop2.utils.service.context.dump.DumpRequest;
import org.openspcoop2.utils.service.context.dump.DumpResponse;
import org.openspcoop2.utils.transport.http.SSLConfig;
import org.slf4j.Logger;
import org.slf4j.MDC;

import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.bd.configurazione.model.GdeInterfaccia;
import it.govpay.bd.configurazione.model.Giornale;
import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.TracciatoNotificaPagamenti;
import it.govpay.bd.model.eventi.DettaglioRichiesta;
import it.govpay.bd.model.eventi.DettaglioRisposta;
import it.govpay.bd.pagamento.EventiBD;
import it.govpay.bd.pagamento.TracciatiNotificaPagamentiBD;
import it.govpay.core.beans.EsitoOperazione;
import it.govpay.core.business.GiornaleEventi;
import it.govpay.core.exceptions.GovPayException;
import it.govpay.core.utils.EventoContext;
import it.govpay.core.utils.EventoContext.Categoria;
import it.govpay.core.utils.EventoContext.Componente;
import it.govpay.core.utils.EventoContext.Esito;
import it.govpay.core.utils.ExceptionUtils;
import it.govpay.core.utils.GovpayConfig;
import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.SimpleDateFormatUtils;
import it.govpay.core.utils.client.BasicClient.ClientException;
import it.govpay.core.utils.rawutils.ConverterUtils;
import it.govpay.model.ConnettoreNotificaPagamenti;
import it.govpay.model.ConnettoreNotificaPagamenti.TipoConnettore;
import it.govpay.model.TracciatoNotificaPagamenti.STATO_ELABORAZIONE;
import it.govpay.model.TracciatoNotificaPagamenti.TIPO_TRACCIATO;

public class SpedizioneTracciatoNotificaPagamentiThread implements Runnable {

	public enum Operazione {
		
		secimInviaTracciatoEmail, pivotInviaTracciatoEmail,
		secimInviaTracciatoFileSystem, pivotInviaTracciatoFileSystem
	} 
	
	public static final String CONNETTORE_NOTIFICA_DISABILITATO = "Connettore Notifica non configurato";
	public static final String TRACCIATO_NOTIFICA_FLUSSO_PAGAMENTI = "TracciatoNotificaPagamenti";
	private static Logger log = LoggerWrapperFactory.getLogger(SpedizioneTracciatoNotificaPagamentiThread.class);
	private TracciatoNotificaPagamenti tracciato;
	private Dominio dominio = null;
	private boolean completed = false;
	private boolean errore = false;
	private ConnettoreNotificaPagamenti connettore = null;
	private IContext ctx = null;
	private TIPO_TRACCIATO tipoTracciato = null;
	private Componente componente;
	private Giornale giornale;
	private EventoContext eventoCtx;

	public SpedizioneTracciatoNotificaPagamentiThread(TracciatoNotificaPagamenti tracciato, ConnettoreNotificaPagamenti connettore, IContext ctx) throws ServiceException {
		// Verifico che tutti i campi siano valorizzati
		this.ctx = ctx;
		BDConfigWrapper configWrapper = new BDConfigWrapper(this.ctx.getTransactionId(), true);
		this.tracciato = tracciato;
		this.tipoTracciato = this.tracciato.getTipo();
		this.dominio = this.tracciato.getDominio(configWrapper);
		this.connettore = connettore;
		
		try {
			this.giornale = AnagraficaManager.getConfigurazione(configWrapper).getGiornale();
		} catch (NotFoundException e) {
			throw new ServiceException("Configurazione giornale eventi mancante");
		}
		
		this.eventoCtx = new EventoContext();
		this.eventoCtx.setCategoriaEvento(Categoria.INTERFACCIA);
		this.eventoCtx.setRole(Role.CLIENT);
		this.eventoCtx.setDataRichiesta(new Date());
		this.eventoCtx.setCodDominio(dominio.getCodDominio());
		
		switch (this.tipoTracciato) {
		case MYPIVOT:
			this.componente = Componente.API_MYPIVOT;
			break;
		case SECIM:
			this.componente = Componente.API_SECIM;
			break;
		}
		this.eventoCtx.setComponente(this.componente);
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
		String url = null;
		DumpRequest dumpRequest = new DumpRequest();
		DumpResponse dumpResponse = new DumpResponse();
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
			
			
			String operationId = null;
			switch (this.connettore.getTipoConnettore()) {
			case EMAIL:
				url = StringUtils.join(this.connettore.getEmailIndirizzi(), ",");
				operationId = appContext.setupNotificaPagamentiClient(TRACCIATO_NOTIFICA_FLUSSO_PAGAMENTI, url);
				appContext.getServerByOperationId(operationId).addGenericProperty(new Property("codDominio", this.dominio.getCodDominio()));
				appContext.getServerByOperationId(operationId).addGenericProperty(new Property("emailIndirizzo", url));
				appContext.getServerByOperationId(operationId).addGenericProperty(new Property("tipoTracciato", this.tipoTracciato.toString()));
				ctx.getApplicationLogger().log("tracciatoNotificaPagamenti.email");
				ctx.getApplicationLogger().log("tracciatoNotificaPagamenti.spedizione");
				this.inviaTracciatoViaEmail(this.tracciato, this.connettore, this.dominio, tracciatiMyPivotBD, configWrapper, beanDati, serializer, ctx, dumpRequest, dumpResponse);
				break;
			case FILE_SYSTEM:
				url = this.connettore.getFileSystemPath();
				operationId = appContext.setupNotificaPagamentiClient(TRACCIATO_NOTIFICA_FLUSSO_PAGAMENTI, url);
				appContext.getServerByOperationId(operationId).addGenericProperty(new Property("codDominio", this.dominio.getCodDominio()));
				appContext.getServerByOperationId(operationId).addGenericProperty(new Property("fileSystemPath", this.connettore.getFileSystemPath()));
				appContext.getServerByOperationId(operationId).addGenericProperty(new Property("tipoTracciato", this.tipoTracciato.toString()));
				ctx.getApplicationLogger().log("tracciatoNotificaPagamenti.fileSystem");
				ctx.getApplicationLogger().log("tracciatoNotificaPagamenti.spedizione");
				this.salvaTracciatoSuFileSystem(this.tracciato, this.connettore, this.dominio, tracciatiMyPivotBD, configWrapper, beanDati, serializer, ctx, dumpRequest, dumpResponse);
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
			
			this.eventoCtx.setEsito(Esito.OK);
			log.info("Tracciato " + this.tipoTracciato + " [Nome: "+tracciato.getNomeFile() + "] inviato con successo");
		} catch(Exception e) {
			errore = true;
			if(e instanceof GovPayException || e instanceof ClientException)
				log.warn("Errore nella Spedizione del tracciato " + this.tipoTracciato + ": " + e.getMessage());
			else
				log.error("Errore nella Spedizione del tracciato " + this.tipoTracciato + "", e);
			
			if(e instanceof GovPayException) {
				this.eventoCtx.setSottotipoEsito(((GovPayException)e).getCodEsito().toString());
			} else if(e instanceof ClientException) {
				this.eventoCtx.setSottotipoEsito(((ClientException)e).getResponseCode() + "");
			} else {
				this.eventoCtx.setSottotipoEsito(EsitoOperazione.INTERNAL.toString());
			}
			
			this.eventoCtx.setEsito(Esito.FAIL);
			this.eventoCtx.setDescrizioneEsito(e.getMessage());
			this.eventoCtx.setException(e);
			
		} finally {
			this.popolaContextEvento(connettore.getTipoConnettore(), url, dumpRequest, dumpResponse, this.eventoCtx);
			
			
			EventiBD eventiBD = new EventiBD(configWrapper);
			try {
				eventiBD.insertEvento(this.eventoCtx.toEventoDTO());
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
			BDConfigWrapper configWrapper, it.govpay.core.beans.tracciati.TracciatoNotificaPagamenti beanDati, ISerializer serializer, IContext ctx, DumpRequest dumpRequest, DumpResponse dumpResponse  ) throws ServiceException {
		it.govpay.model.MailServer mailserver = null;
		
		try {
			mailserver = AnagraficaManager.getConfigurazione(configWrapper).getBatchSpedizioneEmail().getMailserver();
		} catch (NotFoundException nfe) {
			throw new ServiceException("Configurazione mailserver mancante");
		}
		
		switch (this.tipoTracciato) {
		case MYPIVOT:
			this.eventoCtx.setTipoEvento(Operazione.pivotInviaTracciatoEmail.name());
			break;
		case SECIM:
			this.eventoCtx.setTipoEvento(Operazione.secimInviaTracciatoEmail.name());
			break;
		}
		
		dumpRequest.setContentType("text/plain");
		
		
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
			log.debug("Autenticazione con username e password al mailserver");
			mail.setUsername(username);
			mail.setPassword(password);
		}
		mail.setStartTls(false);
		
		if(it.govpay.core.utils.GovpayConfig.getInstance().isMailServerSSLv3()) {
			log.debug("Abilitazione configurazione SSLv3 per comunicazione al mailserver");
			SSLConfig sslConfig = new SSLConfig();
			sslConfig.setSslType("SSLv3");
			mail.setSslConfig(sslConfig );
		}
		
		mail.setFrom(from);
		List<String> indirizzi = connettore.getEmailIndirizzi();
		
		mail.setTo(indirizzi.get(0));
		if(indirizzi.size() > 1) {
			mail.setCc(indirizzi.subList(1, indirizzi.size()));
		}
//		if(promemoria.getDestinatarioCc() !=null)
//			mail.setCc(Arrays.asList(promemoria.getDestinatarioCc()));
		
		log.debug("Invio Tracciato " + this.tipoTracciato + " [Nome: "+tracciato.getNomeFile() + "], al destinatario ["+StringUtils.join(this.connettore.getEmailIndirizzi(), ",")	+"] ...");

		this.impostaOggettoEBodyMail(tracciato, dominio, connettore, beanDati, mail);
		
		String attachmentName = tracciato.getNomeFile();
		byte[] blobRawContentuto = tracciatiMyPivotBD.leggiBlobRawContentuto(tracciato.getId(), it.govpay.orm.TracciatoNotificaPagamenti.model().RAW_CONTENUTO);
		
		//dumpRequest.setPayload(blobRawContentuto);
		dumpRequest.getHeaders().put("Destinatari", StringUtils.join(this.connettore.getEmailIndirizzi(), ","));
		dumpRequest.getHeaders().put("Ente creditore", dominio.getRagioneSociale());
		dumpRequest.getHeaders().put("Id Dominio", dominio.getCodDominio());
		String dataInizio = SimpleDateFormatUtils.newSimpleDateFormatSoloData().format(tracciato.getDataRtDa());
		dumpRequest.getHeaders().put("Data inizio", dataInizio);
		String dataFine = SimpleDateFormatUtils.newSimpleDateFormatSoloData().format(tracciato.getDataRtA());
		dumpRequest.getHeaders().put("Data fine", dataFine);
		dumpRequest.getHeaders().put("Numero pagamenti", beanDati.getNumRtTotali()+"");
		dumpRequest.getHeaders().put("Versione tracciato", tracciato.getVersione());
		
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
				+ "], al destinatario ["+ StringUtils.join(this.connettore.getEmailIndirizzi(), ",")	+"]:"+e.getMessage();
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
			
			dumpResponse.setPayload(errore.getBytes());
		} finally {
			tracciatiMyPivotBD.setupConnection(configWrapper.getTransactionID());
			try {
				tracciato.setBeanDati(serializer.getObject(beanDati));
			} catch (IOException e1) {}
			
			tracciatiMyPivotBD.updateFineElaborazione(tracciato);
			
		} 
	}

	private void impostaOggettoEBodyMail(TracciatoNotificaPagamenti tracciato, Dominio dominio, ConnettoreNotificaPagamenti connettore,
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
		
		if(connettore.getEmailSubject() != null && !connettore.getEmailSubject().isEmpty()) {
			mail.setSubject(connettore.getEmailSubject());
		} else {
			mail.setSubject("[Govpay] Export pagamenti "+tipoTracciatoString+" al " + dataFine + ".");
		}
		
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
			BDConfigWrapper configWrapper, it.govpay.core.beans.tracciati.TracciatoNotificaPagamenti beanDati, ISerializer serializer, IContext ctx, DumpRequest dumpRequest, DumpResponse dumpResponse) throws ServiceException {
		
		log.debug("Salvataggio Tracciato " + this.tipoTracciato + " [Nome: "+tracciato.getNomeFile() + "], su FileSystem ["+connettore.getFileSystemPath()	+"] ...");
		String errore = null;
		boolean retry = true;
		try {
			switch (this.tipoTracciato) {
			case MYPIVOT:
				this.eventoCtx.setTipoEvento(Operazione.pivotInviaTracciatoFileSystem.name());
				break;
			case SECIM:
				this.eventoCtx.setTipoEvento(Operazione.secimInviaTracciatoFileSystem.name());
				break;
			}
			
			dumpRequest.setContentType("application/zip");
			
			File directorySalvataggio = new File(connettore.getFileSystemPath());
			
			if(directorySalvataggio.exists()) {
				if(directorySalvataggio.canWrite()) {
					String fileName = directorySalvataggio.getAbsolutePath() + File.separator + tracciato.getNomeFile();
					try (FileOutputStream fos = new FileOutputStream(fileName);){
						tracciato.setDataCaricamento(new Date());
						byte[] blobRawContentuto = tracciatiMyPivotBD.leggiBlobRawContentuto(tracciato.getId(), it.govpay.orm.TracciatoNotificaPagamenti.model().RAW_CONTENUTO);
						fos.write(blobRawContentuto); 
						fos.flush();
						fos.close();
						
						// dumpRequest.setPayload(blobRawContentuto);
						dumpRequest.getHeaders().put("FilePath", fileName);
						dumpRequest.getHeaders().put("Ente creditore", dominio.getRagioneSociale());
						dumpRequest.getHeaders().put("Id Dominio", dominio.getCodDominio());
						String dataInizio = SimpleDateFormatUtils.newSimpleDateFormatSoloData().format(tracciato.getDataRtDa());
						dumpRequest.getHeaders().put("Data inizio", dataInizio);
						String dataFine = SimpleDateFormatUtils.newSimpleDateFormatSoloData().format(tracciato.getDataRtA());
						dumpRequest.getHeaders().put("Data fine", dataFine);
						dumpRequest.getHeaders().put("Numero pagamenti", beanDati.getNumRtTotali()+"");
						dumpRequest.getHeaders().put("Versione tracciato", tracciato.getVersione());
						
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
				dumpResponse.setPayload(errore.getBytes());
			}
		} finally {
			tracciatiMyPivotBD.setupConnection(configWrapper.getTransactionID());
			try {
				tracciato.setBeanDati(serializer.getObject(beanDati));
			} catch (IOException e1) {}
			
			tracciatiMyPivotBD.updateFineElaborazione(tracciato);
			
		} 
	}
	
	
	protected void popolaContextEvento(TipoConnettore tipoConnettore, String url, DumpRequest dumpRequest, DumpResponse dumpResponse, EventoContext eventoContext) {
		if(GovpayConfig.getInstance().isGiornaleEventiEnabled()) {
			boolean logEvento = false;
			boolean dumpEvento = false;
			GdeInterfaccia configurazioneInterfaccia = GiornaleEventi.getConfigurazioneComponente(this.componente, this.giornale);
			
			log.debug("Log Evento Client: ["+this.componente +"] Tipo Connettore ["+tipoConnettore
						+"], Destinatario ["+ url +"], Esito ["+eventoContext.getEsito()+"]");

			if(configurazioneInterfaccia != null) {
				try {
					log.debug("Configurazione Giornale Eventi API: ["+this.componente+"]: " + ConverterUtils.toJSON(configurazioneInterfaccia,null));
				} catch (ServiceException e) {
					log.error("Errore durante il log della configurazione giornale eventi: " +e.getMessage(), e);
				}
				
				if(GiornaleEventi.isRequestLettura(null, this.componente, eventoContext.getTipoEvento())) {
					logEvento = GiornaleEventi.logEvento(configurazioneInterfaccia.getLetture(), eventoContext.getEsito());
					dumpEvento = GiornaleEventi.dumpEvento(configurazioneInterfaccia.getLetture(), eventoContext.getEsito());
					log.debug("Tipo Operazione 'Lettura', Log ["+logEvento+"], Dump ["+dumpEvento+"].");
				} else if(GiornaleEventi.isRequestScrittura(null, this.componente, eventoContext.getTipoEvento())) {
					logEvento = GiornaleEventi.logEvento(configurazioneInterfaccia.getScritture(), eventoContext.getEsito());
					dumpEvento = GiornaleEventi.dumpEvento(configurazioneInterfaccia.getScritture(), eventoContext.getEsito());
					log.debug("Tipo Operazione 'Scrittura', Log ["+logEvento+"], Dump ["+dumpEvento+"].");
				} else {
					log.debug("Tipo Operazione non riconosciuta, l'evento non verra' salvato.");
				}
				
				eventoContext.setRegistraEvento(logEvento);

				if(logEvento) {
					Date dataIngresso = eventoContext.getDataRichiesta();
					Date dataUscita = new Date();
					// lettura informazioni dalla richiesta
					DettaglioRichiesta dettaglioRichiesta = new DettaglioRichiesta();

					dettaglioRichiesta.setPrincipal(eventoContext.getPrincipal());
					dettaglioRichiesta.setUtente(eventoContext.getUtente());
					dettaglioRichiesta.setUrl(eventoContext.getUrl());
					dettaglioRichiesta.setMethod(null);
					dettaglioRichiesta.setDataOraRichiesta(dataIngresso);
					dettaglioRichiesta.setHeadersFromMap(dumpRequest.getHeaders());


					// lettura informazioni dalla response
					DettaglioRisposta dettaglioRisposta = new DettaglioRisposta();
					dettaglioRisposta.setHeadersFromMap(dumpResponse.getHeaders());
					dettaglioRisposta.setStatus(0);
					dettaglioRisposta.setDataOraRisposta(dataUscita);

					eventoContext.setDataRisposta(dataUscita);
					eventoContext.setStatus(0);
					eventoContext.setSottotipoEsito(eventoContext.getEsito() + "");

					if(dumpEvento) {
						Base64 base = new Base64();
						// dump richiesta
						if(dumpRequest.getPayload() != null && dumpRequest.getPayload().length > 0)
							dettaglioRichiesta.setPayload(base.encodeToString(dumpRequest.getPayload()));

						// dump risposta
						if(dumpResponse.getPayload() != null && dumpResponse.getPayload().length > 0)
							dettaglioRisposta.setPayload(base.encodeToString(dumpResponse.getPayload()));
					} 

					eventoContext.setDettaglioRichiesta(dettaglioRichiesta);
					eventoContext.setDettaglioRisposta(dettaglioRisposta);
				}
			} else {
				log.warn("La configurazione per l'API ["+this.componente+"] non e' corretta, salvataggio evento non eseguito."); 
			}
		}
	}
}
