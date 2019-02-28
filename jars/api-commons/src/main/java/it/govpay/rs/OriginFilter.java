package it.govpay.rs;

import java.io.IOException;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;

public class OriginFilter implements javax.servlet.Filter {
    @Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse res = (HttpServletResponse) response;
        res.addHeader("Access-Control-Allow-Origin", "*");
        res.addHeader("Access-Control-Allow-Methods", "OPTIONS, GET, PUT, POST, PATCH, DELETE");
        res.addHeader("Access-Control-Allow-Credentials", "true");
        res.addHeader("Access-Control-Allow-Headers", "origin, content-type, accept, authorization");
        res.addHeader("Access-Control-Expose-Headers", "content-type, content-disposition");
        chain.doFilter(request, response);
    }

    @Override
	public void destroy() {}

    @Override
	public void init(FilterConfig filterConfig) throws ServletException {}
}
