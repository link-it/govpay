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
package it.govpay.web.console;

import java.awt.GraphicsEnvironment;
import java.util.Arrays;

import it.govpay.ejb.core.exception.GovPayException;
import it.govpay.ejb.core.exception.GovPayException.GovPayExceptionEnum;
import it.govpay.ejb.core.utils.rs.JaxbUtils;
import it.govpay.web.console.utils.GovpayWebConsoleConfiguration;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

import org.apache.logging.log4j.Logger;

@Startup
@Singleton
public class GovPayWebConsoleStartup {
	static{
		 System.setProperty("java.awt.headless", "true");
	}

	@Inject  
	private transient Logger log;

	@PostConstruct 
	void atStartup() throws GovPayException { 

		log.info("WebConsole: Caricamento delle configurazioni");
		try {
			GovpayWebConsoleConfiguration.newInstance();
			} catch (Exception e) {
				throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO, "Impossibile inizializzare la configurazione della console", e);
			}

		log.info("WebConsole: Inizializzazione dei componenti");
		try {
			JaxbUtils.init();
		} catch (Exception e) {
			throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO, "Impossibile inizializzare il Marshaller", e);
		}

		log.debug("Check Graphics Environment: is HeadeLess ["+java.awt.GraphicsEnvironment.isHeadless()+"]");
		
		log.debug("Elenco Nomi Font disponibili: " + Arrays.asList(GraphicsEnvironment
                .getLocalGraphicsEnvironment().getAvailableFontFamilyNames()));
		
		log.info("WebConsole: Start Completato" );
	}
}
