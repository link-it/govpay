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

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPException;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import org.openspcoop2.utils.LoggerWrapperFactory;
import org.openspcoop2.utils.service.context.ContextThreadLocal;
import org.openspcoop2.utils.service.context.IContext;
import org.openspcoop2.utils.service.context.MD5Constants;
import org.slf4j.Logger;
import org.slf4j.MDC;

import it.govpay.core.autorizzazione.utils.AutorizzazioneUtils;
import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.EventoContext.Categoria;

public class MessageLoggingHandlerNDP_010702 implements SOAPHandler<SOAPMessageContext> {

	private static Logger log = LoggerWrapperFactory.getLogger(MessageLoggingHandlerNDP_010702.class);
	private static final String SOAP_ACTION = "SOAPAction";

	@Override
	public Set<QName> getHeaders() {
		return null;
	}

	@Override
	public boolean handleMessage(SOAPMessageContext smc) {
		return this.logToSystemOut(smc);
	}

	@Override
	public boolean handleFault(SOAPMessageContext smc) {
		return this.logToSystemOut(smc);
	}

	@Override
	public void close(MessageContext messageContext) {
	}

	@SuppressWarnings("unchecked")
	private boolean logToSystemOut(SOAPMessageContext smc) {
		try {
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
					GpContext.popolaGpContext(ctx, smc, GpContext.TIPO_SERVIZIO_NDP, 010702);
					
					
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
		} catch (Exception e) {
			log.error("Errore durante il log dell'operazione: " + e.getMessage(),e);
			return true;
		}
	}
}








