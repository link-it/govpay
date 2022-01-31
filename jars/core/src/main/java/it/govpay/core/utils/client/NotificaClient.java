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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.namespace.QName;

import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.LoggerWrapperFactory;
import org.openspcoop2.utils.UtilsException;
import org.openspcoop2.utils.json.ValidationException;
import org.openspcoop2.utils.logger.beans.Property;
import org.openspcoop2.utils.resources.Charset;
import org.openspcoop2.utils.transport.http.HttpRequestMethod;
import org.slf4j.Logger;
import org.xml.sax.SAXException;

import it.govpay.bd.configurazione.model.Giornale;
import it.govpay.bd.model.Applicazione;
import it.govpay.bd.model.Notifica;
import it.govpay.bd.model.Pagamento;
import it.govpay.bd.model.PagamentoPortale;
import it.govpay.bd.model.Rpt;
import it.govpay.bd.model.Versamento;
import it.govpay.core.ec.v1.converter.NotificaAttivazioneConverter;
import it.govpay.core.ec.v1.converter.NotificaTerminazioneConverter;
import it.govpay.core.ec.v2.converter.RicevuteConverter;
import it.govpay.core.exceptions.GovPayException;
import it.govpay.core.exceptions.NdpException;
import it.govpay.core.legacy.utils.Gp21Utils;
import it.govpay.core.utils.EventoContext.Componente;
import it.govpay.core.utils.client.beans.TipoConnettore;
import it.govpay.core.utils.client.exception.ClientException;
import it.govpay.core.utils.rawutils.ConverterUtils;
import it.govpay.core.utils.thread.InviaNotificaThread;
import it.govpay.model.Versionabile.Versione;
import it.govpay.servizi.pa.PaNotificaTransazione;

public class NotificaClient extends BasicClientCORE {

	private static final String NOTIFICHE_V1_NOTIFY_PAGAMENTO_OPERATION_ID = "notifyPagamento";
	private static final String NOTIFICHE_V1_NOTIFY_PAGAMENTO_OPERATION_PATH = "/pagamenti/{0}/{1}";

	private static final String NOTIFICHE_V2_NOTIFICA_RICEVUTA_OPERATION_ID = "notificaRicevuta";
	private static final String NOTIFICHE_V2_NOTIFICA_RICEVUTA_OPERATION_PATH = "/ricevute/{0}/{1}/{2}";

	private static Logger log = LoggerWrapperFactory.getLogger(NotificaClient.class);
	private Versione versione;

	public NotificaClient(Applicazione applicazione, String operationID, Giornale giornale) throws ClientException, ServiceException {
		super(applicazione, TipoConnettore.NOTIFICA);
		this.versione = applicazione.getConnettoreIntegrazione().getVersione();
		this.operationID = operationID;

		this.componente = Componente.API_ENTE;
		this.setGiornale(giornale);
		this.getEventoCtx().setComponente(this.componente); 
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
	 * @throws SAXException 
	 * @throws JAXBException 
	 * @throws NdpException 
	 * @throws UtilsException 
	 * @throws ValidationException 
	 * @throws IOException 
	 */
	public byte[] invoke(Notifica notifica, Rpt rpt, Applicazione applicazione, Versamento versamento, List<Pagamento> pagamenti,
			PagamentoPortale pagamentoPortale) throws ClientException, ServiceException, GovPayException, JAXBException, SAXException, NdpException, UtilsException, ValidationException, IOException {

		switch (this.versione) {
		case GP_REST_01:
			return inviaNotificaConConnettoreV1(notifica, rpt, applicazione, versamento, pagamenti);
		case GP_REST_02:
			return inviaNotificaConConnettoreV2(notifica, rpt, applicazione, versamento, pagamenti);
		case GP_SOAP_01:
			return inviaNotificaConConnettoreSoapV1(notifica, rpt, applicazione, versamento, pagamenti);
		default:
			throw new ClientException("Versione ["+this.versione+"] non supportata per l'operazione di notifica");
		}
	}

	private byte[] inviaNotificaConConnettoreSoapV1(Notifica notifica, Rpt rpt, Applicazione applicazione,
			Versamento versamento, List<Pagamento> pagamenti) throws ClientException, ServiceException, JAXBException, SAXException, IOException {
		String codDominio = rpt.getCodDominio();
		String iuv = rpt.getIuv();
		String ccp = rpt.getCcp();
		log.debug("Spedisco la notifica di " + notifica.getTipo() + " PAGAMENTO della transazione (" + codDominio + ")(" + iuv + ")(" + ccp + ") col connettore versione (" + this.versione.toString() + ") alla URL ("+this.url+")");

		List<Property> headerProperties = new ArrayList<>();
		headerProperties.add(new Property("Accept", "text/xml"));

		PaNotificaTransazione paNotificaTransazione = new PaNotificaTransazione();
		paNotificaTransazione.setCodApplicazione(applicazione.getCodApplicazione());
		paNotificaTransazione.setCodVersamentoEnte(versamento.getCodVersamentoEnte());
		paNotificaTransazione.setTransazione(Gp21Utils.toTransazione(rpt, null));

		paNotificaTransazione.setCodSessionePortale(rpt.getCodSessionePortale());

		QName qname = new QName("http://www.govpay.it/servizi/pa/", "paNotificaTransazione");
		JAXBElement<PaNotificaTransazione> jaxbEl =  new JAXBElement<PaNotificaTransazione>(qname, PaNotificaTransazione.class, paNotificaTransazione);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		SOAPUtils.writeMessage(jaxbEl, null, baos);

		return this.sendSoap(InviaNotificaThread.PA_NOTIFICA_TRANSAZIONE, baos.toByteArray(), this.connettore.isAzioneInUrl());
	}

	private byte[] inviaNotificaConConnettoreV1(Notifica notifica, Rpt rpt, Applicazione applicazione, Versamento versamento,
			List<Pagamento> pagamenti) throws ServiceException, ClientException, JAXBException, SAXException {
		String codDominio = rpt.getCodDominio();
		String iuv = rpt.getIuv();
		String ccp = rpt.getCcp();
		log.debug("Spedisco la notifica di " + notifica.getTipo() + " PAGAMENTO della transazione (" + codDominio + ")(" + iuv + ")(" + ccp + ") col connettore versione (" + this.versione.toString() + ") alla URL ("+this.url+")");

		List<Property> headerProperties = new ArrayList<>();
		headerProperties.add(new Property("Accept", "application/json"));
		String jsonBody = "";
		StringBuilder sb = new StringBuilder();
		Map<String, String> queryParams = new HashMap<>();
		HttpRequestMethod httpMethod = HttpRequestMethod.POST;
		String swaggerOperationID = this.getSwaggerOperationIdApiV1(notifica, rpt);

		switch (notifica.getTipo()) {
		case ATTIVAZIONE:
		case RICEVUTA:
			sb.append(MessageFormat.format(NOTIFICHE_V1_NOTIFY_PAGAMENTO_OPERATION_PATH, codDominio, iuv));
			if(rpt.getCodSessione() != null) {
				queryParams.put("idSession", encode(rpt.getCodSessione()));
			}

			if(rpt.getCodSessionePortale() != null) {
				queryParams.put("idSessionePortale", encode(rpt.getCodSessionePortale()));
			}

			if(rpt.getCodCarrello() != null) {
				queryParams.put("idCarrello", encode(rpt.getCodCarrello()));
			}
			break;
		case FALLIMENTO:
		case ANNULLAMENTO:
			throw new ClientException("Notifica RPT["+notifica.getRptKey() +"] di tipo ["+notifica.getTipo()+"] non verra' spedita verso l'applicazione.");
		}

		// composizione URL
		boolean amp = false;
		for (String key : queryParams.keySet()) {
			if(amp) {
				sb.append("&");
			} else {
				sb.append("?");
				amp = true;
			}

			sb.append(key).append("=").append(queryParams.get(key));
		}

		jsonBody = this.getMessaggioRichiestaApiV1(notifica, rpt, applicazione, versamento, pagamenti);

		return this.sendJson(sb.toString(), jsonBody.getBytes(), headerProperties, httpMethod, swaggerOperationID);
	}

	public String getSwaggerOperationIdApiV1(Notifica notifica, Rpt rpt) throws ServiceException { 
		String swaggerOperationID = "";

		switch (notifica.getTipo()) {
		case ATTIVAZIONE:
		case RICEVUTA:
			swaggerOperationID = NOTIFICHE_V1_NOTIFY_PAGAMENTO_OPERATION_ID;
			break;
		case FALLIMENTO:
		case ANNULLAMENTO:
			log.warn("Notifica RPT["+notifica.getRptKey() +"] di tipo ["+notifica.getTipo()+"] non verra' spedita verso l'applicazione.");
			break;
		}

		return swaggerOperationID;
	}

	private String getMessaggioRichiestaApiV1(Notifica notifica, Rpt rpt, Applicazione applicazione, Versamento versamento, List<Pagamento> pagamenti) throws ServiceException, JAXBException, SAXException {
		String jsonBody = "";

		switch (notifica.getTipo()) {
		case ATTIVAZIONE:
			it.govpay.ec.v1.beans.Notifica notificaAttivazioneRsModel = new NotificaAttivazioneConverter().toRsModel(notifica, rpt, applicazione, versamento, pagamenti);
			jsonBody = ConverterUtils.toJSON(notificaAttivazioneRsModel, null);
			break;
		case RICEVUTA:
			it.govpay.ec.v1.beans.Notifica notificaTerminazioneRsModel = new NotificaTerminazioneConverter().toRsModel(notifica, rpt, applicazione, versamento, pagamenti);
			jsonBody = ConverterUtils.toJSON(notificaTerminazioneRsModel, null);
			break;
		case FALLIMENTO:
		case ANNULLAMENTO:
			log.warn("Notifica RPT["+notifica.getRptKey() +"] di tipo ["+notifica.getTipo()+"] non verra' spedita verso l'applicazione.");
			break;
		}

		return jsonBody;
	}

	private byte[] inviaNotificaConConnettoreV2(Notifica notifica, Rpt rpt, Applicazione applicazione, Versamento versamento,
			List<Pagamento> pagamenti) throws ServiceException, ClientException, JAXBException, SAXException, ValidationException {
		String codDominio = rpt.getCodDominio();
		String iuv = rpt.getIuv();
		String ccp = rpt.getCcp();
		log.debug("Spedisco la notifica di " + notifica.getTipo() + " PAGAMENTO della transazione (" + codDominio + ")(" + iuv + ")(" + ccp + ") col connettore versione (" + this.versione.toString() + ") alla URL ("+this.url+")");

		List<Property> headerProperties = new ArrayList<>();
		headerProperties.add(new Property("Accept", "application/json"));
		String jsonBody = "";
		StringBuilder sb = new StringBuilder();
		Map<String, String> queryParams = new HashMap<>();
		HttpRequestMethod httpMethod = HttpRequestMethod.PUT;
		String swaggerOperationID = this.getSwaggerOperationIdApiV2(notifica, rpt);

		switch (notifica.getTipo()) {

		case RICEVUTA:
			sb.append(MessageFormat.format(NOTIFICHE_V2_NOTIFICA_RICEVUTA_OPERATION_PATH, codDominio, iuv, ccp));
			if(rpt.getCodSessione() != null) {
				queryParams.put("idSession", encode(rpt.getCodSessione()));
			}

			if(rpt.getCodSessionePortale() != null) {
				queryParams.put("idSessionePortale", encode(rpt.getCodSessionePortale()));
			}

			if(rpt.getCodCarrello() != null) {
				queryParams.put("idCarrello", encode(rpt.getCodCarrello()));
			}
			break;
		case ATTIVAZIONE:
		case FALLIMENTO:
		case ANNULLAMENTO:
			throw new ClientException("Notifica RPT["+notifica.getRptKey() +"] di tipo ["+notifica.getTipo()+"] non verra' spedita verso l'applicazione.");
		}

		// composizione URL
		boolean amp = false;
		for (String key : queryParams.keySet()) {
			if(amp) {
				sb.append("&");
			} else {
				sb.append("?");
				amp = true;
			}

			sb.append(key).append("=").append(queryParams.get(key));
		}

		jsonBody = this.getMessaggioRichiestaApiV2(notifica, rpt, applicazione, versamento, pagamenti);

		return this.sendJson(sb.toString(), jsonBody.getBytes(), headerProperties, httpMethod, swaggerOperationID);
	}

	public String getSwaggerOperationIdApiV2(Notifica notifica, Rpt rpt) throws ServiceException { 
		String swaggerOperationID = "";

		switch (notifica.getTipo()) {
		case RICEVUTA:
			swaggerOperationID = NOTIFICHE_V2_NOTIFICA_RICEVUTA_OPERATION_ID;
			break;
		case ATTIVAZIONE:
		case FALLIMENTO:
		case ANNULLAMENTO:
			log.warn("Notifica RPT["+notifica.getRptKey() +"] di tipo ["+notifica.getTipo()+"] non verra' spedita verso l'applicazione.");
			break;
		}

		return swaggerOperationID;
	}

	private String getMessaggioRichiestaApiV2(Notifica notifica, Rpt rpt, Applicazione applicazione, Versamento versamento, List<Pagamento> pagamenti) throws ServiceException, ValidationException {
		String jsonBody = "";

		switch (notifica.getTipo()) {
		case RICEVUTA:
			it.govpay.ec.v2.beans.Ricevuta notificaRicevutaRsModel = RicevuteConverter.toRsModel(notifica, rpt, applicazione, versamento, pagamenti);
			jsonBody = ConverterUtils.toJSON(notificaRicevutaRsModel, null);
			break;
		case ATTIVAZIONE:
		case FALLIMENTO:
		case ANNULLAMENTO:
			log.warn("Notifica RPT["+notifica.getRptKey() +"] di tipo ["+notifica.getTipo()+"] non verra' spedita verso l'applicazione.");
			break;
		}

		return jsonBody;
	}

	private String encode(String value) {
		try {
			return URLEncoder.encode(value, Charset.UTF_8.getValue());
		} catch (UnsupportedEncodingException e) {
			return "";
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

	@Override
	public String getOperationId() {
		return this.operationID;
	}
}
