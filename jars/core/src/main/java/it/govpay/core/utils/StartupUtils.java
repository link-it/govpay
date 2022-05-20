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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Properties;

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

import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.core.utils.logger.Log4JUtils;
import it.govpay.core.utils.service.context.GpContextFactory;
import it.govpay.core.utils.thread.ThreadExecutorManager;
import it.govpay.stampe.utils.GovpayStampe;

public class StartupUtils {

	private static boolean initialized = false;
	
	public static synchronized IContext startup(Logger log, String warName, String govpayVersion, String buildVersion, 
			InputStream govpayPropertiesIS, URL log4j2XmlFile, InputStream msgDiagnosticiIS, String tipoServizioGovpay,
			InputStream mappingSeveritaErroriPropertiesIS,
			InputStream avvisiLabelPropertiesIS) throws RuntimeException {
		
		IContext ctx = null;
		if(!initialized) {
			
			GovpayConfig gpConfig = null;
			try {
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				IOUtils.copy(govpayPropertiesIS, baos);
				gpConfig = GovpayConfig.newInstance(new ByteArrayInputStream(baos.toByteArray()));
				it.govpay.bd.GovpayConfig.newInstance4GovPay(new ByteArrayInputStream(baos.toByteArray()));
				it.govpay.bd.GovpayCustomConfig.newInstance(new ByteArrayInputStream(baos.toByteArray()));
			} catch (Exception e) {
				throw new RuntimeException("Inizializzazione di "+getGovpayVersion(warName, govpayVersion, buildVersion)+" fallita: " + e, e);
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
				log.info("Inizializzazione "+getGovpayVersion(warName, govpayVersion, buildVersion)+" in corso"); 
				
				if(log4j2Config != null) {
					log.info("Caricata configurazione logger: " + gpConfig.getLog4j2Config().getPath());
				} else {
					log.info("Configurazione logger da classpath.");
				}
				gpConfig.readProperties();
			} catch (Exception e) {
				throw new RuntimeException("Inizializzazione di "+getGovpayVersion(warName, govpayVersion, buildVersion)+" fallita: " + e, e);
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
	
			} catch (Exception e) {
				log.error("Errore durante la configurazione dei diagnostici", e);
				throw new RuntimeException("Inizializzazione di "+getGovpayVersion(warName, govpayVersion, buildVersion)+" fallita.", e);
			}

			// Mapping Severita Errori
			try {
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				IOUtils.copy(mappingSeveritaErroriPropertiesIS, baos);
				SeveritaProperties.newInstance(new ByteArrayInputStream(baos.toByteArray()));
			} catch (Exception e) {
				throw new RuntimeException("Inizializzazione di "+getGovpayVersion(warName, govpayVersion, buildVersion)+" fallita: " + e, e);
			}
			
			// Label Avvisi Pagamento
			try {
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				IOUtils.copy(avvisiLabelPropertiesIS, baos);
				LabelAvvisiProperties.newInstance(new ByteArrayInputStream(baos.toByteArray()));
			} catch (Exception e) {
				throw new RuntimeException("Inizializzazione di "+getGovpayVersion(warName, govpayVersion, buildVersion)+" fallita: " + e, e);
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
			log.error("Errore durante predisposizione del contesto: " + e);
			if(ctx != null)
				try {
					ctx.getApplicationLogger().log();
				} catch (UtilsException e1) {
					log.error("Errore durante predisposizione del contesto: " + e1);
				}
			throw new RuntimeException("Inizializzazione di "+getGovpayVersion(warName, govpayVersion, buildVersion)+" fallita.", e);
		}
		
		initialized = true;
		return ctx;
	}
	
	public static synchronized IContext startupServices(Logger log, String warName, String govpayVersion, String buildVersion, IContext ctx,
			String dominioAnagraficaManager,GovpayConfig gpConfig) throws RuntimeException {
		
		try {
//			Locale.setDefault(Locale.ITALY); 
//			log.info("Impostato Locale: "+Locale.getDefault()+" .");
			
			AnagraficaManager.newInstance(dominioAnagraficaManager);
			JaxbUtils.init();
			ThreadExecutorManager.setup();
			GovpayStampe.init(log, gpConfig.getResourceDir());
		} catch (Exception e) {
			throw new RuntimeException("Inizializzazione di "+getGovpayVersion(warName, govpayVersion, buildVersion)+" fallita.", e);
		}
		log.info("Charset.defaultCharset(): " + Charset.defaultCharset() );
		return ctx;
	}
	
	
	public static String getGovpayVersion(String warName, String govpayVersion, String buildVersion) {
		return warName + " " + govpayVersion + " (build " + buildVersion + ")";
	}
}
