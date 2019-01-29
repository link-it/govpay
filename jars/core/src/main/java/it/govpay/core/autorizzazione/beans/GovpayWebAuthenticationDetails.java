package it.govpay.core.autorizzazione.beans;

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.web.authentication.WebAuthenticationDetails;

import it.govpay.core.utils.GovpayConfig;

/***
 * Estrae  dalla request tutti gli headers indicati nella lista.
 * 
 * 
 * @author pintori
 *
 */
public class GovpayWebAuthenticationDetails extends WebAuthenticationDetails {
	
	private Map<String, List<String>> headerValues = new HashMap<>();

	public GovpayWebAuthenticationDetails(HttpServletRequest request) {
		super(request);
		
		this.headerValues = extractHeaders(request);
	}

	private static final long serialVersionUID = 1L;

	
	private Map<String, List<String>> extractHeaders(HttpServletRequest request) {
		List<String> headersNames = GovpayConfig.getInstance().getElencoHeadersRequest();
		
		Map<String, List<String>> headerValues = new HashMap<>();
		for (String headerName : headersNames) {
			Enumeration<String> headers = request.getHeaders(headerName);
			headerValues.put(headerName, Collections.list(headers)); 
		}
		return headerValues;
	}

	public Map<String, List<String>> getHeaderValues() {
		return headerValues;
	}
}
