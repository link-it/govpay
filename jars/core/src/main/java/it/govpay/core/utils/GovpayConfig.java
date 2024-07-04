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
package it.govpay.core.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.openspcoop2.utils.LoggerWrapperFactory;
import org.openspcoop2.utils.TipiDatabase;
import org.openspcoop2.utils.logger.constants.Severity;
import org.openspcoop2.utils.logger.log4j.Log4jLoggerWithApplicationContext;
import org.slf4j.Logger;

import it.govpay.bd.pagamento.util.CustomIuv;
import it.govpay.core.beans.Costanti;
import it.govpay.core.dao.anagrafica.dto.BasicFindRequestDTO;
import it.govpay.core.exceptions.ConfigException;
import it.govpay.core.exceptions.InvalidPropertyException;
import it.govpay.core.exceptions.PropertyNotFoundException;
import it.govpay.core.utils.client.handler.IntegrationOutHandler;
import it.govpay.model.Versamento;

public class GovpayConfig {

	private static final String MSG_ERRORE_DI_INIZIALIZZAZIONE_0_ASSUNTO_VALORE_DI_DEFAULT_1 = "Errore di inizializzazione: {0}. Assunto valore di default: {1}";

	public enum VersioneAvviso {
		v001, v002;
	}

	public static final String PROPERTIES_FILE_NAME = "govpay.properties";
	public static final String PROPERTIES_FILE = "/" + PROPERTIES_FILE_NAME;
	public static final String MSG_DIAGNOSTICI_PROPERTIES_FILE_NAME = "msgDiagnostici.properties";
	public static final String MSG_DIAGNOSTICI_PROPERTIES_FILE = "/" + MSG_DIAGNOSTICI_PROPERTIES_FILE_NAME;
	public static final String LOG4J2_XML_FILE_NAME = "log4j2.xml";
	public static final String LOG4J2_XML_FILE = "/" + LOG4J2_XML_FILE_NAME;
	
	private static GovpayConfig instance;

	public static GovpayConfig getInstance() {
		return instance;
	}

	public static GovpayConfig newInstance(InputStream is) throws Exception {
		instance = new GovpayConfig(is);
		return instance;
	}

	private List<String> outHandlers;

	private URI log4j2Config;
	private String resourceDir;
	private VersioneAvviso versioneAvviso;
	private int dimensionePoolThreadNotifica;
	private int dimensionePoolThreadNotificaAppIo;
	private int dimensionePoolThreadRPT;
	private int dimensionePoolThreadCaricamentoTracciati;
	private int dimensionePoolThreadCaricamentoTracciatiStampaAvvisi;
	private int dimensionePoolThreadSpedizioneTracciatiNotificaPagamenti;
	private int dimensionePoolThreadSpedizioneNotificaPagamentoMaggioli;
	private String ksLocation, ksPassword, ksAlias;
	private String mLogClass, mLogDS;
	private Severity mLogLevel;
	private TipiDatabase mLogDBType;
	private boolean mLogOnLog4j, mLogOnDB, mLogSql, pddAuthEnable;
	private boolean batchOn;
	private String clusterId;
	private long timeoutBatch;

	private boolean batchCaricamentoTracciati;
	private boolean timeoutPendentiModello3;
	private Integer timeoutPendentiModello3Mins;
	private boolean timeoutPendentiModello1;
	private Integer timeoutPendentiModello1Mins;
	private Integer intervalloControlloRptPendenti;
	
	private Integer timeoutPendentiModello3_SANP_24_Mins;
	
	private Integer timeoutInvioRPTModello3Millis;
	
	private Properties[] props;
	
	private String appName;
	private String ambienteDeploy;
	
	private String autenticazioneSPIDNomeHeaderPrincipal;
	private Map<String,String> autenticazioneSPIDElencoHeadersRequest;
	private boolean checkCfDebitore;
	private List<String> autenticazioneHeaderNomeHeaderPrincipal;
	private List<String> autenticazioneHeaderElencoHeadersRequest;
	private Properties autenticazioneSSLHeaderProperties;
	
	// nomi header dove leggere le informazioni per l'autenticazione API-Key (Erogazione)
	private String autenticazioneApiKeyNomeHeaderApiKey;
	private String autenticazioneApiKeyNomeHeaderApiId;
	
	// nomi header dove impostare l'autenticazione API-Key (Fruizione)
	private String autenticazioneApiKeyNomeHeaderApiKeyFruizione;
	private String autenticazioneApiKeyNomeHeaderApiIdFruizione;
	
	private int numeroMassimoEntriesProspettoRiscossione;
	
	private Integer intervalloDisponibilitaPagamentoUtenzaAnonima;
	
	private boolean scritturaDiagnosticiFileEnabled;
	private boolean scritturaDumpFileEnabled;
	private boolean giornaleEventiEnabled;
	
	private String codTipoVersamentoPendenzeLibere;
	private String codTipoVersamentoPendenzeNonCensite;
	private boolean censimentoTipiVersamentoSconosciutiEnabled;
	
	private Properties corsProperties;
	
	private CustomIuv defaultCustomIuvGenerator;
	
	private String templateProspettoRiscossioni;
	
	private Properties apiUserLoginRedirectURLs;
	private Properties apiUserLogoutRedirectURLs;
	
	private Integer batchCaricamentoTracciatiNumeroVersamentiDaCaricarePerThread;
	private Integer batchCaricamentoTracciatiNumeroAvvisiDaStamparePerThread;
	
	private boolean aggiornamentoValiditaMandatorio;
	
	private Integer dimensioneMassimaListaRisultati;
	
	private boolean batchCaricamentoTracciatiNotificaPagamenti;
	private Integer batchCaricamentoTracciatiNotificaPagamentiDimensionePagina;
	
	private boolean ricercaRiconciliazioniIdFlussoCaseInsensitive;
	
	
	private Integer connectionTimeout;
	private Integer readTimeout;
	private Integer connectionRequestTimeout;
	private Integer numeroMassimoConnessioniPerPool;
	private Integer numeroMassimoConnessioniPerRouteDefault;
	
	private Integer numeroMassimoGiorniRPTPendenti;
	
	private String templateQuietanzaPagamento;
	
	private String checkoutBaseURL;
	private boolean checkoutEnabled;
	
	private boolean conversioneMessaggiPagoPAV2NelFormatoV1;
	
	private String nomeHeaderSubscriptionKeyPagoPA;
	
	private boolean dismettiIUVIso11694;
	
	private String operazioneVerifica;
	
	private Integer numeroGiorniValiditaPendenza;
	
	private boolean batchRecuperoRPTPendenti;
	private boolean batchAcquisizioneRendicontazioni;
	private boolean batchChiusuraRPTScadute;
	private boolean batchElaborazioneRiconciliazioni;
	private boolean batchGestionePromemoria;
	private boolean batchSpedizioneNotifiche;
	private boolean batchSpedizioneNotificheAppIO;
	private boolean batchSpedizionePromemoria;
	
	private List<String> keywordsDaSostituireIdentificativiDebitoreAvviso;
	
	private boolean controlloPasswordBackwardCompatibilityMD5;
	
	
	public GovpayConfig(InputStream is) throws Exception {
		// Default values:
		this.versioneAvviso = VersioneAvviso.v002;
		this.dimensionePoolThreadNotifica = 10;
		this.dimensionePoolThreadNotificaAppIo = 10;
		this.dimensionePoolThreadCaricamentoTracciati = 10;
		this.dimensionePoolThreadCaricamentoTracciatiStampaAvvisi = 10;
		this.dimensionePoolThreadRPT = 10;
		this.dimensionePoolThreadSpedizioneTracciatiNotificaPagamenti = 10;
		this.dimensionePoolThreadSpedizioneNotificaPagamentoMaggioli = 10;
		this.log4j2Config = null;
		this.ksAlias = null;
		this.ksLocation = null;
		this.ksPassword = null;
		this.mLogClass = Log4jLoggerWithApplicationContext.class.getName(); //Log4JLoggerWithProxyContext.class.getName();
		this.mLogLevel = Severity.INFO;
		this.mLogOnDB = false;
		this.mLogOnLog4j = true;
		this.mLogSql = false;
		this.mLogDBType = null;
		this.mLogDS = null;
		this.batchOn=true;
		this.pddAuthEnable = true;
		this.batchCaricamentoTracciati = false;
		this.timeoutPendentiModello3 = false;
		this.timeoutPendentiModello3Mins = null;
		this.timeoutPendentiModello1 = false;
		this.timeoutPendentiModello1Mins = null;
		this.timeoutPendentiModello3_SANP_24_Mins = 30;
		this.timeoutInvioRPTModello3Millis = 100;
		
		this.appName = null;
		this.ambienteDeploy = null;

		this.checkCfDebitore = false;
		
		this.autenticazioneSPIDNomeHeaderPrincipal = null;
		this.autenticazioneSPIDElencoHeadersRequest = new HashMap<>();
		this.autenticazioneHeaderNomeHeaderPrincipal = null;
		this.autenticazioneHeaderElencoHeadersRequest = new ArrayList<>();
		this.numeroMassimoEntriesProspettoRiscossione = 5000;
		this.autenticazioneSSLHeaderProperties = new Properties();
		
		this.intervalloControlloRptPendenti = 30;
		this.intervalloDisponibilitaPagamentoUtenzaAnonima = 60;
		
		this.codTipoVersamentoPendenzeLibere = Versamento.TIPO_VERSAMENTO_LIBERO;
		this.codTipoVersamentoPendenzeNonCensite = Versamento.TIPO_VERSAMENTO_LIBERO;
		this.censimentoTipiVersamentoSconosciutiEnabled = false;
		
		this.scritturaDiagnosticiFileEnabled = false;
		this.scritturaDumpFileEnabled = false;
		this.giornaleEventiEnabled = true;
		this.corsProperties = new Properties();
		this.templateProspettoRiscossioni = null;
		
		this.apiUserLoginRedirectURLs = new Properties();
		this.apiUserLogoutRedirectURLs = new Properties();
		
		this.batchCaricamentoTracciatiNumeroVersamentiDaCaricarePerThread = 100;
		this.batchCaricamentoTracciatiNumeroAvvisiDaStamparePerThread = 100;
		
		this.dimensioneMassimaListaRisultati = BasicFindRequestDTO.DEFAULT_MAX_LIMIT;
		
		this.batchCaricamentoTracciatiNotificaPagamenti = false;
		this.ricercaRiconciliazioniIdFlussoCaseInsensitive = false;
		
		this.readTimeout = 180000;
		this.connectionTimeout = 10000;
		this.connectionRequestTimeout = 180000;
		this.numeroMassimoConnessioniPerRouteDefault = 20;
		this.numeroMassimoConnessioniPerPool = 200;
		
		this.aggiornamentoValiditaMandatorio = false;
		
		this.numeroMassimoGiorniRPTPendenti = 30;
		
		this.templateProspettoRiscossioni = null;
		
		this.checkoutBaseURL = null;
		this.checkoutEnabled = false;
		
		this.conversioneMessaggiPagoPAV2NelFormatoV1 = false;
		
		this.nomeHeaderSubscriptionKeyPagoPA = null;
		
		this.dismettiIUVIso11694 = false;
		
		this.operazioneVerifica = null;
		
		this.numeroGiorniValiditaPendenza = null;
		
		this.keywordsDaSostituireIdentificativiDebitoreAvviso = new ArrayList<>();
		
		this.batchRecuperoRPTPendenti = false;
		this.batchAcquisizioneRendicontazioni = false;
		this.batchChiusuraRPTScadute = false;
		this.batchElaborazioneRiconciliazioni = false;
		this.batchGestionePromemoria = false;
		this.batchSpedizioneNotifiche = false;
		this.batchSpedizioneNotificheAppIO = false;
		this.batchSpedizionePromemoria = false;
		
		this.controlloPasswordBackwardCompatibilityMD5 = false;
		
		try {

			// Recupero il property all'interno dell'EAR
			this.props = new Properties[2];
			Properties props1 = new Properties();
			props1.load(is);
			this.props[1] = props1;

			// Recupero la configurazione della working dir
			// Se e' configurata, la uso come prioritaria

			try {
				this.resourceDir = getProperty("it.govpay.resource.path", props1, false, true, null);
				if(this.resourceDir != null) {
					File resourceDirFile = new File(escape(this.resourceDir));
					if(!resourceDirFile.isDirectory())
						throw new Exception(MessageFormat.format("Il path indicato nella property \"it.govpay.resource.path\" ({0}) non esiste o non e'' un folder.", this.resourceDir));

					File log4j2ConfigFile = new File(this.resourceDir + File.separatorChar + LOG4J2_XML_FILE_NAME);

					if(log4j2ConfigFile.exists()) {
						this.log4j2Config = log4j2ConfigFile.toURI();
						LoggerWrapperFactory.getLogger("boot").info(MessageFormat.format("Individuata configurazione log4j: {0}", this.log4j2Config));
					} else {
						LoggerWrapperFactory.getLogger("boot").info("Individuata configurazione log4j interna.");
					}
				}
			} catch (Exception e) {
				LoggerWrapperFactory.getLogger("boot").warn(MessageFormat.format("Errore di inizializzazione: {0}. Property ignorata.", e.getMessage()));
			}
		} catch (Exception e) {
			throw e;
		}
	}

	public void readProperties() throws ConfigException {
		Logger log = LoggerWrapperFactory.getLogger("boot");
		try {
			Properties props0 = null;
			this.props[0] = props0;

			File gpConfigFile = new File(this.resourceDir + File.separatorChar + PROPERTIES_FILE_NAME);
			if(gpConfigFile.exists()) {
				props0 = new Properties();
				
				try(InputStream is = new FileInputStream(gpConfigFile)) {
					props0.load(is);
				} catch (FileNotFoundException e) {
					throw new ConfigException(e);
				} catch (IOException e) {
					throw new ConfigException(e);
				} 
				log.info(MessageFormat.format("Individuata configurazione prioritaria: {0}", gpConfigFile.getAbsolutePath()));
				this.props[0] = props0;
			}

			try {
				String versioneAvvisoProperty = getProperty("it.govpay.avviso.versione", this.props, false, log);
				if(versioneAvvisoProperty != null && !versioneAvvisoProperty.trim().isEmpty()) {
					try {
						this.versioneAvviso = VersioneAvviso.valueOf(versioneAvvisoProperty.trim());
					} catch (Exception e) {
						throw new Exception(MessageFormat.format("Valore della property \"it.govpay.avviso.versione\" non consetito [{0}]. Valori ammessi: {1}", versioneAvvisoProperty, Arrays.toString(VersioneAvviso.values())));
					}
				}
			} catch (Exception e) {
				log.warn(MessageFormat.format(MSG_ERRORE_DI_INIZIALIZZAZIONE_0_ASSUNTO_VALORE_DI_DEFAULT_1, e.getMessage(), VersioneAvviso.v002));
				this.versioneAvviso = VersioneAvviso.v002;
			}

			try {
				String dimensionePoolProperty = getProperty("it.govpay.thread.pool.notifica", this.props, false, log);
				if(dimensionePoolProperty != null && !dimensionePoolProperty.trim().isEmpty()) {
					try {
						this.dimensionePoolThreadNotifica = Integer.parseInt(dimensionePoolProperty.trim());
					} catch (Exception e) {
						throw new Exception("Valore della property \"it.govpay.thread.pool.notifica\" non e' un numero intero");
					}
				}
			} catch (Exception e) {
				log.warn(MessageFormat.format(MSG_ERRORE_DI_INIZIALIZZAZIONE_0_ASSUNTO_VALORE_DI_DEFAULT_1, e.getMessage(), 10));
				this.dimensionePoolThreadNotifica = 10;
			}
			
			try {
				String dimensionePoolProperty = getProperty("it.govpay.thread.pool.notificaAppIO", this.props, false, log);
				if(dimensionePoolProperty != null && !dimensionePoolProperty.trim().isEmpty()) {
					try {
						this.dimensionePoolThreadNotificaAppIo = Integer.parseInt(dimensionePoolProperty.trim());
					} catch (Exception e) {
						throw new Exception("Valore della property \"it.govpay.thread.pool.notificaAppIO\" non e' un numero intero");
					}
				}
			} catch (Exception e) {
				log.warn(MessageFormat.format(MSG_ERRORE_DI_INIZIALIZZAZIONE_0_ASSUNTO_VALORE_DI_DEFAULT_1, e.getMessage(), 10));
				this.dimensionePoolThreadNotificaAppIo = 10;
			}
			
			try {
				String dimensionePoolProperty = getProperty("it.govpay.thread.pool.rpt", this.props, false, log);
				if(dimensionePoolProperty != null && !dimensionePoolProperty.trim().isEmpty()) {
					try {
						this.dimensionePoolThreadRPT = Integer.parseInt(dimensionePoolProperty.trim());
					} catch (Exception e) {
						throw new Exception("Valore della property \"it.govpay.thread.pool.rpt\" non e' un numero intero");
					}
				}
			} catch (Exception e) {
				log.warn(MessageFormat.format(MSG_ERRORE_DI_INIZIALIZZAZIONE_0_ASSUNTO_VALORE_DI_DEFAULT_1, e.getMessage(), 10));
				this.dimensionePoolThreadRPT = 10;
			}
			
			try {
				String dimensionePoolProperty = getProperty("it.govpay.thread.pool.caricamentoTracciati.stampeAvvisiPagamento", this.props, false, log);
				if(dimensionePoolProperty != null && !dimensionePoolProperty.trim().isEmpty()) {
					try {
						this.dimensionePoolThreadCaricamentoTracciatiStampaAvvisi = Integer.parseInt(dimensionePoolProperty.trim());
					} catch (Exception e) {
						throw new Exception("Valore della property \"it.govpay.thread.pool.caricamentoTracciati.stampeAvvisiPagamento\" non e' un numero intero");
					}
				}
			} catch (Exception e) {
				log.warn(MessageFormat.format(MSG_ERRORE_DI_INIZIALIZZAZIONE_0_ASSUNTO_VALORE_DI_DEFAULT_1, e.getMessage(), 10));
				this.dimensionePoolThreadCaricamentoTracciatiStampaAvvisi = 10;
			}
			
			try {
				String dimensionePoolProperty = getProperty("it.govpay.thread.pool.caricamentoTracciati", this.props, false, log);
				if(dimensionePoolProperty != null && !dimensionePoolProperty.trim().isEmpty()) {
					try {
						this.dimensionePoolThreadCaricamentoTracciati = Integer.parseInt(dimensionePoolProperty.trim());
					} catch (Exception e) {
						throw new Exception("Valore della property \"it.govpay.thread.pool.caricamentoTracciati\" non e' un numero intero");
					}
				}
			} catch (Exception e) {
				log.warn(MessageFormat.format(MSG_ERRORE_DI_INIZIALIZZAZIONE_0_ASSUNTO_VALORE_DI_DEFAULT_1, e.getMessage(), 10));
				this.dimensionePoolThreadCaricamentoTracciati = 10;
			}
			
			try {
				String dimensionePoolProperty = getProperty("it.govpay.thread.pool.spedizioneTracciatiNotificaPagamenti", this.props, false, log);
				if(dimensionePoolProperty != null && !dimensionePoolProperty.trim().isEmpty()) {
					try {
						this.dimensionePoolThreadSpedizioneTracciatiNotificaPagamenti = Integer.parseInt(dimensionePoolProperty.trim());
					} catch (Exception e) {
						throw new Exception("Valore della property \"it.govpay.thread.pool.spedizioneTracciatiNotificaPagamenti\" non e' un numero intero");
					}
				}
			} catch (Exception e) {
				log.warn(MessageFormat.format(MSG_ERRORE_DI_INIZIALIZZAZIONE_0_ASSUNTO_VALORE_DI_DEFAULT_1, e.getMessage(), 10));
				this.dimensionePoolThreadSpedizioneTracciatiNotificaPagamenti = 10;
			}
			
			try {
				String dimensionePoolProperty = getProperty("it.govpay.thread.pool.spedizioneNotificaPagamentoMaggioliJPPA", this.props, false, log);
				if(dimensionePoolProperty != null && !dimensionePoolProperty.trim().isEmpty()) {
					try {
						this.dimensionePoolThreadSpedizioneNotificaPagamentoMaggioli = Integer.parseInt(dimensionePoolProperty.trim());
					} catch (Exception e) {
						throw new Exception("Valore della property \"it.govpay.thread.pool.spedizioneNotificaPagamentoMaggioliJPPA\" non e' un numero intero");
					}
				}
			} catch (Exception e) {
				log.warn(MessageFormat.format(MSG_ERRORE_DI_INIZIALIZZAZIONE_0_ASSUNTO_VALORE_DI_DEFAULT_1, e.getMessage(), 10));
				this.dimensionePoolThreadSpedizioneNotificaPagamentoMaggioli = 10;
			}


			String mLogClassString = getProperty("it.govpay.mlog.class", this.props, false, log);
			if(mLogClassString != null && !mLogClassString.isEmpty()) 
				this.mLogClass = mLogClassString;

			String mLogOnLog4jString = getProperty("it.govpay.mlog.log4j", this.props, false, log);
			if(mLogOnLog4jString != null && !Boolean.valueOf(mLogOnLog4jString))
				this.mLogOnLog4j = false;


			String mLogOnLevelString = getProperty("it.govpay.mlog.level", this.props, false, log);
			try {
				this.mLogLevel = Severity.valueOf(mLogOnLevelString);
			} catch (Exception e) {
				log.warn(MessageFormat.format("Valore [{0}] non consentito per la property \"it.govpay.mlog.level\". Assunto valore di default \"INFO\".", mLogOnLevelString));
			}

			String mLogOnDBString = getProperty("it.govpay.mlog.db", this.props, false, log);
			if(mLogOnDBString != null && Boolean.valueOf(mLogOnDBString))
				this.mLogOnDB = true;

			if(this.mLogOnDB) {
				String mLogDBTypeString = getProperty("it.govpay.mlog.db.type", this.props, true, log);
				try {
					this.mLogDBType = TipiDatabase.valueOf(mLogDBTypeString);
				} catch (IllegalArgumentException e) {
					throw new InvalidPropertyException(MessageFormat.format("Valore [{0}] non consentito per la property \"it.govpay.mlog.db.type\": {1}", mLogDBTypeString.trim(), e.getMessage()));
				}

				this.mLogDS = getProperty("it.govpay.mlog.db.ds", this.props, true, log);

				String mLogSqlString = getProperty("it.govpay.mlog.showSql", this.props, false, log);
				if(mLogSqlString != null)
					this.mLogOnLog4j = Boolean.valueOf(mLogSqlString);
			}

			String pddAuthEnableString = getProperty("it.govpay.pdd.auth", this.props, false, log);
			if(pddAuthEnableString != null && pddAuthEnableString.equalsIgnoreCase("false"))
				this.pddAuthEnable = false;

			String listaHandlers = getProperty("it.govpay.integration.client.out", this.props, false, log);

			this.outHandlers = new ArrayList<>();

			if(listaHandlers != null && !listaHandlers.isEmpty()) {
				String[] splitHandlers = listaHandlers.split(",");
				for(String handler: splitHandlers) {
					String handlerClass = getProperty("it.govpay.integration.client.out."+handler, this.props, true, log);
					Class<?> c = null;
					try {
						c = this.getClass().getClassLoader().loadClass(handlerClass);
					} catch (ClassNotFoundException e) {
						throw new ConfigException(MessageFormat.format("La classe [{0}] specificata per l''handler [{1}] non e'' presente nel classpath", handlerClass, handler));
					}
					Object instance;
					try {
						instance = c.getDeclaredConstructor().newInstance();
						if(!(instance instanceof IntegrationOutHandler)) {
							throw new ConfigException(MessageFormat.format("La classe [{0}] specificata per l''handler [{1}] deve implementare l''interfaccia {2}", handlerClass, handler, IntegrationOutHandler.class.getName()));
						}
						this.outHandlers.add(handlerClass);
					} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
						log.error(MessageFormat.format("Errore durante la crazione dell''oggetto di classe [{0}] specificata per l''handler [{1}]: {2}", handlerClass, handler, e.getMessage()));
						throw new ConfigException(e);
					}
				}
			}

			String batchOnString = getProperty("it.govpay.batchOn", this.props, false, log);
			if(batchOnString != null && batchOnString.equalsIgnoreCase("false"))
				this.batchOn = false;
			
			log.info("Abilitazione generale Batch: {}", this.batchOn);

			String clusterIdString = getProperty("it.govpay.clusterId", this.props, false, log);
			if(clusterIdString != null) {
				this.clusterId = clusterIdString.trim();
			}

			String timeoutBatchString = getProperty("it.govpay.timeoutBatch", this.props, false, log);
			try{
				this.timeoutBatch = Long.parseLong(timeoutBatchString) * 1000;
			} catch(Throwable t) {
				log.info("Proprieta \"it.govpay.timeoutBatch\" impostata con valore di default (5 minuti)");
				this.timeoutBatch = (long) 5 * 60 * 1000;
			}
			
			String batchCaricamentoTracciatiString = getProperty("it.govpay.batch.caricamentoTracciati.enabled", this.props, false, log);
			if(batchCaricamentoTracciatiString != null && Boolean.valueOf(batchCaricamentoTracciatiString))
				this.batchCaricamentoTracciati = true;
			
			String timeoutPendentiString = getProperty("it.govpay.modello3.timeoutPagamento", props, false, log);
			if(timeoutPendentiString != null && !timeoutPendentiString.equalsIgnoreCase("false")) {
				try{
					this.timeoutPendentiModello3Mins = Integer.parseInt(timeoutPendentiString);
					this.timeoutPendentiModello3 = true;
				} catch(NumberFormatException nfe) {
					log.warn("La proprieta \"it.govpay.modello3.timeoutPagamento\" deve essere valorizzata a `false` o con un numero. Utilizzato valore di default `false`");
				}
			}
			
			String timeoutPendentiModello1String = getProperty("it.govpay.modello1.timeoutPagamento", props, false, log);
			if(timeoutPendentiModello1String != null && !timeoutPendentiModello1String.equalsIgnoreCase("false")) {
				try{
					this.timeoutPendentiModello1Mins = Integer.parseInt(timeoutPendentiModello1String);
					this.timeoutPendentiModello1 = true;
				} catch(NumberFormatException nfe) {
					log.warn("La proprieta \"it.govpay.modello1.timeoutPagamento\" deve essere valorizzata a `false` o con un numero. Utilizzato valore di default `false`");
				}
			}
			
			String timeoutPendentiModello3_SANP_24_String = getProperty("it.govpay.modello3.sanp24.timeoutPagamento", props, false, log);
			if(timeoutPendentiModello3_SANP_24_String != null) {
				try{
					this.timeoutPendentiModello3_SANP_24_Mins = Integer.parseInt(timeoutPendentiModello3_SANP_24_String);
					
					if(this.timeoutPendentiModello3_SANP_24_Mins.intValue() > 30) {
						this.timeoutPendentiModello3_SANP_24_Mins = 30;
						log.warn("La proprieta \"it.govpay.modello3.sanp24.timeoutPagamento\" deve essere valorizzata con un numero non superiore a 30. Utilizzato valore di default: 30");
					}
					
					if(this.timeoutPendentiModello3_SANP_24_Mins.intValue() < 1) {
						this.timeoutPendentiModello3_SANP_24_Mins = 1;
						log.warn("La proprieta \"it.govpay.modello3.sanp24.timeoutPagamento\" deve essere valorizzata con un numero non inferiore a 1. Utilizzato valore di default: 30");
					}
				} catch(NumberFormatException nfe) {
					log.warn("La proprieta \"it.govpay.modello3.sanp24.timeoutPagamento\" deve essere valorizzata con un numero. Utilizzato valore di default: 30");
				}
			}
			
			this.appName = getProperty("it.govpay.backoffice.gui.appName", this.props, false, log);
			this.ambienteDeploy = getProperty("it.govpay.backoffice.gui.ambienteDeploy", this.props, false, log);
			this.autenticazioneSPIDNomeHeaderPrincipal = getProperty("it.govpay.autenticazioneSPID.nomeHeaderPrincipal", this.props, false, log);
			this.autenticazioneSPIDElencoHeadersRequest = getProperties("it.govpay.autenticazioneSPID.headers.",this.props, false, log);
			
			
			
			String nomiHeadersListS = getProperty("it.govpay.autenticazioneHeader.nomeHeaderPrincipal", props, false, log);
			if(StringUtils.isNotEmpty(nomiHeadersListS)) {
				String[] split = nomiHeadersListS.split(",");
				if(split != null && split.length > 0) {
					this.autenticazioneHeaderNomeHeaderPrincipal = Arrays.asList(split);
				}
			}
			//this.autenticazioneHeaderNomeHeaderPrincipal = getProperty("it.govpay.autenticazioneHeader.nomeHeaderPrincipal", this.props, false, log);
			
			String headersListS = getProperty("it.govpay.autenticazioneHeader.nomiHeadersInfo", props, false, log);
			if(StringUtils.isNotEmpty(headersListS)) {
				String[] split = headersListS.split(",");
				if(split != null && split.length > 0) {
					this.autenticazioneHeaderElencoHeadersRequest = Arrays.asList(split);
				}
			}
			
			Map<String, String> propertiesSH = getProperties("it.govpay.autenticazioneSSLHeader.",this.props, false, log);
			this.autenticazioneSSLHeaderProperties.putAll(propertiesSH);
			
			String checkCFDebitoreString = getProperty("it.govpay.autenticazione.utenzaAnonima.checkCfDebitore.enabled", props, false, log);
			if(StringUtils.isNotEmpty(checkCFDebitoreString) && !checkCFDebitoreString.equalsIgnoreCase("false")) {
				this.checkCfDebitore = true;
			}
			
			String numeroMassimoEntriesProspettoRiscossioneString = getProperty("it.govpay.reportistica.prospettoRiscossione.numeroMassimoEntries", props, false, log);
			if(StringUtils.isNotEmpty(numeroMassimoEntriesProspettoRiscossioneString)) {
				this.numeroMassimoEntriesProspettoRiscossione = Integer.parseInt(numeroMassimoEntriesProspettoRiscossioneString);;
			}
			
			String intervalloControlloRptPendentiString = getProperty("it.govpay.recuperoRptPendenti.intervalloControlloCreazioneRpt", props, false, log);
			try {
				this.intervalloControlloRptPendenti = Integer.parseInt(intervalloControlloRptPendentiString);
			} catch(Throwable t) {
				log.info("Proprieta \"it.govpay.recuperoRptPendenti.intervalloControlloCreazioneRpt\" impostata con valore di default (100000)");
				this.intervalloControlloRptPendenti = 100000;
			}
			
			String intervalloDisponibilitaPagamentoUtenzaAnonimaString = getProperty("it.govpay.autenticazione.utenzaAnonima.intervalloDisponibilitaPagamento", props, false, log);
			try {
				this.intervalloDisponibilitaPagamentoUtenzaAnonima = Integer.parseInt(intervalloDisponibilitaPagamentoUtenzaAnonimaString);
			} catch(Throwable t) {
				log.info("Proprieta \"it.govpay.autenticazione.utenzaAnonima.intervalloDisponibilitaPagamento\" impostata con valore di default (60) minuti");
				this.intervalloDisponibilitaPagamentoUtenzaAnonima = 60;
			}
			
			this.codTipoVersamentoPendenzeLibere = getProperty("it.govpay.versamenti.codTipoVersamentoPerPagamentiLiberi", this.props, true, log);
			this.codTipoVersamentoPendenzeNonCensite = getProperty("it.govpay.versamenti.codTipoVersamentoPerTipiPendenzeNonCensiti", this.props, true, log);
			
			String censimentoTipiVersamentoSconosciutiEnabledString = getProperty("it.govpay.versamenti.censimentoAutomaticoTipiPendenza.enabled", this.props, false, log);
			if(censimentoTipiVersamentoSconosciutiEnabledString != null)
				this.censimentoTipiVersamentoSconosciutiEnabled = Boolean.valueOf(censimentoTipiVersamentoSconosciutiEnabledString);
			
			Map<String, String> properties = getProperties("it.govpay.configurazioneFiltroCors.",this.props, false, log);
			this.corsProperties.putAll(properties);
			
			
			String scritturaDiagnosticiFileEnabledString = getProperty("it.govpay.context.savataggioDiagnosticiSuFile.enabled", this.props, false, log);
			if(scritturaDiagnosticiFileEnabledString != null && Boolean.valueOf(scritturaDiagnosticiFileEnabledString))
				this.scritturaDiagnosticiFileEnabled = true;
			
			String scritturaDumpFileEnabledString = getProperty("it.govpay.context.savataggioDumpSuFile.enabled", this.props, false, log);
			if(scritturaDumpFileEnabledString != null && Boolean.valueOf(scritturaDumpFileEnabledString))
				this.scritturaDumpFileEnabled = true;
			
			String giornaleEventiEnabledString = getProperty("it.govpay.context.giornaleEventi.enabled", this.props, false, log);
			if(giornaleEventiEnabledString != null && Boolean.valueOf(giornaleEventiEnabledString))
				this.giornaleEventiEnabled = true;
			
			String defaultCustomIuvGeneratorClass = getProperty("it.govpay.defaultCustomIuvGenerator.class", this.props, false, log);
			if(defaultCustomIuvGeneratorClass != null && !defaultCustomIuvGeneratorClass.isEmpty()) {
				Class<?> c = null;
				try {
					c = this.getClass().getClassLoader().loadClass(defaultCustomIuvGeneratorClass);
				} catch (ClassNotFoundException e) {
					throw new ConfigException(MessageFormat.format("La classe [{0}] specificata per la gestione di IUV non e'' presente nel classpath", defaultCustomIuvGeneratorClass));
				}
				Object instance;
				try {
					instance = c.getDeclaredConstructor().newInstance();
					if(!(instance instanceof CustomIuv)) {
						throw new ConfigException(MessageFormat.format("La classe [{0}] specificata per la gestione di IUV deve estendere la classe {1}", defaultCustomIuvGeneratorClass, CustomIuv.class.getName()));
					}
					this.defaultCustomIuvGenerator = (CustomIuv) instance;
				} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
					log.error(MessageFormat.format("Errore durante la crazione dell''oggetto di classe [{0}] specificata per la gestione di IUV: {1}", defaultCustomIuvGeneratorClass, e.getMessage()));
					throw new ConfigException(e);
				}
				
			} else {
				this.defaultCustomIuvGenerator = new CustomIuv();
			}
			
			this.templateProspettoRiscossioni = getProperty("it.govpay.reportistica.prospettoRiscossione.templateJasper", this.props, false, log);
			
			String numeroVersamentiPerThreadString = getProperty("it.govpay.batch.caricamentoTracciati.numeroVersamentiPerThread", this.props, false, log);
			try{
				this.batchCaricamentoTracciatiNumeroVersamentiDaCaricarePerThread = Integer.parseInt(numeroVersamentiPerThreadString);
			} catch(Throwable t) {
				log.info("Proprieta \"it.govpay.batch.caricamentoTracciati.numeroVersamentiPerThread\" impostata con valore di default 100");
				this.batchCaricamentoTracciatiNumeroVersamentiDaCaricarePerThread = 100;
			}
			
			String numeroStampePerThreadString = getProperty("it.govpay.batch.caricamentoTracciati.numeroAvvisiDaStamparePerThread", this.props, false, log);
			try{
				this.batchCaricamentoTracciatiNumeroAvvisiDaStamparePerThread = Integer.parseInt(numeroStampePerThreadString);
			} catch(Throwable t) {
				log.info("Proprieta \"it.govpay.batch.caricamentoTracciati.numeroAvvisiDaStamparePerThread\" impostata con valore di default 100");
				this.batchCaricamentoTracciatiNumeroAvvisiDaStamparePerThread = 100;
			}
			
			// Mapping URL-ID -> Url abilitate nel sistema
			Map<String, String> redirectURLs = getProperties("it.govpay.login-redirect.",this.props, false, log);
			this.apiUserLoginRedirectURLs.putAll(redirectURLs);
			
			Map<String, String> logoutRedirectURLs = getProperties("it.govpay.logout-redirect.",this.props, false, log);
			this.apiUserLogoutRedirectURLs.putAll(logoutRedirectURLs);
			
			String dimensioneMassimaListaRisultatiString = getProperty("it.govpay.api.find.maxRisultatiPerPagina", this.props, false, log);
			try{
				this.dimensioneMassimaListaRisultati = Integer.parseInt(dimensioneMassimaListaRisultatiString);
			} catch(Throwable t) {
				log.info(MessageFormat.format("Proprieta \"it.govpay.api.find.maxRisultatiPerPagina\" impostata con valore di default {0}",	BasicFindRequestDTO.DEFAULT_MAX_LIMIT));
				this.dimensioneMassimaListaRisultati = BasicFindRequestDTO.DEFAULT_MAX_LIMIT;
			}
			
			String batchCaricamentoTracciatiNotificaPagamentiString = getProperty("it.govpay.batch.caricamentoTracciatiNotificaPagamenti.enabled", this.props, false, log);
			if(batchCaricamentoTracciatiNotificaPagamentiString != null && Boolean.valueOf(batchCaricamentoTracciatiNotificaPagamentiString))
				this.batchCaricamentoTracciatiNotificaPagamenti = true;
			
			String batchCaricamentoTracciatiNotificaPagamentiDimensionePaginaString = getProperty("it.govpay.batch.caricamentoTracciatiNotificaPagamenti.dimensionePagina", this.props, false, log);
			try{
				this.batchCaricamentoTracciatiNotificaPagamentiDimensionePagina = Integer.parseInt(batchCaricamentoTracciatiNotificaPagamentiDimensionePaginaString);
			} catch(Throwable t) {
				log.info(MessageFormat.format("Proprieta \"it.govpay.batch.caricamentoTracciatiNotificaPagamenti.dimensionePagina\" impostata con valore di default {0}", BasicFindRequestDTO.DEFAULT_MAX_LIMIT));
				this.batchCaricamentoTracciatiNotificaPagamentiDimensionePagina = BasicFindRequestDTO.DEFAULT_MAX_LIMIT;
			}
			
			String ricercaRiconciliazioniIdFlussoCaseInsensitiveString = getProperty("it.govpay.riconciliazione.idFlussoCaseInsensitive.enabled", this.props, false, log);
			if(ricercaRiconciliazioniIdFlussoCaseInsensitiveString != null && Boolean.valueOf(ricercaRiconciliazioniIdFlussoCaseInsensitiveString))
				this.ricercaRiconciliazioniIdFlussoCaseInsensitive = true;
			
			String connectTimeoutString = getProperty("it.govpay.client.connectionTimeout", this.props, false, log);
			try{
				this.connectionTimeout = Integer.parseInt(connectTimeoutString);
			} catch(Throwable t) {
				log.info("Proprieta \"it.govpay.client.connectionTimeout\" impostata con valore di default 10000");
				this.connectionTimeout = 10000;
			}
			
			String readTimeoutString = getProperty("it.govpay.client.readTimeout", this.props, false, log);
			try{
				this.readTimeout = Integer.parseInt(readTimeoutString);
			} catch(Throwable t) {
				log.info("Proprieta \"it.govpay.client.readTimeout\" impostata con valore di default 180000");
				this.readTimeout = 180000;
			}
			
			String connectionRequestTimeoutString = getProperty("it.govpay.client.connectionRequestTimeout", this.props, false, log);
			try{
				this.connectionRequestTimeout = Integer.parseInt(connectionRequestTimeoutString);
			} catch(Throwable t) {
				log.info("Proprieta \"it.govpay.client.connectionTimeout\" impostata con valore di default 180000");
				this.connectionRequestTimeout = 10000;
			}
			
			String numeroMassimoConnessioniPerPoolString = getProperty("it.govpay.client.numeroMassimoConnessioniPerPool", this.props, false, log);
			try{
				this.numeroMassimoConnessioniPerPool = Integer.parseInt(numeroMassimoConnessioniPerPoolString);
			} catch(Throwable t) {
				log.info("Proprieta \"it.govpay.client.numeroMassimoConnessioniPerPool\" impostata con valore di default 200");
				this.numeroMassimoConnessioniPerPool = 200;
			}
			
			String numeroMassimoConnessioniPerRouteDefaultString = getProperty("it.govpay.client.numeroMassimoConnessioniPerRouteDefault", this.props, false, log);
			try{
				this.numeroMassimoConnessioniPerPool = Integer.parseInt(numeroMassimoConnessioniPerRouteDefaultString);
			} catch(Throwable t) {
				log.info("Proprieta \"it.govpay.client.numeroMassimoConnessioniPerRouteDefault\" impostata con valore di default 20");
				this.numeroMassimoConnessioniPerRouteDefault = 20;
			}
			
			String aggiornamentoValiditaMandatorioString = getProperty("it.govpay.context.aggiornamentoValiditaMandatorio", this.props, false, log);
			if(aggiornamentoValiditaMandatorioString != null && Boolean.valueOf(aggiornamentoValiditaMandatorioString))
				this.aggiornamentoValiditaMandatorio = true;
			
			String timeoutInvioRPTModello3MillisString = getProperty("it.govpay.modello3.timeoutInvioRPT", this.props, false, log);
			try{
				this.timeoutInvioRPTModello3Millis = Integer.parseInt(timeoutInvioRPTModello3MillisString);
				
				if(this.timeoutInvioRPTModello3Millis < 0 || this.timeoutInvioRPTModello3Millis > 1000) {
					log.info(MessageFormat.format("Proprieta \"it.govpay.modello3.timeoutInvioRPT\" trovata con valore non valido [{0}], viene impostata con valore di default 100 ms",	this.timeoutInvioRPTModello3Millis));
					this.timeoutInvioRPTModello3Millis = 100;
				}
				
			} catch(Throwable t) {
				log.info("Proprieta \"it.govpay.modello3.timeoutInvioRPT\" impostata con valore di default 100 ms");
				this.timeoutInvioRPTModello3Millis = 100;
			}
			
			
			String numeroMassimoGiorniRPTPendentiString = getProperty("it.govpay.batch.recuperoRptPendenti.limiteTemporaleRecupero", this.props, false, log);
			try{
				this.numeroMassimoGiorniRPTPendenti = Integer.parseInt(numeroMassimoGiorniRPTPendentiString);
			} catch(Throwable t) {
				log.info("Proprieta \"it.govpay.batch.recuperoRptPendenti.limiteTemporaleRecupero\" impostata con valore di default 30");
				this.numeroMassimoGiorniRPTPendenti = 30;
			}
			
			
			this.templateQuietanzaPagamento = getProperty("it.govpay.reportistica.quietanzaPagamento.templateJasper", this.props, false, log);
			
			this.checkoutBaseURL = getProperty("it.govpay.checkout.baseUrl", this.props, true, log);
			
			String checkoutEnabledString = getProperty("it.govpay.checkout.enabled", this.props, false, log);
			if(checkoutEnabledString != null && Boolean.valueOf(checkoutEnabledString))
				this.checkoutEnabled = true;

			String conversioneMessaggiPagoPAV2NelFormatoV1String = getProperty("it.govpay.retrocompatibilitaMessaggiPagoPA.v1.enable", this.props, false, log);
			if(conversioneMessaggiPagoPAV2NelFormatoV1String != null && Boolean.valueOf(conversioneMessaggiPagoPAV2NelFormatoV1String))
				this.conversioneMessaggiPagoPAV2NelFormatoV1 = true;
			
			this.nomeHeaderSubscriptionKeyPagoPA = getProperty("it.govpay.client.pagopa.autenticazione.subscriptionkey.header.name", this.props, false, log);
			
			this.autenticazioneApiKeyNomeHeaderApiId = getProperty("it.govpay.autenticazioneApiKey.apiId.header.name", this.props, false, log);
			this.autenticazioneApiKeyNomeHeaderApiKey = getProperty("it.govpay.autenticazioneApiKey.apiKey.header.name", this.props, false, log);
			
			this.autenticazioneApiKeyNomeHeaderApiIdFruizione = getProperty("it.govpay.client.autenticazioneApiKey.apiId.header.name", this.props, false, log);
			this.autenticazioneApiKeyNomeHeaderApiKeyFruizione = getProperty("it.govpay.client.autenticazioneApiKey.apiKey.header.name", this.props, false, log);
			
			String dismettiIUVIso11694String = getProperty("it.govpay.dismettiIuvIso11694", this.props, false, log);
			if(dismettiIUVIso11694String != null && Boolean.valueOf(dismettiIUVIso11694String))
				this.dismettiIUVIso11694 = true;
			
			this.operazioneVerifica = getProperty("it.govpay.api.ente.verificaPendenza.operazione", this.props, false, log);
			
			if(this.operazioneVerifica != null) {
				if(! (Costanti.VERIFICA_PENDENZE_GET_AVVISO_OPERATION_ID.equals(this.operazioneVerifica) ||
						Costanti.VERIFICA_PENDENZE_VERIFY_PENDENZA_OPERATION_ID.equals(this.operazioneVerifica))) {
					log.info(MessageFormat.format("Proprieta \"it.govpay.api.ente.verificaPendenza.operazione\" trovata con valore non valido [{0}], viene impostata con valore di default null",	this.operazioneVerifica));
					this.operazioneVerifica = null;
				}
			}
			
			String numeroGiorniValiditaPendenzaString = getProperty("it.govpay.modello3.sanp24.giorniValiditaDaAssegnarePendenzaSenzaDataValidita", this.props, false, log);
			if(numeroGiorniValiditaPendenzaString != null) {
				try{
					this.numeroGiorniValiditaPendenza = Integer.parseInt(numeroGiorniValiditaPendenzaString);
					
					if(this.numeroGiorniValiditaPendenza < 1) {
						log.info("Proprieta \"it.govpay.modello3.sanp24.giorniValiditaDaAssegnarePendenzaSenzaDataValidita\" trovata con valore non valido [{}], viene impostata con valore di default null",	this.operazioneVerifica);
						this.numeroGiorniValiditaPendenza = null;
					}
				} catch(NumberFormatException t) {
					log.info("Proprieta \"it.govpay.modello3.sanp24.giorniValiditaDaAssegnarePendenzaSenzaDataValidita\" trovata con valore non valido [{}], viene impostata con valore di default null",	this.operazioneVerifica);
					this.numeroGiorniValiditaPendenza = null;
				}
			}
			
			String batchRecuperoRPTPendentiString = getProperty("it.govpay.batch.recuperoRptPendenti.enabled", this.props, false, log);
			if(batchRecuperoRPTPendentiString != null && Boolean.valueOf(batchRecuperoRPTPendentiString))
				this.batchRecuperoRPTPendenti = true;
			
			String batchAcquisizioneRendicontazioniString = getProperty("it.govpay.batch.acquisizioneRendicontazioni.enabled", this.props, false, log);
			if(batchAcquisizioneRendicontazioniString != null && Boolean.valueOf(batchAcquisizioneRendicontazioniString))
				this.batchAcquisizioneRendicontazioni = true;
			
			String batchChiusuraRPTScaduteString = getProperty("it.govpay.batch.chiusuraRptScadute.enabled", this.props, false, log);
			if(batchChiusuraRPTScaduteString != null && Boolean.valueOf(batchChiusuraRPTScaduteString))
				this.batchChiusuraRPTScadute = true;
			
			String batchElaborazioneRiconciliazioniString = getProperty("it.govpay.batch.elaborazioneRiconciliazioni.enabled", this.props, false, log);
			if(batchElaborazioneRiconciliazioniString != null && Boolean.valueOf(batchElaborazioneRiconciliazioniString))
				this.batchElaborazioneRiconciliazioni = true;
			
			String batchGestionePromemoriaString = getProperty("it.govpay.batch.gestionePromemoria.enabled", this.props, false, log);
			if(batchGestionePromemoriaString != null && Boolean.valueOf(batchGestionePromemoriaString))
				this.batchGestionePromemoria = true;
			
			String batchSpedizioneNotificheString = getProperty("it.govpay.batch.spedizioneNotifiche.enabled", this.props, false, log);
			if(batchSpedizioneNotificheString != null && Boolean.valueOf(batchSpedizioneNotificheString))
				this.batchSpedizioneNotifiche = true;
			
			String batchSpedizioneNotificheAppIOString = getProperty("it.govpay.batch.spedizioneNotificheAppIO.enabled", this.props, false, log);
			if(batchSpedizioneNotificheAppIOString != null && Boolean.valueOf(batchSpedizioneNotificheAppIOString))
				this.batchSpedizioneNotificheAppIO = true;
			
			String batchSpedizionePromemoriaString = getProperty("it.govpay.batch.spedizionePromemoria.enabled", this.props, false, log);
			if(batchSpedizionePromemoriaString != null && Boolean.valueOf(batchSpedizionePromemoriaString))
				this.batchSpedizionePromemoria = true;
			
			String keywordsS = getProperty("it.govpay.stampe.avvisoPagamento.identificativoDebitore.nascondiKeyword", props, false, log);
			if(StringUtils.isNotEmpty(keywordsS)) {
				String[] split = keywordsS.split(",");
				if(split != null && split.length > 0) {
					this.keywordsDaSostituireIdentificativiDebitoreAvviso = Arrays.asList(split);
				}
			}
			
			String controlloPasswordBackwardCompatibilityMD5String = getProperty("it.govpay.autenticazione.controlloPassword.backwardCompatibilityMD5.enabled", this.props, false, log);
			if(controlloPasswordBackwardCompatibilityMD5String != null && Boolean.valueOf(controlloPasswordBackwardCompatibilityMD5String))
				this.controlloPasswordBackwardCompatibilityMD5 = true;
			
		} catch (PropertyNotFoundException e) {
			log.error(MessageFormat.format("Errore di inizializzazione: {0}", e.getMessage()));
			throw new ConfigException(e);
		} catch (InvalidPropertyException e) {
			log.error(MessageFormat.format("Errore di inizializzazione: {0}", e.getMessage()));
			throw new ConfigException(e);
		}
	}
	
	public synchronized void leggiFileEsternoLog4j2() {
		// Recupero la configurazione della working dir Se e' configurata, la uso come prioritaria
		try {
			if(this.resourceDir != null) {
				File resourceDirFile = new File(escape(this.resourceDir));
				if(!resourceDirFile.isDirectory())
					throw new Exception(MessageFormat.format("Il path indicato nella property \"it.govpay.resource.path\" ({0}) non esiste o non e'' un folder.", this.resourceDir));

				File log4j2ConfigFile = new File(this.resourceDir + File.separatorChar + LOG4J2_XML_FILE_NAME);

				if(log4j2ConfigFile.exists()) {
					this.log4j2Config = log4j2ConfigFile.toURI();
					LoggerWrapperFactory.getLogger(GovpayConfig.class).info(MessageFormat.format("Individuata configurazione log4j: {0}", this.log4j2Config));
				} else {
					this.log4j2Config = null;
					LoggerWrapperFactory.getLogger(GovpayConfig.class).info("Individuata configurazione log4j interna.");
				}
			}
		} catch (Exception e) {
			LoggerWrapperFactory.getLogger(GovpayConfig.class).warn(MessageFormat.format("Errore di inizializzazione: {0}. Property ignorata.", e.getMessage()));
		}
	}
	
	private static Map<String,String> getProperties(String baseName, Properties[] props, boolean required, Logger log) throws PropertyNotFoundException {
		Map<String, String> valori = new HashMap<>();
		
		List<String> nomiProperties = new ArrayList<String>();
		// 1. collezionare tutti i nomi di properties da leggere (possono essere definiti in piu' file)
		for(int i=0; i<props.length; i++) {
			if(props[i] != null) {
				for (Object nameObj : props[i].keySet()) {
					String name = (String) nameObj;
					if(name.startsWith(baseName) && !nomiProperties.contains(name)) {
						nomiProperties.add(name);
					}
				}
			}
		}
		
		// 2. leggere la property singola
		for (String nomeProprieta : nomiProperties) {
			String valoreProprieta = getProperty(nomeProprieta, props, required, log);
			
			if (valoreProprieta != null) {
				String key = nomeProprieta.substring(baseName.length());
				valori.put(key, valoreProprieta);
			}
		}
		
		return valori;
	}

	private static String getProperty(String name, Properties props, boolean required, boolean fromInternalConfig, Logger log) throws PropertyNotFoundException {
		String value = System.getProperty(name);

		if(value != null && value.trim().isEmpty()) {
			value = null;
		}
		String logString = "";
		if(fromInternalConfig) logString = "da file interno ";
		else logString = "da file esterno ";

		if(value == null) {
			if(props != null) {
				value = props.getProperty(name);
				if(value != null && value.trim().isEmpty()) {
					value = null;
				}
			}
			if(value == null) {
				if(required) 
					throw new PropertyNotFoundException(MessageFormat.format("Proprieta [{0}] non trovata", name));
				else return null;
			} else {
				if(log != null) log.info(MessageFormat.format("Letta proprieta di configurazione {0}{1}: {2}", logString, name, value));
			}
		} else {
			if(log != null) log.info(MessageFormat.format("Letta proprieta di sistema {0}: {1}", name, value));
		}

		return value.trim();
	}

	private static String getProperty(String name, Properties[] props, boolean required, Logger log) throws PropertyNotFoundException {
		String value = null;
		for(int i=0; i<props.length; i++) {
			try { value = getProperty(name, props[i], required, i==1, log); } catch (PropertyNotFoundException e) { }
			if(value != null && !value.trim().isEmpty()) {
				return value;
			}
		}

		if(log!= null) log.info(MessageFormat.format("Proprieta {0} non trovata", name));

		if(required) 
			throw new PropertyNotFoundException(MessageFormat.format("Proprieta [{0}] non trovata", name));
		else 
			return null;
	}
	
	public static String escape(String string) {
		return string.replaceAll("\\\\", "\\\\\\\\");
	}

	public VersioneAvviso getVersioneAvviso() {
		return this.versioneAvviso;
	}

	public URI getLog4j2Config() {
		return this.log4j2Config;
	}

	public int getDimensionePoolNotifica() {
		return this.dimensionePoolThreadNotifica;
	}
	
	public int getDimensionePoolNotificaAppIO() {
		return this.dimensionePoolThreadNotificaAppIo;
	}
	
	public int getDimensionePoolRPT() {
		return this.dimensionePoolThreadRPT;
	}

	public int getDimensionePoolCaricamentoTracciati() {
		return dimensionePoolThreadCaricamentoTracciati;
	}
	
	public int getDimensionePoolCaricamentoTracciatiStampaAvvisi() {
		return dimensionePoolThreadCaricamentoTracciatiStampaAvvisi;
	}
	
	public int getDimensionePoolThreadSpedizioneTracciatiNotificaPagamenti() {
		return dimensionePoolThreadSpedizioneTracciatiNotificaPagamenti;
	}
	
	public int getDimensionePoolThreadSpedizioneNotificaPagamentoMaggioli() {
		return dimensionePoolThreadSpedizioneNotificaPagamentoMaggioli;
	}

	public String getKsLocation() {
		return this.ksLocation;
	}

	public String getKsPassword() {
		return this.ksPassword;
	}

	public String getKsAlias() {
		return this.ksAlias;
	}

	public String getmLogClass() {
		return this.mLogClass;
	}

	public String getmLogDS() {
		return this.mLogDS;
	}

	public Severity getmLogLevel() {
		return this.mLogLevel;
	}

	public TipiDatabase getmLogDBType() {
		return this.mLogDBType;
	}

	public boolean ismLogOnLog4j() {
		return this.mLogOnLog4j;
	}

	public boolean ismLogOnDB() {
		return this.mLogOnDB;
	}

	public boolean ismLogSql() {
		return this.mLogSql;
	}

	public boolean isPddAuthEnable() {
		return this.pddAuthEnable;
	}

	public List<String> getOutHandlers() {
		return this.outHandlers;
	}

	public String getResourceDir() {
		return this.resourceDir;
	}

	public boolean isBatchOn() {
		return this.batchOn;
	}

	public String getClusterId(){
		return this.clusterId;
	}

	public long getTimeoutBatch(){
		return this.timeoutBatch;
	}

	public Integer getCacheLogo() {
		return 2 * 60 * 60;
	}

	public boolean isBatchCaricamentoTracciati() {
		return batchCaricamentoTracciati;
	}
	
	public boolean isTimeoutPendentiModello3() {
		return timeoutPendentiModello3;
	}

	public Integer getTimeoutPendentiModello3Mins() {
		return timeoutPendentiModello3Mins;
	}
	
	public boolean isTimeoutPendentiModello1() {
		return timeoutPendentiModello1;
	}

	public Integer getTimeoutPendentiModello1Mins() {
		return timeoutPendentiModello1Mins;
	}
	
	public Integer getTimeoutPendentiModello3_SANP_24_Mins() {
		return timeoutPendentiModello3_SANP_24_Mins;
	}

	public String getAppName() {
		return appName;
	}

	public String getAmbienteDeploy() {
		return ambienteDeploy;
	}

	public String getAutenticazioneSPIDNomeHeaderPrincipal() {
		return autenticazioneSPIDNomeHeaderPrincipal;
	}

	public Map<String,String> getAutenticazioneSPIDElencoHeadersRequest() {
		return autenticazioneSPIDElencoHeadersRequest;
	}

	public List<String> getAutenticazioneHeaderNomeHeaderPrincipal() {
		return autenticazioneHeaderNomeHeaderPrincipal;
	}

	public List<String> getAutenticazioneHeaderElencoHeadersRequest() {
		return autenticazioneHeaderElencoHeadersRequest;
	}

	public boolean isCheckCfDebitore() {
		return checkCfDebitore;
	}

	public int getNumeroMassimoEntriesProspettoRiscossione() {
		return numeroMassimoEntriesProspettoRiscossione;
	}

	public Integer getIntervalloControlloRptPendenti() {
		return intervalloControlloRptPendenti;
	}

	public Integer getIntervalloDisponibilitaPagamentoUtenzaAnonima() {
		return intervalloDisponibilitaPagamentoUtenzaAnonima;
	}

	public String getCodTipoVersamentoPendenzeLibere() {
		return codTipoVersamentoPendenzeLibere;
	}

	public String getCodTipoVersamentoPendenzeNonCensite() {
		return codTipoVersamentoPendenzeNonCensite;
	}

	public Properties getCORSProperties() {
		return corsProperties;
	}

	public boolean isScritturaDiagnosticiFileEnabled() {
		return scritturaDiagnosticiFileEnabled;
	}

	public boolean isScritturaDumpFileEnabled() {
		return scritturaDumpFileEnabled;
	}

	public boolean isGiornaleEventiEnabled() {
		return giornaleEventiEnabled;
	}

	public CustomIuv getDefaultCustomIuvGenerator() {
		return this.defaultCustomIuvGenerator;
	}

	public boolean isCensimentoTipiVersamentoSconosciutiEnabled() {
		return censimentoTipiVersamentoSconosciutiEnabled;
	}

	public String getTemplateProspettoRiscossioni() {
		return templateProspettoRiscossioni;
	}
	
	public String getTemplateQuietanzaPagamento() {
		return templateQuietanzaPagamento;
	}

	public Properties getApiUserLoginRedirectURLs() {
		return apiUserLoginRedirectURLs;
	}
	
	public Properties getApiUserLogoutRedirectURLs() {
		return apiUserLogoutRedirectURLs;
	}

	public Properties getAutenticazioneSSLHeaderProperties() {
		return autenticazioneSSLHeaderProperties;
	}

	public Integer getBatchCaricamentoTracciatiNumeroVersamentiDaCaricarePerThread() {
		return batchCaricamentoTracciatiNumeroVersamentiDaCaricarePerThread;
	}

	public Integer getBatchCaricamentoTracciatiNumeroAvvisiDaStamparePerThread() {
		return batchCaricamentoTracciatiNumeroAvvisiDaStamparePerThread;
	}

	public boolean isAggiornamentoValiditaMandatorio() {
		return aggiornamentoValiditaMandatorio;
	}

	public void setAggiornamentoValiditaMandatorio(boolean aggiornamentoValiditaMandatorio) {
		this.aggiornamentoValiditaMandatorio = aggiornamentoValiditaMandatorio;
	}

	public Integer getDimensioneMassimaListaRisultati() {
		return dimensioneMassimaListaRisultati;
	}

	public boolean isBatchCaricamentoTracciatiNotificaPagamenti() {
		return batchCaricamentoTracciatiNotificaPagamenti;
	}
	
	public Integer getBatchCaricamentoTracciatiNotificaPagamentiDimensionePagina() {
		return batchCaricamentoTracciatiNotificaPagamentiDimensionePagina;
	}
	
	public boolean isRicercaRiconciliazioniIdFlussoCaseInsensitive() {
		return ricercaRiconciliazioniIdFlussoCaseInsensitive;
	}

	public Integer getConnectionTimeout() {
		return connectionTimeout;
	}

	public Integer getReadTimeout() {
		return readTimeout;
	}

	public Integer getConnectionRequestTimeout() {
		return connectionRequestTimeout;
	}

	public Integer getNumeroMassimoConnessioniPerPool() {
		return numeroMassimoConnessioniPerPool;
	}

	public Integer getNumeroMassimoConnessioniPerRouteDefault() {
		return numeroMassimoConnessioniPerRouteDefault;
	}
	
	public Integer getTimeoutInvioRPTModello3Millis() {
		return timeoutInvioRPTModello3Millis;
	}
	
	public Integer getNumeroMassimoGiorniRPTPendenti() {
		return numeroMassimoGiorniRPTPendenti;
	}
	
	public String getCheckoutBaseURL() {
		return checkoutBaseURL;
	}
	
	public boolean isCheckoutEnabled() {
		return checkoutEnabled;
	}
	
	public boolean isConversioneMessaggiPagoPAV2NelFormatoV1() {
		return conversioneMessaggiPagoPAV2NelFormatoV1;
	}
	
	public String getNomeHeaderSubscriptionKeyPagoPA() {
		return nomeHeaderSubscriptionKeyPagoPA;
	}
	
	public boolean isDismettiIUVIso11694() {
		return dismettiIUVIso11694;
	}
	
	public String getOperazioneVerifica() {
		return operazioneVerifica;
	}
	
	public Integer getNumeroGiorniValiditaPendenza() {
		return numeroGiorniValiditaPendenza;
	}

	public String getAutenticazioneApiKeyNomeHeaderApiKey() {
		return autenticazioneApiKeyNomeHeaderApiKey;
	}

	public String getAutenticazioneApiKeyNomeHeaderApiId() {
		return autenticazioneApiKeyNomeHeaderApiId;
	}

	public String getAutenticazioneApiKeyNomeHeaderApiKeyFruizione() {
		return autenticazioneApiKeyNomeHeaderApiKeyFruizione;
	}

	public String getAutenticazioneApiKeyNomeHeaderApiIdFruizione() {
		return autenticazioneApiKeyNomeHeaderApiIdFruizione;
	}

	public boolean isBatchRecuperoRPTPendenti() {
		return batchRecuperoRPTPendenti;
	}

	public boolean isBatchAcquisizioneRendicontazioni() {
		return batchAcquisizioneRendicontazioni;
	}

	public boolean isBatchChiusuraRPTScadute() {
		return batchChiusuraRPTScadute;
	}

	public boolean isBatchElaborazioneRiconciliazioni() {
		return batchElaborazioneRiconciliazioni;
	}

	public boolean isBatchGestionePromemoria() {
		return batchGestionePromemoria;
	}

	public boolean isBatchSpedizioneNotifiche() {
		return batchSpedizioneNotifiche;
	}

	public boolean isBatchSpedizioneNotificheAppIO() {
		return batchSpedizioneNotificheAppIO;
	}

	public boolean isBatchSpedizionePromemoria() {
		return batchSpedizionePromemoria;
	}

	public List<String> getKeywordsDaSostituireIdentificativiDebitoreAvviso() {
		return keywordsDaSostituireIdentificativiDebitoreAvviso;
	}
	
	public boolean isControlloPasswordBackwardCompatibilityMD5() {
		return controlloPasswordBackwardCompatibilityMD5;
	}
}
