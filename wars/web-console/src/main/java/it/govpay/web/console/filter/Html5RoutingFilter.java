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
