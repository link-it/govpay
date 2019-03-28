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

import it.govpay.bd.model.TipoVersamentoDominio;
import it.govpay.model.TipoVersamento.Tipo;
import it.govpay.orm.IdDominio;
import it.govpay.orm.TipoVersamento;

public class TipoVersamentoDominioConverter {

	public static List<TipoVersamentoDominio> toDTOList(List<it.govpay.orm.TipoVersamentoDominio> lstVO) throws ServiceException {
		List<TipoVersamentoDominio> lst = new ArrayList<>();
		if(lstVO != null && !lstVO.isEmpty()) {
			for(it.govpay.orm.TipoVersamentoDominio vo: lstVO) {
				lst.add(toDTO(vo));
			}
		}
		return lst;
	}

	public static TipoVersamentoDominio toDTO(it.govpay.orm.TipoVersamentoDominio vo) throws ServiceException {
		TipoVersamentoDominio dto = new TipoVersamentoDominio();
		dto.setId(vo.getId());
		dto.setIdDominio(vo.getIdDominio().getId());
		
		dto.setAbilitato(vo.getAbilitato());
		dto.setCodificaIuvCustom(vo.getCodificaIuv());
		if(vo.getTipo() != null)
			dto.setTipoCustom(Tipo.toEnum(vo.getTipo()));
		dto.setPagaTerziCustom(vo.getPagaTerzi());
		
		dto.setCodTipoVersamento(vo.getTipoVersamento().getCodTipoVersamento());
		dto.setDescrizione(vo.getTipoVersamento().getDescrizione());
		dto.setCodificaIuvDefault(vo.getTipoVersamento().getCodificaIuv());
		if(vo.getTipoVersamento().getTipo() != null)
			dto.setTipoDefault(Tipo.toEnum(vo.getTipoVersamento().getTipo()));
		dto.setPagaTerziDefault(vo.getTipoVersamento().isPagaTerzi());
		dto.setAbilitatoDefault(vo.getTipoVersamento().isAbilitato());
		
		return dto;
	}

	public static it.govpay.orm.TipoVersamentoDominio toVO(TipoVersamentoDominio dto) {
		it.govpay.orm.TipoVersamentoDominio vo = new it.govpay.orm.TipoVersamentoDominio();
		vo.setId(dto.getId());
		
		TipoVersamento tipoVersamento = new TipoVersamento();
		tipoVersamento.setId(dto.getIdTipoVersamento());
		tipoVersamento.setCodTipoVersamento(dto.getCodTipoVersamento());
		tipoVersamento.setDescrizione(dto.getDescrizione());
		tipoVersamento.setCodificaIuv(dto.getCodificaIuvDefault());
		if(dto.getTipoDefault() != null)
			tipoVersamento.setTipo(dto.getTipoDefault().getCodifica());
		tipoVersamento.setPagaTerzi(dto.getPagaTerziDefault());
		tipoVersamento.setAbilitato(dto.isAbilitatoDefault());
		
		vo.setCodificaIuv(dto.getCodificaIuvCustom());
		if(dto.getTipoCustom() != null)
			vo.setTipo(dto.getTipoCustom().getCodifica());
		vo.setPagaTerzi(dto.getPagaTerziCustom());
		vo.setTipoVersamento(tipoVersamento);
		vo.setAbilitato(dto.isAbilitato());
		
		IdDominio idDominio = new IdDominio();
		idDominio.setId(dto.getIdDominio());
		vo.setIdDominio(idDominio);
		
		return vo;
	}
}
