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

	private static final String URL_ENDPOINT_NON_CONFIGURATO_PER_IL_BATCH_0 = "URL endpoint non configurato per il batch [{0}]";
	private static final String ERRORE_DURANTE_L_INIZIALIZZAZIONE_DEL_CLIENT_PER_IL_BATCH_0_1 = "Errore durante l''inizializzazione del client per il batch [{0}]: {1}";
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
			String errorMsg = MessageFormat.format(URL_ENDPOINT_NON_CONFIGURATO_PER_IL_BATCH_0, Operazioni.BATCH_ACA);
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
			String errorMsg = MessageFormat.format(URL_ENDPOINT_NON_CONFIGURATO_PER_IL_BATCH_0, Operazioni.BATCH_FDR);
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
	 * Attiva il batch Recupero RT esterno.
	 *
	 * @param ctx context di esecuzione
	 * @param force forza l'esecuzione del batch anche se già in esecuzione
	 * @return messaggio di conferma
	 * @throws it.govpay.core.exceptions.GovPayException in caso di errore nell'invocazione del batch
	 */
	public static String attivaBatchRecuperoRt(IContext ctx, boolean force) throws it.govpay.core.exceptions.GovPayException {
		// Recupera configurazione batch Recupero RT
		String endpointUrl = GovpayConfig.getInstance().getBatchRecuperoRtEndpointUrl();

		if(StringUtils.isEmpty(endpointUrl)) {
			String errorMsg = MessageFormat.format(URL_ENDPOINT_NON_CONFIGURATO_PER_IL_BATCH_0, Operazioni.BATCH_RECUPERO_RT);
			log.error(errorMsg);
			throw new it.govpay.core.exceptions.GovPayException(errorMsg, it.govpay.core.beans.EsitoOperazione.INTERNAL);
		}

		// Crea connettore
		Connettore connettore = new Connettore();
		connettore.setUrl(endpointUrl);
		connettore.setTipoAutenticazione(EnumAuthType.NONE);

		return attivaBatchEsterno(ctx, Operazioni.BATCH_RECUPERO_RT, TipoDestinatario.BATCH_RECUPERO_RT, connettore, force);
	}

	/**
	 * Attiva il batch Maggioli esterno.
	 *
	 * @param ctx context di esecuzione
	 * @param force forza l'esecuzione del batch anche se già in esecuzione
	 * @return messaggio di conferma
	 * @throws it.govpay.core.exceptions.GovPayException in caso di errore nell'invocazione del batch
	 */
	public static String attivaBatchMaggioli(IContext ctx, boolean force) throws it.govpay.core.exceptions.GovPayException {
		String endpointUrl = GovpayConfig.getInstance().getBatchMaggioliEndpointUrl();

		if(StringUtils.isEmpty(endpointUrl)) {
			String errorMsg = MessageFormat.format(URL_ENDPOINT_NON_CONFIGURATO_PER_IL_BATCH_0, Operazioni.BATCH_MAGGIOLI);
			log.error(errorMsg);
			throw new it.govpay.core.exceptions.GovPayException(errorMsg, it.govpay.core.beans.EsitoOperazione.INTERNAL);
		}

		Connettore connettore = new Connettore();
		connettore.setUrl(endpointUrl);
		connettore.setTipoAutenticazione(EnumAuthType.NONE);

		return attivaBatchEsterno(ctx, Operazioni.BATCH_MAGGIOLI, TipoDestinatario.BATCH_MAGGIOLI, connettore, force);
	}

	/**
	 * Attiva il batch IBAN esterno.
	 *
	 * @param ctx context di esecuzione
	 * @param force forza l'esecuzione del batch anche se già in esecuzione
	 * @return messaggio di conferma
	 * @throws it.govpay.core.exceptions.GovPayException in caso di errore nell'invocazione del batch
	 */
	public static String attivaBatchIban(IContext ctx, boolean force) throws it.govpay.core.exceptions.GovPayException {
		String endpointUrl = GovpayConfig.getInstance().getBatchIbanEndpointUrl();

		if(StringUtils.isEmpty(endpointUrl)) {
			String errorMsg = MessageFormat.format(URL_ENDPOINT_NON_CONFIGURATO_PER_IL_BATCH_0, Operazioni.BATCH_IBAN);
			log.error(errorMsg);
			throw new it.govpay.core.exceptions.GovPayException(errorMsg, it.govpay.core.beans.EsitoOperazione.INTERNAL);
		}

		Connettore connettore = new Connettore();
		connettore.setUrl(endpointUrl);
		connettore.setTipoAutenticazione(EnumAuthType.NONE);

		return attivaBatchEsterno(ctx, Operazioni.BATCH_IBAN, TipoDestinatario.BATCH_IBAN, connettore, force);
	}

	// ==================== OPERAZIONI STATUS ====================

	/**
	 * Recupera lo stato corrente del batch ACA.
	 *
	 * @param ctx context di esecuzione
	 * @return JSON con lo stato del batch
	 * @throws it.govpay.core.exceptions.GovPayException in caso di errore
	 */
	public static String getStatusBatchAca(IContext ctx) throws it.govpay.core.exceptions.GovPayException {
		String endpointUrl = GovpayConfig.getInstance().getBatchAcaEndpointUrl();
		return getStatusBatchEsterno(ctx, Operazioni.BATCH_ACA, TipoDestinatario.BATCH_ACA, endpointUrl);
	}

	/**
	 * Recupera lo stato corrente del batch FDR.
	 *
	 * @param ctx context di esecuzione
	 * @return JSON con lo stato del batch
	 * @throws it.govpay.core.exceptions.GovPayException in caso di errore
	 */
	public static String getStatusBatchFdr(IContext ctx) throws it.govpay.core.exceptions.GovPayException {
		String endpointUrl = GovpayConfig.getInstance().getBatchFdrEndpointUrl();
		return getStatusBatchEsterno(ctx, Operazioni.BATCH_FDR, TipoDestinatario.BATCH_FDR, endpointUrl);
	}

	/**
	 * Recupera lo stato corrente del batch Recupero RT.
	 *
	 * @param ctx context di esecuzione
	 * @return JSON con lo stato del batch
	 * @throws it.govpay.core.exceptions.GovPayException in caso di errore
	 */
	public static String getStatusBatchRecuperoRt(IContext ctx) throws it.govpay.core.exceptions.GovPayException {
		String endpointUrl = GovpayConfig.getInstance().getBatchRecuperoRtEndpointUrl();
		return getStatusBatchEsterno(ctx, Operazioni.BATCH_RECUPERO_RT, TipoDestinatario.BATCH_RECUPERO_RT, endpointUrl);
	}

	/**
	 * Recupera lo stato corrente del batch Maggioli.
	 */
	public static String getStatusBatchMaggioli(IContext ctx) throws it.govpay.core.exceptions.GovPayException {
		String endpointUrl = GovpayConfig.getInstance().getBatchMaggioliEndpointUrl();
		return getStatusBatchEsterno(ctx, Operazioni.BATCH_MAGGIOLI, TipoDestinatario.BATCH_MAGGIOLI, endpointUrl);
	}

	/**
	 * Recupera lo stato corrente del batch IBAN.
	 *
	 * @param ctx context di esecuzione
	 * @return JSON con lo stato del batch
	 * @throws it.govpay.core.exceptions.GovPayException in caso di errore
	 */
	public static String getStatusBatchIban(IContext ctx) throws it.govpay.core.exceptions.GovPayException {
		String endpointUrl = GovpayConfig.getInstance().getBatchIbanEndpointUrl();
		return getStatusBatchEsterno(ctx, Operazioni.BATCH_IBAN, TipoDestinatario.BATCH_IBAN, endpointUrl);
	}

	// ==================== OPERAZIONI LAST EXECUTION ====================

	/**
	 * Recupera informazioni sull'ultima esecuzione del batch ACA.
	 *
	 * @param ctx context di esecuzione
	 * @return JSON con le informazioni dell'ultima esecuzione
	 * @throws it.govpay.core.exceptions.GovPayException in caso di errore
	 */
	public static String getLastExecutionBatchAca(IContext ctx) throws it.govpay.core.exceptions.GovPayException {
		String endpointUrl = GovpayConfig.getInstance().getBatchAcaEndpointUrl();
		return getLastExecutionBatchEsterno(ctx, Operazioni.BATCH_ACA, TipoDestinatario.BATCH_ACA, endpointUrl);
	}

	/**
	 * Recupera informazioni sull'ultima esecuzione del batch FDR.
	 *
	 * @param ctx context di esecuzione
	 * @return JSON con le informazioni dell'ultima esecuzione
	 * @throws it.govpay.core.exceptions.GovPayException in caso di errore
	 */
	public static String getLastExecutionBatchFdr(IContext ctx) throws it.govpay.core.exceptions.GovPayException {
		String endpointUrl = GovpayConfig.getInstance().getBatchFdrEndpointUrl();
		return getLastExecutionBatchEsterno(ctx, Operazioni.BATCH_FDR, TipoDestinatario.BATCH_FDR, endpointUrl);
	}

	/**
	 * Recupera informazioni sull'ultima esecuzione del batch Recupero RT.
	 *
	 * @param ctx context di esecuzione
	 * @return JSON con le informazioni dell'ultima esecuzione
	 * @throws it.govpay.core.exceptions.GovPayException in caso di errore
	 */
	public static String getLastExecutionBatchRecuperoRt(IContext ctx) throws it.govpay.core.exceptions.GovPayException {
		String endpointUrl = GovpayConfig.getInstance().getBatchRecuperoRtEndpointUrl();
		return getLastExecutionBatchEsterno(ctx, Operazioni.BATCH_RECUPERO_RT, TipoDestinatario.BATCH_RECUPERO_RT, endpointUrl);
	}

	/**
	 * Recupera informazioni sull'ultima esecuzione del batch Maggioli.
	 */
	public static String getLastExecutionBatchMaggioli(IContext ctx) throws it.govpay.core.exceptions.GovPayException {
		String endpointUrl = GovpayConfig.getInstance().getBatchMaggioliEndpointUrl();
		return getLastExecutionBatchEsterno(ctx, Operazioni.BATCH_MAGGIOLI, TipoDestinatario.BATCH_MAGGIOLI, endpointUrl);
	}

	/**
	 * Recupera informazioni sull'ultima esecuzione del batch IBAN.
	 *
	 * @param ctx context di esecuzione
	 * @return JSON con le informazioni dell'ultima esecuzione
	 * @throws it.govpay.core.exceptions.GovPayException in caso di errore
	 */
	public static String getLastExecutionBatchIban(IContext ctx) throws it.govpay.core.exceptions.GovPayException {
		String endpointUrl = GovpayConfig.getInstance().getBatchIbanEndpointUrl();
		return getLastExecutionBatchEsterno(ctx, Operazioni.BATCH_IBAN, TipoDestinatario.BATCH_IBAN, endpointUrl);
	}

	// ==================== OPERAZIONI NEXT EXECUTION ====================

	/**
	 * Recupera informazioni sulla prossima esecuzione pianificata del batch ACA.
	 *
	 * @param ctx context di esecuzione
	 * @return JSON con le informazioni della prossima esecuzione
	 * @throws it.govpay.core.exceptions.GovPayException in caso di errore
	 */
	public static String getNextExecutionBatchAca(IContext ctx) throws it.govpay.core.exceptions.GovPayException {
		String endpointUrl = GovpayConfig.getInstance().getBatchAcaEndpointUrl();
		return getNextExecutionBatchEsterno(ctx, Operazioni.BATCH_ACA, TipoDestinatario.BATCH_ACA, endpointUrl);
	}

	/**
	 * Recupera informazioni sulla prossima esecuzione pianificata del batch FDR.
	 *
	 * @param ctx context di esecuzione
	 * @return JSON con le informazioni della prossima esecuzione
	 * @throws it.govpay.core.exceptions.GovPayException in caso di errore
	 */
	public static String getNextExecutionBatchFdr(IContext ctx) throws it.govpay.core.exceptions.GovPayException {
		String endpointUrl = GovpayConfig.getInstance().getBatchFdrEndpointUrl();
		return getNextExecutionBatchEsterno(ctx, Operazioni.BATCH_FDR, TipoDestinatario.BATCH_FDR, endpointUrl);
	}

	/**
	 * Recupera informazioni sulla prossima esecuzione pianificata del batch Recupero RT.
	 *
	 * @param ctx context di esecuzione
	 * @return JSON con le informazioni della prossima esecuzione
	 * @throws it.govpay.core.exceptions.GovPayException in caso di errore
	 */
	public static String getNextExecutionBatchRecuperoRt(IContext ctx) throws it.govpay.core.exceptions.GovPayException {
		String endpointUrl = GovpayConfig.getInstance().getBatchRecuperoRtEndpointUrl();
		return getNextExecutionBatchEsterno(ctx, Operazioni.BATCH_RECUPERO_RT, TipoDestinatario.BATCH_RECUPERO_RT, endpointUrl);
	}

	/**
	 * Recupera informazioni sulla prossima esecuzione pianificata del batch Maggioli.
	 */
	public static String getNextExecutionBatchMaggioli(IContext ctx) throws it.govpay.core.exceptions.GovPayException {
		String endpointUrl = GovpayConfig.getInstance().getBatchMaggioliEndpointUrl();
		return getNextExecutionBatchEsterno(ctx, Operazioni.BATCH_MAGGIOLI, TipoDestinatario.BATCH_MAGGIOLI, endpointUrl);
	}

	/**
	 * Recupera informazioni sulla prossima esecuzione pianificata del batch IBAN.
	 *
	 * @param ctx context di esecuzione
	 * @return JSON con le informazioni della prossima esecuzione
	 * @throws it.govpay.core.exceptions.GovPayException in caso di errore
	 */
	public static String getNextExecutionBatchIban(IContext ctx) throws it.govpay.core.exceptions.GovPayException {
		String endpointUrl = GovpayConfig.getInstance().getBatchIbanEndpointUrl();
		return getNextExecutionBatchEsterno(ctx, Operazioni.BATCH_IBAN, TipoDestinatario.BATCH_IBAN, endpointUrl);
	}

	// ==================== OPERAZIONI CLEAR CACHE ====================

	/**
	 * Svuota la cache del batch ACA.
	 *
	 * @param ctx context di esecuzione
	 * @return messaggio di conferma
	 * @throws it.govpay.core.exceptions.GovPayException in caso di errore
	 */
	public static String clearCacheBatchAca(IContext ctx) throws it.govpay.core.exceptions.GovPayException {
		String endpointUrl = GovpayConfig.getInstance().getBatchAcaEndpointUrl();
		return clearCacheBatchEsterno(ctx, Operazioni.BATCH_ACA, TipoDestinatario.BATCH_ACA, endpointUrl);
	}

	/**
	 * Svuota la cache del batch FDR.
	 *
	 * @param ctx context di esecuzione
	 * @return messaggio di conferma
	 * @throws it.govpay.core.exceptions.GovPayException in caso di errore
	 */
	public static String clearCacheBatchFdr(IContext ctx) throws it.govpay.core.exceptions.GovPayException {
		String endpointUrl = GovpayConfig.getInstance().getBatchFdrEndpointUrl();
		return clearCacheBatchEsterno(ctx, Operazioni.BATCH_FDR, TipoDestinatario.BATCH_FDR, endpointUrl);
	}

	/**
	 * Svuota la cache del batch Recupero RT.
	 *
	 * @param ctx context di esecuzione
	 * @return messaggio di conferma
	 * @throws it.govpay.core.exceptions.GovPayException in caso di errore
	 */
	public static String clearCacheBatchRecuperoRt(IContext ctx) throws it.govpay.core.exceptions.GovPayException {
		String endpointUrl = GovpayConfig.getInstance().getBatchRecuperoRtEndpointUrl();
		return clearCacheBatchEsterno(ctx, Operazioni.BATCH_RECUPERO_RT, TipoDestinatario.BATCH_RECUPERO_RT, endpointUrl);
	}

	/**
	 * Svuota la cache del batch Maggioli.
	 */
	public static String clearCacheBatchMaggioli(IContext ctx) throws it.govpay.core.exceptions.GovPayException {
		String endpointUrl = GovpayConfig.getInstance().getBatchMaggioliEndpointUrl();
		return clearCacheBatchEsterno(ctx, Operazioni.BATCH_MAGGIOLI, TipoDestinatario.BATCH_MAGGIOLI, endpointUrl);
	}

	/**
	 * Svuota la cache del batch IBAN.
	 *
	 * @param ctx context di esecuzione
	 * @return messaggio di conferma
	 * @throws it.govpay.core.exceptions.GovPayException in caso di errore
	 */
	public static String clearCacheBatchIban(IContext ctx) throws it.govpay.core.exceptions.GovPayException {
		String endpointUrl = GovpayConfig.getInstance().getBatchIbanEndpointUrl();
		return clearCacheBatchEsterno(ctx, Operazioni.BATCH_IBAN, TipoDestinatario.BATCH_IBAN, endpointUrl);
	}

	// ==================== UTILITY ====================

	/**
	 * Svuota la cache di tutti i batch esterni configurati.
	 * Invoca clearCache per ogni batch che ha un endpoint URL configurato.
	 *
	 * @param ctx context di esecuzione
	 * @return riepilogo delle operazioni eseguite
	 */
	public static String clearCacheAllBatch(IContext ctx) {
		StringBuilder result = new StringBuilder();
		int successCount = 0;
		int errorCount = 0;
		int skippedCount = 0;

		// Batch ACA
		String acaUrl = GovpayConfig.getInstance().getBatchAcaEndpointUrl();
		if (StringUtils.isNotEmpty(acaUrl)) {
			try {
				clearCacheBatchAca(ctx);
				result.append("ACA: OK\n");
				successCount++;
			} catch (Exception e) {
				result.append("ACA: ERRORE - ").append(e.getMessage()).append("\n");
				errorCount++;
				log.warn("Errore durante il reset cache batch ACA: {}", e.getMessage());
			}
		} else {
			result.append("ACA: SKIPPED (non configurato)\n");
			skippedCount++;
		}

		// Batch FDR
		String fdrUrl = GovpayConfig.getInstance().getBatchFdrEndpointUrl();
		if (StringUtils.isNotEmpty(fdrUrl)) {
			try {
				clearCacheBatchFdr(ctx);
				result.append("FDR: OK\n");
				successCount++;
			} catch (Exception e) {
				result.append("FDR: ERRORE - ").append(e.getMessage()).append("\n");
				errorCount++;
				log.warn("Errore durante il reset cache batch FDR: {}", e.getMessage());
			}
		} else {
			result.append("FDR: SKIPPED (non configurato)\n");
			skippedCount++;
		}

		// Batch Recupero RT
		String recuperoRtUrl = GovpayConfig.getInstance().getBatchRecuperoRtEndpointUrl();
		if (StringUtils.isNotEmpty(recuperoRtUrl)) {
			try {
				clearCacheBatchRecuperoRt(ctx);
				result.append("RecuperoRT: OK\n");
				successCount++;
			} catch (Exception e) {
				result.append("RecuperoRT: ERRORE - ").append(e.getMessage()).append("\n");
				errorCount++;
				log.warn("Errore durante il reset cache batch RecuperoRT: {}", e.getMessage());
			}
		} else {
			result.append("RecuperoRT: SKIPPED (non configurato)\n");
			skippedCount++;
		}

		// Batch Maggioli
		String maggioliUrl = GovpayConfig.getInstance().getBatchMaggioliEndpointUrl();
		if (StringUtils.isNotEmpty(maggioliUrl)) {
			try {
				clearCacheBatchMaggioli(ctx);
				result.append("Maggioli: OK\n");
				successCount++;
			} catch (Exception e) {
				result.append("Maggioli: ERRORE - ").append(e.getMessage()).append("\n");
				errorCount++;
				log.warn("Errore durante il reset cache batch Maggioli: {}", e.getMessage());
			}
		} else {
			result.append("Maggioli: SKIPPED (non configurato)\n");
			skippedCount++;
		}

		// Batch IBAN
		String ibanUrl = GovpayConfig.getInstance().getBatchIbanEndpointUrl();
		if (StringUtils.isNotEmpty(ibanUrl)) {
			try {
				clearCacheBatchIban(ctx);
				result.append("IBAN: OK\n");
				successCount++;
			} catch (Exception e) {
				result.append("IBAN: ERRORE - ").append(e.getMessage()).append("\n");
				errorCount++;
				log.warn("Errore durante il reset cache batch IBAN: {}", e.getMessage());
			}
		} else {
			result.append("IBAN: SKIPPED (non configurato)\n");
			skippedCount++;
		}

		result.append("\nRiepilogo: ").append(successCount).append(" OK, ")
			.append(errorCount).append(" ERRORI, ")
			.append(skippedCount).append(" SKIPPED");

		String resultStr = result.toString();
		log.info("Reset cache batch esterni completato:\n{}", resultStr);
		return resultStr;
	}

	// ==================== METODI PRIVATI ====================

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
		EventoContext eventoCtx = new EventoContext();
		eventoCtx.setComponente(Componente.GOVPAY);
		try {
			log.info("Attivazione batch esterno [{}] in corso (force: {})...", codiceBatch, force);

			BatchClient batchClient = new BatchClient(codiceBatch, tipoDestinatario, connettore, eventoCtx);
			byte[] responseBytes = batchClient.invokeBatch(force);
			String responseBody = responseBytes != null ? new String(responseBytes, StandardCharsets.UTF_8) : "";

			String msg = MessageFormat.format("Batch esterno [{0}] attivato con successo. Risposta: {1}", codiceBatch, responseBody);
			log.info(msg);

			return MessageFormat.format("Batch esterno [{0}] attivato con successo.", codiceBatch);

		} catch (ClientInitializeException e) {
			String errorMsg = MessageFormat.format(ERRORE_DURANTE_L_INIZIALIZZAZIONE_DEL_CLIENT_PER_IL_BATCH_0_1, codiceBatch, e.getMessage());
			LogUtils.logError(log, errorMsg, e);
			throw new it.govpay.core.exceptions.GovPayException(e, errorMsg);
		} catch (ClientException e) {
			return handleClientException(codiceBatch, e, eventoCtx);
		}
	}

	private static String getStatusBatchEsterno(IContext ctx, String codiceBatch, TipoDestinatario tipoDestinatario, String endpointUrl) throws it.govpay.core.exceptions.GovPayException {
		Connettore connettore = createConnettore(codiceBatch, endpointUrl);
		EventoContext eventoCtx = new EventoContext();
		eventoCtx.setComponente(Componente.GOVPAY);
		try {
			log.info("Recupero stato batch esterno [{}]...", codiceBatch);
			BatchClient batchClient = new BatchClient(codiceBatch, tipoDestinatario, connettore, eventoCtx);
			byte[] responseBytes = batchClient.getStatus();
			return responseBytes != null ? new String(responseBytes, StandardCharsets.UTF_8) : "";
		} catch (ClientInitializeException e) {
			String errorMsg = MessageFormat.format(ERRORE_DURANTE_L_INIZIALIZZAZIONE_DEL_CLIENT_PER_IL_BATCH_0_1, codiceBatch, e.getMessage());
			LogUtils.logError(log, errorMsg, e);
			throw new it.govpay.core.exceptions.GovPayException(e, errorMsg);
		} catch (ClientException e) {
			return handleClientException(codiceBatch, e, eventoCtx);
		}
	}

	private static String getLastExecutionBatchEsterno(IContext ctx, String codiceBatch, TipoDestinatario tipoDestinatario, String endpointUrl) throws it.govpay.core.exceptions.GovPayException {
		Connettore connettore = createConnettore(codiceBatch, endpointUrl);
		EventoContext eventoCtx = new EventoContext();
		eventoCtx.setComponente(Componente.GOVPAY);
		try {
			log.info("Recupero ultima esecuzione batch esterno [{}]...", codiceBatch);
			BatchClient batchClient = new BatchClient(codiceBatch, tipoDestinatario, connettore, eventoCtx);
			byte[] responseBytes = batchClient.getLastExecution();
			return responseBytes != null ? new String(responseBytes, StandardCharsets.UTF_8) : "";
		} catch (ClientInitializeException e) {
			String errorMsg = MessageFormat.format(ERRORE_DURANTE_L_INIZIALIZZAZIONE_DEL_CLIENT_PER_IL_BATCH_0_1, codiceBatch, e.getMessage());
			LogUtils.logError(log, errorMsg, e);
			throw new it.govpay.core.exceptions.GovPayException(e, errorMsg);
		} catch (ClientException e) {
			return handleClientException(codiceBatch, e, eventoCtx);
		}
	}

	private static String getNextExecutionBatchEsterno(IContext ctx, String codiceBatch, TipoDestinatario tipoDestinatario, String endpointUrl) throws it.govpay.core.exceptions.GovPayException {
		Connettore connettore = createConnettore(codiceBatch, endpointUrl);
		EventoContext eventoCtx = new EventoContext();
		eventoCtx.setComponente(Componente.GOVPAY);
		try {
			log.info("Recupero prossima esecuzione batch esterno [{}]...", codiceBatch);
			BatchClient batchClient = new BatchClient(codiceBatch, tipoDestinatario, connettore, eventoCtx);
			byte[] responseBytes = batchClient.getNextExecution();
			return responseBytes != null ? new String(responseBytes, StandardCharsets.UTF_8) : "";
		} catch (ClientInitializeException e) {
			String errorMsg = MessageFormat.format(ERRORE_DURANTE_L_INIZIALIZZAZIONE_DEL_CLIENT_PER_IL_BATCH_0_1, codiceBatch, e.getMessage());
			LogUtils.logError(log, errorMsg, e);
			throw new it.govpay.core.exceptions.GovPayException(e, errorMsg);
		} catch (ClientException e) {
			return handleClientException(codiceBatch, e, eventoCtx);
		}
	}

	private static String clearCacheBatchEsterno(IContext ctx, String codiceBatch, TipoDestinatario tipoDestinatario, String endpointUrl) throws it.govpay.core.exceptions.GovPayException {
		Connettore connettore = createConnettore(codiceBatch, endpointUrl);
		EventoContext eventoCtx = new EventoContext();
		eventoCtx.setComponente(Componente.GOVPAY);
		try {
			log.info("Svuotamento cache batch esterno [{}]...", codiceBatch);
			BatchClient batchClient = new BatchClient(codiceBatch, tipoDestinatario, connettore, eventoCtx);
			byte[] responseBytes = batchClient.clearCache();
			String responseBody = responseBytes != null ? new String(responseBytes, StandardCharsets.UTF_8) : "";
			log.info("Cache batch esterno [{}] svuotata con successo. Risposta: {}", codiceBatch, responseBody);
			return MessageFormat.format("Cache batch esterno [{0}] svuotata con successo.", codiceBatch);
		} catch (ClientInitializeException e) {
			String errorMsg = MessageFormat.format(ERRORE_DURANTE_L_INIZIALIZZAZIONE_DEL_CLIENT_PER_IL_BATCH_0_1, codiceBatch, e.getMessage());
			LogUtils.logError(log, errorMsg, e);
			throw new it.govpay.core.exceptions.GovPayException(e, errorMsg);
		} catch (ClientException e) {
			return handleClientException(codiceBatch, e, eventoCtx);
		}
	}

	private static Connettore createConnettore(String codiceBatch, String endpointUrl) throws it.govpay.core.exceptions.GovPayException {
		if(StringUtils.isEmpty(endpointUrl)) {
			String errorMsg = MessageFormat.format(URL_ENDPOINT_NON_CONFIGURATO_PER_IL_BATCH_0, codiceBatch);
			log.error(errorMsg);
			throw new it.govpay.core.exceptions.GovPayException(errorMsg, it.govpay.core.beans.EsitoOperazione.INTERNAL);
		}
		Connettore connettore = new Connettore();
		connettore.setUrl(endpointUrl);
		connettore.setTipoAutenticazione(EnumAuthType.NONE);
		return connettore;
	}

	private static String handleClientException(String codiceBatch, ClientException e, EventoContext eventoCtx) throws GovPayException {
		log.error("Errore durante l'invocazione del batch esterno [{}]. HTTP {}: {}", codiceBatch, e.getResponseCode(), e.getMessage(), e);

		Problem problem = null;
		try {
			if(eventoCtx != null && eventoCtx.getDettaglioRisposta() != null) {
				String responseBody = eventoCtx.getDettaglioRisposta().getPayload();
				problem = ConverterUtils.parse(responseBody, Problem.class);
			}
		} catch (Exception parseEx) {
			log.warn("Impossibile parsare la risposta di errore del batch come Problem: {}", parseEx.getMessage());
		}

		String faultCode = problem != null && StringUtils.isNotEmpty(problem.getDetail()) ? problem.getDetail() : "Errore durante l'invocazione del batch";
		throw new GovPayException(EsitoOperazione.BATCH_001, codiceBatch, faultCode);
	}
}
