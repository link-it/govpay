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

import it.govpay.bd.model.Stazione;
import it.govpay.orm.IdIntermediario;

import java.util.ArrayList;
import java.util.List;

public class StazioneConverter {

	public static List<Stazione> toDTOList(List<it.govpay.orm.Stazione> stazioneLst) {
		List<Stazione> lstDTO = new ArrayList<Stazione>();
		if(stazioneLst != null && !stazioneLst.isEmpty()) {
			for(it.govpay.orm.Stazione stazione: stazioneLst){
				lstDTO.add(toDTO(stazione));
			}
		}
		return lstDTO;
	}

	public static Stazione toDTO(it.govpay.orm.Stazione vo) {
		Stazione dto = new Stazione();
		dto.setId(vo.getId());
		dto.setCodStazione(vo.getCodStazione());
		dto.setPassword(vo.getPassword());
		dto.setIdIntermediario(vo.getIdIntermediario().getId());
		dto.setAbilitato(vo.isAbilitato());
		dto.setApplicationCode(vo.getApplicationCode());
		dto.setNdpStato(vo.getNdpStato());
		dto.setNdpOperazione(vo.getNdpOperazione());
		dto.setNdpDescrizione(vo.getNdpDescrizione());

		return dto;
	}

	public static it.govpay.orm.Stazione toVO(Stazione dto) {
		it.govpay.orm.Stazione vo = new it.govpay.orm.Stazione();
		vo.setId(dto.getId());
		vo.setCodStazione(dto.getCodStazione());
		vo.setPassword(dto.getPassword());
		IdIntermediario idInterm = new IdIntermediario();
		idInterm.setId(dto.getIdIntermediario());
		vo.setIdIntermediario(idInterm);
		vo.setAbilitato(dto.isAbilitato());
		vo.setApplicationCode(dto.getApplicationCode());
		vo.setNdpStato(dto.getNdpStato());
		vo.setNdpOperazione(dto.getNdpOperazione());
		vo.setNdpDescrizione(dto.getNdpDescrizione());
		return vo;
	}

}
