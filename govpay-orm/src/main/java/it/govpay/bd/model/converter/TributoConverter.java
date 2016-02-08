/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2015 Link.it srl (http://www.link.it).
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
import it.govpay.bd.model.Tributo.TipoContabilta;
import it.govpay.orm.IdEnte;
import it.govpay.orm.IdIbanAccredito;

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
		dto.setIdEnte(vo.getIdEnte().getId());
		dto.setCodTributo(vo.getCodTributo());
		dto.setAbilitato(vo.getAbilitato());
		dto.setDescrizione(vo.getDescrizione());
		dto.setIbanAccredito(vo.getIbanAccredito().getId());
		dto.setTipoContabilita(TipoContabilta.toEnum(vo.getTipoContabilita()));
		dto.setCodContabilita(vo.getCodiceContabilita());

		return dto;
	}

	public static it.govpay.orm.Tributo toVO(Tributo dto) {
		it.govpay.orm.Tributo vo = new it.govpay.orm.Tributo();

		vo.setId(dto.getId());
		IdEnte idEnte = new IdEnte();
		idEnte.setId(dto.getIdEnte());
		vo.setIdEnte(idEnte);
		vo.setCodTributo(dto.getCodTributo());
		vo.setAbilitato(dto.isAbilitato());
		vo.setDescrizione(dto.getDescrizione());
		IdIbanAccredito idIbanAccredito = new IdIbanAccredito();
		idIbanAccredito.setId(dto.getIbanAccredito());
		vo.setIbanAccredito(idIbanAccredito);
		vo.setTipoContabilita(dto.getTipoContabilita().getCodifica());
		vo.setCodiceContabilita(dto.getCodContabilita());

		return vo;

	}


}
