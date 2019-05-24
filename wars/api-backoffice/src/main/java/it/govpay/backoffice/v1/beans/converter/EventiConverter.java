package it.govpay.backoffice.v1.beans.converter;

import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.serialization.IOException;

import it.govpay.backoffice.v1.beans.Evento;
import it.govpay.backoffice.v1.beans.Evento.CategoriaEventoEnum;

public class EventiConverter {

	
	public static Evento toRsModel(it.govpay.bd.model.Evento evento) throws IOException, ServiceException {
		Evento rsModel = new Evento();
		
		if(evento.getCategoriaEvento() != null) {
			switch (evento.getCategoriaEvento()) { 
			case INTERFACCIA:
				//rsModel.setParametriRichiesta(evento.getAltriParametriRichiesta());
				//rsModel.setParametriRisposta(evento.getAltriParametriRisposta());
				if(evento.getEsitoEvento() != null)
					rsModel.setEsito(evento.getEsitoEvento().name());
				rsModel.setDataOraRichiesta(evento.getData());
				rsModel.setDataOraRisposta(evento.getData()); // TODO
				
				if(evento.getRpt(null) != null) {
					rsModel.setIdDominio(evento.getRpt(null).getCodDominio());
					rsModel.setIuv(evento.getRpt(null).getIuv());
					rsModel.setCcp(evento.getRpt(null).getCcp());
				}
				if(evento.getControparte() != null) {
					rsModel.setIdentificativoErogatore(evento.getControparte().getErogatore());
					rsModel.setIdentificativoFruitore(evento.getControparte().getFruitore());
					rsModel.setIdCanale(evento.getControparte().getCodCanale());
					rsModel.setIdPsp(evento.getControparte().getCodPsp());
					rsModel.setIdStazione(evento.getControparte().getCodStazione());
					if(evento.getControparte().getTipoVersamento() != null) {
						rsModel.setTipoVersamento(evento.getControparte().getTipoVersamento().name());
					}
				}
				
				if(evento.getDettaglioRichiesta() != null) {
					rsModel.setDescrizioneEsito(evento.getDettaglioRichiesta().getPayload());
				}
				
				rsModel.setComponente(evento.getComponente());
				rsModel.setTipoEvento(evento.getTipoEvento());
				rsModel.setSottotipoEvento(evento.getSottotipoEvento()); 
				rsModel.setCategoriaEvento(CategoriaEventoEnum.INTERFACCIA);
				break;
			case INTERNO:
			default:
				rsModel.setCategoriaEvento(CategoriaEventoEnum.INTERNO);
				rsModel.setDataOraRichiesta(evento.getData());
				if(evento.getRpt(null) != null) {
					rsModel.setIdDominio(evento.getRpt(null).getCodDominio());
					rsModel.setIuv(evento.getRpt(null).getIuv());
					rsModel.setCcp(evento.getRpt(null).getCcp());
				}
				rsModel.setTipoEvento(evento.getTipoEvento());
				
				rsModel.setSottotipoEvento(evento.getSottotipoEvento());
			
				try {
					rsModel.setEsito(evento.getDettaglioEsito());
					if(evento.getDettaglioRichiesta() != null) {
						rsModel.setDescrizioneEsito(evento.getDettaglioRichiesta().getPayload());
					}
				}catch(Exception e) {}
				
				break;
			}
		}
		
		return rsModel;
	}
}

