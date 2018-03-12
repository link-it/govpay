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
package it.govpay.core.utils.client;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.UnmarshalException;
import javax.xml.namespace.QName;

import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.LoggerWrapperFactory;
import org.slf4j.Logger;

import it.govpay.bd.BasicBD;
import it.govpay.bd.model.Versamento;
import it.govpay.core.exceptions.GovPayException;
import it.govpay.core.exceptions.VersamentoAnnullatoException;
import it.govpay.core.exceptions.VersamentoDuplicatoException;
import it.govpay.core.exceptions.VersamentoScadutoException;
import it.govpay.core.exceptions.VersamentoSconosciutoException;
import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.GpThreadLocal;
import it.govpay.core.utils.JaxbUtils;
import it.govpay.core.utils.VersamentoUtils;
import it.govpay.bd.model.Applicazione;
import it.govpay.model.Connettore.Tipo;
import it.govpay.model.Versionabile.Versione;
import it.govpay.servizi.commons.EsitoOperazione;
import it.govpay.servizi.pa.ObjectFactory;
import it.govpay.servizi.pa.PaVerificaVersamento;
import it.govpay.servizi.pa.PaVerificaVersamentoResponse;

public class VerificaClient extends BasicClient {

	private static Logger log = LoggerWrapperFactory.getLogger(VerificaClient.class);
	private Versione versione;
	private Tipo tipo;
	private static ObjectFactory objectFactory;
	private String codApplicazione;
	
	
	public VerificaClient(Applicazione applicazione) throws ClientException {
		super(applicazione, TipoConnettore.VERIFICA);
		versione = applicazione.getConnettoreVerifica().getVersione();
		codApplicazione = applicazione.getCodApplicazione();
		this.tipo = Tipo.valueOf(this.versione.getApi());
		if(objectFactory == null || log == null ){
			objectFactory = new ObjectFactory();
		}
	}
	
	/**
	 * GESTIONE INTERNA DELLA CONNESSIONE AL DB.
	 * Fornirla aperta con tutto gia' committato.
	 * Viene restituita aperta.
	 */
	public Versamento invoke(String codVersamentoEnte, String bundlekey, String codUnivocoDebitore, String codDominio, String iuv, BasicBD bd) throws ClientException, ServiceException, VersamentoAnnullatoException, VersamentoDuplicatoException, VersamentoScadutoException, VersamentoSconosciutoException, GovPayException {
		
		String codVersamentoEnteD = codVersamentoEnte != null ? codVersamentoEnte : "-";
		String bundlekeyD = bundlekey != null ? bundlekey : "-";
		String debitoreD = codUnivocoDebitore != null ? codUnivocoDebitore : "-";
		String codDominioD = codDominio != null ? codDominio : "-";
		String iuvD = iuv != null ? iuv : "-";
		
		log.debug("Richiedo la verifica per il versamento [Applicazione:" + codApplicazione + " Versamento:" + codVersamentoEnteD + " BundleKey:" + bundlekeyD + " Debitore:" + codUnivocoDebitore + " Dominio:" + codDominioD + " Iuv:" + iuvD + "] in versione (" + versione.toString() + ") alla URL ("+url+")");
		
		GpContext ctx = GpThreadLocal.get();
		String idTransaction = ctx.openTransaction();
		
		try {
			ctx.setupPaClient(codApplicazione, "paVerificaVersamento", url.toExternalForm(), versione);
			ctx.log("verifica.verifica", codApplicazione, codVersamentoEnteD, bundlekeyD, debitoreD, codDominioD, iuvD);
			
			//Chiudo la connessione al DB prima della comunicazione HTTP
			bd.closeConnection();
			
			switch (tipo) {
			case SOAP:
				PaVerificaVersamentoResponse paVerificaVersamentoResponse = null;
				try {
					PaVerificaVersamento paVerificaVersamento = new PaVerificaVersamento();
					paVerificaVersamento.getContent().add(new JAXBElement<String>(it.govpay.core.utils.VersamentoUtils._VersamentoKeyCodApplicazione_QNAME, String.class, codApplicazione));
					
					if(codVersamentoEnte != null) {
						paVerificaVersamento.getContent().add(new JAXBElement<String>(it.govpay.core.utils.VersamentoUtils._VersamentoKeyCodVersamentoEnte_QNAME, String.class, codVersamentoEnte));
					} else if(iuv != null) {
						paVerificaVersamento.getContent().add(new JAXBElement<String>(it.govpay.core.utils.VersamentoUtils._VersamentoKeyCodDominio_QNAME, String.class, codDominio));
						paVerificaVersamento.getContent().add(new JAXBElement<String>(it.govpay.core.utils.VersamentoUtils._VersamentoKeyIuv_QNAME, String.class, iuv));
					} else if(bundlekey != null) {
						paVerificaVersamento.getContent().add(new JAXBElement<String>(it.govpay.core.utils.VersamentoUtils._VersamentoKeyBundlekey_QNAME, String.class, bundlekey));
						paVerificaVersamento.getContent().add(new JAXBElement<String>(it.govpay.core.utils.VersamentoUtils._VersamentoKeyCodDominio_QNAME, String.class, codDominio));
						paVerificaVersamento.getContent().add(new JAXBElement<String>(it.govpay.core.utils.VersamentoUtils._VersamentoKeyCodUnivocoDebitore_QNAME, String.class, codUnivocoDebitore));
					}
					
					QName qname = new QName("http://www.govpay.it/servizi/pa/", "paVerificaVersamento");
					byte[] response = sendSoap("paVerificaVersamento", new JAXBElement<PaVerificaVersamento>(qname, PaVerificaVersamento.class, paVerificaVersamento), null, false);
					try {
						paVerificaVersamentoResponse = (PaVerificaVersamentoResponse) SOAPUtils.unmarshal(response, JaxbUtils.GP_PA_schema);
					} catch(UnmarshalException e) {
						String exc = e.getMessage();
						if(exc == null && e.getLinkedException() != null)
							exc = e.getLinkedException().getMessage();
						ctx.log("verifica.verificaKo", codApplicazione, codVersamentoEnteD, bundlekeyD, debitoreD, codDominioD, iuvD, "Messaggio di risposta malformato (" + exc + ")");
						throw new ClientException(exc, e);
					} catch(Exception e) {
						ctx.log("verifica.verificaKo", codApplicazione, codVersamentoEnteD, bundlekeyD, debitoreD, codDominioD, iuvD, "Errore nella deserializzazione del messaggio di risposta (" + e.getMessage() + ")");
						throw new ClientException(e);
					} 
				} finally {
					bd.setupConnection(GpThreadLocal.get().getTransactionId());
				}
				switch (paVerificaVersamentoResponse.getCodEsito()) {
				case OK:
					ctx.log("verifica.avvio", codApplicazione, codVersamentoEnteD, bundlekeyD, debitoreD, codDominioD, iuvD);
					try {
						return VersamentoUtils.toVersamentoModel(paVerificaVersamentoResponse.getVersamento(), bd);
					} catch (GovPayException e) {
						ctx.log("verifica.verificaKo", codApplicazione, codVersamentoEnteD, bundlekeyD, debitoreD, codDominioD, iuvD, "[" + e.getCodEsito() + "] " + e.getMessage());
						throw e;
					}
				case PAGAMENTO_ANNULLATO:
					ctx.log("verifica.verificaAnnullato", codApplicazione, codVersamentoEnteD, bundlekeyD, debitoreD, codDominioD, iuvD);
					throw new VersamentoAnnullatoException(paVerificaVersamentoResponse.getDescrizioneEsito());
				case PAGAMENTO_DUPLICATO:
					ctx.log("verifica.verificaDuplicato", codApplicazione, codVersamentoEnteD, bundlekeyD, debitoreD, codDominioD, iuvD);
					throw new VersamentoDuplicatoException(paVerificaVersamentoResponse.getDescrizioneEsito());
				case PAGAMENTO_SCADUTO:
					ctx.log("verifica.verificaScaduto", codApplicazione, codVersamentoEnteD, bundlekeyD, debitoreD, codDominioD, iuvD);
					throw new VersamentoScadutoException(paVerificaVersamentoResponse.getDescrizioneEsito());
				case PAGAMENTO_SCONOSCIUTO:
					ctx.log("verifica.verificaSconosciuto", codApplicazione, codVersamentoEnteD, bundlekeyD, debitoreD, codDominioD, iuvD);
					throw new VersamentoSconosciutoException();
				}
			case REST:
				return null;
			default:
				bd.setupConnection(GpThreadLocal.get().getTransactionId());
				ctx.log("verifica.verificaKo", codApplicazione, codVersamentoEnteD, codDominioD, iuvD, "Tipo del connettore (" + tipo + ") non supportato");
				throw new GovPayException(EsitoOperazione.INTERNAL, "Tipo del connettore (" + tipo + ") non supportato");
			}
		} catch (ServiceException e) {
			ctx.log("verifica.verificaKo", codApplicazione, codVersamentoEnteD, codDominioD, iuvD, e.getMessage());
			throw e;
		} finally {
			ctx.closeTransaction(idTransaction);
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
