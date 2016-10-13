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
import it.govpay.bd.model.Connettore.Tipo;
import it.govpay.bd.model.Versamento;
import it.govpay.bd.model.Versionabile.Versione;
import it.govpay.core.exceptions.GovPayException;
import it.govpay.core.exceptions.VersamentoAnnullatoException;
import it.govpay.core.exceptions.VersamentoDuplicatoException;
import it.govpay.core.exceptions.VersamentoScadutoException;
import it.govpay.core.exceptions.VersamentoSconosciutoException;
import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.GpThreadLocal;
import it.govpay.core.utils.VersamentoUtils;
import it.govpay.servizi.commons.EsitoOperazione;

import org.openspcoop2.generic_project.exception.ServiceException;

public class VerificaClient extends BasicClient {

	private static Logger log = LogManager.getLogger();
	private Versione versione;
	private Tipo tipo;
	private static ObjectFactory objectFactory;
	private String codApplicazione;
	
	
	public VerificaClient(Applicazione applicazione) throws ClientException {
		super(applicazione, TipoConnettore.VERIFICA);
		versione = applicazione.getVersione();
		tipo = applicazione.getConnettoreVerifica().getTipo();
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
	public Versamento invoke(String codVersamentoEnte, String codDominio, String iuv, BasicBD bd) throws ClientException, ServiceException, VersamentoAnnullatoException, VersamentoDuplicatoException, VersamentoScadutoException, VersamentoSconosciutoException, GovPayException {
		
		log.debug("Richiedo la verifica per il versamento ("+codVersamentoEnte+") in versione (" + versione.toString() + ") alla URL ("+url+")");
		
		GpContext ctx = GpThreadLocal.get();
		String idTransaction = ctx.openTransaction();
		
		try {
			ctx.setupPaClient(codApplicazione, "paVerificaVersamento", url.toExternalForm(), versione);
			ctx.log("verifica.avvio", codApplicazione, codVersamentoEnte, codDominio, iuv);
			
			
			//Chiudo la connessione al DB prima della comunicazione HTTP
			bd.closeConnection();
			
			switch (tipo) {
			case SOAP:
				PaVerificaVersamentoResponse paVerificaVersamentoResponse = null;
				try {
					PaVerificaVersamento paVerificaVersamento = new PaVerificaVersamento();
					paVerificaVersamento.setCodApplicazione(codApplicazione);
					paVerificaVersamento.setCodVersamentoEnte(codVersamentoEnte);
					paVerificaVersamento.setIuv(iuv);
					if(versione.compareTo(Versione.GP_02_02_00) >= 0)
						paVerificaVersamento.setCodDominio(codDominio);
					QName qname = new QName("http://www.govpay.it/servizi/pa/", "paVerificaVersamento");
					byte[] response = sendSoap("paVerificaVersamento", new JAXBElement<PaVerificaVersamento>(qname, PaVerificaVersamento.class, paVerificaVersamento), null, false);
					try {
						paVerificaVersamentoResponse = (PaVerificaVersamentoResponse) SOAPUtils.unmarshal(response);
					} catch(Exception e) {
						ctx.log("verifica.verificaKo", codApplicazione, codVersamentoEnte, codDominio, iuv, "Errore nella deserializzazione del messaggio di risposta (" + e.getMessage() + ")");
						throw new ClientException(e);
					} 
				} finally {
					bd.setupConnection(GpThreadLocal.get().getTransactionId());
				}
				switch (paVerificaVersamentoResponse.getCodEsito()) {
				case OK:
					ctx.log("verifica.avvio", codApplicazione, codVersamentoEnte, codDominio, iuv);
					try {
						return VersamentoUtils.toVersamentoModel(paVerificaVersamentoResponse.getVersamento(), bd);
					} catch (GovPayException e) {
						ctx.log("verifica.verificaKo", codApplicazione, codVersamentoEnte, codDominio, iuv, "[" + e.getCodEsito() + "] " + e.getMessage());
						throw e;
					}
				case PAGAMENTO_ANNULLATO:
					ctx.log("verifica.verificaAnnullato", codApplicazione, codVersamentoEnte, codDominio, iuv);
					throw new VersamentoAnnullatoException(paVerificaVersamentoResponse.getDescrizioneEsito());
				case PAGAMENTO_DUPLICATO:
					ctx.log("verifica.verificaDuplicato", codApplicazione, codVersamentoEnte, codDominio, iuv);
					throw new VersamentoDuplicatoException(paVerificaVersamentoResponse.getDescrizioneEsito());
				case PAGAMENTO_SCADUTO:
					ctx.log("verifica.verificaScaduto", codApplicazione, codVersamentoEnte, codDominio, iuv);
					throw new VersamentoScadutoException(paVerificaVersamentoResponse.getDescrizioneEsito());
				case PAGAMENTO_SCONOSCIUTO:
					ctx.log("verifica.verificaSconosciuto", codApplicazione, codVersamentoEnte, codDominio, iuv);
					throw new VersamentoSconosciutoException();
				}
			default:
				bd.setupConnection(GpThreadLocal.get().getTransactionId());
				ctx.log("verifica.verificaKo", codApplicazione, codVersamentoEnte, codDominio, iuv, "Tipo del connettore (" + tipo + ") non supportato");
				throw new GovPayException(EsitoOperazione.INTERNAL, "Tipo del connettore (" + tipo + ") non supportato");
			}
		} catch (ServiceException e) {
			ctx.log("verifica.verificaKo", codApplicazione, codVersamentoEnte, codDominio, iuv, e.getMessage());
		} finally {
			ctx.closeTransaction(idTransaction);
		}
		
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
