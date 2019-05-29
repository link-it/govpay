/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2017 Link.it srl (http://www.link.it).
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
package it.govpay.core.utils.log;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPException;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.UtilsException;
import org.openspcoop2.utils.service.context.ContextThreadLocal;
import org.openspcoop2.utils.service.context.IContext;
import org.openspcoop2.utils.service.context.MD5Constants;
import org.slf4j.Logger;
import org.slf4j.MDC;

import it.govpay.core.autorizzazione.utils.AutorizzazioneUtils;
import it.govpay.core.utils.EventoContext.Categoria;
import it.govpay.core.utils.GpContext;

public class MessageLoggingHandlerUtils {

	private static final String SOAP_ACTION = "SOAPAction";

	@SuppressWarnings("unchecked")
	public static boolean handleMessage(SOAPMessageContext smc, String tipoServizio, int versioneServizio, Logger log) throws UtilsException, ServiceException {
		Boolean outboundProperty = (Boolean) smc.get (MessageContext.MESSAGE_OUTBOUND_PROPERTY);

		IContext context = ContextThreadLocal.get();
		if(context instanceof org.openspcoop2.utils.service.context.Context) {
			GpContext ctx = (GpContext) ((org.openspcoop2.utils.service.context.Context)context).getApplicationContext();
			if (outboundProperty.booleanValue()) {
				try { 
					smc.getMessage().setProperty("X-GP-CMDID", MDC.get(MD5Constants.OPERATION_ID));
				} catch (SOAPException e) {
					log.warn("Impossibile impostare l'header HTTP X-GP-CMDID nella risposta.");
				}

			} else {
				if(smc.get(MessageContext.WSDL_OPERATION) != null)
	    			MDC.put(MD5Constants.OPERATION_ID, ((QName) smc.get(MessageContext.WSDL_OPERATION)).getLocalPart());
				MDC.put(MD5Constants.TRANSACTION_ID, context.getTransactionId());
				GpContext.popolaGpContext(ctx, smc, tipoServizio, versioneServizio);
				
				
				Map<String, List<String>> httpHeaders = (Map<String, List<String>>) smc.get(MessageContext.HTTP_REQUEST_HEADERS);
				String soapAction = "";
				if(httpHeaders.get(SOAP_ACTION) != null) {
					soapAction = httpHeaders.get(SOAP_ACTION).get(0);
					soapAction = soapAction.replace("\"",""); //.split("#"))[1]);
				}
				
				HttpServletRequest servletRequest = (HttpServletRequest) smc.get(MessageContext.SERVLET_REQUEST);
				ctx.getEventoCtx().setCategoriaEvento(Categoria.INTERFACCIA);
				ctx.getEventoCtx().setMethod(servletRequest.getMethod());
				ctx.getEventoCtx().setTipoEvento(soapAction);
				ctx.getEventoCtx().setPrincipal(AutorizzazioneUtils.getPrincipal(context.getAuthentication()));
				if(AutorizzazioneUtils.getAuthenticationDetails(context.getAuthentication()) != null)
					ctx.getEventoCtx().setUtente(AutorizzazioneUtils.getAuthenticationDetails(context.getAuthentication()).getIdentificativo());
				
				StringBuilder requestURL = new StringBuilder(servletRequest.getRequestURL().toString());
			    String queryString = servletRequest.getQueryString();

			    if (queryString == null) {
			    	ctx.getEventoCtx().setUrl(requestURL.toString());
			    } else {
			    	ctx.getEventoCtx().setUrl(requestURL.append('?').append(queryString).toString());
			    }
			}
		}
		return true;
	}



//	@SuppressWarnings("unchecked")
//	public static boolean logToSystemOut(SOAPMessageContext smc, String tipoServizio, int versioneServizio, Logger log) throws UtilsException {
//		Boolean outboundProperty = (Boolean) smc.get (MessageContext.MESSAGE_OUTBOUND_PROPERTY);
//
//		IContext ctx = null;
//		Message msg = new Message();
//
//		SOAPMessage message = smc.getMessage();
//		ByteArrayOutputStream baos = new ByteArrayOutputStream();  
//		try {
//			message.writeTo(baos);  
//			msg.setContent(baos.toByteArray());
//		} catch (Exception e) {
//			log.error("Exception in handler: " + e);
//		}
//
//		Map<String, List<String>> httpHeaders = null;
//
//		ApplicationContext appContext = null;
//		if (outboundProperty.booleanValue()) {
//			ctx = ContextThreadLocal.get();
//			httpHeaders = (Map<String, List<String>>) smc.get(MessageContext.HTTP_RESPONSE_HEADERS);
//			msg.setType(MessageType.RESPONSE_OUT);
//			appContext = (ApplicationContext) ctx.getApplicationContext();
//			appContext.getResponse().setDate(new Date());
//			appContext.getResponse().setSize(Long.valueOf(baos.size()));
//		} else {
//			try {
//				GpContextFactory factory = new GpContextFactory();
//				ctx = factory.newContext(smc, tipoServizio, versioneServizio);
//				appContext = (ApplicationContext) ctx.getApplicationContext();
//				MDC.put(MD5Constants.TRANSACTION_ID, ctx.getTransactionId());
//				ContextThreadLocal.set(ctx);
//			} catch (Exception e) {
//				log.error(e.getMessage(),e);
//				return false;
//			}
//			httpHeaders = (Map<String, List<String>>) smc.get(MessageContext.HTTP_REQUEST_HEADERS);
//			msg.setType(MessageType.REQUEST_IN);
//			msg.setContentType(((HttpServletRequest) smc.get(MessageContext.SERVLET_REQUEST)).getContentType());
//
//			appContext.getRequest().setDate(new Date());
//			appContext.getRequest().setSize(Long.valueOf(baos.size()));
//		}
//
//		if(httpHeaders != null) {
//			for(String key : httpHeaders.keySet()) {
//				if(httpHeaders.get(key) != null) {
//					if(key == null)
//						msg.addHeader(new Property("Status-line", httpHeaders.get(key).get(0)));
//					else if(httpHeaders.get(key).size() == 1)
//						msg.addHeader(new Property(key, httpHeaders.get(key).get(0)));
//					else
//						msg.addHeader(new Property(key, ArrayUtils.toString(httpHeaders.get(key))));
//				}
//			}
//		}
//
//
//
//		ctx.getApplicationLogger().log(msg);
//
//		return true;
//	}

	//	public static boolean logToSystemOut(UriInfo uriInfo, HttpHeaders rsHttpHeaders,HttpServletRequest request, ByteArrayOutputStream baos,
	//			String nomeOperazione, String nomeServizio,	String tipoServizio, int versioneServizio, Logger log, boolean outbound) throws UtilsException {
	//		return logToSystemOut(uriInfo, rsHttpHeaders, request, baos.toByteArray(), nomeOperazione, nomeServizio, tipoServizio, versioneServizio, log, outbound);
	//	}

	//	public static boolean logToSystemOut(UriInfo uriInfo, HttpHeaders rsHttpHeaders,HttpServletRequest request, byte[] bytes,
	//			String nomeOperazione, String nomeServizio,	String tipoServizio, int versioneServizio, Logger log, boolean outbound) throws UtilsException {
	//		return logToSystemOut(uriInfo, rsHttpHeaders, request, bytes, nomeOperazione, nomeServizio, tipoServizio, versioneServizio, log, outbound, null);
	//	}

	//	public static boolean logToSystemOut(UriInfo uriInfo, HttpHeaders rsHttpHeaders,HttpServletRequest request, byte[] bytes,
	//			String nomeOperazione, String nomeServizio,	String tipoServizio, int versioneServizio, Logger log, boolean outbound, Integer responseCode) throws UtilsException {
	//	
	//
	//		
	//		IContext ctx = null;
	//		Message msg = new Message();
	//		
	//		try {
	//			msg.setContent(bytes);
	//		} catch (Exception e) {
	//			log.error("Exception in handler: " + e);
	//		}
	//		
	//		Map<String, List<String>> httpHeaders = null;
	//		HttpRequestMethod requestMethod = HttpRequestMethod.valueOf(request.getMethod()); 
	//		
	//		String baseUri = uriInfo.getBaseUri().toString();
	//		String requestUri = uriInfo.getRequestUri().toString();
	//		int idxOfBaseUri = requestUri.indexOf(baseUri);
	//		
	//		String servicePathwithParameters = requestUri.substring((idxOfBaseUri + baseUri.length()) - 1);
	//		
	//		msg.addHeader(new Property("HTTP-Method", requestMethod.toString()));
	//		msg.addHeader(new Property("RequestPath", servicePathwithParameters));
	//		ApplicationContext appContext = null;
	//		if (outbound) {
	//			ctx = ContextThreadLocal.get();
	//			//httpHeaders = (Map<String, List<String>>) smc.get(MessageContext.HTTP_RESPONSE_HEADERS);
	//			msg.setType(MessageType.RESPONSE_OUT);
	//			if(rsHttpHeaders.getMediaType() != null)
	//				msg.setContentType(rsHttpHeaders.getMediaType().getType() + "/" + rsHttpHeaders.getMediaType().getSubtype());
	//			
	//			appContext = (ApplicationContext) ctx.getApplicationContext();
	//			appContext.getResponse().setDate(new Date());
	//			appContext.getResponse().setSize(Long.valueOf(bytes.length));
	//			if(responseCode != null) {
	//				ApplicationTransaction appTransaction = (ApplicationTransaction) appContext.getTransaction();
	//				BaseServer server = (appTransaction.getServers() != null && !appTransaction.getServers().isEmpty() )? appTransaction.getServers().get(0) : null;
	//				
	//				if(server == null) {
	//					server = new HttpServer();
	//					server.setName(GpContext.GovPay);
	//					appTransaction.addServer(server);
	//				}
	//				
	//				if(server instanceof HttpServer && responseCode != null) {
	//					((HttpServer) server).setResponseStatusCode(responseCode.intValue());
	//				}
	//			}
	//			
	//			if((responseCode != null && responseCode.intValue() < 299) && HttpRequestMethod.GET.equals(requestMethod) && !GovpayConfig.getInstance().isDumpAPIRestGETResponse()) {
	//				msg.setContent(null);
	//			}
	//			
	//			msg.addHeader(new Property(Costanti.HEADER_NAME_OUTPUT_TRANSACTION_ID, ctx.getTransactionId()));
	//		} else {
	//			try {
	//				GpContextFactory factory = new GpContextFactory();
	//				ctx = factory.newContext(uriInfo,rsHttpHeaders, request, nomeOperazione, nomeServizio, tipoServizio, versioneServizio);
	//				appContext = (ApplicationContext) ctx.getApplicationContext();
	//				MDC.put(MD5Constants.TRANSACTION_ID, ctx.getTransactionId());
	//				ContextThreadLocal.set(ctx);
	//			} catch (Exception e) {
	//				log.error(e.getMessage(),e);
	//				return false;
	//			}
	//			httpHeaders = rsHttpHeaders.getRequestHeaders();
	//			msg.setType(MessageType.REQUEST_IN);
	//			if(rsHttpHeaders.getMediaType() != null)
	//				msg.setContentType(rsHttpHeaders.getMediaType().getType() + "/" + rsHttpHeaders.getMediaType().getSubtype());
	//			appContext.getRequest().setDate(new Date());
	//			appContext.getRequest().setSize(Long.valueOf(bytes.length));
	//			
	//
	//		}
	//		
	//		if(httpHeaders != null) {
	//			for(String key : httpHeaders.keySet()) {
	//				if(httpHeaders.get(key) != null) {
	//					if(key == null)
	//						msg.addHeader(new Property("Status-line", httpHeaders.get(key).get(0)));
	//					else if(httpHeaders.get(key).size() == 1)
	//						msg.addHeader(new Property(key, httpHeaders.get(key).get(0)));
	//					else
	//						msg.addHeader(new Property(key, ArrayUtils.toString(httpHeaders.get(key))));
	//				}
	//			}
	//		}
	//
	//		ctx.getApplicationLogger().log(msg);
	//		
	//		return true;
	//	}
}








