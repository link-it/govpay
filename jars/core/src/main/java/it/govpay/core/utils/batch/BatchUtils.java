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
package it.govpay.core.utils.batch;

import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;

import org.apache.commons.lang3.StringUtils;
import org.openspcoop2.utils.LoggerWrapperFactory;
import org.openspcoop2.utils.service.context.IContext;
import org.slf4j.Logger;

import it.govpay.core.beans.EsitoOperazione;
import it.govpay.core.beans.EventoContext;
import it.govpay.core.beans.EventoContext.Componente;
import it.govpay.core.beans.batch.Problem;
import it.govpay.core.business.Operazioni;
import it.govpay.core.exceptions.GovPayException;
import it.govpay.core.utils.GovpayConfig;
import it.govpay.core.utils.LogUtils;
import it.govpay.core.utils.client.BatchClient;
import it.govpay.core.utils.client.beans.TipoDestinatario;
import it.govpay.core.utils.client.exception.ClientException;
import it.govpay.core.utils.client.exception.ClientInitializeException;
import it.govpay.core.utils.rawutils.ConverterUtils;
import it.govpay.model.Connettore;
import it.govpay.model.Connettore.EnumAuthType;

/**
 * Utility per la gestione e l'invocazione di batch esterni.
 */
public class BatchUtils {

	private static Logger log = LoggerWrapperFactory.getLogger(BatchUtils.class);

	private BatchUtils() {
		// Utility class
	}

	/**
	 * Attiva il batch ACA esterno.
	 *
	 * @param ctx context di esecuzione
	 * @param force forza l'esecuzione del batch anche se già in esecuzione
	 * @return messaggio di conferma
	 * @throws it.govpay.core.exceptions.GovPayException in caso di errore nell'invocazione del batch
	 */
	public static String attivaBatchAca(IContext ctx, boolean force) throws it.govpay.core.exceptions.GovPayException {
		// Recupera configurazione batch ACA
		String endpointUrl = GovpayConfig.getInstance().getBatchAcaEndpointUrl();

		if(StringUtils.isEmpty(endpointUrl)) {
			String errorMsg = MessageFormat.format("URL endpoint non configurato per il batch [{0}]", Operazioni.BATCH_ACA);
			log.error(errorMsg);
			throw new it.govpay.core.exceptions.GovPayException(errorMsg, it.govpay.core.beans.EsitoOperazione.INTERNAL);
		}

		// Crea connettore
		Connettore connettore = new Connettore();
		connettore.setUrl(endpointUrl);
		connettore.setTipoAutenticazione(EnumAuthType.NONE);

		return attivaBatchEsterno(ctx, Operazioni.BATCH_ACA, TipoDestinatario.BATCH_ACA, connettore, force);
	}

	/**
	 * Attiva il batch FDR esterno.
	 *
	 * @param ctx context di esecuzione
	 * @param force forza l'esecuzione del batch anche se già in esecuzione
	 * @return messaggio di conferma
	 * @throws it.govpay.core.exceptions.GovPayException in caso di errore nell'invocazione del batch
	 */
	public static String attivaBatchFdr(IContext ctx, boolean force) throws it.govpay.core.exceptions.GovPayException {
		// Recupera configurazione batch FDR
		String endpointUrl = GovpayConfig.getInstance().getBatchFdrEndpointUrl();

		if(StringUtils.isEmpty(endpointUrl)) {
			String errorMsg = MessageFormat.format("URL endpoint non configurato per il batch [{0}]", Operazioni.BATCH_FDR);
			log.error(errorMsg);
			throw new it.govpay.core.exceptions.GovPayException(errorMsg, it.govpay.core.beans.EsitoOperazione.INTERNAL);
		}

		// Crea connettore
		Connettore connettore = new Connettore();
		connettore.setUrl(endpointUrl);
		connettore.setTipoAutenticazione(EnumAuthType.NONE);

		return attivaBatchEsterno(ctx, Operazioni.BATCH_FDR, TipoDestinatario.BATCH_FDR, connettore, force);
	}

	/**
	 * Attiva un batch esterno invocando l'endpoint HTTP esposto dal batch.
	 *
	 * @param ctx context di esecuzione
	 * @param codiceBatch codice identificativo del batch da attivare (es. BATCH_ACA)
	 * @param connettore connettore configurato per l'invocazione
	 * @param force forza l'esecuzione del batch anche se già in esecuzione
	 * @return messaggio di conferma
	 * @throws it.govpay.core.exceptions.GovPayException in caso di errore nell'invocazione del batch
	 */
	private static String attivaBatchEsterno(IContext ctx, String codiceBatch, TipoDestinatario tipoDestinatario, Connettore connettore, boolean force) throws it.govpay.core.exceptions.GovPayException {
		// Crea evento context
		EventoContext eventoCtx = new EventoContext();
		eventoCtx.setComponente(Componente.GOVPAY);
		try {
			log.info("Attivazione batch esterno [{}] in corso (force: {})...", codiceBatch, force);

			// Crea BatchClient
			BatchClient batchClient = new BatchClient(codiceBatch, tipoDestinatario, connettore, eventoCtx);

			// Invoca batch - il servizio risponde con HTTP 202 in caso di avvio corretto
			byte[] responseBytes = batchClient.invokeBatch(force);
			String responseBody = responseBytes != null ? new String(responseBytes, StandardCharsets.UTF_8) : "";

			String msg = MessageFormat.format("Batch esterno [{0}] attivato con successo. Risposta: {1}", codiceBatch, responseBody);
			log.info(msg);

			return MessageFormat.format("Batch esterno [{0}] attivato con successo.", codiceBatch);

		} catch (ClientInitializeException e) {
			String errorMsg = MessageFormat.format("Errore durante l''inizializzazione del client per il batch [{0}]: {1}", codiceBatch, e.getMessage());
			LogUtils.logError(log, errorMsg, e);
			throw new it.govpay.core.exceptions.GovPayException(e, errorMsg);
		} catch (ClientException e) {
			// Il batch risponde con HTTP 202 in caso di successo, altri codici indicano errore (Problem)
			log.error("Errore durante l''invocazione del batch esterno [{}]. HTTP {}: {}", codiceBatch, e.getResponseCode(), e.getMessage(), e);

			// Estrae la risposta dal context dell'evento e la trasforma in Problem
			Problem problem = null;
			try {
				if(eventoCtx != null && eventoCtx.getDettaglioRisposta() != null) {
					String responseBody = eventoCtx.getDettaglioRisposta().getPayload();
					problem = ConverterUtils.parse(responseBody, Problem.class);
				}
			} catch (Exception parseEx) {
				// Se il parsing fallisce, crea un Problem generico
				log.warn("Impossibile parsare la risposta di errore del batch come Problem: {}", parseEx.getMessage());
			}

			// Crea FaultBean dal Problem per GovPayException
			String faultCode = problem != null && StringUtils.isNotEmpty(problem.getDetail()) ? problem.getDetail() : "Errore durante l'invocazione del batch";
			throw new GovPayException(EsitoOperazione.BATCH_001,codiceBatch, faultCode);
		}
	}
}
