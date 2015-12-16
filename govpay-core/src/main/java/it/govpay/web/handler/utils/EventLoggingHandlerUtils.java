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
package it.govpay.web.handler.utils;

import it.govpay.bd.BasicBD;
import it.govpay.bd.model.Evento;
import it.govpay.bd.model.Evento.CategoriaEvento;
import it.govpay.bd.model.Evento.TipoEvento;
import it.govpay.bd.model.Rpt.TipoVersamento;
import it.govpay.bd.registro.EventiBD;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.ws.handler.MessageContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.openspcoop2.generic_project.exception.ServiceException;

public class EventLoggingHandlerUtils {

	private static final String DATA_ORA_EVENTO_RICHIESTA = "DATA_ORA";
	private boolean client;

	public EventLoggingHandlerUtils() {
	}

	public EventLoggingHandlerUtils(boolean client) {
		this.client = client;
	}

	Logger log = LogManager.getLogger();

	public Set<QName> getHeaders() {
		return null;
	}

//	private boolean isRequest(SOAPMessageContext smc) {
//		Boolean outboundProperty = (Boolean) smc.get (MessageContext.MESSAGE_OUTBOUND_PROPERTY);
//		if(this.client) {
//			return outboundProperty;
//		} else {
//			return !outboundProperty;
//		}
//	}

	public boolean handleMessage(boolean request, String esito) {

//		boolean isRequest = isRequest(smc);
		
		if(request){
			ThreadContext.put(DATA_ORA_EVENTO_RICHIESTA, "" +new Date().getTime());
		} else {
			// TODO estendere a tutte le operazioni
			// Limito la funzionalita' alle sole operazioni previste dalla specifica
			List<Evento> lst = createEventi(esito);//smc);

			BasicBD bd = null;
			try {
				bd = BasicBD.getInstance();
				EventiBD eventiBD = new EventiBD(bd);
				bd.setAutoCommit(false);
				for(Evento evento: lst) {
					switch (evento.getTipoEvento()) {
					case nodoChiediCopiaRT:
					case nodoChiediStatoRPT:
					case nodoInviaCarrelloRPT:
					case nodoInviaEsitoStorno:
					case nodoInviaRichiestaStorno:
					case nodoInviaRPT:
					case paaAttivaRPT:
					case paaInviaRT:
					case paaVerificaRPT:
						eventiBD.insertEvento(evento);
						break;
					default:
						break;
					}
				}
				bd.commit();
			} catch(Exception e) {
				this.log.error("Errore durante l'inserimento di un evento : "+e.getMessage(), e);
				if(bd != null)
					bd.rollback();
				return false;
			} finally {
				if(bd != null)
					bd.closeConnection();
			}
		}
		return true;
	}

	private List<Evento> createEventi(boolean richiesta, String esito) {
		List<Evento> lst = new ArrayList<Evento>();

		Date data = null;

		if(richiesta) {
			if(!ThreadContext.containsKey(DATA_ORA_EVENTO_RICHIESTA)) {
				log.warn("Evento di richiesta non trovato!");
				data = new Date();
			} else {
				try {
					data = new Date(Long.parseLong(ThreadContext.get(DATA_ORA_EVENTO_RICHIESTA)));
				} catch (NumberFormatException e) {
					log.warn("Data Evento di richiesta malformato!");
					data = new Date();
				} 
			}
		}

		String[] lstCCP = null;
		String[] lstIUV = null;
		String[] lstCodDominio = null;

		TipoEvento tipoEvento = null;
		if(ThreadContext.containsKey(Evento.SOAP_ACTION)) {
			try {
				tipoEvento = TipoEvento.toEnum(ThreadContext.get(Evento.SOAP_ACTION));
			} catch (ServiceException e) {
				tipoEvento = TipoEvento.SCONOSCIUTO;
			}
		} else {
			this.log.warn("Tipo Evento non trovato");
			tipoEvento = TipoEvento.SCONOSCIUTO;
		}
		String sottotipoEvento = (richiesta) ? "req" : "rsp";

		String govPaySoggetto = null;
		if(ThreadContext.containsKey(Evento.GP_SOGGETTO))
			govPaySoggetto = ThreadContext.get(Evento.GP_SOGGETTO);

		if(ThreadContext.containsKey(Evento.CCP) && ThreadContext.get(Evento.CCP) != null) {
			lstCCP = (ThreadContext.get(Evento.CCP)).split(Evento.VALUES_SEPARATOR);
		} else {
			lstCCP = new String[1];
			//this.log.warn("Evento: tipo["+tipoEvento+"] sottotipo ["+sottotipoEvento+"]" +Evento.CCP + " non trovato");
		}

		if(ThreadContext.containsKey(Evento.IUV) && ThreadContext.get(Evento.IUV) != null) {
			lstIUV = (ThreadContext.get(Evento.IUV)).split(Evento.VALUES_SEPARATOR);
		} else {
			lstIUV = new String[1];
			//this.log.warn("Evento: tipo["+tipoEvento+"] sottotipo ["+sottotipoEvento+"]" +Evento.IUV + " non trovato");
		}

		if(ThreadContext.containsKey(Evento.ID_DOMINIO) && ThreadContext.get(Evento.ID_DOMINIO) != null) {
			lstCodDominio = (ThreadContext.get(Evento.ID_DOMINIO)).split(Evento.VALUES_SEPARATOR);
		} else {
			lstCodDominio = new String[1];
			//this.log.warn("Evento: tipo["+tipoEvento+"] sottotipo ["+sottotipoEvento+"]" +Evento.ID_DOMINIO + " non trovato");
		}

		for (int i = 0; i < lstIUV.length; i++) {

			Evento evento = new Evento();
			if(richiesta) {
				evento.setDataOraEvento(data);
			} else {
				evento.setDataOraEvento(new Date());
			}
			String iuv = lstIUV[i];
			String ccp = lstCCP[i];
			String codDominio = lstCodDominio[i];

			if(iuv != null) {
				evento.setIuv(iuv);
			}

			if(ccp != null) {
				evento.setCcp(ccp);
			}

			if(codDominio != null) {
				evento.setCodDominio(codDominio);
			}

			if(richiesta) {
				evento.setEsito("OK");
			} else {
				evento.setEsito(esito);
			}


			if(client) {
				evento.setCodErogatore(Evento.NDP_SOGGETTO);
				evento.setCodFruitore(govPaySoggetto);
			} else {
				evento.setCodErogatore(govPaySoggetto);
				evento.setCodFruitore(Evento.NDP_SOGGETTO);
			}

			evento.setTipoEvento(tipoEvento);
			evento.setSottotipoEvento(sottotipoEvento);
			evento.setCategoriaEvento(CategoriaEvento.INTERFACCIA);

			if(ThreadContext.containsKey(Evento.PARAMATERI_SPECIFICI_INTERFACCIA))
				evento.setAltriParametri(ThreadContext.get(Evento.PARAMATERI_SPECIFICI_INTERFACCIA));

			if(ThreadContext.containsKey(Evento.CANALE))
				evento.setCanalePagamento(ThreadContext.get(Evento.CANALE));

			if(ThreadContext.containsKey(Evento.ID_PSP))
				evento.setCodPsp(ThreadContext.get(Evento.ID_PSP));

			if(ThreadContext.containsKey(Evento.ID_STAZIONE))
				evento.setCodStazione(ThreadContext.get(Evento.ID_STAZIONE));

			try {
				if(ThreadContext.containsKey(Evento.ID_APPLICAZIONE)) {
					evento.setIdApplicazione(Long.parseLong(ThreadContext.get(Evento.ID_APPLICAZIONE)));
				}
			} catch(NumberFormatException e) {

			}

			if(ThreadContext.containsKey(Evento.COMPONENTE))
				evento.setComponente(ThreadContext.get(Evento.COMPONENTE));
			if(ThreadContext.containsKey(Evento.COMPONENTE))
				try {
					evento.setTipoVersamento(TipoVersamento.toEnum(ThreadContext.get(Evento.TIPO_VERSAMENTO)));
				} catch (ServiceException e) {
					evento.setTipoVersamento(TipoVersamento.SCONOSCIUTO);
				}

			lst.add(evento);
		}
		return lst;
	}

	private List<Evento> createEventi(String esito) {
		List<Evento> lst = new ArrayList<Evento>();
		lst.addAll(createEventi(true, esito));
		lst.addAll(createEventi(false, esito));
		return lst;
	}

	public boolean handleFault() {
		this.log.info("Ottenuto fault, inserisco solo l'evento di richiesta con esito KO");

		try {
			List<Evento> lst = createEventi(true, null);
			BasicBD bd = BasicBD.getInstance();
			EventiBD eventiBD = new EventiBD(bd);
			for(Evento evento: lst) {
				evento.setEsito("KO");
				eventiBD.insertEvento(evento);
			}
		} catch (ServiceException e) {
			this.log.error("Errore durante l'inserimento di un evento : "+e.getMessage(), e);
		} catch (Exception e) {
			this.log.error("Errore durante l'inserimento di un evento : "+e.getMessage(), e);
		}
		return true;
	}

	// nothing to clean up
	public void close(MessageContext messageContext) {
	}
}
