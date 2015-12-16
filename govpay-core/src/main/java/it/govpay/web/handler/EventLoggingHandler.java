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

import it.govpay.web.handler.utils.EventLoggingHandlerUtils;

import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPException;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class EventLoggingHandler implements SOAPHandler<SOAPMessageContext> {

	private boolean client;
	
	private EventLoggingHandlerUtils eventLoggingHandlerUtils;

	public EventLoggingHandler() {
		this(false);
	}

	public EventLoggingHandler(boolean client) {
		this.eventLoggingHandlerUtils = new EventLoggingHandlerUtils(client);
		this.client = client;
	}

	Logger log = LogManager.getLogger();

	public Set<QName> getHeaders() {
		return null;
	}

	private boolean isRequest(SOAPMessageContext smc) {
		Boolean outboundProperty = (Boolean) smc.get (MessageContext.MESSAGE_OUTBOUND_PROPERTY);
		if(this.client) {
			return outboundProperty;
		} else {
			return !outboundProperty;
		}
	}

	public boolean handleMessage(SOAPMessageContext smc) {
			return this.eventLoggingHandlerUtils.handleMessage(isRequest(smc), getEsito(smc));
	}

	private String getEsito(SOAPMessageContext smc) {
		try {
			if(smc.getMessage() != null && smc.getMessage().getSOAPBody() != null) {
				if(this.client) { 
					return "OK";
				} else {
					NodeList elementsByTagName = smc.getMessage().getSOAPBody().getElementsByTagName("esito");
					if(elementsByTagName != null && elementsByTagName.getLength() > 0) {
						Node item = elementsByTagName.item(0);
						if(item != null) {
							String esito = item.getTextContent();
							// Riduco a dimensione DB
							if(esito.length() > 254)
								return esito.substring(0, 254);
							else
							return esito;

						}

					}
				}
			}

			return "??";
			
		} catch (SOAPException e) {
			this.log.warn("Esito non trovato");
			return "??";
		}
	}

	public boolean handleFault(SOAPMessageContext smc) {
		return this.eventLoggingHandlerUtils.handleFault();
	}

	// nothing to clean up
	public void close(MessageContext messageContext) {
	}
}
