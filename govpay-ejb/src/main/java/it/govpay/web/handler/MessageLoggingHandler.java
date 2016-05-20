/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2016 Link.it srl (http://www.link.it).
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
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
package it.govpay.web.handler;

import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import org.apache.commons.lang.ArrayUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.openspcoop2.utils.logger.beans.Message;
import org.openspcoop2.utils.logger.beans.Property;
import org.openspcoop2.utils.logger.constants.MessageType;

import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.GpThreadLocal;

public class MessageLoggingHandler implements SOAPHandler<SOAPMessageContext> {

	private static Logger log = LogManager.getLogger();
	
	public Set<QName> getHeaders() {
		return null;
	}

	public boolean handleMessage(SOAPMessageContext smc) {
		return logToSystemOut(smc);
	}

	public boolean handleFault(SOAPMessageContext smc) {
		return logToSystemOut(smc);
	}

	public void close(MessageContext messageContext) {
	}


	@SuppressWarnings("unchecked")
	private boolean logToSystemOut(SOAPMessageContext smc) {
		
		Boolean outboundProperty = (Boolean)
				smc.get (MessageContext.MESSAGE_OUTBOUND_PROPERTY);
		
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
				ctx = new GpContext(smc);
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
}








