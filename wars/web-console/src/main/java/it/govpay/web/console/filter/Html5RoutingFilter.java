/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2024 Link.it srl (http://www.link.it).
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

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Html5RoutingFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Inizializzazione del filtro (se necessario)
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // Verifica se la richiesta è per un file o una directory esistente
        String path = httpRequest.getRequestURI().substring(httpRequest.getContextPath().length());
        String queryString = httpRequest.getQueryString();
        if (!path.contains(".")) {
        	 // Se la richiesta non contiene un'estensione, reindirizza al percorso HTML5 compatibile con l'app Angular
            String redirectPath = httpRequest.getContextPath() + "/#" + path;
            if (queryString != null && !queryString.isEmpty()) {
                redirectPath += "?" + queryString; // Aggiungi la query string al percorso di reindirizzamento
            }
            httpResponse.sendRedirect(redirectPath);
        } else {
            // Altrimenti, continua il normale flusso di richiesta
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {
        // Operazioni di chiusura (se necessario)
    }
}
