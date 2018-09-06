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

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.UnmarshalException;
import javax.xml.namespace.QName;

import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.LoggerWrapperFactory;
import org.openspcoop2.utils.logger.beans.Property;
import org.slf4j.Logger;

import it.govpay.bd.BasicBD;
import it.govpay.bd.model.Applicazione;
import it.govpay.bd.model.Versamento;
import it.govpay.core.exceptions.GovPayException;
import it.govpay.core.exceptions.VersamentoAnnullatoException;
import it.govpay.core.exceptions.VersamentoDuplicatoException;
import it.govpay.core.exceptions.VersamentoScadutoException;
import it.govpay.core.exceptions.VersamentoSconosciutoException;
import it.govpay.core.rs.v1.beans.JSONSerializable;
import it.govpay.core.rs.v1.beans.client.PendenzaVerificata;
import it.govpay.core.rs.v1.beans.client.StatoPendenzaVerificata;
import it.govpay.core.rs.v1.costanti.EsitoOperazione;
import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.GpThreadLocal;
import it.govpay.core.utils.JaxbUtils;
import it.govpay.core.utils.VersamentoUtils;
import it.govpay.model.Connettore.Tipo;
import it.govpay.model.Versionabile.Versione;
import it.govpay.servizi.pa.ObjectFactory;
import it.govpay.servizi.pa.PaVerificaVersamento;
import it.govpay.servizi.pa.PaVerificaVersamentoResponse;

public class VerificaClient extends BasicClient {

	private static final String ERROR_MESSAGE_ERRORE_NELLA_DESERIALIZZAZIONE_DEL_MESSAGGIO_DI_RISPOSTA_0 = "Errore nella deserializzazione del messaggio di risposta ({0})";
	private static final String AZIONE_SOAP_PA_VERIFICA_VERSAMENTO = "paVerificaVersamento";
	private static final String LOG_KEY_VERIFICA_VERIFICA_KO = "verifica.verificaKo";
	private static Logger log = LoggerWrapperFactory.getLogger(VerificaClient.class);
	private Versione versione;
	private Tipo tipo;
	private static ObjectFactory objectFactory;
	private String codApplicazione;
	
	
	public VerificaClient(Applicazione applicazione) throws ClientException {
		super(applicazione, TipoConnettore.VERIFICA);
		this.versione = applicazione.getConnettoreVerifica().getVersione();
		this.codApplicazione = applicazione.getCodApplicazione();
		this.tipo = applicazione.getConnettoreVerifica().getTipo();
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
		
		log.debug("Richiedo la verifica per il versamento [Applicazione:" + this.codApplicazione + " Versamento:" + codVersamentoEnteD + " BundleKey:" + bundlekeyD + " Debitore:" + codUnivocoDebitore + " Dominio:" + codDominioD + " Iuv:" + iuvD + "] in versione (" + this.versione.toString() + ") alla URL ("+this.url+")");
		
		GpContext ctx = GpThreadLocal.get();
		String idTransaction = ctx.openTransaction();
		
		try {
			ctx.setupPaClient(this.codApplicazione, AZIONE_SOAP_PA_VERIFICA_VERSAMENTO, this.url.toExternalForm(), this.versione);
			ctx.log("verifica.verifica", this.codApplicazione, codVersamentoEnteD, bundlekeyD, debitoreD, codDominioD, iuvD);
			
			//Chiudo la connessione al DB prima della comunicazione HTTP
			bd.closeConnection();
			
			switch (this.tipo) {
			case SOAP:
				PaVerificaVersamentoResponse paVerificaVersamentoResponse = null;
				try {
					PaVerificaVersamento paVerificaVersamento = new PaVerificaVersamento();
					paVerificaVersamento.getContent().add(new JAXBElement<>(it.govpay.core.utils.VersamentoUtils._VersamentoKeyCodApplicazione_QNAME, String.class, this.codApplicazione));
					
					if(codVersamentoEnte != null) {
						paVerificaVersamento.getContent().add(new JAXBElement<>(it.govpay.core.utils.VersamentoUtils._VersamentoKeyCodVersamentoEnte_QNAME, String.class, codVersamentoEnte));
					} else if(iuv != null) {
						paVerificaVersamento.getContent().add(new JAXBElement<>(it.govpay.core.utils.VersamentoUtils._VersamentoKeyCodDominio_QNAME, String.class, codDominio));
						paVerificaVersamento.getContent().add(new JAXBElement<>(it.govpay.core.utils.VersamentoUtils._VersamentoKeyIuv_QNAME, String.class, iuv));
					} else if(bundlekey != null) {
						paVerificaVersamento.getContent().add(new JAXBElement<>(it.govpay.core.utils.VersamentoUtils._VersamentoKeyBundlekey_QNAME, String.class, bundlekey));
						paVerificaVersamento.getContent().add(new JAXBElement<>(it.govpay.core.utils.VersamentoUtils._VersamentoKeyCodDominio_QNAME, String.class, codDominio));
						paVerificaVersamento.getContent().add(new JAXBElement<>(it.govpay.core.utils.VersamentoUtils._VersamentoKeyCodUnivocoDebitore_QNAME, String.class, codUnivocoDebitore));
					}
					
					QName qname = new QName("http://www.govpay.it/servizi/pa/", AZIONE_SOAP_PA_VERIFICA_VERSAMENTO);
					byte[] response = this.sendSoap(AZIONE_SOAP_PA_VERIFICA_VERSAMENTO, new JAXBElement<>(qname, PaVerificaVersamento.class, paVerificaVersamento), null, false);
					try {
						paVerificaVersamentoResponse = (PaVerificaVersamentoResponse) SOAPUtils.unmarshal(response, JaxbUtils.GP_PA_schema);
					} catch(UnmarshalException e) {
						String exc = e.getMessage();
						if(exc == null && e.getLinkedException() != null)
							exc = e.getLinkedException().getMessage();
						ctx.log(LOG_KEY_VERIFICA_VERIFICA_KO, this.codApplicazione, codVersamentoEnteD, bundlekeyD, debitoreD, codDominioD, iuvD, "Messaggio di risposta malformato (" + exc + ")");
						throw new ClientException(exc, e);
					} catch(Exception e) {
						String logErrorMessage = MessageFormat.format(ERROR_MESSAGE_ERRORE_NELLA_DESERIALIZZAZIONE_DEL_MESSAGGIO_DI_RISPOSTA_0,	e.getMessage());
						ctx.log(LOG_KEY_VERIFICA_VERIFICA_KO, this.codApplicazione, codVersamentoEnteD, bundlekeyD, debitoreD, codDominioD, iuvD, logErrorMessage);
						throw new ClientException(e);
					} 
				} finally {
					bd.setupConnection(GpThreadLocal.get().getTransactionId());
				}
				switch (paVerificaVersamentoResponse.getCodEsito()) {
				case OK:
					ctx.log("verifica.avvio", this.codApplicazione, codVersamentoEnteD, bundlekeyD, debitoreD, codDominioD, iuvD);
					try {
						return VersamentoUtils.toVersamentoModel(paVerificaVersamentoResponse.getVersamento(), bd);
					} catch (GovPayException e) {
						ctx.log(LOG_KEY_VERIFICA_VERIFICA_KO, this.codApplicazione, codVersamentoEnteD, bundlekeyD, debitoreD, codDominioD, iuvD, "[" + e.getCodEsito() + "] " + e.getMessage());
						throw e;
					}
				case PAGAMENTO_ANNULLATO:
					ctx.log("verifica.verificaAnnullato", this.codApplicazione, codVersamentoEnteD, bundlekeyD, debitoreD, codDominioD, iuvD);
					throw new VersamentoAnnullatoException(paVerificaVersamentoResponse.getDescrizioneEsito());
				case PAGAMENTO_DUPLICATO:
					ctx.log("verifica.verificaDuplicato", this.codApplicazione, codVersamentoEnteD, bundlekeyD, debitoreD, codDominioD, iuvD);
					throw new VersamentoDuplicatoException(paVerificaVersamentoResponse.getDescrizioneEsito());
				case PAGAMENTO_SCADUTO:
					ctx.log("verifica.verificaScaduto", this.codApplicazione, codVersamentoEnteD, bundlekeyD, debitoreD, codDominioD, iuvD);
					throw new VersamentoScadutoException(paVerificaVersamentoResponse.getDescrizioneEsito());
				case PAGAMENTO_SCONOSCIUTO:
					ctx.log("verifica.verificaSconosciuto", this.codApplicazione, codVersamentoEnteD, bundlekeyD, debitoreD, codDominioD, iuvD);
					throw new VersamentoSconosciutoException();
				}
			case REST:
				List<Property> headerProperties = new ArrayList<>();
				headerProperties.add(new Property("Accept", "application/json"));
				String jsonResponse = "";
				
				String path = null;
				
				if(iuv == null) {
					path = "/pendenze/" + this.codApplicazione + "/" + codVersamentoEnte;
				} else {
					path = "/avvisi/" + codDominio + "/" + iuv;
				}
				
				StatoPendenzaVerificata stato = null;
				PendenzaVerificata pendenzaVerificata = null;
				try {
					try {
						jsonResponse = new String(this.getJson(path, headerProperties));
						pendenzaVerificata = JSONSerializable.parse(jsonResponse, PendenzaVerificata.class); 
					}catch(ClientException e) {
						String logErrorMessage = MessageFormat.format(ERROR_MESSAGE_ERRORE_NELLA_DESERIALIZZAZIONE_DEL_MESSAGGIO_DI_RISPOSTA_0,	e.getMessage());
						ctx.log(LOG_KEY_VERIFICA_VERIFICA_KO, this.codApplicazione, codVersamentoEnteD, bundlekeyD, debitoreD, codDominioD, iuvD, logErrorMessage);
						throw e;
					}catch(Exception e) {
						String logErrorMessage = MessageFormat.format(ERROR_MESSAGE_ERRORE_NELLA_DESERIALIZZAZIONE_DEL_MESSAGGIO_DI_RISPOSTA_0,	e.getMessage());
						ctx.log(LOG_KEY_VERIFICA_VERIFICA_KO, this.codApplicazione, codVersamentoEnteD, bundlekeyD, debitoreD, codDominioD, iuvD, logErrorMessage);
						throw new ClientException(e);
					}
				} finally {
					bd.setupConnection(GpThreadLocal.get().getTransactionId());
				}
				
				stato = pendenzaVerificata.getStato();
				switch (stato) {
					case NON_ESEGUITA: // CASO OK su
						ctx.log("verifica.avvio", this.codApplicazione, codVersamentoEnteD, bundlekeyD, debitoreD, codDominioD, iuvD);
						try {
							return it.govpay.core.business.VersamentoUtils.toVersamentoModel(VersamentoUtils.getVersamentoFromPendenzaVerificata(pendenzaVerificata),bd);
						} catch (GovPayException e) {
							ctx.log(LOG_KEY_VERIFICA_VERIFICA_KO, this.codApplicazione, codVersamentoEnteD, bundlekeyD, debitoreD, codDominioD, iuvD, "[" + e.getCodEsito() + "] " + e.getMessage());
							throw e;
						}
				case ANNULLATA:
					ctx.log("verifica.verificaAnnullato", this.codApplicazione, codVersamentoEnteD, bundlekeyD, debitoreD, codDominioD, iuvD);
					throw new VersamentoAnnullatoException(pendenzaVerificata.getDescrizioneStato());
				case DUPLICATA:
					ctx.log("verifica.verificaDuplicato", this.codApplicazione, codVersamentoEnteD, bundlekeyD, debitoreD, codDominioD, iuvD);
					throw new VersamentoDuplicatoException(pendenzaVerificata.getDescrizioneStato());
				case SCADUTA:
					ctx.log("verifica.verificaScaduto", this.codApplicazione, codVersamentoEnteD, bundlekeyD, debitoreD, codDominioD, iuvD);
					throw new VersamentoScadutoException(pendenzaVerificata.getDescrizioneStato());
				case SCONOSCIUTA:
					ctx.log("verifica.verificaSconosciuto", this.codApplicazione, codVersamentoEnteD, bundlekeyD, debitoreD, codDominioD, iuvD);
					throw new VersamentoSconosciutoException();
				}
			default:
				bd.setupConnection(GpThreadLocal.get().getTransactionId());
				ctx.log(LOG_KEY_VERIFICA_VERIFICA_KO, this.codApplicazione, codVersamentoEnteD, codDominioD, iuvD, "Tipo del connettore (" + this.tipo + ") non supportato");
				throw new GovPayException(EsitoOperazione.INTERNAL, "Tipo del connettore (" + this.tipo + ") non supportato");
			}
		} catch (ServiceException e) {
			ctx.log(LOG_KEY_VERIFICA_VERIFICA_KO, this.codApplicazione, codVersamentoEnteD, codDominioD, iuvD, e.getMessage());
			throw e;
		} finally {
			ctx.closeTransaction(idTransaction);
		}
	}
	
	public class SendEsitoResponse {

		private int responseCode;
		private String detail;
		public int getResponseCode() {
			return this.responseCode;
		}
		public void setResponseCode(int responseCode) {
			this.responseCode = responseCode;
		}
		public String getDetail() {
			return this.detail;
		}
		public void setDetail(String detail) {
			this.detail = detail;
		}
	}
}
