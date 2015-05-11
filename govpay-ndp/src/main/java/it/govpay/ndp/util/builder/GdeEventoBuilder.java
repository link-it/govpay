/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2015 Link.it srl (http://www.link.it).
 * Copyright (c) 2014-2015 TAS S.p.A. (http://www.tasgroup.it).
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
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
package it.govpay.ndp.util.builder;

import java.sql.Timestamp;

import it.govpay.ndp.model.EventiInterfacciaModel;
import it.govpay.ndp.model.EventiInterfacciaModel.Categoria;
import it.govpay.ndp.model.EventiInterfacciaModel.Componente;
import it.govpay.ndp.model.EventiInterfacciaModel.Evento;
import it.govpay.ndp.model.EventiInterfacciaModel.SottoTipo;
import it.govpay.orm.gde.GdeEvento;

public class GdeEventoBuilder {

	public static GdeEvento toGdeEvento(Evento eventoModel) {

		if (eventoModel == null) {
			return null;
		} else {

			GdeEvento gdeEvento = new GdeEvento();

			gdeEvento.setCanalePagamento(eventoModel.getCanalePagamento());
			gdeEvento.setCategoriaEvento(eventoModel.getCategoria().name());
			gdeEvento.setCodiceContestoPagamento(eventoModel.getCcp());
			gdeEvento.setComponente(eventoModel.getComponente().name());
			gdeEvento.setDtEvento(new Timestamp(eventoModel.getData().getTime()));
			gdeEvento.setEsito(eventoModel.getEsito());
			gdeEvento.setIdDominio(eventoModel.getDominio());
			gdeEvento.setIdEgov(eventoModel.getIdEgov());
			gdeEvento.setIdErogatore(eventoModel.getErogatore());
			gdeEvento.setIdFruitore(eventoModel.getFruitore());
			gdeEvento.setIdPrestatoreserviziPagamento(eventoModel.getPsp());
			gdeEvento.setIdStazioneIntermediarioPa(eventoModel.getStazioneIntermediarioPA());
			gdeEvento.setIdUnivocoVersamento(eventoModel.getIuv());
			gdeEvento.setParamSpecificiInterfaccia(eventoModel.getParametri());
			gdeEvento.setSottoTipoEvento(eventoModel.getSottoTipo().name());
			gdeEvento.setTipoEvento(eventoModel.getTipo());
			gdeEvento.setTipoVersamento(eventoModel.getTipoVersamento());

			return gdeEvento;

		}
	}

	public static Evento fromGdeEvento(GdeEvento gdeEvento) {

		if (gdeEvento == null) {
			return null;
		} else {

			EventiInterfacciaModel.Evento eventoModel = new EventiInterfacciaModel().new Evento();

			eventoModel.setCanalePagamento(gdeEvento.getCanalePagamento());
			eventoModel.setCategoria(Categoria.valueOf(gdeEvento.getCategoriaEvento()));
			eventoModel.setCcp(gdeEvento.getCodiceContestoPagamento());
			eventoModel.setComponente(Componente.valueOf(gdeEvento.getComponente()));
			eventoModel.setData(gdeEvento.getDtEvento());
			eventoModel.setEsito(gdeEvento.getEsito());
			eventoModel.setDominio(gdeEvento.getIdDominio());
			eventoModel.setIdEgov(gdeEvento.getIdEgov());
			eventoModel.setErogatore(gdeEvento.getIdErogatore());
			eventoModel.setFruitore(gdeEvento.getIdFruitore());
			eventoModel.setPsp(gdeEvento.getIdPrestatoreserviziPagamento());
			eventoModel.setStazioneIntermediarioPA(gdeEvento.getIdStazioneIntermediarioPa());
			eventoModel.setIuv(gdeEvento.getIdUnivocoVersamento());
			eventoModel.setParametri(gdeEvento.getParamSpecificiInterfaccia());
			eventoModel.setSottoTipo(SottoTipo.valueOf(gdeEvento.getSottoTipoEvento()));
			eventoModel.setTipo(gdeEvento.getTipoEvento());
			eventoModel.setTipoVersamento(gdeEvento.getTipoVersamento());
			/*AGGIUNTO IDLONG*/
			eventoModel.setId(gdeEvento.getId());

			return eventoModel;

		}
	}
}
