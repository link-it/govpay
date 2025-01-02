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
import java.util.Locale;
import java.util.Properties;
import java.util.TimeZone;

import jakarta.xml.bind.JAXBException;

import org.apache.commons.io.IOUtils;
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

import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.core.exceptions.ConfigException;
import it.govpay.core.utils.logger.Log4JUtils;
import it.govpay.core.utils.service.context.GpContextFactory;
import it.govpay.core.utils.thread.ThreadExecutorManager;
import it.govpay.pagopa.beans.utils.JaxbUtils;
import it.govpay.stampe.utils.GovpayStampe;

public class StartupUtils {
	
	private StartupUtils() {}

	private static boolean initialized = false;
	
	public static synchronized IContext startup(Logger log, String warName, String govpayVersion, String buildVersion, 
			InputStream govpayPropertiesIS, URL log4j2XmlFile, InputStream msgDiagnosticiIS, String tipoServizioGovpay,
			InputStream mappingSeveritaErroriPropertiesIS,
			InputStream avvisiLabelPropertiesIS) throws RuntimeException {
		
		IContext ctx = null;
		String versioneGovPay = getGovpayVersion(warName, govpayVersion, buildVersion);
		if(!initialized) {
			
			GovpayConfig gpConfig = null;
			try {
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				IOUtils.copy(govpayPropertiesIS, baos);
				gpConfig = GovpayConfig.newInstance(new ByteArrayInputStream(baos.toByteArray()));
				it.govpay.bd.GovpayConfig.newInstance4GovPay(new ByteArrayInputStream(baos.toByteArray()));
			} catch (IOException | ConfigException e) {
				throw new RuntimeException("Inizializzazione di "+versioneGovPay+" fallita: " + e, e);
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
				gpConfig.readProperties();
			} catch (ConfigException e) {
				throw new RuntimeException("Inizializzazione di "+versioneGovPay+" fallita: " + e, e);
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
				throw new RuntimeException("Inizializzazione di "+versioneGovPay+" fallita.", e);
			}

			// Mapping Severita Errori
			try {
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				IOUtils.copy(mappingSeveritaErroriPropertiesIS, baos);
				SeveritaProperties.newInstance(new ByteArrayInputStream(baos.toByteArray()));
			} catch (IOException | ConfigException e) {
				throw new RuntimeException("Inizializzazione di "+versioneGovPay+" fallita: " + e, e);
			}
			
			// Label Avvisi Pagamento
			try {
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				IOUtils.copy(avvisiLabelPropertiesIS, baos);
				LabelAvvisiProperties.newInstance(new ByteArrayInputStream(baos.toByteArray()));
			} catch (IOException | ConfigException e) {
				throw new RuntimeException("Inizializzazione di "+versioneGovPay+" fallita: " + e, e);
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
			if(ctx != null)
				try {
					ctx.getApplicationLogger().log();
				} catch (UtilsException e1) {
					LogUtils.logError(log, "Errore durante predisposizione del contesto:  {}, {}", e1.getMessage(), e1);
				}
			throw new RuntimeException("Inizializzazione di "+versioneGovPay+" fallita.", e);
		}
		
		initialized = true;
		return ctx;
	}
	
	public static synchronized IContext startupServices(Logger log, String warName, String govpayVersion, String buildVersion, IContext ctx,
			String dominioAnagraficaManager,GovpayConfig gpConfig) throws RuntimeException {
		
		try {
			AnagraficaManager.newInstance(dominioAnagraficaManager);
			JaxbUtils.init();
			it.govpay.jppapdp.beans.utils.JaxbUtils.init();
			ThreadExecutorManager.setup();
			GovpayStampe.init(log, gpConfig.getResourceDir());
		} catch (UtilsException | JAXBException | SAXException | ConfigException e) {
			throw new RuntimeException("Inizializzazione di "+getGovpayVersion(warName, govpayVersion, buildVersion)+" fallita.", e);
		}
		log.info("Charset.defaultCharset(): {}", Charset.defaultCharset());
		log.info("Locale.getDefault(): {}", Locale.getDefault());
		log.info("TimeZone.getDefault(): {}", TimeZone.getDefault());
		return ctx;
	}
	
	
	public static String getGovpayVersion(String warName, String govpayVersion, String buildVersion) {
		return warName + " " + govpayVersion + " (build " + buildVersion + ")";
	}
}
