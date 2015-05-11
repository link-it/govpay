/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2015 Link.it srl (http://www.link.it).
 * Copyright (c) 2014-2015 TAS S.p.A. (http://www.tasgroup.it).
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
package it.govpay.web;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;

import it.govpay.ejb.exception.GovPayException;
import it.govpay.ejb.exception.GovPayException.GovPayExceptionEnum;
import it.govpay.ejb.utils.DataTypeUtils;
import it.govpay.ejb.utils.GovpayConfiguration;
import it.govpay.ejb.utils.rs.JaxbUtils;
import it.govpay.ndp.util.NdpConfiguration;
import it.govpay.ndp.util.wsclient.NodoPerPa;
import it.govpay.web.controller.GatewayController;
import it.govpay.web.timer.AggiornaPspTimer;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;

@Startup
@Singleton
public class GovPayStartup {
	@Inject
	GatewayController gatewayCtrl;
	
	@Inject
	AggiornaPspTimer aggiornaPspTmr;
	
    @Inject  
    private transient Logger log;

	@PostConstruct 
	void atStartup() throws GovPayException { 
		ThreadContext.put("proc", "Startup");
		log.info("Caricamento delle configurazioni");
		NdpConfiguration.newInstance();
		GovpayConfiguration.newInstance();
		
		log.info("Inizializzazione dei componenti");
		try {
			JaxbUtils.init();
		} catch (Exception e) {
			throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO, "Impossibile inizializzare il Marshaller", e);
		}
		log.info("Marshaller e Unmarshaller inizializzati");
		try {
			DataTypeUtils.init();
		} catch (Exception e) {
			throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO, "Impossibile inizializzare il DataType", e);
		}
		log.info("Datatype inizializzato");
		try {
			NodoPerPa.init();
		} catch (Exception e) {
			throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO, "Impossibile inizializzare il fruitore del Nodo dei Pagamenti", e);
		}
		log.info("Jaxws stub per il servizio Nodo dei Pagamenti inizializzato.");
		
		log.info("Aggiornamento lista PSP disponibili");
		try{
			aggiornaPspTmr.doWork();
			log.info("Lista PSP aggiornata con successo.");
		} catch (Exception e){
			log.warn("Impossibile aggiornare la lista psp: " + e);
		}
		
	}
	
	public static void addPath(String s) throws GovPayException {
		try {
		    File f = new File(s);
		    URI u = f.toURI();
		    URLClassLoader urlClassLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
		    Class<URLClassLoader> urlClass = URLClassLoader.class;
		    Method method = urlClass.getDeclaredMethod("addURL", new Class[]{URL.class});
		    method.setAccessible(true);
		    method.invoke(urlClassLoader, new Object[]{u.toURL()});
		} catch (Exception e) {
			throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO, e);
		}
	}
}
