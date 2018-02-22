package it.govpay.rs.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openspcoop2.utils.LoggerWrapperFactory;
import org.slf4j.Logger;

import it.govpay.core.cache.RuoliCache;
import it.govpay.model.Ruolo;

public class RuoliFilter implements Filter {
	
	private Logger log = LoggerWrapperFactory.getLogger(RuoliFilter.class);
	private RuoliCache ruoliCache = null; 

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		this.ruoliCache = RuoliCache.getInstance();
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		HttpServletRequest request = (HttpServletRequest) req;
		String principal = request.getUserPrincipal() != null ? request.getUserPrincipal().getName() : null;
		this.log.debug("Ricevuta richiesta per la url ["+request.getRequestURI()+"], Ricevuto Principal ["+principal+"]"); 
		
		if(principal == null) {
			((HttpServletResponse)response).setStatus(401);
			return;
		}
		
		this.log.debug("Controllo ruoli associati alla richiesta del principal ["+principal+"] in corso..."); 
		List<Ruolo> listaRuoliPosseduti = new ArrayList<Ruolo>();
		// caricamento dei ruoli ricevuti nella richiesta http
		for (String chiaveRuolo : this.ruoliCache.getChiavi()) {
			if(request.isUserInRole(chiaveRuolo)){
				listaRuoliPosseduti.add(this.ruoliCache.getRuolo(chiaveRuolo));
			}
		}
		
		this.log.debug("Controllo ruoli associati alla richiesta del principal ["+principal+"] completata, trovati ["+listaRuoliPosseduti.size()+"] ruoli.");
		
		// se non possiedi ruoli non sei autorizzato
		if(listaRuoliPosseduti.size() == 0) {
			((HttpServletResponse)response).setStatus(401);
			return;
		}
		
		// continue
		chain.doFilter(req, response);
	}

	@Override
	public void destroy() {
		
	}

}
