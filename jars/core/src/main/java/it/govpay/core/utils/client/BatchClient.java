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

import java.util.ArrayList;
import java.util.List;

import org.openspcoop2.utils.logger.beans.Property;

import it.govpay.core.beans.EventoContext;
import it.govpay.core.utils.client.beans.TipoDestinatario;
import it.govpay.core.utils.client.exception.ClientException;
import it.govpay.core.utils.client.exception.ClientInitializeException;
import it.govpay.core.utils.client.handler.IntegrationContext;
import it.govpay.model.Connettore;

/**
 * Client per l'invocazione di batch esterni via HTTP.
 */
public class BatchClient extends BasicClientCORE {

	private static final String OPERATION_ID = "invocazioneBatchEsterno";
	private String codiceBatch;

	/**
	 * Costruttore per l'invocazione di batch esterni.
	 *
	 * @param codiceBatch Codice identificativo del batch da invocare
	 * @param connettore Connettore configurato con l'endpoint del batch
	 * @param eventoCtx Contesto evento per logging
	 * @throws ClientInitializeException In caso di errore di inizializzazione
	 */
	public BatchClient(String codiceBatch, TipoDestinatario tipoDestinatario, Connettore connettore, EventoContext eventoCtx) throws ClientInitializeException {
		super("BATCH_" + codiceBatch, tipoDestinatario, connettore, eventoCtx);
		this.codiceBatch = codiceBatch;
		errMsg = "Invocazione batch esterno [" + codiceBatch + "]";
		mittente = "GovPay";
		destinatario = "BatchEsterno_" + codiceBatch;
		integrationCtx = new IntegrationContext();
		integrationCtx.setApplicazione(null);
		integrationCtx.setIntermediario(null);
		integrationCtx.setTipoConnettore(null);
		integrationCtx.setTipoDestinatario(tipoDestinatario);
	}

	@Override
	public String getOperationId() {
		return OPERATION_ID + "_" + this.codiceBatch;
	}

	/**
	 * Invoca il batch esterno tramite GET /run.
	 *
	 * @param force Forza l'esecuzione del batch anche se già in esecuzione
	 * @return Response body in byte array
	 * @throws ClientException In caso di errore durante l'invocazione
	 */
	public byte[] invokeBatch(boolean force) throws ClientException {
		List<Property> headerProperties = new ArrayList<>();
		String pathWithParams = "/run";
		if(force) {
			pathWithParams = "/run?force=true";
		}
		return this.getJson(pathWithParams, headerProperties, this.getOperationId() + "_run");
	}

	/**
	 * Recupera lo stato corrente del batch tramite GET /status.
	 *
	 * @return Response body in byte array (JSON BatchStatusInfo)
	 * @throws ClientException In caso di errore durante l'invocazione
	 */
	public byte[] getStatus() throws ClientException {
		List<Property> headerProperties = new ArrayList<>();
		return this.getJson("/status", headerProperties, this.getOperationId() + "_status");
	}

	/**
	 * Recupera informazioni sull'ultima esecuzione completata tramite GET /lastExecution.
	 *
	 * @return Response body in byte array (JSON LastExecutionInfo)
	 * @throws ClientException In caso di errore durante l'invocazione
	 */
	public byte[] getLastExecution() throws ClientException {
		List<Property> headerProperties = new ArrayList<>();
		return this.getJson("/lastExecution", headerProperties, this.getOperationId() + "_lastExecution");
	}

	/**
	 * Recupera informazioni sulla prossima esecuzione pianificata tramite GET /nextExecution.
	 *
	 * @return Response body in byte array (JSON NextExecutionInfo)
	 * @throws ClientException In caso di errore durante l'invocazione
	 */
	public byte[] getNextExecution() throws ClientException {
		List<Property> headerProperties = new ArrayList<>();
		return this.getJson("/nextExecution", headerProperties, this.getOperationId() + "_nextExecution");
	}

	/**
	 * Svuota la cache dell'applicazione tramite GET /cache/clear.
	 *
	 * @return Response body in byte array
	 * @throws ClientException In caso di errore durante l'invocazione
	 */
	public byte[] clearCache() throws ClientException {
		List<Property> headerProperties = new ArrayList<>();
		return this.getJson("/cache/clear", headerProperties, this.getOperationId() + "_clearCache");
	}

	public String getCodiceBatch() {
		return codiceBatch;
	}
}
