package it.govpay.backoffice.v1.beans.converter;

import it.govpay.backoffice.v1.beans.Evento;
import it.govpay.backoffice.v1.beans.Evento.CategoriaEventoEnum;

public class EventiConverter {

	
	public static Evento toRsModel(it.govpay.model.Evento evento) {
		Evento rsModel = new Evento();
		if(evento.getCategoriaEvento() != null) {
			switch (evento.getCategoriaEvento()) { 
			case INTERFACCIA:
				rsModel.setCategoriaEvento(CategoriaEventoEnum.INTERFACCIA);
				break;
			case INTERNO:
			default:
				rsModel.setCategoriaEvento(CategoriaEventoEnum.INTERNO);
				break;
			}
		}
		
		rsModel.setParametriRichiesta(evento.getAltriParametriRichiesta());
		rsModel.setParametriRisposta(evento.getAltriParametriRisposta());
		rsModel.setEsito(evento.getEsito());
		rsModel.setDataOraRichiesta(evento.getDataRichiesta());
		rsModel.setDataOraRisposta(evento.getDataRisposta());
		rsModel.setCcp(evento.getCcp());
		rsModel.setComponente(evento.getComponente());
		rsModel.setIdCanale(evento.getCodCanale());
		rsModel.setIdDominio(evento.getCodDominio());
		rsModel.setIdentificativoErogatore(evento.getErogatore());
		rsModel.setIdentificativoFruitore(evento.getFruitore());
		rsModel.setIdPsp(evento.getCodPsp());
		rsModel.setIdStazione(evento.getCodStazione());
		rsModel.setIuv(evento.getIuv());

		if(evento.getTipoEvento() != null) {
			rsModel.setTipoEvento(evento.getTipoEvento().name());
		}

		if(evento.getTipoVersamento() != null) {
			rsModel.setTipoVersamento(evento.getTipoVersamento().name());
		}
		
		
		return rsModel;
	}
}

