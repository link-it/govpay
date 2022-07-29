package it.govpay.core.dao.eventi.utils;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Date;

import it.govpay.bd.model.Evento;
import it.govpay.bd.model.eventi.DatiPagoPA;
import it.govpay.core.utils.EventoContext;
import it.govpay.core.utils.GovpayConfig;
import it.govpay.gde.GdeInvoker;
import it.govpay.gde.v1.model.NuovoEvento;
import it.govpay.gde.v1.model.NuovoEvento.CategoriaEventoEnum;
import it.govpay.gde.v1.model.NuovoEvento.ComponenteEnum;
import it.govpay.gde.v1.model.NuovoEvento.EsitoEnum;
import it.govpay.gde.v1.model.NuovoEvento.RuoloEnum;

public class GdeUtils {
	
	public static void salvaEvento(EventoContext context) {
		GdeInvoker gdeInvoker = new GdeInvoker(GovpayConfig.getInstance().getGiornaleEventiUrl());
		Evento eventoDTO = context.toEventoDTO();
		gdeInvoker.salvaEvento(GdeUtils.toEventoModel(eventoDTO));
	}
	
	public static void salvaEvento(Evento context) {
		GdeInvoker gdeInvoker = new GdeInvoker(GovpayConfig.getInstance().getGiornaleEventiUrl());
		gdeInvoker.salvaEvento(GdeUtils.toEventoModel(context));
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
	
	private static it.govpay.gde.v1.model.DatiPagoPA getDatiPagoPA(Evento evento) {
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
