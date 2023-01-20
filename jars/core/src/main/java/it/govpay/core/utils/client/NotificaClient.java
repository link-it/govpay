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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.LoggerWrapperFactory;
import org.openspcoop2.utils.logger.beans.Property;
import org.openspcoop2.utils.resources.Charset;
import org.openspcoop2.utils.transport.http.HttpRequestMethod;
import org.slf4j.Logger;

import it.govpay.bd.model.Applicazione;
import it.govpay.bd.model.Pagamento;
import it.govpay.bd.model.Rpt;
import it.govpay.bd.model.Versamento;
import it.govpay.core.beans.EventoContext.Componente;
import it.govpay.core.ec.v1.converter.NotificaAttivazioneConverter;
import it.govpay.core.ec.v1.converter.NotificaTerminazioneConverter;
import it.govpay.core.ec.v2.converter.RicevuteConverter;
import it.govpay.core.exceptions.GovPayException;
import it.govpay.core.exceptions.IOException;
import it.govpay.core.utils.RptUtils;
import it.govpay.core.utils.client.beans.TipoConnettore;
import it.govpay.core.utils.client.exception.ClientException;
import it.govpay.core.utils.rawutils.ConverterUtils;
import it.govpay.model.Notifica;
import it.govpay.model.Versionabile.Versione;
import it.govpay.model.configurazione.Giornale;

public class NotificaClient extends BasicClientCORE implements INotificaClient {

	private static final String NOTIFICHE_V1_NOTIFY_PAGAMENTO_OPERATION_ID = "notifyPagamento";
	private static final String NOTIFICHE_V1_NOTIFY_PAGAMENTO_OPERATION_PATH = "/pagamenti/{0}/{1}";
	
	private static final String NOTIFICHE_V2_NOTIFICA_RICEVUTA_OPERATION_ID = "notificaRicevuta";
	private static final String NOTIFICHE_V2_NOTIFICA_RICEVUTA_OPERATION_PATH = "/ricevute/{0}/{1}/{2}";
	
	private static Logger log = LoggerWrapperFactory.getLogger(NotificaClient.class);
	private Versione versione;
	private Applicazione applicazione;
	private Rpt rpt;
	private Versamento versamento;
	private List<Pagamento> pagamenti;

	public NotificaClient(Applicazione applicazione, Rpt rpt, Versamento versamento, List<Pagamento> pagamenti, String operationID, Giornale giornale) throws ClientException, ServiceException {
		super(applicazione, TipoConnettore.NOTIFICA);
		this.versione = applicazione.getConnettoreIntegrazione().getVersione();
		this.operationID = operationID;
		this.applicazione = applicazione;
		this.rpt = rpt;
		this.versamento = versamento;
		this.pagamenti = pagamenti;

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
	 * @throws IOException 
	 */
	public byte[] invoke(Notifica notifica) throws ClientException, GovPayException {
		
		switch (this.versione) {
		case GP_REST_01:
			return inviaNotificaConConnettoreV1(notifica);
		case GP_REST_02:
			return inviaNotificaConConnettoreV2(notifica);
		case GP_SOAP_03:
		default:
			throw new ClientException("Versione ["+this.versione+"] non supportata per l'operazione di notifica");
		}
	}

	private byte[] inviaNotificaConConnettoreV1(Notifica notifica) throws GovPayException, ClientException {
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
			throw new ClientException("Notifica RPT["+this.getRptKey() +"] di tipo ["+notifica.getTipo()+"] non verra' spedita verso l'applicazione.");
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

		try {
			jsonBody = this.getMessaggioRichiestaApiV1(notifica, rpt, applicazione, versamento, pagamenti);
		} catch (IOException e) {
			throw new GovPayException(e);
		}

		return this.sendJson(sb.toString(), jsonBody.getBytes(), headerProperties, httpMethod, swaggerOperationID);
	}

	public String getSwaggerOperationIdApiV1(Notifica notifica, Rpt rpt) { 
		String swaggerOperationID = "";

		switch (notifica.getTipo()) {
		case ATTIVAZIONE:
		case RICEVUTA:
			swaggerOperationID = NOTIFICHE_V1_NOTIFY_PAGAMENTO_OPERATION_ID;
			break;
		case FALLIMENTO:
		case ANNULLAMENTO:
			log.warn("Notifica RPT["+this.getRptKey() +"] di tipo ["+notifica.getTipo()+"] non verra' spedita verso l'applicazione.");
			break;
		}

		return swaggerOperationID;
	}

	private String getMessaggioRichiestaApiV1(Notifica notifica, Rpt rpt, Applicazione applicazione, Versamento versamento, List<Pagamento> pagamenti) throws IOException {
		String jsonBody = "";

		switch (notifica.getTipo()) {
		case ATTIVAZIONE:
			it.govpay.ec.v1.beans.Notifica notificaAttivazioneRsModel = new NotificaAttivazioneConverter().toRsModel(notifica, rpt, applicazione, versamento, pagamenti);
			jsonBody = ConverterUtils.toJSON(notificaAttivazioneRsModel);
			break;
		case RICEVUTA:
			it.govpay.ec.v1.beans.Notifica notificaTerminazioneRsModel = new NotificaTerminazioneConverter().toRsModel(notifica, rpt, applicazione, versamento, pagamenti);
			jsonBody = ConverterUtils.toJSON(notificaTerminazioneRsModel);
			break;
		case FALLIMENTO:
		case ANNULLAMENTO:
			log.warn("Notifica RPT["+this.getRptKey() +"] di tipo ["+notifica.getTipo()+"] non verra' spedita verso l'applicazione.");
			break;
		}

		return jsonBody;
	}
	
	private byte[] inviaNotificaConConnettoreV2(Notifica notifica) throws GovPayException, ClientException {
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
			throw new ClientException("Notifica RPT["+this.getRptKey() +"] di tipo ["+notifica.getTipo()+"] non verra' spedita verso l'applicazione.");
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

		try {
			jsonBody = this.getMessaggioRichiestaApiV2(notifica, rpt, applicazione, versamento, pagamenti);
		} catch (ServiceException e) {
			throw new GovPayException(e);
		} catch (UnsupportedEncodingException e) {
			throw new GovPayException(e);
		} catch (IOException e) {
			throw new GovPayException(e);
		}

		return this.sendJson(sb.toString(), jsonBody.getBytes(), headerProperties, httpMethod, swaggerOperationID);
	}
	
	public String getSwaggerOperationIdApiV2(Notifica notifica, Rpt rpt) { 
		String swaggerOperationID = "";

		switch (notifica.getTipo()) {
		case RICEVUTA:
			swaggerOperationID = NOTIFICHE_V2_NOTIFICA_RICEVUTA_OPERATION_ID;
			break;
		case ATTIVAZIONE:
		case FALLIMENTO:
		case ANNULLAMENTO:
			log.warn("Notifica RPT["+this.getRptKey() +"] di tipo ["+notifica.getTipo()+"] non verra' spedita verso l'applicazione.");
			break;
		}

		return swaggerOperationID;
	}
	
	private String getMessaggioRichiestaApiV2(Notifica notifica, Rpt rpt, Applicazione applicazione, Versamento versamento, List<Pagamento> pagamenti) throws ServiceException, IOException, UnsupportedEncodingException {
		String jsonBody = "";

		switch (notifica.getTipo()) {
		case RICEVUTA:
			it.govpay.ec.v2.beans.Ricevuta notificaRicevutaRsModel = RicevuteConverter.toRsModel(notifica, rpt, applicazione, versamento, pagamenti);
			jsonBody = ConverterUtils.toJSON(notificaRicevutaRsModel);
			break;
		case ATTIVAZIONE:
		case FALLIMENTO:
		case ANNULLAMENTO:
			log.warn("Notifica RPT["+this.getRptKey() +"] di tipo ["+notifica.getTipo()+"] non verra' spedita verso l'applicazione.");
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
	
	public String getRptKey() {
		return RptUtils.getRptKey(this.rpt);
	}
}
