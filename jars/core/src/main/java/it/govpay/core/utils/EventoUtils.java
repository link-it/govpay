package it.govpay.core.utils;

import java.math.BigDecimal;
import java.time.ZoneId;

import org.slf4j.Logger;

import it.govpay.bd.model.Evento;
import it.govpay.core.beans.EventoContext;
import it.govpay.core.exceptions.BaseExceptionV1;
import it.govpay.core.exceptions.GovPayException;
import it.govpay.core.exceptions.UnprocessableEntityException;
import it.govpay.core.exceptions.ValidationException;
import it.govpay.core.utils.client.exception.ClientException;
import it.govpay.gde.GdeInvoker;
import it.govpay.gde.v1.model.NuovoEvento;
import it.govpay.gde.v1.model.NuovoEvento.CategoriaEventoEnum;
import it.govpay.gde.v1.model.NuovoEvento.ComponenteEnum;
import it.govpay.gde.v1.model.NuovoEvento.EsitoEnum;
import it.govpay.gde.v1.model.NuovoEvento.RuoloEnum;
import it.govpay.model.Evento.CategoriaEvento;
import it.govpay.model.Evento.EsitoEvento;
import it.govpay.model.eventi.DatiPagoPA;

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
	
	public static void salvaEvento(Evento context) {
		GdeInvoker gdeInvoker = new GdeInvoker(GovpayConfig.getInstance().getGiornaleEventiUrl());
		gdeInvoker.salvaEvento(toEventoModel(context));
	}

	private static NuovoEvento toEventoModel(Evento context) {
			NuovoEvento dto = new NuovoEvento();
	
			if(context.getCategoriaEvento() != null) {
				switch (context.getCategoriaEvento()) {
				case INTERFACCIA:
					dto.setCategoriaEvento(CategoriaEventoEnum.INTERFACCIA);
					break;
				case INTERNO:
					dto.setCategoriaEvento(CategoriaEventoEnum.INTERNO);
					break;
				case UTENTE:
					dto.setCategoriaEvento(CategoriaEventoEnum.UTENTE);
					break;
				}
			}
			if(context.getComponente() != null) {
				try {
					dto.setComponente(ComponenteEnum.fromValue(context.getComponente())); 
				} catch(IllegalArgumentException e) {
					dto.setComponente(ComponenteEnum.GOVPAY);
				}
			}
			
			if(context.getData() != null) {
				dto.setDataEvento(context.getData().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
	//			dto.setDataEvento(context.getData().toInstant().atOffset(ZoneOffset.UTC));
			}
			
			dto.setDatiPagoPA(getDatiPagoPA(context)); 
	
			dto.setDettaglioEsito(context.getDettaglioEsito());
			if(context.getEsitoEvento() != null) {
				switch(context.getEsitoEvento()) {
				case FAIL:
					dto.setEsito(EsitoEnum.FAIL);
					break;
				case KO:
					dto.setEsito(EsitoEnum.KO);
					break;
				case OK:
					dto.setEsito(EsitoEnum.OK);
					break;
				}
			}
			dto.setId(context.getId());
			dto.setDurataEvento(context.getIntervallo());
	
			dto.setParametriRichiesta(context.getDettaglioRichiesta());
			dto.setParametriRisposta(context.getDettaglioRisposta());
			
			if(context.getRuoloEvento() != null) {
				switch(context.getRuoloEvento()) {
				case CLIENT:
					dto.setRuolo(RuoloEnum.CLIENT);
					break;
				case SERVER:
					dto.setRuolo(RuoloEnum.SERVER);
					break;
				}
			}
			dto.setSottotipoEsito(context.getSottotipoEsito());
			dto.setSottotipoEvento(context.getSottotipoEvento());
			dto.setTipoEvento(context.getTipoEvento());
			dto.setIdA2A(context.getCodApplicazione());
			dto.setIdPendenza(context.getCodVersamentoEnte());
			dto.setIdDominio(context.getCodDominio());
			dto.setIuv(context.getIuv());
			dto.setCcp(context.getCcp());
			dto.setIdPagamento(context.getIdSessione());
			dto.setIdFr(context.getIdFr());
			dto.setIdTracciato(context.getIdTracciato());
			dto.setIdRiconciliazione(context.getIdIncasso());
			
			if(context.getSeverita() != null) {
				dto.setSeverita(context.getSeverita());
			}
			
			return dto;
		}

	public static it.govpay.gde.v1.model.DatiPagoPA getDatiPagoPA(Evento evento) {
		DatiPagoPA datiPagoPA = evento.getPagoPA();
		
		it.govpay.gde.v1.model.DatiPagoPA datiPagoPAModel = null;
		if(datiPagoPA != null) {
			datiPagoPAModel = new it.govpay.gde.v1.model.DatiPagoPA();
			datiPagoPAModel.setIdCanale(datiPagoPA.getCodCanale());
			datiPagoPAModel.setIdPsp(datiPagoPA.getCodPsp());
			datiPagoPAModel.setIdIntermediarioPsp(datiPagoPA.getCodIntermediarioPsp());
			datiPagoPAModel.setIdIntermediario(datiPagoPA.getCodIntermediario());
			datiPagoPAModel.setIdStazione(datiPagoPA.getCodStazione());
			datiPagoPAModel.setIdDominio(datiPagoPA.getCodDominio());
			if(datiPagoPA.getTipoVersamento() != null) {
				datiPagoPAModel.setTipoVersamento(datiPagoPA.getTipoVersamento().getCodifica());
			}
			if(datiPagoPA.getModelloPagamento() != null) {
				datiPagoPAModel.setModelloPagamento(datiPagoPA.getModelloPagamento().getCodifica() +"");
			}
			
			datiPagoPAModel.setIdFlusso(datiPagoPA.getCodFlusso());
			if(datiPagoPA.getIdTracciato() != null)
				datiPagoPAModel.setIdTracciato(new BigDecimal(datiPagoPA.getIdTracciato()));
			datiPagoPAModel.setIdRiconciliazione(datiPagoPA.getTrn());
			datiPagoPAModel.setSct(datiPagoPA.getSct());
			
			if(datiPagoPA.getIdTracciato() != null)
				datiPagoPAModel.setIdTracciato(new BigDecimal(datiPagoPA.getIdTracciato()));
			
		}
		return datiPagoPAModel;
	}
}
