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
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.openspcoop2.utils.LoggerWrapperFactory;
import org.openspcoop2.utils.logger.LoggerFactory;
import org.openspcoop2.utils.logger.beans.proxy.Operation;
import org.openspcoop2.utils.logger.beans.proxy.Service;
import org.openspcoop2.utils.logger.config.DatabaseConfig;
import org.openspcoop2.utils.logger.config.DatabaseConfigDatasource;
import org.openspcoop2.utils.logger.config.DiagnosticConfig;
import org.openspcoop2.utils.logger.config.Log4jConfig;
import org.openspcoop2.utils.logger.config.MultiLoggerConfig;
import org.slf4j.Logger;
import org.slf4j.MDC;

import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.core.cache.AclCache;
import it.govpay.core.utils.thread.ThreadExecutorManager;
import it.govpay.rs.v1.BaseRsServiceV1;
import it.govpay.stampe.pdf.avvisoPagamento.utils.AvvisoPagamentoProperties;
import it.govpay.stampe.pdf.rt.utils.RicevutaTelematicaProperties;

public class StartupUtils {

	private static boolean initialized = false;
	
	public static synchronized GpContext startup(Logger log, String warName, String govpayVersion, String buildVersion, 
			InputStream govpayPropertiesIS, URL log4j2XmlFile, InputStream msgDiagnosticiIS, String tipoServizioGovpay) throws RuntimeException {
		
		GpContext ctx = null;
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

		}
		try {
			ctx = new GpContext();
			MDC.put("cmd", "Inizializzazione " +  warName);
			MDC.put("op", ctx.getTransactionId());
			Service service = new Service();
			service.setName("Inizializzazione "+ warName);
			service.setType(tipoServizioGovpay);
			ctx.getTransaction().setService(service);
			Operation opt = new Operation();
			opt.setName("Init");
			ctx.getTransaction().setOperation(opt);
			GpThreadLocal.set(ctx);
		} catch (Exception e) {
			log.error("Errore durante predisposizione del contesto: " + e);
			if(ctx != null) ctx.log();
			throw new RuntimeException("Inizializzazione di "+getGovpayVersion(warName, govpayVersion, buildVersion)+" fallita.", e);
		}
		
		initialized = true;
		return ctx;
	}
	
	public static synchronized GpContext initValidator(Logger log, String warName, String govpayVersion, String buildVersion, GpContext ctx, String name, InputStream swaggerfile) throws RuntimeException {
		
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			IOUtils.copy(swaggerfile, baos);
			BaseRsServiceV1.initValidator(log, baos.toByteArray(), name);
		} catch (Exception e) {
			throw new RuntimeException("Inizializzazione di "+getGovpayVersion(warName, govpayVersion, buildVersion)+" fallita.", e);
		}
		
		return ctx;
	}
	
	public static synchronized GpContext startupServices(Logger log, String warName, String govpayVersion, String buildVersion, GpContext ctx,
			String dominioAnagraficaManager,GovpayConfig gpConfig) throws RuntimeException {
		
		try {
			AnagraficaManager.newInstance(dominioAnagraficaManager);
			JaxbUtils.init();
			ThreadExecutorManager.setup();
			AvvisoPagamentoProperties.newInstance(gpConfig.getResourceDir());
			RicevutaTelematicaProperties.newInstance(gpConfig.getResourceDir());
			AclCache.newInstance(log);
		} catch (Exception e) {
			throw new RuntimeException("Inizializzazione di "+getGovpayVersion(warName, govpayVersion, buildVersion)+" fallita.", e);
		}
		
		return ctx;
	}
	
	
	public static String getGovpayVersion(String warName, String govpayVersion, String buildVersion) {
		return warName + " " + govpayVersion + " (build " + buildVersion + ")";
	}
}
