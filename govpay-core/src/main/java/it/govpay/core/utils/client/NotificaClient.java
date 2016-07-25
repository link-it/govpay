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
package it.govpay.core.utils.client;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import it.govpay.servizi.pa.ObjectFactory;
import it.govpay.servizi.pa.PaNotificaStorno;
import it.govpay.servizi.pa.PaNotificaStorno.RichiestaStorno;
import it.govpay.bd.model.Applicazione;
import it.govpay.bd.model.Connettore.Tipo;
import it.govpay.bd.model.Notifica;
import it.govpay.bd.model.Rpt;
import it.govpay.bd.model.Rr;
import it.govpay.bd.model.Versionabile.Versione;
import it.govpay.core.exceptions.GovPayException;
import it.govpay.core.utils.Gp21Utils;
import it.govpay.servizi.commons.StatoRevoca;
import it.govpay.servizi.pa.PaNotificaTransazione;

import org.openspcoop2.generic_project.exception.ServiceException;

public class NotificaClient extends BasicClient {

	private static Logger log = LogManager.getLogger();
	private Tipo tipo;
	private Versione versione;
	private static ObjectFactory objectFactory;
	
	public NotificaClient(Applicazione applicazione) throws ClientException {
		super(applicazione, TipoConnettore.NOTIFICA);
		tipo = applicazione.getConnettoreNotifica().getTipo();
		versione = applicazione.getVersione();
		
		if(objectFactory == null || log == null ){
			objectFactory = new ObjectFactory();
		}
	}
	
	/**
	 * Business utilizzati da precaricare:
	 * notifica.getApplicazione
	 * notifica.getRpt.getVersamento
	 * notifica.getRpt.getCanale
	 * notifica.getRpt.getPsp
	 * notifica.getRpt.getPagamenti
	 * 
	 * @param notifica
	 * @return
	 * @throws ServiceException 
	 * @throws GovPayException 
	 * @throws ClientException
	 */
	public void invoke(Notifica notifica) throws ClientException, ServiceException, GovPayException {
		
		log.debug("Spedisco la notifica di " + notifica.getTipo() + ((notifica.getIdRr() == null) ? " PAGAMENTO" : " STORNO") + " della transazione (" + notifica.getRpt(null).getCodDominio() + ")(" + notifica.getRpt(null).getIuv() + ")(" + notifica.getRpt(null).getCcp() + ") in versione (" + versione.toString() + ") alla URL ("+url+")");
		
		switch (tipo) {
		case SOAP:
			if(notifica.getIdRr() == null) {
				Rpt rpt = notifica.getRpt(null);
				PaNotificaTransazione paNotificaTransazione = new PaNotificaTransazione();
				paNotificaTransazione.setCodApplicazione(notifica.getApplicazione(null).getCodApplicazione());
				paNotificaTransazione.setCodVersamentoEnte(rpt.getVersamento(null).getCodVersamentoEnte());
				paNotificaTransazione.setTransazione(Gp21Utils.toTransazione(versione, rpt, null));
				QName qname = new QName("http://www.govpay.it/servizi/pa/", "paNotificaTransazione");
				sendSoap("paNotificaTransazione", new JAXBElement<PaNotificaTransazione>(qname, PaNotificaTransazione.class, paNotificaTransazione), null, false);
				return;
			} else {
				Rr rr = notifica.getRr(null);
				Rpt rpt = notifica.getRr(null).getRpt(null);
				PaNotificaStorno paNotificaStorno = new PaNotificaStorno();
				paNotificaStorno.setCodApplicazione(notifica.getApplicazione(null).getCodApplicazione());
				paNotificaStorno.setCodVersamentoEnte(rpt.getVersamento(null).getCodVersamentoEnte());
				RichiestaStorno richiestaStorno = new RichiestaStorno();
				richiestaStorno.setCcp(rr.getCcp());
				richiestaStorno.setCodDominio(rr.getCodDominio());
				richiestaStorno.setCodRichiesta(rr.getCodMsgRevoca());
				richiestaStorno.setDataRichiesta(rr.getDataMsgRevoca());
				richiestaStorno.setDescrizioneStato(rr.getDescrizioneStato());
				richiestaStorno.setEr(rr.getXmlEr());
				if(rr.getImportoTotaleRevocato() != null)
					richiestaStorno.setImportoStornato(rr.getImportoTotaleRevocato());
				richiestaStorno.setIuv(rr.getIuv());
				richiestaStorno.setRr(rr.getXmlRr());
				richiestaStorno.setStato(StatoRevoca.valueOf(rr.getStato().toString()));
				paNotificaStorno.setRichiestaStorno(richiestaStorno);
				QName qname = new QName("http://www.govpay.it/servizi/pa/", "paNotificaStorno");
				sendSoap("paNotificaStorno", new JAXBElement<PaNotificaStorno>(qname, PaNotificaStorno.class, paNotificaStorno), null, false);
				return;
			}
		}
	}
	
	public class SendEsitoResponse {

		private int responseCode;
		private String detail;
		public int getResponseCode() {
			return responseCode;
		}
		public void setResponseCode(int responseCode) {
			this.responseCode = responseCode;
		}
		public String getDetail() {
			return detail;
		}
		public void setDetail(String detail) {
			this.detail = detail;
		}
	}
}
