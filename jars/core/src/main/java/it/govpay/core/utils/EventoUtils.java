package it.govpay.core.utils;

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
		//dto.setId(eventoCtx.getId());
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
				log.debug("Classe exception: " + eventoCtx.getException().getClass());

				if(eventoCtx.getException() instanceof GovPayException) {
					try {
						dto.setSeverita(SeveritaProperties.getInstance().getSeverita(((GovPayException) eventoCtx.getException()).getCodEsito()));
					} catch (Exception e) {
						log.error("Errore durante la decodifica del livello di severita': " + e.getMessage(),e);
					}
				}

				if(eventoCtx.getException() instanceof BaseExceptionV1) {
					try {
						dto.setSeverita(SeveritaProperties.getInstance().getSeverita(((BaseExceptionV1) eventoCtx.getException()).getCategoria()));
					} catch (Exception e) {
						log.error("Errore durante la decodifica del livello di severita': " + e.getMessage(),e);
					}
				}

				if(eventoCtx.getException() instanceof UnprocessableEntityException) {
					try {
						dto.setSeverita(SeveritaProperties.getInstance().getSeverita(((UnprocessableEntityException) eventoCtx.getException()).getCategoria()));
					} catch (Exception e) {
						log.error("Errore durante la decodifica del livello di severita': " + e.getMessage(),e);
					}
				}

				if(eventoCtx.getException() instanceof ValidationException) {
					try {
						dto.setSeverita(SeveritaProperties.getInstance().getSeverita(it.govpay.core.exceptions.BaseExceptionV1.CategoriaEnum.RICHIESTA));
					} catch (Exception e) {
						log.error("Errore durante la decodifica del livello di severita': " + e.getMessage(),e);
					}
				}

				if(eventoCtx.getException() instanceof ClientException) {
					dto.setSeverita(5);
				}
			}	
		}

		return dto;
	}
}
