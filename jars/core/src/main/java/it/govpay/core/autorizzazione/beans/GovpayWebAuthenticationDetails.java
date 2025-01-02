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
package it.govpay.core.autorizzazione.beans;

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import it.govpay.core.utils.LogUtils;

/***
 * Estrae  dalla request tutti gli headers indicati nella lista.
 * 
 * 
 * @author pintori
 *
 */
public class GovpayWebAuthenticationDetails extends WebAuthenticationDetails {

	private Map<String, List<String>> headerValues = new HashMap<>();
	
	private Map<String, Object> attributesValues = new HashMap<>();

	public GovpayWebAuthenticationDetails(Logger log, HttpServletRequest request,List<String> headersNames) {
		this(log, request, headersNames, false);
	}

	public GovpayWebAuthenticationDetails(Logger log, HttpServletRequest request,Map<String,String> headersMap) {
		this(log, request, headersMap, false);
	}

	public GovpayWebAuthenticationDetails(Logger log, HttpServletRequest request,List<String> headersNames, boolean readFromSession) {
		super(request);

		if(readFromSession) {
			this.attributesValues = extractAttributes(log, request,headersNames);
		} else {
			this.headerValues = extractHeaders(log, request,headersNames);
		}
	}

	public GovpayWebAuthenticationDetails(Logger log, HttpServletRequest request,Map<String,String> headersMap, boolean readFromSession) {
		super(request);

		if(readFromSession) {
			this.attributesValues = extractAttributes(log, request,headersMap);
		} else {
			this.headerValues = extractHeaders(log, request,headersMap);
		}
	}

	private static final long serialVersionUID = 1L;


	private Map<String, List<String>> extractHeaders(Logger log, HttpServletRequest request, List<String> headersNames) {
		Map<String, List<String>> headerValuesMap = new HashMap<>();
		for (String headerName : headersNames) {
			Enumeration<String> headers = request.getHeaders(headerName);
			List<String> list = Collections.list(headers);
			LogUtils.logDebug(log, "Header: ["+headerName+"] Valore ["+StringUtils.join(list, ",")+"]");
			headerValuesMap.put(headerName, list); 
		}
		return headerValuesMap;
	}

	private Map<String, Object> extractAttributes(Logger log, HttpServletRequest request, List<String> headersNames) {
		Map<String, Object> headerValuesMap = new HashMap<>();

		HttpSession session = request.getSession(false);
		if(session != null) {
			for (String headerName : headersNames) {
				Object attribute = session.getAttribute(headerName);
				LogUtils.logDebug(log, "Attributo: ["+headerName+"] Valore ["+attribute+"]");
				
				if(attribute != null) {
					headerValuesMap.put(headerName, attribute);
				} else {
					headerValuesMap.put(headerName, ObjectUtils.NULL);
				}
			}
		}
		return headerValuesMap;
	}

	private Map<String, List<String>> extractHeaders(Logger log, HttpServletRequest request, Map<String,String> headersMap) {
		Map<String, List<String>> headerValuesMap = new HashMap<>();
		// per ogni header previsto cerco l'header SPID corrispondente
		for (String headerName : headersMap.keySet()) {
			String nomeHeader = headersMap.get(headerName);
			Enumeration<String> headers = request.getHeaders(nomeHeader);
			List<String> list = Collections.list(headers);
			LogUtils.logDebug(log, "Proprieta': ["+headerName+"] Letta dall'header: ["+nomeHeader+"] contiene valore ["+StringUtils.join(list, ",")+"]");
			headerValuesMap.put(headerName, list); 
		}
		return headerValuesMap;
	}

	private Map<String, Object> extractAttributes(Logger log, HttpServletRequest request, Map<String,String> headersMap) {
		Map<String, Object> headerValuesMap = new HashMap<>();
		// per ogni header previsto cerco l'header SPID corrispondente
		HttpSession session = request.getSession(false);
		if(session != null) {
			for (String headerName : headersMap.keySet()) {
				String nomeHeader = headersMap.get(headerName);
				Object attribute = session.getAttribute(nomeHeader);
				LogUtils.logDebug(log, "Proprieta': ["+headerName+"] Letta dall'attributo: ["+nomeHeader+"] contiene valore [" + attribute+"]");
				
				if(attribute != null) {
					headerValuesMap.put(headerName, attribute);
				} else {
					headerValuesMap.put(headerName, ObjectUtils.NULL);
				}
			}
		}
		return headerValuesMap;
	}

	public Map<String, List<String>> getHeaderValues() {
		return headerValues;
	}

	public Map<String, Object> getAttributesValues() {
		return attributesValues;
	}
	
}
