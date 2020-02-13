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
package it.govpay.core.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.openspcoop2.utils.LoggerWrapperFactory;
import org.openspcoop2.utils.TipiDatabase;
import org.openspcoop2.utils.logger.constants.Severity;
import org.openspcoop2.utils.logger.log4j.Log4jLoggerWithApplicationContext;
import org.slf4j.Logger;

import it.govpay.bd.pagamento.util.CustomIuv;
import it.govpay.core.business.IConservazione;
import it.govpay.core.utils.client.handler.IntegrationOutHandler;
import it.govpay.model.Versamento;

public class GovpayConfig {

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
	private int dimensionePoolThreadRPT;
	private int dimensionePoolThreadAvvisaturaDigitale;
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
	
	private Properties[] props;
	private IConservazione conservazionePlugin;
	
	private String appName;
	private String ambienteDeploy;
	
	private String autenticazioneSPIDNomeHeaderPrincipal;
	private Map<String,String> autenticazioneSPIDElencoHeadersRequest;
	private boolean checkCfDebitore;
	private String autenticazioneHeaderNomeHeaderPrincipal;
	private List<String> autenticazioneHeaderElencoHeadersRequest;
	
	private int numeroMassimoEntriesProspettoRiscossione;
	
	private boolean avvisaturaDigitaleEnabled;
	private boolean avvisaturaDigitaleSincronaEnabled;
	private boolean avvisaturaDigitaleAsincronaEnabled;
	private Integer sizePaginaNumeroVersamentiAvvisaturaDigitale;
	private Integer limiteNumeroVersamentiAvvisaturaDigitale;
	private String avvisaturaDigitaleModalitaAnnullamentoAvviso;
	
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
	
	// recovery configurazione sul db
	private Properties configurazioniDefault;
	
	private Properties apiUserLoginRedirectURLs;
	
	public GovpayConfig(InputStream is) throws Exception {
		// Default values:
		this.versioneAvviso = VersioneAvviso.v002;
		this.dimensionePoolThreadNotifica = 10;
		this.dimensionePoolThreadAvvisaturaDigitale = 10;
		this.dimensionePoolThreadRPT = 10;
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
		
		this.appName = null;
		this.ambienteDeploy = null;

		this.checkCfDebitore = false;
		
		this.autenticazioneSPIDNomeHeaderPrincipal = null;
		this.autenticazioneSPIDElencoHeadersRequest = new HashMap<>();
		this.autenticazioneHeaderNomeHeaderPrincipal = null;
		this.autenticazioneHeaderElencoHeadersRequest = new ArrayList<>();
		this.numeroMassimoEntriesProspettoRiscossione = 5000;
		
		this.avvisaturaDigitaleEnabled= false;
		this.avvisaturaDigitaleSincronaEnabled = false;
		this.avvisaturaDigitaleAsincronaEnabled = false;
		this.sizePaginaNumeroVersamentiAvvisaturaDigitale = 100;
		this.limiteNumeroVersamentiAvvisaturaDigitale = 100000;
		this.avvisaturaDigitaleModalitaAnnullamentoAvviso = AvvisaturaUtils.AVVISATURA_DIGITALE_MODALITA_ASINCRONA;
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
		
		// recovery configurazione sul db
		this.configurazioniDefault = new Properties();
		this.apiUserLoginRedirectURLs = new Properties();
		
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
						throw new Exception("Il path indicato nella property \"it.govpay.resource.path\" (" + this.resourceDir + ") non esiste o non e' un folder.");

					File log4j2ConfigFile = new File(this.resourceDir + File.separatorChar + LOG4J2_XML_FILE_NAME);

					if(log4j2ConfigFile.exists()) {
						this.log4j2Config = log4j2ConfigFile.toURI();
						LoggerWrapperFactory.getLogger("boot").info("Individuata configurazione log4j: " + this.log4j2Config);
					} else {
						LoggerWrapperFactory.getLogger("boot").info("Individuata configurazione log4j interna.");
					}
				}
			} catch (Exception e) {
				LoggerWrapperFactory.getLogger("boot").warn("Errore di inizializzazione: " + e.getMessage() + ". Property ignorata.");
			}
		} catch (Exception e) {
			throw e;
		}
	}

	public void readProperties() throws Exception {
		Logger log = LoggerWrapperFactory.getLogger("boot");
		try {
			Properties props0 = null;
			this.props[0] = props0;

			File gpConfigFile = new File(this.resourceDir + File.separatorChar + PROPERTIES_FILE_NAME);
			if(gpConfigFile.exists()) {
				props0 = new Properties();
				props0.load(new FileInputStream(gpConfigFile));
				log.info("Individuata configurazione prioritaria: " + gpConfigFile.getAbsolutePath());
				this.props[0] = props0;
			}

			try {
				String versioneAvvisoProperty = getProperty("it.govpay.avviso.versione", this.props, false, log);
				if(versioneAvvisoProperty != null && !versioneAvvisoProperty.trim().isEmpty()) {
					try {
						this.versioneAvviso = VersioneAvviso.valueOf(versioneAvvisoProperty.trim());
					} catch (Exception e) {
						throw new Exception("Valore della property \"it.govpay.avviso.versione\" non consetito [" + versioneAvvisoProperty + "]. Valori ammessi: " + Arrays.toString(VersioneAvviso.values()));
					}
				}
			} catch (Exception e) {
				log.warn("Errore di inizializzazione: " + e.getMessage() + ". Assunto valore di default: " + VersioneAvviso.v002);
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
				log.warn("Errore di inizializzazione: " + e.getMessage() + ". Assunto valore di default: " + 10);
				this.dimensionePoolThreadNotifica = 10;
			}
			
			try {
				String dimensionePoolProperty = getProperty("it.govpay.thread.pool.avvisaturaDigitale", this.props, false, log);
				if(dimensionePoolProperty != null && !dimensionePoolProperty.trim().isEmpty()) {
					try {
						this.dimensionePoolThreadAvvisaturaDigitale = Integer.parseInt(dimensionePoolProperty.trim());
					} catch (Exception e) {
						throw new Exception("Valore della property \"it.govpay.thread.pool.avvisaturaDigitale\" non e' un numero intero");
					}
				}
			} catch (Exception e) {
				log.warn("Errore di inizializzazione: " + e.getMessage() + ". Assunto valore di default: " + 10);
				this.dimensionePoolThreadAvvisaturaDigitale = 10;
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
				log.warn("Errore di inizializzazione: " + e.getMessage() + ". Assunto valore di default: " + 10);
				this.dimensionePoolThreadRPT = 10;
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
				log.warn("Valore ["+mLogOnLevelString+"] non consentito per la property \"it.govpay.mlog.level\". Assunto valore di default \"INFO\".");
			}

			String mLogOnDBString = getProperty("it.govpay.mlog.db", this.props, false, log);
			if(mLogOnDBString != null && Boolean.valueOf(mLogOnDBString))
				this.mLogOnDB = true;

			if(this.mLogOnDB) {
				String mLogDBTypeString = getProperty("it.govpay.mlog.db.type", this.props, true, log);
				try {
					this.mLogDBType = TipiDatabase.valueOf(mLogDBTypeString);
				} catch (IllegalArgumentException e) {
					throw new Exception("Valore ["+mLogDBTypeString.trim()+"] non consentito per la property \"it.govpay.mlog.db.type\": " +e.getMessage());
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
						throw new Exception("La classe ["+handlerClass+"] specificata per l'handler ["+handler+"] non e' presente nel classpath");
					}
					Object instance = c.newInstance();
					if(!(instance instanceof IntegrationOutHandler)) {
						throw new Exception("La classe ["+handlerClass+"] specificata per l'handler ["+handler+"] deve implementare l'interfaccia " + IntegrationOutHandler.class.getName());
					}
					this.outHandlers.add(handlerClass);
				}
			}

			String batchOnString = getProperty("it.govpay.batchOn", this.props, false, log);
			if(batchOnString != null && batchOnString.equalsIgnoreCase("false"))
				this.batchOn = false;

			String clusterIdString = getProperty("it.govpay.clusterId", this.props, false, log);
			if(clusterIdString != null) {
				this.clusterId = clusterIdString.trim();
			}

			String timeoutBatchString = getProperty("it.govpay.timeoutBatch", this.props, false, log);
			try{
				this.timeoutBatch = Integer.parseInt(timeoutBatchString) * 1000;
			} catch(Throwable t) {
				log.info("Proprieta \"it.govpay.timeoutBatch\" impostata com valore di default (5 minuti)");
				this.timeoutBatch = 5 * 60 * 1000;
			}
			
			String conservazionePluginString = getProperty("it.govpay.plugin.conservazione", this.props, false, log);
			
			if(conservazionePluginString != null && !conservazionePluginString.isEmpty()) {
				Class<?> c = null;
				try {
					c = this.getClass().getClassLoader().loadClass(conservazionePluginString);
				} catch (ClassNotFoundException e) {
					throw new Exception("La classe ["+conservazionePluginString+"] specificata per plugin di conservazione non e' presente nel classpath");
				}
				Object instance = c.newInstance();
				if(!(instance instanceof IConservazione)) {
					throw new Exception("La classe ["+conservazionePluginString+"] specificata per plugin di conservazione deve implementare l'interfaccia " + IConservazione.class.getName());
				}
				this.conservazionePlugin = (IConservazione) instance;
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
			
			this.appName = getProperty("it.govpay.backoffice.gui.appName", this.props, false, log);
			this.ambienteDeploy = getProperty("it.govpay.backoffice.gui.ambienteDeploy", this.props, false, log);
			this.autenticazioneSPIDNomeHeaderPrincipal = getProperty("it.govpay.autenticazioneSPID.nomeHeaderPrincipal", this.props, false, log);
			this.autenticazioneSPIDElencoHeadersRequest = getProperties("it.govpay.autenticazioneSPID.headers.",this.props, false, log);
			
			this.autenticazioneHeaderNomeHeaderPrincipal = getProperty("it.govpay.autenticazioneHeader.nomeHeaderPrincipal", this.props, false, log);
			
			String headersListS = getProperty("it.govpay.autenticazioneHeader.nomiHeadersInfo", props, false, log);
			if(StringUtils.isNotEmpty(headersListS)) {
				String[] split = headersListS.split(",");
				if(split != null && split.length > 0) {
					this.autenticazioneHeaderElencoHeadersRequest = Arrays.asList(split);
				}
			}
			
			String checkCFDebitoreString = getProperty("it.govpay.autenticazione.utenzaAnonima.checkCfDebitore.enabled", props, false, log);
			if(StringUtils.isNotEmpty(checkCFDebitoreString) && !checkCFDebitoreString.equalsIgnoreCase("false")) {
				this.checkCfDebitore = true;
			}
			
			String numeroMassimoEntriesProspettoRiscossioneString = getProperty("it.govpay.reportistica.prospettoRiscossione.numeroMassimoEntries", props, false, log);
			if(StringUtils.isNotEmpty(numeroMassimoEntriesProspettoRiscossioneString)) {
				this.numeroMassimoEntriesProspettoRiscossione = Integer.parseInt(numeroMassimoEntriesProspettoRiscossioneString);;
			}
			
			String batchAvvisaturaDigitaleEnabledString = getProperty("it.govpay.avvisaturaDigitale.enabled", this.props, false, log);
			if(batchAvvisaturaDigitaleEnabledString != null && Boolean.valueOf(batchAvvisaturaDigitaleEnabledString))
				this.avvisaturaDigitaleEnabled = true;
			
			String batchAvvisaturaDigitaleBatchEnabledString = getProperty("it.govpay.avvisaturaDigitale.modalita.asincrona.enabled", this.props, false, log);
			if(batchAvvisaturaDigitaleBatchEnabledString != null && Boolean.valueOf(batchAvvisaturaDigitaleBatchEnabledString))
				this.avvisaturaDigitaleAsincronaEnabled = true;
			
			String batchAvvisaturaDigitaleImmediataEnabledString = getProperty("it.govpay.avvisaturaDigitale.modalita.sincrona.enabled", this.props, false, log);
			if(batchAvvisaturaDigitaleImmediataEnabledString != null && Boolean.valueOf(batchAvvisaturaDigitaleImmediataEnabledString))
				this.avvisaturaDigitaleSincronaEnabled = true;
			
			String sizePaginaNumeroVersamentiPerAvvisoString = getProperty("it.govpay.avvisaturaDigitale.batch.sizePaginaNumeroVersamenti", props, false, log);
			try {
				this.sizePaginaNumeroVersamentiAvvisaturaDigitale = Integer.parseInt(sizePaginaNumeroVersamentiPerAvvisoString);
			} catch(Throwable t) {
				log.info("Proprieta \"it.govpay.avvisaturaDigitale.sizePaginaNumeroVersamenti\" impostata com valore di default (100)");
				this.sizePaginaNumeroVersamentiAvvisaturaDigitale = 100;
			}
			
			String limiteNumeroVersamentiPerAvvisoString = getProperty("it.govpay.avvisaturaDigitale.batch.limiteNumeroVersamenti", props, false, log);
			try {
				this.limiteNumeroVersamentiAvvisaturaDigitale = Integer.parseInt(limiteNumeroVersamentiPerAvvisoString);
			} catch(Throwable t) {
				log.info("Proprieta \"it.govpay.avvisaturaDigitale.limiteNumeroVersamenti\" impostata com valore di default (100000)");
				this.limiteNumeroVersamentiAvvisaturaDigitale = 100000;
			}
			
			this.avvisaturaDigitaleModalitaAnnullamentoAvviso = getProperty("it.govpay.avvisaturaDigitale.annullamentoAvviso.modalita", this.props, false, log);
			
			String intervalloControlloRptPendentiString = getProperty("it.govpay.recuperoRptPendenti.intervalloControlloCreazioneRpt", props, false, log);
			try {
				this.intervalloControlloRptPendenti = Integer.parseInt(intervalloControlloRptPendentiString);
			} catch(Throwable t) {
				log.info("Proprieta \"it.govpay.recuperoRptPendenti.intervalloControlloCreazioneRpt\" impostata com valore di default (100000)");
				this.intervalloControlloRptPendenti = 100000;
			}
			
			String intervalloDisponibilitaPagamentoUtenzaAnonimaString = getProperty("it.govpay.autenticazione.utenzaAnonima.intervalloDisponibilitaPagamento", props, false, log);
			try {
				this.intervalloDisponibilitaPagamentoUtenzaAnonima = Integer.parseInt(intervalloDisponibilitaPagamentoUtenzaAnonimaString);
			} catch(Throwable t) {
				log.info("Proprieta \"it.govpay.autenticazione.utenzaAnonima.intervalloDisponibilitaPagamento\" impostata com valore di default (60) minuti");
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
					throw new Exception("La classe ["+defaultCustomIuvGeneratorClass+"] specificata per la gestione di IUV non e' presente nel classpath");
				}
				Object instance = c.newInstance();
				if(!(instance instanceof CustomIuv)) {
					throw new Exception("La classe ["+defaultCustomIuvGeneratorClass+"] specificata per la gestione di IUV deve estendere la classe " + CustomIuv.class.getName());
				}
				this.defaultCustomIuvGenerator = (CustomIuv) instance;
			} else {
				this.defaultCustomIuvGenerator = new CustomIuv();
			}
			
			this.templateProspettoRiscossioni = getProperty("it.govpay.reportistica.prospettoRiscossione.templateJasper", this.props, false, log);
			
			
			// recovery configurazione sul db
			Map<String, String> propertiesDefault = getProperties("it.govpay.configurazione.",this.props, false, log);
			this.configurazioniDefault.putAll(propertiesDefault);
			
			// Mapping URL-ID -> Url abilitate nel sistema
			Map<String, String> redirectURLs = getProperties("it.govpay.login-redirect.",this.props, false, log);
			this.apiUserLoginRedirectURLs.putAll(redirectURLs);
			
		} catch (Exception e) {
			log.error("Errore di inizializzazione: " + e.getMessage());
			throw e;
		}
	}
	
	private static Map<String,String> getProperties(String baseName, Properties[] props, boolean required, Logger log) throws Exception {
		Map<String, String> valori = new HashMap<>();
		String value = null;
		for(int i=0; i<props.length; i++) {
			if(props[i] != null) {
				for (Object nameObj : props[i].keySet()) {
					String name = (String) nameObj;
					if(name.startsWith(baseName)) {
						String key = name.substring(baseName.length());
						try { value = getProperty(name, props[i], required, i==1, log); } catch (Exception e) { }
						if(value != null && !value.trim().isEmpty()) {
							if(!valori.containsKey(key)) {
								valori.put(key, value);
							}
						}
					}
				}
			}
		}
		
		return valori;
	}

	private static String getProperty(String name, Properties props, boolean required, boolean fromInternalConfig, Logger log) throws Exception {
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
					throw new Exception("Proprieta ["+name+"] non trovata");
				else return null;
			} else {
				if(log != null) log.info("Letta proprieta di configurazione " + logString + name + ": " + value);
			}
		} else {
			if(log != null) log.info("Letta proprieta di sistema " + name + ": " + value);
		}

		return value.trim();
	}

	private static String getProperty(String name, Properties[] props, boolean required, Logger log) throws Exception {
		String value = null;
		for(int i=0; i<props.length; i++) {
			try { value = getProperty(name, props[i], required, i==1, log); } catch (Exception e) { }
			if(value != null && !value.trim().isEmpty()) {
				return value;
			}
		}

		if(log!= null) log.info("Proprieta " + name + " non trovata");

		if(required) 
			throw new Exception("Proprieta ["+name+"] non trovata");
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
	
	public int getDimensionePoolAvvisaturaDigitale() {
		return this.dimensionePoolThreadAvvisaturaDigitale;
	}
	
	public int getDimensionePoolRPT() {
		return this.dimensionePoolThreadRPT;
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
	
	public IConservazione getConservazionPlugin(){
		return this.conservazionePlugin;
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

	public String getAutenticazioneHeaderNomeHeaderPrincipal() {
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

	public boolean isAvvisaturaDigitaleSincronaEnabled() {
		return avvisaturaDigitaleSincronaEnabled;
	}

	public boolean isAvvisaturaDigitaleAsincronaEnabled() {
		return avvisaturaDigitaleAsincronaEnabled;
	}

	public boolean isAvvisaturaDigitaleEnabled() {
		return avvisaturaDigitaleEnabled;
	}
	
	public Integer getSizePaginaNumeroVersamentiAvvisaturaDigitale() {
		return this.sizePaginaNumeroVersamentiAvvisaturaDigitale;
	}

	public Integer getLimiteNumeroVersamentiAvvisaturaDigitale() {
		return this.limiteNumeroVersamentiAvvisaturaDigitale;
	}

	public String getAvvisaturaDigitaleModalitaAnnullamentoAvviso() {
		return avvisaturaDigitaleModalitaAnnullamentoAvviso;
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

	// recovery configurazione generale su db
	public Properties getConfigurazioniDefault() {
		return configurazioniDefault;
	}

	public Properties getApiUserLoginRedirectURLs() {
		return apiUserLoginRedirectURLs;
	}
}
