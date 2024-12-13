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
package it.govpay.core.utils;

import java.math.BigInteger;

import org.slf4j.Logger;

import it.govpay.bd.model.Evento;
import it.govpay.core.beans.EventoContext;
import it.govpay.core.exceptions.BaseExceptionV1;
import it.govpay.core.exceptions.GovPayException;
import it.govpay.core.exceptions.UnprocessableEntityException;
import it.govpay.core.exceptions.ValidationException;
import it.govpay.core.utils.client.exception.ClientException;
import it.govpay.model.Evento.CategoriaEvento;
import it.govpay.model.Evento.EsitoEvento;

public class EventoUtils {
	
	private static final String ERROR_MSG_ERRORE_DURANTE_LA_DECODIFICA_DEL_LIVELLO_DI_SEVERITA = "Errore durante la decodifica del livello di severita': ";

	private EventoUtils() {}

	public static Evento toEventoDTO(EventoContext eventoCtx, Logger log) {
		Evento dto = new Evento();

		if(eventoCtx.getCategoriaEvento() != null) {
			switch (eventoCtx.getCategoriaEvento()) {
			case INTERFACCIA:
				dto.setCategoriaEvento(CategoriaEvento.INTERFACCIA);
				break;
			case INTERNO:
				dto.setCategoriaEvento(CategoriaEvento.INTERNO);
				break;
			case UTENTE:
				dto.setCategoriaEvento(CategoriaEvento.UTENTE);
				break;
			}
		}
		if(eventoCtx.getComponente() != null)
			dto.setComponente(eventoCtx.getComponente().toString());
		dto.setData(eventoCtx.getDataRichiesta());
		dto.setDatiPagoPA(eventoCtx.getDatiPagoPA());
		dto.setDettaglioEsito(eventoCtx.getDescrizioneEsito());
		if(eventoCtx.getEsito() != null) {
			switch(eventoCtx.getEsito()) {
			case FAIL:
				dto.setEsitoEvento(EsitoEvento.FAIL);
				break;
			case KO:
				dto.setEsitoEvento(EsitoEvento.KO);
				break;
			case OK:
				dto.setEsitoEvento(EsitoEvento.OK);
				break;
			}
		}

		if(eventoCtx.getDataRisposta() != null) {
			if(eventoCtx.getDataRichiesta() != null) {
				dto.setIntervallo(eventoCtx.getDataRisposta().getTime() - eventoCtx.getDataRichiesta().getTime());
			} else {
				dto.setIntervallo(0l);
			}
		} else {
			dto.setIntervallo(0l);
		}
		dto.setDettaglioRichiesta(eventoCtx.getDettaglioRichiesta());
		dto.setDettaglioRisposta(eventoCtx.getDettaglioRisposta());
		dto.setRuoloEvento(eventoCtx.getRole());
		dto.setSottotipoEsito(eventoCtx.getSottotipoEsito());
		dto.setSottotipoEvento(eventoCtx.getSottotipoEvento());
		dto.setTipoEvento(eventoCtx.getTipoEvento());
		dto.setCodApplicazione(eventoCtx.getIdA2A());
		dto.setCodVersamentoEnte(eventoCtx.getIdPendenza());
		dto.setCodDominio(eventoCtx.getCodDominio());
		dto.setIuv(eventoCtx.getIuv());
		dto.setCcp(eventoCtx.getCcp());
		dto.setIdSessione(eventoCtx.getIdPagamento());
		dto.setIdFr(eventoCtx.getIdFr());
		dto.setIdTracciato(eventoCtx.getIdTracciato());
		dto.setIdIncasso(eventoCtx.getIdIncasso());

		if(eventoCtx.getSeverita() != null) {
			dto.setSeverita(eventoCtx.getSeverita());
		} else {
			if(eventoCtx.getException() != null) {
				LogUtils.logDebug(log, "Classe exception: " + eventoCtx.getException().getClass());

				if(eventoCtx.getException() instanceof GovPayException govpayException) {
					try {
						dto.setSeverita(SeveritaProperties.getInstance().getSeverita(govpayException.getCodEsito()));
					} catch (Exception e) {
						LogUtils.logError(log, ERROR_MSG_ERRORE_DURANTE_LA_DECODIFICA_DEL_LIVELLO_DI_SEVERITA + e.getMessage(),e);
					}
				}

				if(eventoCtx.getException() instanceof BaseExceptionV1 baseExceptionV1) {
					try {
						dto.setSeverita(SeveritaProperties.getInstance().getSeverita(baseExceptionV1.getCategoria()));
					} catch (Exception e) {
						LogUtils.logError(log, ERROR_MSG_ERRORE_DURANTE_LA_DECODIFICA_DEL_LIVELLO_DI_SEVERITA + e.getMessage(),e);
					}
				}

				if(eventoCtx.getException() instanceof UnprocessableEntityException unprocessableEntityException) {
					try {
						dto.setSeverita(SeveritaProperties.getInstance().getSeverita(unprocessableEntityException.getCategoria()));
					} catch (Exception e) {
						LogUtils.logError(log, ERROR_MSG_ERRORE_DURANTE_LA_DECODIFICA_DEL_LIVELLO_DI_SEVERITA + e.getMessage(),e);
					}
				}

				if(eventoCtx.getException() instanceof ValidationException) {
					try {
						dto.setSeverita(SeveritaProperties.getInstance().getSeverita(it.govpay.core.exceptions.BaseExceptionV1.CategoriaEnum.RICHIESTA));
					} catch (Exception e) {
						LogUtils.logError(log, ERROR_MSG_ERRORE_DURANTE_LA_DECODIFICA_DEL_LIVELLO_DI_SEVERITA + e.getMessage(),e);
					}
				}

				if(eventoCtx.getException() instanceof ClientException) {
					dto.setSeverita(BigInteger.valueOf(5));
				}
			}	
		}
		
		dto.setClusterId(eventoCtx.getClusterId());
		dto.setTransactionId(eventoCtx.getTransactionId());

		return dto;
	}
}
