/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2023 Link.it srl (http://www.link.it).
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
package it.govpay.rs.eventi;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.cxf.ext.logging.event.DefaultLogEventMapper;
import org.apache.cxf.ext.logging.event.LogEvent;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Exchange;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.openspcoop2.utils.LoggerWrapperFactory;
import org.openspcoop2.utils.service.context.ContextThreadLocal;
import org.openspcoop2.utils.service.context.IContext;
import org.slf4j.Logger;

import it.govpay.core.beans.Costanti;
import it.govpay.core.beans.EventoContext;
import it.govpay.core.beans.EventoContext.Componente;
import it.govpay.core.dao.eventi.EventiDAO;
import it.govpay.core.dao.eventi.dto.PutEventoDTO;
import it.govpay.model.eventi.DettaglioRisposta;
import it.govpay.core.utils.GpContext;

public class GiornaleEventiOutInterceptor extends AbstractPhaseInterceptor<Message>  {

	private Logger log = LoggerWrapperFactory.getLogger(GiornaleEventiOutInterceptor.class);
	private GiornaleEventiConfig giornaleEventiConfig = null;
	private EventiDAO eventiDAO = null; 

	public GiornaleEventiOutInterceptor() {
		super(Phase.SETUP_ENDING);
		this.eventiDAO = new EventiDAO(); 
	}

	@Override
	public void handleMessage(Message message) throws Fault {
		String url = null;
		String httpMethodS = null;
		try {
			if(!this.giornaleEventiConfig.isAbilitaGiornaleEventi()) return;
			
			IContext context = ContextThreadLocal.get();
			GpContext appContext = (GpContext) context.getApplicationContext();
			EventoContext eventoCtx = appContext.getEventoCtx();
			
			Exchange exchange = message.getExchange();
			Message inMessage = exchange.getInMessage();
			final LogEvent eventRequest = new DefaultLogEventMapper().map(inMessage);
			url = eventoCtx.getUrl() != null ? eventoCtx.getUrl() : eventRequest.getAddress();
			httpMethodS = eventoCtx.getMethod() != null ? eventoCtx.getMethod() : eventRequest.getHttpMethod();
			Date dataUscita = new Date();

			Integer responseCode = 200;
			if (!Boolean.TRUE.equals(message.get(Message.DECOUPLED_CHANNEL_MESSAGE))) {
				// avoid logging the default responseCode 200 for the decoupled responses
				responseCode = (Integer)message.get(Message.RESPONSE_CODE);
			}

			this.log.debug("Log Evento API: ["+this.giornaleEventiConfig.getApiName()+"] Method ["+httpMethodS+"], Url ["+url+"], StatusCode ["+responseCode+"]");

			if(!eventoCtx.isRegistraEvento()) return;
			
			// informazioni gia' calcolate nell'interceptor di dump
			if(eventoCtx.getDettaglioRisposta() == null)
				eventoCtx.setDettaglioRisposta(new DettaglioRisposta());
			
			if(this.giornaleEventiConfig.getApiNameEnum().equals(Componente.API_PAGOPA) || this.giornaleEventiConfig.getApiNameEnum().equals(Componente.API_MAGGIOLI_JPPA)) {
				@SuppressWarnings("unchecked")
				Map<String, List<String>> responseHeaders = (Map<String, List<String>>)message.get(Message.PROTOCOL_HEADERS);
				if (responseHeaders != null) {
					responseHeaders.put(Costanti.HEADER_NAME_OUTPUT_TRANSACTION_ID, Arrays.asList(context.getTransactionId()));
				}
			}

			final LogEvent eventResponse = new DefaultLogEventMapper().map(message);
			eventoCtx.getDettaglioRisposta().setHeadersFromMap(eventResponse.getHeaders());
			eventoCtx.getDettaglioRisposta().setStatus(responseCode);
			eventoCtx.getDettaglioRisposta().setDataOraRisposta(dataUscita);

			eventoCtx.setDataRisposta(dataUscita);
			eventoCtx.setStatus(responseCode);
			// se non ho impostato un sottotipo esito genero uno di default che corrisponde allo status code
			if(responseCode != null && eventoCtx.getSottotipoEsito() == null)
				eventoCtx.setSottotipoEsito(responseCode + "");

			PutEventoDTO putEventoDTO = new PutEventoDTO(context.getAuthentication());
			putEventoDTO.setEvento(eventoCtx);
			this.eventiDAO.inserisciEvento(putEventoDTO);
		} catch (Throwable e) {
			this.log.error(e.getMessage(),e);
		} finally {
			//ContextThreadLocal.unset();
		}
	}

	public GiornaleEventiConfig getGiornaleEventiConfig() {
		return giornaleEventiConfig;
	}

	public void setGiornaleEventiConfig(GiornaleEventiConfig giornaleEventiConfig) {
		this.giornaleEventiConfig = giornaleEventiConfig;
	}
}
