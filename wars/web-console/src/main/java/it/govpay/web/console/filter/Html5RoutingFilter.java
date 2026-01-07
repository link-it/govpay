/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2026 Link.it srl (http://www.link.it).
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
package it.govpay.web.console.filter;

import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class Html5RoutingFilter implements Filter {

	private static final String HEADER_NAME_X_FORWARDED_PREFIX = "X-Forwarded-Prefix";

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// Inizializzazione del filtro (se necessario)
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest  req  = (HttpServletRequest)  request;
		HttpServletResponse resp = (HttpServletResponse) response;

		// Informazioni impostate nel rewrite di Tomcat
		
		// (URI esterna prima del rewrite interno) ---
		String origUri = (String) req.getAttribute(RequestDispatcher.FORWARD_REQUEST_URI);
		
		// Se non c’è forward, prelevo dalla request
		if (origUri == null) {  
			origUri = req.getRequestURI(); 
		}
		
		// (Context esterno prima del rewrite interno) ---
		String origContext  = (String) req.getAttribute(RequestDispatcher.FORWARD_CONTEXT_PATH);
		// Se non c’è forward, prelevo dalla request
		if (origContext == null) {
			origContext = req.getContextPath();
		}
		
		// (QueryString esterna prima del rewrite interno) ---
		String origQuery = (String) req.getAttribute(RequestDispatcher.FORWARD_QUERY_STRING);
		// Se non c’è forward, prelevo dalla request
		if (origQuery == null || origQuery.isEmpty()) {
			origQuery = req.getQueryString();
		}

		// Informazioni impostate dal frontend apache.
		
		// Leggi X-Forwarded-Prefix (se Apache lo imposta) ---
		String xfPrefix = req.getHeader(HEADER_NAME_X_FORWARDED_PREFIX);

		// Calcolo path della risorsa richiesta
		String path = origUri.substring(origContext.length());

		// Se non è una risorsa statica o file (cioè non contiene un “.”)
		if (!path.contains(".")) {
			// Calcolo prefix da usare per il redirect
			String prefix = getRedirectPrefix(origUri, xfPrefix, path);

			// Costruisco il redirect finale
			String redirectPath =  getRedirectPath(prefix, path, origQuery);
			resp.sendRedirect(redirectPath);
		} else {
			chain.doFilter(request, response);
		}
	}

	private String getRedirectPrefix(String origUri, String xfPrefix, String path) {
		String prefix;
		// Scegli il prefix esterno:
		if (xfPrefix != null && !xfPrefix.isEmpty()) {
			prefix = xfPrefix;
		} else {
			// Parte di URI prima di ‘path’
			prefix = origUri.substring(0, origUri.length() - path.length());
		}
		return prefix;
	}
	
	private String getRedirectPath(String prefix, String path, String queryString) {
		StringBuilder redirect = new StringBuilder(prefix).append("/#").append(path);
		if (queryString != null && !queryString.isEmpty()) {
			redirect.append("?").append(queryString);
		}
		return redirect.toString();
	}

	@Override
	public void destroy() {
		// Operazioni di chiusura (se necessario)
	}
}
