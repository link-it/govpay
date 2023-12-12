/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2023 Link.it srl (http://www.link.it).
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

import java.util.ArrayList;
import java.util.List;

import org.openspcoop2.utils.LoggerWrapperFactory;
import org.openspcoop2.utils.logger.beans.Property;
import org.openspcoop2.utils.transport.http.HttpRequestMethod;
import org.slf4j.Logger;
import org.springframework.http.HttpHeaders;

import it.govpay.core.beans.EventoContext.Componente;
import it.govpay.core.beans.checkout.CartRequest;
import it.govpay.core.exceptions.GovPayException;
import it.govpay.core.exceptions.IOException;
import it.govpay.core.utils.client.beans.TipoDestinatario;
import it.govpay.core.utils.client.exception.ClientException;
import it.govpay.model.Connettore;
import it.govpay.model.Connettore.EnumAuthType;
import it.govpay.model.configurazione.Giornale;

public class CheckoutClient extends BasicClientCORE {

	private static Logger log = LoggerWrapperFactory.getLogger(CheckoutClient.class);
	
	public static final String SWAGGER_OPERATION_POST_CARTS_OPERATION_ID = "PostCarts";
	private static final String CHECKOUT_V1_CARTS_OPERATION_PATH = "/carts";
	
	public CheckoutClient(String operazioneSwaggerCheckout, String checkoutUrl, String operationID, Giornale giornale) throws ClientException { 
		super(operazioneSwaggerCheckout, TipoDestinatario.CHECKOUT_PAGOPA, getConnettore(checkoutUrl)); 

		this.operationID = operationID;
		this.componente = Componente.API_PAGOPA;
		this.setGiornale(giornale);
		this.getEventoCtx().setComponente(this.componente);
	}
	
	/**
	 * Esegue la chiamata verso il checkout PagoPA e restituisce la location verso la quale reinderizzare l'utente.
	 * 
	 * @param cartRequest Richiesta di pagamento compilata.
	 * @return
	 * @throws ClientException Errori di comunicazione
	 * @throws GovPayException Errori interni
	 */
	public String inviaCartRequest(CartRequest cartRequest) throws ClientException, GovPayException {
		log.debug("Spedisco richiesta verso il Checkout alla URL ("+this.url+")");
		List<Property> headerProperties = new ArrayList<>();
		headerProperties.add(new Property("Accept", "application/json"));
		String jsonBody = "";
		StringBuilder sb = new StringBuilder();
//		Map<String, String> queryParams = new HashMap<>();
		HttpRequestMethod httpMethod = HttpRequestMethod.POST;
		String swaggerOperationID = SWAGGER_OPERATION_POST_CARTS_OPERATION_ID;
		sb.append(CHECKOUT_V1_CARTS_OPERATION_PATH);
		
		try {
			jsonBody = cartRequest.toJSON(null);
		} catch (IOException e) {
			throw new GovPayException(e);
		}
		log.trace("CartRequest creata: ["+jsonBody+"]");
		
		this.sendJson(sb.toString(), jsonBody.getBytes(), headerProperties, httpMethod, swaggerOperationID);
		
		// lettura header Location
		String location = this.dumpResponse.getHeaders().get(HttpHeaders.LOCATION);
		
		log.debug("Spedizione richiesta verso il Checkout completata con esito ["+this.getEventoCtx().getStatus()+"], Location ["+location+"].");
		return location;
	}

	@Override
	public String getOperationId() {
		return this.operationID;
	}

	private static Connettore getConnettore(String checkoutUrl) {
		Connettore connettore = new Connettore();

		connettore.setUrl(checkoutUrl);
		connettore.setTipoAutenticazione(EnumAuthType.NONE);
		connettore.setAzioneInUrl(false);

		return connettore;
	}

}
