package it.govpay.backoffice.v1.beans.converter;

import it.govpay.backoffice.v1.beans.Evento;
import it.govpay.backoffice.v1.beans.Evento.CategoriaEventoEnum;
import org.openspcoop2.utils.serialization.IOException;

import it.govpay.bd.model.converter.EventoConverter;
import it.govpay.bd.model.eventi.EventoCooperazione;
import it.govpay.bd.model.eventi.EventoNota;

public class EventiConverter {

	
	public static Evento toRsModel(it.govpay.bd.model.Evento evento) throws IOException {
		Evento rsModel = new Evento();
		
		if(evento.getCategoriaEvento() != null) {
			switch (evento.getCategoriaEvento()) { 
			case INTERFACCIA_COOPERAZIONE:
				EventoCooperazione eventoCooperazione = EventoConverter.toEventoCooperazione(evento);
				rsModel.setParametriRichiesta(eventoCooperazione.getAltriParametriRichiesta());
				rsModel.setParametriRisposta(eventoCooperazione.getAltriParametriRisposta());
				rsModel.setEsito(eventoCooperazione.getEsito());
				rsModel.setDataOraRichiesta(eventoCooperazione.getDataRichiesta());
				rsModel.setDataOraRisposta(eventoCooperazione.getDataRisposta());
				rsModel.setCcp(eventoCooperazione.getCcp());
				rsModel.setComponente(eventoCooperazione.getComponente());
				rsModel.setIdCanale(eventoCooperazione.getCodCanale());
				rsModel.setIdDominio(eventoCooperazione.getCodDominio());
				rsModel.setIdentificativoErogatore(eventoCooperazione.getErogatore());
				rsModel.setIdentificativoFruitore(eventoCooperazione.getFruitore());
				rsModel.setIdPsp(eventoCooperazione.getCodPsp());
				rsModel.setIdStazione(eventoCooperazione.getCodStazione());
				rsModel.setIuv(eventoCooperazione.getIuv());
				rsModel.setTipoEvento(eventoCooperazione.getTipoEvento());

				if(eventoCooperazione.getTipoVersamento() != null) {
					rsModel.setTipoVersamento(eventoCooperazione.getTipoVersamento().name());
				}
				rsModel.setCategoriaEvento(CategoriaEventoEnum.INTERFACCIA);
				break;
			case INTERNO:
			default:
				rsModel.setCategoriaEvento(CategoriaEventoEnum.INTERNO);
				rsModel.setDataOraRichiesta(evento.getData());
				rsModel.setIdDominio(evento.getCodDominio());
				rsModel.setIuv(evento.getIuv());
				rsModel.setTipoEvento(evento.getTipoEvento());
				rsModel.setCcp(evento.getCcp());
				
				try {
					EventoNota eventoNota = EventoConverter.toEventoNota(evento);
					rsModel.setEsito(eventoNota.getOggetto());
				}catch(Exception e) {}
				
				break;
			}
		}
		
		return rsModel;
	}
}

