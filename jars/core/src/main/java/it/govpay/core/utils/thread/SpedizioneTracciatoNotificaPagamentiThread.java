/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2025 Link.it srl (http://www.link.it).
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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.LoggerWrapperFactory;
import org.openspcoop2.utils.UtilsException;
import org.openspcoop2.utils.logger.beans.Property;
import org.openspcoop2.utils.mail.MailAttach;
import org.openspcoop2.utils.mail.MailBinaryAttach;
import org.openspcoop2.utils.mail.Sender;
import org.openspcoop2.utils.mail.SenderFactory;
import org.openspcoop2.utils.mail.SenderType;
import org.openspcoop2.utils.serialization.IDeserializer;
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
import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.TracciatoNotificaPagamenti;
import it.govpay.bd.pagamento.EventiBD;
import it.govpay.bd.pagamento.TracciatiNotificaPagamentiBD;
import it.govpay.core.beans.EsitoOperazione;
import it.govpay.core.beans.EventoContext;
import it.govpay.core.beans.EventoContext.Categoria;
import it.govpay.core.beans.EventoContext.Componente;
import it.govpay.core.beans.EventoContext.Esito;
import it.govpay.core.business.TracciatiNotificaPagamenti;
import it.govpay.core.exceptions.GovPayException;
import it.govpay.core.exceptions.IOException;
import it.govpay.core.utils.EventoUtils;
import it.govpay.core.utils.ExceptionUtils;
import it.govpay.core.utils.GovpayConfig;
import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.LogUtils;
import it.govpay.core.utils.SimpleDateFormatUtils;
import it.govpay.core.utils.client.EnteRendicontazioniClient;
import it.govpay.core.utils.client.exception.ClientException;
import it.govpay.core.utils.client.exception.ClientInitializeException;
import it.govpay.core.utils.eventi.EventiUtils;
import it.govpay.core.utils.rawutils.ConverterUtils;
import it.govpay.core.utils.tracciati.TracciatiNotificaPagamentiUtils;
import it.govpay.ec.rendicontazioni.v1.beans.Rpp;
import it.govpay.model.ConnettoreNotificaPagamenti;
import it.govpay.model.ConnettoreNotificaPagamenti.TipoConnettore;
import it.govpay.model.Evento.RuoloEvento;
import it.govpay.model.TracciatoNotificaPagamenti.STATO_ELABORAZIONE;
import it.govpay.model.TracciatoNotificaPagamenti.TIPO_TRACCIATO;
import it.govpay.model.configurazione.GdeInterfaccia;
import it.govpay.model.configurazione.Giornale;
import it.govpay.model.configurazione.SslConfig;
import it.govpay.model.eventi.DettaglioRichiesta;
import it.govpay.model.eventi.DettaglioRisposta;

public class SpedizioneTracciatoNotificaPagamentiThread implements Runnable {

	private static final String PROPERTY_NAME_FILE_CONTENUTO = "fileContenuto";
	private static final String MSG_ERRORE_ERRORE_DURANTE_IL_SALVATAGGIO_DELL_EVENTO = "Errore durante il salvataggio dell'evento: ";
	private static final String PROPERTY_NAME_CONTENUTO = "contenuto";
	private static final String DEBUG_MSG_SALVATAGGIO_TRACCIATO_0_IN_STATO_ERROR_LOAD = "Salvataggio Tracciato {0} in stato ''ERROR_LOAD''";
	private static final String ERROR_MSG_ERRORE_DURANTE_LA_SPEDIZIONE_RPP_0_DEL_TRACCIATO_1_NOME_2_AL_DESTINATARIO_3_4 = "Errore durante la spedizione RPP {0} del Tracciato {1} [Nome: {2}], al destinatario [{3}]:{4}";
	private static final String MSG_DIAGNOSTICO_TRACCIATO_NOTIFICA_PAGAMENTI_REST_KO_KEY = "tracciatoNotificaPagamenti.restKo";
	private static final String MSG_DIAGNOSTICO_TRACCIATO_NOTIFICA_PAGAMENTI_REST_OK_KEY = "tracciatoNotificaPagamenti.restOk";
	private static final String MSG_DIAGNOSTICO_TRACCIATO_NOTIFICA_PAGAMENTI_REST_CONTENUTO_KO_KEY = "tracciatoNotificaPagamenti.restContenutoKo";
	private static final String MSG_DIAGNOSTICO_TRACCIATO_NOTIFICA_PAGAMENTI_REST_CONTENUTO_OK_KEY = "tracciatoNotificaPagamenti.restContenutoOk";
	private static final String SPEDIZIONE_ENTRY_0_COMPLETATA = "Spedizione Entry: {0} completata.";
	private static final String SPEDIZIONE_ENTRY_0_IN_CORSO = "Spedizione Entry: {0} in corso...";
	private static final String ELABORAZIONE_ENTRY_0 = "Elaborazione Entry: {0}";
	private static final String OGGETTO_DEFAULT_MAIL_GOVPAY_EXPORT_PAGAMENTI_TIPO_AL_DATA = "[GovPay] Export pagamenti {0} al {1}.";


	public enum Operazione {
		
		SECIMINVIATRACCIATOEMAIL("secimInviaTracciatoEmail"), 
		PIVOTINVIATRACCIATOEMAIL ("pivotInviaTracciatoEmail"),
		SECIMINVIATRACCIATOFILESYSTEM("secimInviaTracciatoFileSystem"), 
		PIVOTINVIATRACCIATOFILESYSTEM("pivotInviaTracciatoFileSystem"),
		GOVPAYINVIATRACCIATOFILESYSTEM("govpayInviaTracciatoFileSystem"), 
		GOVPAYINVIATRACCIATOEMAIL("govpayInviaTracciatoEmail"),
		GOVPAYINVIATRACCIATOREST("govpayInviaTracciatoRest"),
		HYPERSICAPKAPPAINVIATRACCIATOEMAIL("hyperSicAPKappaInviaTracciatoEmail"), 
		HYPERSICAPKAPPAINVIATRACCIATOFILESYSTEM("hyperSicAPKappaInviaTracciatoFileSystem"),
		MAGGIOLIJPPAINVIATRACCIATOEMAIL("maggioliJppaInviaTracciatoEmail");
		
		private String value;

		Operazione(String value) {
			this.value = value;
		}

		@Override
		public String toString() {
			return String.valueOf(value);
		}

		public static Operazione fromValue(String text) {
			for (Operazione b : Operazione.values()) {
				if (String.valueOf(b.value).equals(text)) {
					return b;
				}
			}
			return null;
		}
		
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

	public SpedizioneTracciatoNotificaPagamentiThread(TracciatoNotificaPagamenti tracciato, ConnettoreNotificaPagamenti connettore, IContext ctx) throws ServiceException, IOException {
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
		this.eventoCtx.setRole(RuoloEvento.CLIENT);
		this.eventoCtx.setDataRichiesta(new Date());
		this.eventoCtx.setCodDominio(dominio.getCodDominio());
		
		switch (this.tipoTracciato) {
		case MYPIVOT:
			this.componente = Componente.API_MYPIVOT;
			break;
		case SECIM:
			this.componente = Componente.API_SECIM;
			break;
		case GOVPAY:
			this.componente = Componente.API_GOVPAY;
			break;
		case HYPERSIC_APK:
			this.componente = Componente.API_HYPERSIC_APK;
			break;
		case MAGGIOLI_JPPA:
			this.componente = Componente.API_MAGGIOLI_JPPA;
			break;
		}
		this.eventoCtx.setComponente(this.componente);
		
		this.eventoCtx.setTransactionId(ctx.getTransactionId());
		
		String clusterId = GovpayConfig.getInstance().getClusterId();
		if(clusterId != null)
			this.eventoCtx.setClusterId(clusterId);
		else 
			this.eventoCtx.setClusterId(GovpayConfig.getInstance().getAppName());
	}

	@Override
	public void run() {
		ContextThreadLocal.set(this.ctx);
		
		IContext ctx = ContextThreadLocal.get();
		GpContext appContext = (GpContext) ctx.getApplicationContext();
		MDC.put(MD5Constants.TRANSACTION_ID, ctx.getTransactionId());
		BDConfigWrapper configWrapper = new BDConfigWrapper(ctx.getTransactionId(), true);
		TracciatiNotificaPagamentiBD tracciatiMyPivotBD = null;
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
			
			LogUtils.logInfo(log, MessageFormat.format("Spedizione del tracciato {0} {1}] al connettore previsto dalla configurazione...", this.tipoTracciato, this.tracciato.getNomeFile()));
			
			if(!connettore.isAbilitato()) {
				ctx.getApplicationLogger().log("tracciatoNotificaPagamenti.annullato");
				LogUtils.logInfo(log, MessageFormat.format("Connettore {0} non configurato per il Dominio [Id: {1}]. Spedizione inibita.", this.tipoTracciato, this.dominio.getCodDominio()));				
				tracciatiMyPivotBD.setupConnection(configWrapper.getTransactionID());
				
				this.tracciato.setStato(STATO_ELABORAZIONE.ERROR_LOAD);
				beanDati.setStepElaborazione(STATO_ELABORAZIONE.ERROR_LOAD.name());
				beanDati.setDescrizioneStepElaborazione(MessageFormat.format("Connettore {0} non configurato per il Dominio [Id: {1}]. Spedizione inibita.", this.tipoTracciato, this.dominio.getCodDominio()));
				try {
					this.tracciato.setBeanDati(serializer.getObject(beanDati));
				} catch (org.openspcoop2.utils.serialization.IOException e1) {}
				
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
			case REST:
				url = this.connettore.getUrl();
				operationId = appContext.setupNotificaPagamentiClient(TRACCIATO_NOTIFICA_FLUSSO_PAGAMENTI, url);
				appContext.getServerByOperationId(operationId).addGenericProperty(new Property("codDominio", this.dominio.getCodDominio()));
				appContext.getServerByOperationId(operationId).addGenericProperty(new Property("restUrl", url));
				appContext.getServerByOperationId(operationId).addGenericProperty(new Property("tipoTracciato", this.tipoTracciato.toString()));
				ctx.getApplicationLogger().log("tracciatoNotificaPagamenti.rest");
				ctx.getApplicationLogger().log("tracciatoNotificaPagamenti.spedizione");
				this.inviaTracciatoViaAPIRest(operationId, this.tracciato, this.connettore, this.dominio, tracciatiMyPivotBD, configWrapper, beanDati, serializer, ctx, dumpRequest, dumpResponse);
				break;
			}
			
			this.eventoCtx.setEsito(Esito.OK);
			LogUtils.logInfo(log, MessageFormat.format("Tracciato {0} [Nome: {1}] inviato con successo", this.tipoTracciato, tracciato.getNomeFile()));
		} catch(Exception e) {
			errore = true;
			if(e instanceof GovPayException || e instanceof ClientException)
				LogUtils.logWarn(log, MessageFormat.format("Errore nella Spedizione del tracciato {0}: {1}", this.tipoTracciato, e.getMessage()));
			else
				log.error(MessageFormat.format("Errore nella Spedizione del tracciato {0}", this.tipoTracciato), e);
			
			if(e instanceof GovPayException govPayException) {
				this.eventoCtx.setSottotipoEsito(govPayException.getCodEsito().toString());
			} else if(e instanceof ClientException clientException) {
				this.eventoCtx.setSottotipoEsito(clientException.getResponseCode() + "");
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
				eventiBD.insertEvento(EventoUtils.toEventoDTO(this.eventoCtx,log));
			} catch (ServiceException e) {
				log.error(MSG_ERRORE_ERRORE_DURANTE_IL_SALVATAGGIO_DELL_EVENTO, e);
			}
			
			if(tracciatiMyPivotBD != null) {
				tracciatiMyPivotBD.closeConnection();
			}
			
			this.completed = true;
			ContextThreadLocal.unset();
		}
	}

	private void inviaTracciatoViaAPIRest(String operationId, TracciatoNotificaPagamenti tracciato,
			ConnettoreNotificaPagamenti connettore, Dominio dominio, TracciatiNotificaPagamentiBD tracciatiMyPivotBD,
			BDConfigWrapper configWrapper, it.govpay.core.beans.tracciati.TracciatoNotificaPagamenti beanDati,
			ISerializer serializer, IContext ctx, DumpRequest dumpRequest, DumpResponse dumpResponse) throws ServiceException {
		
		this.eventoCtx.setTipoEvento(Operazione.GOVPAYINVIATRACCIATOREST.name());
		
		GpContext appContext = (GpContext) ctx.getApplicationContext();
		String errore = null;
		List<String> contenuti = connettore.getContenuti();
		
		byte[] blobRawContentuto = tracciatiMyPivotBD.leggiBlobRawContentuto(tracciato.getId(), it.govpay.orm.TracciatoNotificaPagamenti.model().RAW_CONTENUTO);
		
		dumpRequest.getHeaders().put("Destinatari", StringUtils.join(this.connettore.getEmailIndirizzi(), ","));
		dumpRequest.getHeaders().put("Ente creditore", dominio.getRagioneSociale());
		dumpRequest.getHeaders().put("Id Dominio", dominio.getCodDominio());
		String dataInizio = SimpleDateFormatUtils.newSimpleDateFormatSoloData().format(tracciato.getDataRtDa());
		dumpRequest.getHeaders().put("Data inizio", dataInizio);
		String dataFine = SimpleDateFormatUtils.newSimpleDateFormatSoloData().format(tracciato.getDataRtA());
		dumpRequest.getHeaders().put("Data fine", dataFine);
		dumpRequest.getHeaders().put("Numero pagamenti", beanDati.getNumRtTotali()+"");
		dumpRequest.getHeaders().put("Versione tracciato", tracciato.getVersione());
		
		List<String> erroriSpedizione = new ArrayList<String>();
		
		tracciato.setDataCaricamento(new Date());
		
		Map<String, Rpp> mappaRPP = new HashMap<String, Rpp>();
		
		try (ByteArrayInputStream bais = new ByteArrayInputStream(blobRawContentuto);
				ZipInputStream stream = new ZipInputStream(bais)){

			EnteRendicontazioniClient client = null;
			
			ZipEntry entry;
			while ((entry = stream.getNextEntry()) != null) {
            	String entryName = entry.getName();
            	LogUtils.logDebug(log, MessageFormat.format(ELABORAZIONE_ENTRY_0, entryName));
            	appContext.getServerByOperationId(operationId).addGenericProperty(new Property(PROPERTY_NAME_FILE_CONTENUTO, entryName));
            	EventoContext spedizioneCsvEventoCtx = new EventoContext(Componente.API_GOVPAY);
            	
            	try (ByteArrayOutputStream baos = new ByteArrayOutputStream();){
	            	// sintesi pagamenti
	            	
					if(entryName.equals(TracciatiNotificaPagamenti.GOVPAY_RENDICONTAZIONE_CSV_FILE_NAME) 
	            			&& contenuti.contains(ConnettoreNotificaPagamenti.Contenuti.SINTESI_PAGAMENTI.toString())) {
						LogUtils.logDebug(log, MessageFormat.format(SPEDIZIONE_ENTRY_0_IN_CORSO, entryName));
						
						appContext.getServerByOperationId(operationId).addGenericProperty(new Property(PROPERTY_NAME_CONTENUTO, ConnettoreNotificaPagamenti.Contenuti.SINTESI_PAGAMENTI.toString()));
						
						Map<String, String> queryParams = new HashMap<>();
						queryParams.put("dataInizio", TracciatiNotificaPagamentiUtils.encode(SimpleDateFormatUtils.newSimpleDateFormat().format(tracciato.getDataRtDa())));
						queryParams.put("dataFine", TracciatiNotificaPagamentiUtils.encode(SimpleDateFormatUtils.newSimpleDateFormat().format(tracciato.getDataRtA())));
						if(connettore.getTipiPendenza() != null) {
							queryParams.put("tipiPendenza", TracciatiNotificaPagamentiUtils.encode(StringUtils.join(connettore.getTipiPendenza(), ",")));
						}
						
	            		IOUtils.copy(stream, baos);
	            		client = new EnteRendicontazioniClient(dominio, tracciato, connettore, operationId, giornale, spedizioneCsvEventoCtx);
						client.inviaFile(baos.toByteArray(), queryParams, ConnettoreNotificaPagamenti.Contenuti.SINTESI_PAGAMENTI, null);
						spedizioneCsvEventoCtx.setEsito(Esito.OK);
						LogUtils.logDebug(log, MessageFormat.format(SPEDIZIONE_ENTRY_0_COMPLETATA, entryName));
						try {
							ctx.getApplicationLogger().log(MSG_DIAGNOSTICO_TRACCIATO_NOTIFICA_PAGAMENTI_REST_CONTENUTO_OK_KEY);
						} catch (UtilsException e1) {
							log.error(e1.getMessage(), e1);
						}
	            	}
	            	
	            	// sintesi flussi
	            	if(entryName.equals(TracciatiNotificaPagamenti.GOVPAY_FLUSSI_RENDICONTAZIONE_CSV_FILE_NAME) 
	            			&& contenuti.contains(ConnettoreNotificaPagamenti.Contenuti.SINTESI_FLUSSI_RENDICONTAZIONE.toString())) {
	            		LogUtils.logDebug(log, MessageFormat.format(SPEDIZIONE_ENTRY_0_IN_CORSO, entryName));
	            		
	            		appContext.getServerByOperationId(operationId).addGenericProperty(new Property(PROPERTY_NAME_CONTENUTO, ConnettoreNotificaPagamenti.Contenuti.SINTESI_FLUSSI_RENDICONTAZIONE.toString()));
	            		
	            		Map<String, String> queryParams = new HashMap<>();
	            		queryParams.put("dataInizio", TracciatiNotificaPagamentiUtils.encode(SimpleDateFormatUtils.newSimpleDateFormat().format(tracciato.getDataRtDa())));
	            		queryParams.put("dataFine", TracciatiNotificaPagamentiUtils.encode(SimpleDateFormatUtils.newSimpleDateFormat().format(tracciato.getDataRtA())));
	            		if(connettore.getTipiPendenza() != null) {
	            			queryParams.put("tipiPendenza", TracciatiNotificaPagamentiUtils.encode(StringUtils.join(connettore.getTipiPendenza(), ",")));
	            		}
	            		
	            		IOUtils.copy(stream, baos);
	            		client = new EnteRendicontazioniClient(dominio, tracciato, connettore, operationId, giornale, spedizioneCsvEventoCtx);
						client.inviaFile(baos.toByteArray(), queryParams, ConnettoreNotificaPagamenti.Contenuti.SINTESI_FLUSSI_RENDICONTAZIONE, null);
						spedizioneCsvEventoCtx.setEsito(Esito.OK);
						LogUtils.logDebug(log, MessageFormat.format(SPEDIZIONE_ENTRY_0_COMPLETATA, entryName));
						try {
							ctx.getApplicationLogger().log(MSG_DIAGNOSTICO_TRACCIATO_NOTIFICA_PAGAMENTI_REST_CONTENUTO_OK_KEY);
						} catch (UtilsException e1) {
							log.error(e1.getMessage(), e1);
						}
	            	}
	            	
	            	// elenco RT
	            	if(entryName.startsWith(TracciatiNotificaPagamenti.FILE_RT_DIR_PREFIX) 
	            			&& contenuti.contains(ConnettoreNotificaPagamenti.Contenuti.RPP.toString())) {
	            		LogUtils.logDebug(log, MessageFormat.format("Creazione RPP a partire dalla Entry: {0} in corso...", entryName));
	            		
	            		appContext.getServerByOperationId(operationId).addGenericProperty(new Property(PROPERTY_NAME_CONTENUTO, ConnettoreNotificaPagamenti.Contenuti.RPP.toString()));
	            		
	            		IOUtils.copy(stream, baos);
	            		
	            		boolean isRT = TracciatiNotificaPagamentiUtils.isRT(entry.getName());
	            		if(isRT) {
	            			String pathRT = TracciatiNotificaPagamentiUtils.creaPathRT(entry.getName());	
	            			
	            			Rpp remove = mappaRPP.remove(pathRT);
	            			
	            			if(remove == null) {
	            				remove = new  Rpp();
	            			}
	            			
	            			remove.setRt(baos.toByteArray());
	            			
	            			mappaRPP.put(pathRT, remove);
	            			LogUtils.logDebug(log, MessageFormat.format("Creazione RPP a partire dalla Entry: {0} aggiunta RT.", entryName));
	            		} 
	            		
	            		boolean isRPT = TracciatiNotificaPagamentiUtils.isRPT(entry.getName());
	            		if(isRPT) {
	            			String pathRPT = TracciatiNotificaPagamentiUtils.creaPathRPT(entry.getName());
	            			
	            			Rpp remove = mappaRPP.remove(pathRPT);
	            			
	            			if(remove == null) {
	            				remove = new  Rpp();
	            			}
	            			
	            			remove.setRpt(baos.toByteArray());
	            			
	            			mappaRPP.put(pathRPT, remove);
	            			LogUtils.logDebug(log, MessageFormat.format("Creazione RPP a partire dalla Entry: {0} aggiunta RPT.", entryName));
	            		}
	            		LogUtils.logDebug(log, MessageFormat.format("Creazione RPP a partire dalla Entry: {0} completata.", entryName));
	            	}
	            	
	            	// elenco flussi
	            	if(entryName.startsWith(TracciatiNotificaPagamenti.FLUSSI_RENDICONTAZIONE_DIR_PREFIX) 
	            			&& contenuti.contains(ConnettoreNotificaPagamenti.Contenuti.FLUSSI_RENDICONTAZIONE.toString())) {
	            		LogUtils.logDebug(log, MessageFormat.format(SPEDIZIONE_ENTRY_0_IN_CORSO, entryName));
	            		
	            		appContext.getServerByOperationId(operationId).addGenericProperty(new Property(PROPERTY_NAME_CONTENUTO, ConnettoreNotificaPagamenti.Contenuti.FLUSSI_RENDICONTAZIONE.toString()));
	            		
	            		Map<String, String> queryParams = new HashMap<>();
	            		queryParams.put("dataOraFlusso", TracciatiNotificaPagamentiUtils.getDataFlussoRendicontazione(entry.getName()));
	            		
	            		IOUtils.copy(stream, baos);
	            		String pathFlussoRendicontazione = TracciatiNotificaPagamentiUtils.creaPathFlussoRendicontazione(entry.getName());
	            		
	            		client = new EnteRendicontazioniClient(dominio, tracciato, connettore, operationId, giornale, spedizioneCsvEventoCtx);
						client.inviaFile(baos.toByteArray(), queryParams, ConnettoreNotificaPagamenti.Contenuti.FLUSSI_RENDICONTAZIONE, pathFlussoRendicontazione);
						spedizioneCsvEventoCtx.setEsito(Esito.OK);
						LogUtils.logDebug(log, MessageFormat.format(SPEDIZIONE_ENTRY_0_COMPLETATA, entryName));
						try {
							ctx.getApplicationLogger().log(MSG_DIAGNOSTICO_TRACCIATO_NOTIFICA_PAGAMENTI_REST_CONTENUTO_OK_KEY);
						} catch (UtilsException e1) {
							log.error(e1.getMessage(), e1);
						}
	            	}
            	
            	} catch (ClientException e) {
        			errore = MessageFormat.format("Errore durante la spedizione del file {0} del Tracciato {1} [Nome: {2}], al destinatario [{3}]:{4}",	entryName, this.tipoTracciato, tracciato.getNomeFile(), this.connettore.getUrl(), e.getMessage());
        			log.error(errore, e);
        			erroriSpedizione.add(errore);
        			
        			try {
        				ctx.getApplicationLogger().log(MSG_DIAGNOSTICO_TRACCIATO_NOTIFICA_PAGAMENTI_REST_CONTENUTO_KO_KEY);
        			} catch (UtilsException e1) {
        				log.error(e1.getMessage(), e1);
        			}
        			
        			if(spedizioneCsvEventoCtx != null) {
        				spedizioneCsvEventoCtx.setSottotipoEsito(e.getResponseCode() + "");
        				spedizioneCsvEventoCtx.setEsito(Esito.FAIL);
        				spedizioneCsvEventoCtx.setDescrizioneEsito(e.getMessage());
        				spedizioneCsvEventoCtx.setException(e);
        			}
        		} catch (ClientInitializeException e) {
        			errore = MessageFormat.format("Errore durante creazione del client per la spedizione del file {0} del Tracciato {1} [Nome: {2}], al destinatario [{3}]:{4}",	entryName, this.tipoTracciato, tracciato.getNomeFile(), this.connettore.getUrl(), e.getMessage());
        			log.error(errore, e);
        			erroriSpedizione.add(errore);
        			
        			try {
        				ctx.getApplicationLogger().log(MSG_DIAGNOSTICO_TRACCIATO_NOTIFICA_PAGAMENTI_REST_CONTENUTO_KO_KEY);
        			} catch (UtilsException e1) {
        				log.error(e1.getMessage(), e1);
        			}
        			
        			if(spedizioneCsvEventoCtx != null) {
        				spedizioneCsvEventoCtx.setSottotipoEsito(EsitoOperazione.INTERNAL.toString());
        				spedizioneCsvEventoCtx.setEsito(Esito.FAIL);
        				spedizioneCsvEventoCtx.setDescrizioneEsito(e.getMessage());
        				spedizioneCsvEventoCtx.setException(e);
        			}
				} finally {
        			if(spedizioneCsvEventoCtx != null && spedizioneCsvEventoCtx.isRegistraEvento()) {
        				EventiBD eventiBD = new EventiBD(configWrapper);
        				try {
        					eventiBD.insertEvento(EventoUtils.toEventoDTO(spedizioneCsvEventoCtx,log));
        				} catch (ServiceException e) {
        					log.error(MSG_ERRORE_ERRORE_DURANTE_IL_SALVATAGGIO_DELL_EVENTO, e);
        				}
        			}
				}
            }
            
            if(!mappaRPP.isEmpty()) {
            	for (String rppKey : mappaRPP.keySet()) {	
            		EventoContext spedizioneRppEventoCtx = new EventoContext(Componente.API_GOVPAY);
            		try {
						Rpp rpp = mappaRPP.get(rppKey);
						
						LogUtils.logDebug(log, MessageFormat.format("Spedizione RPP: {0} in corso...", rppKey));
						appContext.getServerByOperationId(operationId).addGenericProperty(new Property(PROPERTY_NAME_CONTENUTO, ConnettoreNotificaPagamenti.Contenuti.RPP.toString()));
						
						client = new EnteRendicontazioniClient(dominio, tracciato, connettore, operationId, giornale, spedizioneRppEventoCtx);
						client.inviaFile(ConverterUtils.toJSON(rpp).getBytes(), null, ConnettoreNotificaPagamenti.Contenuti.RPP, rppKey);
						spedizioneRppEventoCtx.setEsito(Esito.OK);
						LogUtils.logDebug(log, MessageFormat.format("Spedizione RPP: {0} completata.", rppKey));
						try {
							ctx.getApplicationLogger().log(MSG_DIAGNOSTICO_TRACCIATO_NOTIFICA_PAGAMENTI_REST_CONTENUTO_OK_KEY);
						} catch (UtilsException e1) {
							log.error(e1.getMessage(), e1);
						}
            		} catch (IOException e) {
            			errore = MessageFormat.format(ERROR_MSG_ERRORE_DURANTE_LA_SPEDIZIONE_RPP_0_DEL_TRACCIATO_1_NOME_2_AL_DESTINATARIO_3_4, rppKey, this.tipoTracciato, tracciato.getNomeFile(), this.connettore.getUrl(), e.getMessage());
            			log.error(errore, e);
            			erroriSpedizione.add(errore);
            			
            			try {
            				ctx.getApplicationLogger().log(MSG_DIAGNOSTICO_TRACCIATO_NOTIFICA_PAGAMENTI_REST_CONTENUTO_KO_KEY);
            			} catch (UtilsException e1) {
            				log.error(e1.getMessage(), e1);
            			}
            			
            			if(spedizioneRppEventoCtx != null) {
            				spedizioneRppEventoCtx.setEsito(Esito.FAIL);
            				spedizioneRppEventoCtx.setDescrizioneEsito(e.getMessage());
            				spedizioneRppEventoCtx.setException(e);
            			}
            		} catch (ClientException e) {
            			errore = MessageFormat.format(ERROR_MSG_ERRORE_DURANTE_LA_SPEDIZIONE_RPP_0_DEL_TRACCIATO_1_NOME_2_AL_DESTINATARIO_3_4, rppKey, this.tipoTracciato, tracciato.getNomeFile(), this.connettore.getUrl(), e.getMessage());
            			log.error(errore, e);
            			erroriSpedizione.add(errore);
            			
            			try {
            				ctx.getApplicationLogger().log(MSG_DIAGNOSTICO_TRACCIATO_NOTIFICA_PAGAMENTI_REST_CONTENUTO_KO_KEY);
            			} catch (UtilsException e1) {
            				log.error(e1.getMessage(), e1);
            			}
            			
            			if(client != null) {
            				spedizioneRppEventoCtx.setSottotipoEsito(e.getResponseCode() + "");
            				spedizioneRppEventoCtx.setEsito(Esito.FAIL);
            				spedizioneRppEventoCtx.setDescrizioneEsito(e.getMessage());
            				spedizioneRppEventoCtx.setException(e);
            			}
            		} catch (ClientInitializeException e) {
            			errore = MessageFormat.format("Errore durante creazione del client per la spedizione RPP {0} del Tracciato {1} [Nome: {2}], al destinatario [{3}]:{4}",	rppKey, this.tipoTracciato, tracciato.getNomeFile(), this.connettore.getUrl(), e.getMessage());
            			log.error(errore, e);
            			erroriSpedizione.add(errore);
            			
            			try {
            				ctx.getApplicationLogger().log(MSG_DIAGNOSTICO_TRACCIATO_NOTIFICA_PAGAMENTI_REST_CONTENUTO_KO_KEY);
            			} catch (UtilsException e1) {
            				log.error(e1.getMessage(), e1);
            			}
            			
            			if(spedizioneRppEventoCtx != null) {
            				spedizioneRppEventoCtx.setSottotipoEsito(EsitoOperazione.INTERNAL.toString());
            				spedizioneRppEventoCtx.setEsito(Esito.FAIL);
            				spedizioneRppEventoCtx.setDescrizioneEsito(e.getMessage());
            				spedizioneRppEventoCtx.setException(e);
            			}
    				} finally {
            			if(spedizioneRppEventoCtx != null && spedizioneRppEventoCtx.isRegistraEvento()) {
            				EventiBD eventiBD = new EventiBD(configWrapper);
            				try {
            					eventiBD.insertEvento(EventoUtils.toEventoDTO(spedizioneRppEventoCtx,log));
            				} catch (ServiceException e) {
            					log.error(MSG_ERRORE_ERRORE_DURANTE_IL_SALVATAGGIO_DELL_EVENTO, e);
            				}
            			}
    				}
				}
            }
			
            if(erroriSpedizione.isEmpty()) {
				try {
					ctx.getApplicationLogger().log(MSG_DIAGNOSTICO_TRACCIATO_NOTIFICA_PAGAMENTI_REST_OK_KEY);
				} catch (UtilsException e1) {
					log.error(e1.getMessage(), e1);
				}
				
				// aggiornare in stato spedito
				tracciato.setStato(STATO_ELABORAZIONE.FILE_CARICATO);
				beanDati.setStepElaborazione(STATO_ELABORAZIONE.FILE_CARICATO.name());
				beanDati.setDescrizioneStepElaborazione(null);
				tracciato.setDataCompletamento(new Date());
				
            } else {
            	// rischedulo esecuzione (non cambio lo stato)
            	try {
            		String errorMsg = erroriSpedizione.size() + (erroriSpedizione.size() == 1 ? " invio non e' andato a buon fine." : " invii non sono andati a buon fine." ) ;
    				ctx.getApplicationLogger().log("tracciatoNotificaPagamenti.restRetryKo", errorMsg);
    			} catch (UtilsException e1) {
    				log.error(e1.getMessage(), e1);
    			}
            	LogUtils.logDebug(log, MessageFormat.format("La spedizione del Tracciato {0} si e'' conclusa con errore, verra'' effettuato un nuovo tentativo durante la prossima esecuzione del Batch di spedizione...", this.tipoTracciato));
            	beanDati.setDescrizioneStepElaborazione(StringUtils.join(erroriSpedizione, ","));
            }
		} catch (java.io.IOException e) {
			errore = MessageFormat.format("Errore durante la lettura del contenuto dello zip dal db durante l''invio del Tracciato {0} [Nome: {1}], al destinatario [{2}]:{3}", this.tipoTracciato, tracciato.getNomeFile(), this.connettore.getUrl(), e.getMessage());
			log.error(errore, e);
			
			tracciato.setStato(STATO_ELABORAZIONE.ERROR_LOAD);
			tracciato.setDataCompletamento(new Date());
			beanDati.setStepElaborazione(STATO_ELABORAZIONE.ERROR_LOAD.name());
			beanDati.setDescrizioneStepElaborazione(errore);
			LogUtils.logDebug(log, MessageFormat.format(DEBUG_MSG_SALVATAGGIO_TRACCIATO_0_IN_STATO_ERROR_LOAD, this.tipoTracciato));
			try {
				ctx.getApplicationLogger().log(MSG_DIAGNOSTICO_TRACCIATO_NOTIFICA_PAGAMENTI_REST_KO_KEY, e.getMessage());
			} catch (UtilsException e1) {
				log.error(e1.getMessage(), e1);
			}
			
			dumpResponse.setPayload(errore.getBytes());
		} finally {
			tracciatiMyPivotBD.setupConnection(configWrapper.getTransactionID());
			try {
				tracciato.setBeanDati(serializer.getObject(beanDati));
			} catch (org.openspcoop2.utils.serialization.IOException e1) {}
			
			tracciatiMyPivotBD.updateFineElaborazione(tracciato);
			
		} 
	}
	


	public boolean isCompleted() {
		return this.completed;
	}
	
	public boolean isErrore() {
		return this.errore;
	}
	
	private void inviaTracciatoViaEmail(TracciatoNotificaPagamenti tracciato, ConnettoreNotificaPagamenti connettore, Dominio dominio, TracciatiNotificaPagamentiBD tracciatiMyPivotBD,
			BDConfigWrapper configWrapper, it.govpay.core.beans.tracciati.TracciatoNotificaPagamenti beanDati, ISerializer serializer, IContext ctx, DumpRequest dumpRequest, DumpResponse dumpResponse  ) throws ServiceException, IOException {
		it.govpay.model.configurazione.MailServer mailserver = null;
		
		try {
			mailserver = AnagraficaManager.getConfigurazione(configWrapper).getBatchSpedizioneEmail().getMailserver();
		} catch (NotFoundException nfe) {
			throw new ServiceException("Configurazione mailserver mancante");
		}
		
		switch (this.tipoTracciato) {
		case MYPIVOT:
			this.eventoCtx.setTipoEvento(Operazione.PIVOTINVIATRACCIATOEMAIL.name());
			break;
		case SECIM:
			this.eventoCtx.setTipoEvento(Operazione.SECIMINVIATRACCIATOEMAIL.name());
			break;
		case GOVPAY:
			this.eventoCtx.setTipoEvento(Operazione.GOVPAYINVIATRACCIATOEMAIL.name());
			break;
		case HYPERSIC_APK:
			this.eventoCtx.setTipoEvento(Operazione.HYPERSICAPKAPPAINVIATRACCIATOEMAIL.name());
			break;
		case MAGGIOLI_JPPA:
			this.eventoCtx.setTipoEvento(Operazione.MAGGIOLIJPPAINVIATRACCIATOEMAIL.name());
			break;
		}
		
		dumpRequest.setContentType("text/plain");
		
		
		String erroreSpedizione = null;
		Sender senderCommonsMail = SenderFactory.newSender(SenderType.JAKARTA_MAIL, log);
		
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
			LogUtils.logDebug(log, "Autenticazione con username e password al mailserver");
			mail.setUsername(username);
			mail.setPassword(password);
		}
		
		SslConfig sslConfigSistema = mailserver.getSslConfig();
		if(sslConfigSistema != null && sslConfigSistema.isAbilitato()) {
			SSLConfig sslConfig = new SSLConfig();
			sslConfig.setSslType(sslConfigSistema.getType());
			sslConfig.setHostnameVerifier(sslConfigSistema.isHostnameVerifier());
			if(sslConfigSistema.getKeyStore() != null) {
				sslConfig.setKeyStoreLocation(sslConfigSistema.getKeyStore().getLocation());
				sslConfig.setKeyManagementAlgorithm(sslConfigSistema.getKeyStore().getManagementAlgorithm());
				sslConfig.setKeyStorePassword(sslConfigSistema.getKeyStore().getPassword());
				sslConfig.setKeyStoreType(sslConfigSistema.getKeyStore().getType());
			}
			
			if(sslConfigSistema.getTrustStore() != null) {
				sslConfig.setTrustStoreLocation(sslConfigSistema.getTrustStore().getLocation());
				sslConfig.setTrustManagementAlgorithm(sslConfigSistema.getTrustStore().getManagementAlgorithm());
				sslConfig.setTrustStorePassword(sslConfigSistema.getTrustStore().getPassword());
				sslConfig.setTrustStoreType(sslConfigSistema.getTrustStore().getType());
			}
			
			LogUtils.logDebug(log, "Abilitazione configurazione SSL per comunicazione al mailserver");
			mail.setSslConfig(sslConfig);
		}
		
		mail.setStartTls(mailserver.isStartTls());
		
		mail.setFrom(from);
		List<String> indirizzi = connettore.getEmailIndirizzi();
		
		mail.setTo(indirizzi.get(0));
		if(indirizzi.size() > 1) {
			mail.setCc(indirizzi.subList(1, indirizzi.size()));
		}
		
		LogUtils.logDebug(log, MessageFormat.format("Invio Tracciato {0} [Nome: {1}], al destinatario [{2}] ...", this.tipoTracciato, tracciato.getNomeFile(), StringUtils.join(this.connettore.getEmailIndirizzi(), ",")));

		this.impostaOggettoEBodyMail(tracciato, dominio, connettore, beanDati, mail);
		
		dumpRequest.getHeaders().put("Destinatari", StringUtils.join(this.connettore.getEmailIndirizzi(), ","));
		dumpRequest.getHeaders().put("Ente creditore", dominio.getRagioneSociale());
		dumpRequest.getHeaders().put("Id Dominio", dominio.getCodDominio());
		String dataInizio = SimpleDateFormatUtils.newSimpleDateFormatSoloData().format(tracciato.getDataRtDa());
		dumpRequest.getHeaders().put("Data inizio", dataInizio);
		String dataFine = SimpleDateFormatUtils.newSimpleDateFormatSoloData().format(tracciato.getDataRtA());
		dumpRequest.getHeaders().put("Data fine", dataFine);
		dumpRequest.getHeaders().put("Numero pagamenti", beanDati.getNumRtTotali()+"");
		dumpRequest.getHeaders().put("Versione tracciato", tracciato.getVersione());
		
		if(connettore.isEmailAllegato()) { // spedizione dell'allegato solo se previsto dal connettore
			String attachmentName = tracciato.getNomeFile();
			byte[] blobRawContentuto = tracciatiMyPivotBD.leggiBlobRawContentuto(tracciato.getId(), it.govpay.orm.TracciatoNotificaPagamenti.model().RAW_CONTENUTO);
			MailAttach avvisoAttach = new MailBinaryAttach(attachmentName, blobRawContentuto);
			mail.getBody().getAttachments().add(avvisoAttach );
		}
		try {
			
			tracciato.setDataCaricamento(new Date());
			LogUtils.logDebug(log, MessageFormat.format("Spediazione Tracciato {0} verso il mail server [{1}]:[{2}]...", this.tipoTracciato,	host, port));
			senderCommonsMail.send(mail, true);
			LogUtils.logDebug(log, MessageFormat.format("Spediazione Tracciato {0} verso il mail server [{1}]:[{2}] completata.", this.tipoTracciato, host, port));
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
			erroreSpedizione = MessageFormat.format("Errore durante l''invio del Tracciato {0} [Nome: {1}], al destinatario [{2}]:{3}", this.tipoTracciato, tracciato.getNomeFile(), StringUtils.join(this.connettore.getEmailIndirizzi(), ","), e.getMessage());
			
			gestisciUtilsException(beanDati, erroreSpedizione, e);
			
			dumpResponse.setPayload(erroreSpedizione.getBytes());
		} finally {
			tracciatiMyPivotBD.setupConnection(configWrapper.getTransactionID());
			try {
				tracciato.setBeanDati(serializer.getObject(beanDati));
			} catch (org.openspcoop2.utils.serialization.IOException e1) {}
			
			tracciatiMyPivotBD.updateFineElaborazione(tracciato);
			
		} 
	}
	
	private void gestisciUtilsException(it.govpay.core.beans.tracciati.TracciatoNotificaPagamenti beanDati, String errore, UtilsException e) throws ServiceException {
		Throwable innerException= ExceptionUtils.estraiInnerExceptionDaUtilsException(e); 
		
		if(innerException != null) {
			LogUtils.logInfo(log, errore, e);
			LogUtils.logDebug(log, MessageFormat.format("La spedizione del Tracciato {0} si e'' conclusa con errore che non prevede la rispedizione...", this.tipoTracciato));
			tracciato.setStato(STATO_ELABORAZIONE.ERROR_LOAD);
			tracciato.setDataCompletamento(new Date());
			beanDati.setStepElaborazione(STATO_ELABORAZIONE.ERROR_LOAD.name());
			beanDati.setDescrizioneStepElaborazione(errore);
			LogUtils.logDebug(log, MessageFormat.format(DEBUG_MSG_SALVATAGGIO_TRACCIATO_0_IN_STATO_ERROR_LOAD, this.tipoTracciato));
			try {
				ctx.getApplicationLogger().log("tracciatoNotificaPagamenti.emailKo", e.getMessage());
			} catch (UtilsException e1) {
				log.error(e1.getMessage(), e1);
			}
		} else {
			log.error(errore, e);
			try {
				ctx.getApplicationLogger().log("tracciatoNotificaPagamenti.emailRetryKo", e.getMessage());
			} catch (UtilsException e1) {
				log.error(e1.getMessage(), e1);
			}
			LogUtils.logDebug(log, MessageFormat.format("La spedizione del Tracciato {0} si e'' conclusa con errore, verra'' effettuato un nuovo tentativo durante la prossima esecuzione del Batch di spedizione...", this.tipoTracciato));
		}
	}

	private void impostaOggettoEBodyMail(TracciatoNotificaPagamenti tracciato, Dominio dominio, ConnettoreNotificaPagamenti connettore,
			it.govpay.core.beans.tracciati.TracciatoNotificaPagamenti beanDati,
			org.openspcoop2.utils.mail.Mail mail) {
		
		String tipoTracciatoString = null;
		String dataInizio = SimpleDateFormatUtils.newSimpleDateFormatDataOra().format(tracciato.getDataRtDa());
		String dataFine = SimpleDateFormatUtils.newSimpleDateFormatDataOra().format(tracciato.getDataRtA());
		switch (this.tipoTracciato) {
		case MYPIVOT:
			tipoTracciatoString = " per l'importazione in MyPivot"; 
			break;
		case SECIM:
			tipoTracciatoString = " per l'importazione in Secim"; 
			break;
		case GOVPAY:
			tipoTracciatoString = ""; 
			break;
		case HYPERSIC_APK:
			tipoTracciatoString = " per l'importazione in APKappa"; 
			break;
		case MAGGIOLI_JPPA:
			tipoTracciatoString = " inviati al servizio Maggioli JPPA"; 
			break;
		}
		
		if(connettore.getEmailSubject() != null && !connettore.getEmailSubject().isEmpty()) {
			mail.setSubject(connettore.getEmailSubject());
		} else {
			mail.setSubject(MessageFormat.format(OGGETTO_DEFAULT_MAIL_GOVPAY_EXPORT_PAGAMENTI_TIPO_AL_DATA, tipoTracciatoString, dataFine));
		}
		
		StringBuilder  sb = new StringBuilder();
		
		sb.append("Salve,");
		sb.append("\n");
		
		if(connettore.isEmailAllegato()) {
			sb.append("\nin allegato alla presente il tracciato dei pagamenti").append(tipoTracciatoString).append(":");
		} else {
			sb.append("\nall'indirizzo: ");
			sb.append("\n");
			sb.append(TracciatiNotificaPagamentiUtils.getURLDownloadTracciato(connettore, tracciato));
			sb.append("\n");
			sb.append("e' possibile scaricare il tracciato dei pagamenti").append(tipoTracciatoString).append(":");
		}
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
		
		LogUtils.logDebug(log, MessageFormat.format("Salvataggio Tracciato {0} [Nome: {1}], su FileSystem [{2}] ...", this.tipoTracciato, tracciato.getNomeFile(), connettore.getFileSystemPath()));
		String erroreSalvataggioFile = null;
		boolean retry = true;
		try {
			switch (this.tipoTracciato) {
			case MYPIVOT:
				this.eventoCtx.setTipoEvento(Operazione.PIVOTINVIATRACCIATOFILESYSTEM.name());
				break;
			case SECIM:
				this.eventoCtx.setTipoEvento(Operazione.SECIMINVIATRACCIATOFILESYSTEM.name());
				break;
			case GOVPAY:
				this.eventoCtx.setTipoEvento(Operazione.GOVPAYINVIATRACCIATOFILESYSTEM.name());
				break;
			case HYPERSIC_APK:
				this.eventoCtx.setTipoEvento(Operazione.HYPERSICAPKAPPAINVIATRACCIATOFILESYSTEM.name());
				break;
			case MAGGIOLI_JPPA:
				throw new ServiceException("Modalita' di spedizione non valida");
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
						
						dumpRequest.getHeaders().put("FilePath", fileName);
						dumpRequest.getHeaders().put("Ente creditore", dominio.getRagioneSociale());
						dumpRequest.getHeaders().put("Id Dominio", dominio.getCodDominio());
						String dataInizio = SimpleDateFormatUtils.newSimpleDateFormatSoloData().format(tracciato.getDataRtDa());
						dumpRequest.getHeaders().put("Data inizio", dataInizio);
						String dataFine = SimpleDateFormatUtils.newSimpleDateFormatSoloData().format(tracciato.getDataRtA());
						dumpRequest.getHeaders().put("Data fine", dataFine);
						dumpRequest.getHeaders().put("Numero pagamenti", beanDati.getNumRtTotali()+"");
						dumpRequest.getHeaders().put("Versione tracciato", tracciato.getVersione());
						
						LogUtils.logDebug(log, MessageFormat.format("Salvataggio Tracciato {0} [Nome: {1}], su FileSystem [{2}] completato.", this.tipoTracciato, tracciato.getNomeFile(), connettore.getFileSystemPath()));
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
						erroreSalvataggioFile = MessageFormat.format("Errore durante il salvataggio del Tracciato {0} [Nome: {1}], su FileSystem [{2}]:{3}", this.tipoTracciato, tracciato.getNomeFile(), connettore.getFileSystemPath(), e.getMessage());
						log.error(erroreSalvataggioFile, e);
					} finally {
						
					}
				} else {
					erroreSalvataggioFile = MessageFormat.format("Errore durante il salvataggio del Tracciato {0} [Nome: {1}], su FileSystem [{2}]: accesso in scrittura alla directory non consentito.", this.tipoTracciato, tracciato.getNomeFile(), connettore.getFileSystemPath());
					LogUtils.logDebug(log, MessageFormat.format("Salvataggio Tracciato {0} [Nome: {1}], su FileSystem [{2}] accesso in scrittura alla directory non consentito.", this.tipoTracciato, tracciato.getNomeFile(), connettore.getFileSystemPath()));
					retry = false;
				}
			} else {
				erroreSalvataggioFile = MessageFormat.format("Errore durante il salvataggio del Tracciato {0} [Nome: {1}], su FileSystem [{2}]: directory non presente.", this.tipoTracciato, tracciato.getNomeFile(), connettore.getFileSystemPath());
				LogUtils.logDebug(log, MessageFormat.format("Salvataggio Tracciato {0} [Nome: {1}], su FileSystem [{2}] directory non presente.", this.tipoTracciato, tracciato.getNomeFile(), connettore.getFileSystemPath()));
				retry = false;
			}
			
			if(erroreSalvataggioFile != null) {
				if(!retry) {
					LogUtils.logDebug(log, MessageFormat.format("Salvataggio Tracciato {0} [Nome: {1}], su FileSystem [{2}] si e'' concluso con errore che non prevede la rispedizione...", this.tipoTracciato, tracciato.getNomeFile(), connettore.getFileSystemPath()));
					tracciato.setStato(STATO_ELABORAZIONE.ERROR_LOAD);
					tracciato.setDataCompletamento(new Date());
					beanDati.setStepElaborazione(STATO_ELABORAZIONE.ERROR_LOAD.name());
					beanDati.setDescrizioneStepElaborazione(erroreSalvataggioFile);
					LogUtils.logDebug(log, MessageFormat.format(DEBUG_MSG_SALVATAGGIO_TRACCIATO_0_IN_STATO_ERROR_LOAD, this.tipoTracciato));
					try {
						ctx.getApplicationLogger().log("tracciatoNotificaPagamenti.fileSystemKo", erroreSalvataggioFile);
					} catch (UtilsException e1) {
						log.error(e1.getMessage(), e1);
					}
				} else {
					try {
						ctx.getApplicationLogger().log("tracciatoNotificaPagamenti.fileSystemRetryKo", erroreSalvataggioFile);
					} catch (UtilsException e1) {
						log.error(e1.getMessage(), e1);
					}
					LogUtils.logDebug(log, MessageFormat.format("Salvataggio Tracciato {0} [Nome: {1}], su FileSystem [{2}] si e'' concluso con errore, verra'' effettuato un nuovo tentativo durante la prossima esecuzione del Batch di spedizione...", this.tipoTracciato, tracciato.getNomeFile(), connettore.getFileSystemPath()));
				}
				dumpResponse.setPayload(erroreSalvataggioFile.getBytes());
			}
		} finally {
			tracciatiMyPivotBD.setupConnection(configWrapper.getTransactionID());
			try {
				tracciato.setBeanDati(serializer.getObject(beanDati));
			} catch (org.openspcoop2.utils.serialization.IOException e1) {}
			
			tracciatiMyPivotBD.updateFineElaborazione(tracciato);
			
		} 
	}
	
	
	protected void popolaContextEvento(TipoConnettore tipoConnettore, String url, DumpRequest dumpRequest, DumpResponse dumpResponse, EventoContext eventoContext) {
		if(GovpayConfig.getInstance().isGiornaleEventiEnabled()) {
			boolean logEvento = false;
			boolean dumpEvento = false;
			GdeInterfaccia configurazioneInterfaccia = EventiUtils.getConfigurazioneComponente(this.componente, this.giornale);
			
			LogUtils.logDebug(log, MessageFormat.format("Log Evento Client: [{0}] Tipo Connettore [{1}], Destinatario [{2}], Esito [{3}]", this.componente, tipoConnettore, url, eventoContext.getEsito()));

			if(configurazioneInterfaccia != null) {
				try {
					LogUtils.logDebug(log, MessageFormat.format("Configurazione Giornale Eventi API: [{0}]: {1}", this.componente, ConverterUtils.toJSON(configurazioneInterfaccia)));
				} catch (IOException e) {
					log.error(MessageFormat.format("Errore durante il log della configurazione giornale eventi: {0}", e.getMessage()), e);
				}
				
				if(EventiUtils.isRequestLettura(null, this.componente, eventoContext.getTipoEvento())) {
					logEvento = EventiUtils.logEvento(configurazioneInterfaccia.getLetture(), eventoContext.getEsito());
					dumpEvento = EventiUtils.dumpEvento(configurazioneInterfaccia.getLetture(), eventoContext.getEsito());
					LogUtils.logDebug(log, MessageFormat.format("Tipo Operazione ''Lettura'', Log [{0}], Dump [{1}].", logEvento, dumpEvento));
				} else if(EventiUtils.isRequestScrittura(null, this.componente, eventoContext.getTipoEvento())) {
					logEvento = EventiUtils.logEvento(configurazioneInterfaccia.getScritture(), eventoContext.getEsito());
					dumpEvento = EventiUtils.dumpEvento(configurazioneInterfaccia.getScritture(), eventoContext.getEsito());
					LogUtils.logDebug(log, MessageFormat.format("Tipo Operazione ''Scrittura'', Log [{0}], Dump [{1}].", logEvento, dumpEvento));
				} else {
					LogUtils.logDebug(log, "Tipo Operazione non riconosciuta, l'evento non verra' salvato.");
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
				LogUtils.logWarn(log, MessageFormat.format("La configurazione per l''API [{0}] non e'' corretta, salvataggio evento non eseguito.",	this.componente)); 
			}
		}
	}
}
