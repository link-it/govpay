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
package it.govpay.backoffice.v1.beans.converter;

import java.math.BigDecimal;

import org.apache.commons.lang.StringUtils;
import org.openspcoop2.utils.jaxrs.RawObject;

import it.govpay.backoffice.v1.beans.CategoriaEvento;
import it.govpay.backoffice.v1.beans.ComponenteEvento;
import it.govpay.backoffice.v1.beans.DatiPagoPA;
import it.govpay.backoffice.v1.beans.EsitoEvento;
import it.govpay.backoffice.v1.beans.Evento;
import it.govpay.backoffice.v1.beans.EventoIndex;
import it.govpay.backoffice.v1.beans.RuoloEvento;
import it.govpay.core.exceptions.IOException;

public class EventiConverter {

	public static EventoIndex toRsModelIndex(it.govpay.bd.model.Evento evento) throws IOException {
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

		rsModel.setDatiPagoPA(getDatiPagoPA(evento));
		rsModel.setSeverita(evento.getSeverita());
		
		rsModel.setClusterId(evento.getClusterId());
		rsModel.setTransactionId(evento.getTransactionId());

		return rsModel;
	}

	public static Evento toRsModel(it.govpay.bd.model.Evento evento) throws IOException {
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

		rsModel.setDatiPagoPA(getDatiPagoPA(evento));

		if(evento.getDettaglioRichiesta() != null) {
			rsModel.setParametriRichiesta(new RawObject(evento.getDettaglioAsString(evento.getDettaglioRichiesta())));
//			rsModel.setParametriRichiesta(new RawObject(ConverterUtils.getParametriRichiestaEvento(evento.getDettaglioRichiesta())));
		}
		if(evento.getDettaglioRisposta() != null) {
			rsModel.setParametriRisposta(new RawObject(evento.getDettaglioAsString(evento.getDettaglioRisposta())));
//			rsModel.setParametriRisposta(new RawObject(ConverterUtils.getParametriRispostaEvento(evento.getDettaglioRisposta())));
		}

		rsModel.setSeverita(evento.getSeverita());
		rsModel.setClusterId(evento.getClusterId());
		rsModel.setTransactionId(evento.getTransactionId());

		return rsModel;
	}

	private static DatiPagoPA getDatiPagoPA(it.govpay.bd.model.Evento evento) {
		DatiPagoPA datiPagoPA = null;
		if(evento.getPagoPA() != null) {
			datiPagoPA = new DatiPagoPA();
//			datiPagoPA.setIdentificativoErogatore(evento.getPagoPA().getErogatore());
//			datiPagoPA.setIdentificativoFruitore(evento.getPagoPA().getFruitore());
			datiPagoPA.setIdCanale(evento.getPagoPA().getCodCanale());
			datiPagoPA.setIdPsp(evento.getPagoPA().getCodPsp());
			datiPagoPA.setIdIntermediarioPsp(evento.getPagoPA().getCodIntermediarioPsp());
			datiPagoPA.setIdIntermediario(evento.getPagoPA().getCodIntermediario());
			datiPagoPA.setIdStazione(evento.getPagoPA().getCodStazione());
			datiPagoPA.setIdDominio(evento.getPagoPA().getCodDominio());
			if(evento.getPagoPA().getTipoVersamento() != null) {
				datiPagoPA.setTipoVersamento(evento.getPagoPA().getTipoVersamento().getCodifica());
			}
			if(evento.getPagoPA().getModelloPagamento() != null) {
				datiPagoPA.setModelloPagamento(evento.getPagoPA().getModelloPagamento().getCodifica() +"");
			}

			datiPagoPA.setIdFlusso(evento.getPagoPA().getCodFlusso());
			if(evento.getPagoPA().getIdTracciato() != null)
				datiPagoPA.setIdTracciato(new BigDecimal(evento.getPagoPA().getIdTracciato()));
			datiPagoPA.setIdIncasso(evento.getPagoPA().getTrn());
			datiPagoPA.setSct(evento.getPagoPA().getSct());

		}
		return datiPagoPA;
	}
}

