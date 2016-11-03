/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2016 Link.it srl (http://www.link.it).
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
package it.govpay.bd.model.converter;

import it.govpay.bd.model.Tributo;
import it.govpay.model.Tributo.TipoContabilta;
import it.govpay.orm.IdDominio;
import it.govpay.orm.IdIbanAccredito;
import it.govpay.orm.TipoTributo;

import java.util.ArrayList;
import java.util.List;

import org.openspcoop2.generic_project.exception.ServiceException;

public class TributoConverter {

	public static List<Tributo> toDTOList(List<it.govpay.orm.Tributo> lstVO) throws ServiceException {
		List<Tributo> lst = new ArrayList<Tributo>();
		if(lstVO != null && !lstVO.isEmpty()) {
			for(it.govpay.orm.Tributo vo: lstVO) {
				lst.add(toDTO(vo));
			}
		}
		return lst;
	}

	public static Tributo toDTO(it.govpay.orm.Tributo vo) throws ServiceException {
		Tributo dto = new Tributo();
		dto.setId(vo.getId());
		dto.setIdDominio(vo.getIdDominio().getId());
		dto.setCodTributo(vo.getTipoTributo().getCodTributo());
		dto.setAbilitato(vo.getAbilitato());
		dto.setDescrizione(vo.getTipoTributo().getDescrizione());
		dto.setIdTipoTributo(vo.getTipoTributo().getId()); 
		if(vo.getIdIbanAccredito() != null)
			dto.setIdIbanAccredito(vo.getIdIbanAccredito().getId());
		if(vo.getTipoContabilita() != null)
			dto.setTipoContabilitaDefault(TipoContabilta.toEnum(vo.getTipoContabilita()));
		dto.setCodContabilitaDefault(vo.getCodiceContabilita());
		dto.setCodTributoIuvDefault(vo.getCodiceTributIuv());
		return dto;
	}

	public static it.govpay.orm.Tributo toVO(Tributo dto) {
		it.govpay.orm.Tributo vo = new it.govpay.orm.Tributo();
		vo.setId(dto.getId());
		vo.setAbilitato(dto.isAbilitato());
		
		if(dto.getTipoContabilitaCustom() != null)
			vo.setTipoContabilita(dto.getTipoContabilitaCustom().getCodifica());
		vo.setCodiceContabilita(dto.getCodContabilitaCustom());
		vo.setCodTributoIuv(dto.getCodTributoIuvCustom());
		
		TipoTributo tipoTributo = new TipoTributo();
		tipoTributo.setId(dto.getIdTipoTributo());
		tipoTributo.setCodTributo(dto.getCodTributo());
		tipoTributo.setDescrizione(dto.getDescrizione());
		tipoTributo.setTipoContabilita(dto.getTipoContabilitaDefault().getCodifica());
		tipoTributo.setCodiceContabilita(dto.getCodContabilitaDefault());
		tipoTributo.setCodTributoIuv(dto.getCodTributoIuvDefault());
		
		vo.setTipoTributo(tipoTributo);

		IdDominio idDominio = new IdDominio();
		idDominio.setId(dto.getIdDominio());
		vo.setIdDominio(idDominio);
		
		if(dto.getIdIbanAccredito() != null) {
			IdIbanAccredito idIbanAccredito = new IdIbanAccredito();
			idIbanAccredito.setId(dto.getIdIbanAccredito());
			vo.setIdIbanAccredito(idIbanAccredito);
		}
		
		return vo;
	}


}
