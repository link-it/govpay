/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2026 Link.it srl (http://www.link.it).
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

import org.openspcoop2.utils.LoggerWrapperFactory;
import org.openspcoop2.utils.logger.beans.Property;
import org.slf4j.Logger;

import it.govpay.bd.model.Dominio;
import it.govpay.core.beans.EventoContext;
import it.govpay.core.exceptions.IOException;
import it.govpay.core.utils.client.beans.NotificationPriceResponse;
import it.govpay.core.utils.client.beans.TipoConnettore;
import it.govpay.core.utils.client.beans.TipoDestinatario;
import it.govpay.core.utils.client.exception.ClientException;
import it.govpay.core.utils.client.exception.ClientInitializeException;
import it.govpay.core.utils.rawutils.ConverterUtils;
import it.govpay.model.Connettore;

public class SendClient extends BasicClientCORE {

	private static final String APPLICATION_JSON = "application/json";
	private static final String HEADER_ACCEPT = "Accept";
	private static final String NOTIFICATION_PRICE_OPERATION_PATH = "/delivery/v2.3/price/{0}/{1}";
	private static final String NOTIFICATION_PRICE_OPERATION_ID = "notificationPriceV23";

	private static Logger log = LoggerWrapperFactory.getLogger(SendClient.class);

	public SendClient(Dominio dominio, Connettore connettore, EventoContext eventoCtx) throws ClientInitializeException {
		super(dominio, TipoConnettore.SEND, TipoDestinatario.SEND, connettore, eventoCtx);
		this.operationID = NOTIFICATION_PRICE_OPERATION_ID;
	}

	/**
	 * Interroga il servizio SEND NotificationPriceV23 per recuperare l'importo delle spese
	 * di notifica associate all'avviso, espresso in eurocent.
	 *
	 * @param paTaxId codice fiscale dell'ente creditore (codDominio)
	 * @param noticeCode numero avviso (IUV) della pendenza
	 */
	public Long recuperaImportoNotifica(String paTaxId, String noticeCode) throws ClientException {
		List<Property> headerProperties = new ArrayList<>();
		headerProperties.add(new Property(HEADER_ACCEPT, APPLICATION_JSON));

		String path = MessageFormat.format(NOTIFICATION_PRICE_OPERATION_PATH, paTaxId, noticeCode);

		try {
			String jsonResponse = new String(this.getJson(path, headerProperties, NOTIFICATION_PRICE_OPERATION_ID));
			NotificationPriceResponse response = ConverterUtils.parse(jsonResponse, NotificationPriceResponse.class);
			return response.getAmount();
		} catch (IOException e) {
			log.warn("Errore durante la deserializzazione della risposta del servizio SEND: " + e.getMessage(), e);
			throw new ClientException("Errore nella deserializzazione della risposta del servizio SEND: " + e.getMessage(), e);
		}
	}

	@Override
	public String getOperationId() {
		return this.operationID;
	}
}
