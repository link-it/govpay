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

import it.govpay.bd.model.Psp;
import it.govpay.bd.model.Psp.Canale;
import it.govpay.bd.model.Psp.ModelloPagamento;
import it.govpay.bd.model.Rpt.TipoVersamento;
import it.govpay.orm.IdPsp;

import java.util.ArrayList;
import java.util.List;

import org.openspcoop2.generic_project.exception.ServiceException;

public class CanaleConverter {

	public static List<Canale> toDTO(List<it.govpay.orm.Canale> lstVO, Psp pspDto) throws ServiceException {
		List<Canale> lst = new ArrayList<Canale>();
		if(lstVO != null && !lstVO.isEmpty()) {
			for(it.govpay.orm.Canale vo: lstVO) {
				lst.add(toDTO(vo, pspDto));
			}
		}

		return lst;
	}

	public static Canale toDTO(it.govpay.orm.Canale vo, Psp pspDto) throws ServiceException {
		Canale dto = new Psp().new Canale();
		dto.setId(vo.getId());
		dto.setCodCanale(vo.getCodCanale());
		dto.setCodIntermediario(vo.getCodIntermediario());
		dto.setCondizioni(vo.getCondizioni());
		dto.setDescrizione(vo.getDescrizione());
		dto.setDisponibilita(vo.getDisponibilita());
		dto.setModelloPagamento(ModelloPagamento.toEnum(vo.getModelloPagamento()));
		dto.setTipoVersamento(TipoVersamento.toEnum(vo.getTipoVersamento()));
		dto.setUrlInfo(vo.getUrlInfo());
		dto.setAbilitato(vo.isAbilitato());
		dto.setPsp(pspDto);
		return dto;
	}

	public static List<it.govpay.orm.Canale> toVO(List<Canale> lstDTO) {
		List<it.govpay.orm.Canale> lst = new ArrayList<it.govpay.orm.Canale>();
		if(lstDTO != null && !lstDTO.isEmpty()) {
			for(Canale vo: lstDTO) {
				lst.add(toVO(vo));
			}
		}

		return lst;
	}

	public static it.govpay.orm.Canale toVO(Canale dto) {
		it.govpay.orm.Canale vo = new it.govpay.orm.Canale();
		vo.setId(dto.getId());
		IdPsp idPsp = new IdPsp();
		idPsp.setId(dto.getPsp().getId());
		vo.setIdPsp(idPsp);
		vo.setCodCanale(dto.getCodCanale());
		vo.setCodIntermediario(dto.getCodIntermediario());
		vo.setCondizioni(dto.getCondizioni());
		vo.setDescrizione(dto.getDescrizione());
		vo.setDisponibilita(dto.getDisponibilita());
		vo.setModelloPagamento(dto.getModelloPagamento().getCodifica());
		vo.setTipoVersamento(dto.getTipoVersamento().getCodifica());
		vo.setUrlInfo(dto.getUrlInfo());
		vo.setAbilitato(dto.isAbilitato());
		return vo;
	}


}
