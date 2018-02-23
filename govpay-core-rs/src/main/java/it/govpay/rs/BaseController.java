/**
 * 
 */
package it.govpay.rs;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.UriInfo;

import org.codehaus.jackson.map.ObjectMapper;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.slf4j.Logger;
import org.slf4j.MDC;

import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.GpThreadLocal;
import it.govpay.core.utils.log.MessageLoggingHandlerUtils;

/**
 * @author Bussu Giovanni (bussu@link.it)
 * @author  $Author: bussu $
 * @version $ Rev: 12563 $, $Date: 22 feb 2018 $
 * 
 */
public abstract class BaseController {

	protected Logger log;
	protected String nomeServizio;
	protected HttpServletRequest request;
	protected HttpServletResponse response;
	
	public BaseController(String nomeServizio, Logger log) {
		this.log = log;
		this.nomeServizio = nomeServizio;
	}
	
	public void setRequestResponse(HttpServletRequest request,HttpServletResponse response) {
		this.response = response;
		this.request = request;
	}
	
	public HttpServletRequest getRequest() {
		return this.request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public HttpServletResponse getResponse() {
		return this.response;
	}

	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}

	public int getVersione() {
		return 1;
	}
	
	public void setupContext(UriInfo uriInfo, HttpHeaders rsHttpHeaders,String nomeOperazione) throws ServiceException {
		GpContext ctx = new GpContext(uriInfo,rsHttpHeaders, this.request, nomeOperazione, this.nomeServizio, GpContext.TIPO_SERVIZIO_GOVPAY_JSON, this.getVersione());
		MDC.put("op", ctx.getTransactionId());
		GpThreadLocal.set(ctx);
	}
	
	public void logResponse(UriInfo uriInfo, HttpHeaders rsHttpHeaders, String nomeOperazione, Object o) throws IOException {
		this.logResponse(uriInfo, rsHttpHeaders, nomeOperazione, o, null);
	}
	

	public void logResponse(UriInfo uriInfo, HttpHeaders rsHttpHeaders, String nomeOperazione, Object o, Integer responseCode) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(o);
		this.logResponse(uriInfo, rsHttpHeaders, nomeOperazione, json.getBytes(), responseCode);
	}
	
	public void logRequest(UriInfo uriInfo, HttpHeaders rsHttpHeaders,String nomeOperazione, ByteArrayOutputStream baos) {
		MessageLoggingHandlerUtils.logToSystemOut(uriInfo, rsHttpHeaders, this.request,baos,
				nomeOperazione, this.nomeServizio, GpContext.TIPO_SERVIZIO_GOVPAY_JSON, this.getVersione(), this.log, false);
	}
	
	public void logRequest(UriInfo uriInfo, HttpHeaders rsHttpHeaders,String nomeOperazione, byte[] baos) {
		MessageLoggingHandlerUtils.logToSystemOut(uriInfo, rsHttpHeaders, this.request,baos,
				nomeOperazione, this.nomeServizio, GpContext.TIPO_SERVIZIO_GOVPAY_JSON, this.getVersione(), this.log, false);
	}

	public void logResponse(UriInfo uriInfo, HttpHeaders rsHttpHeaders,String nomeOperazione, ByteArrayOutputStream baos) {
		MessageLoggingHandlerUtils.logToSystemOut(uriInfo, rsHttpHeaders, this.request,baos,
				nomeOperazione, this.nomeServizio, GpContext.TIPO_SERVIZIO_GOVPAY_JSON, this.getVersione(), this.log, true);
	}
	
	public void logResponse(UriInfo uriInfo, HttpHeaders rsHttpHeaders,String nomeOperazione,byte[] bytes) {
		MessageLoggingHandlerUtils.logToSystemOut(uriInfo, rsHttpHeaders, this.request,bytes,
				nomeOperazione, this.nomeServizio, GpContext.TIPO_SERVIZIO_GOVPAY_JSON, this.getVersione(), this.log, true);
	}
	
	public void logResponse(UriInfo uriInfo, HttpHeaders rsHttpHeaders,String nomeOperazione,byte[] bytes, Integer responseCode) {
		MessageLoggingHandlerUtils.logToSystemOut(uriInfo, rsHttpHeaders, this.request,bytes,
				nomeOperazione, this.nomeServizio, GpContext.TIPO_SERVIZIO_GOVPAY_JSON, this.getVersione(), this.log, true, responseCode);
	}
}
