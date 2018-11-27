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

public class NdpUtils {

	public static String toCausaString(it.gov.digitpa.schemas._2011.ws.nodo.FaultBean fb) {
		if(fb == null) {
			return "[-- FaultBean vuoto --]";
		}
		return toCausaString(fb.getFaultCode(), fb.getFaultString(), fb.getId(), fb.getSerial(), fb.getDescription());
	}
	
	public static String toCausaString(it.gov.digitpa.schemas._2011.ws.paa.FaultBean fb) {
		if(fb == null) {
			return "[-- FaultBean vuoto --]";
		}
		return toCausaString(fb.getFaultCode(), fb.getFaultString(), fb.getId(), fb.getSerial(), fb.getDescription());
	}
	
	public static String toCausaString(it.gov.digitpa.schemas._2011.ws.psp.FaultBean fb) {
		if(fb == null) {
			return "[-- FaultBean vuoto --]";
		}
		return toCausaString(fb.getFaultCode(), fb.getFaultString(), fb.getId(), fb.getSerial(), fb.getDescription());
	}
	
	private static String toCausaString(String faultCode, String faultString, String id, Integer serial, String description) {
		StringBuffer sb = new StringBuffer();
		
		String serialString = "";
		if(serial != null) {
			serialString = " Serial:" + serial;
		}
		
		sb.append("[Id: " + id + " Code:" + faultCode + serialString + "] " + faultString);

		if(description != null) {
			sb.append("\n" + description);
		}
		
		return sb.toString();
	}
}
