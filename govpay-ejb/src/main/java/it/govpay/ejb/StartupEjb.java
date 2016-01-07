/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2015 Link.it srl (http://www.link.it).
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
import it.govpay.business.Pagamenti;
import it.govpay.business.RegistroPSP;
import it.govpay.business.Rendicontazioni;
import it.govpay.thread.ThreadExecutorManager;
import it.govpay.utils.JaxbUtils;

import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.ejb.Startup;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;

@Startup
@Singleton
public class StartupEjb {
	
	private static Logger log = LogManager.getLogger();	
	
	@PostConstruct
	public void init() {
		ThreadContext.put("cmd", "Startup");
		ThreadContext.put("op", UUID.randomUUID().toString() );
		
		try {
			AnagraficaManager.newInstance();
			JaxbUtils.init();
			ConnectionManager.initialize();
			ThreadExecutorManager.setup();
			JmxOperazioni.register();
		} catch (Exception e) {
			log.error("Inizializzazione fallita", e);
			shutdown();
			throw new RuntimeException("Inizializzazione GovPay fallita.", e);
		}
		
		BasicBD bd = null;
		try {
			bd = BasicBD.newInstance();
			new RegistroPSP(bd).aggiornaRegistro();
		} catch (Exception e) {
			log.error("Aggiornamento della lista dei PSP fallito",e);
		} finally {
			if(bd != null) bd.closeConnection();
		}
		
		try {
			bd = BasicBD.newInstance();
			new Rendicontazioni(bd).downloadRendicontazioni();
		} catch (Exception e) {
			log.error("Aggiornamento delle rendicontazioni fallito",e);
		} finally {
			if(bd != null) bd.closeConnection();
		}
		
		try {
			bd = BasicBD.newInstance();
			new Pagamenti(bd).verificaRptPedenti();
		} catch (Exception e) {
			log.info("Acquisizione Rpt pendenti fallita", e);
		} finally {
			if(bd != null) bd.closeConnection();
		}
	}
	
	@PreDestroy
	public void shutdown() {
		ThreadContext.put("cmd", "Shutdown");
		ThreadContext.put("op", UUID.randomUUID().toString() );
		log.info("Rimozione delle cache");
		AnagraficaManager.unregister();
		log.info("Shutdown pool thread esiti");
		try {
			ThreadExecutorManager.shutdown();
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