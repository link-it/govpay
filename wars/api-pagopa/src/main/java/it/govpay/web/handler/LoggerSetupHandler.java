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
import java.util.UUID;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPException;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import org.openspcoop2.utils.LoggerWrapperFactory;
import org.slf4j.Logger;
import org.slf4j.MDC;

public class LoggerSetupHandler implements SOAPHandler<SOAPMessageContext> {
	
	private static Logger log = LoggerWrapperFactory.getLogger(LoggerSetupHandler.class);
	
    @Override
	public Set<QName> getHeaders() {
        return null;
    }

    @Override
	public boolean handleMessage(SOAPMessageContext smc) {
    	this.setupMDC(smc);
        return true;
    }

    @Override
	public boolean handleFault(SOAPMessageContext smc) {
    	this.setupMDC(smc);
        return true;
    }

    @Override
	public void close(MessageContext messageContext) {
    }

    private void setupMDC(SOAPMessageContext smc) {
        Boolean outboundProperty = (Boolean) smc.get (MessageContext.MESSAGE_OUTBOUND_PROPERTY);

        if (outboundProperty.booleanValue()) {
        	try { 
        		smc.getMessage().setProperty("X-GP-CMDID", MDC.get("cmd"));
        	} catch (SOAPException e) {
        		log.warn("Impossibile impostare l'header HTTP X-GP-CMDID nella risposta.");
        	}
        } else {
    		String codOperazione = UUID.randomUUID().toString().replace("-", "");
    		if(smc.get(MessageContext.WSDL_OPERATION) != null)
    			MDC.put("cmd", ((QName) smc.get(MessageContext.WSDL_OPERATION)).getLocalPart());
    		MDC.put("op",  codOperazione);
        }
    }
}








