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
package it.govpay.utils;

import it.govpay.exception.GovPayException;

import java.util.Properties;

import org.openspcoop2.utils.UtilsException;
import org.openspcoop2.utils.logger.LoggerFactory;

public class LoggerConfiguration {

	private static final String msgDiagProperties = "example.msgDiagnostici.properties";
	private static boolean initialized;
	
	public static void init() throws GovPayException, UtilsException, ClassNotFoundException {
		if(!initialized) {
			ConfigurationUtils msgDiagConfiguration = new ConfigurationUtils(msgDiagProperties);

			Properties msgDiagProperties = msgDiagConfiguration.getProperties();

			LoggerFactory.initialize(Log4J2Logger.class,
					msgDiagProperties,
					true,
					new Properties());
			
			initialized = true;
		}
	}
}
