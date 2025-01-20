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
package it.govpay.backoffice.listener;

import java.io.InputStream;
import java.net.URL;
import java.util.UUID;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.openspcoop2.utils.LoggerWrapperFactory;
import org.openspcoop2.utils.UtilsException;
import org.openspcoop2.utils.service.context.IContext;
import org.openspcoop2.utils.service.context.MD5Constants;
import org.slf4j.Logger;
import org.slf4j.MDC;

import it.govpay.core.exceptions.StartupException;
import it.govpay.core.utils.GovpayConfig;
import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.InitConstants;
import it.govpay.core.utils.LabelAvvisiProperties;
import it.govpay.core.utils.LogUtils;
import it.govpay.core.utils.SeveritaProperties;
import it.govpay.core.utils.StartupUtils;

public class InitListener implements ServletContextListener{

	private static Logger log = null;
	private static boolean initialized = false;
	private String warName = "GovPay-API-Backoffice";
	private String tipoServizioGovpay = GpContext.TIPO_SERVIZIO_GOVPAY_JSON;
	private String dominioAnagraficaManager = "it.govpay.cache.anagrafica.backoffice";

	public static boolean isInitialized() {
		return InitListener.initialized;
	}

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		// Commit id
		String commit = (InitConstants.GOVPAY_BUILD_NUMBER.length() > 7) ? InitConstants.GOVPAY_BUILD_NUMBER.substring(0, 7) : InitConstants.GOVPAY_BUILD_NUMBER;

		InputStream govpayPropertiesIS = InitListener.class.getResourceAsStream(GovpayConfig.PROPERTIES_FILE);
		URL log4j2URL = InitListener.class.getResource(GovpayConfig.LOG4J2_XML_FILE);
		InputStream msgDiagnosticiIS = InitListener.class.getResourceAsStream(GovpayConfig.MSG_DIAGNOSTICI_PROPERTIES_FILE);
		InputStream mappingSeveritaErroriPropertiesIS = InitListener.class.getResourceAsStream(SeveritaProperties.MAPPING_SEVERITA_ERRORI_PROPERTIES_FILE);
		InputStream avvisiLabelPropertiesIS = InitListener.class.getResourceAsStream(LabelAvvisiProperties.PROPERTIES_FILE);
		IContext ctx = StartupUtils.startup(log, warName, InitConstants.GOVPAY_VERSION, commit, govpayPropertiesIS, log4j2URL, msgDiagnosticiIS, tipoServizioGovpay, mappingSeveritaErroriPropertiesIS, avvisiLabelPropertiesIS);
		try {
			log = LoggerWrapperFactory.getLogger("boot");
			StartupUtils.startupServices(log, warName, InitConstants.GOVPAY_VERSION, commit, ctx, dominioAnagraficaManager, GovpayConfig.getInstance());
		} catch (StartupException e) {
			log.error("Inizializzazione fallita", e);
			try {
				ctx.getApplicationLogger().log();
			} catch (UtilsException e1) {
				log.error("Errore durante il log dell'operazione: "+e1.getMessage(), e1);
			}
			throw e;
		} catch (Exception e) {
			log.error("Inizializzazione fallita", e);
			try {
				ctx.getApplicationLogger().log();
			} catch (UtilsException e1) {
				log.error("Errore durante il log dell'operazione: "+e1.getMessage(), e1);
			}
			throw new StartupException("Inizializzazione "+StartupUtils.getGovpayVersion(warName, InitConstants.GOVPAY_VERSION, commit)+" fallita.", e);
		}

		try {
			ctx.getApplicationLogger().log();
		} catch (UtilsException e) {
			log.error("Errore durante il log dell'operazione: "+e.getMessage(), e);
		}

		LogUtils.logInfo(log, "Inizializzazione "+StartupUtils.getGovpayVersion(warName, InitConstants.GOVPAY_VERSION, commit)+" completata con successo.");
		initialized = true;
	}


	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		// Commit id
		String commit = (InitConstants.GOVPAY_BUILD_NUMBER.length() > 7) ? InitConstants.GOVPAY_BUILD_NUMBER.substring(0, 7) : InitConstants.GOVPAY_BUILD_NUMBER;
		MDC.put(MD5Constants.OPERATION_ID, "Shutdown");
		MDC.put(MD5Constants.TRANSACTION_ID, UUID.randomUUID().toString() );

		StartupUtils.stopServices(log, warName, InitConstants.GOVPAY_VERSION, commit, dominioAnagraficaManager);
		
	}
}
