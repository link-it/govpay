/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2024 Link.it srl (http://www.link.it).
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
import org.openspcoop2.utils.UtilsException;
import org.openspcoop2.utils.logger.beans.Property;
import org.openspcoop2.utils.service.context.ContextThreadLocal;
import org.openspcoop2.utils.service.context.IContext;
import org.slf4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import it.gov.pagopa.bizeventsservice.model.ItGovPagopaBizeventsserviceModelResponseCtReceiptModelResponse;
import it.govpay.core.beans.EventoContext;
import it.govpay.core.exceptions.GovPayException;
import it.govpay.core.exceptions.IOException;
import it.govpay.core.utils.client.beans.TipoOperazioneNodo;
import it.govpay.core.utils.client.exception.ClientException;
import it.govpay.core.utils.client.exception.ClientInitializeException;
import it.govpay.core.utils.rawutils.ConverterUtils;
import it.govpay.model.Intermediario;
import it.govpay.model.configurazione.Giornale;

public class RecuperaRTNodoClient extends BasicClientCORE {

	public static final String HTTP_HEADER_X_REQUEST_ID = "X-Request-Id";

	private static Logger log = LoggerWrapperFactory.getLogger(RecuperaRTNodoClient.class);
	
	private static final String LABEL_SINTASSI = "[SINTASSI] ";
	
	private static final String RECUPERO_RT_OPERATION_PATH = "/organizations/{0}/receipts/{1}";
	
	public static final String LOG_KEY_RECUPERO_RT_RECUPERO_RT_KO = "recuperoRT.recuperoRTKo";
	public static final String LOG_KEY_RECUPERO_RT_RECUPERO_RT_OK = "recuperoRT.recuperoRTOk";
	
	public static final String ERROR_MESSAGE_ERRORE_NELLA_DESERIALIZZAZIONE_DEL_MESSAGGIO_DI_RISPOSTA_0 = "Errore nella deserializzazione del messaggio di risposta ({0})";
	
	public RecuperaRTNodoClient(Intermediario intermediario, String operationID, Giornale giornale, EventoContext eventoCtx) throws ClientInitializeException {
		super(intermediario, TipoOperazioneNodo.RECUPERO_RT, intermediario.getConnettorePddRecuperoRT(), eventoCtx);
		this.operationID = operationID;
		this.setGiornale(giornale);
	}
	
	@Override
	public String getOperationId() {
		return this.operationID;
	}
	
	public void setOperationId(String operationID) {
		this.operationID = operationID;
	}
	
	public ItGovPagopaBizeventsserviceModelResponseCtReceiptModelResponse recuperaRT(String codDominio, String iur, String xRequestId, String swaggerOperationID) throws ClientException, IOException, GovPayException {
		IContext ctx = ContextThreadLocal.get();
		ItGovPagopaBizeventsserviceModelResponseCtReceiptModelResponse ctReceiptModel = null;
		List<Property> headerProperties = new ArrayList<>();
		headerProperties.add(new Property(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE));
		if(xRequestId != null) {
			headerProperties.add(new Property(HTTP_HEADER_X_REQUEST_ID, xRequestId));
		}
		
		String path = MessageFormat.format(RECUPERO_RT_OPERATION_PATH, codDominio, iur);
		log.debug("Recupero RT da PagoPA [CodDominio: {}, IUR: {}], Path [{}]", codDominio, iur, path);		
		try {
			String jsonResponse = new String(this.getJson(path, headerProperties, swaggerOperationID));
			ctReceiptModel = ConverterUtils.parse(jsonResponse, ItGovPagopaBizeventsserviceModelResponseCtReceiptModelResponse.class); 
			RecuperaRTNodoClient.logMessaggioDiagnostico(ctx, LOG_KEY_RECUPERO_RT_RECUPERO_RT_OK, codDominio, iur);
			log.debug("Recupero RT da PagoPA [CodDominio: {}, IUR: {}] completato.", codDominio, iur);	
		}catch(ClientException e) {
			String logErrorMessage = MessageFormat.format(ERROR_MESSAGE_ERRORE_NELLA_DESERIALIZZAZIONE_DEL_MESSAGGIO_DI_RISPOSTA_0,	e.getMessage());
			RecuperaRTNodoClient.logMessaggioDiagnostico(ctx, LOG_KEY_RECUPERO_RT_RECUPERO_RT_KO, codDominio, iur, logErrorMessage);
			throw e;
		} catch (IOException e) {
			RecuperaRTNodoClient.logMessaggioDiagnostico(ctx, LOG_KEY_RECUPERO_RT_RECUPERO_RT_KO, codDominio, iur, LABEL_SINTASSI + e.getMessage());
			throw e;
		}
		
		return ctReceiptModel;
	}

	private static void logMessaggioDiagnostico(IContext ctx, String msg, String ... parametri) throws GovPayException {
		try {
			if(parametri != null && parametri.length > 0) {
				ctx.getApplicationLogger().log(msg, parametri);
			} else {
				ctx.getApplicationLogger().log(msg);
			}
		}catch (UtilsException e) {
			throw new GovPayException(e);
		}
	}
}
