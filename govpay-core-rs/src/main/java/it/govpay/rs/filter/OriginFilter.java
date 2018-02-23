package it.govpay.rs.filter;

import java.io.IOException;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;

public class OriginFilter implements javax.servlet.Filter {
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse res = (HttpServletResponse) response;
        res.addHeader("Access-Control-Allow-Origin", "*");
        res.addHeader("Access-Control-Allow-Methods", "OPTIONS, GET, PUT, POST, PATCH");  // No DELETE
//      res.addHeader("Access-Control-Allow-Headers", "Content-Type");
        res.addHeader("Access-Control-Allow-Credentials", "true");
        res.addHeader("Access-Control-Allow-Headers", "origin, content-type, accept, authorization");
        chain.doFilter(request, response);
    }

    public void destroy() {}

    public void init(FilterConfig filterConfig) throws ServletException {}
}
