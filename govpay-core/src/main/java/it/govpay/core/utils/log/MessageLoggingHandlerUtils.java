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

import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.UriInfo;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import org.apache.commons.lang.ArrayUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.openspcoop2.utils.logger.beans.Message;
import org.openspcoop2.utils.logger.beans.Property;
import org.openspcoop2.utils.logger.constants.MessageType;

import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.GpThreadLocal;

public class MessageLoggingHandlerUtils {

	Logger log = LogManager.getLogger();

	public void logToSystemOut(boolean outboundProperty, Map<String, List<String>> httpHeaders, byte[] msg) {
		if(log.getLevel().compareTo(Level.DEBUG) > 0) {
	
			StringBuffer sb = new StringBuffer();
	
			try {
				if(httpHeaders != null) {
					sb.append("\n\tHTTP Headers: ");
					for(String headerName : httpHeaders.keySet()) {
						List<String> values = httpHeaders.get(headerName);
						for(String value : values) {
							sb.append("\n\t" + headerName + ": " + value);
						}
					}
				}
				log.trace(sb.toString() + "\n" + new String(msg));
			} catch (Exception e) {
				log.error("Exception in handler: " + e);
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public static boolean logToSystemOut(SOAPMessageContext smc, String tipoServizio, int versioneServizio, Logger log) {
		Boolean outboundProperty = (Boolean) smc.get (MessageContext.MESSAGE_OUTBOUND_PROPERTY);
		
		GpContext ctx = null;
		Message msg = new Message();
		
		SOAPMessage message = smc.getMessage();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();  
		try {
			message.writeTo(baos);  
			msg.setContent(baos.toByteArray());
		} catch (Exception e) {
			log.error("Exception in handler: " + e);
		}
		
		Map<String, List<String>> httpHeaders = null;
		
		if (outboundProperty.booleanValue()) {
			ctx = GpThreadLocal.get();
			httpHeaders = (Map<String, List<String>>) smc.get(MessageContext.HTTP_RESPONSE_HEADERS);
			msg.setType(MessageType.RESPONSE_OUT);
			ctx.getContext().getResponse().setOutDate(new Date());
			ctx.getContext().getResponse().setOutSize(Long.valueOf(baos.size()));
		} else {
			try {
				ctx = new GpContext(smc, tipoServizio, versioneServizio);
				ThreadContext.put("op", ctx.getTransactionId());
				GpThreadLocal.set(ctx);
			} catch (Exception e) {
				log.error(e.getMessage(),e);
				return false;
			}
			httpHeaders = (Map<String, List<String>>) smc.get(MessageContext.HTTP_REQUEST_HEADERS);
			msg.setType(MessageType.REQUEST_IN);
			msg.setContentType(((HttpServletRequest) smc.get(MessageContext.SERVLET_REQUEST)).getContentType());
			
			ctx.getContext().getRequest().setInDate(new Date());
			ctx.getContext().getRequest().setInSize(Long.valueOf(baos.size()));
		}
		
		if(httpHeaders != null) {
			for(String key : httpHeaders.keySet()) {
				if(httpHeaders.get(key) != null) {
					if(key == null)
						msg.addHeader(new Property("Status-line", httpHeaders.get(key).get(0)));
					else if(httpHeaders.get(key).size() == 1)
						msg.addHeader(new Property(key, httpHeaders.get(key).get(0)));
					else
						msg.addHeader(new Property(key, ArrayUtils.toString(httpHeaders.get(key))));
				}
			}
		}


		
		ctx.log(msg);
		
		return true;
	}
	
	public static boolean logToSystemOut(UriInfo uriInfo, HttpHeaders rsHttpHeaders,HttpServletRequest request, ByteArrayOutputStream baos,
			String nomeOperazione, String nomeServizio,	String tipoServizio, int versioneServizio, Logger log, boolean outbound) {
		return logToSystemOut(uriInfo, rsHttpHeaders, request, baos.toByteArray(), nomeOperazione, nomeServizio, tipoServizio, versioneServizio, log, outbound);
	}
	
	public static boolean logToSystemOut(UriInfo uriInfo, HttpHeaders rsHttpHeaders,HttpServletRequest request, byte[] bytes,
			String nomeOperazione, String nomeServizio,	String tipoServizio, int versioneServizio, Logger log, boolean outbound) {
		return logToSystemOut(uriInfo, rsHttpHeaders, request, bytes, nomeOperazione, nomeServizio, tipoServizio, versioneServizio, log, outbound, null);
	}
	
	public static boolean logToSystemOut(UriInfo uriInfo, HttpHeaders rsHttpHeaders,HttpServletRequest request, byte[] bytes,
			String nomeOperazione, String nomeServizio,	String tipoServizio, int versioneServizio, Logger log, boolean outbound, Integer responseCode) {
	

		
		GpContext ctx = null;
		Message msg = new Message();
		
		try {
			msg.setContent(bytes);
		} catch (Exception e) {
			log.error("Exception in handler: " + e);
		}
		
		Map<String, List<String>> httpHeaders = null;
		
		if (outbound) {
			ctx = GpThreadLocal.get();
			//httpHeaders = (Map<String, List<String>>) smc.get(MessageContext.HTTP_RESPONSE_HEADERS);
			msg.setType(MessageType.RESPONSE_OUT);
			if(rsHttpHeaders.getMediaType() != null)
				msg.setContentType(rsHttpHeaders.getMediaType().getType() + "/" + rsHttpHeaders.getMediaType().getSubtype());
			ctx.getContext().getResponse().setOutDate(new Date());
			ctx.getContext().getResponse().setOutSize(Long.valueOf(bytes.length));
			if(responseCode != null) ctx.getTransaction().getServer().setTransportCode(responseCode.intValue() + "");
		} else {
			try {
				ctx = new GpContext(uriInfo,rsHttpHeaders, request, nomeOperazione, nomeServizio, tipoServizio, versioneServizio);
				ThreadContext.put("op", ctx.getTransactionId());
				GpThreadLocal.set(ctx);
			} catch (Exception e) {
				log.error(e.getMessage(),e);
				return false;
			}
			httpHeaders = rsHttpHeaders.getRequestHeaders();
			msg.setType(MessageType.REQUEST_IN);
			if(rsHttpHeaders.getMediaType() != null)
				msg.setContentType(rsHttpHeaders.getMediaType().getType() + "/" + rsHttpHeaders.getMediaType().getSubtype());
			ctx.getContext().getRequest().setInDate(new Date());
			ctx.getContext().getRequest().setInSize(Long.valueOf(bytes.length));
		}
		
		if(httpHeaders != null) {
			for(String key : httpHeaders.keySet()) {
				if(httpHeaders.get(key) != null) {
					if(key == null)
						msg.addHeader(new Property("Status-line", httpHeaders.get(key).get(0)));
					else if(httpHeaders.get(key).size() == 1)
						msg.addHeader(new Property(key, httpHeaders.get(key).get(0)));
					else
						msg.addHeader(new Property(key, ArrayUtils.toString(httpHeaders.get(key))));
				}
			}
		}


		
		ctx.log(msg);
		
		return true;
	}
}








