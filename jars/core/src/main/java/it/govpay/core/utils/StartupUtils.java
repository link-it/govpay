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
package it.govpay.core.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.Properties;
import java.util.TimeZone;

import jakarta.xml.bind.JAXBException;

import org.apache.commons.io.IOUtils;
import org.apache.commons.jcs3.engine.control.CompositeCacheManager;
import org.openspcoop2.utils.LoggerWrapperFactory;
import org.openspcoop2.utils.UtilsException;
import org.openspcoop2.utils.logger.LoggerFactory;
import org.openspcoop2.utils.logger.beans.context.core.Operation;
import org.openspcoop2.utils.logger.beans.context.core.Service;
import org.openspcoop2.utils.logger.config.DatabaseConfig;
import org.openspcoop2.utils.logger.config.DatabaseConfigDatasource;
import org.openspcoop2.utils.logger.config.DiagnosticConfig;
import org.openspcoop2.utils.logger.config.Log4jConfig;
import org.openspcoop2.utils.logger.config.MultiLoggerConfig;
import org.openspcoop2.utils.service.context.ContextThreadLocal;
import org.openspcoop2.utils.service.context.IContext;
import org.openspcoop2.utils.service.context.MD5Constants;
import org.slf4j.Logger;
import org.slf4j.MDC;
import org.xml.sax.SAXException;

import it.govpay.bd.ConnectionManager;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.core.exceptions.ConfigException;
import it.govpay.core.exceptions.StartupException;
import it.govpay.core.utils.logger.Log4JUtils;
import it.govpay.core.utils.logger.MessaggioDiagnosticoUtils;
import it.govpay.core.utils.service.context.GpContextFactory;
import it.govpay.core.utils.thread.ThreadExecutorManager;
import it.govpay.pagopa.beans.utils.JaxbUtils;
import it.govpay.stampe.utils.GovpayStampe;

public class StartupUtils {

	private static final String MSG_ERRORE_INIZIALIZZAZIONE_DI_GOVPAY_FALLITA = "Inizializzazione di {0} fallita: {1}";

	private StartupUtils() {}

	private static boolean initialized = false;
	private static boolean destroyed = false;

	public static synchronized IContext startup(Logger log, String warName, String govpayVersion, String buildVersion,
			InputStream govpayPropertiesIS, URL log4j2XmlFile, InputStream msgDiagnosticiIS, String tipoServizioGovpay,
			InputStream mappingSeveritaErroriPropertiesIS,
			InputStream avvisiLabelPropertiesIS) throws StartupException {

		IContext ctx = null;
		String versioneGovPay = getGovpayVersion(warName, govpayVersion, buildVersion);
		String externalFileName = warName.toLowerCase();
		if(!initialized) {

			GovpayConfig gpConfig = null;
			try {
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				IOUtils.copy(govpayPropertiesIS, baos);
				gpConfig = GovpayConfig.newInstance(new ByteArrayInputStream(baos.toByteArray()), externalFileName);
				it.govpay.bd.GovpayConfig.newInstance4GovPay(new ByteArrayInputStream(baos.toByteArray()), externalFileName);
			} catch (IOException | ConfigException e) {
				throw new StartupException(MessageFormat.format(MSG_ERRORE_INIZIALIZZAZIONE_DI_GOVPAY_FALLITA, versioneGovPay, e.getMessage()), e);
			}

			// Gestione della configurazione di Log4J
			URI log4j2Config = gpConfig.getLog4j2Config();
			Log4jConfig log4jConfig = new Log4jConfig();
			if(log4j2Config != null) {
				log4jConfig.setLog4jConfigFile(new File(log4j2Config));
			} else {
				log4jConfig.setLog4jConfigURL(log4j2XmlFile);
			}

			try {
				log = LoggerWrapperFactory.getLogger("boot");
				log.info("Inizializzazione {} in corso", versioneGovPay);

				if(log4j2Config != null) {
					log.info("Caricata configurazione logger: {}" , gpConfig.getLog4j2Config().getPath());
				} else {
					log.info("Configurazione logger da classpath.");
				}
				gpConfig.readProperties(externalFileName);
			} catch (ConfigException e) {
				throw new StartupException(MessageFormat.format(MSG_ERRORE_INIZIALIZZAZIONE_DI_GOVPAY_FALLITA, versioneGovPay, e.getMessage()), e);
			}

			// inizializzo utilities di logging
			Log4JUtils.initialize(log4j2XmlFile);

			// Configurazione del logger Diagnostici/Tracce/Dump
			try {
				DiagnosticConfig diagnosticConfig = new DiagnosticConfig();
				Properties props = new Properties();
				props.load(msgDiagnosticiIS);
				diagnosticConfig.setDiagnosticConfigProperties(props);
				diagnosticConfig.setThrowExceptionPlaceholderFailedResolution(false);

				MultiLoggerConfig mConfig = new MultiLoggerConfig();
				mConfig.setDiagnosticConfig(diagnosticConfig);
				mConfig.setDiagnosticSeverityFilter(GovpayConfig.getInstance().getmLogLevel());
				mConfig.setLog4jLoggerEnabled(GovpayConfig.getInstance().ismLogOnLog4j());
				mConfig.setLog4jConfig(log4jConfig);
				mConfig.setDbLoggerEnabled(GovpayConfig.getInstance().ismLogOnDB());

				if(GovpayConfig.getInstance().ismLogOnDB()) {
					DatabaseConfig dbConfig = new DatabaseConfig();
					DatabaseConfigDatasource dbDSConfig = new DatabaseConfigDatasource();
					dbDSConfig.setJndiName(GovpayConfig.getInstance().getmLogDS());
					dbConfig.setConfigDatasource(dbDSConfig);
					dbConfig.setDatabaseType(GovpayConfig.getInstance().getmLogDBType());
					dbConfig.setLogSql(GovpayConfig.getInstance().ismLogSql());
					mConfig.setDatabaseConfig(dbConfig);
				}
				LoggerFactory.initialize(GovpayConfig.getInstance().getmLogClass(), mConfig);

			} catch (IOException | UtilsException | ClassNotFoundException e) {
				LogUtils.logError(log, "Errore durante la configurazione dei diagnostici", e);
				throw new StartupException(MessageFormat.format(MSG_ERRORE_INIZIALIZZAZIONE_DI_GOVPAY_FALLITA, versioneGovPay, e.getMessage()), e);
			}

			// Mapping Severita Errori
			try {
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				IOUtils.copy(mappingSeveritaErroriPropertiesIS, baos);
				SeveritaProperties.newInstance(new ByteArrayInputStream(baos.toByteArray()));
			} catch (IOException | ConfigException e) {
				throw new StartupException(MessageFormat.format(MSG_ERRORE_INIZIALIZZAZIONE_DI_GOVPAY_FALLITA, versioneGovPay, e.getMessage()), e);
			}

			// Label Avvisi Pagamento
			try {
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				IOUtils.copy(avvisiLabelPropertiesIS, baos);
				LabelAvvisiProperties.newInstance(new ByteArrayInputStream(baos.toByteArray()));
			} catch (IOException | ConfigException e) {
				throw new StartupException(MessageFormat.format(MSG_ERRORE_INIZIALIZZAZIONE_DI_GOVPAY_FALLITA, versioneGovPay, e.getMessage()), e);
			}
		}
		try {
			GpContextFactory factory = new GpContextFactory();
			ctx = factory.newBatchContext();
			MDC.put(MD5Constants.OPERATION_ID, "Inizializzazione " +  warName);
			MDC.put(MD5Constants.TRANSACTION_ID, ctx.getTransactionId());
			Service service = new Service();
			service.setName("Inizializzazione "+ warName);
			service.setType(tipoServizioGovpay);
			ctx.getApplicationContext().getTransaction().setService(service);
			Operation opt = new Operation();
			opt.setName("Init");
			ctx.getApplicationContext().getTransaction().setOperation(opt);
			ContextThreadLocal.set(ctx);
		} catch (Exception e) {
			LogUtils.logError(log, "Errore durante predisposizione del contesto: {}, {}", e.getMessage(), e);
			if(ctx != null){
				MessaggioDiagnosticoUtils.log(log, ctx);
			}
			throw new StartupException(MessageFormat.format(MSG_ERRORE_INIZIALIZZAZIONE_DI_GOVPAY_FALLITA, versioneGovPay, e.getMessage()), e);
		}

		initialized = true;
		return ctx;
	}

	public static synchronized IContext startupServices(Logger log, String warName, String govpayVersion, String buildVersion, IContext ctx,
			String dominioAnagraficaManager,GovpayConfig gpConfig) throws StartupException {

		String versioneGovPay = getGovpayVersion(warName, govpayVersion, buildVersion);
		try {
			AnagraficaManager.newInstance(dominioAnagraficaManager);
			JaxbUtils.init();
			it.govpay.jppapdp.beans.utils.JaxbUtils.init();
			ThreadExecutorManager.setup();
			GovpayStampe.init(log, gpConfig.getResourceDir());
		} catch (UtilsException | JAXBException | SAXException | ConfigException e) {
			throw new StartupException(MessageFormat.format(MSG_ERRORE_INIZIALIZZAZIONE_DI_GOVPAY_FALLITA, versioneGovPay, e.getMessage()), e);
		}
		log.info("Charset.defaultCharset(): {}", Charset.defaultCharset());
		log.info("Locale.getDefault(): {}", Locale.getDefault());
		log.info("TimeZone.getDefault(): {}", TimeZone.getDefault());
		return ctx;
	}


	public static String getGovpayVersion(String warName, String govpayVersion, String buildVersion) {
		return warName + " " + govpayVersion + " (build " + buildVersion + ")";
	}

	public static synchronized void stopServices(Logger log, String warName, String govpayVersion, String buildVersion, String dominioAnagraficaManager) throws StartupException {

		String versioneGovPay = getGovpayVersion(warName, govpayVersion, buildVersion);
		if(!destroyed) {

			log.info("Shutdown {} in corso...", versioneGovPay);

			log.info("De-registrazione delle cache ...");
			AnagraficaManager.unregister();
			log.info("De-registrazione delle cache completato");

			log.info("Shutdown pool thread notifiche ...");
			try {
				ThreadExecutorManager.shutdown();
				log.info("Shutdown pool thread notifiche completato.");
			} catch (InterruptedException e) {
				log.warn("Shutdown pool thread notifiche fallito: "+ e.getMessage(), e);
				 // Restore interrupted state...
			    Thread.currentThread().interrupt();
			}

			log.info("Shutdown del Connection Manager ...");
			try {
				ConnectionManager.shutdown();
				log.info("Shutdown del Connection Manager completato.");
			} catch (Exception e) {
				log.warn("Errore nello shutdown del Connection Manager: " + e.getMessage(), e);
			}

			log.info("Shutdown di {} completato.",versioneGovPay);

		}

		destroyed = true;
	}
}
