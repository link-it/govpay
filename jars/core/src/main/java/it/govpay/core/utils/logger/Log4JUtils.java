/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2022 Link.it srl (http://www.link.it).
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
package it.govpay.core.utils.logger;

import java.io.File;
import java.net.URI;
import java.net.URL;

import org.openspcoop2.utils.LoggerWrapperFactory;
import org.openspcoop2.utils.UtilsException;
import org.slf4j.Logger;

import it.govpay.core.utils.GovpayConfig;

/***
 * 
 * @author pintori
 *
 */
public class Log4JUtils {

	private static Logger log = LoggerWrapperFactory.getLogger(Log4JUtils.class);
	private static URL log4j2XmlFile;

	public static synchronized void initialize(URL log4j2XmlFile) {
		Log4JUtils.log4j2XmlFile = log4j2XmlFile;
	}

	public static synchronized void reloadLog4j() {

		GovpayConfig gpConfig = GovpayConfig.getInstance();

		try {
			// controllo che sia stato impostato un nuovo file esterno di log4j2 
			gpConfig.leggiFileEsternoLog4j2();
			
			// leggo l'eventuale path al file
			URI log4j2Config = gpConfig.getLog4j2Config();
			
			if(log4j2Config != null) {
				LoggerWrapperFactory.setLogConfiguration(new File(log4j2Config));
				log.info("Caricata configurazione di log4j: " + log4j2Config.toString());
			} else {
				LoggerWrapperFactory.setLogConfiguration(log4j2XmlFile);
				log.info("Caricata configurazione di log4j: " + log4j2XmlFile.toString());
			}
		} catch (Throwable e) { 
			log.error("Errore durante il reload del log4j: " + e.getMessage(), e);
			
			try {
				LoggerWrapperFactory.setLogConfiguration(log4j2XmlFile);
				log.info("La configurazione esterna indicata non e' valida, caricata configurazione default log4j: " + log4j2XmlFile.toString());
			} catch (UtilsException e1) {
				log.error("Errore durante il reload del log4j: " + e1.getMessage(), e1);
			}
		} 
	}
}
