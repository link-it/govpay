package it.govpay.rs.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.openspcoop2.utils.LoggerWrapperFactory;
import org.slf4j.Logger;

import it.govpay.core.cache.RuoliCache;

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
		
		request.getUserPrincipal()
	}

	@Override
	public void destroy() {
		
	}

}
