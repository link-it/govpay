package it.govpay.core.autorizzazione.beans;

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.ObjectUtils;
import org.slf4j.Logger;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

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
		Map<String, List<String>> headerValues = new HashMap<>();
		for (String headerName : headersNames) {
			Enumeration<String> headers = request.getHeaders(headerName);
			log.debug("Header: ["+headerName+"] Valore ["+headers+"]");
			headerValues.put(headerName, Collections.list(headers)); 
		}
		return headerValues;
	}

	private Map<String, Object> extractAttributes(Logger log, HttpServletRequest request, List<String> headersNames) {
		Map<String, Object> headerValues = new HashMap<>();

		HttpSession session = request.getSession(false);
		if(session != null) {
			for (String headerName : headersNames) {
				Object attribute = session.getAttribute(headerName);
				log.debug("Attributo: ["+headerName+"] Valore ["+attribute+"]");
				
				if(attribute != null) {
					headerValues.put(headerName, attribute);
				} else {
					headerValues.put(headerName, ObjectUtils.NULL);
				}
			}
		}
		return headerValues;
	}

	private Map<String, List<String>> extractHeaders(Logger log, HttpServletRequest request, Map<String,String> headersMap) {
		Map<String, List<String>> headerValues = new HashMap<>();
		// per ogni header previsto cerco l'header SPID corrispondente
		for (String headerName : headersMap.keySet()) {
			Enumeration<String> headers = request.getHeaders(headersMap.get(headerName));
			log.debug("Header: ["+headersMap.get(headerName)+"] Valore ["+headers+"]");
			headerValues.put(headerName, Collections.list(headers)); 
		}
		return headerValues;
	}

	private Map<String, Object> extractAttributes(Logger log, HttpServletRequest request, Map<String,String> headersMap) {
		Map<String, Object> headerValues = new HashMap<>();
		// per ogni header previsto cerco l'header SPID corrispondente
		HttpSession session = request.getSession(false);
		if(session != null) {
			for (String headerName : headersMap.keySet()) {
				Object attribute = session.getAttribute(headersMap.get(headerName));
				log.debug("Attributo: ["+headerName+"] Valore ["+attribute+"]");
				
				if(attribute != null) {
					headerValues.put(headerName, attribute);
				} else {
					headerValues.put(headerName, ObjectUtils.NULL);
				}
			}
		}
		return headerValues;
	}

	public Map<String, List<String>> getHeaderValues() {
		return headerValues;
	}

	public Map<String, Object> getAttributesValues() {
		return attributesValues;
	}
	
}
