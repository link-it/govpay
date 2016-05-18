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
import it.govpay.servizi.pa.PaVerificaVersamento;
import it.govpay.servizi.pa.PaVerificaVersamentoResponse;
import it.govpay.bd.BasicBD;
import it.govpay.bd.model.Applicazione;
import it.govpay.bd.model.Connettore.Versione;
import it.govpay.bd.model.Versamento;
import it.govpay.core.exceptions.GovPayException;
import it.govpay.core.exceptions.VersamentoAnnullatoException;
import it.govpay.core.exceptions.VersamentoDuplicatoException;
import it.govpay.core.exceptions.VersamentoScadutoException;
import it.govpay.core.exceptions.VersamentoSconosciutoException;
import it.govpay.core.utils.GpThreadLocal;
import it.govpay.core.utils.VersamentoUtils;
import it.govpay.servizi.commons.EsitoOperazione;

import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.logger.beans.proxy.Server;

public class VerificaClient extends BasicClient {

	private static Logger log = LogManager.getLogger();
	private Versione versione;
	private static ObjectFactory objectFactory;
	private String codApplicazione;
	
	
	public VerificaClient(Applicazione applicazione) throws ClientException {
		super(applicazione, TipoConnettore.VERIFICA);
		versione = applicazione.getConnettoreVerifica().getVersione();
		codApplicazione = applicazione.getCodApplicazione();
		if(objectFactory == null || log == null ){
			objectFactory = new ObjectFactory();
		}
	}
	
	/**
	 * GESTIONE INTERNA DELLA CONNESSIONE AL DB.
	 * Fornirla aperta con tutto gia' committato.
	 * Viene restituita aperta.
	 */
	public Versamento invoke(String codVersamentoEnte, String iuv, BasicBD bd) throws ClientException, ServiceException, VersamentoAnnullatoException, VersamentoDuplicatoException, VersamentoScadutoException, VersamentoSconosciutoException, GovPayException {
		log.debug("Richiedo la verifica per il versamento ("+codVersamentoEnte+") in versione (" + versione.toString() + ") alla URL ("+url+")");
		
		Server server = new Server();
		server.setName(codApplicazione);
		GpThreadLocal.get().getTransaction().setServer(server);
		
		//Chiudo la connessione al DB prima della comunicazione HTTP
		bd.closeConnection();
		
		switch (versione) {
		case v2_1:
			PaVerificaVersamentoResponse paVerificaVersamentoResponse = null;
			try {
				PaVerificaVersamento paVerificaVersamento = new PaVerificaVersamento();
				paVerificaVersamento.setCodApplicazione(codApplicazione);
				paVerificaVersamento.setCodVersamentoEnte(codVersamentoEnte);
				paVerificaVersamento.setIuv(iuv);
				QName qname = new QName("http://www.govpay.it/servizi/pa/", "paVerificaVersamento");
				byte[] response = sendSoap("paVerificaVersamento", new JAXBElement<PaVerificaVersamento>(qname, PaVerificaVersamento.class, paVerificaVersamento), null, false);
				try {
					paVerificaVersamentoResponse = (PaVerificaVersamentoResponse) SOAPUtils.unmarshal(response);
				} catch(Exception e) {
					throw new ClientException(e);
				} 
			} finally {
				bd.setupConnection();
			}
			switch (paVerificaVersamentoResponse.getCodEsito()) {
			case OK:
				return VersamentoUtils.toVersamentoModel(paVerificaVersamentoResponse.getVersamento(), bd);
			case PAGAMENTO_ANNULLATO:
				throw new VersamentoAnnullatoException();
			case PAGAMENTO_DUPLICATO:
				throw new VersamentoDuplicatoException();
			case PAGAMENTO_SCADUTO:
				throw new VersamentoScadutoException();
			case PAGAMENTO_SCONOSCIUTO:
				throw new VersamentoSconosciutoException();
			}
		case v1:
		case v2:
			bd.setupConnection();
			throw new GovPayException(EsitoOperazione.INTERNAL, "Versione del connettore (" + versione + ") non supportato");
		}
		
		bd.setupConnection();
		throw new ServiceException();
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
