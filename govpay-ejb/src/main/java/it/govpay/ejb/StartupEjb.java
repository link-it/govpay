/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2016 Link.it srl (http://www.link.it).
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
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
package it.govpay.ejb;

import it.govpay.bd.BasicBD;
import it.govpay.bd.ConnectionManager;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.core.business.Psp;
import it.govpay.core.utils.GovpayConfig;
import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.GpThreadLocal;
import it.govpay.core.utils.JaxbUtils;
import it.govpay.core.utils.thread.ThreadExecutorManager;

import java.io.File;
import java.net.URI;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.ejb.Startup;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.apache.logging.log4j.core.LoggerContext;
import org.openspcoop2.utils.logger.LoggerFactory;
import org.openspcoop2.utils.logger.beans.proxy.Service;
import org.openspcoop2.utils.logger.log4j.Log4JLoggerWithProxyContext;
import org.openspcoop2.utils.logger.log4j.Log4jType;

@Startup
@Singleton
public class StartupEjb {
	
	private static Logger log = LogManager.getLogger();	
	
	@PostConstruct
	public void init() {
		
		// Gestione della configurazione di Log4J
		URI log4j2Config = null;
		try {
			log4j2Config = GovpayConfig.newInstance().getLog4j2Config();
			if(log4j2Config != null) {
				LoggerContext context = (org.apache.logging.log4j.core.LoggerContext) LogManager.getContext(false);
				context.setConfigLocation(log4j2Config);
			}
		} catch (Exception e) {
			log.warn("Errore durante la configurazione del Logger: " + e);
		}
		
		try {
			if(log4j2Config != null) {
				LoggerFactory.initialize(Log4JLoggerWithProxyContext.class.getName(),
						"/msgDiagnostici.properties",
						false,
						new File(log4j2Config), Log4jType.LOG4Jv2);
			} else {
				LoggerFactory.initialize(Log4JLoggerWithProxyContext.class.getName(),
						"/msgDiagnostici.properties",
						false,
						"/log4j2.xml", Log4jType.LOG4Jv2);
			}
		} catch (Exception e) {
			log.error("Errore durante la configurazione dei diagnostici", e);
			throw new RuntimeException("Inizializzazione GovPay fallita.", e);
		}
		
		GpContext ctx = null;
		
		try {
			ctx = new GpContext();
			ThreadContext.put("cmd", "Inizializzazione");
			ThreadContext.put("op", ctx.getTransactionId());
			Service service = new Service();
			service.setName("Inizializzazione");
			ctx.getTransaction().setService(service);
			GpThreadLocal.set(ctx);
		} catch (Exception e) {
			log.error("Errore durante predisposizione del contesto: " + e);
			if(ctx != null) ctx.log();
			throw new RuntimeException("Inizializzazione GovPay fallita.", e);
		}
		
		try {
			AnagraficaManager.newInstance();
			JaxbUtils.init();
			ConnectionManager.initialize();
			ThreadExecutorManager.setup();
			JmxOperazioni.register();
		} catch (Exception e) {
			log.error("Inizializzazione fallita", e);
			shutdown();
			ctx.log();
			throw new RuntimeException("Inizializzazione GovPay fallita.", e);
		}
		
		BasicBD bd = null;
		try {
			bd = BasicBD.newInstance();
			new Psp(bd).aggiornaRegistro();
		} catch (Exception e) {
			log.error("Aggiornamento della lista dei PSP fallito",e);
		} finally {
			if(bd != null) bd.closeConnection();
		}
		
//		try {
//			bd = BasicBD.newInstance();
//			new Rendicontazioni(bd).downloadRendicontazioni();
//		} catch (Exception e) {
//			log.error("Aggiornamento delle rendicontazioni fallito",e);
//		} finally {
//			if(bd != null) bd.closeConnection();
//		}
		
		ctx.log();
	}
	
	@PreDestroy
	public void shutdown() {
		ThreadContext.put("cmd", "Shutdown");
		ThreadContext.put("op", UUID.randomUUID().toString() );
		log.info("Rimozione delle cache");
		AnagraficaManager.unregister();
		log.info("Shutdown pool thread esiti");
		try {
//			ThreadExecutorManager.shutdown();
		} catch (Exception e) {
			log.warn("Shutdown pool thread esiti fallito:" + e);
		}
		log.info("Deregistrazione risorse JMX");
		try {
			JmxOperazioni.unregister();
		} catch (Exception e) {
			log.warn("Errore nella de-registrazione JMX: " + e);
		}
	}
}