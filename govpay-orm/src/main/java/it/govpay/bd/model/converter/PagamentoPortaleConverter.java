/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2017 Link.it srl (http://www.link.it).
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
package it.govpay.bd.model.converter;

import java.util.ArrayList;
import java.util.List;

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.model.PagamentoPortale;
import it.govpay.bd.model.PagamentoPortale.STATO;


public class PagamentoPortaleConverter {

	public static List<PagamentoPortale> toDTO(List<it.govpay.orm.PagamentoPortale> singoliPagamenti) throws ServiceException {
		List<PagamentoPortale> dto = new ArrayList<PagamentoPortale>();
		for(it.govpay.orm.PagamentoPortale vo : singoliPagamenti) {
			dto.add(toDTO(vo));
		}
		return dto;
	}

	public static PagamentoPortale toDTO(it.govpay.orm.PagamentoPortale vo) throws ServiceException {
		PagamentoPortale dto = new PagamentoPortale();

		dto.setId(vo.getId());
		dto.setCodPortale(vo.getCodPortale());
		dto.setCodCanale(vo.getCodCanale());
		dto.setIdSessione(vo.getIdSessione());
		dto.setIdSessionePortale(vo.getIdSessionePortale());
		dto.setIdSessionePsp(vo.getIdSessionePsp());
		dto.setStato(STATO.valueOf(vo.getStato()));
		dto.setPspRedirectUrl(vo.getPspRedirectURL());
		dto.setPspEsito(vo.getPspEsito());
		dto.setJsonRequest(vo.getJsonRequest());
		dto.setWispIdDominio(vo.getWispIdDominio());
		dto.setWispKeyPA(vo.getWispKeyPA());
		dto.setWispKeyWisp(vo.getWispKeyWisp());
		dto.setWispHtml(vo.getWispHtml());
		dto.setDataRichiesta(vo.getDataRichiesta());
		dto.setUrlRitorno(vo.getUrlRitorno());
		dto.setCodPsp(vo.getCodPsp());
		dto.setTipoVersamento(vo.getTipoVersamento());
		dto.setNome(vo.getNome());

		return dto;
	}

	public static it.govpay.orm.PagamentoPortale toVO(PagamentoPortale dto) {
		it.govpay.orm.PagamentoPortale vo = new it.govpay.orm.PagamentoPortale();

		vo.setId(dto.getId());
		vo.setCodPortale(dto.getCodPortale());
		vo.setCodCanale(dto.getCodCanale());
		vo.setIdSessione(dto.getIdSessione());
		vo.setIdSessionePortale(dto.getIdSessionePortale());
		vo.setIdSessionePsp(dto.getIdSessionePsp());
		vo.setStato(dto.getStato().toString());
		vo.setPspRedirectURL(dto.getPspRedirectUrl());
		vo.setPspEsito(dto.getPspEsito());
		vo.setJsonRequest(dto.getJsonRequest());
		vo.setWispIdDominio(dto.getWispIdDominio());
		vo.setWispKeyPA(dto.getWispKeyPA());
		vo.setWispKeyWisp(dto.getWispKeyWisp());
		vo.setWispHtml(dto.getWispHtml());
		vo.setDataRichiesta(dto.getDataRichiesta());
		vo.setUrlRitorno(dto.getUrlRitorno());
		vo.setCodPsp(dto.getCodPsp());
		vo.setTipoVersamento(dto.getTipoVersamento());
		vo.setNome(dto.getNome());

		return vo;
	}

}
