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
package it.govpay.web.handler;

import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import org.openspcoop2.utils.LoggerWrapperFactory;
import org.slf4j.Logger;

import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.log.MessageLoggingHandlerUtils;

public class MessageLoggingHandlerNDP_010702 implements SOAPHandler<SOAPMessageContext> {

	private static Logger log = LoggerWrapperFactory.getLogger(MessageLoggingHandlerNDP_010702.class);
	
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

	private boolean logToSystemOut(SOAPMessageContext smc) {
		return MessageLoggingHandlerUtils.logToSystemOut(smc, GpContext.TIPO_SERVIZIO_NDP, 010702, log);
	}
}








