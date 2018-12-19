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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBException;

import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.LoggerWrapperFactory;
import org.openspcoop2.utils.logger.beans.Property;
import org.openspcoop2.utils.resources.Charset;
import org.slf4j.Logger;
import org.xml.sax.SAXException;

import it.govpay.bd.BasicBD;
import it.govpay.bd.model.Applicazione;
import it.govpay.bd.model.Notifica;
import it.govpay.bd.model.Rpt;
import it.govpay.core.exceptions.GovPayException;
import it.govpay.core.exceptions.NdpException;
import it.govpay.core.utils.client.v1.NotificaConverter;
import it.govpay.core.utils.rawutils.ConverterUtils;
import it.govpay.model.Versionabile.Versione;

public class NotificaClient extends BasicClient {

	private static Logger log = LoggerWrapperFactory.getLogger(NotificaClient.class);
	private Versione versione;

	public NotificaClient(Applicazione applicazione) throws ClientException {
		super(applicazione, TipoConnettore.NOTIFICA);
		this.versione = applicazione.getConnettoreNotifica().getVersione();
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
	 */
	public void invoke(Notifica notifica, BasicBD bd) throws ClientException, ServiceException, GovPayException, JAXBException, SAXException, NdpException {

		log.debug("Spedisco la notifica di " + notifica.getTipo() + ((notifica.getIdRr() == null) ? " PAGAMENTO" : " STORNO") + " della transazione (" + notifica.getRpt(null).getCodDominio() + ")(" + notifica.getRpt(null).getIuv() + ")(" + notifica.getRpt(null).getCcp() + ") in versione (" + this.versione.toString() + ") alla URL ("+this.url+")");

			List<Property> headerProperties = new ArrayList<>();
			headerProperties.add(new Property("Accept", "application/json"));
			String jsonBody = "";
			StringBuilder sb = new StringBuilder();

			if(notifica.getIdRr() == null) {
				Rpt rpt = notifica.getRpt(null);
				
				sb.append("/pagamenti/" + rpt.getCodDominio() + "/"+ rpt.getIuv());

				Map<String, String> queryParams = new HashMap<>();
				
				if(rpt.getCodSessione() != null) {
					queryParams.put("idSession", encode(rpt.getCodSessione()));
				}

				if(rpt.getCodSessionePortale() != null) {
					queryParams.put("idSessionePortale", encode(rpt.getCodSessionePortale()));
				}

				if(rpt.getCodCarrello() != null) {
					queryParams.put("idCarrello", encode(rpt.getCodCarrello()));
				}
				
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
				
				it.govpay.ec.v1.beans.Notifica notificaRsModel = new NotificaConverter().toRsModel(notifica, rpt, bd);
				jsonBody = ConverterUtils.toJSON(notificaRsModel, null);

			} else {
				throw new ServiceException("Notifica Storno REST non implementata!");
			}
			this.sendJson(sb.toString(), jsonBody, headerProperties);
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
}
