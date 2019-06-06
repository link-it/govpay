package it.govpay.backoffice.v1.beans.converter;

import org.apache.commons.lang.StringUtils;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.jaxrs.RawObject;
import org.openspcoop2.utils.serialization.IOException;

import it.govpay.backoffice.v1.beans.CategoriaEvento;
import it.govpay.backoffice.v1.beans.ComponenteEvento;
import it.govpay.backoffice.v1.beans.Controparte;
import it.govpay.backoffice.v1.beans.EsitoEvento;
import it.govpay.backoffice.v1.beans.Evento;
import it.govpay.backoffice.v1.beans.EventoIndex;
import it.govpay.backoffice.v1.beans.RuoloEvento;
import it.govpay.rs.v1.ConverterUtils;

public class EventiConverter {

	public static EventoIndex toRsModelIndex(it.govpay.bd.model.Evento evento) throws IOException, ServiceException {
		EventoIndex rsModel = new EventoIndex();
		
		rsModel.setId(evento.getId());

		if(StringUtils.isNotBlank(evento.getComponente())) {
			rsModel.setComponente(ComponenteEvento.fromValue(evento.getComponente()));
		}

		if(evento.getCategoriaEvento() != null) {
			switch (evento.getCategoriaEvento()) {
			case INTERFACCIA:
				rsModel.setCategoriaEvento(CategoriaEvento.INTERFACCIA);
				break;
			case INTERNO:
				rsModel.setCategoriaEvento(CategoriaEvento.INTERNO);
				break;
			case UTENTE:
				rsModel.setCategoriaEvento(CategoriaEvento.UTENTE);
				break;
			}
		}

		if(evento.getRuoloEvento() != null) {
			switch (evento.getRuoloEvento()) {
			case CLIENT:
				rsModel.setRuolo(RuoloEvento.CLIENT);
				break;
			case SERVER:
				rsModel.setRuolo(RuoloEvento.SERVER);
				break;
			}
		}

		rsModel.setTipoEvento(evento.getTipoEvento());

		if(evento.getEsitoEvento() != null) {
			switch (evento.getEsitoEvento()) {
			case FAIL:
				rsModel.setEsito(EsitoEvento.FAIL);
				break;
			case KO:
				rsModel.setEsito(EsitoEvento.KO);
				break;
			case OK:
				rsModel.setEsito(EsitoEvento.OK);
				break;
			}
		}

		rsModel.setDataEvento(evento.getData());
		rsModel.setDurataEvento(evento.getIntervallo() != null ? evento.getIntervallo().longValue() : 0l);
		rsModel.setSottotipoEvento(evento.getSottotipoEvento()); 
		rsModel.setSottotipoEsito(evento.getSottotipoEsito());
		rsModel.setDettaglioEsito(evento.getDettaglioEsito());

		rsModel.setIdDominio(evento.getCodDominio());
		rsModel.setIuv(evento.getIuv());
		rsModel.setCcp(evento.getCcp());
		rsModel.setIdA2A(evento.getCodApplicazione());
		rsModel.setIdPendenza(evento.getCodVersamentoEnte());
		rsModel.setIdPagamento(evento.getIdSessione());
		
		rsModel.setControparte(getControparte(evento));


		return rsModel;
	}

	public static Evento toRsModel(it.govpay.bd.model.Evento evento) throws IOException, ServiceException {
		Evento rsModel = new Evento();
		
		rsModel.setId(evento.getId());

		if(StringUtils.isNotBlank(evento.getComponente())) {
			rsModel.setComponente(ComponenteEvento.fromValue(evento.getComponente()));
		}

		if(evento.getCategoriaEvento() != null) {
			switch (evento.getCategoriaEvento()) {
			case INTERFACCIA:
				rsModel.setCategoriaEvento(CategoriaEvento.INTERFACCIA);
				break;
			case INTERNO:
				rsModel.setCategoriaEvento(CategoriaEvento.INTERNO);
				break;
			case UTENTE:
				rsModel.setCategoriaEvento(CategoriaEvento.UTENTE);
				break;
			}
		}

		if(evento.getRuoloEvento() != null) {
			switch (evento.getRuoloEvento()) {
			case CLIENT:
				rsModel.setRuolo(RuoloEvento.CLIENT);
				break;
			case SERVER:
				rsModel.setRuolo(RuoloEvento.SERVER);
				break;
			}
		}

		rsModel.setTipoEvento(evento.getTipoEvento());

		if(evento.getEsitoEvento() != null) {
			switch (evento.getEsitoEvento()) {
			case FAIL:
				rsModel.setEsito(EsitoEvento.FAIL);
				break;
			case KO:
				rsModel.setEsito(EsitoEvento.KO);
				break;
			case OK:
				rsModel.setEsito(EsitoEvento.OK);
				break;
			}
		}

		rsModel.setDataEvento(evento.getData());
		rsModel.setDurataEvento(evento.getIntervallo() != null ? evento.getIntervallo().longValue() : 0l);
		rsModel.setSottotipoEvento(evento.getSottotipoEvento()); 
		rsModel.setSottotipoEsito(evento.getSottotipoEsito()); 
		rsModel.setDettaglioEsito(evento.getDettaglioEsito());

		rsModel.setIdDominio(evento.getCodDominio());
		rsModel.setIuv(evento.getIuv());
		rsModel.setCcp(evento.getCcp());
		rsModel.setIdA2A(evento.getCodApplicazione());
		rsModel.setIdPendenza(evento.getCodVersamentoEnte());
		rsModel.setIdPagamento(evento.getIdSessione());

		rsModel.setControparte(getControparte(evento));
		
		if(evento.getDettaglioRichiesta() != null)
			rsModel.setParametriRichiesta(new RawObject(ConverterUtils.getParametriRichiestaEvento(evento.getDettaglioRichiesta())));
		if(evento.getDettaglioRisposta() != null)
			rsModel.setParametriRisposta(new RawObject(ConverterUtils.getParametriRispostaEvento(evento.getDettaglioRisposta())));

		return rsModel;
	}

	private static Controparte getControparte(it.govpay.bd.model.Evento evento) {
		Controparte controparte = null;
		if(evento.getControparte() != null) {
			controparte = new Controparte();
			controparte.setIdentificativoErogatore(evento.getControparte().getErogatore());
			controparte.setIdentificativoFruitore(evento.getControparte().getFruitore());
			controparte.setIdCanale(evento.getControparte().getCodCanale());
			controparte.setIdPsp(evento.getControparte().getCodPsp());
			controparte.setIdStazione(evento.getControparte().getCodStazione());
			if(evento.getControparte().getTipoVersamento() != null) {
				controparte.setTipoVersamento(evento.getControparte().getTipoVersamento().name());
			}
		}
		return controparte;
	}
}

