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
package it.govpay.core.business;

import org.apache.commons.lang.StringUtils;
import org.openspcoop2.utils.LoggerWrapperFactory;
import org.openspcoop2.utils.service.beans.HttpMethodEnum;
import org.slf4j.Logger;

import it.govpay.bd.BasicBD;
import it.govpay.bd.configurazione.model.GdeEvento;
import it.govpay.bd.configurazione.model.GdeInterfaccia;
import it.govpay.bd.configurazione.model.Giornale;
import it.govpay.bd.model.Evento;
import it.govpay.bd.pagamento.EventiBD;
import it.govpay.core.utils.EventoContext;
import it.govpay.core.utils.EventoContext.Componente;
import it.govpay.core.utils.EventoContext.Esito;
import it.govpay.core.utils.client.AvvisaturaClient;
import it.govpay.core.utils.client.NodoClient;

public class GiornaleEventi extends BasicBD {
	
	private static Logger log = LoggerWrapperFactory.getLogger(GiornaleEventi.class	);
	
	public GiornaleEventi(BasicBD basicBD) {
		super(basicBD);
	}

	public void registraEvento(Evento evento) {
		try {
			EventiBD eventiBD = new EventiBD(this);
			eventiBD.insertEvento(evento);
		} catch (Exception e) {
			log.error("Errore nella registrazione degli eventi", e);
		}
	}
	
	public static GdeInterfaccia getConfigurazioneComponente(Componente componente, Giornale giornale) {
		switch(componente) {
		case API_BACKOFFICE:
			return giornale.getApiBackoffice();
		case API_ENTE:
			return giornale.getApiEnte();
		case API_PAGAMENTO:
			return giornale.getApiPagamento();
		case API_PAGOPA:
			return giornale.getApiPagoPA();
		case API_RAGIONERIA:
			return giornale.getApiRagioneria();
		case API_PENDENZE:
			return giornale.getApiPendenze();
		case API_BACKEND_IO:
			return giornale.getApiBackendIO();
		case API_USER:
		case API_WC: return null;
		}
		
		return null;
	}
	
	public static boolean dumpEvento(GdeEvento evento, Integer responseCode) {
		switch (evento.getDump()) {
		case MAI:
			return false;
		case SEMPRE:
			return true;
		case SOLO_ERRORE:
			return responseCode > 399;
		}
		
		return false;
	}
	
	public static boolean logEvento(GdeEvento evento, Integer responseCode) {
		switch (evento.getLog()) {
		case MAI:
			return false;
		case SEMPRE:
			return true;
		case SOLO_ERRORE:
			return responseCode > 399;
		}
		
		return false;
	}
	
	public static boolean dumpEvento(GdeEvento evento, EventoContext.Esito esito) {
		switch (evento.getDump()) {
		case MAI:
			return false;
		case SEMPRE:
			return true;
		case SOLO_ERRORE:
			return !esito.equals(Esito.OK);
		}
		
		return false;
	}
	
	public static boolean logEvento(GdeEvento evento,  EventoContext.Esito esito) {
		switch (evento.getLog()) {
		case MAI:
			return false;
		case SEMPRE:
			return true;
		case SOLO_ERRORE:
			return !esito.equals(Esito.OK);
		}
		
		return false;
	}
	
	public static HttpMethodEnum getHttpMethod(String httpMethod) {
		if(StringUtils.isNotEmpty(httpMethod)) {
			HttpMethodEnum http = HttpMethodEnum.fromValue(httpMethod);
			return http;
		}
		return null;
	}
	
	public static boolean isRequestLettura(HttpMethodEnum httpMethod, Componente componente, String operazione) {
		if(componente.equals(Componente.API_PAGOPA)) {
			if(operazione != null)
				return !isOperazioneScrittura(operazione);
			else 
				return false;
		}
		
		return isRequestLettura(httpMethod);
	}
	
	public static boolean isRequestScrittura(HttpMethodEnum httpMethod, Componente componente, String operazione) {
		if(componente.equals(Componente.API_PAGOPA)) {
			return isOperazioneScrittura(operazione);
		}

		return isRequestScrittura(httpMethod);
	}
	
	public static boolean isRequestLettura(HttpMethodEnum httpMethod) {
		if(httpMethod != null ) {
			switch (httpMethod) {
			case GET:
			case OPTIONS:
			case HEAD:
			case TRACE:
				return true;
			case DELETE:
			case LINK:
			case PATCH:
			case POST:
			case PUT:
			case UNLINK:
				return false;
			}
		}
		return false;
	}
	
	public static boolean isRequestScrittura(HttpMethodEnum httpMethod) {
		if(httpMethod != null ) {
			switch (httpMethod) {
			case PUT:
			case POST:
			case DELETE:
			case PATCH:
			case LINK:
			case UNLINK:
				return true;
			case GET:
			case OPTIONS:
			case HEAD:
			case TRACE:
				return false;
			}
		}
		return false;
	}
	/*
		 Scritture:
			nodoChiediCopiaRT
			nodoChiediStatoRPT
			nodoInviaRPT
			nodoInviaCarrelloRPT
			nodoInviaRichiestaStorno
			nodoInviaRispostaRevoca
			paaVerificaRPT
			paaAttivaRPT
			paaInviaEsitoStorno
			paaInviaRichiestaRevoca
			paaInviaRT
		Letture
			tutto il resto
	 * */
	private static boolean isOperazioneScrittura(String operazione) {
		if(NodoClient.Azione.nodoChiediCopiaRT.toString().equals(operazione) 
				|| NodoClient.Azione.nodoChiediStatoRPT.toString().equals(operazione) 
				|| NodoClient.Azione.nodoInviaRPT.toString().equals(operazione) 
				|| NodoClient.Azione.nodoInviaCarrelloRPT.toString().equals(operazione) 
				|| NodoClient.Azione.nodoInviaRichiestaStorno.toString().equals(operazione) 
				|| NodoClient.Azione.nodoInviaRispostaRevoca.toString().equals(operazione) 
				|| AvvisaturaClient.Azione.nodoInviaAvvisoDigitale.toString().equals(operazione)
				|| EventoContext.APIPAGOPA_TIPOEVENTO_PAAVERIFICARPT.equals(operazione)
				|| EventoContext.APIPAGOPA_TIPOEVENTO_PAAATTIVARPT.equals(operazione)
				|| EventoContext.APIPAGOPA_TIPOEVENTO_PAAINVIAESITOSTORNO.equals(operazione)
				|| EventoContext.APIPAGOPA_TIPOEVENTO_PAAINVIARICHIESTAREVOCA.equals(operazione)
				|| EventoContext.APIPAGOPA_TIPOEVENTO_PAAINVIART.equals(operazione)
				) {
			return true;
		}
		
		
		return false;
	}
}
