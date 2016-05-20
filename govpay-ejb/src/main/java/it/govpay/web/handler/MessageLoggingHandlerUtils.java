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
package it.govpay.web.handler;

import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MessageLoggingHandlerUtils {

	Logger log = LogManager.getLogger();

	public void logToSystemOut(boolean outboundProperty, Map<String, List<String>> httpHeaders, byte[] msg) {
		if(log.getLevel().compareTo(Level.DEBUG) > 0) {
	
			StringBuffer sb = new StringBuffer();
	
			try {
				if(httpHeaders != null) {
					sb.append("\n\tHTTP Headers: ");
					for(String headerName : httpHeaders.keySet()) {
						List<String> values = httpHeaders.get(headerName);
						for(String value : values) {
							sb.append("\n\t" + headerName + ": " + value);
						}
					}
				}
				log.trace(sb.toString() + "\n" + new String(msg));
			} catch (Exception e) {
				log.error("Exception in handler: " + e);
			}
		}
	}
}








