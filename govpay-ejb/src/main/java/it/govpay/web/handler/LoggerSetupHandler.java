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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;

public class LoggerSetupHandler implements SOAPHandler<SOAPMessageContext> {
	
	private static Logger log = LogManager.getLogger();
	
    public Set<QName> getHeaders() {
        return null;
    }

    public boolean handleMessage(SOAPMessageContext smc) {
    	setupThreadContext(smc);
        return true;
    }

    public boolean handleFault(SOAPMessageContext smc) {
    	setupThreadContext(smc);
        return true;
    }

    public void close(MessageContext messageContext) {
    }

    private void setupThreadContext(SOAPMessageContext smc) {
        Boolean outboundProperty = (Boolean) smc.get (MessageContext.MESSAGE_OUTBOUND_PROPERTY);

        if (outboundProperty.booleanValue()) {
        	try { 
        		smc.getMessage().setProperty("X-GP-CMDID", ThreadContext.get("cmd"));
        	} catch (SOAPException e) {
        		log.warn("Impossibile impostare l'header HTTP X-GP-CMDID nella risposta.");
        	}
        } else {
    		String codOperazione = UUID.randomUUID().toString().replace("-", "");
    		if(smc.get(SOAPMessageContext.WSDL_OPERATION) != null)
    			ThreadContext.put("cmd", ((QName) smc.get(SOAPMessageContext.WSDL_OPERATION)).getLocalPart());
    		ThreadContext.put("op",  codOperazione);
        }
    }
}








