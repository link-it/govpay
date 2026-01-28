/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2026 Link.it srl (http://www.link.it).
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
package it.govpay.web.listener;

import java.io.InputStream;
import java.net.URL;
import java.util.UUID;

import org.openspcoop2.utils.LoggerWrapperFactory;
import org.openspcoop2.utils.service.context.IContext;
import org.openspcoop2.utils.service.context.MD5Constants;
import org.slf4j.Logger;
import org.slf4j.MDC;

import it.govpay.core.beans.GovPayExceptionProperties;
import it.govpay.core.exceptions.StartupException;
import it.govpay.core.utils.GovpayConfig;
import it.govpay.core.utils.InitConstants;
import it.govpay.core.utils.LabelAvvisiProperties;
import it.govpay.core.utils.LogUtils;
import it.govpay.core.utils.SeveritaProperties;
import it.govpay.core.utils.StartupUtils;
import it.govpay.core.utils.logger.MessaggioDiagnosticoUtils;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;

public abstract class BaseInitListener implements ServletContextListener{

	private Logger log = null;

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		// Commit id
		String commit = (InitConstants.GOVPAY_BUILD_NUMBER.length() > 7) ? InitConstants.GOVPAY_BUILD_NUMBER.substring(0, 7) : InitConstants.GOVPAY_BUILD_NUMBER;

		InputStream govpayPropertiesIS = BaseInitListener.class.getResourceAsStream(GovpayConfig.PROPERTIES_FILE);
		URL log4j2URL = BaseInitListener.class.getResource(GovpayConfig.LOG4J2_XML_FILE);
		InputStream msgDiagnosticiIS = BaseInitListener.class.getResourceAsStream(GovpayConfig.MSG_DIAGNOSTICI_PROPERTIES_FILE);
		InputStream mappingSeveritaErroriPropertiesIS = BaseInitListener.class.getResourceAsStream(SeveritaProperties.MAPPING_SEVERITA_ERRORI_PROPERTIES_FILE);
		InputStream avvisiLabelPropertiesIS = BaseInitListener.class.getResourceAsStream(LabelAvvisiProperties.PROPERTIES_FILE);
		InputStream govpayExceptionPropertiesIS = BaseInitListener.class.getResourceAsStream(GovPayExceptionProperties.GOVPAY_EXCEPTION_MESSAGES_PROPERTIES_FILE);
		IContext ctx = StartupUtils.startup(log, this.getWarName(), InitConstants.GOVPAY_VERSION, commit, govpayPropertiesIS, log4j2URL, msgDiagnosticiIS, this.getTipoServizioGovpay(), mappingSeveritaErroriPropertiesIS, avvisiLabelPropertiesIS, govpayExceptionPropertiesIS);
		try {
			log = LoggerWrapperFactory.getLogger("boot");
			StartupUtils.startupServices(log, this.getWarName(), InitConstants.GOVPAY_VERSION, commit, ctx, this.getDominioAnagraficaManager(), GovpayConfig.getInstance());
		} catch (StartupException e) {
			LogUtils.logError(log, this.getMessaggioErroreInizializzazione(commit), e);
			MessaggioDiagnosticoUtils.log(log, ctx);
			throw e;
		} catch (NullPointerException e) {
			LogUtils.logError(log, this.getMessaggioErroreInizializzazione(commit), e);
			MessaggioDiagnosticoUtils.log(log, ctx);
			throw new StartupException(this.getMessaggioErroreInizializzazione(commit), e);
		}

		MessaggioDiagnosticoUtils.log(log, ctx);

		LogUtils.logInfo(log, "Inizializzazione "+StartupUtils.getGovpayVersion(this.getWarName(), InitConstants.GOVPAY_VERSION, commit)+" completata con successo.");
	}


	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		// Commit id
		String commit = (InitConstants.GOVPAY_BUILD_NUMBER.length() > 7) ? InitConstants.GOVPAY_BUILD_NUMBER.substring(0, 7) : InitConstants.GOVPAY_BUILD_NUMBER;
		MDC.put(MD5Constants.OPERATION_ID, "Shutdown");
		MDC.put(MD5Constants.TRANSACTION_ID, UUID.randomUUID().toString() );

		StartupUtils.stopServices(log, this.getWarName(), InitConstants.GOVPAY_VERSION, commit, this.getDominioAnagraficaManager());
		
	}
	
	public String getMessaggioErroreInizializzazione(String commit) {
		return "Inizializzazione "+StartupUtils.getGovpayVersion(this.getWarName(), InitConstants.GOVPAY_VERSION, commit)+" fallita.";
	}
	
	public abstract String getWarName();
	public abstract String getTipoServizioGovpay();
	public abstract String getDominioAnagraficaManager();
}
