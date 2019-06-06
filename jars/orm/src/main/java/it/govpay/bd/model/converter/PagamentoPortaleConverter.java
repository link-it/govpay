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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.model.PagamentoPortale;
import it.govpay.bd.model.PagamentoPortale.CODICE_STATO;
import it.govpay.bd.model.PagamentoPortale.STATO;
import it.govpay.model.Utenza.TIPO_UTENZA;
import it.govpay.orm.IdApplicazione;


public class PagamentoPortaleConverter {

	public static List<PagamentoPortale> toDTO(List<it.govpay.orm.PagamentoPortale> singoliPagamenti) throws ServiceException {
		List<PagamentoPortale> dto = new ArrayList<>();
		for(it.govpay.orm.PagamentoPortale vo : singoliPagamenti) {
			dto.add(toDTO(vo));
		}
		return dto;
	}

	public static PagamentoPortale toDTO(it.govpay.orm.PagamentoPortale vo) throws ServiceException {
		PagamentoPortale dto = new PagamentoPortale();

		dto.setId(vo.getId());
		dto.setPrincipal(vo.getPrincipal());
		dto.setTipoUtenza(TIPO_UTENZA.valueOf(vo.getTipoUtenza()));
		dto.setVersanteIdentificativo(vo.getVersanteIdentificativo());
		dto.setCodCanale(vo.getCodCanale());
		dto.setIdSessione(vo.getIdSessione());
		dto.setIdSessionePortale(vo.getIdSessionePortale());
		dto.setIdSessionePsp(vo.getIdSessionePsp());
		dto.setStato(STATO.valueOf(vo.getStato()));
		dto.setCodiceStato(CODICE_STATO.valueOf(vo.getCodiceStato()));
		dto.setDescrizioneStato(vo.getDescrizioneStato());
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
		dto.setImporto(BigDecimal.valueOf(vo.getImporto()));
		dto.setMultiBeneficiario(vo.getMultiBeneficiario()); 
		dto.setAck(vo.getAck());
		dto.setTipo(vo.getTipo());
		
		if(vo.getIdApplicazione() != null)
			dto.setIdApplicazione(vo.getIdApplicazione().getId());

		return dto;
	}

	public static it.govpay.orm.PagamentoPortale toVO(PagamentoPortale dto) throws ServiceException {
		it.govpay.orm.PagamentoPortale vo = new it.govpay.orm.PagamentoPortale();

		vo.setId(dto.getId());
		vo.setPrincipal(dto.getPrincipal());
		vo.setTipoUtenza(dto.getTipoUtenza().toString());
		vo.setVersanteIdentificativo(dto.getVersanteIdentificativo());
		vo.setCodCanale(dto.getCodCanale());
		vo.setIdSessione(dto.getIdSessione());
		vo.setIdSessionePortale(dto.getIdSessionePortale());
		vo.setIdSessionePsp(dto.getIdSessionePsp());
		vo.setStato(dto.getStato().toString());
		vo.setCodiceStato(dto.getCodiceStato().toString());
		vo.setDescrizioneStato(dto.getDescrizioneStato());

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
		vo.setImporto(dto.getImporto().doubleValue());
		vo.setMultiBeneficiario(dto.getMultiBeneficiario()); 
		vo.setAck(dto.isAck());
		vo.setTipo(dto.getTipo());
		
		if(dto.getIdApplicazione() != null) {
			IdApplicazione idApplicazione = new IdApplicazione();
			idApplicazione.setId(dto.getIdApplicazione());
			vo.setIdApplicazione(idApplicazione);
		}

		return vo;
	}

}
