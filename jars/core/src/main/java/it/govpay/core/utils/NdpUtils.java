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
package it.govpay.core.utils;


public class NdpUtils {
	
	private static final String FAULT_BEAN_VUOTO = "[-- FaultBean vuoto --]";

	private NdpUtils() {
		// static only
	}

	public static String toCausaString(gov.telematici.pagamenti.ws.rpt.FaultBean fb) {
		if(fb == null) {
			return FAULT_BEAN_VUOTO;
		}
		return toCausaString(fb.getFaultCode(), fb.getFaultString(), fb.getId(), fb.getSerial(), fb.getDescription());
	}
	
	public static String toCausaString(gov.telematici.pagamenti.ws.rt.FaultBean fb) {
		if(fb == null) {
			return FAULT_BEAN_VUOTO;
		}
		return toCausaString(fb.getFaultCode(), fb.getFaultString(), fb.getId(), fb.getSerial(), fb.getDescription());
	}
	
	public static String toCausaString(gov.telematici.pagamenti.ws.ccp.FaultBean fb) {
		if(fb == null) {
			return FAULT_BEAN_VUOTO;
		}
		return toCausaString(fb.getFaultCode(), fb.getFaultString(), fb.getId(), fb.getSerial(), fb.getDescription());
	}
	
	private static String toCausaString(String faultCode, String faultString, String id, Integer serial, String description) {
		StringBuilder sb = new StringBuilder();
		
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
