/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2015 Link.it srl (http://www.link.it).
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

import it.govpay.web.handler.utils.MessageLoggingHandlerUtils;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MessageLoggingHandler implements SOAPHandler<SOAPMessageContext> {

	private static Logger log = LogManager.getLogger();
	private MessageLoggingHandlerUtils messageLoggingHandlerUtils;
	
	public MessageLoggingHandler() {
		this.messageLoggingHandlerUtils = new MessageLoggingHandlerUtils();
	}
	public Set<QName> getHeaders() {
		return null;
	}

	public boolean handleMessage(SOAPMessageContext smc) {
		if(log.getLevel().compareTo(Level.DEBUG) > 0)
			logToSystemOut(smc);
		return true;
	}

	public boolean handleFault(SOAPMessageContext smc) {
		if(log.getLevel().compareTo(Level.DEBUG) > 0)
			logToSystemOut(smc);
		return true;
	}

	public void close(MessageContext messageContext) {
	}


	@SuppressWarnings("unchecked")
	private void logToSystemOut(SOAPMessageContext smc) {

		Boolean outboundProperty = (Boolean)
				smc.get (MessageContext.MESSAGE_OUTBOUND_PROPERTY);
		
		StringBuffer sb = new StringBuffer();
		Map<String, List<String>> httpHeaders = null;
		if (outboundProperty.booleanValue()) {
			sb.append("Outbound message: ");
			httpHeaders = (Map<String, List<String>>) smc.get(MessageContext.HTTP_RESPONSE_HEADERS);
		} else {
			sb.append("Inbound message:");
			httpHeaders = (Map<String, List<String>>) smc.get(MessageContext.HTTP_REQUEST_HEADERS);
		}

		SOAPMessage message = smc.getMessage();
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
			ByteArrayOutputStream baos = new ByteArrayOutputStream();  
			message.writeTo(baos);  
			this.messageLoggingHandlerUtils.logToSystemOut(outboundProperty, httpHeaders, baos.toByteArray());
		} catch (Exception e) {
			log.error("Exception in handler: " + e);
		}
	}
}








